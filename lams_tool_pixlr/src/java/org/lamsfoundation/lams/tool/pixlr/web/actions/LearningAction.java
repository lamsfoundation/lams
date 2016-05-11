/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.pixlr.web.actions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.pixlr.dto.PixlrDTO;
import org.lamsfoundation.lams.tool.pixlr.dto.PixlrUserDTO;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrConfigItem;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrSession;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrUser;
import org.lamsfoundation.lams.tool.pixlr.service.IPixlrService;
import org.lamsfoundation.lams.tool.pixlr.service.PixlrServiceProxy;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrException;
import org.lamsfoundation.lams.tool.pixlr.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;
    private static final String PIXLR_UTL = "http://www.pixlr.com/editor/";

    private IPixlrService pixlrService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm learningForm = (LearningForm) form;

	// 'toolSessionID' and 'mode' paramters are expected to be present.
	// TODO need to catch exceptions and handle errors.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, MODE_OPTIONAL);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	String redoQuestion = request.getParameter("redoQuestion");
	boolean isRedo = false;
	if (redoQuestion != null && redoQuestion.equals("true")) {
	    isRedo = true;
	}

	// set up pixlrService
	if (pixlrService == null) {
	    pixlrService = PixlrServiceProxy.getPixlrService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	PixlrSession pixlrSession = pixlrService.getSessionBySessionId(toolSessionID);
	if (pixlrSession == null) {
	    throw new PixlrException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Pixlr pixlr = pixlrSession.getPixlr();

	// check defineLater
	if (pixlr.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and PixlrDTO
	request.setAttribute("mode", mode.toString());
	request.setAttribute("toolSessionID", toolSessionID);
	learningForm.setToolSessionID(toolSessionID);

	PixlrDTO pixlrDTO = new PixlrDTO(pixlr);
	request.setAttribute("pixlrDTO", pixlrDTO);

	// Set the content in use flag.
	if (!pixlr.isContentInUse()) {
	    pixlr.setContentInUse(new Boolean(true));
	    pixlrService.saveOrUpdatePixlr(pixlr);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

	// get the user
	PixlrUser pixlrUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    pixlrUser = pixlrService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    pixlrUser = getCurrentUser(toolSessionID);
	}

	// return to the viewAll images page if the user has already clicked it
	if (pixlrUser.isFinishedActivity() && pixlr.isAllowViewOthersImages() && !isRedo) {
	    return viewAllImages(mapping, learningForm, request, response);
	}

	// set up the user dto
	PixlrUserDTO pixlrUserDTO = new PixlrUserDTO(pixlrUser);
	if (pixlrUser.isFinishedActivity()) {
	    // get the notebook entry.
	    NotebookEntry notebookEntry = pixlrService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    PixlrConstants.TOOL_SIGNATURE, pixlrUser.getUserId().intValue());
	    if (notebookEntry != null) {
		pixlrUserDTO.notebookEntry = notebookEntry.getEntry();
		pixlrUserDTO.setFinishedReflection(true);
	    }
	}
	request.setAttribute("pixlrUserDTO", pixlrUserDTO);

	String returnURL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/lapixl10/learning.do?";
	returnURL += "dispatch=updatePixlrImage";
	returnURL += "&toolSessionID=" + pixlrSession.getSessionId();

	String url = PIXLR_UTL + "?";
	url += "&title=" + URLEncoder.encode(pixlr.getTitle(), "UTF8");
	url += "&referrer=LAMS";
	url += "&loc=" + getPixlrLocale();

	String currentImageURL;

	if (pixlrUser.getImageFileName() != null && !pixlrUser.getImageFileName().equals("")) {
	    currentImageURL = PixlrConstants.LAMS_WWW_PIXLR_FOLDER_URL + pixlrUser.getImageFileName();
	    returnURL += "&existingImage=" + URLEncoder.encode(pixlrUser.getImageFileName(), "UTF8");
	} else {
	    returnURL += "&existingImage=";
	    currentImageURL = PixlrConstants.LAMS_WWW_PIXLR_FOLDER_URL + pixlr.getImageFileName();
	}

	request.setAttribute("currentImageURL", currentImageURL);
	request.setAttribute("pixlrURL", URLEncoder.encode(url, "UTF-8"));
	request.setAttribute("returnURL", returnURL);

	// set readOnly flag.
	if (mode.equals(ToolAccessMode.TEACHER) || (pixlr.isLockOnFinished() && pixlrUser.isFinishedActivity())) {
	    request.setAttribute("contentEditable", false);
	} else {
	    request.setAttribute("contentEditable", true);
	}
	request.setAttribute("finishedActivity", pixlrUser.isFinishedActivity());
	return mapping.findForward("pixlr");
    }

    public ActionForward updatePixlrImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Boolean success;

	log.debug("Saving image from pixlr");

	// set up pixlrService
	if (pixlrService == null) {
	    pixlrService = PixlrServiceProxy.getPixlrService(this.getServlet().getServletContext());
	}

	String imageURL = WebUtil.readStrParam(request, "image");
	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	PixlrUser pixlrUser = getCurrentUser(toolSessionID);

	String imageName = WebUtil.readStrParam(request, "existingImage", true);

	if (imageName == null || imageName.equals("")) {
	    imageName = FileUtil.generateUniqueContentFolderID() + ".jpg";
	}

	try {
	    InputStream is = LearningAction.getResponseInputStreamFromExternalServer(imageURL,
		    new HashMap<String, String>());

	    String realBaseDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator
		    + FileUtil.LAMS_WWW_DIR + File.separator + "images" + File.separator + "pixlr";

	    File imageFile = new File(realBaseDir + File.separator + imageName);

	    FileOutputStream out = new FileOutputStream(imageFile);

	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = is.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }

	    pixlrUser.setImageFileName(imageName);

	    // Now save the image size
	    BufferedImage imageBufferedFile = ImageIO.read(imageFile);
	    int width = imageBufferedFile.getWidth();
	    int height = imageBufferedFile.getHeight();
	    pixlrUser.setImageHeight(new Long(height));
	    pixlrUser.setImageWidth(new Long(width));
	    pixlrService.saveOrUpdatePixlrUser(pixlrUser);
	    success = true;
	} catch (Exception e) {
	    log.error("Failed to copy pixlr image from pixlr server with URL: " + imageURL, e);
	    success = false;
	}

	request.setAttribute("success", success);
	return mapping.findForward("success");
    }

    private PixlrUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	PixlrUser pixlrUser = pixlrService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (pixlrUser == null) {
	    PixlrSession pixlrSession = pixlrService.getSessionBySessionId(toolSessionId);
	    pixlrUser = pixlrService.createPixlrUser(user, pixlrSession);
	}

	return pixlrUser;
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	PixlrUser pixlrUser = getCurrentUser(toolSessionID);

	if (pixlrUser != null) {
	    pixlrUser.setFinishedActivity(true);
	    pixlrService.saveOrUpdatePixlrUser(pixlrUser);
	} else {
	    log.error("finishActivity(): couldn't find PixlrUser with id: " + pixlrUser.getUserId()
		    + "and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = PixlrServiceProxy
		.getPixlrSessionManager(getServlet().getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, pixlrUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new PixlrException(e);
	} catch (ToolException e) {
	    throw new PixlrException(e);
	} catch (IOException e) {
	    throw new PixlrException(e);
	}

	return null; // TODO need to return proper page.
    }

    public static InputStream getResponseInputStreamFromExternalServer(String urlStr, HashMap<String, String> params)
	    throws ToolException, IOException {
	if (!urlStr.endsWith("?")) {
	    urlStr += "?";
	}

	for (Entry<String, String> entry : params.entrySet()) {
	    urlStr += "&" + entry.getKey() + "=" + entry.getValue();
	}

	log.debug("Making request to external servlet: " + urlStr);

	URL url = new URL(urlStr);
	URLConnection conn = url.openConnection();
	if (!(conn instanceof HttpURLConnection)) {
	    log.error("Fail to connect to external server though url:  " + urlStr);
	    throw new ToolException("Fail to connect to external server though url:  " + urlStr);
	}

	HttpURLConnection httpConn = (HttpURLConnection) conn;
	if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	    log.error("Fail to fetch data from external server, response code:  " + httpConn.getResponseCode()
		    + " Url: " + urlStr);
	    throw new ToolException("Fail to fetch data from external server, response code:  "
		    + httpConn.getResponseCode() + " Url: " + urlStr);
	}

	InputStream is = url.openConnection().getInputStream();
	if (is == null) {
	    log.error("Fail to fetch data from external server, return InputStream null:  " + urlStr);
	    throw new ToolException("Fail to fetch data from external server, return inputStream null:  " + urlStr);
	}

	return is;
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	PixlrUser pixlrUser = this.getCurrentUser(lrnForm.getToolSessionID());
	PixlrDTO pixlrDTO = new PixlrDTO(pixlrUser.getPixlrSession().getPixlr());

	request.setAttribute("pixlrDTO", pixlrDTO);

	NotebookEntry notebookEntry = pixlrService.getEntry(pixlrUser.getPixlrSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, PixlrConstants.TOOL_SIGNATURE, pixlrUser.getUserId().intValue());

	if (notebookEntry != null) {
	    lrnForm.setEntryText(notebookEntry.getEntry());
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(pixlrUser.getPixlrSession().getSessionId(), request,
		getServlet().getServletContext());

	return mapping.findForward("notebook");
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	PixlrUser pixlrUser = this.getCurrentUser(lrnForm.getToolSessionID());
	Long toolSessionID = pixlrUser.getPixlrSession().getSessionId();
	Integer userID = pixlrUser.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = pixlrService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		PixlrConstants.TOOL_SIGNATURE, userID);

	if (entry == null) {
	    // create new entry
	    pixlrService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    PixlrConstants.TOOL_SIGNATURE, userID, lrnForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(lrnForm.getEntryText());
	    entry.setLastModified(new Date());
	    pixlrService.updateEntry(entry);
	}

	return finishActivity(mapping, form, request, response);
    }

    public ActionForward viewAllImages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, MODE_OPTIONAL);

	// set up pixlrService
	if (pixlrService == null) {
	    pixlrService = PixlrServiceProxy.getPixlrService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	PixlrSession pixlrSession = pixlrService.getSessionBySessionId(toolSessionID);
	if (pixlrSession == null) {
	    throw new PixlrException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Pixlr pixlr = pixlrSession.getPixlr();

	// get the user
	PixlrUser pixlrUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    pixlrUser = pixlrService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    pixlrUser = getCurrentUser(toolSessionID);
	}

	// set completed
	pixlrUser.setFinishedActivity(true);
	pixlrService.saveOrUpdatePixlrUser(pixlrUser);

	// set up the of images learner set
	Set<PixlrUserDTO> learnerSet = new HashSet<PixlrUserDTO>();
	for (PixlrUser learner : pixlrSession.getPixlrUsers()) {
	    if (learner.getImageFileName() != null && !learner.getImageFileName().equals("")
		    && !learner.getImageFileName().equals(pixlr.getImageFileName())) {

		PixlrUserDTO userDTO = new PixlrUserDTO(learner);
		if (userDTO.getImageFileName() == null || userDTO.getImageFileName().equals("")) {
		    userDTO.setImageFileName(pixlr.getImageFileName());
		    userDTO.setImageHeight(pixlr.getImageHeight());
		    userDTO.setImageWidth(pixlr.getImageWidth());
		}
		learnerSet.add(userDTO);
	    }
	}
	request.setAttribute("learnerDTOs", learnerSet);
	request.setAttribute("pixlrDTO", new PixlrDTO(pixlr));
	request.setAttribute("mode", mode);
	request.setAttribute("pixlrImageFolderURL", PixlrConstants.LAMS_WWW_PIXLR_FOLDER_URL);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

	return mapping.findForward("viewAll");
    }

    /**
     * Work out if this user's language is supported by pixlr
     *
     * @return
     */
    private String getPixlrLocale() {
	String locale = "en";

	String languagesCSV = pixlrService.getConfigItem(PixlrConfigItem.KEY_LANGUAGE_CSV) != null
		? pixlrService.getConfigItem(PixlrConfigItem.KEY_LANGUAGE_CSV).getConfigValue() : null;

	if (languagesCSV != null && !languagesCSV.equals("")) {
	    UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	    String[] languages = languagesCSV.split(",");

	    for (int i = 0; i < languages.length; i++) {
		if (languages[i].equals(user.getFckLanguageMapping())) {
		    locale = languages[i];
		    break;
		}
	    }

	}

	return locale;
    }
}

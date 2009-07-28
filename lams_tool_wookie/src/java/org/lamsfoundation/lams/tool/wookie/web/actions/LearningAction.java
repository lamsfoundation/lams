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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.wookie.web.actions;

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
import java.util.Set;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.wookie.dto.WookieDTO;
import org.lamsfoundation.lams.tool.wookie.dto.WookieUserDTO;
import org.lamsfoundation.lams.tool.wookie.model.Wookie;
import org.lamsfoundation.lams.tool.wookie.model.WookieConfigItem;
import org.lamsfoundation.lams.tool.wookie.model.WookieSession;
import org.lamsfoundation.lams.tool.wookie.model.WookieUser;
import org.lamsfoundation.lams.tool.wookie.service.IWookieService;
import org.lamsfoundation.lams.tool.wookie.service.WookieServiceProxy;
import org.lamsfoundation.lams.tool.wookie.util.WookieConstants;
import org.lamsfoundation.lams.tool.wookie.util.WookieException;
import org.lamsfoundation.lams.tool.wookie.web.forms.LearningForm;
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
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm"
 * @struts.action-forward name="wookie" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 * @struts.action-forward name="success" path="/pages/learning/success.jsp"
 * @struts.action-forward name="notebook" path="/pages/learning/notebook.jsp"
 * @struts.action-forward name="viewAll" path="/pages/learning/viewAll.jsp"
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;
    private static final String PIXLR_UTL = "http://www.wookie.com/editor/";

    private IWookieService wookieService;

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

	// set up wookieService
	if (wookieService == null) {
	    wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	WookieSession wookieSession = wookieService.getSessionBySessionId(toolSessionID);
	if (wookieSession == null) {
	    throw new WookieException("Cannot retreive session with toolSessionID" + toolSessionID);
	}

	Wookie wookie = wookieSession.getWookie();

	// check defineLater
	if (wookie.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and WookieDTO
	request.setAttribute("mode", mode.toString());
	request.setAttribute("toolSessionID", toolSessionID);
	learningForm.setToolSessionID(toolSessionID);

	WookieDTO wookieDTO = new WookieDTO(wookie);
	request.setAttribute("wookieDTO", wookieDTO);

	// Set the content in use flag.
	if (!wookie.isContentInUse()) {
	    wookie.setContentInUse(new Boolean(true));
	    wookieService.saveOrUpdateWookie(wookie);
	}

	// check runOffline
	if (wookie.isRunOffline()) {
	    return mapping.findForward("runOffline");
	}

	// get the user
	WookieUser wookieUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    wookieUser = wookieService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    wookieUser = getCurrentUser(toolSessionID);
	}

	// return to the viewAll images page if the user has already clicked it
	if (wookieUser.isFinishedActivity() && wookie.isAllowViewOthersImages() && !isRedo) {
	    return viewAllImages(mapping, learningForm, request, response);
	}

	// set up the user dto
	WookieUserDTO wookieUserDTO = new WookieUserDTO(wookieUser);
	if (wookieUser.isFinishedActivity()) {
	    // get the notebook entry.
	    NotebookEntry notebookEntry = wookieService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    WookieConstants.TOOL_SIGNATURE, wookieUser.getUserId().intValue());
	    if (notebookEntry != null) {
		wookieUserDTO.notebookEntry = notebookEntry.getEntry();
		wookieUserDTO.setFinishedReflection(true);
	    }
	}
	request.setAttribute("wookieUserDTO", wookieUserDTO);

	String returnURL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/lawook10/learning.do?";
	returnURL += "dispatch=updateWookieImage";
	returnURL += "&toolSessionID=" + wookieSession.getSessionId();

	String url = PIXLR_UTL + "?";
	url += "&title=" + URLEncoder.encode(wookie.getTitle(), "UTF8");
	url += "&referrer=LAMS";
	url += "&loc=" + getWookieLocale();

	String currentImageURL;

	if (wookieUser.getImageFileName() != null && !wookieUser.getImageFileName().equals("")) {
	    currentImageURL = WookieConstants.LAMS_WWW_PIXLR_FOLDER_URL + wookieUser.getImageFileName();
	    returnURL += "&existingImage=" + URLEncoder.encode(wookieUser.getImageFileName(), "UTF8");
	} else {
	    returnURL += "&existingImage=";
	    currentImageURL = WookieConstants.LAMS_WWW_PIXLR_FOLDER_URL + wookie.getImageFileName();
	}

	request.setAttribute("currentImageURL", currentImageURL);
	request.setAttribute("wookieURL", url);
	request.setAttribute("returnURL", returnURL);

	// set readOnly flag.
	if (mode.equals(ToolAccessMode.TEACHER) || (wookie.isLockOnFinished() && wookieUser.isFinishedActivity())) {
	    request.setAttribute("contentEditable", false);
	} else {
	    request.setAttribute("contentEditable", true);
	}
	request.setAttribute("finishedActivity", wookieUser.isFinishedActivity());
	return mapping.findForward("wookie");
    }

    public ActionForward updateWookieImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Boolean success;

	log.debug("Saving image from wookie");

	// set up wookieService
	if (wookieService == null) {
	    wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	}

	String imageURL = WebUtil.readStrParam(request, "image");
	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	WookieUser wookieUser = getCurrentUser(toolSessionID);

	String imageName = WebUtil.readStrParam(request, "existingImage", true);

	if (imageName == null || imageName.equals("")) {
	    imageName = FileUtil.generateUniqueContentFolderID() + ".jpg";
	}

	try {
	    InputStream is = getResponseInputStreamFromExternalServer(imageURL, new HashMap<String, String>());

	    String realBaseDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator
		    + FileUtil.LAMS_WWW_DIR + File.separator + "images" + File.separator + "wookie";

	    File imageFile = new File(realBaseDir + File.separator + imageName);

	    FileOutputStream out = new FileOutputStream(imageFile);

	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = is.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	    
	    wookieUser.setImageFileName(imageName);

	    // Now save the image size
	    BufferedImage imageBufferedFile = ImageIO.read(imageFile);
	    int width = imageBufferedFile.getWidth();
	    int height = imageBufferedFile.getHeight();
	    wookieUser.setImageHeight(new Long(height));
	    wookieUser.setImageWidth(new Long(width));
	    wookieService.saveOrUpdateWookieUser(wookieUser);
	    success = true;
	} catch (Exception e) {
	    log.error("Failed to copy wookie image from wookie server with URL: " + imageURL, e);
	    success = false;
	}

	request.setAttribute("success", success);
	return mapping.findForward("success");
    }

    private WookieUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	WookieUser wookieUser = wookieService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (wookieUser == null) {
	    WookieSession wookieSession = wookieService.getSessionBySessionId(toolSessionId);
	    wookieUser = wookieService.createWookieUser(user, wookieSession);
	}

	return wookieUser;
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	WookieUser wookieUser = getCurrentUser(toolSessionID);

	if (wookieUser != null) {
	    wookieUser.setFinishedActivity(true);
	    wookieService.saveOrUpdateWookieUser(wookieUser);
	} else {
	    log.error("finishActivity(): couldn't find WookieUser with tool session id: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = WookieServiceProxy.getWookieSessionManager(getServlet()
		.getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, wookieUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new WookieException(e);
	} catch (ToolException e) {
	    throw new WookieException(e);
	} catch (IOException e) {
	    throw new WookieException(e);
	}

	return null; // TODO need to return proper page.
    }

    public static InputStream getResponseInputStreamFromExternalServer(String urlStr, HashMap<String, String> params)
	    throws ToolException, IOException {
	if (!urlStr.endsWith("?"))
	    urlStr += "?";

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
	WookieUser wookieUser = this.getCurrentUser(lrnForm.getToolSessionID());
	WookieDTO wookieDTO = new WookieDTO(wookieUser.getWookieSession().getWookie());

	request.setAttribute("wookieDTO", wookieDTO);

	NotebookEntry notebookEntry = wookieService.getEntry(wookieUser.getWookieSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, WookieConstants.TOOL_SIGNATURE, wookieUser.getUserId().intValue());

	if (notebookEntry != null) {
	    lrnForm.setEntryText(notebookEntry.getEntry());
	}

	return mapping.findForward("notebook");
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	WookieUser wookieUser = this.getCurrentUser(lrnForm.getToolSessionID());
	Long toolSessionID = wookieUser.getWookieSession().getSessionId();
	Integer userID = wookieUser.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = wookieService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		WookieConstants.TOOL_SIGNATURE, userID);

	if (entry == null) {
	    // create new entry
	    wookieService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    WookieConstants.TOOL_SIGNATURE, userID, lrnForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(lrnForm.getEntryText());
	    entry.setLastModified(new Date());
	    wookieService.updateEntry(entry);
	}

	return finishActivity(mapping, form, request, response);
    }

    public ActionForward viewAllImages(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, MODE_OPTIONAL);

	// set up wookieService
	if (wookieService == null) {
	    wookieService = WookieServiceProxy.getWookieService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	WookieSession wookieSession = wookieService.getSessionBySessionId(toolSessionID);
	if (wookieSession == null) {
	    throw new WookieException("Cannot retreive session with toolSessionID" + toolSessionID);
	}

	Wookie wookie = wookieSession.getWookie();

	// get the user
	WookieUser wookieUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    wookieUser = wookieService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    wookieUser = getCurrentUser(toolSessionID);
	}

	// set completed 
	wookieUser.setFinishedActivity(true);
	wookieService.saveOrUpdateWookieUser(wookieUser);

	// set up the of images learner set
	Set<WookieUserDTO> learnerSet = new HashSet<WookieUserDTO>();
	for (WookieUser learner : wookieSession.getWookieUsers()) {
	    if (learner.getImageFileName() != null && !learner.getImageFileName().equals("")
		    && !learner.getImageFileName().equals(wookie.getImageFileName())) {

		WookieUserDTO userDTO = new WookieUserDTO(learner);
		if (userDTO.getImageFileName() == null || userDTO.getImageFileName().equals("")) {
		    userDTO.setImageFileName(wookie.getImageFileName());
		    userDTO.setImageHeight(wookie.getImageHeight());
		    userDTO.setImageWidth(wookie.getImageWidth());
		}
		learnerSet.add(userDTO);
	    }
	}
	request.setAttribute("learnerDTOs", learnerSet);
	request.setAttribute("wookieDTO", new WookieDTO(wookie));
	request.setAttribute("mode", mode);
	request.setAttribute("wookieImageFolderURL", WookieConstants.LAMS_WWW_PIXLR_FOLDER_URL);
	return mapping.findForward("viewAll");
    }

    /**
     * Work out if this user's language is supported by wookie
     * 
     * @return
     */
    private String getWookieLocale() {
	String locale = "en";

	String languagesCSV = wookieService.getConfigItem(WookieConfigItem.KEY_LANGUAGE_CSV) != null ? wookieService
		.getConfigItem(WookieConfigItem.KEY_LANGUAGE_CSV).getConfigValue() : null;

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

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

package org.lamsfoundation.lams.tool.pixlr.web.actions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrAttachment;
import org.lamsfoundation.lams.tool.pixlr.service.IPixlrService;
import org.lamsfoundation.lams.tool.pixlr.service.PixlrServiceProxy;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrException;
import org.lamsfoundation.lams.tool.pixlr.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author
 * @version
 * 
 * @struts.action path="/authoring" name="authoringForm" parameter="dispatch"
 *                scope="request" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/authoring/main"
 * @struts.action-forward name="message_page" path="tiles:/generic/message"
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IPixlrService pixlrService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";
    private static final String KEY_ONLINE_FILES = "onlineFiles";
    private static final String KEY_OFFLINE_FILES = "offlineFiles";
    private static final String KEY_UNSAVED_ONLINE_FILES = "unsavedOnlineFiles";
    private static final String KEY_UNSAVED_OFFLINE_FILES = "unsavedOfflineFiles";
    private static final String KEY_DELETED_FILES = "deletedFiles";
    
    private static final String LAMS_PIXLR_BASE_DIR = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator + FileUtil.LAMS_WWW_DIR
	+ File.separator + "images" + File.separator + "pixlr";

    /**
     * Default method when no dispatch parameter is specified. It is expected
     * that the parameter <code>toolContentID</code> will be passed in. This
     * will be used to retrieve content for this tool.
     * 
     */
    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	AuthoringForm authForm = (AuthoringForm) form;

	ToolAccessMode mode = null;
	String modeStr = WebUtil.readStrParam(request, "mode", true);
	if (modeStr != null && !modeStr.trim().equals("")) {
	    mode = WebUtil.readToolAccessModeParam(request, "mode", true);
	}

	// set up pixlrService
	if (pixlrService == null) {
	    pixlrService = PixlrServiceProxy.getPixlrService(this.getServlet().getServletContext());
	}

	// retrieving Pixlr with given toolContentID
	Pixlr pixlr = pixlrService.getPixlrByContentId(toolContentID);
	if (pixlr == null) {
	    pixlr = pixlrService.copyDefaultContent(toolContentID);
	    pixlr.setCreateDate(new Date());
	    pixlrService.saveOrUpdatePixlr(pixlr);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	if (mode != null && mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    pixlr.setDefineLater(true);
	    pixlrService.saveOrUpdatePixlr(pixlr);
	}

	String imageUrl = PixlrConstants.LAMS_WWW_PIXLR_FOLDER_URL;
	Boolean imageExists = false;
	if ((pixlr.getImageFileName() != null && !pixlr.getImageFileName().equals(""))) {
	    imageUrl += pixlr.getImageFileName();
	    if (!pixlr.getImageFileName().equals(PixlrConstants.DEFAULT_IMAGE_FILE_NAME)) {
		imageExists = true;
	    }
	} else {
	    imageUrl += PixlrConstants.DEFAULT_IMAGE_FILE_NAME;
	}
	request.setAttribute("imageURL", imageUrl);
	request.setAttribute("imageExists", imageExists);

	// Set up the authForm.
	updateAuthForm(authForm, pixlr);
	authForm.setToolContentID(toolContentID);
	if (mode != null) {
	    authForm.setMode(mode.toString());
	} else {
	    authForm.setMode("");
	}
	authForm.setContentFolderID(contentFolderID);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(pixlr, getAccessMode(request), contentFolderID, toolContentID);
	authForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(PixlrConstants.ATTR_SESSION_MAP, map);

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws PixlrException{
	// TODO need error checking.

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> map = getSessionMap(request, authForm);
	ToolAccessMode mode = (ToolAccessMode) map.get(AuthoringAction.KEY_MODE);

	// get pixlr content.
	Pixlr pixlr = pixlrService.getPixlrByContentId((Long) map.get(AuthoringAction.KEY_TOOL_CONTENT_ID));
	ActionErrors errors = new ActionErrors();;
	
	try {
	    // TODO: Need to check if this is an edit, if so, delete the old image
	    if (authForm.getExistingImageFileName().equals(PixlrConstants.DEFAULT_IMAGE_FILE_NAME)
		    || authForm.getExistingImageFileName().trim().equals("")) {
		errors = validateImageFile(authForm);

		if (!errors.isEmpty()) {
		    this.addErrors(request, errors);
		    updateAuthForm(authForm, pixlr);
		    if (mode != null) {
			authForm.setMode(mode.toString());
		    } else {
			authForm.setMode("");
		    }
		    return mapping.findForward("success");
		}
		uploadFormImage(authForm, pixlr);
	    }
	} catch (Exception e) {
	    logger.error("Problem uploading image", e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(PixlrConstants.ERROR_MSG_FILE_UPLOAD));
	    //throw new PixlrException("Problem uploading image", e);
	}
	
	// update pixlr content using form inputs.
	updatePixlr(pixlr, authForm, mode);

	

	// remove attachments marked for deletion.
	Set<PixlrAttachment> attachments = pixlr.getPixlrAttachments();
	if (attachments == null) {
	    attachments = new HashSet<PixlrAttachment>();
	}

	for (PixlrAttachment att : getAttList(AuthoringAction.KEY_DELETED_FILES, map)) {
	    // remove from db, leave in repository
	    attachments.remove(att);
	}

	// add unsaved attachments
	attachments.addAll(getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map));
	attachments.addAll(getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map));

	// set attachments in case it didn't exist
	pixlr.setPixlrAttachments(attachments);

	// set the update date
	pixlr.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	pixlr.setDefineLater(false);

	pixlrService.saveOrUpdatePixlr(pixlr);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(map.getSessionID());

	request.setAttribute(PixlrConstants.ATTR_SESSION_MAP, map);

	updateAuthForm(authForm, pixlr);
	if (mode != null) {
	    authForm.setMode(mode.toString());
	} else {
	    authForm.setMode("");
	}

	return mapping.findForward("success");
    }

    public ActionForward uploadOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return uploadFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward uploadOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return uploadFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    public ActionForward deleteOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward deleteOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    public ActionForward removeUnsavedOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return removeUnsaved(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_ONLINE, request);
    }

    public ActionForward removeUnsavedOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return removeUnsaved(mapping, (AuthoringForm) form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    /* ========== Private Methods ********** */

    private ActionForward uploadFile(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	FormFile file;
	List<PixlrAttachment> unsavedFiles;
	List<PixlrAttachment> savedFiles;
	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    file = authForm.getOfflineFile();
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map);

	    savedFiles = getAttList(AuthoringAction.KEY_OFFLINE_FILES, map);
	} else {
	    file = authForm.getOnlineFile();
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map);

	    savedFiles = getAttList(AuthoringAction.KEY_ONLINE_FILES, map);
	}

	// validate file max size
	ActionMessages errors = new ActionMessages();
	FileValidatorUtil.validateFileSize(file, true, errors);
	if (!errors.isEmpty()) {
	    request.setAttribute(PixlrConstants.ATTR_SESSION_MAP, map);
	    this.saveErrors(request, errors);
	    return mapping.findForward("success");
	}

	if (file.getFileName().length() != 0) {

	    // upload file to repository
	    PixlrAttachment newAtt = pixlrService.uploadFileToContent((Long) map
		    .get(AuthoringAction.KEY_TOOL_CONTENT_ID), file, type);

	    // Add attachment to unsavedFiles
	    // check to see if file with same name exists
	    PixlrAttachment currAtt;
	    Iterator<PixlrAttachment> iter = savedFiles.iterator();
	    while (iter.hasNext()) {
		currAtt = (PixlrAttachment) iter.next();
		if (StringUtils.equals(currAtt.getFileName(), newAtt.getFileName())
			&& StringUtils.equals(currAtt.getFileType(), newAtt.getFileType())) {
		    // move from this this list to deleted list.
		    getAttList(AuthoringAction.KEY_DELETED_FILES, map).add(currAtt);
		    iter.remove();
		    break;
		}
	    }
	    unsavedFiles.add(newAtt);

	    request.setAttribute(PixlrConstants.ATTR_SESSION_MAP, map);
	    request.setAttribute("unsavedChanges", new Boolean(true));
	}
	return mapping.findForward("success");
    }

    private ActionForward deleteFile(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	List<PixlrAttachment> fileList;
	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    fileList = getAttList(AuthoringAction.KEY_OFFLINE_FILES, map);
	} else {
	    fileList = getAttList(AuthoringAction.KEY_ONLINE_FILES, map);
	}

	Iterator<PixlrAttachment> iter = fileList.iterator();

	while (iter.hasNext()) {
	    PixlrAttachment att = (PixlrAttachment) iter.next();

	    if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
		// move to delete file list, deleted at next updateContent
		getAttList(AuthoringAction.KEY_DELETED_FILES, map).add(att);

		// remove from this list
		iter.remove();
		break;
	    }
	}

	request.setAttribute(PixlrConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("unsavedChanges", new Boolean(true));

	return mapping.findForward("success");
    }

    private ActionForward removeUnsaved(ActionMapping mapping, AuthoringForm authForm, String type,
	    HttpServletRequest request) {
	SessionMap<String, Object> map = getSessionMap(request, authForm);

	List<PixlrAttachment> unsavedFiles;

	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type)) {
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, map);
	} else {
	    unsavedFiles = getAttList(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, map);
	}

	Iterator<PixlrAttachment> iter = unsavedFiles.iterator();
	while (iter.hasNext()) {
	    PixlrAttachment att = (PixlrAttachment) iter.next();

	    if (att.getFileUuid().equals(authForm.getDeleteFileUuid())) {
		// delete from repository and list
		pixlrService.deleteFromRepository(att.getFileUuid(), att.getFileVersionId());
		iter.remove();
		break;
	    }
	}

	request.setAttribute(PixlrConstants.ATTR_SESSION_MAP, map);
	request.setAttribute("unsavedChanges", new Boolean(true));

	return mapping.findForward("success");
    }

    /**
     * Updates Pixlr content using AuthoringForm inputs.
     * 
     * @param authForm
     * @param mode
     * @return
     */
    private void updatePixlr(Pixlr pixlr, AuthoringForm authForm, ToolAccessMode mode){
	pixlr.setTitle(authForm.getTitle());
	pixlr.setInstructions(authForm.getInstructions());
	if (mode.isAuthor()) { // Teacher cannot modify following
	    pixlr.setOfflineInstructions(authForm.getOnlineInstruction());
	    pixlr.setOnlineInstructions(authForm.getOfflineInstruction());
	    pixlr.setLockOnFinished(authForm.isLockOnFinished());
	    pixlr.setReflectOnActivity(authForm.isReflectOnActivity());
	    pixlr.setReflectInstructions(authForm.getReflectInstructions());
	    pixlr.setAllowViewOthersImages(authForm.isAllowViewOthersImages());
	}
    }

    /**
     * Updates AuthoringForm using Pixlr content.
     * 
     * @param pixlr
     * @param authForm
     * @return
     */
    private void updateAuthForm(AuthoringForm authForm, Pixlr pixlr) {
	authForm.setTitle(pixlr.getTitle());
	authForm.setInstructions(pixlr.getInstructions());
	authForm.setOnlineInstruction(pixlr.getOnlineInstructions());
	authForm.setOfflineInstruction(pixlr.getOfflineInstructions());
	authForm.setLockOnFinished(pixlr.isLockOnFinished());
	authForm.setReflectOnActivity(pixlr.isReflectOnActivity());
	authForm.setExistingImageFileName(pixlr.getImageFileName());
	authForm.setReflectInstructions(pixlr.getReflectInstructions());
	authForm.setAllowViewOthersImages(pixlr.isAllowViewOthersImages());

	if (pixlr.getImageFileName() == null || pixlr.getImageFileName().trim().equals("")) {
	    authForm.setFileName(PixlrConstants.DEFAULT_IMAGE_FILE_NAME);
	} else {
	    authForm.setFileName(pixlr.getImageFileName());
	}
    }

    /**
     * Updates SessionMap using Pixlr content.
     * 
     * @param pixlr
     * @param mode
     */
    private SessionMap<String, Object> createSessionMap(Pixlr pixlr, ToolAccessMode mode, String contentFolderID,
	    Long toolContentID) {

	SessionMap<String, Object> map = new SessionMap<String, Object>();

	map.put(AuthoringAction.KEY_MODE, mode);
	map.put(AuthoringAction.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringAction.KEY_TOOL_CONTENT_ID, toolContentID);
	map.put(AuthoringAction.KEY_ONLINE_FILES, new LinkedList<PixlrAttachment>());
	map.put(AuthoringAction.KEY_OFFLINE_FILES, new LinkedList<PixlrAttachment>());
	map.put(AuthoringAction.KEY_UNSAVED_ONLINE_FILES, new LinkedList<PixlrAttachment>());
	map.put(AuthoringAction.KEY_UNSAVED_OFFLINE_FILES, new LinkedList<PixlrAttachment>());
	map.put(AuthoringAction.KEY_DELETED_FILES, new LinkedList<PixlrAttachment>());

	Iterator<PixlrAttachment> iter = pixlr.getPixlrAttachments().iterator();
	while (iter.hasNext()) {
	    PixlrAttachment attachment = (PixlrAttachment) iter.next();
	    String type = attachment.getFileType();
	    if (type.equals(IToolContentHandler.TYPE_OFFLINE)) {
		getAttList(AuthoringAction.KEY_OFFLINE_FILES, map).add(attachment);
	    }
	    if (type.equals(IToolContentHandler.TYPE_ONLINE)) {
		getAttList(AuthoringAction.KEY_ONLINE_FILES, map).add(attachment);
	    }
	}

	return map;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR
     * mode.
     * 
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

    /**
     * Retrieves a List of attachments from the map using the key.
     * 
     * @param key
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<PixlrAttachment> getAttList(String key, SessionMap<String, Object> map) {
	List<PixlrAttachment> list = (List<PixlrAttachment>) map.get(key);
	return list;
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     * 
     * @param request
     * @param authForm
     * @return
     */
    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }

    /**
     * Validate imageGallery item.
     * 
     * @param itemForm
     * @return
     */
    private ActionErrors validateImageFile(AuthoringForm itemForm) {
	ActionErrors errors = new ActionErrors();

	// validate file size
	FileValidatorUtil.validateFileSize(itemForm.getFile(), true, errors);
	// for edit validate: file already exist
	if (!itemForm.isHasFile()
		&& ((itemForm.getFile() == null) || StringUtils.isEmpty(itemForm.getFile().getFileName()))) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PixlrConstants.ERROR_MSG_FILE_BLANK));
	}

	// check for allowed format : gif, png, jpg
	if (itemForm.getFile() != null) {
	    String contentType = itemForm.getFile().getContentType();
	    if (StringUtils.isEmpty(contentType)
		    || !(contentType.equals("image/gif") || contentType.equals("image/png")
			    || contentType.equals("image/jpg") || contentType.equals("image/jpeg") || contentType
			    .equals("image/pjpeg"))) {
		errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(PixlrConstants.ERROR_MSG_NOT_ALLOWED_FORMAT));
	    }
	}

	return errors;
    }

    /**
     * Upload the image to the open www/images/pixlr folder
     * 
     * @param request
     * @param imageForm
     * @throws ImageGalleryException
     */
    private void uploadFormImage(AuthoringForm imageForm, Pixlr pixlr) throws Exception {
	
	String filename = PixlrConstants.DEFAULT_IMAGE_FILE_NAME;

	if (imageForm.getFile() != null) {

	    // check the directory exists, then create it if it doesnt
	    File pixlrDir = new File(LAMS_PIXLR_BASE_DIR);
	    if (!pixlrDir.exists()) {
		pixlrDir.mkdirs();
	    }

	    filename = FileUtil.generateUniqueContentFolderID() + ".jpg";
	    String fileWriteName = LAMS_PIXLR_BASE_DIR + File.separator + filename;
	    File uploadFile = new File(fileWriteName);
	    FileOutputStream out = new FileOutputStream(uploadFile);
	    FormFile formFile = imageForm.getFile();
	    out.write(formFile.getFileData());
	    
	    // Now save the image size
	    BufferedImage imageFile = ImageIO.read(uploadFile);
	    int width = imageFile.getWidth();
	    int height = imageFile.getHeight();
	    
	    pixlr.setImageFileName(filename);
	    pixlr.setImageHeight(new Long(height));
	    pixlr.setImageWidth(new Long(width));
	}

    }

    public ActionForward deleteImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	;

	// set up pixlrService
	if (pixlrService == null) {
	    pixlrService = PixlrServiceProxy.getPixlrService(this.getServlet().getServletContext());
	}

	// retrieving Pixlr with given toolContentID
	Pixlr pixlr = pixlrService.getPixlrByContentId(toolContentID);
	if (pixlr != null && !pixlr.getImageFileName().equals(PixlrConstants.DEFAULT_IMAGE_FILE_NAME)) {
	    String realBaseDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator
		    + FileUtil.LAMS_WWW_DIR + File.separator + "images" + File.separator + "pixlr";
	    String fileName = WebUtil.readStrParam(request, "existingImageFileName");

	    String imageFileName = realBaseDir + File.separator + fileName;

	    File imageFile = new File(imageFileName);

	    if (imageFile.exists()) {
		imageFile.delete();
	    }

	    pixlr.setImageFileName(PixlrConstants.DEFAULT_IMAGE_FILE_NAME);
	    pixlrService.saveOrUpdatePixlr(pixlr);
	}

	return unspecified(mapping, form, request, response);
    }
}

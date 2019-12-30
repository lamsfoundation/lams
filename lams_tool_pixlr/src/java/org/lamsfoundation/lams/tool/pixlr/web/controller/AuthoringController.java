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

package org.lamsfoundation.lams.tool.pixlr.web.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.service.IPixlrService;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrException;
import org.lamsfoundation.lams.tool.pixlr.web.forms.AuthoringForm;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/authoring")
public class AuthoringController {
    private static Logger logger = Logger.getLogger(AuthoringController.class);

    @Autowired
    private IPixlrService pixlrService;

    @Autowired
    @Qualifier("pixlrMessageService")
    private MessageService messageService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

    /**
     * Default method when no dispatch parameter is specified. It is expected
     * that the parameter <code>toolContentID</code> will be passed in. This
     * will be used to retrieve content for this tool.
     *
     */
    @RequestMapping("")
    protected String unspecified(@ModelAttribute("authoringForm") AuthoringForm authoringForm,
	    HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// retrieving Pixlr with given toolContentID
	Pixlr pixlr = pixlrService.getPixlrByContentId(toolContentID);
	if (pixlr == null) {
	    pixlr = pixlrService.copyDefaultContent(toolContentID);
	    pixlr.setCreateDate(new Date());
	    pixlrService.saveOrUpdatePixlr(pixlr);
	    // TODO NOTE: this causes DB orphans when LD not saved.
	}

	return readDatabaseData(authoringForm, pixlr, request, mode);
    }
    
    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute AuthoringForm authoringForm, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Pixlr pixlr = pixlrService.getPixlrByContentId(toolContentID);
	pixlr.setDefineLater(true);
	pixlrService.saveOrUpdatePixlr(pixlr);

	//audit log the teacher has started editing activity in monitor
	pixlrService.auditLogStartEditingActivityInMonitor(toolContentID);

	return readDatabaseData(authoringForm, pixlr, request, ToolAccessMode.TEACHER);
    }
    
    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(AuthoringForm authoringForm, Pixlr pixlr, HttpServletRequest request,
	    ToolAccessMode mode) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String imageUrl = PixlrConstants.LAMS_WWW_PIXLR_FOLDER_URL;
	Boolean imageExists = false;
	if ((pixlr.getImageFileName() != null && !pixlr.getImageFileName().equals(""))) {
	    imageUrl += pixlr.getImageFileName();
	    if (!pixlr.getImageFileName().equals(pixlrService.getDefaultContent().getImageFileName())) {
		imageExists = true;
	    }
	} else {
	    imageUrl += pixlrService.getDefaultContent().getImageFileName();
	}
	request.setAttribute("imageURL", imageUrl);
	request.setAttribute("imageExists", imageExists);

	// Set up the authForm.
	updateAuthForm(authoringForm, pixlr);
	authoringForm.setToolContentID(toolContentID);
	authoringForm.setMode(mode.toString());
	authoringForm.setContentFolderID(contentFolderID);

	// Set up sessionMap
	SessionMap<String, Object> map = createSessionMap(pixlr, mode, contentFolderID, toolContentID);
	authoringForm.setSessionMapID(map.getSessionID());

	// add the sessionMap to HTTPSession.
	request.getSession().setAttribute(map.getSessionID(), map);
	request.setAttribute(PixlrConstants.ATTR_SESSION_MAP, map);

	return "pages/authoring/authoring";
    }

    @RequestMapping(path = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute("authoringForm") AuthoringForm authoringForm,
	    HttpServletRequest request) throws PixlrException {
	// TODO need error checking.

	// get authForm and session map.
	SessionMap<String, Object> map = getSessionMap(request, authoringForm);
	ToolAccessMode mode = (ToolAccessMode) map.get(AuthoringController.KEY_MODE);

	// get pixlr content.
	Pixlr pixlr = pixlrService.getPixlrByContentId((Long) map.get(AuthoringController.KEY_TOOL_CONTENT_ID));
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	try {
	    // TODO: Need to check if this is an edit, if so, delete the old image
	    if (authoringForm.getExistingImageFileName().equals(PixlrConstants.DEFAULT_IMAGE_FILE_NAME)
		    || authoringForm.getExistingImageFileName().trim().equals("")) {
		errorMap = validateImageFile(authoringForm);

		if (!errorMap.isEmpty()) {
		    request.setAttribute("errorMap", errorMap);
		    updateAuthForm(authoringForm, pixlr);
		    if (mode != null) {
			authoringForm.setMode(mode.toString());
		    } else {
			authoringForm.setMode("");
		    }
		    return "pages/authoring/authoring";
		}
		uploadFormImage(authoringForm, pixlr);
	    }
	} catch (Exception e) {
	    logger.error("Problem uploading image", e);
	    errorMap.add("GLOBAL", messageService.getMessage(PixlrConstants.ERROR_MSG_FILE_UPLOAD));
	    //throw new PixlrException("Problem uploading image", e);
	}

	// update pixlr content using form inputs.
	updatePixlr(pixlr, authoringForm);

	// set the update date
	pixlr.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	pixlr.setDefineLater(false);

	pixlrService.saveOrUpdatePixlr(pixlr);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authoringForm.setSessionMapID(map.getSessionID());

	request.setAttribute(PixlrConstants.ATTR_SESSION_MAP, map);

	updateAuthForm(authoringForm, pixlr);
	if (mode != null) {
	    authoringForm.setMode(mode.toString());
	} else {
	    authoringForm.setMode("");
	}

	return "pages/authoring/authoring";
    }

    /* ========== Private Methods ********** */

    /**
     * Updates Pixlr content using AuthoringForm inputs.
     *
     * @param authoringForm
     * @param mode
     * @return
     */
    private void updatePixlr(Pixlr pixlr, @ModelAttribute("authoringForm") AuthoringForm authoringForm) {
	pixlr.setTitle(authoringForm.getTitle());
	pixlr.setInstructions(authoringForm.getInstructions());
	pixlr.setLockOnFinished(authoringForm.isLockOnFinished());
	pixlr.setReflectOnActivity(authoringForm.isReflectOnActivity());
	pixlr.setReflectInstructions(authoringForm.getReflectInstructions());
	pixlr.setAllowViewOthersImages(authoringForm.isAllowViewOthersImages());
    }

    /**
     * Updates AuthoringForm using Pixlr content.
     *
     * @param pixlr
     * @param authoringForm
     * @return
     */
    private void updateAuthForm(@ModelAttribute("authoringForm") AuthoringForm authoringForm, Pixlr pixlr) {
	authoringForm.setTitle(pixlr.getTitle());
	authoringForm.setInstructions(pixlr.getInstructions());
	authoringForm.setLockOnFinished(pixlr.isLockOnFinished());
	authoringForm.setReflectOnActivity(pixlr.isReflectOnActivity());
	authoringForm.setExistingImageFileName(pixlr.getImageFileName());
	authoringForm.setReflectInstructions(pixlr.getReflectInstructions());
	authoringForm.setAllowViewOthersImages(pixlr.isAllowViewOthersImages());

	if (pixlr.getImageFileName() == null || pixlr.getImageFileName().trim().equals("")) {
	    authoringForm.setFileName(PixlrConstants.DEFAULT_IMAGE_FILE_NAME);
	} else {
	    authoringForm.setFileName(pixlr.getImageFileName());
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

	SessionMap<String, Object> map = new SessionMap<>();

	map.put(AuthoringController.KEY_MODE, mode);
	map.put(AuthoringController.KEY_CONTENT_FOLDER_ID, contentFolderID);
	map.put(AuthoringController.KEY_TOOL_CONTENT_ID, toolContentID);

	return map;
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
    private MultiValueMap<String, String> validateImageFile(AuthoringForm itemForm) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	// validate file size
	FileValidatorUtil.validateFileSize(itemForm.getFile(), true);
	// for edit validate: file already exist
	if (!itemForm.isHasFile()
		&& ((itemForm.getFile() == null) || StringUtils.isEmpty(itemForm.getFile().getOriginalFilename()))) {
	    errorMap.add("GLOBAL", messageService.getMessage(PixlrConstants.ERROR_MSG_FILE_BLANK));
	}

	// check for allowed format : gif, png, jpg
	if (itemForm.getFile() != null) {
	    String contentType = itemForm.getFile().getContentType();
	    if (StringUtils.isEmpty(contentType) || !(contentType.equals("image/gif") || contentType.equals("image/png")
		    || contentType.equals("image/jpg") || contentType.equals("image/jpeg")
		    || contentType.equals("image/pjpeg"))) {
		errorMap.add("GLOBAL", messageService.getMessage(PixlrConstants.ERROR_MSG_NOT_ALLOWED_FORMAT));
	    }
	}

	return errorMap;
    }

    /**
     * Upload the image to the open www/images/pixlr folder
     *
     * @param request
     * @param authoringForm
     * @throws ImageGalleryException
     */
    private void uploadFormImage(@ModelAttribute("authoringForm") AuthoringForm authoringForm, Pixlr pixlr)
	    throws Exception {

	String filename = PixlrConstants.DEFAULT_IMAGE_FILE_NAME;

	// set up pixlrService

	if (authoringForm.getFile() != null) {

	    // check the directory exists, then create it if it doesnt
	    File pixlrDir = new File(PixlrConstants.LAMS_PIXLR_BASE_DIR);
	    if (!pixlrDir.exists()) {
		pixlrDir.mkdirs();
	    }

	    MultipartFile formFile = authoringForm.getFile();

	    filename = FileUtil.generateUniqueContentFolderID()
		    + pixlrService.getFileExtension(formFile.getOriginalFilename());
	    String fileWriteName = PixlrConstants.LAMS_PIXLR_BASE_DIR + File.separator + filename;
	    File uploadFile = new File(fileWriteName);
	    FileOutputStream out = new FileOutputStream(uploadFile);

	    out.write(formFile.getBytes());

	    // Now save the image size
	    BufferedImage imageFile = ImageIO.read(uploadFile);
	    int width = imageFile.getWidth();
	    int height = imageFile.getHeight();

	    pixlr.setImageFileName(filename);
	    pixlr.setImageHeight(new Long(height));
	    pixlr.setImageWidth(new Long(width));
	}

    }

    @RequestMapping("/deleteImage")
    public String deleteImage(@ModelAttribute("authoringForm") AuthoringForm authoringForm,
	    HttpServletRequest request) {

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

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

	return unspecified(authoringForm, request);
    }
}

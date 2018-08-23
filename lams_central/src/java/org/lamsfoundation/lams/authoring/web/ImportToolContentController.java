/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.authoring.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Import tool content servlet. It needs an uploaded learning design zip file.
 *
 * @author Steve.Ni
 */
@Controller
public class ImportToolContentController {

    public static final String EXPORT_TOOLCONTENT_SERVICE_BEAN_NAME = "exportToolContentService";
    public static final String USER_SERVICE_BEAN_NAME = "userManagementService";
    public static final String MESSAGE_SERVICE_BEAN_NAME = "authoringMessageService";
    public static final String PARAM_LEARING_DESIGN_ID = "learningDesignID";
    public static final String PARAM_LEARNING_FILE_NAME = "fileName";
    public static final String ATTR_TOOLS_ERROR_MESSAGE = "toolsErrorMessages";
    public static final String ATTR_LD_ERROR_MESSAGE = "ldErrorMessages";
    public static final String ATTR_LD_ID = "learningDesignID";

    private static final String KEY_MSG_IMPORT_FILE_NOT_FOUND = "msg.import.file.not.found";
    private static final String KEY_MSG_IMPORT_FAILED_UNKNOWN_REASON = "msg.import.failed.unknown.reason";

    private Logger log = Logger.getLogger(ImportToolContentController.class);

    @Autowired
    @Qualifier("userManagementService")
    IUserManagementService userManagementService;
    @Autowired
    @Qualifier("exportToolContentService")
    IExportToolContentService exportToolContentService;
    @Autowired
    @Qualifier("authoringMessageService")
    MessageService authoringMessageService;

    @RequestMapping("/authoring/importToolContent")
    public String execute(HttpServletRequest request) throws Exception {
	String param = request.getParameter("method");
	String customCSV = WebUtil.readStrParam(request, AttributeNames.PARAM_CUSTOM_CSV, true);
	//-----------------------Resource Author function ---------------------------
	if (StringUtils.equals(param, "import")) {
	    if (customCSV != null) {
		request.setAttribute(AttributeNames.PARAM_CUSTOM_CSV, customCSV);
	    }
	    //display initial page for upload
	    return "toolcontent/import";
	} else {
	    importLD(request);
	    return "toolcontent/importresult";
	}
    }

    /**
     * @param request
     */
    private void importLD(HttpServletRequest request) {

	List<String> ldErrorMsgs = new ArrayList<>();
	List<String> toolsErrorMsgs = new ArrayList<>();
	Long ldId = null;

	try {
	    Integer workspaceFolderUid = null;

	    //get shared session
	    HttpSession ss = SessionManager.getSession();
	    //get back login user DTO
	    UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    User user = (User) userManagementService.findById(User.class, userDto.getUserID());

	    File designFile = null;
	    Map<String, String> params = new HashMap<>();
	    String filename = null;

	    String uploadPath = FileUtil.createTempDirectory("_uploaded_learningdesign");

	    DiskFileUpload fu = new DiskFileUpload();
	    // maximum size that will be stored in memory
	    fu.setSizeThreshold(4096);
	    // the location for saving data that is larger than getSizeThreshold()
	    // fu.setRepositoryPath(uploadPath);

	    List fileItems = fu.parseRequest(request);
	    Iterator iter = fileItems.iterator();
	    while (iter.hasNext()) {
		FileItem fi = (FileItem) iter.next();
		//UPLOAD_FILE is input field from HTML page
		if (!fi.getFieldName().equalsIgnoreCase("UPLOAD_FILE")) {
		    params.put(fi.getFieldName(), fi.getString());
		} else {
		    // filename on the client
		    filename = FileUtil.getFileName(fi.getName());
		    designFile = new File(uploadPath + filename);
		    fi.write(designFile);

		}
		workspaceFolderUid = NumberUtils.createInteger(params.get("WORKSPACE_FOLDER_UID"));
	    }

	    // get customCSV for tool adapters if it was an external LMS request
	    String customCSV = params.get(AttributeNames.PARAM_CUSTOM_CSV);

	    if (designFile == null) {
		MessageService msgService = authoringMessageService;
		log.error("Upload file missing. Filename was " + filename);
		String msg = msgService.getMessage(KEY_MSG_IMPORT_FILE_NOT_FOUND);
		ldErrorMsgs.add(msg != null ? msg : "Upload file missing");

	    } else {

		Object[] ldResults = exportToolContentService.importLearningDesign(designFile, user, workspaceFolderUid,
			toolsErrorMsgs, customCSV);
		ldId = (Long) ldResults[0];
		ldErrorMsgs = (List<String>) ldResults[1];
		toolsErrorMsgs = (List<String>) ldResults[2];

	    }

	} catch (Exception e) {
	    log.error("Error occured during import", e);
	    ldErrorMsgs.add(e.getClass().getName() + " " + e.getMessage());
	}

	request.setAttribute(ATTR_LD_ID, ldId);
	if ((ldId == null || ldId.longValue() == -1) && ldErrorMsgs.size() == 0) {
	    MessageService msgService = authoringMessageService;
	    ldErrorMsgs.add(msgService.getMessage(KEY_MSG_IMPORT_FAILED_UNKNOWN_REASON));
	}
	if (ldErrorMsgs.size() > 0) {
	    request.setAttribute(ATTR_LD_ERROR_MESSAGE, ldErrorMsgs);
	}
	if (toolsErrorMsgs.size() > 0) {
	    request.setAttribute(ATTR_TOOLS_ERROR_MESSAGE, toolsErrorMsgs);
	}

    }
}

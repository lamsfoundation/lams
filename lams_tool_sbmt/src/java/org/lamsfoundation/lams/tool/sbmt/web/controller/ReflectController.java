package org.lamsfoundation.lams.tool.sbmt.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.sbmt.SbmtConstants;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author steven
 *
 */
@Controller
@RequestMapping("/learning")
public class ReflectController {

    @Autowired
    @Qualifier("submitFilesService")
    private ISubmitFilesService submitFilesService;

    /**
     * Display empty reflection form.
     */
    @RequestMapping("/newReflection")
    public String newReflection(@ModelAttribute ReflectionForm refForm, HttpServletRequest request,
	    HttpServletResponse response) {

//		ISubmitFilesService submitFilesService = getService();
//		ActionErrors errors = validateBeforeFinish(request,submitFilesService);
//		if(!errors.isEmpty()){
//			this.addErrors(request,errors);
//			return mapping.getInputForward();
//		}

	//get session value
	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry

	SessionMap map = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		SbmtConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return "learner/notebook";
    }

    /**
     * Submit reflection form input database.
     */
    @RequestMapping("/submitReflection")
    public String submitReflection(@ModelAttribute ReflectionForm refForm, HttpServletRequest request) {
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = submitFilesService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		SbmtConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    submitFilesService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    SbmtConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    submitFilesService.updateEntry(entry);
	}

	return "redirect:/learner.do?method=finish";
    }

    //**********************************************************************************************
    //		Private mehtods
    //**********************************************************************************************

    public static void validateBeforeFinish(Errors errors, HttpServletRequest request,
	    ISubmitFilesService submitFilesService) {
	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userID = user.getUserID();

	List list = submitFilesService.getFilesUploadedByUser(userID, sessionId, request.getLocale(), false);
	int minUpload = (Integer) sessionMap.get(SbmtConstants.PARAM_MIN_UPLOAD);
	if (minUpload > 0) {
	    errors.reject("error.learning.minimum.upload.number.less");
	}
    }
}

package org.lamsfoundation.lams.tool.sbmt.web.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.sbmt.SbmtConstants;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author steven
 */
public class ReflectAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	//================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(SbmtConstants.SUCCESS);
    }

    /**
     * Display empty reflection form.
     */
    public ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	ISubmitFilesService submitFilesService = getService();

	SessionMap map = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		SbmtConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(SbmtConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database.
     */
    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ISubmitFilesService submitFilesService = getService();

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

	return mapping.findForward(SbmtConstants.SUCCESS);
    }

    //**********************************************************************************************
    //		Private mehtods
    //**********************************************************************************************
    private ISubmitFilesService getService() {
	ISubmitFilesService submitFilesService = SubmitFilesServiceProxy
		.getSubmitFilesService(this.getServlet().getServletContext());
	return submitFilesService;
    }

    public static ActionErrors validateBeforeFinish(HttpServletRequest request,
	    ISubmitFilesService submitFilesService) {
	ActionErrors errors = new ActionErrors();
	String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int minUpload = (Integer) sessionMap.get(SbmtConstants.PARAM_MIN_UPLOAD);
	if (minUpload > 0) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.learning.minimum.upload.number.less"));
	}

	return errors;
    }
}

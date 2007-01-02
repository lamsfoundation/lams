package org.lamsfoundation.lams.admin.web;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.LibraryActivityDTO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Manage the learning libraries to allow sysadmin to enable/disable a libraries.
 * 
 * @struts:action path="/libraryManage"
 * 				validate="false"
 * @struts:action-forward name="success"
 *                        path=".librarylist"
 * @author steven
 *
 */
public class LibraryManageAction extends Action {
	public static final String LEARNING_DESIGN_SERVICE_BEAN_NAME = "learningDesignService";
	private static final String USER_MANAGEMENT_SERVICE_BEAN_NAME = "userManagementService";
	public static final String REQUEST_LIBRARIES = "libraries";
	public static final String PARAM_LIBRARY_ID = "libraryID";
	public static final String PARAM_ACTION = "action";
	public static final String ERROR_MSG_NO_PRIVILEDGE = "error.no.sysadmin.priviledge";
	
	/**
	 * Entry of STRUST action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String param = request.getParameter(PARAM_ACTION);
		// ---------------------------
		if (StringUtils.equals(param,"enable")) {
			if(!checkPriviledge(request)){
				return mapping.findForward("success");
			}
			enableLibray(mapping, form, request, response);
		}
		if (StringUtils.equals(param,"disable")) {
			disableLibray(mapping, form, request, response);
		}

		// ---------------------------
		// Display all libraries
		ILearningDesignService ldService = getLearningDesignService();
		List<LearningLibraryDTO> lds = ldService.getAllLearningLibraryDetails(false);
		
		//handle multiple activities library: only left on activity which has learning_libarayIid populated
		for (LearningLibraryDTO libraryDTO : lds) {
			Vector<LibraryActivityDTO> activities = libraryDTO.getTemplateActivities();
			if(activities != null){
				Iterator<LibraryActivityDTO> actIter = activities.iterator();
				while(actIter.hasNext()){
					LibraryActivityDTO act = actIter.next();
					if(act.getLearningLibraryID() == null)
						actIter.remove();
				}
			}
		}
		request.setAttribute(REQUEST_LIBRARIES, lds);
		return mapping.findForward("success");
	}



	private void disableLibray(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long learningLibraryId = WebUtil.readLongParam(request, PARAM_LIBRARY_ID, false);
		ILearningDesignService ldService = getLearningDesignService();
		ldService.setValid(learningLibraryId,false);
	}

	private void enableLibray(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long learningLibraryId = WebUtil.readLongParam(request, PARAM_LIBRARY_ID, false);
		ILearningDesignService ldService = getLearningDesignService();
		ldService.setValid(learningLibraryId,true);

	}
	
	//*************************************************************************************
	// Private method 
	//*************************************************************************************
	/**
	 * Return Service bean.
	 */
	private ILearningDesignService getLearningDesignService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (ILearningDesignService) wac.getBean(LEARNING_DESIGN_SERVICE_BEAN_NAME);
	}
	private IUserManagementService getUserManagementService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		return (IUserManagementService) wac.getBean(USER_MANAGEMENT_SERVICE_BEAN_NAME);
	}
	/**
	 * Double check priviledge : is sys admin?
	 * @param request
	 * @return
	 */
	private boolean checkPriviledge(HttpServletRequest request) {
		if(!getUserManagementService().isUserSysAdmin()){
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(ERROR_MSG_NO_PRIVILEDGE));
			this.saveErrors(request, errors);
			return false;
		}	
		
		return true;
	}
}

package org.lamsfoundation.lams.admin.web.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.util.LtiUtils;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author Andrey Balan
 */
public class LtiConsumerManagementAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LtiConsumerManagementAction.class);
    private IUserManagementService userManagementService;
    private MessageService messageService;
    private IIntegrationService integrationService;

    private void initServices() {
	if (userManagementService == null) {
	    userManagementService = AdminServiceProxy.getService(getServlet().getServletContext());
	}
	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	}
	if (integrationService == null) {
	    integrationService = AdminServiceProxy.getIntegrationService(getServlet().getServletContext());
	}
    }
    
    /**
     * Shows all available LTI tool consumers
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initServices();
	
	List<ExtServer> ltiConsumers = integrationService.getAllToolConsumers();
	Collections.sort(ltiConsumers);
	request.setAttribute("ltiConsumers", ltiConsumers);
	
	return mapping.findForward("ltiConsumerList");
    }

    /**
     * Edits specified LTI tool consumer
     */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();

	DynaActionForm ltiConsumerForm = (DynaActionForm) form;
	Integer sid = WebUtil.readIntParam(request, "sid", true);

	// editing a tool consumer
	if (sid != null) {
	    ExtServer ltiConsumer = integrationService.getExtServer(sid);
	    BeanUtils.copyProperties(ltiConsumerForm, ltiConsumer);
	    String lessonFinishUrl = ltiConsumer.getLessonFinishUrl() == null ? "-" : ltiConsumer.getLessonFinishUrl();
	    request.setAttribute("lessonFinishUrl", lessonFinishUrl);

	// create a tool consumer
	} else { 
	    //do nothing
	}

	return mapping.findForward("ltiConsumer");	
    }

    /**
     * Disables or enables (depending on "disable" parameter) specified LTI tool consumer
     */
    public ActionForward disable(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();
	
	Integer sid = WebUtil.readIntParam(request, "sid", true);
	boolean disable = WebUtil.readBooleanParam(request, "disable");
	ExtServer ltiConsumer = integrationService.getExtServer(sid);
	ltiConsumer.setDisabled(disable);
	integrationService.saveExtServer(ltiConsumer);
	
	return unspecified(mapping, form, request, response);
    }

    /**
     * Removes specified LTI tool consumer
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();

	Integer sid = WebUtil.readIntParam(request, "sid", true);
	userManagementService.deleteById(ExtServer.class, sid);
	
	return unspecified(mapping, form, request, response);
    }
    
    /**
     * Stores in the DB a new or edited LTI tool consumer
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();

	if (isCancelled(request)) {
	    //show LTI consumer list page
	    return unspecified(mapping, form, request, response);
	}

	DynaActionForm extServerForm = (DynaActionForm) form;
	ActionMessages errors = new ActionMessages();
	String[] requiredFields = { "serverid", "serverkey", "servername", "prefix" };
	for (String requiredField : requiredFields) {
	    if (StringUtils.trimToNull(extServerForm.getString(requiredField)) == null) {
		errors.add(requiredField,
			new ActionMessage("error.required", messageService.getMessage("sysadmin." + requiredField)));
	    }
	}
	
	Integer sid = (Integer) extServerForm.get("sid");
	//check duplication
	if (errors.isEmpty()) {
	    String[] uniqueFields = { "serverid", "prefix" };
	    for (String uniqueField : uniqueFields) {
		List<ExtServer> list = userManagementService.findByProperty(ExtServer.class, uniqueField,
			extServerForm.get(uniqueField));
		if (list != null && list.size() > 0) {
		    if (sid.equals(0)) {//new map
			errors.add(uniqueField, new ActionMessage("error.not.unique",
				messageService.getMessage("sysadmin." + uniqueField)));
		    } else {
			ExtServer ltiConsumer = list.get(0);
			if (!ltiConsumer.getSid().equals(sid)) {
			    errors.add(uniqueField, new ActionMessage("error.not.unique",
				    messageService.getMessage("sysadmin." + uniqueField)));
			}
		    }

		}
	    }
	}
	if (errors.isEmpty()) {
	    ExtServer ltiConsumer = null;
	    if (sid.equals(0)) {
		ltiConsumer = new ExtServer();
		BeanUtils.copyProperties(ltiConsumer, extServerForm);
		ltiConsumer.setSid(null);
		ltiConsumer.setServerTypeId(ExtServer.LTI_CONSUMER_SERVER_TYPE);
		ltiConsumer.setUserinfoUrl("blank");
	    } else {
		ltiConsumer = integrationService.getExtServer(sid);
		BeanUtils.copyProperties(ltiConsumer, extServerForm);
	    }
	    integrationService.saveExtServer(ltiConsumer);
	    return unspecified(mapping, form, request, response);
	    
	} else {
	    saveErrors(request, errors);
	    return mapping.getInputForward();
	}
    }

}

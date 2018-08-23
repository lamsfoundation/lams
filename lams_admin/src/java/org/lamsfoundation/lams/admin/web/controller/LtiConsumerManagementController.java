package org.lamsfoundation.lams.admin.web.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.LtiConsumerForm;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/ltiConsumerManagement")
public class LtiConsumerManagementController {

    private static Logger log = Logger.getLogger(LtiConsumerManagementController.class);
    private IUserManagementService userManagementService;
    private MessageService messageService;
    private IIntegrationService integrationService;

    @Autowired
    private WebApplicationContext applicationContext;

    private void initServices() {
	if (userManagementService == null) {
	    userManagementService = AdminServiceProxy.getService(applicationContext.getServletContext());
	}
	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(applicationContext.getServletContext());
	}
	if (integrationService == null) {
	    integrationService = AdminServiceProxy.getIntegrationService(applicationContext.getServletContext());
	}
    }

    /**
     * Shows all available LTI tool consumers
     */
    @RequestMapping(path = "/start")
    public String unspecified(HttpServletRequest request) {
	initServices();

	List<ExtServer> ltiConsumers = integrationService.getAllToolConsumers();
	Collections.sort(ltiConsumers);
	request.setAttribute("ltiConsumers", ltiConsumers);

	return "lti/ltiConsumerList";
    }

    /**
     * Edits specified LTI tool consumer
     */
    @RequestMapping(path = "/edit")
    public String edit(@ModelAttribute LtiConsumerForm ltiConsumerForm, HttpServletRequest request) throws Exception {

	initServices();

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

	return "lti/ltiConsumer";
    }

    /**
     * Disables or enables (depending on "disable" parameter) specified LTI tool consumer
     */
    @RequestMapping(path = "/disable", method = RequestMethod.POST)
    public String disable(HttpServletRequest request) throws Exception {

	initServices();

	Integer sid = WebUtil.readIntParam(request, "sid", true);
	boolean disable = WebUtil.readBooleanParam(request, "disable");
	ExtServer ltiConsumer = integrationService.getExtServer(sid);
	ltiConsumer.setDisabled(disable);
	integrationService.saveExtServer(ltiConsumer);

	return unspecified(request);
    }

    /**
     * Removes specified LTI tool consumer
     */
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws Exception {

	initServices();

	Integer sid = WebUtil.readIntParam(request, "sid", true);
	userManagementService.deleteById(ExtServer.class, sid);

	return unspecified(request);
    }

    /**
     * Stores in the DB a new or edited LTI tool consumer
     */
    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute LtiConsumerForm ltiConsumerForm, Errors errors, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();

	String[] requiredFields = { "serverid", "serverkey", "servername", "prefix" };
	for (String requiredField : requiredFields) {
	    if (StringUtils.trimToNull(requiredField) == null) {
		errors.reject("error.required", messageService.getMessage("sysadmin." + requiredField));
	    }
	}

	Integer sid = ltiConsumerForm.getSid();
	//check duplication
	if (!errors.hasErrors()) {
	    String[] uniqueFields = { "serverid", "prefix" };
	    for (String uniqueField : uniqueFields) {
		List<ExtServer> list = userManagementService.findByProperty(ExtServer.class, "uniqueField",
			uniqueField);
		if (list != null && list.size() > 0) {
		    if (sid.equals(0)) {//new map
			errors.reject("error.not.unique", messageService.getMessage("sysadmin." + uniqueField));
		    } else {
			ExtServer ltiConsumer = list.get(0);
			if (!ltiConsumer.getSid().equals(sid)) {
			    errors.reject("error.not.unique", messageService.getMessage("sysadmin." + uniqueField));
			}
		    }

		}
	    }
	}
	if (!errors.hasErrors()) {
	    ExtServer ltiConsumer = null;
	    if (sid.equals(0)) {
		ltiConsumer = new ExtServer();
		BeanUtils.copyProperties(ltiConsumer, ltiConsumerForm);
		ltiConsumer.setSid(null);
		ltiConsumer.setServerTypeId(ExtServer.LTI_CONSUMER_SERVER_TYPE);
		ltiConsumer.setUserinfoUrl("blank");
	    } else {
		ltiConsumer = integrationService.getExtServer(sid);
		BeanUtils.copyProperties(ltiConsumer, ltiConsumerForm);
	    }
	    integrationService.saveExtServer(ltiConsumer);
	    return unspecified(request);

	} else {
	    return "lti/ltiConsumer";
	}
    }

}

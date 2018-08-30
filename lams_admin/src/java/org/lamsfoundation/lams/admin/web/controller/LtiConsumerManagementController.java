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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    @RequestMapping(path = "/disable")
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
    @RequestMapping(path = "/delete")
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
    public String save(@ModelAttribute LtiConsumerForm ltiConsumerForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	if (StringUtils.trimToNull(ltiConsumerForm.getServerid()) == null) {
	    errorMap.add("serverid", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.serverid") }));
	}
	if (StringUtils.trimToNull(ltiConsumerForm.getServerkey()) == null) {
	    errorMap.add("serverkey", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.serverkey") }));
	}
	if (StringUtils.trimToNull(ltiConsumerForm.getServername()) == null) {
	    errorMap.add("servername", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.servername") }));
	}
	if (StringUtils.trimToNull(ltiConsumerForm.getPrefix()) == null) {
	    errorMap.add("prefix", messageService.getMessage("error.required",
		    new Object[] { messageService.getMessage("sysadmin.prefix") }));
	}

	Integer sid = ltiConsumerForm.getSid();
	//check duplication
	if (errorMap.isEmpty()) {
	    List<ExtServer> listServer = userManagementService.findByProperty(ExtServer.class, "serverid",
		    ltiConsumerForm.getServerid());
	    if (listServer != null && listServer.size() > 0) {
		if (sid == null) {//new map
		    errorMap.add("serverid", messageService.getMessage("error.not.unique",
			    new Object[] { messageService.getMessage("sysadmin.serverid") }));
		} else {
		    ExtServer ltiConsumer = listServer.get(0);
		    if (!ltiConsumer.getSid().equals(sid)) {
			errorMap.add("serverid", messageService.getMessage("error.not.unique",
				new Object[] { messageService.getMessage("sysadmin.serverid") }));
		    }
		}
	    }

	    List<ExtServer> listPrefix = userManagementService.findByProperty(ExtServer.class, "prefix",
		    ltiConsumerForm.getPrefix());
	    if (listPrefix != null && listPrefix.size() > 0) {
		if (sid == null) {//new map
		    errorMap.add("prefix", messageService.getMessage("error.not.unique",
			    new Object[] { messageService.getMessage("sysadmin.prefix") }));
		} else {
		    ExtServer ltiConsumer = listPrefix.get(0);
		    if (!ltiConsumer.getSid().equals(sid)) {
			errorMap.add("prefix", messageService.getMessage("error.not.unique",
				new Object[] { messageService.getMessage("sysadmin.prefix") }));
		    }
		}
	    }
	}

	if (errorMap.isEmpty()) {
	    ExtServer ltiConsumer = null;
	    if (sid == null) {
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
	    request.setAttribute("errorMap", errorMap);
	    return "lti/ltiConsumer";
	}
    }

}

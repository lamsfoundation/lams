package org.lamsfoundation.lams.admin.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.form.LtiConsumerForm;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.timezone.Timezone;
import org.lamsfoundation.lams.timezone.dto.TimezoneDTO;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.timezone.util.TimezoneIDComparator;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.filter.AuditLogFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/ltiConsumerManagement")
public class LtiConsumerManagementController {
    private static Logger log = Logger.getLogger(LtiConsumerManagementController.class);

    @Autowired
    private IIntegrationService integrationService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ITimezoneService timezoneService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    /**
     * Shows all available LTI tool consumers
     */
    @RequestMapping(path = "/start")
    public String start(HttpServletRequest request) {
	securityService.isSysadmin(getUserId(), "open LTI consumer list", true);

	List<ExtServer> ltiConsumers = integrationService.getAllToolConsumers();
	Collections.sort(ltiConsumers);
	request.setAttribute("ltiConsumers", ltiConsumers);

	return "integration/ltiConsumerList";
    }

    /**
     * Edits specified LTI tool consumer
     */
    @RequestMapping(path = "/edit")
    public String edit(@ModelAttribute LtiConsumerForm ltiConsumerForm, HttpServletRequest request) throws Exception {
	securityService.isSysadmin(getUserId(), "open LTI consumer edit page", true);

	Integer sid = WebUtil.readIntParam(request, "sid", true);

	// editing a tool consumer
	if (sid != null) {
	    ExtServer ltiConsumer = integrationService.getExtServer(sid);
	    BeanUtils.copyProperties(ltiConsumerForm, ltiConsumer);

	    SupportedLocale locale = ltiConsumer.getDefaultLocale();
	    if (locale != null) {
		ltiConsumerForm.setDefaultLocaleId(locale.getLocaleId());
	    }
	} else {
	    // do nothing in case of creating a tool consumer
	}

	boolean isLtiAdvantageEnabled = false;
	try {
	    Class clazz = Class.forName("org.lamsfoundation.lams.lti.advantage.util.LtiAdvantageUtil", false,
		    this.getClass().getClassLoader());
	    isLtiAdvantageEnabled = clazz != null;
	} catch (Exception e) {
	}

	if (isLtiAdvantageEnabled) {
	    request.setAttribute("ltiAdvantageEnabled", isLtiAdvantageEnabled);

	    List<SupportedLocale> locales = userManagementService.findAll(SupportedLocale.class);
	    Collections.sort(locales);
	    request.setAttribute("locales", locales);

	    request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(false));

	    List<Timezone> availableTimeZones = timezoneService.getDefaultTimezones();
	    TreeSet<TimezoneDTO> timezoneDtos = new TreeSet<>(new TimezoneIDComparator());
	    for (Timezone availableTimeZone : availableTimeZones) {
		String timezoneId = availableTimeZone.getTimezoneId();
		TimezoneDTO timezoneDto = new TimezoneDTO();
		timezoneDto.setTimeZoneId(timezoneId);
		timezoneDto.setDisplayName(TimeZone.getTimeZone(timezoneId).getDisplayName());
		timezoneDtos.add(timezoneDto);
	    }
	    request.setAttribute("timezoneDtos", timezoneDtos);
	}

	integrationService.clearLessonFinishUrlCache();

	return "integration/ltiConsumer";
    }

    /**
     * Disables or enables (depending on "disable" parameter) specified LTI tool consumer
     */
    @RequestMapping(path = "/disable", method = RequestMethod.POST)
    public String disable(HttpServletRequest request) throws Exception {
	securityService.isSysadmin(getUserId(), "disable LTI consumer", true);

	Integer sid = WebUtil.readIntParam(request, "sid", true);
	boolean disable = WebUtil.readBooleanParam(request, "disable");
	ExtServer ltiConsumer = integrationService.getExtServer(sid);
	ltiConsumer.setDisabled(disable);
	integrationService.saveExtServer(ltiConsumer);

	integrationService.clearLessonFinishUrlCache();

	AuditLogFilter.log(AuditLogFilter.LTI_INTEGRATED_SERVER_DISABLE_ACTION,
		"integrated server name: " + ltiConsumer.getServerid());

	return start(request);
    }

    @RequestMapping(path = "/enable", method = RequestMethod.POST)
    public String enable(HttpServletRequest request) throws Exception {
	Integer sid = WebUtil.readIntParam(request, "sid", false);
	ExtServer ltiConsumer = integrationService.getExtServer(sid);
	ltiConsumer.setDisabled(false);
	integrationService.saveExtServer(ltiConsumer);

	integrationService.clearLessonFinishUrlCache();

	AuditLogFilter.log(AuditLogFilter.LTI_INTEGRATED_SERVER_ENABLE_ACTION,
		"LTI integrated server name: " + ltiConsumer.getServerid());

	return "redirect:/extserver/serverlist.do";
    }

    /**
     * Removes specified LTI tool consumer
     */
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws Exception {
	securityService.isSysadmin(getUserId(), "delete LTI consumer", true);

	Integer sid = WebUtil.readIntParam(request, "sid", true);
	ExtServer extServer = integrationService.getExtServer(sid);

	AuditLogFilter.log(AuditLogFilter.LTI_INTEGRATED_SERVER_DELETE_ACTION,
		"LTI integrated server name: " + extServer.getServerid());

	userManagementService.delete(extServer);

	integrationService.clearLessonFinishUrlCache();

	return start(request);
    }

    /**
     * Stores in the DB a new or edited LTI tool consumer
     */
    @RequestMapping(path = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute LtiConsumerForm ltiConsumerForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	securityService.isSysadmin(getUserId(), "save LTI consumer", true);

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
	}

	if (errorMap.isEmpty()) {
	    ExtServer ltiConsumer = null;
	    if (sid.equals(-1)) {
		ltiConsumer = new ExtServer();
		BeanUtils.copyProperties(ltiConsumer, ltiConsumerForm);
		ltiConsumer.setSid(null);
		ltiConsumer.setServerTypeId(ExtServer.LTI_CONSUMER_SERVER_TYPE);
		ltiConsumer.setUserinfoUrl("blank");

		AuditLogFilter.log(AuditLogFilter.LTI_INTEGRATED_SERVER_ADD_ACTION,
			"LTI integrated server name: " + ltiConsumer.getServerid());
	    } else {
		ltiConsumer = integrationService.getExtServer(sid);
		BeanUtils.copyProperties(ltiConsumer, ltiConsumerForm);

		AuditLogFilter.log(AuditLogFilter.LTI_INTEGRATED_SERVER_EDIT_ACTION,
			"LTI integrated server name: " + ltiConsumer.getServerid());
	    }
	    ltiConsumer.setTimeToLiveLoginRequestEnabled(false);

	    SupportedLocale locale = (SupportedLocale) userManagementService.findById(SupportedLocale.class,
		    ltiConsumerForm.getDefaultLocaleId());
	    ltiConsumer.setDefaultLocale(locale);

	    integrationService.saveExtServer(ltiConsumer);

	    integrationService.clearLessonFinishUrlCache();

	    return start(request);

	} else {
	    request.setAttribute("errorMap", errorMap);
	    return "integration/ltiConsumer";
	}
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}
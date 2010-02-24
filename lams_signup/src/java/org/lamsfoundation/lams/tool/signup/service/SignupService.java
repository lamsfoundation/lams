package org.lamsfoundation.lams.tool.signup.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.signup.dao.SignupDAO;
import org.lamsfoundation.lams.tool.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.tool.signup.model.SignupUser;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.LanguageUtil;

public class SignupService {

    private SignupDAO signupDAO;
    private IUserManagementService userManagementService;
    private ILessonService lessonService;

    public SignupDAO getSignupDAO() {
	return signupDAO;
    }

    public void setSignupDAO(SignupDAO signupDAO) {
	this.signupDAO = signupDAO;
    }

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public AuthenticationMethod getAuthenticationMethod(Integer id) {
	return (AuthenticationMethod) signupDAO.find(AuthenticationMethod.class, id);
    }

    // replicating LanguageUtil method here because it's service bean injection
    // method doesn't work from here
    public SupportedLocale getDefaultLocale() {
	String localeName = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
	String langIsoCode = LanguageUtil.DEFAULT_LANGUAGE;
	String countryIsoCode = LanguageUtil.DEFAULT_COUNTRY;
	if (StringUtils.isNotBlank(localeName) && localeName.length() > 2) {
	    langIsoCode = localeName.substring(0, 2);
	    countryIsoCode = localeName.substring(3);
	}

	SupportedLocale locale = null;
	locale = getSupportedLocaleOrNull(langIsoCode, countryIsoCode);
	if (locale == null) {
	    locale = getSupportedLocaleOrNull(LanguageUtil.DEFAULT_LANGUAGE, LanguageUtil.DEFAULT_COUNTRY);
	}

	return locale;
    }

    // replicating LanguageUtil method here because it's service bean injection
    // method doesn't work from here
    private SupportedLocale getSupportedLocaleOrNull(String langIsoCode, String countryIsoCode) {
	SupportedLocale locale = null;

	Map<String, Object> properties = new HashMap<String, Object>();

	if (StringUtils.isNotBlank(countryIsoCode)) {
	    properties.put("countryIsoCode", countryIsoCode.trim());
	}
	if (StringUtils.isNotBlank(langIsoCode)) {
	    properties.put("languageIsoCode", langIsoCode.trim());
	}

	if (properties.isEmpty()) {
	    return null;
	}

	List list = signupDAO.findByProperties(SupportedLocale.class, properties);
	if (list != null && list.size() > 0) {
	    Collections.sort(list);
	    locale = (SupportedLocale) list.get(0);
	} else {
	    locale = null;
	}
	return locale;
    }

    public void signupUser(User user, String context) {
	// save User
	user.setFlashTheme(userManagementService.getDefaultFlashTheme());
	user.setHtmlTheme(userManagementService.getDefaultHtmlTheme());
	user.setDisabledFlag(false);
	user.setAuthenticationMethod(getAuthenticationMethod(AuthenticationMethod.DB));
	user.setLocale(getDefaultLocale());
	user.setCreateDate(new Date());
	signupDAO.insert(user);

	// add to org
	SignupOrganisation signup = signupDAO.getSignupOrganisation(context);

	ArrayList<String> rolesList = new ArrayList<String>();
	rolesList.add(Role.ROLE_LEARNER.toString());
	if (signup.getAddAsStaff()) {
	    rolesList.add(Role.ROLE_MONITOR.toString());
	    rolesList.add(Role.ROLE_AUTHOR.toString());
	} else if (signup.getAddWithAuthor()) {
	    rolesList.add(Role.ROLE_AUTHOR.toString());
	} else if (signup.getAddWithMonitor()) {
	    rolesList.add(Role.ROLE_MONITOR.toString());
	}

	userManagementService
		.setRolesForUserOrganisation(user, signup.getOrganisation().getOrganisationId(), rolesList);

	if (signup.getAddToLessons()) {
	    // add to lessons
	    user = userManagementService.getUserByLogin(user.getLogin());

	    Set lessonSet = signup.getOrganisation().getLessons();
	    Iterator lessonIterator = lessonSet.iterator();
	    while (lessonIterator.hasNext()) {
		Lesson lesson = (Lesson) lessonIterator.next();
		lessonService.addLearner(lesson.getLessonId(), user.getUserId());
		if (signup.getAddAsStaff()) {
		    lessonService.addStaffMember(lesson.getLessonId(), user.getUserId());
		}
	    }
	}

	// add record of signup
	SignupUser sUser = new SignupUser();
	sUser.setOrganisation(signup.getOrganisation());
	sUser.setUser(user);
	sUser.setSignupDate(new Date());
	signupDAO.insert(sUser);
    }
}

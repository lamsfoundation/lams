package org.lamsfoundation.lams.signup.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.signup.dao.ISignupDAO;
import org.lamsfoundation.lams.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.LanguageUtil;

public class SignupService implements ISignupService {

    private ISignupDAO signupDAO;
    private IUserManagementService userManagementService;
    private ILessonService lessonService;

    @Override
    public void signupUser(User user, String password, String context) {
	// save User
	user.setTheme(userManagementService.getDefaultTheme());
	user.setAuthenticationMethod(getAuthenticationMethod(AuthenticationMethod.DB));
	user.setLocale(getDefaultLocale());
	user.setCreateDate(new Date());
	
	userManagementService.updatePassword(user, password);
	
	// add to org
	SignupOrganisation signup = signupDAO.getSignupOrganisation(context);

	ArrayList<String> rolesList = new ArrayList<>();
	rolesList.add(Role.ROLE_LEARNER.toString());
	if (signup.getAddAsStaff()) {
	    rolesList.add(Role.ROLE_MONITOR.toString());
	    rolesList.add(Role.ROLE_AUTHOR.toString());
	} else if (signup.getAddWithAuthor()) {
	    rolesList.add(Role.ROLE_AUTHOR.toString());
	} else if (signup.getAddWithMonitor()) {
	    rolesList.add(Role.ROLE_MONITOR.toString());
	}

	userManagementService.setRolesForUserOrganisation(user, signup.getOrganisation().getOrganisationId(),
		rolesList);

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
    }

    @Override
    public void signinUser(String login, String context) {
	User user = userManagementService.getUserByLogin(login);

	// add to org
	SignupOrganisation signup = signupDAO.getSignupOrganisation(context);

	ArrayList<String> rolesList = new ArrayList<>();
	rolesList.add(Role.ROLE_LEARNER.toString());
	if (signup.getAddAsStaff()) {
	    rolesList.add(Role.ROLE_MONITOR.toString());
	    rolesList.add(Role.ROLE_AUTHOR.toString());
	} else if (signup.getAddWithAuthor()) {
	    rolesList.add(Role.ROLE_AUTHOR.toString());
	} else if (signup.getAddWithMonitor()) {
	    rolesList.add(Role.ROLE_MONITOR.toString());
	}

	userManagementService.setRolesForUserOrganisation(user, signup.getOrganisation().getOrganisationId(),
		rolesList);

	if (signup.getAddToLessons()) {
	    // add to lessons
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
    }

    @Override
    public User getUserByLogin(String login) {
	return userManagementService.getUserByLogin(login);
    }

    @Override
    public SignupOrganisation getSignupOrganisation(String context) {
	SignupOrganisation result = signupDAO.getSignupOrganisation(context);
	// initialize lazy-loaded organisation here, so it can be used in JSP produced by SignupController#execute()
	Hibernate.initialize(result.getOrganisation());
	return result;
    }

    @Override
    public boolean usernameExists(String username) {
	return signupDAO.usernameExists(username);
    }

    @Override
    public boolean courseKeyIsValid(String context, String courseKey) {
	return signupDAO.courseKeyIsValid(context, courseKey);
    }

    @Override
    public List<SignupOrganisation> getSignupOrganisations() {
	return signupDAO.getSignupOrganisations();
    }

    @Override
    public List<Organisation> getOrganisationCandidates() {
	return signupDAO.getOrganisationCandidates();
    }

    @Override
    public boolean contextExists(Integer soid, String context) {
	return signupDAO.contextExists(soid, context);
    }

    @Override
    public boolean emailVerify(String login, String hash) {
	User user = getUserByLogin(login);
	if (user == null || !hash.equals(HashUtil.sha256(user.getEmail(), user.getSalt()))) {
	    return false;
	}
	if (!Boolean.TRUE.equals(user.getEmailVerified())) {
	    user.setEmailVerified(true);
	    user.setDisabledFlag(false);
	    userManagementService.save(user);
	}
	return true;
    }

    public void setSignupDAO(ISignupDAO signupDAO) {
	this.signupDAO = signupDAO;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    private AuthenticationMethod getAuthenticationMethod(Integer id) {
	return (AuthenticationMethod) userManagementService.findById(AuthenticationMethod.class, id);
    }

    // replicating LanguageUtil method here because it's service bean injection
    // method doesn't work from here
    private SupportedLocale getDefaultLocale() {
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

	Map<String, Object> properties = new HashMap<>();

	if (StringUtils.isNotBlank(countryIsoCode)) {
	    properties.put("countryIsoCode", countryIsoCode.trim());
	}
	if (StringUtils.isNotBlank(langIsoCode)) {
	    properties.put("languageIsoCode", langIsoCode.trim());
	}

	if (properties.isEmpty()) {
	    return null;
	}

	List list = userManagementService.findByProperties(SupportedLocale.class, properties);
	if (list != null && list.size() > 0) {
	    Collections.sort(list);
	    locale = (SupportedLocale) list.get(0);
	} else {
	    locale = null;
	}
	return locale;
    }
}

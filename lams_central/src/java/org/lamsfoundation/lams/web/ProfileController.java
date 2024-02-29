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
package org.lamsfoundation.lams.web;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.index.IndexOrgBean;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyDTO;
import org.lamsfoundation.lams.policies.service.IPolicyService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.timezone.TimeZoneUtil;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.IndexUtils;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
    private static Logger log = Logger.getLogger(ProfileController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILearnerService learnerService;
    @Autowired
    private IThemeService themeService;
    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;
    @Autowired
    private IPolicyService policyService;

    private static List<SupportedLocale> locales;

    @RequestMapping("/view")
    public String view(HttpServletRequest request) throws Exception {
	User requestor = userManagementService.getUserByLogin(request.getRemoteUser());
	String fullName = (requestor.getTitle() != null ? requestor.getTitle() + " " : "") + requestor.getFullName();
	String email = requestor.getEmail();

	request.setAttribute("fullName", fullName);
	request.setAttribute("email", (email != null ? email : ""));
	request.setAttribute("portraitUuid", requestor.getPortraitUuid());

	return "profile/profile";
    }

    @RequestMapping("/lessons")
    public String lessons(HttpServletRequest request) throws Exception {

	// list all active lessons for this learner (single sql query)
	User requestor = userManagementService.getUserByLogin(request.getRemoteUser());
	LessonDTO[] lessons = learnerService.getActiveLessonsFor(requestor.getUserId());

	// make org-sorted beans out of the lessons
	HashMap<Integer, IndexOrgBean> orgBeansMap = new HashMap<>();
	for (LessonDTO lesson : lessons) {
	    Integer orgId = lesson.getOrganisationID();
	    Organisation org = (Organisation) userManagementService.findById(Organisation.class, orgId);
	    Integer orgTypeId = org.getOrganisationType().getOrganisationTypeId();
	    IndexLessonBean lessonBean = new IndexLessonBean(lesson.getLessonName(),
		    "javascript:openLearner(" + lesson.getLessonID() + ", null, true)");
	    lessonBean.setId(lesson.getLessonID());
	    log.debug("Lesson: " + lesson.getLessonName());

	    // insert or update bean if it is a course
	    if (orgTypeId.equals(OrganisationType.COURSE_TYPE)) {
		IndexOrgBean orgBean = (!orgBeansMap.containsKey(orgId)) ? new IndexOrgBean(org.getOrganisationId(),
			org.getName(), orgTypeId) : orgBeansMap.get(orgId);
		orgBean.addLesson(lessonBean);
		orgBeansMap.put(orgId, orgBean);
	    } else if (orgTypeId.equals(OrganisationType.CLASS_TYPE)) {

		// if it is a class, find existing or create new parent bean
		Organisation parentOrg = org.getParentOrganisation();
		Integer parentOrgId = parentOrg.getOrganisationId();
		IndexOrgBean parentOrgBean = (!orgBeansMap.containsKey(parentOrgId))
			? new IndexOrgBean(parentOrg.getOrganisationId(), parentOrg.getName(),
			OrganisationType.COURSE_TYPE)
			: orgBeansMap.get(parentOrgId);
		// create new bean for class, or use existing bean
		IndexOrgBean orgBean = new IndexOrgBean(org.getOrganisationId(), org.getName(), orgTypeId);
		List<IndexOrgBean> childOrgBeans = parentOrgBean.getChildIndexOrgBeans();
		if (childOrgBeans.contains(orgBean)) {
		    // use existing org bean
		    orgBean = getOrgBean(org.getName(), childOrgBeans);
		    childOrgBeans.remove(orgBean);
		    orgBean.addLesson(lessonBean);
		    childOrgBeans.add(orgBean);
		    parentOrgBean.setChildIndexOrgBeans(childOrgBeans);
		} else {
		    // using new org bean
		    orgBean.addLesson(lessonBean);
		    parentOrgBean.addChildOrgBean(orgBean);
		}
		orgBeansMap.put(parentOrgId, parentOrgBean);
	    }
	}

	// sort group and subgroup names
	ArrayList<IndexOrgBean> beans = new ArrayList<>(orgBeansMap.values());
	Collections.sort(beans);
	for (IndexOrgBean b : beans) {
	    Collections.sort(b.getChildIndexOrgBeans());
	}

	// sort lessons inside each org bean
	for (Object o : beans) {
	    IndexOrgBean bean = (IndexOrgBean) o;
	    Organisation org = (Organisation) userManagementService.findById(Organisation.class, bean.getId());

	    // put lesson beans into id-indexed map
	    HashMap<Long, IndexLessonBean> map = new HashMap<>();
	    for (IndexLessonBean lbean : bean.getLessons()) {
		map.put(lbean.getId(), lbean);
	    }

	    bean.setLessons(IndexUtils.sortLessonBeans(org.getOrderedLessonIds(), map));
	}

	request.setAttribute("beans", beans);

	return "profile/lessons";
    }

    private IndexOrgBean getOrgBean(String name, List<IndexOrgBean> list) {
	for (IndexOrgBean bean : list) {
	    if (StringUtils.equals(name, bean.getName())) {
		return bean;
	    }
	}
	return null;
    }

    @RequestMapping("/policyConsents")
    public String policyConsents(HttpServletRequest request) throws Exception {
	Integer userId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
	List<PolicyDTO> policyDtos = policyService.getPolicyDtosByUser(userId);
	request.setAttribute("policyDtos", policyDtos);

	return "profile/profilePolicyConsents";
    }

    @RequestMapping("/displayPolicyDetails")
    public String displayPolicyDetails(HttpServletRequest request) throws Exception {
	long policyUid = WebUtil.readLongParam(request, "policyUid");
	Policy policy = policyService.getPolicyByUid(policyUid);
	request.setAttribute("policy", policy);

	return "profile/policyDetails";
    }

    @RequestMapping("/edit")
    public String edit(@ModelAttribute("newForm") UserForm userForm, HttpServletRequest request) throws Exception {

	//some errors may have already been set in ProfileSaveAction
	MultiValueMap<String, String> errorMap = (MultiValueMap<String, String>) request.getAttribute("errorMap");
	if (errorMap == null) {
	    errorMap = new LinkedMultiValueMap<>();
	}

	if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_EDIT_ENABLE)) {
	    if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)) {
		errorMap.add("GLOBAL", messageService.getMessage("error.edit.disabled"));
	    } else {
		errorMap.add("GLOBAL", messageService.getMessage("message.partial.edit.only"));
	    }
	    request.setAttribute("errorMap", errorMap);
	    ;
	}

	User requestor = userManagementService.getUserByLogin(request.getRemoteUser());
	BeanUtils.copyProperties(userForm, requestor);
	SupportedLocale locale = requestor.getLocale();
	if (locale == null) {
	    locale = LanguageUtil.getDefaultLocale();
	}
	userForm.setLocaleId(locale.getLocaleId());
	if (locales == null) {
	    locales = userManagementService.findAll(SupportedLocale.class);
	}
	request.setAttribute("locales", locales);
	request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(false));

	// Get all the css themes
	List<Theme> themes = themeService.getAllThemes();
	request.setAttribute("themes", themes);

	// Check the user css theme is still installed
	Long userSelectedTheme = null;
	if (requestor.getTheme() != null) {
	    for (Theme theme : themes) {
		if (theme.getThemeId() == requestor.getTheme().getThemeId()) {
		    userSelectedTheme = theme.getThemeId();
		    break;
		}
	    }
	}
	// if still null, use the default
	if (userSelectedTheme == null) {
	    userSelectedTheme = themeService.getDefaultTheme().getThemeId();
	}
	userForm.setUserTheme(userSelectedTheme);

	request.setAttribute("timezones", TimeZoneUtil.getTimeZones());

	return "profile/editprofile";
    }
}
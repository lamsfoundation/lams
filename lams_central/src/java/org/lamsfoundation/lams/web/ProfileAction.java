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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.index.IndexOrgBean;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.timezone.Timezone;
import org.lamsfoundation.lams.timezone.dto.TimezoneDTO;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.timezone.util.TimezoneDTOComparator;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.IndexUtils;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class ProfileAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(ProfileAction.class);

    private static IUserManagementService service;

    private static List<SupportedLocale> locales;

    private static ICoreLearnerService learnerService;

    private static IThemeService themeService;

    private static ITimezoneService timezoneService;

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	User requestor = getService().getUserByLogin(request.getRemoteUser());
	String fullName = (requestor.getTitle() != null ? requestor.getTitle() + " " : "") + requestor.getFirstName()
		+ " " + requestor.getLastName();
	String email = requestor.getEmail();

	request.setAttribute("fullName", fullName);
	request.setAttribute("email", (email != null ? email : ""));
	request.setAttribute("portraitUuid", requestor.getPortraitUuid());

	return mapping.findForward("view");
    }

    public ActionForward lessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// list all active lessons for this learner (single sql query)
	User requestor = getService().getUserByLogin(request.getRemoteUser());
	LessonDTO[] lessons = getLearnerService().getActiveLessonsFor(requestor.getUserId());

	// make org-sorted beans out of the lessons
	HashMap<Integer, IndexOrgBean> orgBeansMap = new HashMap<Integer, IndexOrgBean>();
	for (LessonDTO lesson : lessons) {
	    Integer orgId = lesson.getOrganisationID();
	    Organisation org = (Organisation) getService().findById(Organisation.class, orgId);
	    Integer orgTypeId = org.getOrganisationType().getOrganisationTypeId();
	    IndexLessonBean lessonBean = new IndexLessonBean(lesson.getLessonName(),
		    "javascript:openLearner(" + lesson.getLessonID() + ")");
	    lessonBean.setId(lesson.getLessonID());
	    log.debug("Lesson: " + lesson.getLessonName());

	    // insert or update bean if it is a course
	    if (orgTypeId.equals(OrganisationType.COURSE_TYPE)) {
		IndexOrgBean orgBean = (!orgBeansMap.containsKey(orgId))
			? new IndexOrgBean(org.getOrganisationId(), org.getName(), orgTypeId) : orgBeansMap.get(orgId);
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
	ArrayList<IndexOrgBean> beans = new ArrayList<IndexOrgBean>(orgBeansMap.values());
	Collections.sort(beans);
	for (IndexOrgBean b : beans) {
	    Collections.sort(b.getChildIndexOrgBeans());
	}

	// sort lessons inside each org bean
	for (Object o : beans) {
	    IndexOrgBean bean = (IndexOrgBean) o;
	    Organisation org = (Organisation) service.findById(Organisation.class, bean.getId());

	    // put lesson beans into id-indexed map
	    HashMap<Long, IndexLessonBean> map = new HashMap<Long, IndexLessonBean>();
	    for (IndexLessonBean lbean : bean.getLessons()) {
		map.put(lbean.getId(), lbean);
	    }

	    bean.setLessons(IndexUtils.sortLessonBeans(org.getOrderedLessonIds(), map));
	}

	request.setAttribute("beans", beans);

	return mapping.findForward("lessons");
    }

    private IndexOrgBean getOrgBean(String name, List<IndexOrgBean> list) {
	for (IndexOrgBean bean : list) {
	    if (StringUtils.equals(name, bean.getName())) {
		return bean;
	    }
	}
	return null;
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	//some errors may have already been set in ProfileSaveAction
	ActionMessages errors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
	if (errors == null) {
	    errors = new ActionMessages();
	}

	if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_EDIT_ENABLE)) {
	    if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.edit.disabled"));
	    } else {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.partial.edit.only"));
	    }
	    saveErrors(request, errors);
	}

	User requestor = getService().getUserByLogin(request.getRemoteUser());
	DynaActionForm userForm = (DynaActionForm) form;
	BeanUtils.copyProperties(userForm, requestor);
	SupportedLocale locale = requestor.getLocale();
	if (locale == null) {
	    locale = LanguageUtil.getDefaultLocale();
	}
	userForm.set("localeId", locale.getLocaleId());
	request.setAttribute("locales", locales);
	if (requestor.isTwoFactorAuthenticationEnabled()) {
	    request.setAttribute("sharedSecret", requestor.getTwoFactorAuthenticationSecret());    
	}

	boolean hasLamsCommunityToken = requestor.getLamsCommunityToken() != null;
	request.setAttribute("hasLamsCommunityToken", hasLamsCommunityToken);

	themeService = getThemeService();

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
	userForm.set("userTheme", userSelectedTheme);

	List<Timezone> availableTimeZones = getTimezoneService().getDefaultTimezones();
	TreeSet<TimezoneDTO> timezoneDtos = new TreeSet<TimezoneDTO>(new TimezoneDTOComparator());
	for (Timezone availableTimeZone : availableTimeZones) {
	    String timezoneId = availableTimeZone.getTimezoneId();
	    TimezoneDTO timezoneDto = new TimezoneDTO();
	    timezoneDto.setTimeZoneId(timezoneId);
	    timezoneDto.setDisplayName(TimeZone.getTimeZone(timezoneId).getDisplayName());
	    timezoneDtos.add(timezoneDto);
	}
	request.setAttribute("timezoneDtos", timezoneDtos);

	return mapping.findForward("edit");
    }

    private IUserManagementService getService() {
	if (service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    service = (IUserManagementService) ctx.getBean("userManagementService");
	    locales = getService().findAll(SupportedLocale.class);
	    Collections.sort(locales);
	}
	return service;
    }

    private ICoreLearnerService getLearnerService() {
	if (learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    learnerService = (ICoreLearnerService) ctx.getBean("learnerService");
	}
	return learnerService;
    }

    private IThemeService getThemeService() {
	if (themeService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    themeService = (IThemeService) ctx.getBean("themeService");
	}
	return themeService;
    }

    private ITimezoneService getTimezoneService() {
	if (timezoneService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    timezoneService = (ITimezoneService) ctx.getBean("timezoneService");
	}
	return timezoneService;
    }
}
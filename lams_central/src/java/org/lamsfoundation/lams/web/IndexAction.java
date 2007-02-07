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
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version
 * 
 * <p>
 * <a href="IndexAction.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 * 
 * Created at 16:59:28 on 13/06/2006
 */
/**
 * struts doclet
 * 
 * @struts.action path="/index" validate="false"
 * 
 * @struts.action-forward name="main" path="/main.jsp"
 * @struts.action-forward name="community" path=".community"
 * @struts.action-forward name="profile" path="/profile.do?method=view"
 * @struts.action-forward name="editprofile" path="/profile.do?method=edit"
 * @struts.action-forward name="password" path="/password.do"
 * @struts.action-forward name="passwordChanged" path=".passwordChangeOk"
 * @struts.action-forward name="portrait" path="/portrait.do"
 * @struts.action-forward name="content" path="/indexContent.jsp"
 */
public class IndexAction extends Action {

	private static Logger log = Logger.getLogger(IndexAction.class);

	private static IUserManagementService service;
	
	private Integer state;
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String stateParam = WebUtil.readStrParam(request, "state");
		state = (stateParam.equals("active")?OrganisationState.ACTIVE:OrganisationState.ARCHIVED);
		
		log.debug("User:"+request.getRemoteUser());
		// only set header links if we are displaying 'active' organisations; i.e., on the index page
		if(state.equals(OrganisationState.ACTIVE)){
			setHeaderLinks(request);
			setAdminLinks(request);
		}
		
		// check if user is flagged as needing to change their password
		User loggedInUser = getService().getUserByLogin(request.getRemoteUser());
		if (loggedInUser.getChangePassword()) {
			return mapping.findForward("password");
		}
		
		String tab = WebUtil.readStrParam(request, "tab", true);
		if (StringUtils.equals(tab, "profile")) {
			return mapping.findForward("profile");
		} else if (StringUtils.equals(tab, "editprofile")) {
			return mapping.findForward("editprofile");
		} else if (StringUtils.equals(tab, "password")) {
			return mapping.findForward("password");
		} else if (StringUtils.equals(tab, "passwordChanged")) {
			request.setAttribute("tab", "profile");
			return mapping.findForward("passwordChanged");
		} else if (StringUtils.equals(tab, "portrait")) {
			return mapping.findForward("portrait");
		} else if (StringUtils.equals(tab, "community")) {
			request.setAttribute("tab", tab);
			return mapping.findForward("community");
		}
		
		List<IndexOrgBean> orgBeans = new ArrayList<IndexOrgBean>();
		if (request.isUserInRole(Role.SYSADMIN)) {
			List<Organisation> organisations = getService().getOrganisationsByTypeAndStatus(OrganisationType.COURSE_TYPE,state);
			// when listing archived orgs, make sure we get subgroups that are archived under still-active groups
			if (state.equals(OrganisationState.ARCHIVED)) {
				organisations.addAll(getService().getOrganisationsByTypeAndStatus(OrganisationType.COURSE_TYPE,OrganisationState.ACTIVE));
			}
			for (Organisation org:organisations) {
				log.debug("archived date: "+org.getArchivedDate());
				List<Integer> roles = new ArrayList<Integer>();
				roles.add(Role.ROLE_SYSADMIN);
				List<UserOrganisationRole> userOrganisationRoles = getService().getUserOrganisationRoles(org.getOrganisationId(),request.getRemoteUser());
				for(UserOrganisationRole userOrganisationRole:userOrganisationRoles){
					roles.add(userOrganisationRole.getRole().getRoleId());
				}
				// don't set the orgbean to display if subgroups are not archived
				IndexOrgBean iob = createOrgBean(org, roles, request.getRemoteUser(),true);
				if (state.equals(OrganisationState.ARCHIVED)
						&& org.getOrganisationState().getOrganisationStateId().equals(OrganisationState.ACTIVE)
						&& iob.getChildIndexOrgBeans().isEmpty()) {
					continue;
				}
				orgBeans.add(iob);
			}
		} else {
			List<UserOrganisation> userOrganisations = getService().getUserOrganisationsForUserByTypeAndStatus(request.getRemoteUser(),OrganisationType.COURSE_TYPE,state);
			// when listing archived orgs, make sure we get subgroups that are archived under still-active groups
			if (state.equals(OrganisationState.ARCHIVED)) {
				userOrganisations.addAll(getService().getUserOrganisationsForUserByTypeAndStatus(request.getRemoteUser(),OrganisationType.COURSE_TYPE,OrganisationState.ACTIVE));
			}
			for (UserOrganisation userOrganisation: userOrganisations) {
				log.debug("archived date: "+userOrganisation.getOrganisation().getArchivedDate());
				List<Integer> roles = new ArrayList<Integer>();
				for(Object userOrganisationRole:userOrganisation.getUserOrganisationRoles()){
					roles.add(((UserOrganisationRole)userOrganisationRole).getRole().getRoleId());
				}
				// don't set the orgbean to display if subgroups are not archived
				IndexOrgBean iob = createOrgBean(userOrganisation.getOrganisation(),roles,request.getRemoteUser(),false);
				if (state.equals(OrganisationState.ARCHIVED)
						&& userOrganisation.getOrganisation().getOrganisationState().getOrganisationStateId().equals(OrganisationState.ACTIVE)
						&& iob.getChildIndexOrgBeans().isEmpty()) {
					continue;
				}
				orgBeans.add(iob);
			}
		}
		request.setAttribute("orgBeans",orgBeans);
		if(state.equals(OrganisationState.ACTIVE))
			return mapping.findForward("main");
		else
			return mapping.findForward("content");
	}
	
	private void setHeaderLinks(HttpServletRequest request) {
		List<IndexLinkBean> headerLinks = new ArrayList<IndexLinkBean>();
		if (request.isUserInRole(Role.AUTHOR) || request.isUserInRole(Role.AUTHOR_ADMIN)) {
			log.debug("user is author");
			headerLinks.add(new IndexLinkBean("index.author", "javascript:openAuthor()"));
		}
		headerLinks.add(new IndexLinkBean("index.myprofile", "index.do?state=active&tab=profile"));
		
		if(Configuration.getAsBoolean(ConfigurationKeys.LAMS_COMMUNITY_ENABLE))
			if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.GROUP_ADMIN) || request.isUserInRole(Role.GROUP_MANAGER) || 
				request.isUserInRole(Role.AUTHOR) || request.isUserInRole(Role.AUTHOR_ADMIN) || request.isUserInRole(Role.MONITOR))
				headerLinks.add(new IndexLinkBean("index.community", "index.do?state=active&tab=community"));
		
		log.debug("set headerLinks in request");
		request.setAttribute("headerLinks", headerLinks);
	}
	
	private void setAdminLinks(HttpServletRequest request) {
		List<IndexLinkBean> adminLinks = new ArrayList<IndexLinkBean>();
		if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.GROUP_ADMIN) || request.isUserInRole(Role.GROUP_MANAGER))
			adminLinks.add(new IndexLinkBean("index.courseman", "javascript:openOrgManagement(" + getService().getRootOrganisation().getOrganisationId()+')'));
		if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.AUTHOR_ADMIN) || getService().isUserGlobalGroupAdmin())
			adminLinks.add(new IndexLinkBean("index.sysadmin", "javascript:openSysadmin()"));
		request.setAttribute("adminLinks", adminLinks);
	}

	@SuppressWarnings({"unchecked","static-access"})
	private IndexOrgBean createOrgBean(Organisation org, List<Integer> roles, String username, boolean isSysAdmin) {
		//log.debug("creating orgBean for org:"+org.getName());
		User user = (User)getService().findByProperty(User.class, "login",username).get(0);
		IndexOrgBean orgBean = new IndexOrgBean(org.getName(), org.getOrganisationType().getOrganisationTypeId());
		List<IndexLinkBean> links = new ArrayList<IndexLinkBean>();
		if(isSysAdmin && state.equals(OrganisationState.ACTIVE)){
			if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
				links.add(new IndexLinkBean("index.classman", "javascript:openOrgManagement(" + org.getOrganisationId()+")", "manage-group-button"));
			}
		}
		if ((contains(roles, Role.ROLE_GROUP_ADMIN) || contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles,Role.ROLE_MONITOR))
				&& state.equals(OrganisationState.ACTIVE)) {
			if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
				if((!isSysAdmin)&&(contains(roles, Role.ROLE_GROUP_ADMIN) || contains(roles, Role.ROLE_GROUP_MANAGER))){
					links.add(new IndexLinkBean("index.classman", "javascript:openOrgManagement(" + org.getOrganisationId()+")", "manage-group-button"));
				}
				if(contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles,Role.ROLE_MONITOR))
					links.add(new IndexLinkBean("index.addlesson", "javascript:openAddLesson(" + org.getOrganisationId()+",'')", "add-lesson-button"));
			}else{//CLASS_TYPE
				if(contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles,Role.ROLE_MONITOR))
					links.add(new IndexLinkBean("index.addlesson","javascript:openAddLesson("+org.getParentOrganisation().getOrganisationId()+","+org.getOrganisationId()+")", "add-lesson-button"));
			}
		}
		orgBean.setLinks(links);
		
		if (state.equals(OrganisationState.ARCHIVED) && org.getOrganisationState().getOrganisationStateId().equals(OrganisationState.ARCHIVED)) {
			orgBean.setArchivedDate(org.getArchivedDate());
		}

		List<IndexLessonBean> lessonBeans = new ArrayList<IndexLessonBean>();
		Set<Lesson> lessons = org.getLessons();
		for(Lesson lesson:lessons) {
			if(isInLesson(user,lesson)){
				if(!lesson.isPreviewLesson()){
					List<IndexLinkBean> lessonLinks = new ArrayList<IndexLinkBean>();
					String url = null;
					if(state.equals(OrganisationState.ACTIVE)){
						if(contains(roles,Role.ROLE_GROUP_MANAGER)||contains(roles,Role.ROLE_MONITOR)){
							if(!lesson.getLessonStateId().equals(lesson.REMOVED_STATE)){
								lessonLinks.add(new IndexLinkBean("index.monitor", "javascript:openMonitorLesson(" + lesson.getLessonId()+")"));
							}
						}
						if(contains(roles,Role.ROLE_LEARNER)){
							log.debug("Lesson State:"+lesson.getLessonStateId());
							if(lesson.getLessonStateId().equals(lesson.STARTED_STATE)||lesson.getLessonStateId().equals(lesson.FINISHED_STATE)){
								url = "javascript:openLearner("+lesson.getLessonId()+")";
							}
						}
					}else if(state.equals(OrganisationState.ARCHIVED)){
						if(contains(roles,Role.ROLE_GROUP_MANAGER)){
							if(!lesson.getLessonStateId().equals(lesson.REMOVED_STATE)){
								lessonLinks.add(new IndexLinkBean("index.monitor", "javascript:openMonitorLesson(" + lesson.getLessonId()+")"));
							}
						}
						if(contains(roles,Role.ROLE_LEARNER)){
							log.debug("Lesson State:"+lesson.getLessonStateId());
							if(lesson.getLessonStateId().equals(lesson.STARTED_STATE)||lesson.getLessonStateId().equals(lesson.FINISHED_STATE)){
								lessonLinks.add(new IndexLinkBean("label.export.portfolio","javascript:openExportPortfolio("+lesson.getLessonId()+")"));
							}
						}
					}
					if(lessonLinks.size()>0 || url!=null){
						IndexLessonBean lessonBean = new IndexLessonBean(lesson.getLessonName(), lessonLinks);
						lessonBean.setUrl(url);
						lessonBean.setState(lesson.getLessonStateId());
						lessonBeans.add(lessonBean);
					}
				}
			}
		}
		Collections.sort(lessonBeans);
		orgBean.setLessons(lessonBeans);

		if(orgBean.getType().equals(OrganisationType.COURSE_TYPE)){
			Set<Organisation> children = org.getChildOrganisations();
			List<IndexOrgBean> childOrgBeans = new ArrayList<IndexOrgBean>();
			for(Organisation organisation:children){
				if(organisation.getOrganisationState().getOrganisationStateId().equals(state)){
					List<Integer> classRoles = new ArrayList<Integer>();
					List<UserOrganisationRole> userOrganisationRoles = getService().getUserOrganisationRoles(organisation.getOrganisationId(),username);
					for(UserOrganisationRole userOrganisationRole:userOrganisationRoles){
						classRoles.add(userOrganisationRole.getRole().getRoleId());
					}
					if(contains(roles,Role.ROLE_GROUP_MANAGER)) classRoles.add(Role.ROLE_GROUP_MANAGER);
					childOrgBeans.add(createOrgBean(organisation,classRoles,username,isSysAdmin));
				}
			}
			orgBean.setChildIndexOrgBeans(childOrgBeans);
		}
		return orgBean;
	}

	private boolean isInLesson(User user, Lesson lesson) {
		return lesson.getLessonClass().isStaffMember(user)||lesson.getLessonClass().getLearners().contains(user);
	}

	private boolean contains(List<Integer> roles, Integer roleId) {
		for (int i = 0; i < roles.size(); i++) {
			if (roleId.equals(roles.get(i)))
				return true;
		}
		return false;
	}
	
	private IUserManagementService getService(){
		if(service==null){
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
		}
		return service;
	}
}

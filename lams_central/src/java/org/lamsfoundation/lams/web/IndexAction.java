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
 * 
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
			List<IndexLinkBean> headerLinks = new ArrayList<IndexLinkBean>();
			if (request.isUserInRole(Role.AUTHOR)) {
				log.debug("user is author");
				headerLinks.add(new IndexLinkBean("index.author", "javascript:openAuthor()"));
			}
			if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.COURSE_ADMIN) || request.isUserInRole(Role.COURSE_MANAGER)) {
				log.debug("user is an admin or manager");
				headerLinks.add(new IndexLinkBean("index.courseman", "admin/orgmanage.do?org=" + getService().getRootOrganisation().getOrganisationId()));
			}
			if (request.isUserInRole(Role.SYSADMIN)) {
				log.debug("user is sysadmin");
				headerLinks.add(new IndexLinkBean("index.sysadmin", "admin/sysadminstart.do"));
			}
			headerLinks.add(new IndexLinkBean("index.myprofile", "profile.do?method=view"));
			log.debug("set headerLinks in request");
			request.setAttribute("headerLinks", headerLinks);
		}
		
		List<IndexOrgBean> orgBeans = new ArrayList<IndexOrgBean>();
		if (request.isUserInRole(Role.SYSADMIN)) {
			List<Integer> roles = new ArrayList<Integer>();
			List<Organisation> organisations = getService().getOrganisationsByTypeAndStatus(OrganisationType.COURSE_TYPE,state);
			log.debug("we got "+organisations.size()+" organisations whose type is "+OrganisationType.COURSE_DESCRIPTION+" and whose state is "+state);
			roles.add(Role.ROLE_SYSADMIN);
			for (Organisation org:organisations) {
				List<UserOrganisationRole> userOrganisationRoles = getService().getUserOrganisationRoles(org.getOrganisationId(),request.getRemoteUser());
				for(UserOrganisationRole userOrganisationRole:userOrganisationRoles){
					roles.add(userOrganisationRole.getRole().getRoleId());
				}
				orgBeans.add(createOrgBean(org, roles, request.getRemoteUser(),true));
			}
		} else {
			List<UserOrganisation> userOrganisations = getService().getUserOrganisationsForUserByTypeAndStatus(request.getRemoteUser(),OrganisationType.COURSE_TYPE,state);
			log.debug("we got "+userOrganisations.size()+" organisations whose type is "+OrganisationType.COURSE_DESCRIPTION+" and whose state is "+state);
			for (UserOrganisation userOrganisation: userOrganisations) {
				List<Integer> roles = new ArrayList<Integer>();
				for(Object userOrganisationRole:userOrganisation.getUserOrganisationRoles()){
					roles.add(((UserOrganisationRole)userOrganisationRole).getRole().getRoleId());
				}
				orgBeans.add(createOrgBean(userOrganisation.getOrganisation(),roles,request.getRemoteUser(),false));
			}
		}
		request.setAttribute("orgBeans",orgBeans);
		if(state.equals(OrganisationState.ACTIVE))
			return mapping.findForward("main");
		else
			return mapping.findForward("content");
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
		if ((contains(roles, Role.ROLE_COURSE_ADMIN) || contains(roles, Role.ROLE_COURSE_MANAGER) || contains(roles,Role.ROLE_MONITOR))
				&& state.equals(OrganisationState.ACTIVE)) {
			if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
				if((!isSysAdmin)&&(contains(roles, Role.ROLE_COURSE_ADMIN) || contains(roles, Role.ROLE_COURSE_MANAGER))){
					links.add(new IndexLinkBean("index.classman", "javascript:openOrgManagement(" + org.getOrganisationId()+")", "manage-group-button"));
				}
				if(contains(roles, Role.ROLE_COURSE_MANAGER) || contains(roles,Role.ROLE_MONITOR))
					links.add(new IndexLinkBean("index.addlesson", "javascript:openAddLesson(" + org.getOrganisationId()+",'')", "add-lesson-button"));
			}else{//CLASS_TYPE
				if(contains(roles, Role.ROLE_COURSE_MANAGER) || contains(roles,Role.ROLE_MONITOR))
					links.add(new IndexLinkBean("index.addlesson","javascript:openAddLesson("+org.getParentOrganisation().getOrganisationId()+","+org.getOrganisationId()+")", "add-lesson-button"));
			}
		}
		
		orgBean.setLinks(links);

		List<IndexLessonBean> lessonBeans = new ArrayList<IndexLessonBean>();
		Set<Lesson> lessons = org.getLessons();
		for(Lesson lesson:lessons) {
			if(isInLesson(user,lesson)){
				if(!lesson.isPreviewLesson()){
					List<IndexLinkBean> lessonLinks = new ArrayList<IndexLinkBean>();
					String url = null;
					if(state.equals(OrganisationState.ACTIVE)){
						if(contains(roles,Role.ROLE_COURSE_MANAGER)||contains(roles,Role.ROLE_MONITOR)){
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
						if(contains(roles,Role.ROLE_COURSE_MANAGER)){
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
					if(contains(roles,Role.ROLE_COURSE_MANAGER)) classRoles.add(Role.ROLE_COURSE_MANAGER);
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

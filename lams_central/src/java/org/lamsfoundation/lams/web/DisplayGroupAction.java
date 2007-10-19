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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */ 
 
/* $Id$ */ 
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
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
 
/**
 * @author jliew
 * 
 * @struts.action path="/displayGroup" validate="false"
 * @struts.action-forward name="group" path="/group.jsp"
 */
public class DisplayGroupAction extends Action {

	private static Logger log = Logger.getLogger(DisplayGroupAction.class);
	private static IUserManagementService service;
	private Integer stateId = OrganisationState.ACTIVE;
	
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		stateId = WebUtil.readIntParam(request, "stateId", false);
		Integer orgId = WebUtil.readIntParam(request, "orgId", false);
		Organisation org = null;
		if (orgId != null) {
			org = (Organisation)getService().findById(Organisation.class, orgId);
		}
		
		if (org != null) {
			List<Integer> roles = new ArrayList<Integer>();
			List<UserOrganisationRole> userOrganisationRoles = getService().getUserOrganisationRoles(orgId,request.getRemoteUser());
			for(UserOrganisationRole userOrganisationRole:userOrganisationRoles){
				roles.add(userOrganisationRole.getRole().getRoleId());
			}
			IndexOrgBean iob = createOrgBean(org, roles, request.getRemoteUser(), request.isUserInRole(Role.SYSADMIN));
			request.setAttribute("orgBean", iob);
		}
		
		return mapping.findForward("group");
	}

	//@SuppressWarnings({"unchecked","static-access"})
	private IndexOrgBean createOrgBean(Organisation org, List<Integer> roles, String username, boolean isSysAdmin) {
		User user = (User)getService().findByProperty(User.class, "login", username).get(0);
		IndexOrgBean orgBean = new IndexOrgBean(org.getName(), org.getOrganisationType().getOrganisationTypeId());

		List<IndexLinkBean> links = new ArrayList<IndexLinkBean>();
		if(isSysAdmin && stateId.equals(OrganisationState.ACTIVE)){
			if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
				links.add(new IndexLinkBean("index.classman", "javascript:openOrgManagement(" + org.getOrganisationId()+")", "manage-group-button"));
			}
		}
		if ((contains(roles, Role.ROLE_GROUP_ADMIN) || contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles,Role.ROLE_MONITOR))
				&& stateId.equals(OrganisationState.ACTIVE)) {
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
		
		if (stateId.equals(OrganisationState.ARCHIVED) && org.getOrganisationState().getOrganisationStateId().equals(OrganisationState.ARCHIVED)) {
			orgBean.setArchivedDate(org.getArchivedDate());
		}

		List<IndexLessonBean> lessonBeans = new ArrayList<IndexLessonBean>();
		Set<Lesson> lessons = org.getLessons();
		for(Lesson lesson:lessons) {
			if(isInLesson(user,lesson)){
				if(!lesson.isPreviewLesson()){
					List<IndexLinkBean> lessonLinks = new ArrayList<IndexLinkBean>();
					String url = null;
					if(stateId.equals(OrganisationState.ACTIVE)){
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
					}else if(stateId.equals(OrganisationState.ARCHIVED)){
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
						IndexLessonBean lessonBean = new IndexLessonBean(lesson.getLessonName(), 
								lesson.getLessonDescription(), 
								url,
								lesson.getLessonStateId(),
								lessonLinks);
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
				if(organisation.getOrganisationState().getOrganisationStateId().equals(stateId)){
					List<Integer> classRoles = new ArrayList<Integer>();
					List<UserOrganisationRole> userOrganisationRoles = getService().getUserOrganisationRoles(organisation.getOrganisationId(),username);
					// don't list the subgroup if user is not a member, and not a group admin/manager
					if (userOrganisationRoles==null || userOrganisationRoles.isEmpty()) {
						if (!contains(roles,Role.ROLE_GROUP_ADMIN) && 
								!contains(roles,Role.ROLE_GROUP_MANAGER) &&
								!isSysAdmin) {
							continue;
						}
					}
					for(UserOrganisationRole userOrganisationRole:userOrganisationRoles){
						classRoles.add(userOrganisationRole.getRole().getRoleId());
					}
					if(contains(roles,Role.ROLE_GROUP_MANAGER)) classRoles.add(Role.ROLE_GROUP_MANAGER);
					childOrgBeans.add(createOrgBean(organisation,classRoles,username,isSysAdmin));
				}
			}
			Collections.sort(childOrgBeans);
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
			service = (IUserManagementService) ctx.getBean("userManagementService");
		}
		return service;
	}
}
 
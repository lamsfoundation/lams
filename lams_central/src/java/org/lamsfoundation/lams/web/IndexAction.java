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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
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
 * @struts.action-forward name="index" path="/indexContent.jsp"
 */
public class IndexAction extends Action {
	private static Logger log = Logger.getLogger(IndexAction.class);

	private static WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());

	private static UserManagementService service = (UserManagementService) ctx.getBean("userManagementServiceTarget");

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("User:"+request.getRemoteUser());
		List<IndexLinkBean> headerLinks = new ArrayList<IndexLinkBean>();
		headerLinks.add(new IndexLinkBean("index.dummymonitor","javascript:openDummyMonitor()"));
		headerLinks.add(new IndexLinkBean("index.myprofile", "javascript:openProfile('" + request.getRemoteUser()+"')"));
		if (request.isUserInRole(Role.SYSADMIN)) {
			log.debug("user is sysadmin");
			headerLinks.add(new IndexLinkBean("index.sysadmin", "javascript:openSysadmin()"));
			headerLinks.add(new IndexLinkBean("index.courseman", "javascript:openOrgManagement(" + service.getRootOrganisation().getOrganisationId()+")"));
		}
		if (request.isUserInRole(Role.AUTHOR)) {
			log.debug("user is author");
			headerLinks.add(new IndexLinkBean("index.author", "javascript:openAuthor()"));
		}
		log.debug("set headerLinks in request");
		request.setAttribute("headerLinks", headerLinks);

		List<IndexOrgBean> orgBeans = new ArrayList<IndexOrgBean>();
		if (request.isUserInRole(Role.SYSADMIN)) {
			List<Integer> roles = new ArrayList<Integer>();
			List<Organisation> organisations = service.getOrganisationsByTypeAndStatus(OrganisationType.COURSE_TYPE, OrganisationState.ACTIVE);
			log.debug("we got "+organisations.size()+" organisations whose type is "+OrganisationType.COURSE_DESCRIPTION+" and whose state is "+OrganisationState.ACTIVE);
			roles.add(Role.ROLE_SYSADMIN);
			for (Organisation org:organisations) {
				List<UserOrganisationRole> userOrganisationRoles = service.getUserOrganisationRoles(org.getOrganisationId(),request.getRemoteUser());
				for(UserOrganisationRole userOrganisationRole:userOrganisationRoles){
					roles.add(userOrganisationRole.getRole().getRoleId());
				}
				orgBeans.add(createOrgBean(org, roles, request.getRemoteUser(),true));
			}
		} else {
			List<UserOrganisation> userOrganisations = service.getUserOrganisationsForUserByTypeAndStatus(request.getRemoteUser(),OrganisationType.COURSE_TYPE,OrganisationState.ACTIVE);
			log.debug("we got "+userOrganisations.size()+" organisations whose type is "+OrganisationType.COURSE_DESCRIPTION+" and whose state is "+OrganisationState.ACTIVE);
			for (UserOrganisation userOrganisation: userOrganisations) {
				List<Integer> roles = new ArrayList<Integer>();
				for(Object userOrganisationRole:userOrganisation.getUserOrganisationRoles()){
					roles.add(((UserOrganisationRole)userOrganisationRole).getRole().getRoleId());
				}
				for(Integer roleId:roles){
					log.debug("role:"+roleId);
				}
				orgBeans.add(createOrgBean(userOrganisation.getOrganisation(),roles,request.getRemoteUser(),false));
			}
		}
		log.debug("set orgBeans in request");
		request.setAttribute("orgBeans",orgBeans);
		return mapping.findForward("index");
	}

	private IndexOrgBean createOrgBean(Organisation org, List<Integer> roles, String username, boolean isSysAdmin) {
		log.debug("creating orgBean for org:"+org.getName());
		IndexOrgBean orgBean = new IndexOrgBean(org.getName(), org.getOrganisationType().getOrganisationTypeId());
		List<IndexLinkBean> links = new ArrayList<IndexLinkBean>();
		if(isSysAdmin){
			if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
				links.add(new IndexLinkBean("index.classman", "javascript:openOrgManagement(" + org.getOrganisationId()+")"));
			}
		}
		if (contains(roles, Role.ROLE_COURSE_ADMIN) || contains(roles, Role.ROLE_COURSE_MANAGER) || contains(roles,Role.ROLE_STAFF)) {
			if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
				if((!isSysAdmin)&&(contains(roles, Role.ROLE_COURSE_ADMIN) || contains(roles, Role.ROLE_COURSE_MANAGER))){
					links.add(new IndexLinkBean("index.classman", "javascript:openOrgManagement(" + org.getOrganisationId()+")"));
				}
				links.add(new IndexLinkBean("index.addlesson", "javascript:openAddLesson(" + org.getOrganisationId()+",'')"));
			}else{//CLASS_TYPE
				links.add(new IndexLinkBean("index.addlesson","javascript:openAddLesson("+org.getParentOrganisation().getOrganisationId()+","+org.getOrganisationId()+")"));
			}
		}
		
		orgBean.setLinks(links);

		List<IndexLessonBean> lessonBeans = new ArrayList<IndexLessonBean>();
		Set<Lesson> lessons = org.getLessons();
		for(Lesson lesson:lessons) {
			if(!lesson.isPreviewLesson()){
				List<IndexLinkBean> lessonLinks = new ArrayList<IndexLinkBean>();
				if(contains(roles,Role.ROLE_COURSE_MANAGER)||contains(roles,Role.ROLE_STAFF)){
					if(!lesson.getLessonStateId().equals(lesson.REMOVED_STATE)){
						lessonLinks.add(new IndexLinkBean("index.monitor", "javascript:openMonitorLesson(" + lesson.getLessonId()+")"));
					}
				}
				if(contains(roles,Role.ROLE_LEARNER)){
					log.debug("Lesson State:"+lesson.getLessonStateId());
					if(lesson.getLessonStateId().equals(lesson.STARTED_STATE)||lesson.getLessonStateId().equals(lesson.FINISHED_STATE)){
						lessonLinks.add(new IndexLinkBean("index.participate","javascript:openLearner("+lesson.getLessonId()+")"));
					}
				}
				IndexLessonBean lessonBean = new IndexLessonBean(lesson.getLessonName(), lessonLinks);
				lessonBeans.add(lessonBean);
			}
		}
		Collections.sort(lessonBeans);
		orgBean.setLessons(lessonBeans);

		if(orgBean.getType().equals(OrganisationType.COURSE_TYPE)){
			Set<Organisation> children = org.getChildOrganisations();
			List<IndexOrgBean> childOrgBeans = new ArrayList<IndexOrgBean>();
			for(Organisation organisation:children){
				if(organisation.getOrganisationState().getOrganisationStateId().equals(OrganisationState.ACTIVE)){
					List<Integer> classRoles = new ArrayList<Integer>();
					List<UserOrganisationRole> userOrganisationRoles = service.getUserOrganisationRoles(organisation.getOrganisationId(),username);
					for(UserOrganisationRole userOrganisationRole:userOrganisationRoles){
						classRoles.add(userOrganisationRole.getRole().getRoleId());
					}
					childOrgBeans.add(createOrgBean(organisation,classRoles,username,isSysAdmin));
				}
			}
			orgBean.setChildIndexOrgBeans(childOrgBeans);
		}
		return orgBean;
	}

	private boolean contains(List<Integer> roles, Integer roleId) {
		log.debug("roleId:"+roleId);
		for (int i = 0; i < roles.size(); i++) {
			log.debug(roles.get(i));
			if (roleId.equals(roles.get(i)))
				return true;
		}
		return false;
	}
	
}

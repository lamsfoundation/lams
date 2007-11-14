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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
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
 * @struts.action-forward name="groupHeader" path="/groupHeader.jsp"
 * @struts.action-forward name="groupContents" path="/groupContents.jsp"
 * @struts.action-forward name="group" path="/group.jsp"
 */
public class DisplayGroupAction extends Action {

	private static Logger log = Logger.getLogger(DisplayGroupAction.class);
	private static IUserManagementService service;
	private Integer stateId = OrganisationState.ACTIVE;
	
	@SuppressWarnings({"unchecked"})
	public ActionForward execute(ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		String display = WebUtil.readStrParam(request, "display", false);
		stateId = WebUtil.readIntParam(request, "stateId", false);
		Integer orgId = WebUtil.readIntParam(request, "orgId", false);
		
		Organisation org = null;
		if (orgId != null) {
			org = (Organisation)getService().findById(Organisation.class, orgId);
		}
		
		String forwardPath = "group";
		if (org != null) {
			boolean allowSorting = false;
			List<Integer> roles = new ArrayList<Integer>();
			List<UserOrganisationRole> userOrganisationRoles = getService().getUserOrganisationRoles(orgId,request.getRemoteUser());
			for(UserOrganisationRole userOrganisationRole:userOrganisationRoles){
				Integer roleId = userOrganisationRole.getRole().getRoleId();
				roles.add(roleId);
				if (roleId.equals(Role.ROLE_GROUP_MANAGER) || roleId.equals(Role.ROLE_MONITOR)) {
					allowSorting = true;
				}
			}
			
			IndexOrgBean iob;
			if (StringUtils.equals(display, "contents")) {
				iob = new IndexOrgBean(org.getOrganisationId(), org.getName(), org.getOrganisationType().getOrganisationTypeId());
				iob = populateContentsOrgBean(iob, org, roles, request.getRemoteUser(), request.isUserInRole(Role.SYSADMIN));
				forwardPath = "groupContents";
			} else if (StringUtils.equals(display, "header")) {
				iob = createHeaderOrgBean(org, roles, request.getRemoteUser(), request.isUserInRole(Role.SYSADMIN), false);
				forwardPath = "groupHeader";
			} else {
				iob = createHeaderOrgBean(org, roles, request.getRemoteUser(), request.isUserInRole(Role.SYSADMIN), true);
			}
			
			request.setAttribute("orgBean", iob);
			request.setAttribute("allowSorting", allowSorting);
		}
		
		return mapping.findForward(forwardPath);
	}

	@SuppressWarnings({"unchecked"})
	private IndexOrgBean createHeaderOrgBean(Organisation org, List<Integer> roles, String username, boolean isSysAdmin, boolean includeContents)
		throws SQLException, NamingException {
		IndexOrgBean orgBean = new IndexOrgBean(org.getOrganisationId(), org.getName(), org.getOrganisationType().getOrganisationTypeId());

		// set org links
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
		
		// set archived date if archived
		if (stateId.equals(OrganisationState.ARCHIVED) && org.getOrganisationState().getOrganisationStateId().equals(OrganisationState.ARCHIVED)) {
			orgBean.setArchivedDate(org.getArchivedDate());
		}
		
		if (includeContents) {
			orgBean = populateContentsOrgBean(orgBean, org, roles, username, isSysAdmin);
		}
		
		return orgBean;
	}
	
	private IndexOrgBean populateContentsOrgBean(IndexOrgBean orgBean, Organisation org, List<Integer> roles, String username, boolean isSysAdmin)
		throws SQLException, NamingException {
		User user = (User)getService().findByProperty(User.class, "login", username).get(0);
		
		//	set lesson beans
		List<IndexLessonBean> lessonBeans = null;
		try {
			lessonBeans = getLessonBeans(user.getUserId(), org.getOrganisationId(), roles, org.getOrderedLessonIds());
		} catch (Exception e) {
			log.error("Failed retrieving user's lessons from database: " + e, e);
		}
		orgBean.setLessons(lessonBeans);

		// create subgroup beans
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
					childOrgBeans.add(createHeaderOrgBean(organisation,classRoles,username,isSysAdmin,true));
				}
			}
			Collections.sort(childOrgBeans);
			orgBean.setChildIndexOrgBeans(childOrgBeans);
		}
		return orgBean;
	}
	
	// get lesson beans and sort them
	private List<IndexLessonBean> getLessonBeans(Integer userId, Integer orgId, List<Integer> roles, String orderedLessonIds) 
		throws SQLException, NamingException {
		Map<Long, IndexLessonBean> map = populateLessonBeans(userId, orgId, roles);
		ArrayList<IndexLessonBean> orderedList = new ArrayList<IndexLessonBean>();
		
		if (orderedLessonIds != null) {
			List<String> idList = Arrays.asList(orderedLessonIds.split(","));
		
			// sort mapped lesson beans according to orderedLessonIds
			for (String idString : idList) {
				try {
					Long id = new Long(Long.parseLong(idString));
					if (map.containsKey(id)) {
						orderedList.add(map.get(id));
						map.remove(id);
					}
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}
		
		// append lesson beans not mentioned in orderedLessonIds
		if (!map.values().isEmpty()) {
			orderedList.addAll(map.values());
		}
		
		return orderedList;
	}
	
	// create lesson beans
	private Map<Long, IndexLessonBean> populateLessonBeans(Integer userId, Integer orgId, List<Integer> roles) 
		throws SQLException, NamingException {
		
		// iterate through user's lessons where they are learner
		Map<Long, IndexLessonBean> map = getLessonsByOrgAndUser(userId, orgId, false);
		for (IndexLessonBean bean : map.values()) {
			List<IndexLinkBean> lessonLinks = new ArrayList<IndexLinkBean>();
			String url = null;
			Integer lessonStateId = bean.getState();
			if (stateId.equals(OrganisationState.ACTIVE)) {
				if (contains(roles, Role.ROLE_LEARNER)) {
					if (lessonStateId.equals(Lesson.STARTED_STATE) || lessonStateId.equals(Lesson.FINISHED_STATE)) {
						url = "javascript:openLearner("+bean.getId()+")";
					}
				}
			} else if (stateId.equals(OrganisationState.ARCHIVED)) {
				if (contains(roles, Role.ROLE_LEARNER)) {
					if (lessonStateId.equals(Lesson.STARTED_STATE) || lessonStateId.equals(Lesson.FINISHED_STATE)) {
						lessonLinks.add(new IndexLinkBean("label.export.portfolio","javascript:openExportPortfolio("+bean.getId()+")"));
					}
				}
			}
			if (lessonLinks.size()>0 || url!=null) {
				bean.setUrl(url);
				bean.setLinks(lessonLinks);
			}
		}
		
		// iterate through user's lessons where they are staff, and add staff links to the beans in the map.
		Map<Long, IndexLessonBean> staffMap = getLessonsByOrgAndUser(userId, orgId, true);
		for (IndexLessonBean bean : staffMap.values()) {
			if (map.containsKey(bean.getId())) {
				bean = map.get(bean.getId());
			}
			List<IndexLinkBean> lessonLinks = bean.getLinks();
			if (lessonLinks == null) lessonLinks = new ArrayList<IndexLinkBean>();
			if (stateId.equals(OrganisationState.ACTIVE)) {
				if (contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles, Role.ROLE_MONITOR)){
					lessonLinks.add(new IndexLinkBean("index.monitor", "javascript:openMonitorLesson(" + bean.getId()+")"));
				}
			} else if (stateId.equals(OrganisationState.ARCHIVED)) {
				if (contains(roles, Role.ROLE_GROUP_MANAGER)) {
					lessonLinks.add(new IndexLinkBean("index.monitor", "javascript:openMonitorLesson(" + bean.getId()+")"));
				}
			}
			if (lessonLinks.size() > 0) {
				bean.setLinks(lessonLinks);
			}
			map.put(bean.getId(), bean);
		}
		
		return map;
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
	
	// get lesson beans where user is learner (if isStaff=true, gets lessons where user is staff)
	private Map<Long, IndexLessonBean> getLessonsByOrgAndUser(Integer userId, Integer orgId, boolean isStaff) 
		throws SQLException, NamingException {
		// TODO refactor using hibernate named query
		String learnerQuery = "select l.lesson_id, l.name, l.description, l.lesson_state_id, lp.lesson_completed_flag "
			+ " from (lams_lesson l, lams_learning_design ld, lams_group g, lams_user_group ug, lams_grouping gi)"
			+ " left join lams_learner_progress lp on lp.user_id=ug.user_id and lp.lesson_id=l.lesson_id"
			+ " where l.learning_design_id=ld.learning_design_id"
			+ " and ld.copy_type_id!=" + LearningDesign.COPY_TYPE_PREVIEW
			+ " and l.organisation_id=?"
			+ " and l.class_grouping_id=g.grouping_id"
			+ " and l.lesson_state_id!=" + Lesson.REMOVED_STATE
			+ " and ug.group_id=g.group_id"
			+ " and ug.user_id=?"
			+ " and gi.grouping_id=g.grouping_id"
			+ " and g.group_id" + (isStaff ? "" : "!") + "=gi.staff_group_id";
		
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:/jdbc/lams-ds");
		Connection conn = null;
		
		HashMap<Long, IndexLessonBean> map = new HashMap<Long, IndexLessonBean>();
		
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement(learnerQuery);
			ps.setInt(1, orgId.intValue());
			ps.setInt(2, userId.intValue());
			ResultSet rs = ps.executeQuery();

			// check if there is any result
			while (rs.next() != false) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				String description = rs.getString(3);
				int state = rs.getInt(4);
				boolean lessonCompleted = rs.getBoolean(5);
				IndexLessonBean bean = new IndexLessonBean(
					new Long(id), name, description, new Integer(state), lessonCompleted
				);
				map.put(new Long(id), bean);
			}

			rs.close();
		} finally {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		}
		
		return map;
	}
}
 
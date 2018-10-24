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


package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Used by main.jsp to persist the ordering of lessonIds when lessons are
 * ordered by a staff member for a group. Currently stores lessonIds as a
 * single comma separated string in lams_organisation.
 *
 * @author jliew
 */
public class LessonOrderServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(LessonOrderServlet.class);
    
    @Autowired
    private UserManagementService userManagementService;
    
    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// even though response is empty, it is needed so Firefox does not show parsing error
	response.setContentType("text/html;charset=utf-8");

	Integer orgId = WebUtil.readIntParam(request, "orgId", false);
	String ids = request.getParameter("ids");

	if (orgId != null && ids != null) {
	    Organisation org = (Organisation) userManagementService.findById(Organisation.class, orgId);

	    if (org != null) {
		// make sure user has permission to sort org lessons
		boolean allowSorting = false;
		List<UserOrganisationRole> userOrganisationRoles = userManagementService.getUserOrganisationRoles(orgId,
			request.getRemoteUser());
		for (UserOrganisationRole userOrganisationRole : userOrganisationRoles) {
		    Integer roleId = userOrganisationRole.getRole().getRoleId();
		    if (roleId.equals(Role.ROLE_GROUP_MANAGER) || roleId.equals(Role.ROLE_MONITOR)) {
			allowSorting = true;
			break;
		    }
		}
		if (!allowSorting) {
		    log.warn("User " + request.getRemoteUser() + " tried to sort lessons in orgId " + orgId);
		    response.sendError(HttpServletResponse.SC_FORBIDDEN);
		    return;
		}

		// make sure we record lesson ids that belong to this org
		List<String> idList = Arrays.asList(ids.split(","));
		List lessons = userManagementService.findByProperty(Lesson.class, "organisation", org);
		for (String id : idList) {
		    try {
			Long l = new Long(Long.parseLong(id));
			if (!contains(lessons, l)) {
			    log.warn("Lesson with id " + l + " doesn't belong in org with id " + orgId);
			    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			    return;
			}
		    } catch (NumberFormatException e) {
			continue;
		    }
		}

		String oldIds = org.getOrderedLessonIds();
		String updatedIds = mergeLessonIds((oldIds != null ? oldIds : ""), ids);
		org.setOrderedLessonIds(updatedIds);
		userManagementService.save(org);
	    }
	}

    }

    private boolean contains(List lessons, Long id) {
	if (lessons != null) {
	    Iterator it = lessons.iterator();
	    while (it.hasNext()) {
		Lesson lesson = (Lesson) it.next();
		if (lesson.getLessonId().equals(id)) {
		    return true;
		}
	    }
	}
	return false;
    }

    // take the updated list and insert elements of the old list that don't exist in it;
    private String mergeLessonIds(String old, String updated) {
	List<String> oldIds = Arrays.asList(old.split(","));
	List<String> updatedIds = Arrays.asList(updated.split(","));
	ArrayList<String> mergedIds = new ArrayList<String>(updatedIds);

	for (int i = 0; i < oldIds.size(); i++) {
	    String current = oldIds.get(i);
	    if (!updatedIds.contains(current)) {
		int indexNextExistingId = indexNextExistingId(i, oldIds, mergedIds);
		if (indexNextExistingId < 0) {
		    mergedIds.add(current);
		} else {
		    mergedIds.add(indexNextExistingId, current);
		}
	    }
	}
	return joinString(mergedIds);
    }

    // find the index of the next lesson Id in the old list that exists in the updated list
    private int indexNextExistingId(int i, List<String> oldIds, List<String> updatedIds) {
	for (int j = i; j < oldIds.size(); j++) {
	    String current = oldIds.get(j);
	    int index = updatedIds.indexOf(current);
	    if (index > -1) {
		return index;
	    }
	}
	return -1;
    }

    // implode strings in list separated by commas
    private String joinString(List<String> list) {
	String s = "";
	if (list != null) {
	    for (String i : list) {
		s += i + ",";
	    }
	    s = s.substring(0, s.length() - 1);
	}
	return s;
    }
}

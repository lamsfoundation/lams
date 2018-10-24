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
 
  
package org.lamsfoundation.bb.integration.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.bb.integration.Constants;
import org.lamsfoundation.bb.integration.util.LamsPluginUtil;
import org.lamsfoundation.bb.integration.util.LamsSecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import blackboard.data.course.Course;
import blackboard.data.course.Group;
import blackboard.data.user.User;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.Id;
import blackboard.persist.PersistenceException;
import blackboard.persist.course.CourseDbLoader;
import blackboard.persist.course.GroupDbLoader;
import blackboard.persist.user.UserDbLoader;
import blackboard.platform.persistence.PersistenceServiceFactory;

/**
 * Fetch groups of the specified course. Serves 2 different types of calls: 1-initial request for group names;
 * 2-sequential request for selected group users.
 * 
 * @author Andrey Balan
 */
public class GroupDataServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(GroupDataServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	try {

	    // get Parameter values
	    String usernameParam = request.getParameter(Constants.PARAM_USER_ID);
	    String tsParam = request.getParameter(Constants.PARAM_TIMESTAMP);
	    String hashParam = request.getParameter(Constants.PARAM_HASH);
	    String courseIdParam = request.getParameter(Constants.PARAM_COURSE_ID);
	    String[] groupIdsParam = request.getParameterValues("extGroupIds");

	    // check paramaeters
	    if (usernameParam == null || tsParam == null || hashParam == null || courseIdParam == null) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing expected parameters");
		return;
	    }

	    String secretKey = LamsPluginUtil.getServerSecretKey();
	    String serverId = LamsPluginUtil.getServerId();

	    if (!LamsSecurityUtil.sha1(
		    tsParam.toLowerCase() + usernameParam.toLowerCase() + serverId.toLowerCase()
			    + secretKey.toLowerCase()).equals(hashParam)) {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "authentication failed");
		return;
	    }

	    // get the persistence manager
	    BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
	    CourseDbLoader cLoader = CourseDbLoader.Default.getInstance();
	    GroupDbLoader groupLoader = (GroupDbLoader) bbPm.getLoader(GroupDbLoader.TYPE);
	    UserDbLoader userDbLoader = (UserDbLoader) bbPm.getLoader(UserDbLoader.TYPE);
//	    Id courseId = bbPm.generateId(Course.DATA_TYPE, courseIdParam);
	    
	    //create JSON objects to return
	    Course course = cLoader.loadByCourseId(courseIdParam);
	    List<Group> groups = groupLoader.loadAvailableByCourseId(course.getId());
	    JsonArray jsonGroups = new JsonArray();
	    
	    //in case groupIds is supplied - it means we need to provide groups along with all users
	    if (groupIdsParam != null && groupIdsParam.length > 0) {
		
		for (String groupIdParam : groupIdsParam) {
		    for (Group group : groups) {
			//only groups with ids requested by LAMS should be processed 
			Id groupId = group.getId();
			if (!groupId.toExternalString().equals(groupIdParam)) {
			    continue;
			}

			JsonObject jsonGroup = new JsonObject();
			jsonGroup.addProperty("groupId", groupId.toExternalString());
			jsonGroup.addProperty("groupName", group.getTitle());
			jsonGroups.add(jsonGroup);

			JsonArray jsonUsers = new JsonArray();
			jsonGroup.add("users", jsonUsers);

			List<User> users = userDbLoader.loadByGroupId(groupId, null, true);
			for (User user : users) {
			    JsonObject jsonUser = new JsonObject();
			    jsonUsers.add(jsonUser);

			    jsonUser.addProperty("userName", user.getUserName());

			    // The CSV list should be the format below
			    // <Title>,<First name>,<Last name>,<Address>,<City>,<State>,
			    // <Postcode>,<Country ISO code>,<Day time number>,<Mobile number>,
			    // <Fax number>,<Email>,<Locale>
			    jsonUser.addProperty("1", user.getTitle());
			    jsonUser.addProperty("2", user.getGivenName());
			    jsonUser.addProperty("3", user.getFamilyName());
			    jsonUser.addProperty("4", user.getStreet1() + user.getStreet2());
			    jsonUser.addProperty("5", user.getCity());
			    jsonUser.addProperty("6", user.getState());
			    jsonUser.addProperty("7", user.getZipCode());
			    jsonUser.addProperty("8", LamsSecurityUtil.getCountryCode(user.getCountry()));
			    jsonUser.addProperty("9", user.getHomePhone1());
			    jsonUser.addProperty("10", user.getMobilePhone());
			    jsonUser.addProperty("11", user.getBusinessFax());
			    jsonUser.addProperty("12", user.getEmailAddress());
			    jsonUser.addProperty("13", user.getLocale());
			}
		    }
		}
	    } else {
		for (Group group : groups) {
		    Id groupId = group.getId();
		    JsonObject jsonGroup = new JsonObject();
		    jsonGroup.addProperty("groupId", groupId.toExternalString());
		    jsonGroup.addProperty("groupName", group.getTitle());
		    jsonGroup.addProperty("groupSize", group.getGroupMemberships().size());
		    jsonGroups.add(jsonGroup);

		}
	    }

	    response.getWriter().write(jsonGroups.toString());

	} catch (PersistenceException e) {
	    throw new ServletException("Failed to fetch course's groups", e);
	}
    }

}


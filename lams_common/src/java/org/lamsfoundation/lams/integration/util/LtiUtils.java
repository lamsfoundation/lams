package org.lamsfoundation.lams.integration.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.lamsfoundation.lams.integration.ExtServer;

public class LtiUtils {

    public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    public static final String LTI_MESSAGE_TYPE_CONTENTITEMSELECTIONREQUEST = "ContentItemSelectionRequest";
    public static final String CONTENT_ITEM_RETURN_URL = "content_item_return_url";
    
    /**
     * Return <code>true</code> if the user is an administrator.
     * {Method added by LAMS}
     *
     * @return <code>true</code> if the user has a role of administrator
     */
    public static boolean isAdmin(String roles) {
	List<String> rolesToSearchFor = new LinkedList<String>();
	rolesToSearchFor.add("urn:lti:role:ims/lis/Administrator");
	rolesToSearchFor.add("urn:lti:sysrole:ims/lis/SysAdmin");
	rolesToSearchFor.add("urn:lti:sysrole:ims/lis/Administrator");
	rolesToSearchFor.add("urn:lti:instrole:ims/lis/Administrator");
	
	return hasRole(roles, rolesToSearchFor);
    }

    /**
     * Return <code>true</code> if the user is staff.
     * {Method added by LAMS}
     *
     * @return <code>true</code> if the user has a role of instructor, contentdeveloper or teachingassistant
     */
    public static boolean isStaff(String roles, ExtServer extServer) {
	List<String> rolesToSearchFor = new LinkedList<String>();
	rolesToSearchFor.add("urn:lti:role:ims/lis/Instructor");
	rolesToSearchFor.add("urn:lti:instrole:ims/lis/Instructor");
	rolesToSearchFor.add("urn:lti:role:ims/lis/ContentDeveloper");
	rolesToSearchFor.add("urn:lti:role:ims/lis/TeachingAssistant");
	
	String toolConsumerMonitorRoles = extServer.getLtiToolConsumerMonitorRoles();
	if (toolConsumerMonitorRoles != null) {
	    rolesToSearchFor.addAll(Arrays.asList(toolConsumerMonitorRoles.split(",")));
	}
	
	return hasRole(roles, rolesToSearchFor);
    }
    
    public static boolean isToolConsumerCustomRole(String roles, String toolConsumerCustomRoles) {
	if (roles == null || toolConsumerCustomRoles == null) {
	    return false;
	}
	
	List<String> rolesToSearchFor = Arrays.asList(toolConsumerCustomRoles.split(","));
	return hasRole(roles, rolesToSearchFor);
    }

    /**
     * Return <code>true</code> if the user is a learner.
     * {Method added by LAMS}
     *
     * @return <code>true</code> if the user has a role of learner
     */
    public static boolean isLearner(String roles) {
	List<String> rolesToSearchFor = new LinkedList<String>();
	rolesToSearchFor.add("urn:lti:role:ims/lis/Learner");
	
	return hasRole(roles, rolesToSearchFor);
    }

    /*
     * Check whether the user has a specified role name.
     * {Method added by LAMS}
     *
     * @param role
     *            Name of role
     *
     * @return <code>true</code> if the user has the specified role
     */
    private static boolean hasRole(String roles, List<String> rolesToSearchFor) {
	String[] roleArray = roles.split(",");
	
	boolean hasRole = false;
	for (String role : roleArray) {
	    for (String roleToSearchFor : rolesToSearchFor) {
		if (role.equals(roleToSearchFor)) {
		    hasRole = true;
		    break;
		}
	    }
	}

	return hasRole;
    }

}

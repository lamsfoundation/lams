package org.lamsfoundation.lams.web.filter;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Logs requests to controllers with their parameters
 *
 * @author Marcin Cieslak
 */
public class AuditLogFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(AuditLogFilter.class);

    // paths that have more accurate logs built in business logic
    // or just need to be ignored
    private static final Set<String> IGNORED_PATHS = Set.of("/lams/admin/userorgsave.do",
	    "/lams/admin/userorgrolesave.do", "/lams/admin/userrolessave.do");

    public static final String CALL_ACTION = "endpoint_call";
    public static final String CONFIG_CHANGE_ACTION = "configuration_change";
    public static final String TIMEZONE_CHANGE_ACTION = "timezone_change";
    public static final String ACCOUNT_LOCKED_ACTION = "account_lock";

    public static final String TOOL_ENABLED_ACTION = "tool_enable_action";
    public static final String TOOL_DISABLE_ACTION = "tool_disable_action";

    public static final String USER_ADD_ACTION = "user_add";
    public static final String USER_DELETE_ACTION = "user_delete";
    public static final String USER_EDIT_ACTION = "user_edit";
    public static final String USER_ENABLE_ACTION = "user_enable";
    public static final String USER_DISABLE_ACTION = "user_disable";
    public static final String USER_BULK_ADD_ACTION = "user_bulk_add";

    public static final String ROLE_CHECK_ACTION = "role_check";
    public static final String ROLE_ADD_ACTION = "org_role_add";
    public static final String USER_ENROLL_ACTION = "user_enroll";
    public static final String USER_UNENROLL_ACTION = "user_unenroll";

    public static final String ORGANISATION_ADD_ACTION = "org_add";
    public static final String ORGANISATION_STATE_CHANGE_ACTION = "org_state_change";
    public static final String ORGANISATION_EDIT_ACTION = "org_edit";

    public static final String LESSON_CLONE_ACTION = "lesson_clone";
    public static final String LESSON_REMOVE_PERMAMENTLY_ACTION = "lesson_remove_perm";

    public static final String INTEGRATED_SERVER_ADD_ACTION = "integrated_server_add";
    public static final String INTEGRATED_SERVER_DELETE_ACTION = "integrated_server_delete";
    public static final String INTEGRATED_SERVER_DISABLE_ACTION = "integrated_server_disable";
    public static final String INTEGRATED_SERVER_ENABLE_ACTION = "integrated_server_enable";
    public static final String INTEGRATED_SERVER_EDIT_ACTION = "integrated_server_edit";

    public static final String LTI_INTEGRATED_SERVER_ADD_ACTION = "lti_integrated_server_add";
    public static final String LTI_INTEGRATED_SERVER_DELETE_ACTION = "lti_integrated_server_delete";
    public static final String LTI_INTEGRATED_SERVER_DISABLE_ACTION = "lti_integrated_server_disable";
    public static final String LTI_INTEGRATED_SERVER_ENABLE_ACTION = "lti_integrated_server_enable";
    public static final String LTI_INTEGRATED_SERVER_EDIT_ACTION = "lti_integrated_server_edit";

    public static final String POLICY_ADD_ACTION = "policy_add";
    public static final String POLICY_ENABLE_ACTION = "policy_enable";
    public static final String POLICY_DISABLE_ACTION = "policy_disable";

    public static final String SIGNUP_PAGE_ADD_ACTION = "signup_page_add";
    public static final String SIGNUP_PAGE_DELETE_ACTION = "signup_page_delete";

    @Override
    public final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	try {

	    String calledPath = request.getRequestURI();
	    if (!IGNORED_PATHS.contains(calledPath)) {
		StringBuilder logMessageBuilder = new StringBuilder();

		// what path was called
		logMessageBuilder.append("called ").append(request.getRequestURI());

		// optional parameters
		if (!request.getParameterMap().isEmpty()) {
		    logMessageBuilder.append(" with parameters: ");
		    for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			String key = entry.getKey();
			// skip CSRF parameter
			if ("OWASP-CSRFGUARD".equals(key)) {
			    continue;
			}

			// print out all parameters with their values
			logMessageBuilder.append(key).append("=[");
			String[] values = entry.getValue();
			if (values != null && values.length > 0) {
			    for (String value : entry.getValue()) {
				logMessageBuilder.append(value.replaceAll("(?:\\n|\\r)", "")).append(", ");
			    }
			    logMessageBuilder.delete(logMessageBuilder.length() - 2, logMessageBuilder.length());
			}
			logMessageBuilder.append("], ");
		    }
		    logMessageBuilder.delete(logMessageBuilder.length() - 2, logMessageBuilder.length());
		}

		AuditLogFilter.log(CALL_ACTION, logMessageBuilder);
	    }
	} catch (Exception e) {
	    logger.error("Exception while logging to audit log", e);
	}

	chain.doFilter(request, response);
    }

    // utility methods to call from controllers

    public static final void log(String action, CharSequence message) {
	// look for user information
	UserDTO user = AuditLogFilter.getUserDto();
	AuditLogFilter.log(user, action, message);
    }

    public static final void log(UserDTO user, String action, CharSequence message) {
	if (user == null) {
	    AuditLogFilter.log(null, null, null, action, message);
	} else {
	    AuditLogFilter.log(user.getLogin(), user.getFirstName(), user.getLastName(), action, message);
	}
    }

    public static final void log(String login, String firstName, String lastName, String action, CharSequence message) {
	StringBuilder logMessageBuilder = new StringBuilder("| ");

	if (StringUtils.isBlank(login)) {
	    logMessageBuilder.append("Unauthenticated user | ");
	} else {
	    if (StringUtils.isNotBlank(firstName)) {
		logMessageBuilder.append(firstName).append(" ");
	    }
	    if (StringUtils.isNotBlank(lastName)) {
		logMessageBuilder.append(lastName).append(" ");
	    }
	    logMessageBuilder.append("(").append(login).append(") | ");
	}

	logMessageBuilder.append(action).append(" | ").append(message);
	logger.info(logMessageBuilder);
    }

    private static final UserDTO getUserDto() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}
package org.lamsfoundation.lams.web.filter;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

		AuditLogFilter.log(logMessageBuilder);
	    }
	} catch (Exception e) {
	    logger.error("Exception while logging to audit log", e);
	}

	chain.doFilter(request, response);
    }

    // utility methods to call from controllers

    public static final void log(CharSequence message) {
	// look for user information
	UserDTO user = AuditLogFilter.getUserDto();
	AuditLogFilter.log(user.getUserID(), user.getLogin(), message);
    }

    public static final void log(Integer userId, String userName, CharSequence message) {
	StringBuilder logMessageBuilder = new StringBuilder();

	if (userId == null) {
	    logMessageBuilder.append("Unauthenticated user ");
	} else {
	    logMessageBuilder.append("\"").append(userName).append("\" (").append(userId).append(") ");
	}
	logMessageBuilder.append(message);

	logger.info(logMessageBuilder);
    }

    private static final UserDTO getUserDto() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}

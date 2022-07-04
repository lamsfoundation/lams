package org.lamsfoundation.lams.web.filter;

import java.io.IOException;
import java.util.Map.Entry;

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
public class SecurityLogFilter extends OncePerRequestFilter {

    private static final Logger log = Logger.getLogger(SecurityLogFilter.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	try {
	    // look for user information
	    UserDTO user = SecurityLogFilter.getUserDto();
	    StringBuilder logMessageBuilder = new StringBuilder();
	    if (user == null) {
		logMessageBuilder.append("Unauthenticated user ");
	    } else {
		logMessageBuilder.append("\"").append(user.getLogin()).append("\" (").append(user.getUserID())
			.append(") ");
	    }

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
			    logMessageBuilder.append(value).append(", ");
			}
			logMessageBuilder.delete(logMessageBuilder.length() - 2, logMessageBuilder.length());
		    }
		    logMessageBuilder.append("], ");
		}
		logMessageBuilder.delete(logMessageBuilder.length() - 2, logMessageBuilder.length());
	    }

	    log.info(logMessageBuilder);
	} catch (Exception e) {
	    log.error("Exception while logging to security log", e);
	}

	chain.doFilter(request, response);
    }

    private static UserDTO getUserDto() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}
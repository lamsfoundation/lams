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
/* $$Id$$ */
package org.lamsfoundation.lams.web.filter;

import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter request to preferred locale according to user option in database, client browser locale or default locale. The
 * proity is user setting in database is highest, then browser locale, then the default locale.
 * <p>
 * <b>NOTICE: This filter must set after <code>org.lamsfoundation.lams.web.session.SystemSessionFilter</code> in
 * web.xml because it need get value from SystemSession .</b>
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class LocaleFilter extends OncePerRequestFilter {

    // private static Logger log = Logger.getLogger(LocaleFilter.class);
    private String encoding;

    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE";
    /** Key used in request to get the required direction. Used by the HTML tag */
    public static final String DIRECTION = "page_direction";

    /**
     * Set the encoding to use for requests. This encoding will be passed into a ServletRequest.setCharacterEncoding
     * call.
     */
    public void setEncoding(String encoding) {
	this.encoding = encoding;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	// charset encoding
	if (!StringUtils.isEmpty(encoding)) {
	    request.setCharacterEncoding(encoding);
	} else {
	    request.setCharacterEncoding("UTF-8");
	}

	Locale preferredLocale = null;
	String direction = null;
	TimeZone tz = null;
	// Comment: This getParameter() cause problem when reading WDDX packet, which need request.getInputStream()
	// method.
	// user set has first prority:
	// String locale = request.getParameter("locale");
	// if (locale != null)
	// preferredLocale = new Locale(locale);

	// if request does not assign locale, then get it from database
	if (preferredLocale == null) {
	    HttpSession sharedsession = SessionManager.getSession();
	    if (sharedsession != null) {
		UserDTO user = (UserDTO) sharedsession.getAttribute(AttributeNames.USER);
		if (user != null) {
		    direction = user.getDirection();
		    tz = user.getTimeZone();
		    String lang = user.getLocaleLanguage();
		    String country = user.getLocaleCountry();
		    // would prefer both the language and country but that's not always feasible.
		    // so we may end up with some confusing situations.
		    if (!StringUtils.isEmpty(lang)) {
			preferredLocale = new Locale(lang, country != null ? country : "");
		    }
		}
	    }
	}
	if (preferredLocale == null) {
	    // if request does not have, set it default then.
	    String defaults[] = LanguageUtil.getDefaultLangCountry();
	    preferredLocale = new Locale(defaults[0] != null ? defaults[0] : "",
		    defaults[1] != null ? defaults[1] : "");

	}

	if (direction == null) {
	    direction = LanguageUtil.getDefaultDirection();
	}
	if (tz == null) {
	    LanguageUtil.getDefaultTimeZone();
	}

	HttpSession session = request.getSession(false);
	// set locale for STURTS and JSTL
	// set the time zone - must be set for dates to display the time zone
	if (session != null) {
	    if (preferredLocale != null) {
		session.setAttribute(LocaleFilter.PREFERRED_LOCALE_KEY, preferredLocale);
		Config.set(session, Config.FMT_LOCALE, preferredLocale);
		session.setAttribute(LocaleFilter.DIRECTION, direction);
	    }
	    Config.set(session, Config.FMT_TIME_ZONE, tz);
	}
	if (preferredLocale != null && !(request instanceof LocaleRequestWrapper)) {
	    request = new LocaleRequestWrapper(request, preferredLocale);
	    LocaleContextHolder.setLocale(preferredLocale);
	}

	if (chain != null) {
	    chain.doFilter(request, response);
	}

	// Reset thread-bound LocaleContext.
	LocaleContextHolder.setLocaleContext(null);
    }

}

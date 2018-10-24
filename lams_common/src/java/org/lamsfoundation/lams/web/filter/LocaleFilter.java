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
 * prority is user setting in database is highest, then browser locale, then the default locale.
 * <p>
 * <b>This filter must set after <code>org.lamsfoundation.lams.web.session.SystemSessionFilter</code> in web.xml because
 * it need get value from SystemSession .</b>
 *
 * @author Steve.Ni, Marcin Cieslak
 */
public class LocaleFilter extends OncePerRequestFilter {

    public static final String PREFERRED_LOCALE_KEY = "preferredLocale";
    /** Key used in request to get the required direction. Used by the HTML tag */
    public static final String DIRECTION = "page_direction";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	// setting encoding in standalone.xml does not do the trick (yet?), so set it here
	request.setCharacterEncoding("UTF-8");

	Locale preferredLocale = null;
	String direction = null;
	TimeZone tz = null;

	// 1. get locale from user settings
	HttpSession session = SessionManager.getSession();
	if (session != null) {
	    UserDTO user = (UserDTO) session.getAttribute(AttributeNames.USER);
	    if (user != null) {
		direction = user.getDirection();
		tz = user.getTimeZone();
		String lang = user.getLocaleLanguage();
		String country = user.getLocaleCountry();
		// would prefer both the language and country but that's not always feasible.
		// so we may end up with some confusing situations.
		if (!StringUtils.isEmpty(lang)) {
		    preferredLocale = new Locale(lang, country == null ? "" : country);
		}
	    }
	}
	// 2. get default locale from client's browser.
	// 3. get server's default locale
	// 4. fall back to "en_AU"
	if (preferredLocale == null) {
	    Locale browserLocale = request.getLocale();
	    preferredLocale = LanguageUtil.getSupportedLocaleByNameOrLanguageCode(browserLocale);
	}

	if (direction == null) {
	    direction = LanguageUtil.getDefaultDirection();
	}
	if (tz == null) {
	    LanguageUtil.getDefaultTimeZone();
	}

	// set locale for STURTS and JSTL
	// set the time zone - must be set for dates to display the time zone
	if (session != null) {
	    session.setAttribute(LocaleFilter.PREFERRED_LOCALE_KEY, preferredLocale);
	    session.setAttribute(LocaleFilter.DIRECTION, direction);
	    Config.set(session, Config.FMT_LOCALE, preferredLocale);
	    Config.set(session, Config.FMT_TIME_ZONE, tz);
	}
	if (!(request instanceof LocaleRequestWrapper)) {
	    request = new LocaleRequestWrapper(request, preferredLocale);
	    LocaleContextHolder.setLocale(preferredLocale);
	}

	chain.doFilter(request, response);

	// Reset thread-bound LocaleContext.
	LocaleContextHolder.setLocaleContext(null);
    }
}
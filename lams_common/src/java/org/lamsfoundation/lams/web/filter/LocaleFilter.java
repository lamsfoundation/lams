/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.web.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * Filter request to preferred locale according to user option in database, client browser locale or default locale.
 * The proity is user setting in database is highest, then browser locale, then the default locale. 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class LocaleFilter extends OncePerRequestFilter {

	private static final String DEFAULT_LANGUAGE = "en";
	private static final String DEFUALT_COUNTRY = "AU";
	private static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE";

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
            FilterChain chain) throws IOException, ServletException {
    	Locale preferredLocale = null;

		HttpSession sharedsession = SessionManager.getSession(); 
		UserDTO user = (UserDTO) sharedsession.getAttribute(AttributeNames.USER);
		if(user != null){
			String lang = user.getLocaleLanguage();
			String country = user.getLocaleCountry();
			//whatever user set lang or country, then try to use it.
			if(lang != null || country!= null){
				if(lang == null)
					lang = DEFAULT_LANGUAGE;
				if(country == null)
					country = DEFUALT_COUNTRY;
				preferredLocale = new Locale(lang,country);
			}
		}else{
			//user does not set any locale information, try to get from request
			String locale = request.getParameter("locale");
	        if (locale != null) {
	            preferredLocale = new Locale(locale);
	        }else
	        	//if request does not have, set it default then.
	        	preferredLocale = new Locale(DEFAULT_LANGUAGE,DEFUALT_COUNTRY);
		}
		
        HttpSession session = request.getSession(false);
        //set locale for STURTS and JSTL
        if (session != null) {
            if (preferredLocale == null) {
                preferredLocale = (Locale) session.getAttribute(PREFERRED_LOCALE_KEY);
            } else {
                session.setAttribute(PREFERRED_LOCALE_KEY, preferredLocale);
                Config.set(session, Config.FMT_LOCALE, preferredLocale);
            }
        }
        if (preferredLocale != null && !(request instanceof LocaleRequestWrapper)) {
            request = new LocaleRequestWrapper(request, preferredLocale);
            LocaleContextHolder.setLocale(preferredLocale);
        }

        chain.doFilter(request, response);
        
        // Reset thread-bound LocaleContext.
        LocaleContextHolder.setLocaleContext(null);
	}


}

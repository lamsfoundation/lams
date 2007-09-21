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
package org.lamsfoundation.lams.util; 

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
 
/**
 * @author jliew
 *
 */
public class LangUtil {

	private static IUserManagementService service;
	
	private static IUserManagementService getService() {
		if (service == null) {
			WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(HttpSessionManager.getInstance().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementService");
		}
		return service;
	}
	
	/*
	 * Returns server default locale.
	 */
	public static SupportedLocale getDefaultLocale() {
		String localeName = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
		return getSupportedLocale(localeName.substring(0,2),localeName.substring(3));
	}
	
	/*
	 * Searches for a locale based on language, then country, matching the single input string.
	 * Otherwise returns server default locale.
	 */
	public static SupportedLocale getSupportedLocale(String input) {
    	List list = getService().findByProperty(SupportedLocale.class, "languageIsoCode", input);
    	if (list!=null && list.size()>0) {
    		return (SupportedLocale)list.get(0);
    	} else {
    		list = getService().findByProperty(SupportedLocale.class, "countryIsoCode", input);
    		if (list!=null && list.size()>0) {
    			return (SupportedLocale)list.get(0);
    		}
    	}
    	return getDefaultLocale();
    }
    
	/*
	 * Finds a locale based on language and/or country.
	 */
	public static SupportedLocale getSupportedLocale(String langIsoCode, String countryIsoCode) {
		SupportedLocale locale = null;
		Map<String, Object> properties = new HashMap<String, Object>();
		if(countryIsoCode.trim().length()>0 && langIsoCode.trim().length()>0){
			properties.put("languageIsoCode", langIsoCode);
			properties.put("countryIsoCode", countryIsoCode);
		}else if(langIsoCode.trim().length()>0){
			properties.put("languageIsoCode", langIsoCode);
		}else if(countryIsoCode.trim().length()>0){
			properties.put("countryIsoCode", countryIsoCode);
		}
		List list = getService().findByProperties(SupportedLocale.class, properties);
		if(list!=null && list.size()>0){
			Collections.sort(list);
			locale = (SupportedLocale)list.get(0);
		}else{
			locale = getDefaultLocale();
		}
		return locale;
	}
}
 
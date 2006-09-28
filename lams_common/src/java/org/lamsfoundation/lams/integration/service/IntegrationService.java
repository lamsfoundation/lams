/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.integration.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServerOrgMap;
import org.lamsfoundation.lams.integration.ExtUserUseridMap;
import org.lamsfoundation.lams.integration.UserInfoFetchException;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.CSVUtil;
import org.lamsfoundation.lams.util.HashUtil;

/**
 * <p>
 * <a href="IntegrationService.java.html"><i>View Source</i><a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class IntegrationService implements IIntegrationService{
	
	private static Logger log = Logger.getLogger(IntegrationService.class);
	
	private IUserManagementService service;
	
	public IUserManagementService getService() {
		return service;
	}

	public void setService(IUserManagementService service) {
		this.service = service;
	}

	public ExtServerOrgMap getExtServerOrgMap(String serverId) {
		List list = service.findByProperty(ExtServerOrgMap.class, "serverid", serverId);
		if(list==null || list.size()==0){
			return null;
		}else{
			return (ExtServerOrgMap)list.get(0);
		}
	}
	
	public ExtCourseClassMap getExtCourseClassMap(ExtServerOrgMap serverMap, ExtUserUseridMap userMap, String extCourseId, String countryIsoCode, String langIsoCode){
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("courseid", extCourseId);
		properties.put("extServerOrgMap.sid", serverMap.getSid());
		List list = service.findByProperties(ExtCourseClassMap.class, properties);
		if(list==null || list.size()==0){
			return createExtCourseClassMap(serverMap, userMap.getUser(), extCourseId, countryIsoCode, langIsoCode);
		}else{
			ExtCourseClassMap map = (ExtCourseClassMap)list.get(0);
			User user = userMap.getUser();
			Organisation org = map.getOrganisation();
			if(service.getUserOrganisation(user.getUserId(), org.getOrganisationId())==null){
				addMemberships(user, org);
			}
			return map;
		}
		
	}
	
	private void addMemberships(User user, Organisation org){
		UserOrganisation uo = new UserOrganisation(user,org);
		service.save(uo);
		Integer[] roles = new Integer[]{Role.ROLE_AUTHOR, Role.ROLE_MONITOR, Role.ROLE_COURSE_MANAGER, Role.ROLE_LEARNER};
		for(Integer roleId:roles){
			UserOrganisationRole uor = new UserOrganisationRole(uo,(Role)service.findById(Role.class,roleId));
			service.save(uor);
			uo.addUserOrganisationRole(uor);
		}
		user.addUserOrganisation(uo);
		service.save(user);
	}

	public ExtUserUseridMap getExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername) throws UserInfoFetchException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("extServerOrgMap.sid", serverMap.getSid());
		properties.put("extUsername", extUsername);
		List list = service.findByProperties(ExtUserUseridMap.class, properties);
		if(list==null || list.size()==0){
			return createExtUserUseridMap(serverMap, extUsername);
		}else{
			return (ExtUserUseridMap)list.get(0);
		}
	}

	private ExtCourseClassMap createExtCourseClassMap(ExtServerOrgMap serverMap, User user, String extCourseId, String countryIsoCode, String langIsoCode) {
		Organisation org = new Organisation();
		org.setName(buildName(serverMap.getPrefix(), extCourseId));
		org.setCode(extCourseId);
		org.setParentOrganisation(serverMap.getOrganisation());
		org.setOrganisationType((OrganisationType)service.findById(OrganisationType.class,OrganisationType.COURSE_TYPE));
		org.setOrganisationState((OrganisationState)service.findById(OrganisationState.class,OrganisationState.ACTIVE));
		org.setLocale(getLocale(countryIsoCode, langIsoCode));
		service.saveOrganisation(org, user.getUserId());
		addMemberships(user,org);
		ExtCourseClassMap map = new ExtCourseClassMap();
		map.setCourseid(extCourseId);
		map.setExtServerOrgMap(serverMap);
		map.setOrganisation(org);
		service.save(map);
		return map;
	}

	private SupportedLocale getLocale(String countryIsoCode, String langIsoCode) {
		SupportedLocale locale = null;
		if(countryIsoCode.trim().length()>0 && langIsoCode.trim().length()>0){
			locale = service.getSupportedLocale(countryIsoCode, langIsoCode);
		}else if(langIsoCode.trim().length()>0){
			List list = service.findByProperty(SupportedLocale.class, "languageIsoCode", langIsoCode);
			if(list!=null && list.size()>0){
				locale = (SupportedLocale)list.get(0);
			}
		}else if(countryIsoCode.trim().length()>0){
			List list = service.findByProperty(SupportedLocale.class, "countryIsoCode", countryIsoCode);
			if(list!=null && list.size()>0){
				locale = (SupportedLocale)list.get(0);
			}
		}
		if(locale==null){
			String defaultLocale = Configuration.get(ConfigurationKeys.SERVER_LANGUAGE);
			locale = service.getSupportedLocale(defaultLocale.substring(0,2), defaultLocale.substring(3));
		}
		return locale;
	}

	private ExtUserUseridMap createExtUserUseridMap(ExtServerOrgMap serverMap, String extUsername) throws UserInfoFetchException {
		String[] userData = getUserDataFromExtServer(serverMap, extUsername);
		User user = new User();
		user.setLogin(buildName(serverMap.getPrefix(),extUsername));
		user.setPassword(HashUtil.sha1(RandomPasswordGenerator.nextPassword(10)));
		user.setTitle(userData[0]);
        user.setFirstName(userData[1]);
        user.setLastName(userData[2]);
        user.setAddressLine1(userData[3]);
        user.setCity(userData[4]);
        user.setState(userData[5]);
        user.setPostcode(userData[6]);
        user.setCountry(userData[7]);
        user.setDayPhone(userData[8]);
        user.setMobilePhone(userData[9]);
        user.setFax(userData[10]);
        user.setEmail(userData[11]);
        user.setAuthenticationMethod((AuthenticationMethod)service.findById(AuthenticationMethod.class, AuthenticationMethod.DB));
        user.setCreateDate(new Date());
        user.setDisabledFlag(false);
        user.setLocale(getLocale(userData[12],userData[13]));
		String flashName = Configuration.get(ConfigurationKeys.DEFAULT_FLASH_THEME);
		List list = service.findByProperty(CSSThemeVisualElement.class, "name", flashName);
		if (list!=null && list.size()>0) {
			CSSThemeVisualElement flashTheme = (CSSThemeVisualElement)list.get(0);
			user.setFlashTheme(flashTheme);
		}
		String htmlName = Configuration.get(ConfigurationKeys.DEFAULT_HTML_THEME);
		list = getService().findByProperty(CSSThemeVisualElement.class, "name", htmlName);
		if (list!=null && list.size()>0) {
			CSSThemeVisualElement htmlTheme = (CSSThemeVisualElement)list.get(0);
			user.setHtmlTheme(htmlTheme);
		}
		service.save(user);
		ExtUserUseridMap map = new ExtUserUseridMap();
		map.setExtServerOrgMap(serverMap);
		map.setExtUsername(extUsername);
		map.setUser(user);
		service.save(map);
		return map;
	}

	private String[] getUserDataFromExtServer(ExtServerOrgMap serverMap, String extUsername) throws UserInfoFetchException {
        //the callback url must contain %username%, %timestamp% and %hash%
        //eg: "http://test100.ics.mq.edu.au/webapps/lams-plglamscontent-bb_bb60/UserData?uid=%username%&ts=%timestamp%&hash=%hash%";
        //where %username%, %timestamp% and %hash% will be replaced with their real values
        try {
			String userDataCallbackUrl = serverMap.getUserinfoUrl();
			String timestamp = Long.toString(new Date().getTime());
			String hash = hash(serverMap, extUsername, timestamp);
			
			//set the values for the parameters
			userDataCallbackUrl = userDataCallbackUrl.replaceAll("%username%", extUsername).replaceAll("%timestamp%", timestamp).replaceAll("%hash%", hash);
			log.debug(userDataCallbackUrl);
			URL url = new URL(userDataCallbackUrl);
			URLConnection conn = url.openConnection();
			if (!(conn instanceof HttpURLConnection))
			    throw new UserInfoFetchException("Fail to fetch user data from external server:"
			            + serverMap.getServerid() + "- Invalid connection type");

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK)
			    throw new UserInfoFetchException(
			            "Fail to fetch user data from external server:"
			                    + serverMap.getServerid()
			                    + " - Unexpected return HTTP Status:"+httpConn.getResponseCode());

			InputStream is = url.openConnection().getInputStream();
			BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
			String str = isReader.readLine();
			if (str == null) {
			    throw new UserInfoFetchException("Fail to fetch user data from external server:"
			            + serverMap.getServerid()
			            + " - No data returned from external server");
			}

			return CSVUtil.parse(str);

        } catch (MalformedURLException e) {
			log.error(e);
			throw new UserInfoFetchException(e);
		} catch (IOException e) {
			log.error(e);
			throw new UserInfoFetchException(e);
		} catch (ParseException e) {
			log.error(e);
			throw new UserInfoFetchException(e);
		}
	}

	private String hash(ExtServerOrgMap serverMap, String extUsername, String timestamp) {
        String serverId = serverMap.getServerid();
        String serverKey = serverMap.getServerkey();
        String plaintext = timestamp.trim().toLowerCase()+extUsername.trim().toLowerCase()+serverId.trim().toLowerCase()+serverKey.trim().toLowerCase();
		return HashUtil.sha1(plaintext);
	}

	private String buildName(String prefix, String name){
		return prefix+'_'+name;
	}
}
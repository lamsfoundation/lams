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
package org.lamsfoundation.lams.admin.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.config.Registration;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import sun.misc.BASE64Decoder;

/**
 * ConfigAction
 * 
 * @author Mitchell Seaton, edited by Luke Foxton
 */

/**
 * struts doclets
 * 
 * @struts.action path="/register" parameter="method" name="RegisterForm"
 *                input=".register" scope="request" validate="false"
 * @struts.action-forward name="register" path=".register"
 * @struts.action-forward name="sysadmin" path="/sysadminstart.do"
 * @struts.action-forward name="success"
 *                        path="http://lamscommunity.org/registration"
 * @struts.action-forward name="error" path=".error"
 */
public class RegisterAction extends LamsDispatchAction {

    private static final Logger log = Logger.getLogger(RegisterAction.class);
    private static final String LAMS_COMMUNITY_REGISTER_URL = "http://lamscommunity.org/lams/x/registration";
    public static final String LAMS_COMMUNITY_KEY = "17^76iTkqYSywJ73";

    private static IUserManagementService userManagementService;
    private Configuration configurationService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// check permission
	if (!request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("errorName", "RegisterAction");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	userManagementService = AdminServiceProxy.getService(getServlet().getServletContext());

	RegisterForm registerForm = (RegisterForm) form;

	configurationService = getConfiguration();

	Registration reg = configurationService.getRegistration();
	if (reg == null) {
	    reg = new Registration();
	    reg.setPublicDirectory(true);
	}
	updateForm(registerForm, reg);

	
	RegisterDTO registerDTO = new RegisterDTO();

	// Get Server statistics for registration 
	List groups = userManagementService.findByProperty(Organisation.class, "organisationType.organisationTypeId",
		OrganisationType.COURSE_TYPE);
	List subgroups = userManagementService.findByProperty(Organisation.class,
		"organisationType.organisationTypeId", OrganisationType.CLASS_TYPE);

	registerDTO.setGroupNumber(Integer.valueOf(groups.size()));
	registerDTO.setSubgroupNumber(Integer.valueOf(subgroups.size()));
	registerDTO.setSysadminNumber(userManagementService.getCountRoleForSystem(Role.ROLE_SYSADMIN));
	registerDTO.setAdminNumber(userManagementService.getCountRoleForSystem(Role.ROLE_GROUP_ADMIN));
	registerDTO.setAuthorNumber(userManagementService.getCountRoleForSystem(Role.ROLE_AUTHOR));
	registerDTO.setMonitorNumber(userManagementService.getCountRoleForSystem(Role.ROLE_MONITOR));
	registerDTO.setManagerNumber(userManagementService.getCountRoleForSystem(Role.ROLE_GROUP_MANAGER));
	registerDTO.setLearnerNumber(userManagementService.getCountRoleForSystem(Role.ROLE_LEARNER));
	registerDTO.setAuthorAdminNumber(userManagementService.getCountRoleForSystem(Role.ROLE_AUTHOR_ADMIN));
	registerDTO.setUserNumber(Integer.valueOf(userManagementService.findAll(User.class).size()));
	registerDTO.setServerUrl(Configuration.get(ConfigurationKeys.SERVER_URL));
	registerDTO.setServerVersion(Configuration.get(ConfigurationKeys.VERSION));
	registerDTO.setServerBuild(Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER));
	registerDTO.setServerLocale(Configuration.get(ConfigurationKeys.SERVER_LANGUAGE));
	registerDTO.setServerLanguageDate(Configuration.get(ConfigurationKeys.DICTIONARY_DATE_CREATED));
	
	request.setAttribute("registerDTO", registerDTO);

	return mapping.findForward("register");
    }

    @SuppressWarnings("unchecked")
    public void updateForm(RegisterForm registerForm, Registration reg) {
	UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	if (reg.getName() != null) {
	    registerForm.setName(reg.getName());
	} else {
	    registerForm.setName(sysadmin.getFirstName() + " " + sysadmin.getLastName());
	}

	if (reg.getEmail() != null) {
	    registerForm.setEmail(reg.getEmail());
	} else {
	    registerForm.setEmail(sysadmin.getEmail());
	}

	// setting the persistent register values
	registerForm.setSiteName(reg.getSiteName());
	registerForm.setOrganisation(reg.getOrganisation());
	registerForm.setServerCountry(reg.getServerCountry());
	
	registerForm.setPublicDirectory(reg.isPublicDirectory());
	registerForm.setEnableLamsCommunityIntegration(reg.isEnableLamsCommunityIntegration());
    }

    public void updateRegistration(RegisterForm registerForm, Registration reg) {
	reg.setName(registerForm.getName());
	reg.setEmail(registerForm.getEmail());
	reg.setSiteName(registerForm.getSiteName());
	reg.setOrganisation(registerForm.getOrganisation());
	reg.setServerCountry(registerForm.getServerCountry());
	reg.setPublicDirectory(registerForm.isPublicDirectory());
	reg.setEnableLamsCommunityIntegration(registerForm.isEnableLamsCommunityIntegration());
    }

    @SuppressWarnings("unchecked")
    public ActionForward register(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	RegisterForm registerForm = (RegisterForm) form;

	// get the default registration if there is any
	configurationService = getConfiguration();
	Registration reg = configurationService.getRegistration();

	if (reg == null) {
	    reg = new Registration();
	}
	updateRegistration(registerForm, reg);
	
	if (reg.getServerKey() == null) {
	   
	    String url = LAMS_COMMUNITY_REGISTER_URL;

	    HashMap<String, String> params = new HashMap<String, String>();
	    params.put("sitename", URLEncoder.encode(registerForm.getSiteName(), "UTF8"));
	    params.put("organisation", URLEncoder.encode(registerForm.getOrganisation(), "UTF8"));
	    params.put("rname", URLEncoder.encode(registerForm.getName(), "UTF8"));
	    params.put("remail", URLEncoder.encode(registerForm.getEmail(), "UTF8"));
	    params.put("servercountry", URLEncoder.encode(registerForm.getServerCountry(), "UTF8"));
	    params.put("public", "" + registerForm.isPublicDirectory());
	    
	    
	    
	    params.put("serverurl", URLEncoder.encode(Configuration.get(ConfigurationKeys.SERVER_URL), "UTF8"));
	    params.put("serverversion", URLEncoder.encode(Configuration.get(ConfigurationKeys.VERSION), "UTF8"));
	    params.put("serverbuild", URLEncoder.encode(Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER),
		    "UTF8"));
	    params.put("serverlocale", URLEncoder.encode(Configuration.get(ConfigurationKeys.SERVER_LANGUAGE), "UTF8"));
	    params.put("langdate", URLEncoder.encode(Configuration.get(ConfigurationKeys.DICTIONARY_DATE_CREATED),
		    "UTF8"));

	    // Get Server statistics for registration 
	    List groups = userManagementService.findByProperty(Organisation.class,
		    "organisationType.organisationTypeId", OrganisationType.COURSE_TYPE);
	    List subgroups = userManagementService.findByProperty(Organisation.class,
		    "organisationType.organisationTypeId", OrganisationType.CLASS_TYPE);

	    params.put("groupno", Integer.valueOf(groups.size()).toString());
	    params.put("subgroupno", Integer.valueOf(subgroups.size()).toString());
	    params.put("sysadminno", userManagementService.getCountRoleForSystem(Role.ROLE_SYSADMIN).toString());
	    params.put("adminno", userManagementService.getCountRoleForSystem(Role.ROLE_GROUP_ADMIN).toString());
	    params.put("managerno", userManagementService.getCountRoleForSystem(Role.ROLE_GROUP_MANAGER).toString());
	    params.put("authorno", userManagementService.getCountRoleForSystem(Role.ROLE_AUTHOR).toString());
	    params.put("monitorno", userManagementService.getCountRoleForSystem(Role.ROLE_MONITOR).toString());
	    params.put("learnerno", userManagementService.getCountRoleForSystem(Role.ROLE_LEARNER).toString());
	    params.put("authoradminno", userManagementService.getCountRoleForSystem(Role.ROLE_AUTHOR_ADMIN).toString());
	    params.put("userno", Integer.valueOf(userManagementService.findAll(User.class).size()).toString());

	    // make the request to lamscommunity.org
	    InputStream is = WebUtil.getResponseInputStreamFromExternalServer(url, params);
	    BufferedReader isReader = new BufferedReader(new InputStreamReader(is));
	    String str = isReader.readLine();
	    log.debug("Response from lamscommunity: " + str);
	    
	    // get the serverId,serverKey pair result back from lamscommunity
	    if (str!=null)
	    {
		String result[] = str.split(",");
		
		if (result.length == 2 && result[0].equals("success"))
		{
		    String decrypted = decrypt(result[1], LAMS_COMMUNITY_KEY);
		    String decryptedResult[] = decrypted.split(",");
		    reg.setServerKey(decryptedResult[0]);
		    reg.setServerID(decryptedResult[1]);
		    configurationService.saveOrUpdateRegistration(reg);
		    request.setAttribute("successKey", "register.success");
		}
		else
		{
		    request.setAttribute("errorKey", "register.error.registrationFailed");
		}
	    }
	    else
	    {
		request.setAttribute("errorKey", "register.error.noResponseFromLamsCommunity");
	    }
	}

	return unspecified(mapping, registerForm, request, response);
    }

    private Configuration getConfiguration() {
	if (configurationService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    configurationService = (Configuration) ctx.getBean("configurationService");

	}
	return configurationService;
    }
    
    public static String decrypt(String text, String password) throws Exception{
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	
	//setup key
	byte[] keyBytes= new byte[16];
	byte[] b= password.getBytes("UTF-8");
	int len= b.length; 
	if (len > keyBytes.length) len = keyBytes.length;
	System.arraycopy(b, 0, keyBytes, 0, len);
	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
	IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
	cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
	
	BASE64Decoder decoder = new BASE64Decoder();
	byte [] results = cipher.doFinal(decoder.decodeBuffer(text));
	return new String(results,"UTF-8");
    }

}

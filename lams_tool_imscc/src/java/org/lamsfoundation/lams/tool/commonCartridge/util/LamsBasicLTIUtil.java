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


package org.lamsfoundation.lams.tool.commonCartridge.util;

import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.imsglobal.lti.BasicLTIConstants;
import org.imsglobal.lti.BasicLTIUtil;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeConfigItem;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.lamsfoundation.lams.tool.commonCartridge.service.ICommonCartridgeService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Utility code for IMS Basic LTI. This is mostly code to support making and
 * launching BLTI resources.
 */
@SuppressWarnings("deprecation")
public class LamsBasicLTIUtil {

    private static Logger log = Logger.getLogger(LamsBasicLTIUtil.class);

    /**
     * Look at a Placement and come up with the launch urls, and other launch parameters to drive the launch.
     *
     * @param info
     * @param launch
     * @param placement
     * @return
     */
    public static Properties loadFromPlacement(Properties launch, CommonCartridgeItem basicLTIItem) {
	Properties info = new Properties();

	LamsBasicLTIUtil.setProperty(info, "launch_url", basicLTIItem.getLaunchUrl());
	LamsBasicLTIUtil.setProperty(info, "secure_launch_url", basicLTIItem.getSecureLaunchUrl());
	LamsBasicLTIUtil.setProperty(info, "secret", basicLTIItem.getSecret());
	LamsBasicLTIUtil.setProperty(info, "key", basicLTIItem.getKey());
	LamsBasicLTIUtil.setProperty(info, "debug", "false");
	LamsBasicLTIUtil.setProperty(info, "frameheight", new Integer(basicLTIItem.getFrameHeight()).toString());
	LamsBasicLTIUtil.setProperty(info, "newwindow", new Boolean(basicLTIItem.isOpenUrlNewWindow()).toString());
	LamsBasicLTIUtil.setProperty(info, "title", basicLTIItem.getTitle());
	LamsBasicLTIUtil.setProperty(info, BasicLTIUtil.BASICLTI_SUBMIT, basicLTIItem.getButtonText());

	// Pull in and parse the custom parameters
	String customstr = LamsBasicLTIUtil.toNull(basicLTIItem.getCustomStr());
	if (customstr != null) {
	    String[] params = customstr.split("[\n;]");
	    for (int i = 0; i < params.length; i++) {
		String param = params[i];
		if (param == null) {
		    continue;
		}
		if (param.length() < 1) {
		    continue;
		}
		int pos = param.indexOf("=");
		if (pos < 1) {
		    continue;
		}
		if (pos + 1 > param.length()) {
		    continue;
		}
		String key = BasicLTIUtil.mapKeyName(param.substring(0, pos));
		if (key == null) {
		    continue;
		}
		String value = param.substring(pos + 1);
		value = value.trim();
		if (value.length() < 1) {
		    continue;
		}
		if (value == null) {
		    continue;
		}
		LamsBasicLTIUtil.setProperty(info, "custom_" + key, value);
	    }
	}

	if (info.getProperty("launch_url", null) != null || info.getProperty("secure_launch_url", null) != null) {
	    return info;
	}
	return null;
    }

    /**
     * Retrieve the LAMS information about users, etc.
     *
     * @param basicLTIItem
     * @return launch Properties
     */
    public static Properties inititializeLaunchProperties(ICommonCartridgeService service,
	    CommonCartridgeItem basicLTIItem) {
	Properties launchProperties = new Properties();
	// Start setting the Basic LTI parameters
	String resourceLinkId = (basicLTIItem.getUid() != null) ? basicLTIItem.getUid().toString()
		: "" + basicLTIItem.getCreateDate().getTime();
	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.RESOURCE_LINK_ID, resourceLinkId);

	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.RESOURCE_LINK_TITLE, basicLTIItem.getTitle());
	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.RESOURCE_LINK_DESCRIPTION,
		basicLTIItem.getDescription());

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (user != null) {
	    LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.USER_ID, user.getUserID().toString());
	    LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.LAUNCH_PRESENTATION_LOCALE,
		    Configuration.get(ConfigurationKeys.SERVER_LANGUAGE));

	    String isExposeUserName = service.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_NAME)
		    .getConfigValue();
	    if (Boolean.parseBoolean(isExposeUserName)) {
		LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.LIS_PERSON_NAME_GIVEN,
			user.getFirstName());
		LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.LIS_PERSON_NAME_FAMILY,
			user.getLastName());
		LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.LIS_PERSON_NAME_FULL,
			user.getFullName());
	    }

	    String isExposeUserEmail = service.getConfigItem(CommonCartridgeConfigItem.KEY_EXPOSE_USER_EMAIL)
		    .getConfigValue();
	    if (Boolean.parseBoolean(isExposeUserEmail)) {
		LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.LIS_PERSON_CONTACT_EMAIL_PRIMARY,
			user.getEmail());
	    }
	}

	String theRole = "Learner";
	//TODO check if we need instructor
//	if (SecurityService.isSuperUser()) {
//	    theRole = "Instructor";
//	} else if (SiteService.allowUpdateSite(context)) {
//	    theRole = "Instructor";
//	}
	LamsBasicLTIUtil.setProperty(launchProperties, "roles", theRole);

	//TODO check if the next statement is correct and we don't need the following parameters:
	//Lams tools doesn't have info on the context(Lesson) so we can't provide with this info
//	if (site != null) {
//	    String context_type = site.getType();
//	    if (context_type != null && context_type.toLowerCase().contains("course")) {
//		setProperty(props, BasicLTIConstants.CONTEXT_TYPE, BasicLTIConstants.CONTEXT_TYPE_COURSE_SECTION);
//	    }
//	    setProperty(props, BasicLTIConstants.CONTEXT_ID, site.getId());
//	    setProperty(props, BasicLTIConstants.CONTEXT_LABEL, site.getTitle());
//	    setProperty(props, BasicLTIConstants.CONTEXT_TITLE, site.getTitle());
//	    String courseRoster = getExternalRealmId(site.getId());
//	    if (courseRoster != null) {
//		setProperty(props, BasicLTIConstants.LIS_COURSE_OFFERING_SOURCEDID, courseRoster);
//	    }
//	}

	// Get the organizational information
	//TODO if we need this?
	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_GUID,
		Configuration.get(ConfigurationKeys.SERVER_URL));
	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_NAME, "SchoolU");
	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_DESCRIPTION,
		"University of School (LMSng) ");
	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_CONTACT_EMAIL,
		"System.Admin@school.edu");
	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_URL, "");
	LamsBasicLTIUtil.setProperty(launchProperties, BasicLTIConstants.LAUNCH_PRESENTATION_RETURN_URL,
		"launch_presentation_return_url");

	// Let tools know we are coming from Lams
	LamsBasicLTIUtil.setProperty(launchProperties, "ext_lms", "lams");
	return launchProperties;
    }

//    /**
//     * Generate HTML from a descriptor and properties from
//     *
//     * @param descriptor
//     * @param contextId
//     * @param basicLTIItem
//     * @param props
//     * @param messageService
//     * @param service
//     * @return
//     */
//    public static String postLaunchHTML(String descriptor, String contextId, CommonCartridgeItem basicLTIItem,
//	    MessageService messageService, ICommonCartridgeService service) {
//	if (descriptor == null || contextId == null)
//	    return "<p>" + messageService.getMessage("error.descriptor", "Error, missing contextId, resourceid or descriptor") + "</p>";
//
//	// Add user, course, etc to the launch parameters
//	Properties launch = inititializeLaunchProperties(service, basicLTIItem);
//
//	Properties info = new Properties();
//	if (!BasicLTIUtil.parseDescriptor(info, launch, descriptor)) {
//	    return "<p>" + messageService.getMessage("error.badxml.resource", "Error, cannot parse descriptor for resource=")
//		    + basicLTIItem.getUid() + ".</p>";
//	}
//
//	return postLaunchHTML(info, launch, messageService);
//    }

    /**
     * This must return an HTML message as the [0] in the array
     *
     * @param service
     * @param messageService
     * @param basicLTIItem
     * @return
     */
    public static String postLaunchHTML(ICommonCartridgeService service, MessageService messageService,
	    CommonCartridgeItem basicLTIItem) {

	// Add user, course, etc to the launch parameters
	Properties launch = LamsBasicLTIUtil.inititializeLaunchProperties(service, basicLTIItem);

	// Retrieve the launch detail
	Properties info = LamsBasicLTIUtil.loadFromPlacement(launch, basicLTIItem);
	if (info == null) {
	    return "<p>" + messageService.getMessage("error.nolaunch", "Not Configured.") + "</p>";
	}
	return LamsBasicLTIUtil.postLaunchHTML(info, launch, messageService);
    }

    public static String postLaunchHTML(Properties info, Properties launch, MessageService messageService) {

	String launch_url = info.getProperty("secure_launch_url");
	if (launch_url == null) {
	    launch_url = info.getProperty("launch_url");
	}
	if (launch_url == null) {
	    return "<p>" + messageService.getMessage("error.missing", "Not configured") + "</p>";
	}

	String org_guid = info.getProperty(BasicLTIConstants.TOOL_CONSUMER_INSTANCE_GUID);
	String org_desc = info.getProperty(BasicLTIConstants.TOOL_CONSUMER_INSTANCE_DESCRIPTION);
	String org_url = info.getProperty(BasicLTIConstants.LAUNCH_PRESENTATION_RETURN_URL);

	// Look up the LMS-wide secret and key - default key is guid
	String key = LamsBasicLTIUtil.getToolConsumerInfo(launch_url, "key");
	if (key == null) {
	    key = org_guid;
	}
	String secret = LamsBasicLTIUtil.getToolConsumerInfo(launch_url, "secret");

	// Demand key/secret in a pair
	if (key == null || secret == null) {
	    key = null;
	    secret = null;
	}

	// If we do not have LMS-wide info, use the local key/secret
	if (secret == null) {
	    secret = LamsBasicLTIUtil.toNull(info.getProperty("secret"));
	    key = LamsBasicLTIUtil.toNull(info.getProperty("key"));
	}

	// Pull in all of the custom parameters
	for (Object okey : info.keySet()) {
	    String skey = (String) okey;
	    if (!skey.startsWith(BasicLTIConstants.CUSTOM_PREFIX)) {
		continue;
	    }
	    String value = info.getProperty(skey);
	    if (value == null) {
		continue;
	    }
	    LamsBasicLTIUtil.setProperty(launch, skey, value);
	}

	LamsBasicLTIUtil.setProperty(launch, "oauth_callback", "about:blank");
	String buttonLabel = StringUtils.isBlank(info.getProperty(BasicLTIUtil.BASICLTI_SUBMIT))
		? messageService.getMessage("launch.button", "Press to Launch External Tool")
		: info.getProperty(BasicLTIUtil.BASICLTI_SUBMIT);
	LamsBasicLTIUtil.setProperty(launch, BasicLTIUtil.BASICLTI_SUBMIT, buttonLabel);

	// Sanity checks
	if (secret == null) {
	    return "<p>" + messageService.getMessage("error.nosecret", "Error - must have a secret.") + "</p>";
	}
	if (secret != null && key == null) {
	    return "<p>" + messageService.getMessage("error.nokey", "Error - must have a secret and a key.") + "</p>";
	}

	launch = BasicLTIUtil.signProperties(launch, launch_url, "POST", key, secret, org_guid, org_desc, org_url);

	if (launch == null) {
	    return "<p>" + messageService.getMessage("error.sign", "Error signing message.") + "</p>";
	}

	boolean dodebug = LamsBasicLTIUtil.toNull(info.getProperty("debug")) != null;
	String postData = BasicLTIUtil.postLaunchHTML(launch, launch_url, dodebug);
	return postData;
    }

    // To make absolutely sure we never send an XSS, we clean these values
    public static void setProperty(Properties props, String key, String value) {
	if (value == null) {
	    return;
	}
//	value = Web.cleanHtml(value);
	if (value.trim().length() < 1) {
	    return;
	}
	props.setProperty(key, value);
    }

    /**
     * TODO use external service proposed by Ernie
     *
     * Look through a series of secrets from the properties based on the launchUrl
     *
     * @param launchUrl
     * @param data
     *            "key" or "secret"
     * @return
     */

    private static String getToolConsumerInfo(String launchUrl, String data) {
//	String default_data = ServerConfigurationService.getString("basiclti.consumer_instance_" + data, null);
//	URL url = null;
//	try {
//	    url = new URL(launchUrl);
//	} catch (Exception e) {
//	    url = null;
//	}
//	if (url == null)
//	    return default_data;
//	String hostName = url.getHost();
//	if (hostName == null || hostName.length() < 1)
//	    return default_data;
//
//	// Look for the property starting with the full name
//	String org_info = ServerConfigurationService.getString("basiclti.consumer_instance_" + data + "." + hostName,
//		null);
//	if (org_info != null)
//	    return org_info;
//
//	// Look for the property starting with the part name
//	for (int i = 0; i < hostName.length(); i++) {
//	    if (hostName.charAt(i) != '.')
//		continue;
//	    if (i > hostName.length() - 2)
//		continue;
//	    String hostPart = hostName.substring(i + 1);
//	    String propName = "basiclti.consumer_instance_" + data + "." + hostPart;
//	    org_info = ServerConfigurationService.getString(propName, null);
//	    if (org_info != null)
//		return org_info;
//	}
//	return default_data;
	return null;
    }

    public static String toNull(String str) {
	if (str == null) {
	    return null;
	}
	if (str.trim().length() < 1) {
	    return null;
	}
	return str;
    }

}
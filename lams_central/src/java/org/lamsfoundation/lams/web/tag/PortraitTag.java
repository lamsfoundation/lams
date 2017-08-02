/***************************************************************************
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
 * ***********************************************************************/
package org.lamsfoundation.lams.web.tag;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Output a URL to display a user's portrait. If you modify the logic here, change portrait.js too!
 */
public class PortraitTag extends TagSupport {
    private static final long serialVersionUID = -3143529984657965761L;
    private static final Logger log = Logger.getLogger(PortraitTag.class);
    private static final int NUM_COLORS = 7;
    private IUserManagementService userManagementService;

    private static final String CSS_ROUND = " portrait-round";
    private static final String PORTRAIT_VERSION_SUFFIX = " portrait-color-";
    
    private static final String STYLE_SMALL = "small";
    private static final String CSS_SMALL[] = {"portrait-sm", "&version=4"};
    private static final String CSS_GENERIC_SMALL = "portrait-generic-sm";
    private static final String STYLE_MEDIUM = "medium";
    private static final String CSS_MEDIUM[] = {"portrait-md", "&version=3"};
    private static final String CSS_GENERIC_MEDIUM = "portrait-generic-md";
    private static final String STYLE_LARGE = "large";
    private static final String CSS_LARGE[] = {"portrait-lg", "&version=2"};;
    private static final String CSS_GENERIC_LARGE = "portrait-generic-lg";
    private static final String STYLE_XLARGE = "xlarge";
    private static final String CSS_XLARGE[] = {"portrait-xl", "&version=1"};
    private static final String CSS_GENERIC_XLARGE = "portrait-generic-xl";

    /* Attributes */
    private String userId = null;
    private String size = null;
    private String round = null;

    public PortraitTag() {
	super();
    }

    @Override
    public int doStartTag() throws JspException {

	String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
	serverURL = serverURL == null ? null : serverURL.trim();

	try {
	    String code = null;
	    boolean isRound = (getRound() != null ? Boolean.getBoolean(getRound()) : true);
	    if (userId != null && userId.length() > 0) {
		HashMap<String, String> cache = getPortraitCache();
		code = cache.get(userId);
		if (code == null) {
		    Integer userIdLong = Integer.decode(userId);
		    User user = (User) getUserManagementService().findById(User.class, userIdLong);
		    Long portraitId = user != null ? user.getPortraitUuid() : null;
		    if (portraitId != null) {
			String[] sizes = getSizeClass();
			StringBuilder bldr = new StringBuilder("<img class=\"").append(sizes[0]);
			if ( isRound ) {
			    bldr.append(CSS_ROUND);
			}
			bldr.append("\" src='").append(serverURL);
			if (!serverURL.endsWith("/")) {
			    bldr.append("/");
			}
			bldr.append("download?preferDownload=false&uuid=").append(portraitId)
				.append(sizes[1])
				.append("'></img>");
			code = bldr.toString();
		    } else {
			code = new StringBuilder("<div class=\"").append(getGenericSizeClass())
				.append(PORTRAIT_VERSION_SUFFIX).append(userIdLong % NUM_COLORS).append("\"></div>")
				.toString();
		    }
		    cache.put(userId, code);
		}
	    } else {
		code = "<div class=\"portrait-generic-small\"></div>";
	    }

	    JspWriter writer = pageContext.getOut();
	    writer.println(code);

	} catch (NumberFormatException nfe) {
	    PortraitTag.log.error("PortraitId unable to write out portrait details as userId is invalid. " + userId,
		    nfe);
	} catch (IOException ioe) {
	    PortraitTag.log.error(
		    "PortraitId unable to write out portrait details due to IOException. UserId is " + userId, ioe);
	} catch (Exception e) {
	    PortraitTag.log.error(
		    "PortraitId unable to write out portrait details due to an exception. UserId is " + userId, e);
	}
	return Tag.SKIP_BODY;
    }

    private IUserManagementService getUserManagementService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(pageContext.getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userManagementService;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, String> getPortraitCache() {
	HashMap<String, String> cache = (HashMap<String, String>) pageContext.getAttribute("portraitCache");
	if (cache == null) {
	    cache = new HashMap<String, String>();
	    pageContext.setAttribute("portraitCache", cache);
	}
	return cache;
    }

    /* Get String[size css class, version id] based on size attribute */
    private String[]  getSizeClass() {
	if (size != null) {
	    if (size.equalsIgnoreCase(STYLE_MEDIUM))
		return CSS_MEDIUM;
	    if (size.equalsIgnoreCase(STYLE_LARGE))
		return CSS_LARGE;
	    if (size.equalsIgnoreCase(STYLE_XLARGE))
		return CSS_XLARGE;
	}
	return CSS_SMALL;
    }

    private String getGenericSizeClass() {
	if (size != null) {
	    if (size.equalsIgnoreCase(STYLE_MEDIUM))
		return CSS_GENERIC_MEDIUM;
	    if (size.equalsIgnoreCase(STYLE_LARGE))
		return CSS_GENERIC_LARGE;
	    if (size.equalsIgnoreCase(STYLE_XLARGE))
		return CSS_GENERIC_XLARGE;
	}
	return CSS_GENERIC_SMALL;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getSize() {
	return size;
    }

    public void setSize(String size) {
	this.size = size;
    }

    public String getRound() {
	return round;
    }

    public void setRound(String round) {
	this.round = round;
    }
}
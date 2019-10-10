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
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

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
public class PortraitTag extends BodyTagSupport {
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
    private String hover = null;

    public PortraitTag() {
	super();
    }

    @Override
    public int doStartTag() throws JspException {

	return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {

	String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
	serverURL = serverURL == null ? null : serverURL.trim();

	try {
	    if (userId != null && userId.length() > 0) {
		String code = null;
		HashMap<String, String> cache = getPortraitCache();
		code = cache.get(userId);

		if (code == null) {
		    Integer userIdInt = Integer.decode(userId);
		    User user = (User) getUserManagementService().findById(User.class, userIdInt);
		    boolean isHover = (hover != null ? Boolean.valueOf(hover) : false);
		    if ( isHover ) {
			code = buildHoverUrl(user);
		    } else {
			code = buildDivUrl(user);
		    }
		    cache.put(userId, code);
		}

		JspWriter writer = pageContext.getOut();
		writer.print(code);
	    }

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

    private String buildDivUrl(User user) {
	Long portraitId = user != null ? user.getPortraitUuid() : null;
	if (portraitId != null) {
	    boolean isRound = (round != null ? Boolean.valueOf(round) : true);
	    String[] sizes = getSizeClass();
	    StringBuilder bldr = new StringBuilder("<img class=\"").append(sizes[0]);
	    if (isRound) {
		bldr.append(CSS_ROUND);
	    }
	    String serverURL = Configuration.get(ConfigurationKeys.SERVER_URL);
	    bldr.append("\" src=\"").append(serverURL);
	    if (!serverURL.endsWith("/")) {
		bldr.append("/");
	    }
	    bldr.append("download?preferDownload=false&uuid=").append(portraitId).append(sizes[1]).append("\"></img>");
	    return bldr.toString();
	} else {
	    return new StringBuilder("<div class=\"").append(getGenericSizeClass()).append(PORTRAIT_VERSION_SUFFIX)
		    .append(user.getUserId() % NUM_COLORS).append("\"></div>").toString();
	}
    }

    private String buildHoverUrl(User user) {
	Long portraitId = user != null ? user.getPortraitUuid() : null;
	String linkText = getBodyContent() != null ? getBodyContent().getString() : null;
	if (portraitId != null) {
	    String fullName = user.getFullName();
	    if ( linkText == null || linkText.length() == 0)
		linkText = fullName;
	    return new StringBuilder(
		    "<a tabindex=\"0\" class=\"popover-link new-popover\" role=\"button\" data-toggle=\"popover\" data-id=\"popover-")
			    .append(userId).append("\" data-portrait=\"").append(portraitId)
			    .append("\" data-fullname=\"").append(fullName).append("\">")
			    .append(linkText).append("</a>").toString();
	} else {
	    return linkText != null ? linkText : "";
	}
    }
    
    private IUserManagementService getUserManagementService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(pageContext.getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userManagementService;
    }

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

    public String getHover() {
	return hover;
    }

    public void setHover(String hover) {
	this.hover = hover;
    }
}
/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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


package org.lamsfoundation.lams.tool.wiki.service;

import javax.servlet.ServletContext;

import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * This class act as the proxy between web layer and service layer. It is
 * designed to decouple the presentation logic and business logic completely. In
 * this way, the presentation tier will no longer be aware of the changes in
 * service layer. Therefore we can feel free to switch the business logic
 * implementation.
 * </p>
 */

public class WikiServiceProxy {

    public static final IWikiService getWikiService(ServletContext servletContext) {
	return (IWikiService) WikiServiceProxy.getWikiDomainService(servletContext);
    }

    private static Object getWikiDomainService(ServletContext servletContext) {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	return wac.getBean("wikiService");
    }

    /*
     * Return the wiki tool version of tool session manager implementation. It
     * will delegate to the Spring helper method to retrieve the proper bean
     * from Spring bean factory. @param servletContext the servletContext for
     * current application @return noticeboard service object.
     */
    public static final ToolSessionManager getWikiSessionManager(ServletContext servletContext) {
	return (ToolSessionManager) WikiServiceProxy.getWikiDomainService(servletContext);
    }

    /*
     * Return the wiki tool version of tool content manager implementation. It
     * will delegate to the Spring helper method to retrieve the proper bean
     * from Spring bean factory. @param servletContext the servletContext for
     * current application @return noticeboard service object.
     */
    public static final ToolContentManager getWikiContentManager(ServletContext servletContext) {
	return (ToolContentManager) WikiServiceProxy.getWikiDomainService(servletContext);
    }

}

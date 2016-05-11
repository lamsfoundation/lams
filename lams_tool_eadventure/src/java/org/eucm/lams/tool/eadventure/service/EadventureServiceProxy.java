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

package org.eucm.lams.tool.eadventure.service;

import javax.servlet.ServletContext;

import org.eucm.lams.tool.eadventure.EadventureConstants;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Dapeng.Ni
 * 
 *         <p>
 *         This class act as the proxy between web layer and service layer. It is
 *         designed to decouple the presentation logic and business logic completely.
 *         In this way, the presentation tier will no longer be aware of the changes in
 *         service layer. Therefore we can feel free to switch the business logic
 *         implementation.
 *         </p>
 */
public class EadventureServiceProxy {
    /**
     * Return the domain service object. It will delegate to the Spring
     * helper method to retrieve the proper bean from Spring bean factory.
     * 
     * @param servletContext
     *            the servletContext for current application
     * @return Shared eadventure service object.
     */
    public static final IEadventureService getEadventureService(ServletContext servletContext) {
	return (IEadventureService) EadventureServiceProxy.getEadventureDomainService(servletContext);
    }

    public static final ToolSessionManager getSessionManager(ServletContext servletContext) {
	return (ToolSessionManager) EadventureServiceProxy.getEadventureDomainService(servletContext);
    }

    public static final ToolContentManager getContentManager(ServletContext servletContext) {
	return (ToolContentManager) EadventureServiceProxy.getEadventureDomainService(servletContext);
    }

    private static Object getEadventureDomainService(ServletContext servletContext) {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	return wac.getBean(EadventureConstants.RESOURCE_SERVICE);
    }

}

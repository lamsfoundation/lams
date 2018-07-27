/**
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 */
package org.lamsfoundation.lams.integration.security;

import javax.servlet.ServletContext;

import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;

/**
 * Sets deployment configuration for all LAMS modules. Central has its own SsoHandler.
 *
 * @author Marcin Cieslak
 *
 */
public class CommonDeploymentHandler implements ServletExtension {
    @Override
    public void handleDeployment(final DeploymentInfo deploymentInfo, final ServletContext servletContext) {
	// If WildFly was run with VM parameter -Dlams.keepSessionId=true
	// session ID will not be changed after log in
	// This is necessary for TestHarness to run as it does not process session ID change correctly
	boolean keepSessionId = Boolean.parseBoolean(System.getProperty(SsoHandler.KEEP_SESSION_ID_KEY));
	if (keepSessionId) {
	    deploymentInfo.setChangeSessionIdOnLogin(false);
	}
    }
}
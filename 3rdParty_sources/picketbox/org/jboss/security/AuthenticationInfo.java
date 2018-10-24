/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.security;

import javax.security.auth.AuthPermission;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.AppConfigurationEntry;

/** The login module configuration information.

@author Scott.Stark@jboss.org
@version $Revision$
*/
public class AuthenticationInfo  
{
    public static final AuthPermission GET_CONFIG_ENTRY_PERM = new AuthPermission("getLoginConfiguration");
    public static final AuthPermission SET_CONFIG_ENTRY_PERM = new AuthPermission("setLoginConfiguration");
    private AppConfigurationEntry[] loginModules;
    private CallbackHandler callbackHandler;

    /** Get an application authentication configuration. This requires an
    AuthPermission("getLoginConfiguration") access.
    */
    public AppConfigurationEntry[] getAppConfigurationEntry()
    {
        SecurityManager manager = System.getSecurityManager();
        if (manager != null) {
            manager.checkPermission(GET_CONFIG_ENTRY_PERM);
        }
        return loginModules;
    }
    /** Set an application authentication configuration. This requires an
    AuthPermission("setLoginConfiguration") access.
    */
    public void setAppConfigurationEntry(AppConfigurationEntry[] loginModules)
    {
        SecurityManager manager = System.getSecurityManager();
        if (manager !=  null) {
            manager.checkPermission(SET_CONFIG_ENTRY_PERM);
        }
        this.loginModules = loginModules;
    }

    /**
    */
    public CallbackHandler getAppCallbackHandler()
    {
        return callbackHandler;
    }
    public void setAppCallbackHandler(CallbackHandler handler)
    {
        this.callbackHandler = handler;
    }
}

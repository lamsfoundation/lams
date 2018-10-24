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

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.KeyStore;
import java.security.PermissionCollection;
import java.security.Permissions;

import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;

/** A combination of keystore, authentication and authorization entries.

@author Scott.Stark@jboss.org
@version $Revision$
*/
public class AppPolicy
{
    /** A PermissionCollection that allows no permissions */
    public static final PermissionCollection NO_PERMISSIONS =  new Permissions();
    /** A PermissionCollection that allows all permissions */
    private static PermissionCollection ALL_PERMISSIONS;

    /**
     * @label defaultAppPolicy 
     */
    private static AppPolicy defaultAppPolicy;

    // Setup the class statics
    static
    {
        // A PermissionCollection that allows all permissions
        AllPermission all = new AllPermission();
        ALL_PERMISSIONS = all.newPermissionCollection();
        ALL_PERMISSIONS.add(all);
        // A default policy with no authentication and NO_PERMISSIONS
        defaultAppPolicy = new AppPolicy("other");
    }

    private String appName;
    private KeyStore keyStore;

    /**
     * @label permissions 
     */
    private AuthorizationInfo permissionInfo;

    /**
     * @label login 
     */
    private AuthenticationInfo loginInfo;

    public KeyStore getKeyStore()
    {
        return keyStore;
    }
    public void setKeyStore(KeyStore keyStore)
    {
        this.keyStore = keyStore;
    }

    public static void setDefaultAppPolicy(AppPolicy policy)
    {
       SecurityManager sm = System.getSecurityManager();
       if (sm != null) {
          sm.checkPermission(new RuntimePermission(AppPolicy.class.getName() + ".setDefaultAppPolicy"));
       }
        if( policy == null )
            throw PicketBoxMessages.MESSAGES.invalidNullArgument("policy");
        defaultAppPolicy = policy;
    }
    public static AppPolicy getDefaultAppPolicy()
    {
        return defaultAppPolicy;
    }

    public AppPolicy(String appName)
    {
        this.appName = appName;
    }

    public AuthenticationInfo getLoginInfo()
    {
        SecurityManager manager = System.getSecurityManager();
        if (manager != null) {
            manager.checkPermission(AuthenticationInfo.GET_CONFIG_ENTRY_PERM);
        }
        return loginInfo;
    }
    public void setLoginInfo(AuthenticationInfo loginInfo)
    {
        SecurityManager manager = System.getSecurityManager();
        if (manager != null) {
            manager.checkPermission(AuthenticationInfo.SET_CONFIG_ENTRY_PERM);
        }
        this.loginInfo = loginInfo;
    }
    public AuthorizationInfo getPermissionInfo()
    {
        return permissionInfo;
    }
    public void setPermissionInfo(AuthorizationInfo permissionInfo)
    {
        this.permissionInfo = permissionInfo;
    }

    public AppConfigurationEntry[] getAppConfigurationEntry()
    {
        AppConfigurationEntry[] appConfig = null;
        if( loginInfo != null )
            appConfig = loginInfo.getAppConfigurationEntry();
        if( appConfig == null && this != defaultAppPolicy )
            appConfig = defaultAppPolicy.getAppConfigurationEntry();
        AppConfigurationEntry[] copy = null;
        if( appConfig != null )
        {
            copy = new AppConfigurationEntry[appConfig.length];
            for(int c = 0; c < copy.length; c ++)
            {
                AppConfigurationEntry e0 = appConfig[c];
                AppConfigurationEntry e1 = new AppConfigurationEntry(
                    e0.getLoginModuleName(),
                    e0.getControlFlag(),
                    e0.getOptions()
                    );
                copy[c] = e1;
            }
        }
        return copy;
    }
	public PermissionCollection getPermissions(Subject subject, CodeSource codesource)
	{
        PermissionCollection perms = NO_PERMISSIONS;
        AuthorizationInfo info = getPermissionInfo();
        if( info == null )
            info = defaultAppPolicy.getPermissionInfo();
        if( info != null )
        {
            perms = info.getPermissions(subject, codesource);
        }

        return perms;
	}

    public String toString()
    {
        StringBuffer buffer = new StringBuffer(appName);
        buffer.append('\n');
        buffer.append("AuthenticationInfo:\n");
        if( loginInfo != null )
            buffer.append(loginInfo);
        buffer.append("AuthorizationInfo:\n");
        if( permissionInfo != null )
            buffer.append(permissionInfo);
        return buffer.toString();
    }
   
}
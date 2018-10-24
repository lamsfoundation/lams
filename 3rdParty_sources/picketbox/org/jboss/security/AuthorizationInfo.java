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
 
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;

/**

@author Scott.Stark@jboss.org
@version $Revision$
*/
public class AuthorizationInfo
{
    private static Set<Principal> emptySet = new HashSet<Principal>();
    private ArrayList<PolicyEntry> policyMap = new ArrayList<PolicyEntry>(); 

    /** An inner class that represents a grant entry in policyMap. It is composed
     *of a CodeSource and an array of Prinicpals along with the granted
     *permissions.
     */
    static class PolicyEntry
    {
        private CodeSource cs;
        private Principal[] principals;
        private ArrayList<Permission> permissions;

        PolicyEntry(CodeSource cs, Principal[] principals, ArrayList<Permission> permissions)
        {
            this.cs = cs;
            this.principals = principals;
            this.permissions = permissions;
        }

        public void getPermissions(PermissionCollection perms)
        {
            int length = permissions == null ? 0 : permissions.size();
            for(int n = 0; n < length; n ++)
            {
                Permission permission = (Permission) permissions.get(n);
                perms.add(permission);
            }
        }

        public boolean implies(CodeSource codesrc, Set<Principal> subjectPrincipals)
        {
            boolean implies = false;
            // Check codesources
            if( this.cs == codesrc )
            {   // Both null or the same object
                implies = true;
            }
            else if( this.cs != null && codesrc != null && this.cs.implies(codesrc) )
            {
                implies = true;
            }

            // Check Principals
            if( implies == true )
            {
                if( this.principals != null )
                {   // Every one of our principals must be in subjectPrincipals
                    for(int p = 0; p < this.principals.length; p ++)
                    {
                        if( subjectPrincipals.contains(this.principals[p]) == false )
                        {
                            implies = false;
                            break;
                        }
                    }
                }
            }

            return implies;
        }
        public boolean equals(Object obj)
        {
            if(obj instanceof PolicyEntry == false)
               return false;
            PolicyEntry key = (PolicyEntry) obj;
            boolean equals = this.cs == key.cs;
            if( equals == false )
            {
                if( this.cs != null && key.cs != null )
                    equals = this.cs.equals(key.cs);
                if( equals == true )
                {   // Every principal in this must equal 
                    if( this.principals != null && key.principals != null && this.principals.length == key.principals.length )
                    {
                        for(int p = 0; p < this.principals.length; p ++)
                        {
                            if( this.principals[p].equals(key.principals[p]) == false )
                            {
                                equals = false;
                                break;
                            }
                        }
                    }
                    else if( this.principals != null || key.principals != null )
                    {
                        equals = false;
                    }
                }
            }
            return equals;
        }
        public int hashCode()
        {
            int hashCode = 0;
            if( cs != null )
                hashCode = cs.hashCode();
            int length = (this.principals == null ? 0 : this.principals.length);
            for(int p = 0; p < length; p ++)
            {
                hashCode += this.principals[p].hashCode();
            }
            return hashCode;
        }

        public String toString()
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("cs=");
            buffer.append(cs);
            buffer.append("; principals=");
            for(int p = 0; principals != null && p < principals.length; p ++)
                buffer.append(principals[p]);
            buffer.append("; permissions=");
            buffer.append(permissions);
            return buffer.toString();
        }
    }

    public AuthorizationInfo()
    {
    }

	public PermissionCollection getPermissions(Subject subject, CodeSource codesource)
	{
		PermissionCollection perms = new Permissions();
        Set<Principal> subjectPrincipals = emptySet;
        if( subject != null )
            subjectPrincipals = subject.getPrincipals();
        for(int n = 0; n < policyMap.size(); n ++)
        {
            PolicyEntry entry = (PolicyEntry) policyMap.get(n);
            if( entry.implies(codesource, subjectPrincipals) == true )
                entry.getPermissions(perms);
        }
		return perms;
	}

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("permissions:");
        return buffer.toString();
    }

    public void grant(CodeSource cs, ArrayList<Permission> permissions)
    {
        grant(cs, permissions, null);
    }
    public void grant(CodeSource cs, ArrayList<Permission> permissions, Principal[] principals)
    {
        PolicyEntry entry = new PolicyEntry(cs, principals, permissions);
        policyMap.add(entry);
    }
}

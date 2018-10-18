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

import java.security.Principal;

/** An implementation of Principal and Comparable that represents no role.
Any Principal or name of a Principal when compared to an NobodyPrincipal
using {@link #equals(Object) equals} or {@link #compareTo(Object) compareTo} 
will always be found not equal to the NobodyPrincipal.

Note that this class is not likely to operate correctly in a collection
since the hashCode() and equals() methods are not correlated.

@author Scott.Stark@jboss.org
@version $Revision$
*/
public class NobodyPrincipal implements Comparable<Object>, Principal
{
    public static final String NOBODY = "<NOBODY>";
    public static final NobodyPrincipal NOBODY_PRINCIPAL = new NobodyPrincipal();

    public int hashCode()
    {
        return NOBODY.hashCode();
    }

    /**
    @return "<NOBODY>"
    */
    public String getName()
    {
        return NOBODY;
    }

    public String toString()
    {
        return NOBODY;
    }
    
    /** This method always returns 0 to indicate equality for any argument.
    This is only meaningful when comparing against other Principal objects
     or names of Principals.

    @return false to indicate inequality for any argument.
    */
    public boolean equals(Object another)
    {
        return false;
    }

    /** This method always returns 1 to indicate inequality for any argument.
    This is only meaningful when comparing against other Principal objects
     or names of Principals.

    @return 1 to indicate inequality for any argument.
    */
    public int compareTo(Object o)
    {
        return 1;
    }

}
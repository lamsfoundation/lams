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

import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

 
/** An implementation of Group that manages a collection of Principal
objects based on their hashCode() and equals() methods. This class
is not thread safe.

@author Scott.Stark@jboss.org
@version $Revision$
*/
@SuppressWarnings({"rawtypes","unchecked"})
public class SimpleGroup extends SimplePrincipal implements Group, Cloneable
{
    /** The serialVersionUID */
   private static final long serialVersionUID = 6051859639378507247L;
   
   private HashMap members;
   
   private static final String OVERRIDE_EQUALS_BEHAVIOR = "org.jboss.security.simpleprincipal.equals.override";
 
   public SimpleGroup(String groupName)
    {
        super(groupName);
        members = new HashMap(3);
    }

    /** Adds the specified member to the group.
     @param user the principal to add to this group.
     @return true if the member was successfully added,
         false if the principal was already a member.
     */
    public boolean addMember(Principal user)
    {
        boolean isMember = members.containsKey(user);
        if( isMember == false )
            members.put(user, user);
        return isMember == false;
    }
    /** Returns true if the passed principal is a member of the group.
        This method does a recursive search, so if a principal belongs to a
        group which is a member of this group, true is returned.

        A special check is made to see if the member is an instance of
        org.jboss.security.AnybodyPrincipal or org.jboss.security.NobodyPrincipal
        since these classes do not hash to meaningful values.
    @param member the principal whose membership is to be checked.
    @return true if the principal is a member of this group,
        false otherwise.
    */
    public boolean isMember(Principal member)
    {
        // First see if there is a key with the member name
        boolean isMember = members.containsKey(member);
        if( isMember == false )
        {   // Check the AnybodyPrincipal & NobodyPrincipal special cases
            isMember = (member instanceof org.jboss.security.AnybodyPrincipal);
            if( isMember == false )
            {
                if( member instanceof org.jboss.security.NobodyPrincipal )
                return false;
            }
        }
        if( isMember == false )
        {   // Check any Groups for membership
            Collection values = members.values();
            Iterator iter = values.iterator();
            while( isMember == false && iter.hasNext() )
            {
                Object next = iter.next();
                if( next instanceof Group )
                {
                    Group group = (Group) next;
                    isMember = group.isMember(member);
                }
            }
        }
        if ("true".equals(AccessController.doPrivileged(new PrivilegedAction()
        {
           public Object run()
           {
              return System.getProperty(OVERRIDE_EQUALS_BEHAVIOR, "false");
           }
        })))
        {
           if (isMember == false)
           {
              for (Iterator iterator = members.keySet().iterator(); iterator.hasNext();)
              {
                 Principal p = (Principal) iterator.next();
                 if (member instanceof SimplePrincipal)
                    isMember = p.getName() == null ? member.getName() == null : p.getName().equals(member.getName());
                 if (isMember)
                    break;
              }
           }
        }
        return isMember;
    }

    /** Returns an enumeration of the members in the group.
        The returned objects can be instances of either Principal
        or Group (which is a subinterface of Principal).
    @return an enumeration of the group members.
    */
    public Enumeration<Principal> members()
    {
        return Collections.enumeration(members.values());
    }

    /** Removes the specified member from the group.
    @param user the principal to remove from this group.
    @return true if the principal was removed, or
        false if the principal was not a member.
    */
    public boolean removeMember(Principal user)
    {
        Object prev = members.remove(user);
        return prev != null;
    }

   public String toString()
   {
      StringBuffer tmp = new StringBuffer(getName());
      tmp.append("(members:");
      Iterator iter = members.keySet().iterator();
      while( iter.hasNext() )
      {
         tmp.append(iter.next());
         tmp.append(',');
      }
      tmp.setCharAt(tmp.length()-1, ')');
      return tmp.toString();
   }
   
   public synchronized Object clone() throws CloneNotSupportedException  
   {  
      SimpleGroup clone = (SimpleGroup) super.clone();  
      if(clone != null) 
        clone.members = (HashMap)this.members.clone();   
      return clone;  
   } 
}
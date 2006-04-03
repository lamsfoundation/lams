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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.HashMap;

public class SimpleGroup extends SimplePrincipal implements Group
{
	private HashMap members;

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
		//not implemented.
		return false;
		
		/*
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
		return isMember;
		*/
	}

	/** Returns an enumeration of the members in the group.
		The returned objects can be instances of either Principal
		or Group (which is a subinterface of Principal).
	@return an enumeration of the group members.
	*/
	public Enumeration members()
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
}
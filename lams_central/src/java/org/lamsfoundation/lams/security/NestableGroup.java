package org.lamsfoundation.lams.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.LinkedList;

public class NestableGroup extends SimplePrincipal implements Group
{
	/** The stack of the Groups. Elements are pushed/poped by
		inserting/removing element 0.
	*/
	private LinkedList rolesStack;

	/** Creates new NestableGroup with the given name
	*/
	public NestableGroup(String name)
	{
		super(name);
		rolesStack = new LinkedList();
	}

// --- Begin Group interface methods
	/** Returns an enumeration that contains the single active Principal.
	@return an Enumeration of the single active Principal.
	*/
	public Enumeration members()
	{
		return new IndexEnumeration();
	}

	/** Removes the first occurence of user from the Principal stack.

	@param user the principal to remove from this group.
	@return true if the principal was removed, or 
	 * false if the principal was not a member.
	*/
	public boolean removeMember(Principal user)
	{
		return rolesStack.remove(user);
	}

	/** Pushes the group onto the Group stack and makes it the active
		Group.
	@param group, the instance of Group that contains the roles to set as the
		active Group.
	@exception IllegalArgumentException, thrown if group is not an instance of Group.
	@return true always.
	*/
	public boolean addMember(Principal group) throws IllegalArgumentException
	{
		if( (group instanceof Group) == false )
			throw new IllegalArgumentException("The addMember argument must be a Group");

		rolesStack.addFirst(group);
		return true;
	}

	/** Returns true if the passed principal is a member of the active group.
		This method does a recursive search, so if a principal belongs to a 
		group which is a member of this group, true is returned.

	 @param member the principal whose membership is to be checked.

	 @return true if the principal is a member of this group, false otherwise.
	*/
	public boolean isMember(Principal member)
	{
		if( rolesStack.size() == 0 )
			return false;
		Group activeGroup = (Group) rolesStack.getFirst();
		boolean isMember = activeGroup.isMember(member);
		return isMember;
	}

   public String toString()
   {
	  StringBuffer tmp = new StringBuffer(getName());
	  tmp.append("(members:");
	  Enumeration iter = members();
	  while( iter.hasMoreElements() )
	  {
		 tmp.append(iter.nextElement());
		 tmp.append(',');
	  }
	  tmp.setCharAt(tmp.length()-1, ')');
	  return tmp.toString();
   }
// --- End Group interface methods

	private class IndexEnumeration implements Enumeration
	{
		private Enumeration iter;

		IndexEnumeration()
		{
			if( rolesStack.size() > 0 )
			{
				Group grp = (Group) rolesStack.get(0);
				iter = grp.members();
			}
		}
		public boolean hasMoreElements()
		{
			boolean hasMore = iter != null && iter.hasMoreElements();
			return hasMore;
		}
		public Object nextElement()
		{
			Object next = null;
			if( iter != null )
				next = iter.nextElement();
			return next;
		}
	}
}

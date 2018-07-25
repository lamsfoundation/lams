/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SimpleGroup extends SimplePrincipal implements Group {
    private static final long serialVersionUID = -875843422197596410L;

    private Map<Principal, Principal> members;

    public SimpleGroup(String groupName) {
	super(groupName);
	members = new HashMap<Principal, Principal>(3);
    }

    /**
     * Adds the specified member to the group.
     *
     * @param user
     *            the principal to add to this group.
     * @return true if the member was successfully added, false if the principal was already a member.
     */
    @Override
    public boolean addMember(Principal user) {
	boolean isMember = members.containsKey(user);
	if (!isMember) {
	    members.put(user, user);
	}
	return !isMember;
    }

    /**
     * Returns true if the passed principal is a member of the group. This method does a recursive search, so if a
     * principal belongs to a group which is a member of this group, true is returned.
     *
     * @param member
     *            the principal whose membership is to be checked.
     * @return true if the principal is a member of this group, false otherwise.
     */

    @Override
    public boolean isMember(Principal member) {
	return false;
    }

    /**
     * Returns an enumeration of the members in the group. The returned objects can be instances of either Principal or
     * Group (which is a subinterface of Principal).
     *
     * @return an enumeration of the group members.
     */
    @Override
    public Enumeration<Principal> members() {
	return Collections.enumeration(members.values());
    }

    /**
     * Removes the specified member from the group.
     *
     * @param user
     *            the principal to remove from this group.
     * @return true if the principal was removed, or false if the principal was not a member.
     */
    @Override
    public boolean removeMember(Principal user) {
	Object prev = members.remove(user);
	return prev != null;
    }

    @Override
    public String toString() {
	StringBuffer tmp = new StringBuffer(getName());
	tmp.append("(members:");
	for (Principal principal : members.keySet()) {
	    tmp.append(principal);
	    tmp.append(',');
	}
	tmp.setCharAt(tmp.length() - 1, ')');
	return tmp.toString();
    }
}
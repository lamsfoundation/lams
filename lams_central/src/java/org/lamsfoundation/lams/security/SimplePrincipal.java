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

public class SimplePrincipal implements Principal, java.io.Serializable {
    private static final long serialVersionUID = 8351373842078910512L;

    private String name;

    public SimplePrincipal(String name) {
	this.name = name;
    }

    /**
     * Compare this SimplePrincipal's name against another Principal
     *
     * @return true if name equals another.getName()
     */
    @Override
    public boolean equals(Object another) {
	if (!(another instanceof Principal)) {
	    return false;
	}
	String anotherName = ((Principal) another).getName();
	boolean equals = false;
	if (name == null) {
	    equals = anotherName == null;
	} else {
	    equals = name.equals(anotherName);
	}
	return equals;
    }

    @Override
    public int hashCode() {
	return (name == null ? 0 : name.hashCode());
    }

    @Override
    public String toString() {
	return name;
    }

    @Override
    public String getName() {
	return name;
    }
}
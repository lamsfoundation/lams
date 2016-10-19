/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Ozgur Demirtas A comparator implementation that can be used as a constructor to collections. The TreeMap in
 *         the web layer makes use of it.
 *
 */
public class VoteComparator implements Comparator<Object>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1411627466727527798L;

    @Override
    public int compare(Object o1, Object o2) {

	int key1;
	int key2;

	if (o1 instanceof Long) {
	    Long s1 = (Long) o1;
	    Long s2 = (Long) o2;

	    key1 = s1.intValue();
	    key2 = s2.intValue();

	} else {
	    String s1 = (String) o1;
	    String s2 = (String) o2;

	    key1 = new Long(s1).intValue();
	    key2 = new Long(s2).intValue();
	}

	return key1 - key2;
    }

    @Override
    public boolean equals(Object o) {
	return compare(this, o) == 0;
    }
}

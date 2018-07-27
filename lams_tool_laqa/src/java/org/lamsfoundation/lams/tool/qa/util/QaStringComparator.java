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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.qa.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Ozgur Demirtas
 *
 *         A comparator implementation that can be used as a constructor to collections.
 *         The TreeMap in the web layer makes use of it.
 *
 */
public class QaStringComparator implements Comparator, Serializable {

    @Override
    public int compare(Object o1, Object o2) {
	String s1 = (String) o1;
	String s2 = (String) o2;

	return s1.compareTo(s2);
    }

    @Override
    public boolean equals(Object o) {
	String s = (String) o;
	return compare(this, o) == 0;
    }
}

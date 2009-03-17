/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.gradebook.dto.comparators;

import java.util.Comparator;

import org.lamsfoundation.lams.gradebook.dto.GradeBookUserDTO;

@SuppressWarnings("unchecked")
public class GradeBookUserLastNameComparator implements Comparator {

    public int compare(Object gradeBookUserDTO, Object anotherGradeBookUserDTO) {

	if (gradeBookUserDTO instanceof GradeBookUserDTO && anotherGradeBookUserDTO instanceof GradeBookUserDTO) {
	    String lastName1 = ((GradeBookUserDTO) gradeBookUserDTO).getUserDTO().getLastName().toUpperCase();
	    String lastName2 = ((GradeBookUserDTO) anotherGradeBookUserDTO).getUserDTO().getLastName().toUpperCase();

	    int ret;
	    if (lastName1.equals(lastName2)) {
		String firstName1 = ((GradeBookUserDTO) gradeBookUserDTO).getUserDTO().getFirstName().toUpperCase();
		String firstName2 = ((GradeBookUserDTO) anotherGradeBookUserDTO).getUserDTO().getFirstName().toUpperCase();
		ret = firstName1.compareTo(firstName2);
		
	    }
	    else {
		ret = lastName1.compareTo(lastName2);
	    }
	    return ret;
	} else {
	    return 0;
	}
    }
}

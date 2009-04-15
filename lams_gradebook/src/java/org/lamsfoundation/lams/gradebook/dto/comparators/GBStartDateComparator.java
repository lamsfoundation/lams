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
import java.util.Date;

import org.lamsfoundation.lams.gradebook.dto.GBActivityGridRowDTO;

@SuppressWarnings("unchecked")
public class GBStartDateComparator implements Comparator {
    public int compare(Object actGridRow, Object anotherActGridRow) {
	if (actGridRow instanceof GBActivityGridRowDTO && anotherActGridRow instanceof GBActivityGridRowDTO) {

	    Date startDate1 = ((GBActivityGridRowDTO) actGridRow).getStartDate();
	    Date startDate2 = ((GBActivityGridRowDTO) anotherActGridRow).getStartDate();

	    Long id1 = ((GBActivityGridRowDTO) actGridRow).getId();
	    Long id2 = ((GBActivityGridRowDTO) anotherActGridRow).getId();

	    if (startDate1 == null && startDate2 == null) {
		if (id1 == null || id2 == null) {
		    return 0;
		}
		Long ret = id1 - id2;
		return ret.intValue() * -1;
	    } else if (startDate1 == null) {
		return +1;
	    } else if (startDate2 == null) {
		return -1;
	    } else {
		return startDate1.compareTo(startDate2);
	    }
	} else {
	    return 0;
	}
    }
}

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


package org.lamsfoundation.lams.gradebook.dto.comparators;

import java.util.Comparator;
import java.util.Date;

import org.lamsfoundation.lams.gradebook.dto.GBActivityGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;

@SuppressWarnings("unchecked")
public class GBStartDateComparator implements Comparator {
    @Override
    public int compare(Object actGridRow, Object anotherActGridRow) {
	if (actGridRow instanceof GBActivityGridRowDTO && anotherActGridRow instanceof GBActivityGridRowDTO) {

	    Date startDate1 = ((GBActivityGridRowDTO) actGridRow).getStartDate();
	    Date startDate2 = ((GBActivityGridRowDTO) anotherActGridRow).getStartDate();

	    if (startDate1 == null && startDate2 == null) {

		Long id1 = null;
		Long id2 = null;

		try {
		    id1 = Long.parseLong(((GradebookGridRowDTO) actGridRow).getId());
		    id2 = Long.parseLong(((GradebookGridRowDTO) anotherActGridRow).getId());
		} catch (NumberFormatException e) {
		    return 0;
		}

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

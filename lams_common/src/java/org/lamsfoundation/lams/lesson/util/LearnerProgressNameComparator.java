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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.lesson.util;

import java.util.Comparator;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Compares two learner progress objects, based on user name.
 *
 * @author Marcin Cieslak
 */
public class LearnerProgressNameComparator implements Comparator<LearnerProgress> {

    @Override
    public int compare(LearnerProgress o1, LearnerProgress o2) {
	if (o1 == null) {
	    return o2 == null ? 0 : -1;
	}
	if (o2 == null) {
	    return 1;
	}

	return User.COMPARATOR.compare(o1.getUser(), o2.getUser());
    }
}
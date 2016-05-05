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

/* $Id$ */
package org.eucm.lams.tool.eadventure.util;

import java.util.Comparator;

import org.eucm.lams.tool.eadventure.model.EadventureCondition;

public class EadventureConditionComparator implements Comparator<EadventureCondition> {

    @Override
    public int compare(EadventureCondition o1, EadventureCondition o2) {
	if (o1 != null && o2 != null) {
	    return o1.getSequenceId() - o2.getSequenceId();
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}

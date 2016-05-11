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


package org.lamsfoundation.lams.tool.forum.util;

import java.util.Comparator;
import java.util.Date;

import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;

/**
 * Topics comparator, compares by post date.
 *
 * @author Andrey Balan
 */
public class MessageDTOByDateComparator implements Comparator<MessageDTO> {

    @Override
    public int compare(MessageDTO o1, MessageDTO o2) {
	if (o1 != null && o2 != null) {
	    Date o1Date = o1.getMessage().getUpdated();
	    Date o2Date = o2.getMessage().getUpdated();

	    return o1Date.compareTo(o2Date) == 0 ? o1.getMessage().getUid().compareTo(o2.getMessage().getUid())
		    : o1Date.compareTo(o2Date);
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }

}

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



package org.lamsfoundation.lams.tool.forum.util;

import java.util.Comparator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.lamsfoundation.lams.tool.forum.model.Message;

/**
 * For finding topic order in Forum conditions.
 *
 * @author Marcin Cieslak
 *
 */
public class ConditionTopicComparator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
	if (o1 != null && o2 != null) {
	    // use same attributes to determine equality as Message.equals() method
	    if (new EqualsBuilder()
		    //.append(o1.getUid(),o2.getUid())
		    .append(o1.getSubject(), o2.getSubject()).append(o1.getBody(), o2.getBody())
		    .append(o1.getReplyNumber(), o2.getReplyNumber())
		    //.append(this.lastReplyDate,genericEntity.lastReplyDate)
		    //.append(this.created,genericEntity.created)
		    //.append(this.updated,genericEntity.updated)
		    .append(o1.getCreatedBy(), o2.getCreatedBy()).append(o1.getModifiedBy(), o2.getModifiedBy())
		    .isEquals()) {
		return 0;
	    }
	    return new DateComparator().compare(o1.getCreated(), o2.getCreated());
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}
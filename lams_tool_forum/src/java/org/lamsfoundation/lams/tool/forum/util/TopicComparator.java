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

import static org.lamsfoundation.lams.tool.forum.ForumConstants.OLD_FORUM_STYLE;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.tool.forum.model.MessageSeq;

/**
 * This class implementaion <code>java.util.Comparator</code> interface. It can sort meesage according to
 * message post date and message level.
 * 
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class TopicComparator implements Comparator<MessageSeq> {
    private static final Logger log = Logger.getLogger(TopicComparator.class);

    @Override
    public int compare(MessageSeq msgSeq1, MessageSeq msgSeq2) {
	Message msg1 = msgSeq1.getMessage();
	Message msg2 = msgSeq2.getMessage();

	short level1, level2;
	level1 = msgSeq1.getMessageLevel();
	level2 = msgSeq2.getMessageLevel();
	Message parent1, parent2;
	//choose the smaller level value
	short lessLevel = level1 > level2 ? level2 : level1;
	for (int compareLevel = 0; compareLevel <= lessLevel; compareLevel++) {
	    //init value, loop from current message
	    parent1 = msg1;
	    parent2 = msg2;
	    level1 = msgSeq1.getMessageLevel();
	    level2 = msgSeq2.getMessageLevel();
	    while (level1 > compareLevel) {
		//get parent until assigned level
		if (parent1 == null) {
		    log.error("Message " + parent1 + " level " + level1 + " has null parent");
		    return 0;
		}
		parent1 = parent1.getParent();
		level1--;
	    }
	    while (level2 > compareLevel) {
		//get parent until assigned level
		if (parent2 == null) {
		    log.error("Message " + parent2 + " level " + level2 + " has null parent");
		    return 0;
		}
		parent2 = parent2.getParent();
		level2--;
	    }
	    //this comparation will handle different branch node
	    if (parent1 != parent2) {
		//compare last modified date, the latest is at beginning
		if (OLD_FORUM_STYLE) {
		    return parent1.getUpdated().before(parent2.getUpdated()) ? -1 : 1;
		} else {
		    return parent1.getUpdated().before(parent2.getUpdated()) ? 1 : -1;
		}
	    }
	    //this comparation will handle same branch node
	    //the direct parent level, their parent(or themselves) are still equal
	    if (compareLevel == lessLevel) {
		if (msgSeq1.getMessageLevel() != msgSeq2.getMessageLevel()) {
		    return msgSeq1.getMessageLevel() - msgSeq2.getMessageLevel();
		} else {
		    if (OLD_FORUM_STYLE) {
			return msg1.getUpdated().before(msg2.getUpdated()) ? -1 : 1;
		    } else {
			return msg1.getUpdated().before(msg2.getUpdated()) ? 1 : -1;
		    }
		}
	    }

	}
	if (OLD_FORUM_STYLE) {
	    return msg1.getUpdated().before(msg2.getUpdated()) ? -1 : 1;
	} else {
	    return msg1.getUpdated().before(msg2.getUpdated()) ? 1 : -1;
	}
    }

}

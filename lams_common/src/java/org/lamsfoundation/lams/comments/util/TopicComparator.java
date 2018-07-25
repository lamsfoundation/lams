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



package org.lamsfoundation.lams.comments.util;

import java.util.Comparator;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.comments.Comment;

/**
 * This class implements the <code>java.util.Comparator</code> interface. Based on the Topic Comparator from Forum.
 */
public class TopicComparator implements Comparator<Comment> {
    private static final Logger log = Logger.getLogger(TopicComparator.class);

    @Override
    public int compare(Comment msg1, Comment msg2) {

	short level1, level2;
	level1 = msg1.getCommentLevel();
	level2 = msg2.getCommentLevel();
	Comment parent1, parent2;
	//choose the smaller level value
	short lessLevel = level1 > level2 ? level2 : level1;
	for (int compareLevel = 0; compareLevel <= lessLevel; compareLevel++) {
	    //init value, loop from current message
	    parent1 = msg1;
	    parent2 = msg2;
	    level1 = msg1.getCommentLevel();
	    level2 = msg2.getCommentLevel();
	    while (level1 > compareLevel) {
		//get parent until assigned level
		if (parent1 == null) {
		    log.error("Comment " + parent1 + " level " + level1 + " has null parent");
		    return 0;
		}
		parent1 = parent1.getParent();
		level1--;
	    }
	    while (level2 > compareLevel) {
		//get parent until assigned level
		if (parent2 == null) {
		    log.error("Comment " + parent2 + " level " + level2 + " has null parent");
		    return 0;
		}
		parent2 = parent2.getParent();
		level2--;
	    }
	    //this comparison will handle different branch node
	    if (parent1 != parent2) {
		// show the newest first - this will have the higher uid
		return compareUids(compareLevel, parent1, parent2);
	    }
	    //this comparison will handle same branch node
	    //the direct parent level, their parent(or themselves) are still equal
	    if (compareLevel == lessLevel) {
		if (msg1.getCommentLevel() != msg2.getCommentLevel()) {
		    return msg1.getCommentLevel() - msg2.getCommentLevel();
		} else {
		    return compareUids(compareLevel, msg1, parent2);
		}
	    }

	}
	return msg1.getUpdated().before(msg2.getUpdated()) ? -1 : 1;
    }

    // If Level = 1 then "top" so sort newest first. Otherwise sort oldest first.
    protected int compareUids(int compareLevel, Comment c1, Comment c2) {
	if (compareLevel <= 2) {
	    return c1.getUid() < c2.getUid() ? 1 : -1;
	} else {
	    return c1.getUid() > c2.getUid() ? 1 : -1;
	}
    }
}

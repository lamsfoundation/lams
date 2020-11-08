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

package org.lamsfoundation.lams.tool.taskList.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;

/**
 * Comparator for <code>TaskListItemAttachment</code>.
 *
 * @author Andrey Balan
 */
public class TaskListItemAttachmentComparator implements Comparator<TaskListItemAttachment> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(TaskListItemAttachment o1, TaskListItemAttachment o2) {
	if (o1 != null && o2 != null) {
	    int timestampComparision = o1.getCreated().compareTo(o2.getCreated());
	    if (timestampComparision == 0) {
		return o1.getUid() == null ? (o2.getUid() == null ? 0 : -1)
			: (o2.getUid() == null ? 1 : o1.getUid().compareTo(o2.getUid()));
	    } else {
		return timestampComparision;
	    }
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}

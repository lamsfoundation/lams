package org.lamsfoundation.lams.tool.taskList.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;

/**
 * Comparator for <code>TaskListItem</code>.
 *
 * @author Andrey Balan
 */
public class TaskListItemComparator implements Comparator<TaskListItem> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(TaskListItem o1, TaskListItem o2) {
	if (o1 != null && o2 != null) {
	    return o1.getSequenceId() - o2.getSequenceId();
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }

}

package org.lamsfoundation.lams.tool.daco.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;

/**
 *
 * @author steven
 *
 */
public class DacoQuestionComparator implements Comparator<DacoQuestion> {
    @Override
    public int compare(DacoQuestion o1, DacoQuestion o2) {
	if (o1 != null && o2 != null & o1.getCreateDate() != null && o2.getCreateDate() != null) {
	    return o1.getCreateDate().getTime() - o2.getCreateDate().getTime() > 0 ? 1 : -1;
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}
package org.lamsfoundation.lams.tool.qa.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.qa.model.QaSession;

/**
 * McSessionComparator.
 */
public class QaSessionComparator implements Comparator<QaSession> {

    @Override
    public int compare(QaSession o1, QaSession o2) {
	if (o1 != null && o2 != null) {
	    return o1.getSession_name().compareTo(o2.getSession_name());
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }

}

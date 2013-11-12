package org.lamsfoundation.lams.tool.mc.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.mc.pojos.McSession;

/**
 * McSessionComparator.
 */
public class McSessionComparator implements Comparator<McSession> {

    public int compare(McSession o1, McSession o2) {
	if (o1 != null && o2 != null) {
	    return o1.getSession_name().compareTo(o2.getSession_name());
	} else if (o1 != null)
	    return 1;
	else
	    return -1;
    }

}

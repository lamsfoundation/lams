package org.lamsfoundation.lams.tool.mc.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.lamsfoundation.lams.util.AlphanumComparator;

/**
 * McSessionComparator.
 */
public class McSessionComparator implements Comparator<McSession> {

    private static AlphanumComparator alphanumComparator = new AlphanumComparator();

    @Override
    public int compare(McSession session1, McSession session2) {

	String session1Name = session1 != null ? session1.getSession_name() : "";
	String session2Name = session2 != null ? session2.getSession_name() : "";

	return alphanumComparator.compare(session1Name, session2Name);
    }

}

package org.lamsfoundation.lams.tool.dokumaran.util;

import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;

import java.util.Comparator;

public class DokumaranSessionComparator implements Comparator<DokumaranSession> {
    @Override
    public int compare(DokumaranSession o1, DokumaranSession o2) {
	if (o1 != null && o2 != null) {
	    String name1 = o1.getSessionName().replaceAll("\\D+", "");
	    String name2 = o2.getSessionName().replaceAll("\\D+", "");
	    if (name1.length() == 0 || name2.length() == 0) {
		return name1.compareTo(name2);
	    }
	    Long num1 = Long.parseLong(name1);
	    Long num2 = Long.parseLong(name2);
	    return num1.compareTo(num2);
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}
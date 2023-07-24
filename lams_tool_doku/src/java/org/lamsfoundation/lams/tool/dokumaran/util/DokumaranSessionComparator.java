package org.lamsfoundation.lams.tool.dokumaran.util;

import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;

import java.util.Comparator;

public class DokumaranSessionComparator implements Comparator<DokumaranSession> {
    @Override
    public int compare(DokumaranSession o1, DokumaranSession o2) {
	String name1 = o1.getSessionName();
	String name2 = o2.getSessionName();
	return DokumaranSessionNameComparator.compareSessionNames(name1, name2);
    }
}
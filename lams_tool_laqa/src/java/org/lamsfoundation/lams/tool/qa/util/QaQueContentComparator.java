package org.lamsfoundation.lams.tool.qa.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.qa.QaQuestion;

/**
 * Comparator for <code>QaQuestion</code>. Only the display order is
 * compared.
 * 
 * @author Marcin Cieslak
 */
public class QaQueContentComparator implements Comparator<QaQuestion> {

    /**
     * {@inheritDoc}
     */
    public int compare(QaQuestion o1, QaQuestion o2) {
	if (o1 != null && o2 != null) {
	    return o1.getDisplayOrder() - o2.getDisplayOrder();
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }

}

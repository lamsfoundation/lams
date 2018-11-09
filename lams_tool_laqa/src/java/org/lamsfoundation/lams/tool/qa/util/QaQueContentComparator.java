package org.lamsfoundation.lams.tool.qa.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.qa.model.QaQueContent;

/**
 * Comparator for <code>QaQueContent</code>. Only the display order is
 * compared.
 *
 * @author Marcin Cieslak
 */
public class QaQueContentComparator implements Comparator<QaQueContent> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(QaQueContent o1, QaQueContent o2) {
	if (o1 != null && o2 != null) {
	    return o1.getDisplayOrder() - o2.getDisplayOrder();
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }

}

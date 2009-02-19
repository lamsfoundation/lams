package org.lamsfoundation.lams.tool.qa.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.qa.QaQuestionContentDTO;

/**
 * Comparator for <code>QaQuestionContentDTO</code>. Only the display order
 * is compared.
 * 
 * @author Marcin Cieslak
 */
public class QaQuestionContentDTOComparator implements Comparator<QaQuestionContentDTO> {

    /**
     * {@inheritDoc}
     */
    public int compare(QaQuestionContentDTO o1, QaQuestionContentDTO o2) {
	if (o1 != null && o2 != null) {
	    return Integer.parseInt(o1.getDisplayOrder()) - Integer.parseInt(o2.getDisplayOrder());
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}
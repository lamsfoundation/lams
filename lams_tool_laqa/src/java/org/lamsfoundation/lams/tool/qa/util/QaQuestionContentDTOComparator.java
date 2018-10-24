package org.lamsfoundation.lams.tool.qa.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;

/**
 * Comparator for <code>QaQuestionDTO</code>. Only the display order
 * is compared.
 *
 * @author Marcin Cieslak
 */
public class QaQuestionContentDTOComparator implements Comparator<QaQuestionDTO> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(QaQuestionDTO o1, QaQuestionDTO o2) {
	if (o1 != null && o2 != null) {
	    return Integer.parseInt(o1.getDisplayOrder()) - Integer.parseInt(o2.getDisplayOrder());
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}
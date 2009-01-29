package org.lamsfoundation.lams.tool.assessment.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;

/**
 * 
 * @author Andrey Balan
 * 
 */
public class AssessmentQuestionComparator implements Comparator<AssessmentQuestion> {

    public int compare(AssessmentQuestion o1, AssessmentQuestion o2) {
	if (o1 != null && o2 != null) {
	    return o1.getSequenceId() - o2.getSequenceId();
	} else if (o1 != null)
	    return 1;
	else
	    return -1;
    }

}

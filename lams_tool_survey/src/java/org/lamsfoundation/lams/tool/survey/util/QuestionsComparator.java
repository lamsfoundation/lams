package org.lamsfoundation.lams.tool.survey.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
/**
 * 
 * @author steven
 *
 */
public class QuestionsComparator implements Comparator<SurveyQuestion> {

	public int compare(SurveyQuestion o1, SurveyQuestion o2) {
		if(o1 != null && o2 != null){
			return o1.getSequenceId() -o2.getSequenceId();
		}else if(o1 != null)
			return 1;
		else
			return -1;
	}

}

package org.lamsfoundation.lams.tool.survey.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.survey.model.SurveyOption;

public class SurveyOptionComparator implements Comparator<SurveyOption> {

	public int compare(SurveyOption o1, SurveyOption o2) {
		if(o1 != null && o2 != null){
			return o1.getSequenceId() - o2.getSequenceId();
		}else if(o1 != null)
			return 1;
		else
			return -1;
	}
}

package org.lamsfoundation.lams.tool.survey.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.survey.model.SurveyUser;

public class SurveyUserComparator implements Comparator<SurveyUser> {
	
	public int compare(SurveyUser o1, SurveyUser o2) {
		if(o1 != null && o2 != null){
			return o1.getLoginName().compareTo( o2.getLoginName());
		}else if(o1 != null)
			return 1;
		else
			return -1;
	}
}

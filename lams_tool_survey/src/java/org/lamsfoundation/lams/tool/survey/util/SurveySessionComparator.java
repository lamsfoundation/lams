package org.lamsfoundation.lams.tool.survey.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.survey.model.SurveySession;
/**
 * 
 * @author steven
 *
 */
public class SurveySessionComparator implements Comparator<SurveySession> {

	public int compare(SurveySession o1, SurveySession o2) {
		if(o1 != null && o2 != null){
			return o1.getSessionName().compareTo(o2.getSessionName());
		}else if(o1 != null)
			return 1;
		else
			return -1;
	}

}

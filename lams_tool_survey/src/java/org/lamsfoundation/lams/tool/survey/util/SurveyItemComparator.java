package org.lamsfoundation.lams.tool.survey.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
/**
 * 
 * @author steven
 *
 */
public class SurveyItemComparator implements Comparator<SurveyQuestion> {

	public int compare(SurveyQuestion o1, SurveyQuestion o2) {
		if(o1 != null && o2 != null & o1.getCreateDate() !=null && o2.getCreateDate() != null){
			return (o1.getCreateDate().getTime() - o2.getCreateDate().getTime()) > 0?1:-1;
		}else if(o1 != null)
			return 1;
		else
			return -1;
	}

}

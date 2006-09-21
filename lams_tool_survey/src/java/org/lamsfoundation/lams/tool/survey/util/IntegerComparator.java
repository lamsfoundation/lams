package org.lamsfoundation.lams.tool.survey.util;

import java.util.Comparator;
/**
 * 
 * @author steven
 *
 */
public class IntegerComparator implements Comparator<Integer> {

	public int compare(Integer o1, Integer o2) {
		if(o1 != null && o2 != null){
			return o1.compareTo(o2);
		}else if(o1 != null)
			return 1;
		else
			return -1;
	}

}

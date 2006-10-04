package org.lamsfoundation.lams.tool.scribe.util;

public class ScribeUtils {
	
	public static int calculateVotePercentage(int numberOfVotes, int numberOfLearners) {
		
		Float v = new Float(numberOfVotes);
		Float l = new Float(numberOfLearners);
		
		Float result = v/l*100;
		
		return result.intValue();
	}
	
}

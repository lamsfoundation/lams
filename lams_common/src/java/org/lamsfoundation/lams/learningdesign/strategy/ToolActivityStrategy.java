/*
 * Created on Mar 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.ArrayList;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ToolActivityStrategy extends SimpleActivityStrategy {

	
	public Integer[] getContributionType(Activity activity) {
		ArrayList contributionType = new ArrayList();
		
		ToolActivity toolActivity = (ToolActivity)activity;
		if(toolActivity.getTool().getSupportsModeration().booleanValue())
			contributionType.add(new Integer(MODERATION));
		if(toolActivity.getTool().getSupportsContribute().booleanValue())
			contributionType.add(new Integer(CONTRIBUTION));
		if(toolActivity.getDefineLater().booleanValue())
			contributionType.add(new Integer(DEFINE_LATER));
		
		return (Integer[])contributionType.toArray(new Integer[contributionType.size()]);
	}

}

/*
 * Created on Mar 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.ArrayList;

import org.lamsfoundation.lams.learningdesign.Activity;
/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PermissionGateActivityStrategy extends SimpleActivityStrategy {

	/**
	 *  (non-Javadoc)
	 * @see org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy#getContributionType(org.lamsfoundation.lams.learningdesign.Activity)
	 */
	public Integer[] getContributionType(Activity activity) {
		ArrayList contributionType = new ArrayList();
		contributionType.add(new Integer(PERMISSION));
		return (Integer[])contributionType.toArray(new Integer[contributionType.size()]);
	}

}

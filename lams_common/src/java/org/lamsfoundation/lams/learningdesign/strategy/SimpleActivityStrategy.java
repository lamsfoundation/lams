/*
 * Created on Mar 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.strategy;

import org.lamsfoundation.lams.learningdesign.Activity;




/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SimpleActivityStrategy {
	
	public static final int MODERATION = 1;
	public static final int DEFINE_LATER = 2;
	public static final int PERMISSION = 3;
	public static final int CHOSEN_GROUPING = 4;
	public static final int CONTRIBUTION = 5;
	 
	public abstract Integer[] getContributionType(Activity activity);
}

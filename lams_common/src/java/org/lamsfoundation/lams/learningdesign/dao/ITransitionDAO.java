/*
 * Created on Dec 6, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;

import org.lamsfoundation.lams.learningdesign.Transition;

/**
 * @author MMINHAS
 */
public interface ITransitionDAO extends IBaseDAO {
	
	/**
	 * @param transitionID
	 * @return Transition populated Transition object
	 */
	public Transition getTransitionById(Long transitionID);

}

/*
 * Created on Dec 4, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
;
/**
 * @author manpreet
 */
public interface ILearningDesignDAO extends IBaseDAO{
	
	/**
	 * @param learningDesignId
	 * @return LearningDesign populated LearningDesign object
	 */
	public LearningDesign getLearningDesignById(Long learningDesignId);
	/**
	 * @param title
	 * @return LearningDesign populated LearningDesign object
	 */
	public LearningDesign getLearningDesignByTitle(String title);
	
	/**
	 * @return List of all Learning designs
	 */
	public List getAllLearningDesigns();	
	
	/**
	 * @param userID
	 * @return List of learning designs with given userID
	 */
	public List getLearningDesignByUserId(Long userID);
}

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
	
	/**
	 * This method returns a list of all designs that are valid in 
	 * the given workspaceFolder.
	 * 
	 * @param workspaceFolderID The workspace_folder_id of the WorkspaceFolder
	 * 							from where the designs have to be fetched.
	 * @return List The List of all available designs
	 */
	public List getAllValidLearningDesignsInFolder(Integer workspaceFolderID);
	
	/**
	 * This method returns a list of all available designs in 
	 * the given workspaceFolder.
	 * 
	 * @param workspaceFolderID The workspace_folder_id of the WorkspaceFolder
	 * 							from where the designs have to be fetched.
	 * @return List The List of all available designs
	 */
	public List getAllLearningDesignsInFolder(Integer workspaceFolderID);
	
	
}

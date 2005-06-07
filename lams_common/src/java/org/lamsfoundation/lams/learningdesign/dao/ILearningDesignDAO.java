/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
;
/**
 * @author Manpreet Minhas
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
	
	/**
	 * This method returns a List of Learning Designs with given  
	 * <code>parent_learning_design_id</code>
	 * 
	 * @param parentDesignID The <code>parent_learning_design_id</code>
	 * @return List The List of all corresponding Learning designs with
	 * 				given <code>parent_learning_design_id</code> 
	 */
	public List getLearningDesignsByParent(Long parentDesignID);
}

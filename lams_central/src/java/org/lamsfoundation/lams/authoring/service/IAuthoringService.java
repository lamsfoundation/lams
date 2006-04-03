/***************************************************************************
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
 * ************************************************************************
 */
package org.lamsfoundation.lams.authoring.service;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;

/**
 * @author Manpreet Minhas 
 */
public interface IAuthoringService {
	
	/** Message key returned by the storeLearningDesignDetails() method */
	public static final String STORE_LD_MESSAGE_KEY = "storeLearningDesignDetails";

	/**
	 * Returns a populated LearningDesign object corresponding to the given learningDesignID
	 * 
	 * @param learningDesignID The learning_design_id of the design which has to be fetched
	 * @return LearningDesign The populated LearningDesign object corresponding to the given learningDesignID
	 */
	public LearningDesign getLearningDesign(Long learningDesignID);
	
	
	/**
	 * Create a copy of learning design as per the requested learning design 
	 * and saves it in the given workspacefolder. Does not set the original 
	 * 
	 * @param originalLearningDesign The source learning design id.
	 * @param copyType purpose of copying the design. Can have one of the follwing values
	 * <ul>
	 * <li>LearningDesign.COPY_TYPE_NONE (for authoring enviornment)</li>
	 * <li>LearningDesign.COPY_TYPE_LESSON (for monitoring enviornment while creating a Lesson)</li>
	 * <li>LearningDesign.COPY_TYPE_PREVIEW (for previewing purposes)</li>
	 * </ul>
	 * @param user The user who has sent this request(author/teacher)
	 * @param setOriginalDesign If true, then sets the originalLearningDesign field in the new design
	 * @return LearningDesign The new copy of learning design.
	 */
	public LearningDesign copyLearningDesign(LearningDesign originalLearningDesign,Integer copyType,User user, WorkspaceFolder workspaceFolder, boolean setOriginalDesign);
	
	/**
	 * Create a copy of learning design as per the requested learning design
	 * and saves it in the given workspacefolder. Designed to be called when user tries
	 * to copy a learning design using the Flash interface. Does not set the original 
	 * learning design field, so it should not be used for creating lesson learning designs.
	 * 
	 * @param originalLearningDesingID the source learning design id.
	 * @param copyType purpose of copying the design. Can have one of the follwing values
	 * <ul>
	 * <li>LearningDesign.COPY_TYPE_NONE (for authoring enviornment)</li>
	 * <li>LearningDesign.COPY_TYPE_LESSON (for monitoring enviornment while creating a Lesson)</li>
	 * <li>LearningDesign.COPY_TYPE_PREVIEW (for previewing purposes)</li>
	 * </ul>
	 * @param userID The user_id of the user who has sent this request(author/teacher)
	 * @param workspaceFolderID The workspacefolder where this copy of the design would be saved
	 * @param setOriginalDesign If true, then sets the originalLearningDesign field in the new design
	 * @return new LearningDesign   
	 */	
	public LearningDesign copyLearningDesign(Long originalLearningDesignID,Integer copyType,
											 Integer userID, Integer workspaceFolder, boolean setOriginalDesign)throws UserException, LearningDesignException,
											 										 WorkspaceFolderException, IOException;
	/**
	 * @return List Returns the list of all the available LearningDesign's   
	 * */
	public List getAllLearningDesigns();
	/**
	 * Saves the LearningDesign to the database. Will update if already saved.
	 * @param learningDesign The LearningDesign to be saved 
	 * */
	public void saveLearningDesign(LearningDesign learningDesign);
	 
	/**
	 * @return List Returns a list of all available Learning Libraries
	 */
	public List getAllLearningLibraries();
	
	/**
	 * Returns a string representing the requested LearningDesign in WDDX format
	 * 
	 * @param learningDesignID The learning_design_id of the design whose WDDX packet is requested 
	 * @return String The requested LearningDesign in WDDX format
	 * @throws Exception
	 */
	public String getLearningDesignDetails(Long learningDesignID)throws IOException;
	
	/**
	 * This method saves the information which comes in WDDX format 
	 * into the database. It returns An acknowledgemnet message 
	 * telling FLASH the status of the operation, i.e. whether the 
	 * design has been successfully saved or not. If Yes it returns 
	 * the learning_design_id of the design just saved. This information
	 * is sent back in a format understood by FLASH, WDDX.
	 * 
	 * @param wddxPacket The WDDX packet to be stored in the database
	 * @return String The acknowledgemnet message 
	 * @throws Exception
	 */
	public String storeLearningDesignDetails(String wddxPacket)throws Exception;
	
	/**
	 * This method returns a list of all available Learning Designs
	 * in WDDX format.
	 * 
	 * @return String The required list in WDDX format
	 * @throws IOException
	 */
	public String getAllLearningDesignDetails()throws IOException;
	
	/**
	 * Returns a list of LearningDesign's 
	 * in WDDX format, belonging to the given user
	 *  
	 * @param user The user_id of the User for whom the designs are to be fetched 
	 * @return The requested list of LearningDesign's in WDDX format
	 * @throws IOException
	 */
	public String getLearningDesignsForUser(Long userID) throws IOException;
	
	/**
	 * This method returns a list of all available system libraries in
	 * WDDX format.
	 * 
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public String getAllLearningLibraryDetails()throws IOException;
	
	/**
	 * Returns a string representing the new tool content id in 
	 * WDDX format.
	 * 
	 * Typically, when a user clicks on an activity to edit the tool contnet,
	 * it must have a tool content id passed to it. This method uses the 
	 * ToolContentIDGenerator to generate the new tool content id and passes
	 * this value back to flash in WDDX format. 
	 * 
	 * @param toolID The toolID in which to generate the new tool content id for
	 * @return String The new tool content id in WDDX Format
	 */
	public String getToolContentID(Long toolID) throws IOException;

	/** Get the available licenses. This will include our supported Creative Common
	 * licenses and an "OTHER" license which may be used for user entered license details.
	 * The picture url supplied should be a full URL i.e. if it was a relative URL in the 
	 * database, it should have been converted to a complete server URL (starting http://)
	 * before sending to the client.
	 * 
	 * @return Vector of LicenseDTO objects. It is a Vector to ensure compatibility with WDDX 
	 */
	public Vector getAvailableLicenses();
	
	/** Delete a learning design from the database. Does not remove any content stored in tools - 
	 * that is done by the LamsCoreToolService */
	public void deleteLearningDesign(LearningDesign design);

}

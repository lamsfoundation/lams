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
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Iterator;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 * 
 * This class acts as a data transfer object for transfering 
 * information between FLASH and the core module. It passes 
 * information about all available Learning Libraries.
 * 
 */
public class LearningLibraryDTO extends BaseDTO {
	
	private Long learningLibraryID;
	private String description;
	private String title;
	private Boolean validFlag;
	private Vector templateActivities;
	
	public LearningLibraryDTO(){
		
	}
	public LearningLibraryDTO(Long learningLibraryID, String description,
			String title, Boolean validFlag,
			Vector templateActivities) {		
		this.learningLibraryID = learningLibraryID;
		this.description = description;
		this.title = title;
		this.validFlag = validFlag;		
		this.templateActivities = templateActivities;
	}
	public LearningLibraryDTO(LearningLibrary learningLibrary){
		this.learningLibraryID = learningLibrary.getLearningLibraryId();
		this.description = learningLibrary.getDescription();
		this.title = learningLibrary.getTitle();
		this.validFlag = learningLibrary.getValidLibrary();		
		this.templateActivities = populateActivities(learningLibrary.getActivities().iterator());
	}	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the learningLibraryID.
	 */
	public Long getLearningLibraryID() {
		return learningLibraryID!=null?learningLibraryID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title!=null?title:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the validFlag.
	 */
	public Boolean getValidFlag() {
		return validFlag!=null?validFlag:WDDXTAGS.BOOLEAN_NULL_VALUE;
	}
	
	/**
	 * @return Returns the activities.
	 */
	public Vector getTemplateActivities() {
		return templateActivities;
	}
	public Vector populateActivities(Iterator iterator){		
		Vector activities = new Vector();
		while(iterator.hasNext()){
			Activity activity = (Activity)iterator.next();
			activities.add(activity.getLibraryActivityDTO());
		}		
		return activities;		
	}
}

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

import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.tool.dto.AuthoringToolDTO;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 * 
 * This class acts as a data transfer object for transfering 
 * information between FLASH and the core module. This class
 * passes information about the template activities(tool activities)
 * assocaited with a given Learning Library.
 * 
 * Every Learning Library has a Tool activity (also known as template
 * activity) associated with it. And every tool activity has its own Tool.
 * 
 * This is utility class required by the authoring enviorment to pass
 * information about installed libraries to populate the tool panel 
 * on the left hand side of the Authoring Canvas. 
 *   
 */
public class LibraryActivityDTO extends BaseDTO {
	
	/*****************************************************************************
	 * Attributes
	 *****************************************************************************/
	
	/** identifier field */
	private Long activityID;

	/** FLASH generated value. Unique per LearningDesign.
	 * Required by flash only.*/
	private Integer activityUIID;

	/** Description of the activity*/
	private String description;

	/** Title of the activity*/
	private String title;
	
	/** Help text for the activity*/
	private String helpText;

	/** UI specific attribute indicating the
	 * position of the activity*/
	private Integer xCoord;

	/** UI specific attribute indicating the
	 * position of the activity*/
	private Integer yCoord;
	
	/** The image that represents the icon of this 
	 * activity in the UI*/
	private String libraryActivityUiImage;
	
	/** The type of activity */
	private Integer activityTypeID;
	
	/** The category of activity */
	private Integer activityCategoryID;
	
	private AuthoringToolDTO tool;
	
	/*****************************************************************************
	 * Constructors 
	 *****************************************************************************/
	
	public LibraryActivityDTO(Long activityId, Integer activityUIID,
			String description, String title, String helpText, Integer xcoord,
			Integer ycoord,String libraryActivityUiImage,AuthoringToolDTO tool) {
		super();
		this.activityID = activityId;
		this.activityUIID = activityUIID;
		this.description = description;
		this.title = title;
		this.helpText = helpText;
		this.xCoord = xcoord;
		this.yCoord = ycoord;		
		this.libraryActivityUiImage = libraryActivityUiImage;
		this.tool = tool;
	}
	public LibraryActivityDTO(ToolActivity activity){
		this.activityID = activity.getActivityId();
		this.activityUIID = activity.getActivityUIID();
		this.description = activity.getDescription();
		this.title = activity.getTitle();
		this.helpText = activity.getHelpText();
		this.xCoord = activity.getXcoord();
		this.yCoord = activity.getYcoord();			
		this.libraryActivityUiImage = activity.getLibraryActivityUiImage();
		this.activityTypeID = activity.getActivityTypeId();
		this.activityCategoryID = activity.getActivityCategoryID();
		this.tool = activity.getTool().getAuthoringToolDTO();
	}
		
	/*****************************************************************************
	 * Setter Methods
	 *****************************************************************************/
	
	
	/**
	 * @return Returns the activityID.
	 */
	public Long getActivityID() {
		return activityID!=null?activityID:WDDXTAGS.NUMERIC_NULL_VALUE_LONG;
	}	
	/**
	 * @return Returns the activityUIID.
	 */
	public Integer getActivityUIID() {
		return activityUIID!=null?activityUIID:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}	
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description!=null?description:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the helpText.
	 */
	public String getHelpText() {
		return helpText!=null?helpText:WDDXTAGS.STRING_NULL_VALUE;
	}
	/**
	 * @return Returns the libraryActivityUiImage.
	 */
	public String getLibraryActivityUiImage() {
		return libraryActivityUiImage!=null?libraryActivityUiImage:WDDXTAGS.STRING_NULL_VALUE;
	}	
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title!=null?title:WDDXTAGS.STRING_NULL_VALUE;
	}	
	/**
	 * @return Returns the xcoord.
	 */
	public Integer getxCoord() {
		return xCoord!=null?xCoord:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}
	/**
	 * @return Returns the ycoord.
	 */
	public Integer getyCoord() {
		return yCoord!=null?yCoord:WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;
	}	
	/**
	 * @return Returns the tool.
	 */
	public AuthoringToolDTO getTool() {
		return tool;
	}
	
	/**
	 * @return Returns the activityCategoryID.
	 */
	public Integer getActivityCategoryID() {
		return activityCategoryID;
	}
	/**
	 * @return Returns the activityTypeID.
	 */
	public Integer getActivityTypeID() {
		return activityTypeID;
	}
}

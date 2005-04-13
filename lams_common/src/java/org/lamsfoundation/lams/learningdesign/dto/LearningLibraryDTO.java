/*
 * Created on Apr 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LearningLibraryDTO extends BaseDTO {
	
	private Long learningLibraryID;
	private String description;
	private String title;
	private Boolean validFlag;
	private Date createDateTime;
	
	public LearningLibraryDTO(){
		
	}
	public LearningLibraryDTO(Long learningLibraryID, String description,
			String title, Boolean validFlag, Date createDateTime) {		
		this.learningLibraryID = learningLibraryID;
		this.description = description;
		this.title = title;
		this.validFlag = validFlag;
		this.createDateTime = createDateTime;
	}
	public LearningLibraryDTO(LearningLibrary learningLibrary){
		this.learningLibraryID = learningLibrary.getLearningLibraryId();
		this.description = learningLibrary.getDescription();
		this.title = learningLibrary.getTitle();
		this.validFlag = learningLibrary.getValidLibrary();
		this.createDateTime = learningLibrary.getCreateDateTime();		
	}
	/**
	 * @return Returns the createDateTime.
	 */
	public Date getCreateDateTime() {
		return createDateTime!=null?createDateTime:WDDXTAGS.DATE_NULL_VALUE;
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
}

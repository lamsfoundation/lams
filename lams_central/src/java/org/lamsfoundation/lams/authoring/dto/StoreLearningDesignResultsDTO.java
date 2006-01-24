package org.lamsfoundation.lams.authoring.dto;

import java.util.Vector;

/**
 * DTO created so the valid flag and any other messages
 * can be sent back to Flash.
 * 
 * @author mtruong
 *
 */
public class StoreLearningDesignResultsDTO {
	
	private boolean valid; //indicates whether learning design is valid or not
	
	/*
	 * If the design is valid, then the learningDesignID will be returned.
	 * Otherwise will return a list of ValidationErrorDTOs
	 */
	private Vector messages; 
	
	public StoreLearningDesignResultsDTO()
	{}
	
	public StoreLearningDesignResultsDTO(boolean valid, Vector messages)
	{
		this.valid = valid;
		this.messages = messages;
	}

	
	public StoreLearningDesignResultsDTO(boolean valid, Long learningDesignID)
	{
		this.valid = valid;
		this.messages = new Vector();
		messages.add(learningDesignID);
	}
	
	

	public boolean isValid() {
		return valid;
	}
	
	public Vector getMessages() {
		return messages;
	}
}

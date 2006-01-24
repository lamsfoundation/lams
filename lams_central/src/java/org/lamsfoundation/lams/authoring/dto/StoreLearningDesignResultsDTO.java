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
	
	private Long learningDesignID;
	
	/*
	 * If the design is valid, then the learningDesignID will be returned.
	 * Otherwise will return a list of ValidationErrorDTOs
	 */
	private Vector messages; 
	
	public StoreLearningDesignResultsDTO()
	{}
	
	public StoreLearningDesignResultsDTO(boolean valid, Vector messages, Long learningDesignID)
	{
		this.valid = valid;
		this.messages = messages;
		this.learningDesignID = learningDesignID;
	}

	
	public StoreLearningDesignResultsDTO(boolean valid, Long learningDesignID)
	{
		this.valid = valid;
		this.learningDesignID = learningDesignID;
	}
	
	public Long getLearningDesignID()
	{
		return learningDesignID;
	}

	public boolean isValid() {
		return valid;
	}
	
	public Vector getMessages() {
		return messages;
	}
}

package org.lamsfoundation.lams.authoring.dto;

import java.util.Vector;

public class StoreLearningDesignResultsDTO {
	
	private boolean valid;
	
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

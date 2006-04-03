/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
/* $$Id$$ */
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

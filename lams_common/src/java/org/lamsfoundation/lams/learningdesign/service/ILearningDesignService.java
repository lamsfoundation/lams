/***************************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;

/**
 * @author Mitchell Seaton
 */

public interface ILearningDesignService{
	
	/**
	 * Get the learning design DTO, suitable to send to Flash via WDDX 
	 * @param learningDesignId
	 * @return LearningDesignDTO
	 */
	public LearningDesignDTO getLearningDesignDTO(Long learningDesignID);

	/**
	 * This method calls other validation methods which apply the validation 
	 * rules to determine whether or not the learning design is valid.
	 *
	 * @param learningDesign
	 * @return list of validation errors
	 */
	public Vector<ValidationErrorDTO> validateLearningDesign(LearningDesign learningDesign);
	
	/**
	 * Get the DTO list of all valid learning libraries, which equals getAllLearningLibraryDetails(true) method.
	 * @return list of LearningLibraryDTO
	 * @throws IOException
	 */
	public ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails() throws IOException;
	/**
	 * Get the DTO list of all learning libraries whatever it is valid or not.
	 * @param valid
	 * @return
	 * @throws IOException
	 */
	public ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails(boolean valid) throws IOException;
	
	/**
	 * Set valid flag to learning library.
	 * @param learningLibraryId
	 * @param valid
	 */
	public void setValid(Long learningLibraryId, boolean valid);
	
}

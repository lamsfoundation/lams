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

package org.lamsfoundation.lams.learningdesign.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.LearningLibraryGroup;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.tool.dto.ToolDTO;

/**
 * @author Mitchell Seaton
 */

public interface ILearningDesignService {

    /**
     * Get the learning design DTO
     */
    LearningDesignDTO getLearningDesignDTO(Long learningDesignID, String languageCode);

    /**
     * This method calls other validation methods which apply the validation rules to determine whether or not the
     * learning design is valid.
     */
    Vector<ValidationErrorDTO> validateLearningDesign(LearningDesign learningDesign);

    /**
     * Get the DTO list of all valid learning libraries, which equals getAllLearningLibraryDetails(true) method.
     */
    ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails(String languageCode) throws IOException;

    /**
     * Get the DTO list of all learning libraries whatever it is valid or not.
     */
    ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails(boolean valid, String languageCode) throws IOException;

    LearningLibrary getLearningLibrary(Long learningLibraryId);

    /**
     * Gets all existing learning library groups.
     */
    List<LearningLibraryGroup> getLearningLibraryGroups();

    void saveLearningLibraryGroups(Collection<LearningLibraryGroup> groups);

    /**
     * Set valid flag to learning library.
     */
    void setValid(Long learningLibraryId, boolean valid);

    List<ToolDTO> getToolDTOs(boolean includeParallel, boolean includeInvalid, String userName) throws IOException;

    void fillLearningLibraryID(AuthoringActivityDTO activity);
    
    String internationaliseActivityTitle(Long learningLibraryID);
}

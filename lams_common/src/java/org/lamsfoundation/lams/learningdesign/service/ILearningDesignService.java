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
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.tool.dto.ToolDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;

/**
 * @author Mitchell Seaton
 */

public interface ILearningDesignService {
    static final String LD_SVG_TOP_DIR = FileUtil.getFullPath(Configuration.get(ConfigurationKeys.LAMS_EAR_DIR),
	    "lams-www.war\\secure\\learning-design-images");

    /**
     * Returns a populated LearningDesign object corresponding to the given learningDesignID
     *
     * @param learningDesignID
     *            The learning_design_id of the design which has to be fetched
     * @return LearningDesign The populated LearningDesign object corresponding to the given learningDesignID
     */
    LearningDesign getLearningDesign(Long learningDesignID);

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
     * Set valid flag to learning library.
     */
    void setValid(Long learningLibraryId, boolean valid);

    List<ToolDTO> getToolDTOs(boolean includeParallel, boolean includeInvalid, String userName) throws IOException;

    void fillLearningLibraryID(AuthoringActivityDTO activity, Collection<AuthoringActivityDTO> activities)
	    throws ImportToolContentException;

    String internationaliseActivityTitle(Long learningLibraryID);

    /**
     * Get a unique name for a learning design, based on the names of the learning designs in the folder. If the
     * learning design has duplicated name in same folder, then the new name will have a timestamp. The new name format
     * will be oldname_ddMMYYYY_idx. The idx will be auto incremental index number, start from 1. Warning - this may be
     * quite intensive as it gets all the learning designs in a folder. Moved from AuthoringService to here so that the
     * Import code can use it.
     *
     *
     * @param originalLearningDesign
     * @param workspaceFolder
     * @param copyType
     * @return
     */
    String getUniqueNameForLearningDesign(String originalTitle, Integer workspaceFolderId);

}

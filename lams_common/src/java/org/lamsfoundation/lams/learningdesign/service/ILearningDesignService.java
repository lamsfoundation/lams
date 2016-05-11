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

import org.apache.batik.transcoder.TranscoderException;
import org.jdom.JDOMException;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.LearningLibraryGroup;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningLibraryDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.tool.dto.ToolDTO;

/**
 * @author Mitchell Seaton
 */

public interface ILearningDesignService {

    /**
     * Get the learning design DTO, suitable to send to Flash via WDDX
     *
     * @param learningDesignId
     * @param languageCode
     *            Two letter language code needed to I18N the help url
     * @return LearningDesignDTO
     */
    public LearningDesignDTO getLearningDesignDTO(Long learningDesignID, String languageCode);

    /**
     * This method calls other validation methods which apply the validation rules to determine whether or not the
     * learning design is valid.
     *
     * @param learningDesign
     * @return list of validation errors
     */
    public Vector<ValidationErrorDTO> validateLearningDesign(LearningDesign learningDesign);

    /**
     * Get the DTO list of all valid learning libraries, which equals getAllLearningLibraryDetails(true) method.
     *
     * @return list of LearningLibraryDTO
     * @throws IOException
     */
    public ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails(String languageCode) throws IOException;

    /**
     * Get the DTO list of all learning libraries whatever it is valid or not.
     *
     * @param valid
     * @return
     * @throws IOException
     */
    public ArrayList<LearningLibraryDTO> getAllLearningLibraryDetails(boolean valid, String languageCode)
	    throws IOException;

    public LearningLibrary getLearningLibrary(Long learningLibraryId);

    /**
     * Gets all existing learning library groups.
     */
    public List<LearningLibraryGroup> getLearningLibraryGroups();

    public void saveLearningLibraryGroups(Collection<LearningLibraryGroup> groups);

    /**
     * Set valid flag to learning library.
     *
     * @param learningLibraryId
     * @param valid
     */
    public void setValid(Long learningLibraryId, boolean valid);

    /**
     * Creates learning design SVG/PNG file. Also stores it into the file system for caching.
     *
     * @param learningDesignId
     *            source learning design for the outcome image
     * @param imageFormat
     *            it can be either SVGGenerator.OUTPUT_FORMAT_SVG or SVGGenerator.OUTPUT_FORMAT_PNG
     * @return
     * @throws JDOMException
     * @throws IOException
     * @throws TranscoderException
     */
    String createLearningDesignSVG(Long learningDesignId, int imageFormat) throws IOException;

    String createBranchingSVG(Long branchingActivityId, int imageFormat) throws IOException;

    public List<ToolDTO> getToolDTOs(boolean includeParallel, String userName) throws IOException;
}

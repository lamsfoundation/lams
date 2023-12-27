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

package org.lamsfoundation.lams.authoring.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.lamsfoundation.lams.authoring.IAuthoringService;
import org.lamsfoundation.lams.authoring.ObjectExtractorException;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAccess;
import org.lamsfoundation.lams.learningdesign.dto.LicenseDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.tool.dto.ToolOutputDefinitionDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;

/**
 * @author Manpreet Minhas
 */
public interface IAuthoringFullService extends IAuthoringService {

    static int LEARNING_DESIGN_ACCESS_ENTRIES_LIMIT = 7;

    /**
     * Create a copy of learning design as per the requested learning design and saves it in the given workspacefolder.
     * Designed to be called when user tries to copy a learning design using the Authoring interface. Does not set the
     * original learning design field, so it should not be used for creating lesson learning designs.
     *
     * @param originalLearningDesingID
     *            the source learning design id.
     * @param copyType
     *            purpose of copying the design. Can have one of the follwing values
     *            <ul>
     *            <li>LearningDesign.COPY_TYPE_NONE (for authoring enviornment)</li>
     *            <li>LearningDesign.COPY_TYPE_LESSON (for monitoring enviornment while creating a Lesson)</li>
     *            <li>LearningDesign.COPY_TYPE_PREVIEW (for previewing purposes)</li>
     *            </ul>
     * @param userID
     *            The user_id of the user who has sent this request(author/teacher)
     * @param workspaceFolderID
     *            The workspacefolder where this copy of the design would be saved
     * @param setOriginalDesign
     *            If true, then sets the originalLearningDesign field in the new design
     * @return new LearningDesign
     */
    LearningDesign copyLearningDesign(Long originalLearningDesignID, Integer copyType, Integer userID,
	    Integer workspaceFolder, boolean setOriginalDesign)
	    throws UserException, LearningDesignException, WorkspaceFolderException, IOException;

    LearningDesign saveLearningDesignDetails(ObjectNode ldJSON)
	    throws UserException, WorkspaceFolderException, ObjectExtractorException, ParseException;

    /**
     * Validate the learning design, updating the valid flag appropriately.
     *
     * This needs to be run in a separate transaction to storeLearningDesignDetails to ensure the database is fully
     * updated before the validation occurs (due to some quirks we are finding using Hibernate)
     *
     * @param learningDesignId
     * @throws Exception
     */
    Vector<ValidationErrorDTO> validateLearningDesign(Long learningDesignId);

    /**
     * This method returns a output definitions of the Tool.
     */
    List<ToolOutputDefinitionDTO> getToolOutputDefinitions(Long toolContentID, int definitionType);

    Long insertToolContentID(Long toolID);

    /**
     * Calls an appropriate tool to copy the content indicated by toolContentId. Returns the new tool content id.
     *
     * The is called when the user copies and pastes a tool activity icon in authoring. It should only be called on a
     * ToolActivity - never a Gate or Grouping or Complex activity.
     *
     * @param toolContentID
     *            The toolContentID indicating the content to copy
     * @param customCSV
     *            The customCSV if this is a tool adapter tool.
     * @return Long the new content id
     */
    Long copyToolContent(Long toolContentID, String customCSV) throws IOException;

    Long createToolContent(UserDTO user, String toolSignature, ObjectNode toolContentJSON) throws IOException;

    /**
     * Get the available licenses. This will include our supported Creative Common licenses and an "OTHER" license which
     * may be used for user entered license details. The picture url supplied should be a full URL i.e. if it was a
     * relative URL in the database, it should have been converted to a complete server URL (starting http://) before
     * sending to the client.
     *
     * @return Vector of LicenseDTO objects.
     */
    Vector<LicenseDTO> getAvailableLicenses();

    /**
     *
     *
     * @param learningDesignID
     *            The learning_design_id of the design for which editing has finished.
     * @param userID
     *            user_id of the User who has finished editing the design.
     * @param cancelled
     *            flag specifying whether user cancelled or saved the edit
     * @throws IOException
     * @throws Exception
     */
    void finishEditOnFly(Long learningDesignID, Integer userID, boolean cancelled) throws Exception;

    String getToolAuthorUrl(Long toolID, Long toolContentID, String contentFolderID);

    Long insertSingleActivityLearningDesign(String learningDesignTitle, Long toolID, Long toolContentID,
	    Long learningLibraryID, String contentFolderID, Integer organisationID);

    List<LearningDesignAccess> updateLearningDesignAccessByUser(Integer userId);

    void storeLearningDesignAccess(Long learningDesignId, Integer userId);

    Long createTblAssessmentToolContent(UserDTO user, String title, String instructions,
	    boolean selectLeaderToolOutput, boolean enableNumbering, boolean shuffleQuestions,
	    boolean enableConfidenceLevels, boolean allowDiscloseAnswers, boolean allowAnswerJustification,
	    boolean enableDiscussionSentiment, Long qbCollectionUid, ArrayNode questions) throws IOException;
}
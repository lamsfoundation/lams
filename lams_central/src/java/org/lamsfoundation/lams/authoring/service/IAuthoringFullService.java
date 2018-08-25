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

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.IAuthoringService;
import org.lamsfoundation.lams.authoring.ObjectExtractorException;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAccess;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.tool.dto.ToolOutputDefinitionDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Manpreet Minhas
 */
public interface IAuthoringFullService extends IAuthoringService {

    /** Message key returned by the storeLearningDesignDetails() method */
    public static final String STORE_LD_MESSAGE_KEY = "storeLearningDesignDetails";

    public static final String INSERT_LD_MESSAGE_KEY = "insertLearningDesign";

    public static final String START_EDIT_ON_FLY_MESSAGE_KEY = "startEditOnFly";

    public static final String COPY_TOOL_CONTENT_MESSAGE_KEY = "copyMultipleToolContent";

    /**
     * Returns a populated LearningDesign object corresponding to the given learningDesignID
     *
     * @param learningDesignID
     *            The learning_design_id of the design which has to be fetched
     * @return LearningDesign The populated LearningDesign object corresponding to the given learningDesignID
     */
    LearningDesign getLearningDesign(Long learningDesignID);

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

    /**
     * Insert a learning design into another learning design. This is a copy and paste type of copy - it just dumps the
     * contents (with modified activity ui ids) in the main learning design. It doesn't wrap up the contents in a
     * sequence activity. Always sets the type to COPY_TYPE_NONE.
     *
     * @param originalDesignID
     *            The design to be "modified". Required.
     * @param designToImportID
     *            The design to be imported into originalLearningDesign. Required.
     * @param userId
     *            Current User. Required.
     * @param customCSV
     *            The custom CSV required to insert tool adapter tools, so their content can be copied in the external
     *            server
     * @param createNewLearningDesign
     *            If true, then a copy of the originalLearningDesign is made and the copy modified. If it is false, then
     *            the originalLearningDesign is modified. Required.
     * @param newDesignName
     *            New name for the design if a new design is being create. Optional.
     * @param workspaceFolderID
     *            The folder in which to put the new learning design if createNewLearningDesign = true. May be null if
     *            createNewLearningDesign = false
     * @return New / updated learning design
     */
    LearningDesign insertLearningDesign(Long originalDesignID, Long designToImportID, Integer userID,
	    boolean createNewLearningDesign, String newDesignName, Integer workspaceFolderID, String customCSV)
	    throws UserException, LearningDesignException, WorkspaceFolderException, IOException;

    LearningDesign saveLearningDesignDetails(JSONObject ldJSON)
	    throws UserException, JSONException, WorkspaceFolderException, ObjectExtractorException, ParseException;

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
     *
     * @param learningDesignId
     * @return
     */
    Vector<AuthoringActivityDTO> getToolActivities(Long learningDesignId, String languageCode);

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

    /**
     * Get the available licenses. This will include our supported Creative Common licenses and an "OTHER" license which
     * may be used for user entered license details. The picture url supplied should be a full URL i.e. if it was a
     * relative URL in the database, it should have been converted to a complete server URL (starting http://) before
     * sending to the client.
     *
     * @return Vector of LicenseDTO objects.
     */
    Vector<License> getAvailableLicenses();

    /**
     * Delete a learning design from the database. Does not remove any content stored in tools - that is done by the
     * LamsCoreToolService
     */
    void deleteLearningDesign(LearningDesign design);

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

    /** Get the message service, which gives access to the I18N text */
    MessageService getMessageService();

    String getToolAuthorUrl(Long toolID, Long toolContentID, String contentFolderID);

    Long insertSingleActivityLearningDesign(String learningDesignTitle, Long toolID, Long toolContentID,
	    Long learningLibraryID, String contentFolderID, Integer organisationID);

    List<LearningDesignAccess> updateLearningDesignAccessByUser(Integer userId);

    void storeLearningDesignAccess(Long learningDesignId, Integer userId);
}
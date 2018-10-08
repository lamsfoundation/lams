package org.lamsfoundation.lams.authoring;

import java.io.IOException;

import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;

public interface IAuthoringService {

    /**
     * Prepares a LearningDesign to be ready for Edit-On-The-Fly (Editing).
     */
    void setupEditOnFlyGate(Long learningDesignID, Integer userID)
	    throws UserException, LearningDesignException, IOException;

    boolean setupEditOnFlyLock(Long learningDesignID, Integer userID)
	    throws LearningDesignException, UserException, IOException;
    
    /**
     * Create a copy of learning design as per the requested learning design and saves it in the given workspacefolder.
     * Does not set the original
     *
     * @param originalLearningDesign
     *            The source learning design id.
     * @param copyType
     *            purpose of copying the design. Can have one of the follwing values
     *            <ul>
     *            <li>LearningDesign.COPY_TYPE_NONE (for authoring enviornment)</li>
     *            <li>LearningDesign.COPY_TYPE_LESSON (for monitoring enviornment while creating a Lesson)</li>
     *            <li>LearningDesign.COPY_TYPE_PREVIEW (for previewing purposes)</li>
     *            </ul>
     * @param user
     *            The user who has sent this request(author/teacher)
     * @param setOriginalDesign
     *            If true, then sets the originalLearningDesign field in the new design
     * @param custom
     *            comma separated values used for tool adapters
     * @return LearningDesign The new copy of learning design.
     */
    LearningDesign copyLearningDesign(LearningDesign originalLearningDesign, Integer copyType, User user,
	    WorkspaceFolder workspaceFolder, boolean setOriginalDesign, String newDesignName, String customCSV);

    Grouping getGroupingById(Long groupingID);

    /**
     * This method returns the root workspace folder for a particular user.
     *
     * @param userID
     *            The <code>user_id</code> of the user for whom the folders have to fetched
     * @return FolderContentDTO for the user's root workspace folder
     * @throws IOException
     */
    FolderContentDTO getUserWorkspaceFolder(Integer userID) throws IOException;
}

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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAccess;

/**
 * @author Manpreet Minhas
 */
public interface ILearningDesignDAO extends IBaseDAO {

    /**
     * @param learningDesignId
     * @return LearningDesign populated LearningDesign object
     */
    public LearningDesign getLearningDesignById(Long learningDesignId);

    /**
     * This method returns a list of all designs that are valid in
     * the given workspaceFolder.
     *
     * @param workspaceFolderID
     *            The workspace_folder_id of the WorkspaceFolder
     *            from where the designs have to be fetched.
     * @return List The List of all available designs
     */
    public List getAllValidLearningDesignsInFolder(Integer workspaceFolderID);

    /**
     * This method returns a list of all available designs in
     * the given workspaceFolder.
     *
     * @param workspaceFolderID
     *            The workspace_folder_id of the WorkspaceFolder
     *            from where the designs have to be fetched.
     * @return List The List of all available designs
     */
    public List getAllLearningDesignsInFolder(Integer workspaceFolderID);

    /**
     * This method returns a List of Learning Designs with given
     * <code>original_learning_design_id</code>
     *
     * @param originalDesignID
     *            The <code>original_learning_design_id</code>
     * @return List The List of all corresponding Learning designs with
     *         given <code>original_learning_design_id</code>
     */
    public List getLearningDesignsByOriginalDesign(Long originalDesignID);

    /**
     * Get the titles of all the learning designs with the given prefix in the given folder.
     */
    public List getLearningDesignTitlesByWorkspaceFolder(Integer workspaceFolderID, String prefix);

    public List<LearningDesignAccess> getLearningDesignAccess(Integer userId);

    public LearningDesignAccess getLearningDesignAccess(Long learningDesignID, Integer userId);

    /**
     * Get a portion of the learning designs in the given folder. If page & size are null, the effect the same data
     * is returned as getAllLearningDesignsInFolder but sorted.
     */
    public List<LearningDesign> getAllPagedLearningDesigns(Integer workspaceFolderID, Integer page, Integer size,
	    String sortName, String sortDate);

    /**
     * Get a portion of the valid learning designs in the given folder. If page & size are null, the effect the same
     * data
     * is returned as getAllValidLearningDesignsInFolder but sorted.
     */
    public List<LearningDesign> getValidPagedLearningDesigns(Integer workspaceFolderID, Integer page, Integer size,
	    String sortName, String sortDate);

    /**
     * Count how many learning designs exist in a given folder.
     */
    public long countAllLearningDesigns(Integer workspaceFolderID, boolean validDesignsOnly);

}
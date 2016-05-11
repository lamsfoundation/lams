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

package org.lamsfoundation.lams.workspace.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;

/**
 * @author Manpreet Minhas
 */
public interface IWorkspaceFolderContentDAO extends IBaseDAO {

    /**
     * This method returns the <code>WorkspaceFolderContent</code>
     * corresponding to the given <code>folderContentID</code>
     * 
     * @param folderContentID
     *            The <code>folder_content_id</code> of the required object
     * @return WorkspaceFolderContent The populated <code>WorkspaceFolderContent</code> object
     */
    public WorkspaceFolderContent getWorkspaceFolderContentByID(Long folderContentID);

    /**
     * This method deletes the content with the given UUID, versionID
     * from the content table.
     * 
     * @param uuid
     *            The UUID of the content to be deleted
     * @param versionID
     *            The versionID of the content to be deleted
     * @param folderContentID
     *            The folder_content_id to be deleted
     * @return
     */
    public int deleteContentWithVersion(Long uuid, Long versionID, Long folderContentID);

}

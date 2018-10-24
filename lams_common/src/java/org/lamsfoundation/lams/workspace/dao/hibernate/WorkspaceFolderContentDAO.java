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

package org.lamsfoundation.lams.workspace.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO;
import org.springframework.stereotype.Repository;

/**
 * @author Manpreet Minhas
 */
@Repository
public class WorkspaceFolderContentDAO extends LAMSBaseDAO implements IWorkspaceFolderContentDAO {

    private static final String TABLENAME = "lams_workspace_folder_content";

    private static final String DELETE_BY_VERSION = "from " + TABLENAME + " in class "
	    + WorkspaceFolderContent.class.getName() + " where folder_content_id=:folder_content_id AND uuid=:uuid AND version_id=:version_id";

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO#getWorkspaceFolderContentByID(java.lang.Long)
     */
    @Override
    public WorkspaceFolderContent getWorkspaceFolderContentByID(Long folderContentID) {
	return (WorkspaceFolderContent) super.find(WorkspaceFolderContent.class, folderContentID);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO#deleteContentWithVersion(java.lang.Long,
     *      java.lang.Long, java.lang.Long)
     */
    @Override
    public int deleteContentWithVersion(Long uuid, Long versionID, Long folderContentID) {
	int numDeleted = 0;
	if (uuid != null && versionID != null && folderContentID != null) {
	    List list = getSessionFactory().getCurrentSession().createQuery(DELETE_BY_VERSION)
		    .setLong("folder_content_id", folderContentID.longValue()).setLong("uuid", uuid.longValue())
		    .setLong("version_id", versionID.longValue()).list();
	    if (list != null && list.size() > 0) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
		    Object element = iter.next();
		    getSessionFactory().getCurrentSession().delete(element);
		    numDeleted++;
		}
	    }
	}
	return numDeleted;
    }
}

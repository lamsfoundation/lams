/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.workspace.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO;

/**
 * @author Manpreet Minhas
 */
public class WorkspaceFolderContentDAO extends BaseDAO implements IWorkspaceFolderContentDAO {
	
	private static final String TABLENAME ="lams_workspace_folder_content";
	
	private static final String FIND_BY_TYPE_IN_FOLDER = "from " + TABLENAME +" in class " +
														 WorkspaceFolderContent.class.getName() +
														 " where workspace_folder_id=? AND mime_type=?";
	
	private static final String DELETE_BY_VERSION = "from " + TABLENAME +" in class " +
													WorkspaceFolderContent.class.getName() +
													" where folder_content_id=? AND uuid=? AND version_id=?";

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO#getWorkspaceFolderContentByID(java.lang.Long)
	 */
	public WorkspaceFolderContent getWorkspaceFolderContentByID(Long folderContentID) {
		return (WorkspaceFolderContent) super.find(WorkspaceFolderContent.class, folderContentID);
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO#deleteContentWithVersion(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	public int deleteContentWithVersion(Long uuid, Long versionID, Long folderContentID){
		int numDeleted = 0;
		if ( uuid != null && versionID != null && folderContentID != null ) {
			List list = this.getSession().createQuery(DELETE_BY_VERSION)
				.setLong(0, folderContentID.longValue())
				.setLong(1, uuid.longValue())
				.setLong(2,versionID.longValue())
				.list();
			if ( list != null && list.size() > 0 ) {
				Iterator iter = list.iterator();
				while (iter.hasNext()) {
					Object element = (Object) iter.next();
					this.getSession().delete(element);
					numDeleted++;
				}
			}
		}
		return numDeleted;
	}
}

/*
 * Created on Apr 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkspaceFolderContentDAO extends BaseDAO implements IWorkspaceFolderContentDAO {
	
	private static final String TABLENAME ="lams_workspace_folder_content";
	
	private static final String FIND_BY_FOLDER ="from " + TABLENAME +" in class " + 
												WorkspaceFolderContent.class.getName() +
												" where workspace_folder_id=?";
	
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
	 * @see org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO#getContentByWorkspaceFolder(java.lang.Long)
	 */
	public List getContentByWorkspaceFolder(Long workspaceFolderID) {
		return this.getHibernateTemplate().find(FIND_BY_FOLDER,workspaceFolderID);
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO#getContentByTypeFromWorkspaceFolder(java.lang.Long, java.lang.String)
	 */
	public List getContentByTypeFromWorkspaceFolder(Long workspaceFolderID,String mimeType) {
		List list = this.getHibernateTemplate().find(FIND_BY_TYPE_IN_FOLDER,
													 new Object[]{workspaceFolderID,mimeType},
													 new Type[]{Hibernate.LONG,Hibernate.STRING}); 
		if(list==null||list.size()==0)
			return null;
		else
			return list;
	}
	public int deleteContentWithVersion(Long uuid, Long versionID, Long folderContentID){
		return this.getHibernateTemplate().delete(DELETE_BY_VERSION,
										   new Object[]{folderContentID,uuid,versionID},
										   new Type[]{Hibernate.LONG,Hibernate.LONG,Hibernate.LONG});		
		
	}
}

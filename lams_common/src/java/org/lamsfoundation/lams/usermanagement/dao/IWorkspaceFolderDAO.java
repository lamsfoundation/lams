/*
 * Created on Feb 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;

/**
 * @author Manpreet Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IWorkspaceFolderDAO extends IBaseDAO{	
	public WorkspaceFolder getWorkspaceFolderByID(Integer workspaceFolderID);
	
	public WorkspaceFolder getRunSequencesFolderForUser(Integer userID);
	public List getWorkspaceFolderByParentFolder(Integer parentFolderID);
	public List getWorkspaceFolderByUser(Integer userID);
}

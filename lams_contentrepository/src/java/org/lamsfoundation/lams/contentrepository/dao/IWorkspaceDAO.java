/* 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt 
*/

package org.lamsfoundation.lams.contentrepository.dao;

import java.io.Serializable;
import java.util.List;

import org.lamsfoundation.lams.contentrepository.CrWorkspace;
import org.lamsfoundation.lams.learningdesign.dao.IBaseDAO;


/**
 * Data access routines for Workspaces.
 * 
 * @author Fiona Malikoff
 */
public interface IWorkspaceDAO extends IBaseDAO {

	/** Get a workspace. 
	 * 
	 * @param workspaceName
	 * @return first (and expected only) workspace with this name. 
	 */
	public CrWorkspace findByName(String workspaceName);
	
	/** Get all the nodes for a workspace.
	 * 
	 * Can't just get the workspace as the hibernate implementation 
	 * will lazy load the nodes, and so getNodes() will be missing 
	 * the necessary info when workspace is returned to the calling 
	 * code. 
	 * 
	 * @param workspaceId
	 * @return first (and expected only) workspace with this name. 
	 */
	public List findWorkspaceNodes(Long workspaceId);

	/** Finds an object. Return null if not found (note: this
	 * is not the standard behaviour for Spring and Hibernate combined.)
	 * @param objClass
	 * @param id
	 * @return object built from database
	 */
	public Object find(Class objClass, Serializable id);

	
}

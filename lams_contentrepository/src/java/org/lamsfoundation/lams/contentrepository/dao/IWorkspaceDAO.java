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


package org.lamsfoundation.lams.contentrepository.dao;

import java.io.Serializable;
import java.util.List;

import org.lamsfoundation.lams.contentrepository.CrWorkspace;
import org.lamsfoundation.lams.dao.IBaseDAO;

/**
 * Data access routines for Workspaces.
 *
 * @author Fiona Malikoff
 */
public interface IWorkspaceDAO extends IBaseDAO {

    /** String used to define node factory in Spring context */
    public static final String WORKSPACE_DAO_ID = "workspaceDAO";

    /**
     * Get a workspace.
     * 
     * @param workspaceName
     * @return first (and expected only) workspace with this name.
     */
    public CrWorkspace findByName(String workspaceName);

    /**
     * Get all the nodes for a workspace.
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
}
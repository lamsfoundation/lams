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

import org.lamsfoundation.lams.contentrepository.CrCredential;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.IWorkspace;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryRuntimeException;
import org.lamsfoundation.lams.dao.IBaseDAO;

public interface ICredentialDAO extends IBaseDAO {

    /**
     * Check the supplied credential to the workspace.
     * By doing the check at this level, the password does
     * not need to be read from the database.
     * 
     * @return true if credential is found
     * @throws repository
     *             exception if an internal (db) error occurs
     */
    public abstract boolean checkCredential(ICredentials credential, IWorkspace workspace)
	    throws RepositoryRuntimeException;

    /**
     * Check the supplied credential to the repository
     * By doing the check at this level, the password does
     * not need to be read from the database.
     * 
     * @return true if credential is found
     * @throws repository
     *             exception if an internal (db) error occurs
     */
    public abstract boolean checkCredential(ICredentials credential) throws RepositoryRuntimeException;

    public abstract CrCredential findByName(String name);

}

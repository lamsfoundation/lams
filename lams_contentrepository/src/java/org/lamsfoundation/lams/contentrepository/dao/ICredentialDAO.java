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

import org.lamsfoundation.lams.contentrepository.CrCredential;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.IWorkspace;
import org.lamsfoundation.lams.contentrepository.RepositoryRuntimeException;

public interface ICredentialDAO {

	/** Check the supplied credential to the workspace.
	 * By doing the check at this level, the password does 
	 * not need to be read from the database.
	 * @return true if credential is found 
	 * @throws repository exception if an internal (db) error occurs
	 */
	public abstract boolean checkCredential(ICredentials credential, IWorkspace workspace) throws RepositoryRuntimeException; 

	/** Check the supplied credential to the repository
	 * By doing the check at this level, the password does 
	 * not need to be read from the database.
	 * @return true if credential is found 
	 * @throws repository exception if an internal (db) error occurs
	 */
	public abstract boolean checkCredential(ICredentials credential) throws RepositoryRuntimeException; 

	public abstract void insert(Object object);		

	public abstract void update(Object object);

	public abstract void delete(Object object);

	public abstract CrCredential findByName(String name);

}

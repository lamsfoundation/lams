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

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


package org.lamsfoundation.lams.contentrepository.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.contentrepository.CrCredential;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.IWorkspace;
import org.lamsfoundation.lams.contentrepository.dao.ICredentialDAO;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryRuntimeException;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.springframework.stereotype.Repository;

/**
 *
 * Implements the credentials lookup using Hibernate.
 *
 * @author Fiona Malikoff
 *
 */
@Repository
public class CredentialDAO extends LAMSBaseDAO implements ICredentialDAO {
    private Logger log = Logger.getLogger(CredentialDAO.class);

    private static final String GET_CREDENTIAL = "FROM " + CrCredential.class.getName() + " AS cr WHERE cr.name = ?";
    private static final String CHECK_CREDENTIAL = "SELECT COUNT(*) FROM " + CrCredential.class.getName()
	    + " AS cr WHERE cr.name = :name AND cr.password = :password";
    private static final String CHECK_CREDENTIAL_WITH_WORKSPACE = "SELECT COUNT(*) FROM " + CrCredential.class.getName()
	    + " AS cr INNER JOIN cr.crWorkspaceCredentials AS wcr WHERE cr.name = :name AND cr.password = :password "
	    + " AND wcr.crWorkspace.workspaceId = :workspaceId";

    /**
     * Checks whether a user can login to this workspace. The Credential must include the password.
     */
    @Override
    public boolean checkCredential(ICredentials credential, IWorkspace workspace) throws RepositoryRuntimeException {
	if (log.isDebugEnabled()) {
	    log.debug("Checking credential " + credential + " for workspace " + workspace);
	}
	if ((credential == null) || (workspace == null) || (workspace.getWorkspaceId() == null)) {
	    return false;
	}

	Session hibernateSession = getSessionFactory().getCurrentSession();
	Query<?> query = hibernateSession.createQuery(CredentialDAO.CHECK_CREDENTIAL_WITH_WORKSPACE);
	query.setParameter("name", credential.getName());
	query.setParameter("password", String.valueOf(credential.getPassword()));
	query.setParameter("workspaceId", workspace.getWorkspaceId());

	Long count = (Long) query.uniqueResult();
	if (count > 2) {
	    log.warn("More than one credential found for workspace " + workspace.getWorkspaceId() + " and credential "
		    + credential.getName());
	}

	return count > 0;
    }

    /**
     * Checks whether a user can login to the repository. The Credential must include the password.
     */
    @Override
    public boolean checkCredential(ICredentials credential) throws RepositoryRuntimeException {
	if (log.isDebugEnabled()) {
	    log.debug("Checking credential " + credential);
	}

	if (credential == null) {
	    return false;
	}

	Session hibernateSession = getSessionFactory().getCurrentSession();
	Query<?> query = hibernateSession.createQuery(CredentialDAO.CHECK_CREDENTIAL);
	query.setParameter("name", credential.getName());
	query.setParameter("password", String.valueOf(credential.getPassword()));

	Long count = (Long) query.uniqueResult();
	if (count > 2) {
	    log.warn("More than one credential found for name " + credential.getName());
	}
	return count > 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CrCredential findByName(String name) {
	if (log.isDebugEnabled()) {
	    log.debug("Getting credential for name " + name);
	}

	List<CrCredential> credentials = (List<CrCredential>) doFind(CredentialDAO.GET_CREDENTIAL, name);
	return credentials.size() == 0 ? null : credentials.get(0);
    }
}
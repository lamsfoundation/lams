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

package org.lamsfoundation.lams.contentrepository.dao.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.contentrepository.CrCredential;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.IWorkspace;
import org.lamsfoundation.lams.contentrepository.RepositoryRuntimeException;
import org.lamsfoundation.lams.contentrepository.dao.ICredentialDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;


/**
 *  
 * Implements the credentials lookup using Hibernate.
 * 
 * @author Fiona Malikoff
 *
 */
public class CredentialDAO extends BaseDAO implements ICredentialDAO {
  
	protected Logger log = Logger.getLogger(CredentialDAO.class);	

	/** 
	 * Checks whether a user can login to this workspace. The 
	 * Credential must include the password.
	 */
	public boolean checkCredential(ICredentials credential, IWorkspace workspace) 
						throws RepositoryRuntimeException {
		if ( log.isDebugEnabled() )
			log.debug("Checking credential "+credential+" for workspace "+workspace);

		if ( credential == null || workspace == null || workspace.getWorkspaceId() == null )
			return false;
		
		return checkCredentialInternal(credential, workspace);
	}

	/** 
	 * Checks whether a user can login to the repository. The 
	 * Credential must include the password.
	 */
	public boolean checkCredential(ICredentials credential) 
						throws RepositoryRuntimeException {
		if ( log.isDebugEnabled() )
			log.debug("Checking credential "+credential);

		if ( credential == null  )
			return false;
		
		return checkCredentialInternal(credential, null);
	}
	
	/** 
	 * Checks whether a user can login to the repository. The 
	 * Credential must include the password.
	 * 
	 * If workspace defined then checks workspace, otherwise just checks password
	 */
	public boolean checkCredentialInternal(ICredentials credential, IWorkspace workspace) 
						throws RepositoryRuntimeException {

		// given the input credential, is there a credential matching
		// in the database? why go to so much trouble in this code? I'm trying
		// to avoid converting the char[] to a string.
		// There will be better ways to do this, but this will do for starters
		// until I get more familiar with Spring.
		
		boolean credentialMatched = false;
		
		Session hibernateSession = getSession();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = hibernateSession.connection();
			
			StringBuffer buf = new StringBuffer(200);
			buf.append("select count(*) num from lams_cr_credential c");
			if ( workspace != null ) {
				buf.append(", lams_cr_workspace_credential wc ");
			}
			buf.append(" where c.name = \"");
			buf.append(credential.getName());
			buf.append("\" and c.password = \""); 
			buf.append(credential.getPassword());
			buf.append("\"");
			if ( workspace != null ) {
					buf.append(" and wc.credential_id = c.credential_id ");
					buf.append(" and wc.workspace_id = ");
					buf.append(workspace.getWorkspaceId());
			}
	
			ps = conn.prepareStatement(buf.toString());
			ps.execute();
			ResultSet rs = ps.getResultSet();
			if ( rs.next() )  {
				int val = rs.getInt("num");
				if ( val > 0 ) {
					credentialMatched = true;
					if ( val > 1 ) {
						log.warn("More than one credential found for workspace "
									+workspace.getWorkspaceId()
									+" credential name "
									+credential.getName());
					}
				}
			}
			
		} catch (HibernateException e) {
			log.error("Hibernate exception occured during login. ",e);
			throw new RepositoryRuntimeException("Unable to login due to internal error.", e);
		} catch (SQLException se) {
			log.error("SQL exception occured during login. ",se);
			throw new RepositoryRuntimeException("Unable to login due to internal error.", se);
		} finally {
			if ( ps != null ) {
				try {
					ps.close();
				} catch (SQLException se2) {
					log.error("SQL exception occured during login, while closing statement. ",se2);
					throw new RepositoryRuntimeException("Unable to login due to internal error.", se2);
				}
			}
		}

		return credentialMatched;
	}

	public CrCredential findByName(String name)  {

		log.debug("Getting credential for name "+name);
		
		String queryString = "from CrCredential as c where c.name = ?";
		List credentials = getHibernateTemplate().find(queryString,name);
		
		if(credentials.size() == 0){
			log.debug("No credentials found");
			return null;
		}else{
			return (CrCredential)credentials.get(0);
		}
	}
}

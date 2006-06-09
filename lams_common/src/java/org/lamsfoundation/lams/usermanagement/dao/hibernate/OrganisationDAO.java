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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;
import java.util.Iterator;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;

import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Organisation objects.
 * 
 * <p>
 * <a href="OrganisationDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class OrganisationDAO extends BaseDAO implements
		IOrganisationDAO {
	
	private static final String TABLENAME ="lams_organisation";
	private static final String FIND_BY_WORKSPACE = "from " + TABLENAME +
													" in class " + Organisation.class.getName() +
													" where workspace_id=?";

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#getAllOrganisations()
	 */
	public List getAllOrganisations() {
		return getHibernateTemplate().find("from Organisation");
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#getOrganisationById(java.lang.Integer)
	 */
	public Organisation getOrganisationById(Integer organisationId) {
		return (Organisation)getHibernateTemplate().get(Organisation.class, organisationId);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#getOrganisationsByName(java.lang.String)
	 */
	public List getOrganisationsByName(String name) {
		String queryString = "from Organisation o where o.name=?";
		return getHibernateTemplate().find(queryString,name);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#getChildOrganisations(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
    public List getChildOrganisations(Organisation parentOrg){
    	String queryString = "from Organisation o where o.parentOrganisation.organisationId=?";
    	return getHibernateTemplate().find(queryString,parentOrg.getOrganisationId());
    }
	
	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#deleteOrganisationById(java.lang.Integer)
	 */
	public void deleteOrganisationById(Integer organisationId) {
 		delete(getOrganisationById(organisationId));
	}
	public Organisation getOrganisationByWorkspaceID(Integer workspaceID){		
		List list = getHibernateTemplate().find(FIND_BY_WORKSPACE,workspaceID);
		if(list.size()!=0)
			return (Organisation) list.get(0);
		else
			return null;
	}

	public List getOrganisationsByType(Integer organisationTypeId) {
		String queryString = "from Organisation o where o.organisationType.organisationTypeId=?";
		return getHibernateTemplate().find(queryString,organisationTypeId);
	}

}

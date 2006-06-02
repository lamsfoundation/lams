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
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @version
 *
 * <p>
 * <a href="OrganisationStateDAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 11:30:40 on 2006-6-2
 */
public class OrganisationStateDAO extends HibernateDaoSupport implements
		IOrganisationStateDAO {

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#getAllOrganisationStates()
	 */
	public List<OrganisationState> getAllOrganisationStates() {
		return getHibernateTemplate().find("from OrganisationState");
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#getOrganisationStateById(java.lang.Integer)
	 */
	public OrganisationState getOrganisationStateById(
			Integer organisationStateId) {
		return (OrganisationState)getHibernateTemplate().get(OrganisationState.class, organisationStateId);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#getOrganisationStateByName(java.lang.String)
	 */
	public OrganisationState getOrganisationStateByName(String name) {
		String queryString = "from OrganisationState os where os.description=?";
		List organisationStates = getHibernateTemplate().find(queryString,name);
		if(organisationStates.size() == 0){
			return null;
		}else{
			return (OrganisationState)organisationStates.get(0);
		}	
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#saveOrganisationState(org.lamsfoundation.lams.usermanagement.OrganisationState)
	 */
	public void saveOrganisationState(OrganisationState organisationState) {
		getHibernateTemplate().save(organisationState);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#updateOrganisationState(org.lamsfoundation.lams.usermanagement.OrganisationState)
	 */
	public void updateOrganisationState(OrganisationState organisationState) {
		getHibernateTemplate().update(organisationState);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#saveOrUpdateOrganisationState(org.lamsfoundation.lams.usermanagement.OrganisationState)
	 */
	public void saveOrUpdateOrganisationState(
			OrganisationState organisationState) {
		getHibernateTemplate().saveOrUpdate(organisationState);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#deleteOrganisationState(org.lamsfoundation.lams.usermanagement.OrganisationState)
	 */
	public void deleteOrganisationState(OrganisationState organisationState) {
		getHibernateTemplate().delete(organisationState);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#deleteOrganisationStateById(java.lang.Integer)
	 */
	public void deleteOrganisationStateById(Integer organisationStateId) {
		getHibernateTemplate().delete(getOrganisationStateById(organisationStateId));
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationStateDAO#deleteOrganisationStateByName(java.lang.String)
	 */
	public void deleteOrganisationStateByName(String name) {
		getHibernateTemplate().delete(getOrganisationStateByName(name));
	}

}

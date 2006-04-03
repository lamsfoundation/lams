/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO;

/**
 * OrganisationType DAO Hibernate Implementation.
 *
 * <p>
 * <a href="OrganisationTypeDAO.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class OrganisationTypeDAO extends HibernateDaoSupport implements
		IOrganisationTypeDAO {

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#getAllOrganisationTypes()
	 */
	public List getAllOrganisationTypes() {
		return getHibernateTemplate().find("from OrganisationType");
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#getOrganisationTypeById(java.lang.Integer)
	 */
	public OrganisationType getOrganisationTypeById(Integer organisationTypeId) {
		return (OrganisationType)getHibernateTemplate().get(OrganisationType.class, organisationTypeId);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#getOrganisationTypeByName(java.lang.String)
	 */
	public OrganisationType getOrganisationTypeByName(String name) {
		String queryString = "from OrganisationType ot where ot.name=?";
		List organisationTypes = getHibernateTemplate().find(queryString,name);
		if(organisationTypes.size() == 0){
			return null;
		}else{
			return (OrganisationType)organisationTypes.get(0);
		}
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#saveOrganisationType(org.lamsfoundation.lams.usermanagement.OrganisationType)
	 */
	public void saveOrganisationType(OrganisationType organisationType) {
		getHibernateTemplate().save(organisationType);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#updateOrganisationType(org.lamsfoundation.lams.usermanagement.OrganisationType)
	 */
	public void updateOrganisationType(OrganisationType organisationType) {
		getHibernateTemplate().update(organisationType);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#saveOrUpdateOrganisationType(org.lamsfoundation.lams.usermanagement.OrganisationType)
	 */
	public void saveOrUpdateOrganisationType(OrganisationType organisationType) {
		getHibernateTemplate().saveOrUpdate(organisationType);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#deleteOrganisationType(org.lamsfoundation.lams.usermanagement.OrganisationType)
	 */
	public void deleteOrganisationType(OrganisationType organisationType) {
		getHibernateTemplate().delete(organisationType);
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#deleteOrganisationTypeById(java.lang.Integer)
	 */
	public void deleteOrganisationTypeById(Integer organisationTypeId) {
		getHibernateTemplate().delete(getOrganisationTypeById(organisationTypeId));
	}

	/** 
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO#deleteOrganisationTypeByName(java.lang.String)
	 */
	public void deleteOrganisationTypeByName(String name) {
		getHibernateTemplate().delete(getOrganisationTypeByName(name));
	}

}

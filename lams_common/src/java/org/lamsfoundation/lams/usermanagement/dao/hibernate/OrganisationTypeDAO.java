/*
 * Created on Nov 25, 2004
 *
 * Last modified on Nov 25, 2004
 */
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

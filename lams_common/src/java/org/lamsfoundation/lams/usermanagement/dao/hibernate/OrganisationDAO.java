/*
 * Created on Nov 21, 2004
 *
 * Last modified on Nov 21, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;
import java.util.Iterator;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

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
public class OrganisationDAO extends HibernateDaoSupport implements
		IOrganisationDAO {

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
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#saveOrganisation(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
	public void saveOrganisation(Organisation organisation) {
		getHibernateTemplate().save(organisation);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#updateOrganisation(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
	public void updateOrganisation(Organisation organisation) {
		getHibernateTemplate().update(organisation);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#saveOrUpdateOrganisation(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
	public void saveOrUpdateOrganisation(Organisation organisation) {
		getHibernateTemplate().saveOrUpdate(organisation);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#deleteOrganisation(org.lamsfoundation.lams.usermanagement.Organisation)
	 */
	public void deleteOrganisation(Organisation organisation) {
		Iterator iter = organisation.getChildOrganisations().iterator();
		Organisation parentOrganisation = organisation.getParentOrganisation();
		while(iter.hasNext()){
			Organisation childOrganisation = (Organisation)iter.next();
			childOrganisation.setParentOrganisation(parentOrganisation);
			iter.remove();
		}
		getHibernateTemplate().delete(organisation);
	}

	/**
	 * @see org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO#deleteOrganisationById(java.lang.Integer)
	 */
	public void deleteOrganisationById(Integer organisationId) {
 		deleteOrganisation(getOrganisationById(organisationId));
	}

}

package org.lamsfoundation.lams.tool.forum.core;

import org.lamsfoundation.lams.tool.forum.persistence.GenericEntity;
import org.lamsfoundation.lams.tool.forum.persistence.GenericEntityDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 6/06/2005
 * Time: 15:54:45
 * To change this template use File | Settings | File Templates.
 */
public class PersistenceDelegateImpl implements PersistenceDelegate {
    private static final String DAO = "Dao";
    private static final String GETBYID_ERROR = "unable to delegate getById operation, cause: ";
    private static final String DAO_TYPE_ERROR = "unable to find Dao object for this class: ";
    private static final String SAVE_ERROR = "unable to delegate save operation cause: ";
    private static final String DELETE_ERROR = "unable to delegate delete operation cause: ";
    private static final String FIND_ERROR = "unable to delegate find operation cause: ";
    private static final String NOSUCHENTITY_ERROR = "unable to find entity for subtype: ";

    public PersistenceDelegateImpl() {
        super();
    }

    public GenericEntity getById(Class subType, Long id) throws PersistenceException {
        if (!GenericEntity.class.isAssignableFrom(subType)) {
            throw new PersistenceException(GETBYID_ERROR + "not a valid subType, only GenericEntity objects allowed");
        }

        try {
            GenericEntity loaded = this.lookupDaoFor(subType).getById(id);
            if(loaded==null) {
                throw new PersistenceException(NOSUCHENTITY_ERROR + subType + " and  id: " + id);
            }
            return loaded;
        } catch (Exception e) {
            throw new PersistenceException(GETBYID_ERROR + e);
        }
    }

    public void saveOrUpdate(GenericEntity genericEntity) throws PersistenceException {
        try {
            this.lookupDaoFor(genericEntity.getClass()).saveOrUpdate(genericEntity);
        } catch (Exception e) {
            throw new PersistenceException(SAVE_ERROR + e.getMessage(), e);
        }
    }

    public void delete(GenericEntity genericEntity) throws PersistenceException {
        try {
            this.lookupDaoFor(genericEntity.getClass()).delete(genericEntity);
        } catch (Exception e) {
            throw new PersistenceException(DELETE_ERROR + e.getMessage(), e);
        }
    }

    public List findByNamedQuery(Class subType, String name) throws PersistenceException {
        try {
            return this.lookupDaoFor(subType).findByNamedQuery(name);
        } catch (Exception e) {
            throw new PersistenceException(FIND_ERROR + e.getMessage(), e);
        }
    }

    public List findByNamedQuery(Class subType, String name, Object value) throws PersistenceException {
        try {
            return this.lookupDaoFor(subType).findByNamedQuery(name, value);
        } catch (Exception e) {
            throw new PersistenceException(FIND_ERROR + e.getMessage(), e);
        }
    }

    public List findByNamedQuery(Class subType, String name, Object[] values) throws PersistenceException {
        try {
            return this.lookupDaoFor(subType).findByNamedQuery(name, values);
        } catch (Exception e) {
            throw new PersistenceException(FIND_ERROR + e.getMessage(), e);
        }
    }

    protected GenericEntityDao lookupDaoFor(Class genericEntityClass) throws PersistenceException, FactoryException {
        Class genericEntityDaoType = this.identifyDaoType(genericEntityClass);
        return (GenericEntityDao) GenericObjectFactoryImpl.getInstance().lookup(genericEntityDaoType);
    }

    protected Class identifyDaoType(Class subType) throws PersistenceException {
        Class genericEntityDaoType = null;
        String[] classNameHierarchy = this.getGenericEntityClassNameHierarchy(subType);

        for (int i = 0; i < classNameHierarchy.length; i++) {
            try {
                genericEntityDaoType = Class.forName(classNameHierarchy[i] + DAO);
                break;
            } catch (Exception e) {
                continue;
            }
        }
        if (genericEntityDaoType == null) {
            throw new PersistenceException(DAO_TYPE_ERROR + subType);
        }
        return genericEntityDaoType;
    }

    protected String[] getGenericEntityClassNameHierarchy(Class subType) {
        ArrayList classNames = new ArrayList();
        classNames.add(subType.getName());

        while (!Object.class.equals(subType.getSuperclass())) {
            subType = subType.getSuperclass();
            classNames.add(subType.getName());
        }
        return (String[]) classNames.toArray(new String[0]);
    }
}

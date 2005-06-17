package org.lamsfoundation.lams.tool.forum.core;

import org.lamsfoundation.lams.tool.forum.persistence.GenericEntity;

import java.util.List;

/**
 * PersistenceDelegate
 */
public interface PersistenceDelegate {

    /**
     * Deletegates the getById operation to the appropriate Dao object
     * @param subType GenericEntity subtype, will be attempted to match to a Dao.
     * @param id the primary key
     * @return genericEntity;
     * @throws PersistenceException
     */
    public GenericEntity getById(Class subType, Long id) throws PersistenceException;

    /**
     * Delegates the save operation to the appropriate Dao object
     * @param genericEntity the GenericEntity subtype, will be attempted to match to a Dao
     * @throws PersistenceException
     */
    public void saveOrUpdate(GenericEntity genericEntity) throws PersistenceException;

    /**
     * Delegates the delete operation to the appropriate Dao object
     * @param genericEntity
     * @throws PersistenceException
     */
    public void delete(GenericEntity genericEntity) throws PersistenceException;

    /**
     * Delegates the find operation to the appropriate Dao object.
     * or it's superclass.
     * @param subType
     * @param name
     * @throws PersistenceException
     * @return a List of GenericEntities
     */
    public List findByNamedQuery(Class subType, String name) throws PersistenceException;

    /**
     * Delegates the find operation to the appropriate Dao object.
     * or it's superclass.
     * @param subType the Dao subtype
     * @param name the query name
     * @param value the value passed into the query at the first position
     * @return a list of GenericEntities
     * @throws PersistenceException
     */
    public List findByNamedQuery(Class subType, String name, Object value) throws PersistenceException;

    /**
     * Delegates the find operation to the appropriate Dao object.
     * or it's superclass.
     * @param subType the Dao subtype
     * @param name the query name
     * @param values an object array of values passed into the query in the order of their array index position.
     * @return a list of GenericEntities
     * @throws PersistenceException
     */
    public List findByNamedQuery(Class subType, String name, Object[] values) throws PersistenceException;
}

package org.lamsfoundation.lams.tool.forum.persistence;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

/**
 * @author conradb
 *
 *
 */

public class GenericEntityDao extends HibernateDaoSupport {

    /**
     * Retrieve the GenericEntity with a particular id. The GenericEntity must be of
     * the same class as the DAO's getPersistentClass() method returns.
     *
     * @param id the id of the GenericEntity to look up
     * @return the corresponding GenericEntity, or null if the object does not exist with the
     *         appropriate class and id.
     */
    public GenericEntity getById(final Long id) {
        GenericEntity entity = (GenericEntity) this.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return session.get(getPersistentClass(), id);
            }
        });
        return entity;
    }

    /**
     * Save the GenericEntity if it doesn't exist or update an existing entity with the same id.
     *
     * @param genericEntity
     */
    public void saveOrUpdate(GenericEntity genericEntity) {
        genericEntity.updateModificationData();
        this.getHibernateTemplate().saveOrUpdate(genericEntity);
    }

    /**
     * Delete the GenericEntity with this id
     * @param genericEntity
     */
    public void delete(GenericEntity genericEntity) {
        this.getHibernateTemplate().delete(genericEntity);
    }

    /**
     * Find a list of GenericEntity objects, fetched by a given named query in the Entity object hierarchy.
     * @param name the query name
     * @return a list of GenericEntity objects.
     */
    public List findByNamedQuery(String name) {
        return this.getHibernateTemplate().findByNamedQuery(name);
    }

    /**
     * Find a list of GenericEntity objects, fetched by a given named query in the Entity object hierarchy.
     * @param name the query name
     * @param value the query parameter at position 0
     * @return a list of GenericEntity objects.
     */
    public List findByNamedQuery(String name, Object value) {
        return this.getHibernateTemplate().findByNamedQuery(name, value);
    }

    /**
     * Find a list of GenericEntity objects, fetched by a given named query in the Entity object hierarchy.
     * @param name the query name
     * @param values an array of query parameters passed into the query at the position of their array index.
     * @return a list of GenericEntity objects.
     */
    public List findByNamedQuery(String name, Object[] values) {
        return this.getHibernateTemplate().findByNamedQuery(name, values);
    }

    /**
     * Implement this to return the actual class mapped to hibernate.
     *
     * @return
     */
    public Class getPersistentClass() {
        return GenericEntity.class;
    }
}

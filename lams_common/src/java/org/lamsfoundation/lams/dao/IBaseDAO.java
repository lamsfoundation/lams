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

package org.lamsfoundation.lams.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

/**
 * @version
 *
 *          <p>
 *          <a href="IBaseDAO.java.html"><i>View Source</i></a>
 *          </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 *         Created at 22:50:25 on 16/06/2006
 */
public interface IBaseDAO {

    /**
     * Insert an object into the database. Should only be used if the object has not
     * been persisted previously.
     * 
     * @param object
     *            The object to be inserted
     */
    public void insert(Object object);

    /**
     * Update a previously inserted object into the database.
     * 
     * @param object
     *            The object to be updated
     */
    public void update(Object object);

    /**
     * Insert or update an object into the database. It is up to the persistence
     * engine to decide whether to insert or update.
     * 
     * @param object
     *            The object to be inserted/updated
     */
    public void insertOrUpdate(Object object);

    /**
     * @param objects
     * @return void
     */
    public void insertOrUpdateAll(Collection objects);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryString
     * @return void
     */
    public void update(String queryString);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryString
     * @param value
     * @return void
     */
    public void update(String queryString, Object value);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryString
     * @param values
     * @return void
     */
    public void update(String queryString, Object[] values);

    /**
     * @param clazz
     * @param propertyToChange
     * @param newValue
     * @param conditionProperty
     * @param conditionValue
     * @return void
     */
    public void update(Class clazz, String propertyToChange, Object newValue, String conditionProperty,
	    Object conditionValue);

    /**
     * @param clazz
     * @param propertyToChange
     * @param newValue
     * @param conditions
     *            in a map
     * @return void
     */
    public void update(Class clazz, String propertyToChange, Object newValue, Map<String, Object> conditions);

    /**
     * @param clazz
     * @param newValues
     *            in a map
     * @param conditionProperty
     * @param conditionValue
     * @return void
     */
    public void update(Class clazz, Map<String, Object> newValues, String conditionProperty, Object conditionValue);

    /**
     * @param clazz
     * @param newValues
     *            in a map
     * @param conditions
     *            in a map
     * @return void
     */
    public void update(Class clazz, Map<String, Object> newValues, Map<String, Object> conditions);

    /**
     * These 2 objects have to be instances of the Class
     * 
     * @param clazz
     * @param newValues
     *            in a object
     * @param conditions
     *            in a object
     * @return void
     */
    public void updateAnythingLike(Class clazz, Object newValues, Object conditions);

    /**
     * Force this session to flush. Must be called at the end of a unit of work, before commiting the transaction and
     * closing the session (depending on flush-mode, Transaction.commit() calls this method).
     * 
     * @throws HibernateException
     *             - Indicates problems flushing the session or talking to the database.
     */
    void flush();

    /**
     * Remove an object from the database.
     * 
     * @param object
     *            The object to be deleted
     */
    public void delete(Object object);

    /**
     * Be careful to use this method. It will clean up the whole table for the Class
     * 
     * @param clazz
     * @return void
     */
    public void deleteAll(Class clazz);

    /**
     * @param objects
     *            to delete
     * @return void
     */
    public void deleteAll(Collection objects);

    /**
     * @param clazz
     *            java Class
     * @param id
     *            identifier
     * @return void
     */
    public void deleteById(Class clazz, Serializable id);

    /**
     * @param clazz
     * @param name
     * @param value
     * @return void
     */
    public void deleteByProperty(Class clazz, String name, Object value);

    /**
     * @param properties
     *            a map of property names and values
     * @return void
     */
    public void deleteByProperties(Class clazz, Map<String, Object> properties);

    /**
     * Delete any object which has the same non-null property values as the object
     * 
     * @param object
     * @return void
     */
    public void deleteAnythingLike(Object object);

    /**
     * Find an object. If the object is not found
     * then it will return null
     * 
     * @param clazz
     * @param id
     */
    public Object find(Class clazz, Serializable id);

    /**
     * @param clazz
     * @return all of rows in the table for the Class as objects
     */
    public List findAll(Class clazz);

    /**
     * @param clazz
     * @param name
     * @param value
     * @return a list of objects which have the same propery value
     */
    public List findByProperty(Class clazz, String name, Object value);

    /**
     * @param properties
     *            a map of property names and values
     * @return a list of objects which have the same property values
     */
    public List findByProperties(Class clazz, Map<String, Object> properties);

    /**
     * Find any object which has the same non-null property values as the object
     * 
     * @param object
     * @return a list of objects which has the same non-null property values as the object
     */
    public List findAnythingLike(Object object);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryString
     * @return void
     */
    public List find(String queryString);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryString
     * @param value
     * @return void
     */
    public List find(String queryString, Object value);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryString
     * @param values
     * @return void
     */
    public List find(String queryString, Object[] values);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryName
     * @return void
     */
    public List findByNamedQuery(String queryName);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryName
     * @param value
     * @return void
     */
    public List findByNamedQuery(String queryName, Object value);

    /**
     * Just a wrapper of the method in HibernateTemplate
     * shouldn't be used unless a complicated query needed
     * 
     * @param queryName
     * @param values
     * @return void
     */
    public List findByNamedQuery(String queryName, Object[] values);

    /**
     * @param clazz
     * @param name
     *            of the property
     * @param pattern
     *            to match
     * @return a list of objects
     */
    public List searchByStringProperty(Class clazz, String name, String pattern);

    /**
     * @param clazz
     * @param name
     *            of the property
     * @param pattern
     *            to match
     * @return a list of objects
     */
    public List searchByStringProperties(Class clazz, Map<String, String> properties);

    /**
     * @param clazz
     * @param name
     *            of the property
     * @param min
     * @param minIncluded
     * @param max
     * @param maxIncluded
     * @return a list of objects
     */
    public List searchByNumberSpan(Class clazz, String name, Integer min, Boolean minIncluded, Integer max,
	    Boolean maxIncluded);

    /**
     * Force initialization of a Hibernate proxy or persistent collection
     * 
     * @param proxy
     *            of persistent object or a collection
     */
    public void initialize(Object proxy);

    /**
     * Count all rows in a table for a hibernate-mapped class
     * 
     * @param clazz
     * @return
     */
    public long countAll(Class clazz);

    /**
     * Create a query based on the properties, and count the result
     * 
     * @param properties
     *            a map of property names and values
     * @return a list of objects which have the same property values
     */
    public long countByProperties(Class clazz, Map<String, Object> properties);
}

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
package org.lamsfoundation.lams.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

/**
 * @version
 * 
 * Wrapper of HibernteTemplate() with some more OO methods
 * 
 * <p>
 * <a href="BaseDAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 23:13:41 on 16/06/2006
 */
public class BaseDAO extends HibernateDaoSupport implements IBaseDAO {
	
	private static class Qv {
		
		String queryString;
		Object[] values;
		
		Qv(String queryString, Object[] values) {
			super();
			
			this.queryString = queryString;
			this.values = values;
		}
	}

	private static final String SELECT = "from ";
	private static final String UPDATE = "update ";
	private static final String DELETE = "delete ";
	private static final String WHERE = " where ";
	private static final String AND = " and ";
	private static final String SPACE = " ";
	private static final String SPOT = ".";
	private static final String EQUAL_TO_WHAT = "=?";
	private static final String LIKE_WHAT = " like ?";
	
	private static Logger log = Logger.getLogger(BaseDAO.class);
	

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#insert(java.lang.Object)
	 */
	public void insert(Object object) {
		getHibernateTemplate().save(object);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#update(java.lang.Object)
	 */
	public void update(Object object) {
		getHibernateTemplate().update(object);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#insertOrUpdate(java.lang.Object)
	 */
	public void insertOrUpdate(Object object) {
		getHibernateTemplate().saveOrUpdate(object);
	}
    
        /* (non-Javadoc)
         * @see org.lamsfoundation.lams.dao.IBaseDAO#insertOrUpdateAll(java.util.Collection)
         */
        public void insertOrUpdateAll(Collection objects) {
        	if (objects != null) {
        	    for (Object object : objects) {
        		getHibernateTemplate().saveOrUpdate(object);
        	    }
        	}
        }

	public void update(String queryString) {
		getHibernateTemplate().bulkUpdate(queryString);
	}

	public void update(String queryString, Object value) {
		getHibernateTemplate().bulkUpdate(queryString, value);
	}

	public void update(String queryString, Object[] values) {
		getHibernateTemplate().bulkUpdate(queryString, values);
	}

	public void update(Class clazz, String propertyToChange, Object newValue, String conditionProperty, Object conditionValue) {
		//TODO implement me
	}

	public void update(Class clazz, String propertyToChange, Object newValue, Map<String, Object> conditions) {
		//TODO implement me
	}

	public void update(Class clazz, Map<String, Object> newValues, String conditionProperty, Object conditionValue) {
		//TODO implement me
	}

	public void update(Class clazz, Map<String, Object> newValues, Map<String, Object> conditions) {
		//TODO implement me
	}

	public void updateAnythingLike(Class clazz, Object newValues, Object conditions) {
		//TODO implement me
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#delete(java.lang.Object)
	 */
	public void delete(Object object) {
		getHibernateTemplate().delete(object);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteAll(java.lang.Class)
	 */
	public void deleteAll(Class clazz) {
		String queryString = buildQueryString(clazz,DELETE);
		getHibernateTemplate().bulkUpdate(queryString);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteAll(java.util.Collection)
	 */
	public void deleteAll(Collection objects) {
		getHibernateTemplate().deleteAll(objects);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteById(java.lang.Class, java.io.Serializable)
	 */
	public void deleteById(Class clazz, Serializable id) {
		delete(find(clazz,id));
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteByProperty(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public void deleteByProperty(Class clazz, String name, Object value) {
		String queryString = buildQueryString(clazz,name,DELETE);
		getHibernateTemplate().bulkUpdate(queryString,value);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteByProperties(java.lang.Class, java.util.Map)
	 */
	public void deleteByProperties(Class clazz, Map<String,Object> properties) {
		Qv qv = buildQueryString(clazz,properties,DELETE,EQUAL_TO_WHAT);
		getHibernateTemplate().bulkUpdate(qv.queryString,qv.values);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteAnythingLike(java.lang.Object)
	 */
	public void deleteAnythingLike(Object object) {
		try{
		Qv qv = buildQueryString(object,DELETE);
		getHibernateTemplate().bulkUpdate(qv.queryString,qv.values);
		}catch(Exception e){
			log.debug(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#find(java.lang.Class, java.io.Serializable)
	 */
	public Object find(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz,id);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#findAll(java.lang.Class)
	 */
	public List findAll(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#findByProperty(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	public List findByProperty(Class clazz, String name, Object value) {
		String queryString = buildQueryString(clazz,name,SELECT);
		return getHibernateTemplate().find(queryString,value);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#findByProperties(java.lang.Class, java.util.Map)
	 */
	public List findByProperties(Class clazz, Map<String,Object> properties) {
		Qv qv = buildQueryString(clazz,properties,SELECT,EQUAL_TO_WHAT);
		return getHibernateTemplate().find(qv.queryString,qv.values);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#findAnythingLike(java.lang.Object)
	 */
	public List findAnythingLike(Object object) {
		return getHibernateTemplate().findByExample(object);
	}
	
	private String buildQueryString(Class clazz,String operation){
		StringBuffer queryString = new StringBuffer(operation).append(clazz.getSimpleName());
		//log.debug(queryString);
		return queryString.toString();
	}
	
	private String buildQueryString(Class clazz,String name, String operation){
		String clazzName = clazz.getSimpleName();
		String objName = createObjectName(clazzName);
		StringBuffer queryString = new StringBuffer(operation).append(clazzName).append(SPACE)
										.append(objName).append(WHERE).append(objName).append(SPOT)
										.append(name).append(EQUAL_TO_WHAT);
		//log.debug(queryString);
		return queryString.toString();
	}
	
	public List find(String queryString) {
		return getHibernateTemplate().find(queryString);
	}

	public List find(String queryString, Object value) {
		return getHibernateTemplate().find(queryString,value);
	}

	public List find(String queryString, Object[] values) {
		return getHibernateTemplate().find(queryString, values);
	}

	public List findByNamedQuery(String queryName) {
		return getHibernateTemplate().findByNamedQuery(queryName);
	}

	public List findByNamedQuery(String queryName, Object value) {
		return getHibernateTemplate().findByNamedQuery(queryName, value);
	}

	public List findByNamedQuery(String queryName, Object[] values) {
		return getHibernateTemplate().findByNamedQuery(queryName, values);
	}

	public List searchByStringProperty(Class clazz, String name, String pattern) {
		//TODO implement me
		return null;
	}

	public List searchByStringProperties(Class clazz, Map<String, String> properties) {
		Map<String,Object> p = new HashMap<String,Object>();
		for(Map.Entry<String,String> entry : properties.entrySet()){
			p.put(entry.getKey(), (Object)entry.getValue());
		}
		Qv qv = buildQueryString(clazz,p,SELECT,LIKE_WHAT);
		return getHibernateTemplate().find(qv.queryString,qv.values);
	}

	public List searchByNumberSpan(Class clazz, String name, Integer min, Boolean minIncluded, Integer max, Boolean maxIncluded) {
		//TODO implement me
		return null;
	}
	
	private Qv buildQueryString(Class clazz, Map<String,Object> properties, String operation, String condition){
		String clazzName = clazz.getSimpleName();
		String objName = createObjectName(clazzName);
		StringBuffer queryString = new StringBuffer(operation).append(clazzName).append(SPACE).append(objName).append(WHERE);
		Object[] values = new Object[properties.size()];
		int i=0;
		for(Map.Entry<String,Object> entry : properties.entrySet()){
			queryString.append(objName).append(SPOT).append(entry.getKey()).append(condition);
			if(i!=properties.size()-1){
				queryString.append(AND);
			}
			values[i] = entry.getValue();
			i++;
		}
		//log.debug(queryString);
		return new Qv(queryString.toString(),values);
	}
	
	private Qv buildQueryString(Object obj, String operation) throws Exception{
		String clazzName = obj.getClass().getSimpleName();
		String objName = createObjectName(clazzName);
		StringBuffer queryString = new StringBuffer(operation).append(clazzName).append(SPACE).append(objName).append(WHERE);
		Field[] fields = obj.getClass().getDeclaredFields();
		List<Object> values = new ArrayList<Object>();
		for(int i=0; i<fields.length; i++){
			String name = fields[i].getName();
			Method readMethod = getReadMethod(fields[i],name, obj.getClass());
			Object value = readMethod.invoke(obj);
			if(value!=null){
				queryString.append(objName).append(SPOT).append(name).append(EQUAL_TO_WHAT);
				if(i != fields.length-1){
					queryString.append(AND);
				}
				values.add(value);
			}
		}
		//log.debug(queryString);
		return new Qv(queryString.toString(),values.toArray());
	}
	
	private Method getReadMethod(Field field, String fieldName, Class clazz) throws Exception{
		String convertedName = fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
		if(field.getType().getSimpleName().equals("Boolean")){
			return clazz.getDeclaredMethod("is"+convertedName);
		}else{
			return clazz.getDeclaredMethod("get"+convertedName);
		}
		
	}
	
	private String createObjectName(String clazzName){
		return clazzName.substring(0,1).toLowerCase();
	}

	public void initialize(Object proxy) {
		getHibernateTemplate().initialize(proxy);
	}
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#countAll(java.lang.Class)
	 */
	public long countAll(Class clazz) {
		String query = "select count(*) from " + clazz.getSimpleName();
		
		List list = getHibernateTemplate().find(query);
		
		if (list != null && list.size() > 0) {
			return (Long)list.get(0);
		} else {
			return 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.dao.IBaseDAO#countByProperties(java.lang.Class, java.util.Map)
	 */
	public long countByProperties(Class clazz, Map<String,Object> properties) {
		Qv qv = buildQueryString(clazz,properties,SELECT,EQUAL_TO_WHAT);
		String query = "select count(*) " + qv.queryString;
		
		List list = getHibernateTemplate().find(query,qv.values);
		
		if (list != null && list.size() > 0) {
			return (Long)list.get(0);
		} else {
			return 0;
		}
	}

}

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
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.springframework.stereotype.Repository;

@Repository
public class LAMSBaseDAO implements IBaseDAO {

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
    private static final String DELETE = "delete ";
    private static final String WHERE = " where ";
    private static final String AND = " and ";
    private static final String SPACE = " ";
    private static final String SPOT = ".";
    private static final String EQUAL_TO_WHAT = "=?";
    private static final String LIKE_WHAT = " like ?";

    private static Logger log = Logger.getLogger(LAMSBaseDAO.class);

    private SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#insert(java.lang.Object)
     */
    @Override
    public void insert(Object object) {
	getSession().save(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#update(java.lang.Object)
     */
    @Override
    public void update(Object object) {
	getSession().update(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lamsfoundation.lams.dao.IBaseDAO#insertOrUpdate(java.lang.Object)
     */
    @Override
    public void insertOrUpdate(Object object) {
	getSession().saveOrUpdate(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lamsfoundation.lams.dao.IBaseDAO#insertOrUpdateAll(java.util.Collection
     * )
     */
    @Override
    public void insertOrUpdateAll(Collection objects) {
	if (objects != null) {
	    for (Object object : objects) {
		getSession().saveOrUpdate(object);
	    }
	}
    }

    @Override
    public void update(String queryString) {
	doBulkUpdate(queryString);
    }

    @Override
    public void update(String queryString, Object value) {
	doBulkUpdate(queryString, value);
    }

    @Override
    public void update(String queryString, Object[] values) {
	doBulkUpdate(queryString, values);
    }

    @Override
    public void update(Class clazz, String propertyToChange, Object newValue, String conditionProperty,
	    Object conditionValue) {
	// TODO implement me
    }

    @Override
    public void update(Class clazz, String propertyToChange, Object newValue, Map<String, Object> conditions) {
	// TODO implement me
    }

    @Override
    public void update(Class clazz, Map<String, Object> newValues, String conditionProperty, Object conditionValue) {
	// TODO implement me
    }

    @Override
    public void update(Class clazz, Map<String, Object> newValues, Map<String, Object> conditions) {
	// TODO implement me
    }

    @Override
    public void updateAnythingLike(Class clazz, Object newValues, Object conditions) {
	// TODO implement me
    }

    @Override
    public void flush() {
	getSession().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#delete(java.lang.Object)
     */
    @Override
    public void delete(Object object) {
	getSession().delete(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteAll(java.lang.Class)
     */
    @Override
    public void deleteAll(Class clazz) {
	String queryString = buildQueryString(clazz, DELETE);
	doBulkUpdate(queryString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteAll(java.util.Collection)
     */
    @Override
    public void deleteAll(Collection objects) {
	doDeleteAll(objects);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#deleteById(java.lang.Class,
     * java.io.Serializable)
     */
    @Override
    public void deleteById(Class clazz, Serializable id) {
	delete(find(clazz, id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lamsfoundation.lams.dao.IBaseDAO#deleteByProperty(java.lang.Class,
     * java.lang.String, java.lang.Object)
     */
    @Override
    public void deleteByProperty(Class clazz, String name, Object value) {
	String queryString = buildQueryString(clazz, name, DELETE);
	doBulkUpdate(queryString, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lamsfoundation.lams.dao.IBaseDAO#deleteByProperties(java.lang.Class,
     * java.util.Map)
     */
    @Override
    public void deleteByProperties(Class clazz, Map<String, Object> properties) {
	Qv qv = buildQueryString(clazz, properties, DELETE, EQUAL_TO_WHAT);
	doBulkUpdate(qv.queryString, qv.values);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lamsfoundation.lams.dao.IBaseDAO#deleteAnythingLike(java.lang.Object)
     */
    @Override
    public void deleteAnythingLike(Object object) {
	try {
	    Qv qv = buildQueryString(object, DELETE);
	    doBulkUpdate(qv.queryString, qv.values);
	} catch (Exception e) {
	    log.debug(e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#find(java.lang.Class,
     * java.io.Serializable)
     */
    @Override
    public Object find(Class clazz, Serializable id) {
	return getSession().get(clazz, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#findAll(java.lang.Class)
     */
    @Override
    public List findAll(Class clazz) {
	return loadAll(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#findByProperty(java.lang.Class,
     * java.lang.String, java.lang.Object)
     */
    @Override
    public List findByProperty(Class clazz, String name, Object value) {
	String queryString = buildQueryString(clazz, name, SELECT);
	return doFind(queryString, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lamsfoundation.lams.dao.IBaseDAO#findByProperties(java.lang.Class,
     * java.util.Map)
     */
    @Override
    public List findByProperties(Class clazz, Map<String, Object> properties) {
	Qv qv = buildQueryString(clazz, properties, SELECT, EQUAL_TO_WHAT);
	return doFind(qv.queryString, qv.values);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lamsfoundation.lams.dao.IBaseDAO#findAnythingLike(java.lang.Object)
     */
    @Override
    public List findAnythingLike(Object object) {
	return doFindByExample(null, object, -1, -1);
    }

    private String buildQueryString(Class clazz, String operation) {
	StringBuilder queryString = new StringBuilder(operation).append(clazz.getSimpleName());
	// log.debug(queryString);
	return queryString.toString();
    }

    private String buildQueryString(Class clazz, String name, String operation) {
	String clazzName = clazz.getSimpleName();
	String objName = createObjectName(clazzName);
	StringBuilder queryString = new StringBuilder(operation).append(clazzName).append(SPACE).append(objName)
		.append(WHERE).append(objName).append(SPOT).append(name).append(EQUAL_TO_WHAT);
	// log.debug(queryString);
	return queryString.toString();
    }

    @Override
    public List find(String queryString) {
	return doFind(queryString);
    }

    @Override
    public List find(String queryString, Object value) {
	return doFind(queryString, value);
    }

    @Override
    public List find(String queryString, Object[] values) {
	return doFind(queryString, values);
    }

    @Override
    public List findByNamedQuery(String queryName) {
	return doFindByNamedQuery(queryName);
    }

    @Override
    public List findByNamedQuery(String queryName, Object value) {
	return doFindByNamedQuery(queryName, value);
    }

    @Override
    public List findByNamedQuery(String queryName, Object[] values) {
	return doFindByNamedQuery(queryName, values);
    }

    @Override
    public List searchByStringProperty(Class clazz, String name, String pattern) {
	// TODO implement me
	return null;
    }

    @Override
    public List searchByStringProperties(Class clazz, Map<String, String> properties) {
	Map<String, Object> p = new HashMap<String, Object>();
	for (Map.Entry<String, String> entry : properties.entrySet()) {
	    p.put(entry.getKey(), entry.getValue());
	}
	Qv qv = buildQueryString(clazz, p, SELECT, LIKE_WHAT);
	return doFind(qv.queryString, qv.values);
    }

    @Override
    public List searchByNumberSpan(Class clazz, String name, Integer min, Boolean minIncluded, Integer max,
	    Boolean maxIncluded) {
	// TODO implement me
	return null;
    }

    private Qv buildQueryString(Class clazz, Map<String, Object> properties, String operation, String condition) {
	String clazzName = clazz.getSimpleName();
	String objName = createObjectName(clazzName);
	StringBuilder queryString = new StringBuilder(operation).append(clazzName).append(SPACE).append(objName)
		.append(WHERE);
	Object[] values = new Object[properties.size()];
	int i = 0;
	for (Map.Entry<String, Object> entry : properties.entrySet()) {
	    queryString.append(objName).append(SPOT).append(entry.getKey()).append(condition);
	    if (i != properties.size() - 1) {
		queryString.append(AND);
	    }
	    values[i] = entry.getValue();
	    i++;
	}
	// log.debug(queryString);
	return new Qv(queryString.toString(), values);
    }

    private Qv buildQueryString(Object obj, String operation) throws Exception {
	String clazzName = obj.getClass().getSimpleName();
	String objName = createObjectName(clazzName);
	StringBuilder queryString = new StringBuilder(operation).append(clazzName).append(SPACE).append(objName)
		.append(WHERE);
	Field[] fields = obj.getClass().getDeclaredFields();
	List<Object> values = new ArrayList<Object>();
	for (int i = 0; i < fields.length; i++) {
	    String name = fields[i].getName();
	    Method readMethod = getReadMethod(fields[i], name, obj.getClass());
	    Object value = readMethod.invoke(obj);
	    if (value != null) {
		queryString.append(objName).append(SPOT).append(name).append(EQUAL_TO_WHAT);
		if (i != fields.length - 1) {
		    queryString.append(AND);
		}
		values.add(value);
	    }
	}
	// log.debug(queryString);
	return new Qv(queryString.toString(), values.toArray());
    }

    private Method getReadMethod(Field field, String fieldName, Class clazz) throws Exception {
	String convertedName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	if (field.getType().getSimpleName().equals("Boolean")) {
	    return clazz.getDeclaredMethod("is" + convertedName);
	} else {
	    return clazz.getDeclaredMethod("get" + convertedName);
	}

    }

    private String createObjectName(String clazzName) {
	return clazzName.substring(0, 1).toLowerCase();
    }

    @Override
    public void initialize(Object proxy) {
	Hibernate.initialize(proxy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.dao.IBaseDAO#countAll(java.lang.Class)
     */
    @Override
    public long countAll(Class clazz) {
	String query = "select count(*) from " + clazz.getSimpleName();

	List list = doFind(query);

	if (list != null && list.size() > 0) {
	    return (Long) list.get(0);
	} else {
	    return 0;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lamsfoundation.lams.dao.IBaseDAO#countByProperties(java.lang.Class,
     * java.util.Map)
     */
    @Override
    public long countByProperties(Class clazz, Map<String, Object> properties) {
	Qv qv = buildQueryString(clazz, properties, SELECT, EQUAL_TO_WHAT);
	String query = "select count(*) " + qv.queryString;

	List list = doFind(query, qv.values);

	if (list != null && list.size() > 0) {
	    return (Long) list.get(0);
	} else {
	    return 0;
	}
    }

    protected Session getSession() {
	return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
	return sessionFactory;
    }

    public List<?> doFind(final String queryString, final Object... values) {
	Query queryObject = getSession().createQuery(queryString);
	if (values != null) {
	    for (int i = 0; i < values.length; i++) {
		queryObject.setParameter(i, values[i]);
	    }
	}
	return queryObject.list();
    }

    public int doBulkUpdate(final String queryString, final Object... values) {
	Query queryObject = getSession().createQuery(queryString);
	if (values != null) {
	    for (int i = 0; i < values.length; i++) {
		queryObject.setParameter(i, values[i]);
	    }
	}
	return queryObject.executeUpdate();
    }

    public List<?> doFindByNamedQuery(final String queryName, final Object... values) {
	Query queryObject = getSession().getNamedQuery(queryName);
	if (values != null) {
	    for (int i = 0; i < values.length; i++) {
		queryObject.setParameter(i, values[i]);
	    }
	}
	return queryObject.list();
    }

    public <T> List<T> doFindByExample(final String entityName, final T exampleEntity, final int firstResult,
	    final int maxResults) {
	if (exampleEntity == null) {
	    throw new IllegalArgumentException("Example entity must not be null");
	}

	Criteria executableCriteria = (entityName != null ? getSession().createCriteria(entityName)
		: getSession().createCriteria(exampleEntity.getClass()));
	executableCriteria.add(Example.create(exampleEntity));
	if (firstResult >= 0) {
	    executableCriteria.setFirstResult(firstResult);
	}
	if (maxResults > 0) {
	    executableCriteria.setMaxResults(maxResults);
	}
	return executableCriteria.list();
    }

    public <T> List<T> findByExample(final String entityName, final T exampleEntity, final int firstResult,
	    final int maxResults) {

	if (exampleEntity == null) {
	    throw new IllegalArgumentException("Example entity must not be null");
	}

	Criteria executableCriteria = (entityName != null ? getSession().createCriteria(entityName)
		: getSession().createCriteria(exampleEntity.getClass()));
	executableCriteria.add(Example.create(exampleEntity));
	if (firstResult >= 0) {
	    executableCriteria.setFirstResult(firstResult);
	}
	if (maxResults > 0) {
	    executableCriteria.setMaxResults(maxResults);
	}
	return executableCriteria.list();
    }

    public <T> List<T> loadAll(final Class<T> entityClass) {
	Criteria criteria = getSession().createCriteria(entityClass);
	criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	return criteria.list();
    }

    public void doDeleteAll(final Collection<?> entities) {
	for (Object entity : entities) {
	    getSession().delete(entity);
	}
    }

    public List<?> doFindByNamedParam(final String queryString, final String[] paramNames, final Object[] values) {

	if (paramNames.length != values.length) {
	    throw new IllegalArgumentException("Length of paramNames array must match length of values array");
	}
	Query queryObject = getSession().createQuery(queryString);
	if (values != null) {
	    for (int i = 0; i < values.length; i++) {
		applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
	    }
	}
	return queryObject.list();
    }

    private void applyNamedParameterToQuery(Query queryObject, String paramName, Object value)
	    throws HibernateException {

	if (value instanceof Collection) {
	    queryObject.setParameterList(paramName, (Collection<?>) value);
	} else if (value instanceof Object[]) {
	    queryObject.setParameterList(paramName, (Object[]) value);
	} else {
	    queryObject.setParameter(paramName, value);
	}
    }

    public List<?> doFindByNamedQueryAndNamedParam(String queryName, String paramName, Object value) {

	return doFindByNamedQueryAndNamedParam(queryName, new String[] { paramName }, new Object[] { value });
    }

    public List<?> doFindByNamedQueryAndNamedParam(final String queryName, final String[] paramNames,
	    final Object[] values) {

	if (values != null && (paramNames == null || paramNames.length != values.length)) {
	    throw new IllegalArgumentException("Length of paramNames array must match length of values array");
	}
	Query queryObject = getSession().getNamedQuery(queryName);
	if (values != null) {
	    for (int i = 0; i < values.length; i++) {
		applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
	    }
	}

	return queryObject.list();
    }

    /**
     * @see com.edgenius.paradise.dao.DAO#saveObject(java.lang.Object)
     */
    public void saveObject(Object o) {
	getSession().saveOrUpdate(o);
    }

    /**
     * @see com.edgenius.paradise.dao.DAO#getObject(java.lang.Class, java.io.Serializable)
     */
    public Object getObject(Class clazz, Serializable id) {
	Object o = getSession().get(clazz, id);
	return o;
    }

    /**
     * @see com.edgenius.paradise.dao.DAO#getObjects(java.lang.Class)
     */
    public List getObjects(Class clazz) {
	return loadAll(clazz);
    }

    /**
     * @see com.edgenius.paradise.dao.DAO#removeObject(java.lang.Class, java.io.Serializable)
     */
    public void removeObject(Class clazz, Serializable id) {
	getSession().delete(getObject(clazz, id));
    }

    public void releaseFromCache(Object o) {
	getSessionFactory().getCurrentSession().evict(o);
    }
}

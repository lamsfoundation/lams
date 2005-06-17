package net.sf.hibernate.impl;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.transaction.TransactionManager;
import javax.xml.transform.Templates;

import net.sf.cglib.core.KeyFactory;
import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.Databinder;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Interceptor;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.QueryException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.exception.SQLExceptionConverter;
import net.sf.hibernate.cache.QueryCache;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.cfg.Settings;
import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.connection.ConnectionProvider;
import net.sf.hibernate.dialect.Dialect;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.hql.FilterTranslator;
import net.sf.hibernate.hql.QueryTranslator;
import net.sf.hibernate.id.IdentifierGenerator;
import net.sf.hibernate.id.UUIDHexGenerator;
import net.sf.hibernate.mapping.Collection;
import net.sf.hibernate.mapping.NamedSQLQuery;
import net.sf.hibernate.mapping.PersistentClass;
import net.sf.hibernate.metadata.ClassMetadata;
import net.sf.hibernate.metadata.CollectionMetadata;
import net.sf.hibernate.persister.ClassPersister;
import net.sf.hibernate.persister.PersisterFactory;
import net.sf.hibernate.persister.Queryable;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;
import net.sf.hibernate.tool.hbm2ddl.SchemaUpdate;
import net.sf.hibernate.transaction.TransactionFactory;
import net.sf.hibernate.type.Type;
import net.sf.hibernate.xml.XMLDatabinder;

import org.apache.commons.collections.ReferenceMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.hibernate.cache.UpdateTimestampsCache;


/**
 * BEWARE! Hacked version to implement equals() and hashCode()
 * FIX for transaction not rolling back
 *
 * Concrete implementation of the <tt>SessionFactory</tt> interface. Has the following
 * responsibilites
 * <ul>
 * <li>caches configuration settings (immutably)
 * <li>caches "compiled" mappings ie. <tt>ClassPersister</tt>s and
 *     <tt>CollectionPersister</tt>s (immutable)
 * <li>caches "compiled" queries (memory sensitive cache)
 * <li>manages <tt>PreparedStatement</tt>s
 * <li> delegates JDBC <tt>Connection</tt> management to the <tt>ConnectionProvider</tt>
 * <li>factory for instances of <tt>SessionImpl</tt>
 * </ul>
 * This class must appear immutable to clients, even if it does all kinds of caching
 * and pooling under the covers. It is crucial that the class is not only thread
 * safe, but also highly concurrent. Synchronization must be used extremely sparingly.
 *
 * @see net.sf.hibernate.connection.ConnectionProvider
 * @see Session
 * @see net.sf.hibernate.hql.QueryTranslator
 * @see net.sf.hibernate.persister.ClassPersister
 * @see net.sf.hibernate.collection.CollectionPersister
 * @author Gavin King
 */
public final class SessionFactoryImpl implements SessionFactory, SessionFactoryImplementor {

	private final String name;
	private final String uuid;

	private final transient Map classPersisters;
	private final transient Map classPersistersByName;
	private final transient Map classMetadata;
	private final transient Map collectionPersisters;
	private final transient Map collectionMetadata;
	private final transient Map namedQueries;
	private final transient Map namedSqlQueries;
	private final transient Map imports;
	private final transient Templates templates;
	private final transient Interceptor interceptor;
	private final transient Settings settings;
	private final transient Properties properties;
	private transient SchemaExport schemaExport;
	private final transient TransactionManager transactionManager;
	private final transient QueryCache queryCache;
	private final transient UpdateTimestampsCache updateTimestampsCache;
	private final transient Map queryCaches;

	private static final IdentifierGenerator UUID_GENERATOR = new UUIDHexGenerator();

	private static final Log log = LogFactory.getLog(SessionFactoryImpl.class);

	public SessionFactoryImpl(Configuration cfg, Settings settings) throws HibernateException {

		log.info("building session factory");

		this.properties = cfg.getProperties();
		this.interceptor = cfg.getInterceptor();
		this.settings = settings;

		if ( log.isDebugEnabled() ) log.debug(
			"instantiating session factory with properties: " + properties
		);

		//Persisters:

		classPersisters = new HashMap();
		classPersistersByName = new HashMap();
		Map classMeta = new HashMap();
		Iterator classes = cfg.getClassMappings();
		while ( classes.hasNext() ) {
			PersistentClass model = (PersistentClass) classes.next();
			ClassPersister cp = PersisterFactory.createClassPersister(model, this);
			classPersisters.put( model.getMappedClass(), cp );
			classPersistersByName.put( model.getName(), cp );
			classMeta.put( model.getMappedClass(), cp.getClassMetadata() );
		}
		classMetadata = Collections.unmodifiableMap(classMeta);

		collectionPersisters = new HashMap();
		Iterator collections = cfg.getCollectionMappings();
		while ( collections.hasNext() ) {
			Collection map = (Collection) collections.next();
			CollectionPersister persister = PersisterFactory.createCollectionPersister(cfg, map, this);
			collectionPersisters.put( map.getRole(), persister.getCollectionMetadata() );
		}
		collectionMetadata = Collections.unmodifiableMap(collectionPersisters);

		// after *all* persisters are registered
		Iterator iter = classPersisters.values().iterator();
		while ( iter.hasNext() ) {
			( (ClassPersister) iter.next() ).postInstantiate();
		}

		// For databinding:

		templates = XMLDatabinder.getOutputStyleSheetTemplates(properties);

		//JNDI + Serialization:

		name = settings.getSessionFactoryName();
		try {
			uuid = (String) UUID_GENERATOR.generate(null, null);
		}
		catch (Exception e) {
			throw new AssertionFailure("Could not generate UUID");
		}
		SessionFactoryObjectFactory.addInstance(uuid, name, this, properties);

		//Named Queries:
		// TODO: precompile and cache named queries

		namedQueries = new HashMap( cfg.getNamedQueries() );
		namedSqlQueries = new HashMap( cfg.getNamedSQLQueries().size() );
		Iterator namedIter = cfg.getNamedSQLQueries().entrySet().iterator();
		while ( namedIter.hasNext() ) {
			Map.Entry entry = (Entry) namedIter.next();
			NamedSQLQuery nsq = (NamedSQLQuery) entry.getValue();
			namedSqlQueries.put(
				entry.getKey(),
				new InternalNamedSQLQuery(
					nsq.getQueryString(),
					nsq.getReturnAliases(),
					nsq.getReturnClasses(),
					nsq.getSynchronizedTables()
				)
			);
		}

		imports = new HashMap( cfg.getImports() );

		log.debug("instantiated session factory");

		if ( settings.isAutoCreateSchema() ) new SchemaExport(cfg).create(false, true);
		if ( settings.isAutoUpdateSchema() ) new SchemaUpdate(cfg).execute(false, true);
		if ( settings.isAutoDropSchema() ) schemaExport = new SchemaExport(cfg);

		if ( settings.getTransactionManagerLookup()!=null ) {
			log.debug("obtaining JTA TransactionManager");
			transactionManager = settings.getTransactionManagerLookup().getTransactionManager(properties);
		}
		else {
			transactionManager = null;
		}

		if ( settings.isQueryCacheEnabled() ) {
			updateTimestampsCache = new UpdateTimestampsCache( settings.getCacheProvider(), properties );
			queryCache = settings
			        .getQueryCacheFactory()
			        .getQueryCache(null, settings.getCacheProvider(), updateTimestampsCache, properties);
			queryCaches = Collections.synchronizedMap( new HashMap() );
		}
		else {
			updateTimestampsCache = null;
			queryCache = null;
			queryCaches = null;
		}

	}

	// Emulates constant time LRU/MRU algorithms for cache
	// It is better to hold strong refernces on some (LRU/MRU) queries
	private static final int MAX_STRONG_REF_COUNT = 128; //TODO: configurable?
	private final transient Object[] strongRefs = new Object[MAX_STRONG_REF_COUNT]; //strong reference to MRU queries
	private transient int strongRefIndex = 0;
	private final transient Map softQueryCache = new ReferenceMap(ReferenceMap.SOFT, ReferenceMap.SOFT) ;
	// both keys and values may be soft since value keeps a hard ref to the key (and there is a hard ref to MRU values)

	//returns generated class instance
	private static final QueryCacheKeyFactory QUERY_KEY_FACTORY;
	private static final FilterCacheKeyFactory FILTER_KEY_FACTORY;
	static {
		QUERY_KEY_FACTORY = (QueryCacheKeyFactory) KeyFactory.create(QueryCacheKeyFactory.class);
		FILTER_KEY_FACTORY = (FilterCacheKeyFactory) KeyFactory.create(FilterCacheKeyFactory.class);
	}

	static interface QueryCacheKeyFactory {
		//Will not recalculate hashKey for constant queries
		Object newInstance(String query, boolean scalar);
	}

	static interface FilterCacheKeyFactory {
		//Will not recalculate hashKey for constant queries
		Object newInstance(String role, String query, boolean scalar);
	}

	//TODO: this stuff can be implemented in separate class to reuse soft MRU/LRU caching
	private synchronized Object get(Object key) {
		Object result = softQueryCache.get(key);
		if( result != null ) {
			strongRefs[ ++strongRefIndex % MAX_STRONG_REF_COUNT ] = result;
		}
		return result;
	}

	private void put(Object key, Object value) {
		softQueryCache.put(key, value);
		strongRefs[ ++strongRefIndex % MAX_STRONG_REF_COUNT ] = value;
	}

	private synchronized QueryTranslator[] createQueryTranslators(String[] concreteQueryStrings, Object cacheKey) {
		final int length = concreteQueryStrings.length;
		final QueryTranslator[] queries = new QueryTranslator[length];
		for ( int i=0; i<length; i++ ) queries[i] = new QueryTranslator( concreteQueryStrings[i] );
		put(cacheKey, queries);
		return queries;
	}

	private synchronized FilterTranslator createFilterTranslator(String filterString, Object cacheKey) {
		final FilterTranslator filter = new FilterTranslator(filterString);
		put(cacheKey, filter);
		return filter;
	}

	public QueryTranslator[] getQuery(String queryString, boolean shallow) throws QueryException, MappingException {

		Object cacheKey = QUERY_KEY_FACTORY.newInstance(queryString, shallow);

		// have to be careful to ensure that if the JVM does out-of-order execution
		// then another thread can't get an uncompiled QueryTranslator from the cache
		// we also have to be very careful to ensure that other threads can perform
		// compiled queries while another query is being compiled

		QueryTranslator[] queries = (QueryTranslator[]) get(cacheKey);
		if ( queries==null ) {
			// a query that names an interface or unmapped class in the from clause
			// is actually executed as multiple queries
			String[] concreteQueryStrings = QueryTranslator.concreteQueries(queryString, this);
			queries = createQueryTranslators(concreteQueryStrings, cacheKey);
		}
		for ( int i=0; i<queries.length; i++) queries[i].compile(this, settings.getQuerySubstitutions(), shallow);
		// see comment above. note that QueryTranslator.compile() is synchronized
		return queries;

	}

	public FilterTranslator getFilter(String filterString, String collectionRole, boolean scalar) throws QueryException, MappingException {

		Object cacheKey = FILTER_KEY_FACTORY.newInstance(collectionRole, filterString, scalar);

		FilterTranslator filter = (FilterTranslator) get(cacheKey);
		if ( filter==null ) filter = createFilterTranslator(filterString, cacheKey);
		filter.compile(collectionRole, this, settings.getQuerySubstitutions(), scalar);
		// see comment above. note that FilterTranslator.compile() is synchronized
		return filter;

	}

	private Session openSession(Connection connection, boolean autoClose, long timestamp, Interceptor interceptor) {
		return new SessionImpl( connection, this, autoClose, timestamp, interceptor );
	}

	public Session openSession(Connection connection, Interceptor interceptor) {
		return openSession( connection, false, Long.MIN_VALUE, interceptor );
	}

	public Session openSession(Interceptor interceptor) throws HibernateException {
		// note that this timestamp is not correct if the connection provider
		// returns an older JDBC connection that was associated with a
		// transaction that was already begun before openSession() was called
		// (don't know any possible solution to this!)
		long timestamp = settings.getCacheProvider().nextTimestamp();
		return openSession( null, true, timestamp, interceptor );
	}

	public Session openSession(Connection connection) {
		return openSession(connection, interceptor); //prevents this session from adding things to cache
	}

	public Session openSession() throws HibernateException {
		return openSession(interceptor);
	}

	public ClassPersister getPersister(String className) throws MappingException {
		if (className.startsWith("[")) throw new MappingException("No persister for array result, likely a broken query");
		ClassPersister result = (ClassPersister) classPersistersByName.get(className);
		if (result==null) throw new MappingException( "No persister for: " + className );
		return result;
	}
	public ClassPersister getPersister(Class theClass) throws MappingException {
		ClassPersister result = (ClassPersister) classPersisters.get(theClass);
		if (result==null){
			 throw new MappingException( "Unknown entity class: " + theClass.getName() );
		}
		return result;
	}
	public CollectionPersister getCollectionPersister(String role) throws MappingException {
		CollectionPersister result = (CollectionPersister) collectionPersisters.get(role);
		if (result==null) {
			throw new MappingException( "Unknown collection role: " + role );
		}
		return result;
	}

	public Databinder openDatabinder() throws HibernateException {
		if (templates==null) throw new HibernateException(
			"No output stylesheet configured. Use the property hibernate.output_stylesheet and ensure xalan.jar is in classpath"
		);
		try {
			return new XMLDatabinder( this, templates.newTransformer() );
		}
		catch (Exception e) {
			log.error("Could not open Databinder", e);
			throw new HibernateException("Could not open Databinder", e);
		}
	}

	public Dialect getDialect() {
		return settings.getDialect();
	}

	public TransactionFactory getTransactionFactory() {
		return settings.getTransactionFactory();
	}

	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	public SQLExceptionConverter getSQLExceptionConverter() {
		return settings.getSQLExceptionConverter();
	}

	// from javax.naming.Referenceable
	public Reference getReference() throws NamingException {
		log.debug("Returning a Reference to the SessionFactory");
		return new Reference(
			SessionFactoryImpl.class.getName(),
			new StringRefAddr("uuid", uuid),
			SessionFactoryObjectFactory.class.getName(),
			null
		);
	}

	private Object readResolve() throws ObjectStreamException {
		log.trace("Resolving serialized SessionFactory");
		// look for the instance by uuid
		Object result = SessionFactoryObjectFactory.getInstance(uuid);
		if (result==null) {
			// in case we were deserialized in a different JVM, look for an instance with the same name
			// (alternatively we could do an actual JNDI lookup here....)
			result = SessionFactoryObjectFactory.getNamedInstance(name);
			if (result==null) {
				throw new InvalidObjectException("Could not find a SessionFactory named: " + name);
			}
			else {
				log.debug("resolved SessionFactory by name");
			}
		}
		else {
			log.debug("resolved SessionFactory by uid");
		}
		return result;
	}

	public boolean isJdbcBatchUpdateEnabled() {
		return settings.getJdbcBatchSize()>0;
	}

	public int getJdbcBatchSize() {
		return settings.getJdbcBatchSize();
	}

	public boolean isScrollableResultSetsEnabled() {
		return settings.isScrollableResultSetsEnabled();
	}

	public boolean isGetGeneratedKeysEnabled() {
		return settings.isGetGeneratedKeysEnabled();
	}

	public boolean isOuterJoinedFetchEnabled() {
		return settings.isOuterJoinFetchEnabled();
	}

	public String getNamedQuery(String queryName) {
		String queryString = (String) namedQueries.get(queryName);
		return queryString;
	}

	public InternalNamedSQLQuery getNamedSQLQuery(String queryName) {
		return (InternalNamedSQLQuery) namedSqlQueries.get(queryName);
	}

	public Type getIdentifierType(Class ObjectClass) throws MappingException {
		return getPersister(ObjectClass).getIdentifierType();
	}
	public String getIdentifierPropertyName(Class ObjectClass) throws MappingException {
		return getPersister(ObjectClass).getIdentifierPropertyName();
	}

	private final void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		log.trace("deserializing");
		in.defaultReadObject();
		log.debug("deserialized: " + uuid);
	}
	private final void writeObject(ObjectOutputStream out) throws IOException {
		log.debug("serializing: " + uuid);
		out.defaultWriteObject();
		log.trace("serialized");
	}

	public Type[] getReturnTypes(String queryString) throws HibernateException {
		String[] queries = QueryTranslator.concreteQueries(queryString, this);
		if ( queries.length==0 ) throw new HibernateException("Query does not refer to any persistent classes: " + queryString);
		return getQuery( queries[0], true )[0].getReturnTypes();
	}

	public String getDefaultSchema() {
		return settings.getDefaultSchemaName();
	}

	public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException {
		return (ClassMetadata) classMetadata.get(persistentClass);
	}

	public CollectionMetadata getCollectionMetadata(String roleName) throws HibernateException {
		return (CollectionMetadata) collectionMetadata.get(roleName);
	}

	/**
	 * Return the names of all persistent (mapped) classes that extend or implement the
	 * given class or interface, accounting for implicit/explicit polymorphism settings
	 * and excluding mapped subclasses/joined-subclasses of other classes in the result.
	 */
	public String[] getImplementors(Class clazz) {
		ArrayList results = new ArrayList();
		Iterator iter = classPersisters.values().iterator();
		while ( iter.hasNext() ) {
			ClassPersister p = (ClassPersister) iter.next();
			if ( p instanceof Queryable ) {
				Queryable q = (Queryable) p;
				String className = q.getClassName();
				boolean isMappedClass = clazz.equals( q.getMappedClass() );
				if ( q.isExplicitPolymorphism() ) {
					if (isMappedClass) return new String[] { className };
				}
				else {
					if (isMappedClass) {
						results.add(className);
					}
					else if (
						clazz.isAssignableFrom( q.getMappedClass() ) &&
						( !q.isInherited() || !clazz.isAssignableFrom( q.getMappedSuperclass() ) )
					) {
						results.add(className);
					}
				}
			}
		}
		return (String[]) results.toArray( new String[ results.size() ] );
	}

	public String getImportedClassName(String className) {
		String result = (String) imports.get(className);
		return (result==null) ? className : result;
	}

	public Map getAllClassMetadata() throws HibernateException {
		return classMetadata;
	}

	public Map getAllCollectionMetadata() throws HibernateException {
		return collectionMetadata;
	}

	/**
	 * <ol>
	 * <li>close the prepared statement cache (and all prepared statements)
	 * <li>close the JDBC connection
	 * <li>remove the JNDI binding
	 * </ol>
	 */
	public void close() throws HibernateException {

		log.info("closing");

		Iterator iter = classPersisters.values().iterator();
		while ( iter.hasNext() ) {
			ClassPersister p = (ClassPersister) iter.next();
			if ( p.hasCache() ) p.getCache().destroy();
		}

		iter = collectionPersisters.values().iterator();
		while ( iter.hasNext() ) {
			CollectionPersister p = (CollectionPersister) iter.next();
			if ( p.hasCache() ) p.getCache().destroy();
		}

		try {
			settings.getConnectionProvider().close();
		}
		finally {
			SessionFactoryObjectFactory.removeInstance(uuid, name, properties);
		}

		if ( isQueryCacheEnabled() )  {
			queryCache.destroy();

			iter = queryCaches.values().iterator();
			while ( iter.hasNext() ) {
				QueryCache cache = (QueryCache) iter.next();
				cache.destroy();
			}
			updateTimestampsCache.destroy();
		}

		if ( settings.isAutoDropSchema() ) schemaExport.drop(false, true);

	}

	public void evict(Class persistentClass, Serializable id) throws HibernateException {
		ClassPersister p = getPersister(persistentClass);
		if ( p.hasCache() ) {
			if ( log.isDebugEnabled() ) log.debug( "evicting second-level cache: " + MessageHelper.infoString(p, id) );
			p.getCache().remove(id);
		}
	}

	public void evict(Class persistentClass) throws HibernateException {
		ClassPersister p = getPersister(persistentClass);
		if ( p.hasCache() ) {
			if ( log.isDebugEnabled() ) log.debug( "evicting second-level cache: " + p.getClassName() );
			p.getCache().clear();
		}
	}

	public void evictCollection(String roleName, Serializable id) throws HibernateException {
		CollectionPersister p = getCollectionPersister(roleName);
		if ( p.hasCache() ) {
			if ( log.isDebugEnabled() ) log.debug( "evicting second-level cache: " + MessageHelper.infoString(p, id) );
			p.getCache().remove(id);
		}
	}

	public void evictCollection(String roleName) throws HibernateException {
		CollectionPersister p = getCollectionPersister(roleName);
		if ( p.hasCache() ) {
			if ( log.isDebugEnabled() ) log.debug( "evicting second-level cache: " + p.getRole() );
			p.getCache().clear();
		}
	}

	public Integer getMaximumFetchDepth() {
		return settings.getMaximumFetchDepth();
	}

	public Type getPropertyType(Class persistentClass, String propertyName)
		throws MappingException {
		return getPersister(persistentClass).getPropertyType(propertyName);
	}

	public boolean isShowSqlEnabled() {
		return settings.isShowSqlEnabled();
	}

	public Integer getJdbcFetchSize() {
		return settings.getStatementFetchSize();
	}

	public ConnectionProvider getConnectionProvider() {
		return settings.getConnectionProvider();
	}

	public  UpdateTimestampsCache getUpdateTimestampsCache() {
		return updateTimestampsCache;
	}
	public QueryCache getQueryCache() {
		return queryCache;
	}

	public QueryCache getQueryCache(String cacheRegion) throws HibernateException {
		if (cacheRegion==null) {
			return getQueryCache();
		}

		QueryCache currentQueryCache = (QueryCache) queryCaches.get(cacheRegion);
		if (currentQueryCache==null) {
			currentQueryCache = settings.getQueryCacheFactory().getQueryCache(
			        cacheRegion,
			        settings.getCacheProvider(),
			        updateTimestampsCache,
			        properties
			);
			queryCaches.put(cacheRegion, currentQueryCache);
		}
		return currentQueryCache;
	}

	public boolean isQueryCacheEnabled() {
		return settings.isQueryCacheEnabled();
	}

	public boolean isJdbcBatchVersionedData() {
		return settings.isJdbcBatchVersionedData();
	}

	public void evictQueries() throws HibernateException {
		if (queryCache != null) {
			queryCache.clear();
			if ( queryCaches.size()==0 ) updateTimestampsCache.clear();
		}
	}

	public void evictQueries(String cacheRegion) throws HibernateException {
		if (cacheRegion==null) {
			throw new NullPointerException("use the zero-argument form to evict the default query cache");
		}
		else if(queryCaches != null) {
			QueryCache currentQueryCache = (QueryCache) queryCaches.get(cacheRegion);
			if (currentQueryCache!=null) currentQueryCache.clear();
		}
	}

	//TODO: a better way to normalize the NamedSQLQuery aspect
	static class InternalNamedSQLQuery {

		private String query;
		private String[] returnAliases;
		private Class[] returnClasses;
		private List querySpaces;

		public InternalNamedSQLQuery(String query, String[] aliases, Class[] clazz, List querySpaces) {
			this.returnClasses = clazz;
			this.returnAliases = aliases;
			this.query = query;
			this.querySpaces = querySpaces;
		}

		/**
		 * @return Returns the aliases.
		 */
		public String[] getReturnAliases() {
			return returnAliases;
		}
		/**
		 * @return Returns the clazz.
		 */
		public Class[] getReturnClasses() {
			return returnClasses;
		}
		/**
		 * @return Returns the query.
		 */
		public String getQueryString() {
			return query;
		}

		public java.util.Collection getQuerySpaces() {
			return querySpaces;
		}
	}

    /**
     * This equals method is as evil as Lord Vader himself. It approximates the sessionFactory by summing up all sorts
     * of sizes of configuration objects, in absence of a real hashCode() on those objects. The idea is to cheat the spring
     * TransactionSynchronizationManager to believe it is the same factory, so it can give you it's ThreadLocal cached version
     * of it. Definitely not recommended for projects with more than one session factory.
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionFactoryImpl)) return false;

        final SessionFactoryImpl sessionFactory = (SessionFactoryImpl) o;

        if (classPersisters != null ? !(classPersisters.size()==sessionFactory.classPersisters.size()) : sessionFactory.classPersisters != null) return false;
        if (classMetadata != null ? !(classMetadata.size()==sessionFactory.classMetadata.size()) : sessionFactory.classMetadata != null) return false;
        if (collectionMetadata != null ? !(collectionMetadata.size()==sessionFactory.collectionMetadata.size()) : sessionFactory.collectionMetadata != null) return false;
        if (collectionPersisters != null ? !(collectionPersisters.size()==sessionFactory.collectionPersisters.size()) : sessionFactory.collectionPersisters != null) return false;
        if (namedQueries != null ? !(namedQueries.size()==sessionFactory.namedQueries.size()) : sessionFactory.namedQueries != null) return false;
        if (namedSqlQueries != null ? !(namedSqlQueries.size()==sessionFactory.namedSqlQueries.size()) : sessionFactory.namedSqlQueries != null) return false;
        if (imports != null ? !(imports.size()==sessionFactory.imports.size()) : sessionFactory.imports != null) return false;
        if (name != null ? !name.equals(sessionFactory.name) : sessionFactory.name != null) return false;
        if (properties != null ? !properties.equals(sessionFactory.properties) : sessionFactory.properties != null) return false;

        return true;
    }

    /**
     * See equals
     * @return
     */
    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 29 * result + (classPersisters != null ? classPersisters.size() : 0);
        result = 29 * result + (classMetadata != null ? classMetadata.size() : 0);
        result = 29 * result + (collectionMetadata != null ? collectionMetadata.size() : 0);
        result = 29 * result + (collectionPersisters != null ? collectionPersisters.size() : 0);
        result = 29 * result + (namedQueries != null ? namedQueries.size() : 0);
        result = 29 * result + (namedSqlQueries != null ? namedSqlQueries.size() : 0);
        result = 29 * result + (imports != null ? imports.size() : 0);
        result = 29 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}

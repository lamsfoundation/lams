/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package org.hibernate.persister.collection;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.jdbc.batch.internal.BasicBatchKey;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SubselectFetch;
import org.hibernate.internal.FilterAliasGenerator;
import org.hibernate.internal.StaticFilterAliasGenerator;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.jdbc.Expectation;
import org.hibernate.jdbc.Expectations;
import org.hibernate.loader.collection.BatchingCollectionInitializerBuilder;
import org.hibernate.loader.collection.CollectionInitializer;
import org.hibernate.loader.collection.SubselectCollectionLoader;
import org.hibernate.mapping.Collection;
import org.hibernate.persister.entity.Joinable;
import org.hibernate.pretty.MessageHelper;
import org.hibernate.sql.Delete;
import org.hibernate.sql.Insert;
import org.hibernate.sql.SelectFragment;
import org.hibernate.sql.Update;
import org.hibernate.type.AssociationType;

/**
 * Collection persister for collections of values and many-to-many associations.
 *
 * @author Gavin King
 */
public class BasicCollectionPersister extends AbstractCollectionPersister {

	public boolean isCascadeDeleteEnabled() {
		return false;
	}

	public BasicCollectionPersister(
			Collection collection,
			CollectionRegionAccessStrategy cacheAccessStrategy,
			Configuration cfg,
			SessionFactoryImplementor factory) throws MappingException, CacheException {
		super( collection, cacheAccessStrategy, cfg, factory );
	}

	/**
	 * Generate the SQL DELETE that deletes all rows
	 */
	@Override
    protected String generateDeleteString() {
		
		Delete delete = new Delete()
				.setTableName( qualifiedTableName )
				.addPrimaryKeyColumns( keyColumnNames );
		
		if ( hasWhere ) delete.setWhere( sqlWhereString );
		
		if ( getFactory().getSettings().isCommentsEnabled() ) {
			delete.setComment( "delete collection " + getRole() );
		}
		
		return delete.toStatementString();
	}

	/**
	 * Generate the SQL INSERT that creates a new row
	 */
	@Override
    protected String generateInsertRowString() {
		
		Insert insert = new Insert( getDialect() )
				.setTableName( qualifiedTableName )
				.addColumns( keyColumnNames );
		
		if ( hasIdentifier) insert.addColumn( identifierColumnName );
		
		if ( hasIndex /*&& !indexIsFormula*/ ) {
			insert.addColumns( indexColumnNames, indexColumnIsSettable );
		}
		
		if ( getFactory().getSettings().isCommentsEnabled() ) {
			insert.setComment( "insert collection row " + getRole() );
		}
		
		//if ( !elementIsFormula ) {
			insert.addColumns( elementColumnNames, elementColumnIsSettable, elementColumnWriters );
		//}
		
		return insert.toStatementString();
	}

	/**
	 * Generate the SQL UPDATE that updates a row
	 */
	@Override
    protected String generateUpdateRowString() {
		
		Update update = new Update( getDialect() )
			.setTableName( qualifiedTableName );
		
		//if ( !elementIsFormula ) {
			update.addColumns( elementColumnNames, elementColumnIsSettable, elementColumnWriters );
		//}
		
		if ( hasIdentifier ) {
			update.addPrimaryKeyColumns( new String[]{ identifierColumnName } );
		}
		else if ( hasIndex && !indexContainsFormula ) {
			update.addPrimaryKeyColumns( ArrayHelper.join( keyColumnNames, indexColumnNames ) );
		}
		else {
			update.addPrimaryKeyColumns( keyColumnNames );
			update.addPrimaryKeyColumns( elementColumnNames, elementColumnIsInPrimaryKey, elementColumnWriters );
		}
		
		if ( getFactory().getSettings().isCommentsEnabled() ) {
			update.setComment( "update collection row " + getRole() );
		}
		
		return update.toStatementString();
	}
	
	@Override
	protected void doProcessQueuedOps(PersistentCollection collection, Serializable id, SessionImplementor session)
			throws HibernateException {
		// nothing to do
	}

	/**
	 * Generate the SQL DELETE that deletes a particular row
	 */
	@Override
    protected String generateDeleteRowString() {
		
		Delete delete = new Delete()
			.setTableName( qualifiedTableName );
		
		if ( hasIdentifier ) {
			delete.addPrimaryKeyColumns( new String[]{ identifierColumnName } );
		}
		else if ( hasIndex && !indexContainsFormula ) {
			delete.addPrimaryKeyColumns( ArrayHelper.join( keyColumnNames, indexColumnNames ) );
		}
		else {
			delete.addPrimaryKeyColumns( keyColumnNames );
			delete.addPrimaryKeyColumns( elementColumnNames, elementColumnIsInPrimaryKey, elementColumnWriters );
		}
		
		if ( getFactory().getSettings().isCommentsEnabled() ) {
			delete.setComment( "delete collection row " + getRole() );
		}
		
		return delete.toStatementString();
	}

	public boolean consumesEntityAlias() {
		return false;
	}

	public boolean consumesCollectionAlias() {
//		return !isOneToMany();
		return true;
	}

	public boolean isOneToMany() {
		return false;
	}

	@Override
    public boolean isManyToMany() {
		return elementType.isEntityType(); //instanceof AssociationType;
	}

	private BasicBatchKey updateBatchKey;

	@Override
    protected int doUpdateRows(Serializable id, PersistentCollection collection, SessionImplementor session)
			throws HibernateException {
		
		if ( ArrayHelper.isAllFalse(elementColumnIsSettable) ) return 0;

		try {
			PreparedStatement st = null;
			Expectation expectation = Expectations.appropriateExpectation( getUpdateCheckStyle() );
			boolean callable = isUpdateCallable();
			boolean useBatch = expectation.canBeBatched();
			Iterator entries = collection.entries( this );
			String sql = getSQLUpdateRowString();
			int i = 0;
			int count = 0;
			while ( entries.hasNext() ) {
				Object entry = entries.next();
				if ( collection.needsUpdating( entry, i, elementType ) ) {
					int offset = 1;

					if ( useBatch ) {
						if ( updateBatchKey == null ) {
							updateBatchKey = new BasicBatchKey(
									getRole() + "#UPDATE",
									expectation
							);
						}
						st = session.getTransactionCoordinator()
								.getJdbcCoordinator()
								.getBatch( updateBatchKey )
								.getBatchStatement( sql, callable );
					}
					else {
						st = session.getTransactionCoordinator()
								.getJdbcCoordinator()
								.getStatementPreparer()
								.prepareStatement( sql, callable );
					}

					try {
						offset+= expectation.prepare( st );
						int loc = writeElement( st, collection.getElement( entry ), offset, session );
						if ( hasIdentifier ) {
							writeIdentifier( st, collection.getIdentifier( entry, i ), loc, session );
						}
						else {
							loc = writeKey( st, id, loc, session );
							if ( hasIndex && !indexContainsFormula ) {
								writeIndexToWhere( st, collection.getIndex( entry, i, this ), loc, session );
							}
							else {
								writeElementToWhere( st, collection.getSnapshotElement( entry, i ), loc, session );
							}
						}

						if ( useBatch ) {
							session.getTransactionCoordinator()
									.getJdbcCoordinator()
									.getBatch( updateBatchKey )
									.addToBatch();
						}
						else {
							expectation.verifyOutcome( session.getTransactionCoordinator().getJdbcCoordinator().getResultSetReturn().executeUpdate( st ), st, -1 );
						}
					}
					catch ( SQLException sqle ) {
						if ( useBatch ) {
							session.getTransactionCoordinator().getJdbcCoordinator().abortBatch();
						}
						throw sqle;
					}
					finally {
						if ( !useBatch ) {
							session.getTransactionCoordinator().getJdbcCoordinator().release( st );
						}
					}
					count++;
				}
				i++;
			}
			return count;
		}
		catch ( SQLException sqle ) {
			throw getSQLExceptionHelper().convert(
					sqle,
					"could not update collection rows: " + MessageHelper.collectionInfoString( this, collection, id, session ),
					getSQLUpdateRowString()
				);
		}
	}

	public String selectFragment(
	        Joinable rhs,
	        String rhsAlias,
	        String lhsAlias,
	        String entitySuffix,
	        String collectionSuffix,
	        boolean includeCollectionColumns) {
		// we need to determine the best way to know that two joinables
		// represent a single many-to-many...
		if ( rhs != null && isManyToMany() && !rhs.isCollection() ) {
			AssociationType elementType = ( ( AssociationType ) getElementType() );
			if ( rhs.equals( elementType.getAssociatedJoinable( getFactory() ) ) ) {
				return manyToManySelectFragment( rhs, rhsAlias, lhsAlias, collectionSuffix );
			}
		}
		return includeCollectionColumns ? selectFragment( lhsAlias, collectionSuffix ) : "";
	}

	private String manyToManySelectFragment(
	        Joinable rhs,
	        String rhsAlias,
	        String lhsAlias,
	        String collectionSuffix) {
		SelectFragment frag = generateSelectFragment( lhsAlias, collectionSuffix );

		String[] elementColumnNames = rhs.getKeyColumnNames();
		frag.addColumns( rhsAlias, elementColumnNames, elementColumnAliases );
		appendIndexColumns( frag, lhsAlias );
		appendIdentifierColumns( frag, lhsAlias );

		return frag.toFragmentString()
				.substring( 2 ); //strip leading ','
	}

	/**
	 * Create the <tt>CollectionLoader</tt>
	 *
	 * @see org.hibernate.loader.collection.BasicCollectionLoader
	 */
	@Override
    protected CollectionInitializer createCollectionInitializer(LoadQueryInfluencers loadQueryInfluencers)
			throws MappingException {
		return BatchingCollectionInitializerBuilder.getBuilder( getFactory() )
				.createBatchingCollectionInitializer( this, batchSize, getFactory(), loadQueryInfluencers );
	}

	@Override
	public String fromJoinFragment(String alias, boolean innerJoin, boolean includeSubclasses) {
		return "";
	}

	@Override
	public String fromJoinFragment(String alias, boolean innerJoin, boolean includeSubclasses, Set<String> treatAsDeclarations) {
		return "";
	}

	@Override
	public String whereJoinFragment(String alias, boolean innerJoin, boolean includeSubclasses) {
		return "";
	}

	@Override
	public String whereJoinFragment(String alias, boolean innerJoin, boolean includeSubclasses, Set<String> treatAsDeclarations) {
		return "";
	}

	@Override
    protected CollectionInitializer createSubselectInitializer(SubselectFetch subselect, SessionImplementor session) {
		return new SubselectCollectionLoader( 
				this,
				subselect.toSubselectString( getCollectionType().getLHSPropertyName() ),
				subselect.getResult(),
				subselect.getQueryParameters(),
				subselect.getNamedParameterLocMap(),
				session.getFactory(),
				session.getLoadQueryInfluencers() 
		);
	}

	@Override
	public FilterAliasGenerator getFilterAliasGenerator(String rootAlias) {
		return new StaticFilterAliasGenerator(rootAlias);
	}

}

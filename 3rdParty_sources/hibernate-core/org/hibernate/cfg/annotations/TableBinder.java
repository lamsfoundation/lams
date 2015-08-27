/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
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
 */
package org.hibernate.cfg.annotations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.UniqueConstraint;

import org.hibernate.AnnotationException;
import org.hibernate.AssertionFailure;
import org.hibernate.annotations.Index;
import org.hibernate.cfg.BinderHelper;
import org.hibernate.cfg.Ejb3JoinColumn;
import org.hibernate.cfg.IndexOrUniqueKeySecondPass;
import org.hibernate.cfg.JPAIndexHolder;
import org.hibernate.cfg.Mappings;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.cfg.ObjectNameNormalizer;
import org.hibernate.cfg.ObjectNameSource;
import org.hibernate.cfg.UniqueConstraintHolder;
import org.hibernate.cfg.naming.NamingStrategyDelegate;
import org.hibernate.cfg.naming.NamingStrategyDelegator;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.collections.CollectionHelper;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.DependantValue;
import org.hibernate.mapping.JoinedSubclass;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.ToOne;
import org.hibernate.mapping.Value;

import org.jboss.logging.Logger;

/**
 * Table related operations
 *
 * @author Emmanuel Bernard
 */
@SuppressWarnings("unchecked")
public class TableBinder {
	//TODO move it to a getter/setter strategy
    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, TableBinder.class.getName());

	private String schema;
	private String catalog;
	private String name;
	private boolean isAbstract;
	private List<UniqueConstraintHolder> uniqueConstraints;
//	private List<String[]> uniqueConstraints;
	String constraints;
	Table denormalizedSuperTable;
	Mappings mappings;
	private String ownerEntityTable;
	private String associatedEntityTable;
	private String propertyName;
	private String ownerEntity;
	private String ownerJpaEntity;
	private String associatedEntity;
	private String associatedJpaEntity;
	private boolean isJPA2ElementCollection;
	private List<JPAIndexHolder> jpaIndexHolders;

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAbstract(boolean anAbstract) {
		isAbstract = anAbstract;
	}

	public void setUniqueConstraints(UniqueConstraint[] uniqueConstraints) {
		this.uniqueConstraints = TableBinder.buildUniqueConstraintHolders( uniqueConstraints );
	}

	public void setJpaIndex(javax.persistence.Index[] jpaIndex){
		this.jpaIndexHolders = buildJpaIndexHolder( jpaIndex );
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}

	public void setDenormalizedSuperTable(Table denormalizedSuperTable) {
		this.denormalizedSuperTable = denormalizedSuperTable;
	}

	public void setMappings(Mappings mappings) {
		this.mappings = mappings;
	}

	public void setJPA2ElementCollection(boolean isJPA2ElementCollection) {
		this.isJPA2ElementCollection = isJPA2ElementCollection;
	}

	private static class AssociationTableNameSource implements ObjectNameSource {
		private final String explicitName;
		private final String logicalName;

		private AssociationTableNameSource(String explicitName, String logicalName) {
			this.explicitName = explicitName;
			this.logicalName = logicalName;
		}

		public String getExplicitName() {
			return explicitName;
		}

		public String getLogicalName() {
			return logicalName;
		}
	}

	// only bind association table currently
	public Table bind() {
		//logicalName only accurate for assoc table...
		final String unquotedOwnerTable = StringHelper.unquote( ownerEntityTable );
		final String unquotedAssocTable = StringHelper.unquote( associatedEntityTable );

		//@ElementCollection use ownerEntity_property instead of the cleaner ownerTableName_property
		// ownerEntity can be null when the table name is explicitly set; <== gb: doesn't seem to be true...
		final String ownerObjectName = isJPA2ElementCollection && ownerEntity != null ?
				StringHelper.unqualify( ownerEntity ) : unquotedOwnerTable;
		final ObjectNameSource nameSource = buildNameContext(
				ownerObjectName,
				unquotedAssocTable
		);

		final boolean ownerEntityTableQuoted = StringHelper.isQuoted( ownerEntityTable );
		final boolean associatedEntityTableQuoted = StringHelper.isQuoted( associatedEntityTable );
		final ObjectNameNormalizer.NamingStrategyHelper namingStrategyHelper = new ObjectNameNormalizer.NamingStrategyHelper() {
			public String determineImplicitName(NamingStrategy strategy) {
				throw new AssertionFailure( "method call should have been replaced by #determineImplicitName(NamingStrategyDelegate strategyDelegate)" );
			}

			public String handleExplicitName(NamingStrategy strategy, String name) {
				return strategy.tableName( name );
			}

			@Override
			public String determineImplicitName(NamingStrategyDelegator strategyDelegator) {
				final NamingStrategyDelegate strategyDelegate = getNamingStrategyDelegate( strategyDelegator );
				final String strategyResult;
				if ( isJPA2ElementCollection ) {
					strategyResult = strategyDelegate.determineImplicitElementCollectionTableName(
							ownerEntity,
							ownerJpaEntity,
							unquotedOwnerTable,
							propertyName
					);
				}
				else {
					strategyResult =  strategyDelegate.determineImplicitEntityAssociationJoinTableName(
							ownerEntity,
							ownerJpaEntity,
							unquotedOwnerTable,
							associatedEntity,
							associatedJpaEntity,
							unquotedAssocTable,
							propertyName
					);
				}
				return ownerEntityTableQuoted || associatedEntityTableQuoted
						? StringHelper.quote( strategyResult )
						: strategyResult;
			}

			@Override
			public String handleExplicitName(NamingStrategyDelegator strategyDelegator, String name) {
				return getNamingStrategyDelegate( strategyDelegator ).toPhysicalTableName( name );
			}
		};

		return buildAndFillTable(
				schema,
				catalog,
				nameSource,
				namingStrategyHelper,
				isAbstract,
				uniqueConstraints,
				jpaIndexHolders,
				constraints,
				denormalizedSuperTable,
				mappings,
				null
		);
	}

	private ObjectNameSource buildNameContext(
			String unquotedOwnerTable,
			String unquotedAssocTable) {
		final NamingStrategyDelegate strategyDelegate = getNamingStrategyDelegate(
				mappings.getNamingStrategyDelegator()
		);
		String logicalName;
		if ( isJPA2ElementCollection ) {
			logicalName = strategyDelegate.determineLogicalElementCollectionTableName(
					name,
					ownerEntity,
					ownerJpaEntity,
					unquotedOwnerTable,
					propertyName
			);
		}
		else {
			logicalName =  strategyDelegate.determineLogicalEntityAssociationJoinTableName(
					name,
					ownerEntity,
					ownerJpaEntity,
					unquotedOwnerTable,
					associatedEntity,
					associatedJpaEntity,
					unquotedAssocTable,
					propertyName
			);
		}
		if ( StringHelper.isQuoted( ownerEntityTable ) || StringHelper.isQuoted( associatedEntityTable ) ) {
			logicalName = StringHelper.quote( logicalName );
		}

		return new AssociationTableNameSource( name, logicalName );
	}

	private NamingStrategyDelegate getNamingStrategyDelegate(
			NamingStrategyDelegator strategyDelegator) {
		return strategyDelegator.getNamingStrategyDelegate( false );
	}

	public static Table buildAndFillTable(
			String schema,
			String catalog,
			ObjectNameSource nameSource,
			ObjectNameNormalizer.NamingStrategyHelper namingStrategyHelper,
			boolean isAbstract,
			List<UniqueConstraintHolder> uniqueConstraints,
			List<JPAIndexHolder> jpaIndexHolders,
			String constraints,
			Table denormalizedSuperTable,
			Mappings mappings,
			String subselect){
		schema = BinderHelper.isEmptyAnnotationValue( schema ) ? mappings.getSchemaName() : schema;
		catalog = BinderHelper.isEmptyAnnotationValue( catalog ) ? mappings.getCatalogName() : catalog;

		String realTableName = mappings.getObjectNameNormalizer().normalizeDatabaseIdentifier(
				nameSource.getExplicitName(),
				namingStrategyHelper
		);

		final Table table;
		if ( denormalizedSuperTable != null ) {
			table = mappings.addDenormalizedTable(
					schema,
					catalog,
					realTableName,
					isAbstract,
					subselect,
					denormalizedSuperTable
			);
		}
		else {
			table = mappings.addTable(
					schema,
					catalog,
					realTableName,
					subselect,
					isAbstract
			);
		}

		if ( CollectionHelper.isNotEmpty( uniqueConstraints ) ) {
			mappings.addUniqueConstraintHolders( table, uniqueConstraints );
		}

		if ( CollectionHelper.isNotEmpty( jpaIndexHolders ) ) {
			mappings.addJpaIndexHolders( table, jpaIndexHolders );
		}

		if ( constraints != null ) table.addCheckConstraint( constraints );

		// logicalName is null if we are in the second pass
		final String logicalName = nameSource.getLogicalName();
		if ( logicalName != null ) {
			mappings.addTableBinding( schema, catalog, logicalName, realTableName, denormalizedSuperTable );
		}
		return table;
	}



	public static Table buildAndFillTable(
			String schema,
			String catalog,
			ObjectNameSource nameSource,
			ObjectNameNormalizer.NamingStrategyHelper namingStrategyHelper,
			boolean isAbstract,
			List<UniqueConstraintHolder> uniqueConstraints,
			String constraints,
			Table denormalizedSuperTable,
			Mappings mappings,
			String subselect) {
		return buildAndFillTable( schema, catalog, nameSource, namingStrategyHelper, isAbstract, uniqueConstraints, null, constraints
		, denormalizedSuperTable, mappings, subselect);
	}

	/**
	 * @deprecated Use {@link #buildAndFillTable} instead.
	 */
	@Deprecated
    @SuppressWarnings({ "JavaDoc" })
	public static Table fillTable(
			String schema,
			String catalog,
			String realTableName,
			String logicalName,
			boolean isAbstract,
			List uniqueConstraints,
			String constraints,
			Table denormalizedSuperTable,
			Mappings mappings) {
		schema = BinderHelper.isEmptyAnnotationValue( schema ) ? mappings.getSchemaName() : schema;
		catalog = BinderHelper.isEmptyAnnotationValue( catalog ) ? mappings.getCatalogName() : catalog;
		Table table;
		if ( denormalizedSuperTable != null ) {
			table = mappings.addDenormalizedTable(
					schema,
					catalog,
					realTableName,
					isAbstract,
					null, //subselect
					denormalizedSuperTable
			);
		}
		else {
			table = mappings.addTable(
					schema,
					catalog,
					realTableName,
					null, //subselect
					isAbstract
			);
		}
		if ( uniqueConstraints != null && uniqueConstraints.size() > 0 ) {
			mappings.addUniqueConstraints( table, uniqueConstraints );
		}
		if ( constraints != null ) table.addCheckConstraint( constraints );
		//logicalName is null if we are in the second pass
		if ( logicalName != null ) {
			mappings.addTableBinding( schema, catalog, logicalName, realTableName, denormalizedSuperTable );
		}
		return table;
	}

	public static void bindFk(
			PersistentClass referencedEntity,
			PersistentClass destinationEntity,
			Ejb3JoinColumn[] columns,
			SimpleValue value,
			boolean unique,
			Mappings mappings) {
		PersistentClass associatedClass;
		if ( destinationEntity != null ) {
			//overridden destination
			associatedClass = destinationEntity;
		}
		else {
			associatedClass = columns[0].getPropertyHolder() == null
					? null
					: columns[0].getPropertyHolder().getPersistentClass();
		}
		final String mappedByProperty = columns[0].getMappedBy();
		if ( StringHelper.isNotEmpty( mappedByProperty ) ) {
			/**
			 * Get the columns of the mapped-by property
			 * copy them and link the copy to the actual value
			 */
			LOG.debugf( "Retrieving property %s.%s", associatedClass.getEntityName(), mappedByProperty );

			final Property property = associatedClass.getRecursiveProperty( columns[0].getMappedBy() );
			Iterator mappedByColumns;
			if ( property.getValue() instanceof Collection ) {
				Collection collection = ( (Collection) property.getValue() );
				Value element = collection.getElement();
				if ( element == null ) {
					throw new AnnotationException(
							"Illegal use of mappedBy on both sides of the relationship: "
									+ associatedClass.getEntityName() + "." + mappedByProperty
					);
				}
				mappedByColumns = element.getColumnIterator();
			}
			else {
				mappedByColumns = property.getValue().getColumnIterator();
			}
			while ( mappedByColumns.hasNext() ) {
				Column column = (Column) mappedByColumns.next();
				columns[0].overrideFromReferencedColumnIfNecessary( column );
				columns[0].linkValueUsingAColumnCopy( column, value );
			}
		}
		else if ( columns[0].isImplicit() ) {
			/**
			 * if columns are implicit, then create the columns based on the
			 * referenced entity id columns
			 */
			Iterator idColumns;
			if ( referencedEntity instanceof JoinedSubclass ) {
				idColumns = referencedEntity.getKey().getColumnIterator();
			}
			else {
				idColumns = referencedEntity.getIdentifier().getColumnIterator();
			}
			while ( idColumns.hasNext() ) {
				Column column = (Column) idColumns.next();
				columns[0].overrideFromReferencedColumnIfNecessary( column );
				columns[0].linkValueUsingDefaultColumnNaming( column, referencedEntity, value );
			}
		}
		else {
			int fkEnum = Ejb3JoinColumn.checkReferencedColumnsType( columns, referencedEntity, mappings );

			if ( Ejb3JoinColumn.NON_PK_REFERENCE == fkEnum ) {
				String referencedPropertyName;
				if ( value instanceof ToOne ) {
					referencedPropertyName = ( (ToOne) value ).getReferencedPropertyName();
				}
				else if ( value instanceof DependantValue ) {
					String propertyName = columns[0].getPropertyName();
					if ( propertyName != null ) {
						Collection collection = (Collection) referencedEntity.getRecursiveProperty( propertyName )
								.getValue();
						referencedPropertyName = collection.getReferencedPropertyName();
					}
					else {
						throw new AnnotationException( "SecondaryTable JoinColumn cannot reference a non primary key" );
					}

				}
				else {
					throw new AssertionFailure(
							"Do a property ref on an unexpected Value type: "
									+ value.getClass().getName()
					);
				}
				if ( referencedPropertyName == null ) {
					throw new AssertionFailure(
							"No property ref found while expected"
					);
				}
				Property synthProp = referencedEntity.getReferencedProperty( referencedPropertyName );
				if ( synthProp == null ) {
					throw new AssertionFailure(
							"Cannot find synthProp: " + referencedEntity.getEntityName() + "." + referencedPropertyName
					);
				}
				linkJoinColumnWithValueOverridingNameIfImplicit(
						referencedEntity, synthProp.getColumnIterator(), columns, value
				);

			}
			else {
				if ( Ejb3JoinColumn.NO_REFERENCE == fkEnum ) {
					//implicit case, we hope PK and FK columns are in the same order
					if ( columns.length != referencedEntity.getIdentifier().getColumnSpan() ) {
						throw new AnnotationException(
								"A Foreign key refering " + referencedEntity.getEntityName()
										+ " from " + associatedClass.getEntityName()
										+ " has the wrong number of column. should be " + referencedEntity.getIdentifier()
										.getColumnSpan()
						);
					}
					linkJoinColumnWithValueOverridingNameIfImplicit(
							referencedEntity,
							referencedEntity.getIdentifier().getColumnIterator(),
							columns,
							value
					);
				}
				else {
					//explicit referencedColumnName
					Iterator idColItr = referencedEntity.getKey().getColumnIterator();
					org.hibernate.mapping.Column col;
					Table table = referencedEntity.getTable(); //works cause the pk has to be on the primary table
					if ( !idColItr.hasNext() ) {
						LOG.debug( "No column in the identifier!" );
					}
					while ( idColItr.hasNext() ) {
						boolean match = false;
						//for each PK column, find the associated FK column.
						col = (org.hibernate.mapping.Column) idColItr.next();
						for (Ejb3JoinColumn joinCol : columns) {
							String referencedColumn = joinCol.getReferencedColumn();
							referencedColumn = mappings.getPhysicalColumnName( referencedColumn, table );
							//In JPA 2 referencedColumnName is case insensitive
							if ( referencedColumn.equalsIgnoreCase( col.getQuotedName() ) ) {
								//proper join column
								if ( joinCol.isNameDeferred() ) {
									joinCol.linkValueUsingDefaultColumnNaming(
											col, referencedEntity, value
									);
								}
								else {
									joinCol.linkWithValue( value );
								}
								joinCol.overrideFromReferencedColumnIfNecessary( col );
								match = true;
								break;
							}
						}
						if ( !match ) {
							throw new AnnotationException(
									"Column name " + col.getName() + " of "
											+ referencedEntity.getEntityName() + " not found in JoinColumns.referencedColumnName"
							);
						}
					}
				}
			}
		}
		value.createForeignKey();
		if ( unique ) {
			createUniqueConstraint( value );
		}
	}

	public static void linkJoinColumnWithValueOverridingNameIfImplicit(
			PersistentClass referencedEntity,
			Iterator columnIterator,
			Ejb3JoinColumn[] columns,
			SimpleValue value) {
		for (Ejb3JoinColumn joinCol : columns) {
			Column synthCol = (Column) columnIterator.next();
			if ( joinCol.isNameDeferred() ) {
				//this has to be the default value
				joinCol.linkValueUsingDefaultColumnNaming( synthCol, referencedEntity, value );
			}
			else {
				joinCol.linkWithValue( value );
				joinCol.overrideFromReferencedColumnIfNecessary( synthCol );
			}
		}
	}

	public static void createUniqueConstraint(Value value) {
		Iterator iter = value.getColumnIterator();
		ArrayList cols = new ArrayList();
		while ( iter.hasNext() ) {
			cols.add( iter.next() );
		}
		value.getTable().createUniqueKey( cols );
	}

	public static void addIndexes(Table hibTable, Index[] indexes, Mappings mappings) {
		for (Index index : indexes) {
			//no need to handle inSecondPass here since it is only called from EntityBinder
			mappings.addSecondPass(
					new IndexOrUniqueKeySecondPass( hibTable, index.name(), index.columnNames(), mappings )
			);
		}
	}

	public static void addIndexes(Table hibTable, javax.persistence.Index[] indexes, Mappings mappings) {
		mappings.addJpaIndexHolders( hibTable, buildJpaIndexHolder( indexes ) );
	}

	public static List<JPAIndexHolder> buildJpaIndexHolder(javax.persistence.Index[] indexes){
		List<JPAIndexHolder> holders = new ArrayList<JPAIndexHolder>( indexes.length );
		for(javax.persistence.Index index : indexes){
			holders.add( new JPAIndexHolder( index ) );
		}
		return holders;
	}

	/**
	 * @deprecated Use {@link #buildUniqueConstraintHolders} instead
	 */
	@Deprecated
	@SuppressWarnings({ "JavaDoc" })
	public static List<String[]> buildUniqueConstraints(UniqueConstraint[] constraintsArray) {
		List<String[]> result = new ArrayList<String[]>();
		if ( constraintsArray.length != 0 ) {
			for (UniqueConstraint uc : constraintsArray) {
				result.add( uc.columnNames() );
			}
		}
		return result;
	}

	/**
	 * Build a list of {@link org.hibernate.cfg.UniqueConstraintHolder} instances given a list of
	 * {@link UniqueConstraint} annotations.
	 *
	 * @param annotations The {@link UniqueConstraint} annotations.
	 *
	 * @return The built {@link org.hibernate.cfg.UniqueConstraintHolder} instances.
	 */
	public static List<UniqueConstraintHolder> buildUniqueConstraintHolders(UniqueConstraint[] annotations) {
		List<UniqueConstraintHolder> result;
		if ( annotations == null || annotations.length == 0 ) {
			result = java.util.Collections.emptyList();
		}
		else {
			result = new ArrayList<UniqueConstraintHolder>( CollectionHelper.determineProperSizing( annotations.length ) );
			for ( UniqueConstraint uc : annotations ) {
				result.add(
						new UniqueConstraintHolder()
								.setName( uc.name() )
								.setColumns( uc.columnNames() )
				);
			}
		}
		return result;
	}

	public void setDefaultName(
			String ownerEntity,
			String ownerJpaEntity,
			String ownerEntityTable,
			String associatedEntity,
			String associatedJpaEntity,
			String associatedEntityTable,
			String propertyName
	) {
		this.ownerEntity = ownerEntity;
		this.ownerJpaEntity = ownerJpaEntity;
		this.ownerEntityTable = ownerEntityTable;
		this.associatedEntity = associatedEntity;
		this.associatedJpaEntity = associatedJpaEntity;
		this.associatedEntityTable = associatedEntityTable;
		this.propertyName = propertyName;
		this.name = null;
	}
}

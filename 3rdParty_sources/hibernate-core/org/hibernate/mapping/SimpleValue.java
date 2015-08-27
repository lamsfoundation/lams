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
package org.hibernate.mapping;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.persistence.AttributeConverter;

import org.hibernate.FetchMode;
import org.hibernate.MappingException;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.cfg.AttributeConverterDefinition;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.Mappings;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.converter.AttributeConverterSqlTypeDescriptorAdapter;
import org.hibernate.type.descriptor.converter.AttributeConverterTypeAdapter;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.java.JavaTypeDescriptorRegistry;
import org.hibernate.type.descriptor.sql.JdbcTypeJavaClassMappings;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptorRegistry;
import org.hibernate.usertype.DynamicParameterizedType;

import org.jboss.logging.Logger;

/**
 * Any value that maps to columns.
 * @author Gavin King
 */
public class SimpleValue implements KeyValue {
	private static final Logger log = Logger.getLogger( SimpleValue.class );

	public static final String DEFAULT_ID_GEN_STRATEGY = "assigned";

	private final Mappings mappings;

	private final List<Selectable> columns = new ArrayList<Selectable>();

	private String typeName;
	private Properties identifierGeneratorProperties;
	private String identifierGeneratorStrategy = DEFAULT_ID_GEN_STRATEGY;
	private String nullValue;
	private Table table;
	private String foreignKeyName;
	private boolean alternateUniqueKey;
	private Properties typeParameters;
	private boolean cascadeDeleteEnabled;

	private AttributeConverterDefinition attributeConverterDefinition;
	private Type type;

	public SimpleValue(Mappings mappings) {
		this.mappings = mappings;
	}

	public SimpleValue(Mappings mappings, Table table) {
		this( mappings );
		this.table = table;
	}

	public Mappings getMappings() {
		return mappings;
	}

	public boolean isCascadeDeleteEnabled() {
		return cascadeDeleteEnabled;
	}

	public void setCascadeDeleteEnabled(boolean cascadeDeleteEnabled) {
		this.cascadeDeleteEnabled = cascadeDeleteEnabled;
	}
	
	public void addColumn(Column column) {
		if ( !columns.contains(column) ) columns.add(column);
		column.setValue(this);
		column.setTypeIndex( columns.size()-1 );
	}
	
	public void addFormula(Formula formula) {
		columns.add(formula);
	}
	
	public boolean hasFormula() {
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			Object o = iter.next();
			if (o instanceof Formula) return true;
		}
		return false;
	}

	public int getColumnSpan() {
		return columns.size();
	}
	public Iterator<Selectable> getColumnIterator() {
		return columns.iterator();
	}
	public List getConstraintColumns() {
		return columns;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String type) {
		this.typeName = type;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public void createForeignKey() throws MappingException {}

	public void createForeignKeyOfEntity(String entityName) {
		if ( !hasFormula() && !"none".equals(getForeignKeyName())) {
			ForeignKey fk = table.createForeignKey( getForeignKeyName(), getConstraintColumns(), entityName );
			fk.setCascadeDeleteEnabled(cascadeDeleteEnabled);
		}
	}

	public IdentifierGenerator createIdentifierGenerator(
			IdentifierGeneratorFactory identifierGeneratorFactory,
			Dialect dialect, 
			String defaultCatalog, 
			String defaultSchema, 
			RootClass rootClass) throws MappingException {
		
		Properties params = new Properties();
		
		//if the hibernate-mapping did not specify a schema/catalog, use the defaults
		//specified by properties - but note that if the schema/catalog were specified
		//in hibernate-mapping, or as params, they will already be initialized and
		//will override the values set here (they are in identifierGeneratorProperties)
		if ( defaultSchema!=null ) {
			params.setProperty(PersistentIdentifierGenerator.SCHEMA, defaultSchema);
		}
		if ( defaultCatalog!=null ) {
			params.setProperty(PersistentIdentifierGenerator.CATALOG, defaultCatalog);
		}
		
		//pass the entity-name, if not a collection-id
		if (rootClass!=null) {
			params.setProperty( IdentifierGenerator.ENTITY_NAME, rootClass.getEntityName() );
			params.setProperty( IdentifierGenerator.JPA_ENTITY_NAME, rootClass.getJpaEntityName() );
		}
		
		//init the table here instead of earlier, so that we can get a quoted table name
		//TODO: would it be better to simply pass the qualified table name, instead of
		//      splitting it up into schema/catalog/table names
		String tableName = getTable().getQuotedName(dialect);
		params.setProperty( PersistentIdentifierGenerator.TABLE, tableName );
		
		//pass the column name (a generated id almost always has a single column)
		String columnName = ( (Column) getColumnIterator().next() ).getQuotedName(dialect);
		params.setProperty( PersistentIdentifierGenerator.PK, columnName );
		
		if (rootClass!=null) {
			StringBuilder tables = new StringBuilder();
			Iterator iter = rootClass.getIdentityTables().iterator();
			while ( iter.hasNext() ) {
				Table table= (Table) iter.next();
				tables.append( table.getQuotedName(dialect) );
				if ( iter.hasNext() ) tables.append(", ");
			}
			params.setProperty( PersistentIdentifierGenerator.TABLES, tables.toString() );
		}
		else {
			params.setProperty( PersistentIdentifierGenerator.TABLES, tableName );
		}

		if (identifierGeneratorProperties!=null) {
			params.putAll(identifierGeneratorProperties);
		}

		// TODO : we should pass along all settings once "config lifecycle" is hashed out...
		params.put(
				Environment.PREFER_POOLED_VALUES_LO,
				mappings.getConfigurationProperties().getProperty( Environment.PREFER_POOLED_VALUES_LO, "false" )
		);

		identifierGeneratorFactory.setDialect( dialect );
		return identifierGeneratorFactory.createIdentifierGenerator( identifierGeneratorStrategy, getType(), params );
		
	}

	public boolean isUpdateable() {
		//needed to satisfy KeyValue
		return true;
	}
	
	public FetchMode getFetchMode() {
		return FetchMode.SELECT;
	}

	public Properties getIdentifierGeneratorProperties() {
		return identifierGeneratorProperties;
	}

	public String getNullValue() {
		return nullValue;
	}

	public Table getTable() {
		return table;
	}

	/**
	 * Returns the identifierGeneratorStrategy.
	 * @return String
	 */
	public String getIdentifierGeneratorStrategy() {
		return identifierGeneratorStrategy;
	}
	
	public boolean isIdentityColumn(IdentifierGeneratorFactory identifierGeneratorFactory, Dialect dialect) {
		identifierGeneratorFactory.setDialect( dialect );
		return identifierGeneratorFactory.getIdentifierGeneratorClass( identifierGeneratorStrategy )
				.equals( IdentityGenerator.class );
	}

	/**
	 * Sets the identifierGeneratorProperties.
	 * @param identifierGeneratorProperties The identifierGeneratorProperties to set
	 */
	public void setIdentifierGeneratorProperties(Properties identifierGeneratorProperties) {
		this.identifierGeneratorProperties = identifierGeneratorProperties;
	}

	/**
	 * Sets the identifierGeneratorStrategy.
	 * @param identifierGeneratorStrategy The identifierGeneratorStrategy to set
	 */
	public void setIdentifierGeneratorStrategy(String identifierGeneratorStrategy) {
		this.identifierGeneratorStrategy = identifierGeneratorStrategy;
	}

	/**
	 * Sets the nullValue.
	 * @param nullValue The nullValue to set
	 */
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	public String getForeignKeyName() {
		return foreignKeyName;
	}

	public void setForeignKeyName(String foreignKeyName) {
		this.foreignKeyName = foreignKeyName;
	}

	public boolean isAlternateUniqueKey() {
		return alternateUniqueKey;
	}

	public void setAlternateUniqueKey(boolean unique) {
		this.alternateUniqueKey = unique;
	}

	public boolean isNullable() {
		if ( hasFormula() ) return true;
		boolean nullable = true;
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			if ( !( (Column) iter.next() ).isNullable() ) {
				nullable = false;
				return nullable; //shortcut
			}
		}
		return nullable;
	}

	public boolean isSimpleValue() {
		return true;
	}

	public boolean isValid(Mapping mapping) throws MappingException {
		return getColumnSpan()==getType().getColumnSpan(mapping);
	}

	public Type getType() throws MappingException {
		if ( type != null ) {
			return type;
		}

		if ( typeName == null ) {
			throw new MappingException( "No type name" );
		}
		if ( typeParameters != null
				&& Boolean.valueOf( typeParameters.getProperty( DynamicParameterizedType.IS_DYNAMIC ) )
				&& typeParameters.get( DynamicParameterizedType.PARAMETER_TYPE ) == null ) {
			createParameterImpl();
		}

		Type result = mappings.getTypeResolver().heuristicType( typeName, typeParameters );
		if ( result == null ) {
			String msg = "Could not determine type for: " + typeName;
			if ( table != null ) {
				msg += ", at table: " + table.getName();
			}
			if ( columns != null && columns.size() > 0 ) {
				msg += ", for columns: " + columns;
			}
			throw new MappingException( msg );
		}

		return result;
	}

	public void setTypeUsingReflection(String className, String propertyName) throws MappingException {
		// NOTE : this is called as the last piece in setting SimpleValue type information, and implementations
		// rely on that fact, using it as a signal that all information it is going to get is defined at this point...

		if ( typeName != null ) {
			// assume either (a) explicit type was specified or (b) determine was already performed
			return;
		}

		if ( type != null ) {
			return;
		}

		if ( attributeConverterDefinition == null ) {
			// this is here to work like legacy.  This should change when we integrate with metamodel to
			// look for SqlTypeDescriptor and JavaTypeDescriptor individually and create the BasicType (well, really
			// keep a registry of [SqlTypeDescriptor,JavaTypeDescriptor] -> BasicType...)
			if ( className == null ) {
				throw new MappingException( "you must specify types for a dynamic entity: " + propertyName );
			}
			typeName = ReflectHelper.reflectedPropertyClass( className, propertyName ).getName();
			return;
		}

		// we had an AttributeConverter...
		type = buildAttributeConverterTypeAdapter();
	}

	/**
	 * Build a Hibernate Type that incorporates the JPA AttributeConverter.  AttributeConverter works totally in
	 * memory, meaning it converts between one Java representation (the entity attribute representation) and another
	 * (the value bound into JDBC statements or extracted from results).  However, the Hibernate Type system operates
	 * at the lower level of actually dealing directly with those JDBC objects.  So even though we have an
	 * AttributeConverter, we still need to "fill out" the rest of the BasicType data and bridge calls
	 * to bind/extract through the converter.
	 * <p/>
	 * Essentially the idea here is that an intermediate Java type needs to be used.  Let's use an example as a means
	 * to illustrate...  Consider an {@code AttributeConverter<Integer,String>}.  This tells Hibernate that the domain
	 * model defines this attribute as an Integer value (the 'entityAttributeJavaType'), but that we need to treat the
	 * value as a String (the 'databaseColumnJavaType') when dealing with JDBC (aka, the database type is a
	 * VARCHAR/CHAR):<ul>
	 *     <li>
	 *         When binding values to PreparedStatements we need to convert the Integer value from the entity
	 *         into a String and pass that String to setString.  The conversion is handled by calling
	 *         {@link AttributeConverter#convertToDatabaseColumn(Object)}
	 *     </li>
	 *     <li>
	 *         When extracting values from ResultSets (or CallableStatement parameters) we need to handle the
	 *         value via getString, and convert that returned String to an Integer.  That conversion is handled
	 *         by calling {@link AttributeConverter#convertToEntityAttribute(Object)}
	 *     </li>
	 * </ul>
	 *
	 * @return The built AttributeConverter -> Type adapter
	 *
	 * @todo : ultimately I want to see attributeConverterJavaType and attributeConverterJdbcTypeCode specify-able separately
	 * then we can "play them against each other" in terms of determining proper typing
	 *
	 * @todo : see if we already have previously built a custom on-the-fly BasicType for this AttributeConverter; see note below about caching
	 */
	@SuppressWarnings("unchecked")
	private Type buildAttributeConverterTypeAdapter() {
		// todo : validate the number of columns present here?

		final Class entityAttributeJavaType = attributeConverterDefinition.getEntityAttributeType();
		final Class databaseColumnJavaType = attributeConverterDefinition.getDatabaseColumnType();


		// resolve the JavaTypeDescriptor ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// For the JavaTypeDescriptor portion we simply resolve the "entity attribute representation" part of
		// the AttributeConverter to resolve the corresponding descriptor.
		final JavaTypeDescriptor entityAttributeJavaTypeDescriptor = JavaTypeDescriptorRegistry.INSTANCE.getDescriptor( entityAttributeJavaType );


		// build the SqlTypeDescriptor adapter ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Going back to the illustration, this should be a SqlTypeDescriptor that handles the Integer <-> String
		//		conversions.  This is the more complicated piece.  First we need to determine the JDBC type code
		//		corresponding to the AttributeConverter's declared "databaseColumnJavaType" (how we read that value out
		// 		of ResultSets).  See JdbcTypeJavaClassMappings for details.  Again, given example, this should return
		// 		VARCHAR/CHAR
		final int jdbcTypeCode = JdbcTypeJavaClassMappings.INSTANCE.determineJdbcTypeCodeForJavaClass( databaseColumnJavaType );
		// find the standard SqlTypeDescriptor for that JDBC type code.
		final SqlTypeDescriptor sqlTypeDescriptor = SqlTypeDescriptorRegistry.INSTANCE.getDescriptor( jdbcTypeCode );
		// find the JavaTypeDescriptor representing the "intermediate database type representation".  Back to the
		// 		illustration, this should be the type descriptor for Strings
		final JavaTypeDescriptor intermediateJavaTypeDescriptor = JavaTypeDescriptorRegistry.INSTANCE.getDescriptor( databaseColumnJavaType );
		// and finally construct the adapter, which injects the AttributeConverter calls into the binding/extraction
		// 		process...
		final SqlTypeDescriptor sqlTypeDescriptorAdapter = new AttributeConverterSqlTypeDescriptorAdapter(
				attributeConverterDefinition.getAttributeConverter(),
				sqlTypeDescriptor,
				intermediateJavaTypeDescriptor
		);

		// todo : cache the AttributeConverterTypeAdapter in case that AttributeConverter is applied multiple times.

		final String name = String.format(
				"BasicType adapter for AttributeConverter<%s,%s>",
				entityAttributeJavaType.getSimpleName(),
				databaseColumnJavaType.getSimpleName()
		);
		return new AttributeConverterTypeAdapter(
				name,
				attributeConverterDefinition.getAttributeConverter(),
				sqlTypeDescriptorAdapter,
				entityAttributeJavaType,
				databaseColumnJavaType,
				entityAttributeJavaTypeDescriptor
		);
	}

	public boolean isTypeSpecified() {
		return typeName!=null;
	}

	public void setTypeParameters(Properties parameterMap) {
		this.typeParameters = parameterMap;
	}
	
	public Properties getTypeParameters() {
		return typeParameters;
	}

	@Override
    public String toString() {
		return getClass().getName() + '(' + columns.toString() + ')';
	}

	public Object accept(ValueVisitor visitor) {
		return visitor.accept(this);
	}
	
	public boolean[] getColumnInsertability() {
		boolean[] result = new boolean[ getColumnSpan() ];
		int i = 0;
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			Selectable s = (Selectable) iter.next();
			result[i++] = !s.isFormula();
		}
		return result;
	}
	
	public boolean[] getColumnUpdateability() {
		return getColumnInsertability();
	}

	public void setJpaAttributeConverterDefinition(AttributeConverterDefinition attributeConverterDefinition) {
		this.attributeConverterDefinition = attributeConverterDefinition;
	}

	private void createParameterImpl() {
		try {
			String[] columnsNames = new String[columns.size()];
			for ( int i = 0; i < columns.size(); i++ ) {
				Selectable column = columns.get(i);
				if (column instanceof Column){
					columnsNames[i] = ((Column) column).getName();
				}
			}

			final XProperty xProperty = (XProperty) typeParameters.get( DynamicParameterizedType.XPROPERTY );
			// todo : not sure this works for handling @MapKeyEnumerated
			final Annotation[] annotations = xProperty == null
					? null
					: xProperty.getAnnotations();

			typeParameters.put(
					DynamicParameterizedType.PARAMETER_TYPE,
					new ParameterTypeImpl(
							ReflectHelper.classForName(
									typeParameters.getProperty( DynamicParameterizedType.RETURNED_CLASS )
							),
							annotations,
							table.getCatalog(),
							table.getSchema(),
							table.getName(),
							Boolean.valueOf( typeParameters.getProperty( DynamicParameterizedType.IS_PRIMARY_KEY ) ),
							columnsNames
					)
			);
		}
		catch ( ClassNotFoundException cnfe ) {
			throw new MappingException( "Could not create DynamicParameterizedType for type: " + typeName, cnfe );
		}
	}

	private final class ParameterTypeImpl implements DynamicParameterizedType.ParameterType {

		private final Class returnedClass;
		private final Annotation[] annotationsMethod;
		private final String catalog;
		private final String schema;
		private final String table;
		private final boolean primaryKey;
		private final String[] columns;

		private ParameterTypeImpl(Class returnedClass, Annotation[] annotationsMethod, String catalog, String schema,
				String table, boolean primaryKey, String[] columns) {
			this.returnedClass = returnedClass;
			this.annotationsMethod = annotationsMethod;
			this.catalog = catalog;
			this.schema = schema;
			this.table = table;
			this.primaryKey = primaryKey;
			this.columns = columns;
		}

		@Override
		public Class getReturnedClass() {
			return returnedClass;
		}

		@Override
		public Annotation[] getAnnotationsMethod() {
			return annotationsMethod;
		}

		@Override
		public String getCatalog() {
			return catalog;
		}

		@Override
		public String getSchema() {
			return schema;
		}

		@Override
		public String getTable() {
			return table;
		}

		@Override
		public boolean isPrimaryKey() {
			return primaryKey;
		}

		@Override
		public String[] getColumns() {
			return columns;
		}
	}
}

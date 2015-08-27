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
package org.hibernate.hql.internal.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.QueryException;
import org.hibernate.engine.internal.JoinSequence;
import org.hibernate.engine.internal.ParameterBinder;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.antlr.HqlSqlBaseWalker;
import org.hibernate.hql.internal.antlr.HqlSqlTokenTypes;
import org.hibernate.hql.internal.antlr.HqlTokenTypes;
import org.hibernate.hql.internal.antlr.SqlTokenTypes;
import org.hibernate.hql.internal.ast.tree.AggregateNode;
import org.hibernate.hql.internal.ast.tree.AssignmentSpecification;
import org.hibernate.hql.internal.ast.tree.CastFunctionNode;
import org.hibernate.hql.internal.ast.tree.CollectionFunction;
import org.hibernate.hql.internal.ast.tree.ConstructorNode;
import org.hibernate.hql.internal.ast.tree.DeleteStatement;
import org.hibernate.hql.internal.ast.tree.DotNode;
import org.hibernate.hql.internal.ast.tree.FromClause;
import org.hibernate.hql.internal.ast.tree.FromElement;
import org.hibernate.hql.internal.ast.tree.FromElementFactory;
import org.hibernate.hql.internal.ast.tree.FromReferenceNode;
import org.hibernate.hql.internal.ast.tree.IdentNode;
import org.hibernate.hql.internal.ast.tree.IndexNode;
import org.hibernate.hql.internal.ast.tree.InsertStatement;
import org.hibernate.hql.internal.ast.tree.IntoClause;
import org.hibernate.hql.internal.ast.tree.MethodNode;
import org.hibernate.hql.internal.ast.tree.OperatorNode;
import org.hibernate.hql.internal.ast.tree.ParameterContainer;
import org.hibernate.hql.internal.ast.tree.ParameterNode;
import org.hibernate.hql.internal.ast.tree.QueryNode;
import org.hibernate.hql.internal.ast.tree.ResolvableNode;
import org.hibernate.hql.internal.ast.tree.RestrictableStatement;
import org.hibernate.hql.internal.ast.tree.ResultVariableRefNode;
import org.hibernate.hql.internal.ast.tree.SelectClause;
import org.hibernate.hql.internal.ast.tree.SelectExpression;
import org.hibernate.hql.internal.ast.tree.UpdateStatement;
import org.hibernate.hql.internal.ast.util.ASTPrinter;
import org.hibernate.hql.internal.ast.util.ASTUtil;
import org.hibernate.hql.internal.ast.util.AliasGenerator;
import org.hibernate.hql.internal.ast.util.JoinProcessor;
import org.hibernate.hql.internal.ast.util.LiteralProcessor;
import org.hibernate.hql.internal.ast.util.NodeTraverser;
import org.hibernate.hql.internal.ast.util.SessionFactoryHelper;
import org.hibernate.hql.internal.ast.util.SyntheticAndFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.id.BulkInsertionCapableIdentifierGenerator;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.CoreLogging;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.param.CollectionFilterKeyParameterSpecification;
import org.hibernate.param.NamedParameterSpecification;
import org.hibernate.param.ParameterSpecification;
import org.hibernate.param.PositionalParameterSpecification;
import org.hibernate.param.VersionTypeSeedParameterSpecification;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.sql.JoinType;
import org.hibernate.type.AssociationType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.DbTimestampType;
import org.hibernate.type.Type;
import org.hibernate.type.VersionType;
import org.hibernate.usertype.UserVersionType;

import antlr.ASTFactory;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.collections.AST;

/**
 * Implements methods used by the HQL->SQL tree transform grammar (a.k.a. the second phase).
 * <ul>
 * <li>Isolates the Hibernate API-specific code from the ANTLR generated code.</li>
 * <li>Handles the SQL fragments generated by the persisters in order to create the SELECT and FROM clauses,
 * taking into account the joins and projections that are implied by the mappings (persister/queryable).</li>
 * <li>Uses SqlASTFactory to create customized AST nodes.</li>
 * </ul>
 *
 * @see SqlASTFactory
 */
public class HqlSqlWalker extends HqlSqlBaseWalker implements ErrorReporter, ParameterBinder.NamedParameterSource {
	private static final CoreMessageLogger LOG = CoreLogging.messageLogger( HqlSqlWalker.class );

	private final QueryTranslatorImpl queryTranslatorImpl;
	private final HqlParser hqlParser;
	private final SessionFactoryHelper sessionFactoryHelper;
	private final Map tokenReplacements;
	private final AliasGenerator aliasGenerator = new AliasGenerator();
	private final LiteralProcessor literalProcessor;
	private final ParseErrorHandler parseErrorHandler;
	private final ASTPrinter printer;
	private final String collectionFilterRole;

	private FromClause currentFromClause;
	private SelectClause selectClause;

	/**
	 * Maps each top-level result variable to its SelectExpression;
	 * (excludes result variables defined in subqueries)
	 */
	private Map<String, SelectExpression> selectExpressionsByResultVariable = new HashMap<String, SelectExpression>();

	private Set<Serializable> querySpaces = new HashSet<Serializable>();

	private int parameterCount;
	private Map namedParameters = new HashMap();
	private ArrayList<ParameterSpecification> parameters = new ArrayList<ParameterSpecification>();
	private int numberOfParametersInSetClause;
	private int positionalParameterCount;

	private ArrayList assignmentSpecifications = new ArrayList();

	private JoinType impliedJoinType = JoinType.INNER_JOIN;

	/**
	 * Create a new tree transformer.
	 *
	 * @param qti Back pointer to the query translator implementation that is using this tree transform.
	 * @param sfi The session factory implementor where the Hibernate mappings can be found.
	 * @param parser A reference to the phase-1 parser
	 * @param tokenReplacements Registers the token replacement map with the walker.  This map will
	 * be used to substitute function names and constants.
	 * @param collectionRole The collection role name of the collection used as the basis for the
	 * filter, NULL if this is not a collection filter compilation.
	 */
	public HqlSqlWalker(
			QueryTranslatorImpl qti,
			SessionFactoryImplementor sfi,
			HqlParser parser,
			Map tokenReplacements,
			String collectionRole) {
		setASTFactory( new SqlASTFactory( this ) );
		// Initialize the error handling delegate.
		this.parseErrorHandler = new ErrorCounter( qti.getQueryString() );
		this.queryTranslatorImpl = qti;
		this.sessionFactoryHelper = new SessionFactoryHelper( sfi );
		this.literalProcessor = new LiteralProcessor( this );
		this.tokenReplacements = tokenReplacements;
		this.collectionFilterRole = collectionRole;
		this.hqlParser = parser;
		this.printer = new ASTPrinter( SqlTokenTypes.class );
	}

	// handle trace logging ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private int traceDepth;

	@Override
	public void traceIn(String ruleName, AST tree) {
		if ( !LOG.isTraceEnabled() ) {
			return;
		}
		if ( inputState.guessing > 0 ) {
			return;
		}
		String prefix = StringHelper.repeat( '-', ( traceDepth++ * 2 ) ) + "-> ";
		String traceText = ruleName + " (" + buildTraceNodeName( tree ) + ")";
		LOG.trace( prefix + traceText );
	}

	private String buildTraceNodeName(AST tree) {
		return tree == null
				? "???"
				: tree.getText() + " [" + printer.getTokenTypeName( tree.getType() ) + "]";
	}

	@Override
	public void traceOut(String ruleName, AST tree) {
		if ( !LOG.isTraceEnabled() ) {
			return;
		}
		if ( inputState.guessing > 0 ) {
			return;
		}
		String prefix = "<-" + StringHelper.repeat( '-', ( --traceDepth * 2 ) ) + " ";
		LOG.trace( prefix + ruleName );
	}

	@Override
	protected void prepareFromClauseInputTree(AST fromClauseInput) {
		if ( !isSubQuery() ) {
//			// inject param specifications to account for dynamic filter param values
//			if ( ! getEnabledFilters().isEmpty() ) {
//				Iterator filterItr = getEnabledFilters().values().iterator();
//				while ( filterItr.hasNext() ) {
//					FilterImpl filter = ( FilterImpl ) filterItr.next();
//					if ( ! filter.getFilterDefinition().getParameterNames().isEmpty() ) {
//						Iterator paramItr = filter.getFilterDefinition().getParameterNames().iterator();
//						while ( paramItr.hasNext() ) {
//							String parameterName = ( String ) paramItr.next();
//							// currently param filters *only* work with single-column parameter types;
//							// if that limitation is ever lifted, this logic will need to change to account for that
//							ParameterNode collectionFilterKeyParameter = ( ParameterNode ) astFactory.create( PARAM, "?" );
//							DynamicFilterParameterSpecification paramSpec = new DynamicFilterParameterSpecification(
//									filter.getName(),
//									parameterName,
//									filter.getFilterDefinition().getParameterType( parameterName ),
//									 positionalParameterCount++
//							);
//							collectionFilterKeyParameter.setHqlParameterSpecification( paramSpec );
//							parameters.add( paramSpec );
//						}
//					}
//				}
//			}

			if ( isFilter() ) {
				// Handle collection-filter compilation.
				// IMPORTANT NOTE: This is modifying the INPUT (HQL) tree, not the output tree!
				QueryableCollection persister = sessionFactoryHelper.getCollectionPersister( collectionFilterRole );
				Type collectionElementType = persister.getElementType();
				if ( !collectionElementType.isEntityType() ) {
					throw new QueryException( "collection of values in filter: this" );
				}

				String collectionElementEntityName = persister.getElementPersister().getEntityName();
				ASTFactory inputAstFactory = hqlParser.getASTFactory();
				AST fromElement = inputAstFactory.create( HqlTokenTypes.FILTER_ENTITY, collectionElementEntityName );
				ASTUtil.createSibling( inputAstFactory, HqlTokenTypes.ALIAS, "this", fromElement );
				fromClauseInput.addChild( fromElement );
				// Show the modified AST.
				LOG.debug( "prepareFromClauseInputTree() : Filter - Added 'this' as a from element..." );
				queryTranslatorImpl.showHqlAst( hqlParser.getAST() );

				// Create a parameter specification for the collection filter...
				Type collectionFilterKeyType = sessionFactoryHelper.requireQueryableCollection( collectionFilterRole )
						.getKeyType();
				ParameterNode collectionFilterKeyParameter = (ParameterNode) astFactory.create( PARAM, "?" );
				CollectionFilterKeyParameterSpecification collectionFilterKeyParameterSpec = new CollectionFilterKeyParameterSpecification(
						collectionFilterRole, collectionFilterKeyType, positionalParameterCount++
				);
				collectionFilterKeyParameter.setHqlParameterSpecification( collectionFilterKeyParameterSpec );
				parameters.add( collectionFilterKeyParameterSpec );
			}
		}
	}

	public boolean isFilter() {
		return collectionFilterRole != null;
	}

	public String getCollectionFilterRole() {
		return collectionFilterRole;
	}

	public SessionFactoryHelper getSessionFactoryHelper() {
		return sessionFactoryHelper;
	}

	public Map getTokenReplacements() {
		return tokenReplacements;
	}

	public AliasGenerator getAliasGenerator() {
		return aliasGenerator;
	}

	public FromClause getCurrentFromClause() {
		return currentFromClause;
	}

	public ParseErrorHandler getParseErrorHandler() {
		return parseErrorHandler;
	}

	@Override
	public void reportError(RecognitionException e) {
		parseErrorHandler.reportError( e ); // Use the delegate.
	}

	@Override
	public void reportError(String s) {
		parseErrorHandler.reportError( s ); // Use the delegate.
	}

	@Override
	public void reportWarning(String s) {
		parseErrorHandler.reportWarning( s );
	}

	/**
	 * Returns the set of unique query spaces (a.k.a.
	 * table names) that occurred in the query.
	 *
	 * @return A set of table names (Strings).
	 */
	public Set<Serializable> getQuerySpaces() {
		return querySpaces;
	}

	@Override
	protected AST createFromElement(String path, AST alias, AST propertyFetch) throws SemanticException {
		FromElement fromElement = currentFromClause.addFromElement( path, alias );
		fromElement.setAllPropertyFetch( propertyFetch != null );
		return fromElement;
	}

	@Override
	protected AST createFromFilterElement(AST filterEntity, AST alias) throws SemanticException {
		FromElement fromElement = currentFromClause.addFromElement( filterEntity.getText(), alias );
		FromClause fromClause = fromElement.getFromClause();
		QueryableCollection persister = sessionFactoryHelper.getCollectionPersister( collectionFilterRole );
		// Get the names of the columns used to link between the collection
		// owner and the collection elements.
		String[] keyColumnNames = persister.getKeyColumnNames();
		String fkTableAlias = persister.isOneToMany()
				? fromElement.getTableAlias()
				: fromClause.getAliasGenerator().createName( collectionFilterRole );
		JoinSequence join = sessionFactoryHelper.createJoinSequence();
		join.setRoot( persister, fkTableAlias );
		if ( !persister.isOneToMany() ) {
			join.addJoin(
					(AssociationType) persister.getElementType(),
					fromElement.getTableAlias(),
					JoinType.INNER_JOIN,
					persister.getElementColumnNames( fkTableAlias )
			);
		}
		join.addCondition( fkTableAlias, keyColumnNames, " = ?" );
		fromElement.setJoinSequence( join );
		fromElement.setFilter( true );
		LOG.debug( "createFromFilterElement() : processed filter FROM element." );
		return fromElement;
	}

	@Override
	protected void createFromJoinElement(
			AST path,
			AST alias,
			int joinType,
			AST fetchNode,
			AST propertyFetch,
			AST with) throws SemanticException {
		boolean fetch = fetchNode != null;
		if ( fetch && isSubQuery() ) {
			throw new QueryException( "fetch not allowed in subquery from-elements" );
		}
		// The path AST should be a DotNode, and it should have been evaluated already.
		if ( path.getType() != SqlTokenTypes.DOT ) {
			throw new SemanticException( "Path expected for join!" );
		}
		DotNode dot = (DotNode) path;
		JoinType hibernateJoinType = JoinProcessor.toHibernateJoinType( joinType );
		dot.setJoinType( hibernateJoinType );    // Tell the dot node about the join type.
		dot.setFetch( fetch );
		// Generate an explicit join for the root dot node.   The implied joins will be collected and passed up
		// to the root dot node.
		dot.resolve( true, false, alias == null ? null : alias.getText() );

		final FromElement fromElement;
		if ( dot.getDataType() != null && dot.getDataType().isComponentType() ) {
			if ( dot.getDataType().isAnyType() ) {
				throw new SemanticException( "An AnyType attribute cannot be join fetched" );
				// ^^ because the discriminator (aka, the "meta columns") must be known to the SQL in
				// 		a non-parameterized way.
			}
			FromElementFactory factory = new FromElementFactory(
					getCurrentFromClause(),
					dot.getLhs().getFromElement(),
					dot.getPropertyPath(),
					alias == null ? null : alias.getText(),
					null,
					false
			);
			fromElement = factory.createComponentJoin( (ComponentType) dot.getDataType() );
		}
		else {
			fromElement = dot.getImpliedJoin();
			fromElement.setAllPropertyFetch( propertyFetch != null );

			if ( with != null ) {
				if ( fetch ) {
					throw new SemanticException( "with-clause not allowed on fetched associations; use filters" );
				}
				handleWithFragment( fromElement, with );
			}
		}

		if ( LOG.isDebugEnabled() ) {
			LOG.debug( "createFromJoinElement() : " + getASTPrinter().showAsString( fromElement, "-- join tree --" ) );
		}
	}

	private void handleWithFragment(FromElement fromElement, AST hqlWithNode) throws SemanticException {
		try {
			withClause( hqlWithNode );
			AST hqlSqlWithNode = returnAST;
			if ( LOG.isDebugEnabled() ) {
				LOG.debug(
						"handleWithFragment() : " + getASTPrinter().showAsString(
								hqlSqlWithNode,
								"-- with clause --"
						)
				);
			}
			WithClauseVisitor visitor = new WithClauseVisitor( fromElement, queryTranslatorImpl );
			NodeTraverser traverser = new NodeTraverser( visitor );
			traverser.traverseDepthFirst( hqlSqlWithNode );

			String withClauseJoinAlias = visitor.getJoinAlias();
			if ( withClauseJoinAlias == null ) {
				withClauseJoinAlias = fromElement.getCollectionTableAlias();
			}
			else {
				FromElement referencedFromElement = visitor.getReferencedFromElement();
				if ( referencedFromElement != fromElement ) {
					LOG.warnf(
							"with-clause expressions do not reference the from-clause element to which the " +
									"with-clause was associated.  The query may not work as expected [%s]",
							queryTranslatorImpl.getQueryString()
					);
				}
			}

			SqlGenerator sql = new SqlGenerator( getSessionFactoryHelper().getFactory() );
			sql.whereExpr( hqlSqlWithNode.getFirstChild() );

			fromElement.setWithClauseFragment( withClauseJoinAlias, "(" + sql.getSQL() + ")" );
		}
		catch (SemanticException e) {
			throw e;
		}
		catch (InvalidWithClauseException e) {
			throw e;
		}
		catch (Exception e) {
			throw new SemanticException( e.getMessage() );
		}
	}

	private static class WithClauseVisitor implements NodeTraverser.VisitationStrategy {
		private final FromElement joinFragment;
		private final QueryTranslatorImpl queryTranslatorImpl;

		private FromElement referencedFromElement;
		private String joinAlias;

		public WithClauseVisitor(FromElement fromElement, QueryTranslatorImpl queryTranslatorImpl) {
			this.joinFragment = fromElement;
			this.queryTranslatorImpl = queryTranslatorImpl;
		}

		public void visit(AST node) {
			// TODO : currently expects that the individual with expressions apply to the same sql table join.
			//      This may not be the case for joined-subclass where the property values
			//      might be coming from different tables in the joined hierarchy.  At some
			//      point we should expand this to support that capability.  However, that has
			//      some difficulties:
			//          1) the biggest is how to handle ORs when the individual comparisons are
			//              linked to different sql joins.
			//          2) here we would need to track each comparison individually, along with
			//              the join alias to which it applies and then pass that information
			//              back to the FromElement so it can pass it along to the JoinSequence
			if ( node instanceof DotNode ) {
				DotNode dotNode = (DotNode) node;
				FromElement fromElement = dotNode.getFromElement();
				if ( referencedFromElement != null ) {
					if ( fromElement != referencedFromElement ) {
						throw new HibernateException( "with-clause referenced two different from-clause elements" );
					}
				}
				else {
					referencedFromElement = fromElement;
					joinAlias = extractAppliedAlias( dotNode );
					// TODO : temporary
					//      needed because currently persister is the one that
					// creates and renders the join fragments for inheritance
					//      hierarchies...
					if ( !joinAlias.equals( referencedFromElement.getTableAlias() ) ) {
						throw new InvalidWithClauseException(
								"with clause can only reference columns in the driving table",
								queryTranslatorImpl.getQueryString()
						);
					}
				}
			}
			else if ( node instanceof ParameterNode ) {
				applyParameterSpecification( ( (ParameterNode) node ).getHqlParameterSpecification() );
			}
			else if ( node instanceof ParameterContainer ) {
				applyParameterSpecifications( (ParameterContainer) node );
			}
		}

		private void applyParameterSpecifications(ParameterContainer parameterContainer) {
			if ( parameterContainer.hasEmbeddedParameters() ) {
				ParameterSpecification[] specs = parameterContainer.getEmbeddedParameters();
				for ( ParameterSpecification spec : specs ) {
					applyParameterSpecification( spec );
				}
			}
		}

		private void applyParameterSpecification(ParameterSpecification paramSpec) {
			joinFragment.addEmbeddedParameter( paramSpec );
		}

		private String extractAppliedAlias(DotNode dotNode) {
			return dotNode.getText().substring( 0, dotNode.getText().indexOf( '.' ) );
		}

		public FromElement getReferencedFromElement() {
			return referencedFromElement;
		}

		public String getJoinAlias() {
			return joinAlias;
		}
	}

	/**
	 * Sets the current 'FROM' context.
	 *
	 * @param fromNode The new 'FROM' context.
	 * @param inputFromNode The from node from the input AST.
	 */
	@Override
	protected void pushFromClause(AST fromNode, AST inputFromNode) {
		FromClause newFromClause = (FromClause) fromNode;
		newFromClause.setParentFromClause( currentFromClause );
		currentFromClause = newFromClause;
	}

	/**
	 * Returns to the previous 'FROM' context.
	 */
	private void popFromClause() {
		currentFromClause = currentFromClause.getParentFromClause();
	}

	@Override
	protected void lookupAlias(AST aliasRef)
			throws SemanticException {
		FromElement alias = currentFromClause.getFromElement( aliasRef.getText() );
		FromReferenceNode aliasRefNode = (FromReferenceNode) aliasRef;
		aliasRefNode.setFromElement( alias );
	}

	@Override
	protected void setImpliedJoinType(int joinType) {
		impliedJoinType = JoinProcessor.toHibernateJoinType( joinType );
	}

	public JoinType getImpliedJoinType() {
		return impliedJoinType;
	}

	@Override
	protected AST lookupProperty(AST dot, boolean root, boolean inSelect) throws SemanticException {
		DotNode dotNode = (DotNode) dot;
		FromReferenceNode lhs = dotNode.getLhs();
		AST rhs = lhs.getNextSibling();
		switch ( rhs.getType() ) {
			case SqlTokenTypes.ELEMENTS:
			case SqlTokenTypes.INDICES:
				if ( LOG.isDebugEnabled() ) {
					LOG.debugf(
							"lookupProperty() %s => %s(%s)",
							dotNode.getPath(),
							rhs.getText(),
							lhs.getPath()
					);
				}
				CollectionFunction f = (CollectionFunction) rhs;
				// Re-arrange the tree so that the collection function is the root and the lhs is the path.
				f.setFirstChild( lhs );
				lhs.setNextSibling( null );
				dotNode.setFirstChild( f );
				resolve( lhs );            // Don't forget to resolve the argument!
				f.resolve( inSelect );    // Resolve the collection function now.
				return f;
			default:
				// Resolve everything up to this dot, but don't resolve the placeholders yet.
				dotNode.resolveFirstChild();
				return dotNode;
		}
	}

	@Override
	protected boolean isNonQualifiedPropertyRef(AST ident) {
		final String identText = ident.getText();
		if ( currentFromClause.isFromElementAlias( identText ) ) {
			return false;
		}

		List fromElements = currentFromClause.getExplicitFromElements();
		if ( fromElements.size() == 1 ) {
			final FromElement fromElement = (FromElement) fromElements.get( 0 );
			try {
				LOG.tracev( "Attempting to resolve property [{0}] as a non-qualified ref", identText );
				return fromElement.getPropertyMapping( identText ).toType( identText ) != null;
			}
			catch (QueryException e) {
				// Should mean that no such property was found
			}
		}

		return false;
	}

	@Override
	protected AST lookupNonQualifiedProperty(AST property) throws SemanticException {
		final FromElement fromElement = (FromElement) currentFromClause.getExplicitFromElements().get( 0 );
		AST syntheticDotNode = generateSyntheticDotNodeForNonQualifiedPropertyRef( property, fromElement );
		return lookupProperty( syntheticDotNode, false, getCurrentClauseType() == HqlSqlTokenTypes.SELECT );
	}

	private AST generateSyntheticDotNodeForNonQualifiedPropertyRef(AST property, FromElement fromElement) {
		AST dot = getASTFactory().create( DOT, "{non-qualified-property-ref}" );
		// TODO : better way?!?
		( (DotNode) dot ).setPropertyPath( ( (FromReferenceNode) property ).getPath() );

		IdentNode syntheticAlias = (IdentNode) getASTFactory().create( IDENT, "{synthetic-alias}" );
		syntheticAlias.setFromElement( fromElement );
		syntheticAlias.setResolved();

		dot.setFirstChild( syntheticAlias );
		dot.addChild( property );

		return dot;
	}

	@Override
	protected void processQuery(AST select, AST query) throws SemanticException {
		if ( LOG.isDebugEnabled() ) {
			LOG.debugf( "processQuery() : %s", query.toStringTree() );
		}

		try {
			QueryNode qn = (QueryNode) query;

			// Was there an explicit select expression?
			boolean explicitSelect = select != null && select.getNumberOfChildren() > 0;

			// Add in the EntityGraph attribute nodes.
			if ( queryTranslatorImpl.getEntityGraphQueryHint() != null ) {
				qn.getFromClause().getFromElements().addAll(
						queryTranslatorImpl.getEntityGraphQueryHint().toFromElements( qn.getFromClause(), this )
				);
			}

			if ( !explicitSelect ) {
				// No explicit select expression; render the id and properties
				// projection lists for every persister in the from clause into
				// a single 'token node'.
				//TODO: the only reason we need this stuff now is collection filters,
				//      we should get rid of derived select clause completely!
				createSelectClauseFromFromClause( qn );
			}
			else {
				// Use the explicitly declared select expression; determine the
				// return types indicated by each select token
				useSelectClause( select );
			}

			// After that, process the JOINs.
			// Invoke a delegate to do the work, as this is farily complex.
			JoinProcessor joinProcessor = new JoinProcessor( this );
			joinProcessor.processJoins( qn );

			// Attach any mapping-defined "ORDER BY" fragments
			Iterator itr = qn.getFromClause().getProjectionList().iterator();
			while ( itr.hasNext() ) {
				final FromElement fromElement = (FromElement) itr.next();
//			if ( fromElement.isFetch() && fromElement.isCollectionJoin() ) {
				if ( fromElement.isFetch() && fromElement.getQueryableCollection() != null ) {
					// Does the collection referenced by this FromElement
					// specify an order-by attribute?  If so, attach it to
					// the query's order-by
					if ( fromElement.getQueryableCollection().hasOrdering() ) {
						String orderByFragment = fromElement
								.getQueryableCollection()
								.getSQLOrderByString( fromElement.getCollectionTableAlias() );
						qn.getOrderByClause().addOrderFragment( orderByFragment );
					}
					if ( fromElement.getQueryableCollection().hasManyToManyOrdering() ) {
						String orderByFragment = fromElement.getQueryableCollection()
								.getManyToManyOrderByString( fromElement.getTableAlias() );
						qn.getOrderByClause().addOrderFragment( orderByFragment );
					}
				}
			}
		}
		finally {
			popFromClause();
		}
	}

	protected void postProcessDML(RestrictableStatement statement) throws SemanticException {
		statement.getFromClause().resolve();

		FromElement fromElement = (FromElement) statement.getFromClause().getFromElements().get( 0 );
		Queryable persister = fromElement.getQueryable();
		// Make #@%$^#^&# sure no alias is applied to the table name
		fromElement.setText( persister.getTableName() );

//		// append any filter fragments; the EMPTY_MAP is used under the assumption that
//		// currently enabled filters should not affect this process
//		if ( persister.getDiscriminatorType() != null ) {
//			new SyntheticAndFactory( getASTFactory() ).addDiscriminatorWhereFragment(
//			        statement,
//			        persister,
//			        java.util.Collections.EMPTY_MAP,
//			        fromElement.getTableAlias()
//			);
//		}
		if ( persister.getDiscriminatorType() != null || !queryTranslatorImpl.getEnabledFilters().isEmpty() ) {
			new SyntheticAndFactory( this ).addDiscriminatorWhereFragment(
					statement,
					persister,
					queryTranslatorImpl.getEnabledFilters(),
					fromElement.getTableAlias()
			);
		}

	}

	@Override
	protected void postProcessUpdate(AST update) throws SemanticException {
		UpdateStatement updateStatement = (UpdateStatement) update;

		postProcessDML( updateStatement );
	}

	@Override
	protected void postProcessDelete(AST delete) throws SemanticException {
		postProcessDML( (DeleteStatement) delete );
	}

	@Override
	protected void postProcessInsert(AST insert) throws SemanticException, QueryException {
		InsertStatement insertStatement = (InsertStatement) insert;
		insertStatement.validate();

		SelectClause selectClause = insertStatement.getSelectClause();
		Queryable persister = insertStatement.getIntoClause().getQueryable();

		if ( !insertStatement.getIntoClause().isExplicitIdInsertion() ) {
			// the insert did not explicitly reference the id.  See if
			//		1) that is allowed
			//		2) whether we need to alter the SQL tree to account for id
			final IdentifierGenerator generator = persister.getIdentifierGenerator();
			if ( !BulkInsertionCapableIdentifierGenerator.class.isInstance( generator ) ) {
				throw new QueryException(
						"Invalid identifier generator encountered for implicit id handling as part of bulk insertions"
				);
			}
			final BulkInsertionCapableIdentifierGenerator capableGenerator =
					BulkInsertionCapableIdentifierGenerator.class.cast( generator );
			if ( !capableGenerator.supportsBulkInsertionIdentifierGeneration() ) {
				throw new QueryException(
						"Identifier generator reported it does not support implicit id handling as part of bulk insertions"
				);
			}

			final String fragment = capableGenerator.determineBulkInsertionIdentifierGenerationSelectFragment(
					sessionFactoryHelper.getFactory().getDialect()
			);
			if ( fragment != null ) {
				// we got a fragment from the generator, so alter the sql tree...
				//
				// first, wrap the fragment as a node
				AST fragmentNode = getASTFactory().create( HqlSqlTokenTypes.SQL_TOKEN, fragment );
				// next, rearrange the SQL tree to add the fragment node as the first select expression
				AST originalFirstSelectExprNode = selectClause.getFirstChild();
				selectClause.setFirstChild( fragmentNode );
				fragmentNode.setNextSibling( originalFirstSelectExprNode );
				// finally, prepend the id column name(s) to the insert-spec
				insertStatement.getIntoClause().prependIdColumnSpec();
			}
		}

		if ( sessionFactoryHelper.getFactory().getDialect().supportsParametersInInsertSelect() ) {
			AST child = selectClause.getFirstChild();
			int i = 0;
			while ( child != null ) {
				if ( child instanceof ParameterNode ) {
					// infer the parameter type from the type listed in the INSERT INTO clause
					( (ParameterNode) child ).setExpectedType(
							insertStatement.getIntoClause()
									.getInsertionTypes()[selectClause.getParameterPositions().get( i )]
					);
					i++;
				}
				child = child.getNextSibling();
			}
		}

		final boolean includeVersionProperty = persister.isVersioned() &&
				!insertStatement.getIntoClause().isExplicitVersionInsertion() &&
				persister.isVersionPropertyInsertable();
		if ( includeVersionProperty ) {
			// We need to seed the version value as part of this bulk insert
			VersionType versionType = persister.getVersionType();
			AST versionValueNode = null;

			if ( sessionFactoryHelper.getFactory().getDialect().supportsParametersInInsertSelect() ) {
				int[] sqlTypes = versionType.sqlTypes( sessionFactoryHelper.getFactory() );
				if ( sqlTypes == null || sqlTypes.length == 0 ) {
					throw new IllegalStateException( versionType.getClass() + ".sqlTypes() returns null or empty array" );
				}
				if ( sqlTypes.length > 1 ) {
					throw new IllegalStateException(
							versionType.getClass() +
									".sqlTypes() returns > 1 element; only single-valued versions are allowed."
					);
				}
				versionValueNode = getASTFactory().create( HqlSqlTokenTypes.PARAM, "?" );
				ParameterSpecification paramSpec = new VersionTypeSeedParameterSpecification( versionType );
				( (ParameterNode) versionValueNode ).setHqlParameterSpecification( paramSpec );
				parameters.add( 0, paramSpec );

				if ( sessionFactoryHelper.getFactory().getDialect().requiresCastingOfParametersInSelectClause() ) {
					// we need to wrtap the param in a cast()
					MethodNode versionMethodNode = (MethodNode) getASTFactory().create(
							HqlSqlTokenTypes.METHOD_CALL,
							"("
					);
					AST methodIdentNode = getASTFactory().create( HqlSqlTokenTypes.IDENT, "cast" );
					versionMethodNode.addChild( methodIdentNode );
					versionMethodNode.initializeMethodNode( methodIdentNode, true );
					AST castExprListNode = getASTFactory().create( HqlSqlTokenTypes.EXPR_LIST, "exprList" );
					methodIdentNode.setNextSibling( castExprListNode );
					castExprListNode.addChild( versionValueNode );
					versionValueNode.setNextSibling(
							getASTFactory().create(
									HqlSqlTokenTypes.IDENT,
									sessionFactoryHelper.getFactory().getDialect().getTypeName( sqlTypes[0] )
							)
					);
					processFunction( versionMethodNode, true );
					versionValueNode = versionMethodNode;
				}
			}
			else {
				if ( isIntegral( versionType ) ) {
					try {
						Object seedValue = versionType.seed( null );
						versionValueNode = getASTFactory().create( HqlSqlTokenTypes.SQL_TOKEN, seedValue.toString() );
					}
					catch (Throwable t) {
						throw new QueryException( "could not determine seed value for version on bulk insert [" + versionType + "]" );
					}
				}
				else if ( isDatabaseGeneratedTimestamp( versionType ) ) {
					String functionName = sessionFactoryHelper.getFactory()
							.getDialect()
							.getCurrentTimestampSQLFunctionName();
					versionValueNode = getASTFactory().create( HqlSqlTokenTypes.SQL_TOKEN, functionName );
				}
				else {
					throw new QueryException( "cannot handle version type [" + versionType + "] on bulk inserts with dialects not supporting parameters in insert-select statements" );
				}
			}

			AST currentFirstSelectExprNode = selectClause.getFirstChild();
			selectClause.setFirstChild( versionValueNode );
			versionValueNode.setNextSibling( currentFirstSelectExprNode );

			insertStatement.getIntoClause().prependVersionColumnSpec();
		}

		if ( insertStatement.getIntoClause().isDiscriminated() ) {
			String sqlValue = insertStatement.getIntoClause().getQueryable().getDiscriminatorSQLValue();
			AST discrimValue = getASTFactory().create( HqlSqlTokenTypes.SQL_TOKEN, sqlValue );
			insertStatement.getSelectClause().addChild( discrimValue );
		}

	}

	private boolean isDatabaseGeneratedTimestamp(Type type) {
		// currently only the Hibernate-supplied DbTimestampType is supported here
		return DbTimestampType.class.isAssignableFrom( type.getClass() );
	}

	private boolean isIntegral(Type type) {
		return Long.class.isAssignableFrom( type.getReturnedClass() )
				|| Integer.class.isAssignableFrom( type.getReturnedClass() )
				|| long.class.isAssignableFrom( type.getReturnedClass() )
				|| int.class.isAssignableFrom( type.getReturnedClass() );
	}

	private void useSelectClause(AST select) throws SemanticException {
		selectClause = (SelectClause) select;
		selectClause.initializeExplicitSelectClause( currentFromClause );
	}

	private void createSelectClauseFromFromClause(QueryNode qn) throws SemanticException {
		AST select = astFactory.create( SELECT_CLAUSE, "{derived select clause}" );
		AST sibling = qn.getFromClause();
		qn.setFirstChild( select );
		select.setNextSibling( sibling );
		selectClause = (SelectClause) select;
		selectClause.initializeDerivedSelectClause( currentFromClause );
		LOG.debug( "Derived SELECT clause created." );
	}

	@Override
	protected void resolve(AST node) throws SemanticException {
		if ( node != null ) {
			// This is called when it's time to fully resolve a path expression.
			ResolvableNode r = (ResolvableNode) node;
			if ( isInFunctionCall() ) {
				r.resolveInFunctionCall( false, true );
			}
			else {
				r.resolve( false, true );    // Generate implicit joins, only if necessary.
			}
		}
	}

	@Override
	protected void resolveSelectExpression(AST node) throws SemanticException {
		// This is called when it's time to fully resolve a path expression.
		int type = node.getType();
		switch ( type ) {
			case DOT: {
				DotNode dot = (DotNode) node;
				dot.resolveSelectExpression();
				break;
			}
			case ALIAS_REF: {
				// Notify the FROM element that it is being referenced by the select.
				FromReferenceNode aliasRefNode = (FromReferenceNode) node;
				//aliasRefNode.resolve( false, false, aliasRefNode.getText() ); //TODO: is it kosher to do it here?
				aliasRefNode.resolve( false, false ); //TODO: is it kosher to do it here?
				FromElement fromElement = aliasRefNode.getFromElement();
				if ( fromElement != null ) {
					fromElement.setIncludeSubclasses( true );
				}
				break;
			}
			default: {
				break;
			}
		}
	}

	@Override
	protected void beforeSelectClause() throws SemanticException {
		// Turn off includeSubclasses on all FromElements.
		FromClause from = getCurrentFromClause();
		List fromElements = from.getFromElements();
		for ( Iterator iterator = fromElements.iterator(); iterator.hasNext(); ) {
			FromElement fromElement = (FromElement) iterator.next();
			fromElement.setIncludeSubclasses( false );
		}
	}

	@Override
	protected AST generatePositionalParameter(AST inputNode) throws SemanticException {
		if ( namedParameters.size() > 0 ) {
			throw new SemanticException(
					"cannot define positional parameter after any named parameters have been defined"
			);
		}
		LOG.warnf(
				"[DEPRECATION] Encountered positional parameter near line %s, column %s.  Positional parameter " +
						"are considered deprecated; use named parameters or JPA-style positional parameters instead.",
				inputNode.getLine(),
				inputNode.getColumn()
		);
		ParameterNode parameter = (ParameterNode) astFactory.create( PARAM, "?" );
		PositionalParameterSpecification paramSpec = new PositionalParameterSpecification(
				inputNode.getLine(),
				inputNode.getColumn(),
				positionalParameterCount++
		);
		parameter.setHqlParameterSpecification( paramSpec );
		parameters.add( paramSpec );
		return parameter;
	}

	@Override
	protected AST generateNamedParameter(AST delimiterNode, AST nameNode) throws SemanticException {
		String name = nameNode.getText();
		trackNamedParameterPositions( name );

		// create the node initially with the param name so that it shows
		// appropriately in the "original text" attribute
		ParameterNode parameter = (ParameterNode) astFactory.create( NAMED_PARAM, name );
		parameter.setText( "?" );

		NamedParameterSpecification paramSpec = new NamedParameterSpecification(
				delimiterNode.getLine(),
				delimiterNode.getColumn(),
				name
		);
		parameter.setHqlParameterSpecification( paramSpec );
		parameters.add( paramSpec );
		return parameter;
	}

	private void trackNamedParameterPositions(String name) {
		Integer loc = parameterCount++;
		Object o = namedParameters.get( name );
		if ( o == null ) {
			namedParameters.put( name, loc );
		}
		else if ( o instanceof Integer ) {
			ArrayList list = new ArrayList( 4 );
			list.add( o );
			list.add( loc );
			namedParameters.put( name, list );
		}
		else {
			( (ArrayList) o ).add( loc );
		}
	}

	@Override
	protected void processConstant(AST constant) throws SemanticException {
		literalProcessor.processConstant(
				constant,
				true
		);  // Use the delegate, resolve identifiers as FROM element aliases.
	}

	@Override
	protected void processBoolean(AST constant) throws SemanticException {
		literalProcessor.processBoolean( constant );  // Use the delegate.
	}

	@Override
	protected void processNumericLiteral(AST literal) {
		literalProcessor.processNumeric( literal );
	}

	@Override
	protected void processIndex(AST indexOp) throws SemanticException {
		IndexNode indexNode = (IndexNode) indexOp;
		indexNode.resolve( true, true );
	}

	@Override
	protected void processFunction(AST functionCall, boolean inSelect) throws SemanticException {
		MethodNode methodNode = (MethodNode) functionCall;
		methodNode.resolve( inSelect );
	}

	@Override
	protected void processCastFunction(AST castFunctionCall, boolean inSelect) throws SemanticException {
		CastFunctionNode castFunctionNode = (CastFunctionNode) castFunctionCall;
		castFunctionNode.resolve( inSelect );
	}

	@Override
	protected void processAggregation(AST node, boolean inSelect) throws SemanticException {
		AggregateNode aggregateNode = (AggregateNode) node;
		aggregateNode.resolve();
	}

	@Override
	protected void processConstructor(AST constructor) throws SemanticException {
		ConstructorNode constructorNode = (ConstructorNode) constructor;
		constructorNode.prepare();
	}

	@Override
	protected void setAlias(AST selectExpr, AST ident) {
		( (SelectExpression) selectExpr ).setAlias( ident.getText() );
		// only put the alias (i.e., result variable) in selectExpressionsByResultVariable
		// if is not defined in a subquery.
		if ( !isSubQuery() ) {
			selectExpressionsByResultVariable.put( ident.getText(), (SelectExpression) selectExpr );
		}
	}

	@Override
	protected boolean isOrderExpressionResultVariableRef(AST orderExpressionNode) throws SemanticException {
		// ORDER BY is not supported in a subquery
		// TODO: should an exception be thrown if an ORDER BY is in a subquery?
		if ( !isSubQuery() &&
				orderExpressionNode.getType() == IDENT &&
				selectExpressionsByResultVariable.containsKey( orderExpressionNode.getText() ) ) {
			return true;
		}
		return false;
	}

	@Override
	protected void handleResultVariableRef(AST resultVariableRef) throws SemanticException {
		if ( isSubQuery() ) {
			throw new SemanticException(
					"References to result variables in subqueries are not supported."
			);
		}
		( (ResultVariableRefNode) resultVariableRef ).setSelectExpression(
				selectExpressionsByResultVariable.get( resultVariableRef.getText() )
		);
	}

	/**
	 * Returns the locations of all occurrences of the named parameter.
	 */
	public int[] getNamedParameterLocations(String name) throws QueryException {
		Object o = namedParameters.get( name );
		if ( o == null ) {
			throw new QueryException(
					QueryTranslator.ERROR_NAMED_PARAMETER_DOES_NOT_APPEAR + name,
					queryTranslatorImpl.getQueryString()
			);
		}
		if ( o instanceof Integer ) {
			return new int[] {(Integer) o};
		}
		else {
			return ArrayHelper.toIntArray( (ArrayList) o );
		}
	}

	public void addQuerySpaces(Serializable[] spaces) {
		querySpaces.addAll( Arrays.asList( spaces ) );
	}

	public Type[] getReturnTypes() {
		return selectClause.getQueryReturnTypes();
	}

	public String[] getReturnAliases() {
		return selectClause.getQueryReturnAliases();
	}

	public SelectClause getSelectClause() {
		return selectClause;
	}

	public FromClause getFinalFromClause() {
		FromClause top = currentFromClause;
		while ( top.getParentFromClause() != null ) {
			top = top.getParentFromClause();
		}
		return top;
	}

	public boolean isShallowQuery() {
		// select clauses for insert statements should alwasy be treated as shallow
		return getStatementType() == INSERT || queryTranslatorImpl.isShallowQuery();
	}

	public Map getEnabledFilters() {
		return queryTranslatorImpl.getEnabledFilters();
	}

	public LiteralProcessor getLiteralProcessor() {
		return literalProcessor;
	}

	public ASTPrinter getASTPrinter() {
		return printer;
	}

	public ArrayList<ParameterSpecification> getParameters() {
		return parameters;
	}

	public int getNumberOfParametersInSetClause() {
		return numberOfParametersInSetClause;
	}

	@Override
	protected void evaluateAssignment(AST eq) throws SemanticException {
		prepareLogicOperator( eq );
		Queryable persister = getCurrentFromClause().getFromElement().getQueryable();
		evaluateAssignment( eq, persister, -1 );
	}

	private void evaluateAssignment(AST eq, Queryable persister, int targetIndex) {
		if ( persister.isMultiTable() ) {
			// no need to even collect this information if the persister is considered multi-table
			AssignmentSpecification specification = new AssignmentSpecification( eq, persister );
			if ( targetIndex >= 0 ) {
				assignmentSpecifications.add( targetIndex, specification );
			}
			else {
				assignmentSpecifications.add( specification );
			}
			numberOfParametersInSetClause += specification.getParameters().length;
		}
	}

	public ArrayList getAssignmentSpecifications() {
		return assignmentSpecifications;
	}

	@Override
	protected AST createIntoClause(String path, AST propertySpec) throws SemanticException {
		Queryable persister = (Queryable) getSessionFactoryHelper().requireClassPersister( path );

		IntoClause intoClause = (IntoClause) getASTFactory().create( INTO, persister.getEntityName() );
		intoClause.setFirstChild( propertySpec );
		intoClause.initialize( persister );

		addQuerySpaces( persister.getQuerySpaces() );

		return intoClause;
	}

	@Override
	protected void prepareVersioned(AST updateNode, AST versioned) throws SemanticException {
		UpdateStatement updateStatement = (UpdateStatement) updateNode;
		FromClause fromClause = updateStatement.getFromClause();
		if ( versioned != null ) {
			// Make sure that the persister is versioned
			Queryable persister = fromClause.getFromElement().getQueryable();
			if ( !persister.isVersioned() ) {
				throw new SemanticException( "increment option specified for update of non-versioned entity" );
			}

			VersionType versionType = persister.getVersionType();
			if ( versionType instanceof UserVersionType ) {
				throw new SemanticException( "user-defined version types not supported for increment option" );
			}

			AST eq = getASTFactory().create( HqlSqlTokenTypes.EQ, "=" );
			AST versionPropertyNode = generateVersionPropertyNode( persister );

			eq.setFirstChild( versionPropertyNode );

			AST versionIncrementNode = null;
			if ( isTimestampBasedVersion( versionType ) ) {
				versionIncrementNode = getASTFactory().create( HqlSqlTokenTypes.PARAM, "?" );
				ParameterSpecification paramSpec = new VersionTypeSeedParameterSpecification( versionType );
				( (ParameterNode) versionIncrementNode ).setHqlParameterSpecification( paramSpec );
				parameters.add( 0, paramSpec );
			}
			else {
				// Not possible to simply re-use the versionPropertyNode here as it causes
				// OOM errors due to circularity :(
				versionIncrementNode = getASTFactory().create( HqlSqlTokenTypes.PLUS, "+" );
				versionIncrementNode.setFirstChild( generateVersionPropertyNode( persister ) );
				versionIncrementNode.addChild( getASTFactory().create( HqlSqlTokenTypes.IDENT, "1" ) );
			}

			eq.addChild( versionIncrementNode );

			evaluateAssignment( eq, persister, 0 );

			AST setClause = updateStatement.getSetClause();
			AST currentFirstSetElement = setClause.getFirstChild();
			setClause.setFirstChild( eq );
			eq.setNextSibling( currentFirstSetElement );
		}
	}

	private boolean isTimestampBasedVersion(VersionType versionType) {
		final Class javaType = versionType.getReturnedClass();
		return Date.class.isAssignableFrom( javaType )
				|| Calendar.class.isAssignableFrom( javaType );
	}

	private AST generateVersionPropertyNode(Queryable persister) throws SemanticException {
		String versionPropertyName = persister.getPropertyNames()[persister.getVersionProperty()];
		AST versionPropertyRef = getASTFactory().create( HqlSqlTokenTypes.IDENT, versionPropertyName );
		AST versionPropertyNode = lookupNonQualifiedProperty( versionPropertyRef );
		resolve( versionPropertyNode );
		return versionPropertyNode;
	}

	@Override
	protected void prepareLogicOperator(AST operator) throws SemanticException {
		( (OperatorNode) operator ).initialize();
	}

	@Override
	protected void prepareArithmeticOperator(AST operator) throws SemanticException {
		( (OperatorNode) operator ).initialize();
	}

	@Override
	protected void validateMapPropertyExpression(AST node) throws SemanticException {
		try {
			FromReferenceNode fromReferenceNode = (FromReferenceNode) node;
			QueryableCollection collectionPersister = fromReferenceNode.getFromElement().getQueryableCollection();
			if ( !Map.class.isAssignableFrom( collectionPersister.getCollectionType().getReturnedClass() ) ) {
				throw new SemanticException( "node did not reference a map" );
			}
		}
		catch (SemanticException se) {
			throw se;
		}
		catch (Throwable t) {
			throw new SemanticException( "node did not reference a map" );
		}
	}

	public Set<String> getTreatAsDeclarationsByPath(String path) {
		return hqlParser.getTreatMap().get( path );
	}

	public static void panic() {
		throw new QueryException( "TreeWalker: panic" );
	}
}

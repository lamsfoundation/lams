// $ANTLR 2.7.6 (2005-12-22): "sql-gen.g" -> "SqlGeneratorBase.java"$


package org.hibernate.hql.antlr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import antlr.TreeParser;
import antlr.Token;
import antlr.collections.AST;
import antlr.RecognitionException;
import antlr.ANTLRException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.collections.impl.BitSet;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;


/**
 * SQL Generator Tree Parser, providing SQL rendering of SQL ASTs produced by the previous phase, HqlSqlWalker.  All
 * syntax decoration such as extra spaces, lack of spaces, extra parens, etc. should be added by this class.
 * <br>
 * This grammar processes the HQL/SQL AST and produces an SQL string.  The intent is to move dialect-specific
 * code into a sub-class that will override some of the methods, just like the other two grammars in this system.
 * @author Joshua Davis (joshua@hibernate.org)
 */
public class SqlGeneratorBase extends antlr.TreeParser       implements SqlTokenTypes
 {

	private static Logger log = LoggerFactory.getLogger(SqlGeneratorBase.class);

   /** the buffer resulting SQL statement is written to */
	private StringBuffer buf = new StringBuffer();

	protected void out(String s) {
		buf.append(s);
	}

	/**
	 * Returns the last character written to the output, or -1 if there isn't one.
	 */
	protected int getLastChar() {
		int len = buf.length();
		if ( len == 0 )
			return -1;
		else
			return buf.charAt( len - 1 );
	}

	/**
	 * Add a aspace if the previous token was not a space or a parenthesis.
	 */
	protected void optionalSpace() {
		// Implemented in the sub-class.
	}

	protected void out(AST n) {
		out(n.getText());
	}

	protected void separator(AST n, String sep) {
		if (n.getNextSibling() != null)
			out(sep);
	}

	protected boolean hasText(AST a) {
		String t = a.getText();
		return t != null && t.length() > 0;
	}

	protected void fromFragmentSeparator(AST a) {
		// moved this impl into the subclass...
	}

	protected void nestedFromFragment(AST d,AST parent) {
		// moved this impl into the subclass...
	}

	protected StringBuffer getStringBuffer() {
		return buf;
	}

	protected void nyi(AST n) {
		throw new UnsupportedOperationException("Unsupported node: " + n);
	}

	protected void beginFunctionTemplate(AST m,AST i) {
		// if template is null we just write the function out as it appears in the hql statement
		out(i);
		out("(");
	}

	protected void endFunctionTemplate(AST m) {
	      out(")");
	}

	protected void commaBetweenParameters(String comma) {
		out(comma);
	}
public SqlGeneratorBase() {
	tokenNames = _tokenNames;
}

	public final void statement(AST _t) throws RecognitionException {
		
		AST statement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SELECT:
			{
				selectStatement(_t);
				_t = _retTree;
				break;
			}
			case UPDATE:
			{
				updateStatement(_t);
				_t = _retTree;
				break;
			}
			case DELETE:
			{
				deleteStatement(_t);
				_t = _retTree;
				break;
			}
			case INSERT:
			{
				insertStatement(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectStatement(AST _t) throws RecognitionException {
		
		AST selectStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t3 = _t;
			AST tmp1_AST_in = (AST)_t;
			match(_t,SELECT);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("select ");
			}
			selectClause(_t);
			_t = _retTree;
			from(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				AST __t5 = _t;
				AST tmp2_AST_in = (AST)_t;
				match(_t,WHERE);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" where ");
				}
				whereExpr(_t);
				_t = _retTree;
				_t = __t5;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			case GROUP:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case GROUP:
			{
				AST __t7 = _t;
				AST tmp3_AST_in = (AST)_t;
				match(_t,GROUP);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" group by ");
				}
				groupExprs(_t);
				_t = _retTree;
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case HAVING:
				{
					AST __t9 = _t;
					AST tmp4_AST_in = (AST)_t;
					match(_t,HAVING);
					_t = _t.getFirstChild();
					if ( inputState.guessing==0 ) {
						out(" having ");
					}
					booleanExpr(_t,false);
					_t = _retTree;
					_t = __t9;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t7;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			case ORDER:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ORDER:
			{
				AST __t11 = _t;
				AST tmp5_AST_in = (AST)_t;
				match(_t,ORDER);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" order by ");
				}
				orderExprs(_t);
				_t = _retTree;
				_t = __t11;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t3;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void updateStatement(AST _t) throws RecognitionException {
		
		AST updateStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t13 = _t;
			AST tmp6_AST_in = (AST)_t;
			match(_t,UPDATE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("update ");
			}
			AST __t14 = _t;
			AST tmp7_AST_in = (AST)_t;
			match(_t,FROM);
			_t = _t.getFirstChild();
			fromTable(_t);
			_t = _retTree;
			_t = __t14;
			_t = _t.getNextSibling();
			setClause(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				whereClause(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t13;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void deleteStatement(AST _t) throws RecognitionException {
		
		AST deleteStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t17 = _t;
			AST tmp8_AST_in = (AST)_t;
			match(_t,DELETE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("delete");
			}
			from(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case WHERE:
			{
				whereClause(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t17;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void insertStatement(AST _t) throws RecognitionException {
		
		AST insertStatement_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST i = null;
		
		try {      // for error handling
			AST __t20 = _t;
			AST tmp9_AST_in = (AST)_t;
			match(_t,INSERT);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out( "insert " );
			}
			i = (AST)_t;
			match(_t,INTO);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				out( i ); out( " " );
			}
			selectStatement(_t);
			_t = _retTree;
			_t = __t20;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectClause(AST _t) throws RecognitionException {
		
		AST selectClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t48 = _t;
			AST tmp10_AST_in = (AST)_t;
			match(_t,SELECT_CLAUSE);
			_t = _t.getFirstChild();
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALL:
			case DISTINCT:
			{
				distinctOrAll(_t);
				_t = _retTree;
				break;
			}
			case COUNT:
			case DOT:
			case FALSE:
			case SELECT:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CONSTRUCTOR:
			case CASE2:
			case METHOD_CALL:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case SELECT_EXPR:
			case SQL_NODE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			int _cnt51=0;
			_loop51:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_0.member(_t.getType()))) {
					selectColumn(_t);
					_t = _retTree;
				}
				else {
					if ( _cnt51>=1 ) { break _loop51; } else {throw new NoViableAltException(_t);}
				}
				
				_cnt51++;
			} while (true);
			}
			_t = __t48;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void from(AST _t) throws RecognitionException {
		
		AST from_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST f = null;
		
		try {      // for error handling
			AST __t66 = _t;
			f = _t==ASTNULL ? null :(AST)_t;
			match(_t,FROM);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(" from ");
			}
			{
			_loop68:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT)) {
					fromTable(_t);
					_t = _retTree;
				}
				else {
					break _loop68;
				}
				
			} while (true);
			}
			_t = __t66;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void whereExpr(AST _t) throws RecognitionException {
		
		AST whereExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FILTERS:
			{
				filters(_t);
				_t = _retTree;
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case THETA_JOINS:
				{
					if ( inputState.guessing==0 ) {
						out(" and ");
					}
					thetaJoins(_t);
					_t = _retTree;
					break;
				}
				case 3:
				case AND:
				case BETWEEN:
				case EXISTS:
				case IN:
				case LIKE:
				case NOT:
				case OR:
				case IS_NOT_NULL:
				case IS_NULL:
				case NOT_BETWEEN:
				case NOT_IN:
				case NOT_LIKE:
				case EQ:
				case NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case SQL_TOKEN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case AND:
				case BETWEEN:
				case EXISTS:
				case IN:
				case LIKE:
				case NOT:
				case OR:
				case IS_NOT_NULL:
				case IS_NULL:
				case NOT_BETWEEN:
				case NOT_IN:
				case NOT_LIKE:
				case EQ:
				case NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case SQL_TOKEN:
				{
					if ( inputState.guessing==0 ) {
						out(" and ");
					}
					booleanExpr(_t, true );
					_t = _retTree;
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				break;
			}
			case THETA_JOINS:
			{
				thetaJoins(_t);
				_t = _retTree;
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case AND:
				case BETWEEN:
				case EXISTS:
				case IN:
				case LIKE:
				case NOT:
				case OR:
				case IS_NOT_NULL:
				case IS_NULL:
				case NOT_BETWEEN:
				case NOT_IN:
				case NOT_LIKE:
				case EQ:
				case NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case SQL_TOKEN:
				{
					if ( inputState.guessing==0 ) {
						out(" and ");
					}
					booleanExpr(_t, true );
					_t = _retTree;
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				break;
			}
			case AND:
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case NOT:
			case OR:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case SQL_TOKEN:
			{
				booleanExpr(_t,false);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void groupExprs(AST _t) throws RecognitionException {
		
		AST groupExprs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			expr(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALL:
			case ANY:
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case SELECT:
			case SOME:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CASE2:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			{
				if ( inputState.guessing==0 ) {
					out(" , ");
				}
				groupExprs(_t);
				_t = _retTree;
				break;
			}
			case 3:
			case HAVING:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanExpr(AST _t,
		 boolean parens 
	) throws RecognitionException {
		
		AST booleanExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST st = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			case NOT:
			case OR:
			{
				booleanOp(_t, parens );
				_t = _retTree;
				break;
			}
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				comparisonExpr(_t, parens );
				_t = _retTree;
				break;
			}
			case SQL_TOKEN:
			{
				st = (AST)_t;
				match(_t,SQL_TOKEN);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(st);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void orderExprs(AST _t) throws RecognitionException {
		
		AST orderExprs_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST dir = null;
		
		try {      // for error handling
			{
			expr(_t);
			_t = _retTree;
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ASCENDING:
			case DESCENDING:
			{
				dir = _t==ASTNULL ? null : (AST)_t;
				orderDirection(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" "); out(dir);
				}
				break;
			}
			case 3:
			case ALL:
			case ANY:
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case SELECT:
			case SOME:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CASE2:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALL:
			case ANY:
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case SELECT:
			case SOME:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CASE2:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case VECTOR_EXPR:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			{
				if ( inputState.guessing==0 ) {
					out(", ");
				}
				orderExprs(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void fromTable(AST _t) throws RecognitionException {
		
		AST fromTable_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST a = null;
		AST b = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FROM_FRAGMENT:
			{
				AST __t70 = _t;
				a = _t==ASTNULL ? null :(AST)_t;
				match(_t,FROM_FRAGMENT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(a);
				}
				{
				_loop72:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT)) {
						tableJoin(_t, a );
						_t = _retTree;
					}
					else {
						break _loop72;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					fromFragmentSeparator(a);
				}
				_t = __t70;
				_t = _t.getNextSibling();
				break;
			}
			case JOIN_FRAGMENT:
			{
				AST __t73 = _t;
				b = _t==ASTNULL ? null :(AST)_t;
				match(_t,JOIN_FRAGMENT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(b);
				}
				{
				_loop75:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT)) {
						tableJoin(_t, b );
						_t = _retTree;
					}
					else {
						break _loop75;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					fromFragmentSeparator(b);
				}
				_t = __t73;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void setClause(AST _t) throws RecognitionException {
		
		AST setClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t22 = _t;
			AST tmp11_AST_in = (AST)_t;
			match(_t,SET);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(" set ");
			}
			comparisonExpr(_t,false);
			_t = _retTree;
			{
			_loop24:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_1.member(_t.getType()))) {
					if ( inputState.guessing==0 ) {
						out(", ");
					}
					comparisonExpr(_t,false);
					_t = _retTree;
				}
				else {
					break _loop24;
				}
				
			} while (true);
			}
			_t = __t22;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void whereClause(AST _t) throws RecognitionException {
		
		AST whereClause_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t26 = _t;
			AST tmp12_AST_in = (AST)_t;
			match(_t,WHERE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(" where ");
			}
			whereClauseExpr(_t);
			_t = _retTree;
			_t = __t26;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void comparisonExpr(AST _t,
		 boolean parens 
	) throws RecognitionException {
		
		AST comparisonExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case EQ:
			case NE:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				binaryComparisonExpression(_t);
				_t = _retTree;
				break;
			}
			case BETWEEN:
			case EXISTS:
			case IN:
			case LIKE:
			case IS_NOT_NULL:
			case IS_NULL:
			case NOT_BETWEEN:
			case NOT_IN:
			case NOT_LIKE:
			{
				if ( inputState.guessing==0 ) {
					if (parens) out("(");
				}
				exoticComparisonExpression(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					if (parens) out(")");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void whereClauseExpr(AST _t) throws RecognitionException {
		
		AST whereClauseExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			boolean synPredMatched29 = false;
			if (_t==null) _t=ASTNULL;
			if (((_t.getType()==SQL_TOKEN))) {
				AST __t29 = _t;
				synPredMatched29 = true;
				inputState.guessing++;
				try {
					{
					AST tmp13_AST_in = (AST)_t;
					match(_t,SQL_TOKEN);
					_t = _t.getNextSibling();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched29 = false;
				}
				_t = __t29;
inputState.guessing--;
			}
			if ( synPredMatched29 ) {
				conditionList(_t);
				_t = _retTree;
			}
			else if ((_tokenSet_2.member(_t.getType()))) {
				booleanExpr(_t, false );
				_t = _retTree;
			}
			else {
				throw new NoViableAltException(_t);
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void conditionList(AST _t) throws RecognitionException {
		
		AST conditionList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			sqlToken(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SQL_TOKEN:
			{
				if ( inputState.guessing==0 ) {
					out(" and ");
				}
				conditionList(_t);
				_t = _retTree;
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void expr(AST _t) throws RecognitionException {
		
		AST expr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST e = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CASE2:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			{
				simpleExpr(_t);
				_t = _retTree;
				break;
			}
			case VECTOR_EXPR:
			{
				AST __t116 = _t;
				AST tmp14_AST_in = (AST)_t;
				match(_t,VECTOR_EXPR);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("(");
				}
				{
				_loop118:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_tokenSet_3.member(_t.getType()))) {
						e = _t==ASTNULL ? null : (AST)_t;
						expr(_t);
						_t = _retTree;
						if ( inputState.guessing==0 ) {
							separator(e," , ");
						}
					}
					else {
						break _loop118;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					out(")");
				}
				_t = __t116;
				_t = _t.getNextSibling();
				break;
			}
			case SELECT:
			{
				parenSelect(_t);
				_t = _retTree;
				break;
			}
			case ANY:
			{
				AST __t119 = _t;
				AST tmp15_AST_in = (AST)_t;
				match(_t,ANY);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("any ");
				}
				quantified(_t);
				_t = _retTree;
				_t = __t119;
				_t = _t.getNextSibling();
				break;
			}
			case ALL:
			{
				AST __t120 = _t;
				AST tmp16_AST_in = (AST)_t;
				match(_t,ALL);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("all ");
				}
				quantified(_t);
				_t = _retTree;
				_t = __t120;
				_t = _t.getNextSibling();
				break;
			}
			case SOME:
			{
				AST __t121 = _t;
				AST tmp17_AST_in = (AST)_t;
				match(_t,SOME);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("some ");
				}
				quantified(_t);
				_t = _retTree;
				_t = __t121;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void orderDirection(AST _t) throws RecognitionException {
		
		AST orderDirection_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ASCENDING:
			{
				AST tmp18_AST_in = (AST)_t;
				match(_t,ASCENDING);
				_t = _t.getNextSibling();
				break;
			}
			case DESCENDING:
			{
				AST tmp19_AST_in = (AST)_t;
				match(_t,DESCENDING);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void filters(AST _t) throws RecognitionException {
		
		AST filters_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t42 = _t;
			AST tmp20_AST_in = (AST)_t;
			match(_t,FILTERS);
			_t = _t.getFirstChild();
			conditionList(_t);
			_t = _retTree;
			_t = __t42;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void thetaJoins(AST _t) throws RecognitionException {
		
		AST thetaJoins_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t44 = _t;
			AST tmp21_AST_in = (AST)_t;
			match(_t,THETA_JOINS);
			_t = _t.getFirstChild();
			conditionList(_t);
			_t = _retTree;
			_t = __t44;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void sqlToken(AST _t) throws RecognitionException {
		
		AST sqlToken_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST t = null;
		
		try {      // for error handling
			t = (AST)_t;
			match(_t,SQL_TOKEN);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				out(t);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void distinctOrAll(AST _t) throws RecognitionException {
		
		AST distinctOrAll_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DISTINCT:
			{
				AST tmp22_AST_in = (AST)_t;
				match(_t,DISTINCT);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("distinct ");
				}
				break;
			}
			case ALL:
			{
				AST tmp23_AST_in = (AST)_t;
				match(_t,ALL);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("all ");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectColumn(AST _t) throws RecognitionException {
		
		AST selectColumn_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST p = null;
		AST sc = null;
		
		try {      // for error handling
			p = _t==ASTNULL ? null : (AST)_t;
			selectExpr(_t);
			_t = _retTree;
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SELECT_COLUMNS:
			{
				sc = (AST)_t;
				match(_t,SELECT_COLUMNS);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(sc);
				}
				break;
			}
			case 3:
			case COUNT:
			case DOT:
			case FALSE:
			case SELECT:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CONSTRUCTOR:
			case CASE2:
			case METHOD_CALL:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case SELECT_EXPR:
			case SQL_NODE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				separator( (sc != null) ? sc : p,", ");
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectExpr(AST _t) throws RecognitionException {
		
		AST selectExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST e = null;
		AST c = null;
		AST sn = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case SELECT_EXPR:
			{
				e = _t==ASTNULL ? null : (AST)_t;
				selectAtom(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(e);
				}
				break;
			}
			case COUNT:
			{
				count(_t);
				_t = _retTree;
				break;
			}
			case CONSTRUCTOR:
			{
				AST __t55 = _t;
				AST tmp24_AST_in = (AST)_t;
				match(_t,CONSTRUCTOR);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case DOT:
				{
					AST tmp25_AST_in = (AST)_t;
					match(_t,DOT);
					_t = _t.getNextSibling();
					break;
				}
				case IDENT:
				{
					AST tmp26_AST_in = (AST)_t;
					match(_t,IDENT);
					_t = _t.getNextSibling();
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				{
				int _cnt58=0;
				_loop58:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_tokenSet_0.member(_t.getType()))) {
						selectColumn(_t);
						_t = _retTree;
					}
					else {
						if ( _cnt58>=1 ) { break _loop58; } else {throw new NoViableAltException(_t);}
					}
					
					_cnt58++;
				} while (true);
				}
				_t = __t55;
				_t = _t.getNextSibling();
				break;
			}
			case METHOD_CALL:
			{
				methodCall(_t);
				_t = _retTree;
				break;
			}
			case AGGREGATE:
			{
				aggregate(_t);
				_t = _retTree;
				break;
			}
			case FALSE:
			case TRUE:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			{
				c = _t==ASTNULL ? null : (AST)_t;
				constant(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(c);
				}
				break;
			}
			case CASE:
			case CASE2:
			case UNARY_MINUS:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			{
				arithmeticExpr(_t);
				_t = _retTree;
				break;
			}
			case PARAM:
			{
				AST tmp27_AST_in = (AST)_t;
				match(_t,PARAM);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("?");
				}
				break;
			}
			case SQL_NODE:
			{
				sn = (AST)_t;
				match(_t,SQL_NODE);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(sn);
				}
				break;
			}
			case SELECT:
			{
				if ( inputState.guessing==0 ) {
					out("(");
				}
				selectStatement(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(")");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void selectAtom(AST _t) throws RecognitionException {
		
		AST selectAtom_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			{
				AST tmp28_AST_in = (AST)_t;
				match(_t,DOT);
				_t = _t.getNextSibling();
				break;
			}
			case SQL_TOKEN:
			{
				AST tmp29_AST_in = (AST)_t;
				match(_t,SQL_TOKEN);
				_t = _t.getNextSibling();
				break;
			}
			case ALIAS_REF:
			{
				AST tmp30_AST_in = (AST)_t;
				match(_t,ALIAS_REF);
				_t = _t.getNextSibling();
				break;
			}
			case SELECT_EXPR:
			{
				AST tmp31_AST_in = (AST)_t;
				match(_t,SELECT_EXPR);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void count(AST _t) throws RecognitionException {
		
		AST count_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t60 = _t;
			AST tmp32_AST_in = (AST)_t;
			match(_t,COUNT);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out("count(");
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ALL:
			case DISTINCT:
			{
				distinctOrAll(_t);
				_t = _retTree;
				break;
			}
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CASE2:
			case INDEX_OP:
			case METHOD_CALL:
			case ROW_STAR:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			countExpr(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				out(")");
			}
			_t = __t60;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void methodCall(AST _t) throws RecognitionException {
		
		AST methodCall_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST m = null;
		AST i = null;
		
		try {      // for error handling
			AST __t157 = _t;
			m = _t==ASTNULL ? null :(AST)_t;
			match(_t,METHOD_CALL);
			_t = _t.getFirstChild();
			i = (AST)_t;
			match(_t,METHOD_NAME);
			_t = _t.getNextSibling();
			if ( inputState.guessing==0 ) {
				beginFunctionTemplate(m,i);
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case EXPR_LIST:
			{
				AST __t159 = _t;
				AST tmp33_AST_in = (AST)_t;
				match(_t,EXPR_LIST);
				_t = _t.getFirstChild();
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ALL:
				case ANY:
				case COUNT:
				case DOT:
				case FALSE:
				case NULL:
				case SELECT:
				case SOME:
				case TRUE:
				case CASE:
				case AGGREGATE:
				case CASE2:
				case INDEX_OP:
				case METHOD_CALL:
				case UNARY_MINUS:
				case VECTOR_EXPR:
				case CONSTANT:
				case NUM_DOUBLE:
				case NUM_FLOAT:
				case NUM_LONG:
				case JAVA_CONSTANT:
				case PLUS:
				case MINUS:
				case STAR:
				case DIV:
				case PARAM:
				case NUM_INT:
				case QUOTED_STRING:
				case IDENT:
				case ALIAS_REF:
				case SQL_TOKEN:
				case NAMED_PARAM:
				{
					arguments(_t);
					_t = _retTree;
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				_t = __t159;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				endFunctionTemplate(m);
			}
			_t = __t157;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void aggregate(AST _t) throws RecognitionException {
		
		AST aggregate_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST a = null;
		
		try {      // for error handling
			AST __t155 = _t;
			a = _t==ASTNULL ? null :(AST)_t;
			match(_t,AGGREGATE);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(a); out("(");
			}
			expr(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				out(")");
			}
			_t = __t155;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void constant(AST _t) throws RecognitionException {
		
		AST constant_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NUM_DOUBLE:
			{
				AST tmp34_AST_in = (AST)_t;
				match(_t,NUM_DOUBLE);
				_t = _t.getNextSibling();
				break;
			}
			case NUM_FLOAT:
			{
				AST tmp35_AST_in = (AST)_t;
				match(_t,NUM_FLOAT);
				_t = _t.getNextSibling();
				break;
			}
			case NUM_INT:
			{
				AST tmp36_AST_in = (AST)_t;
				match(_t,NUM_INT);
				_t = _t.getNextSibling();
				break;
			}
			case NUM_LONG:
			{
				AST tmp37_AST_in = (AST)_t;
				match(_t,NUM_LONG);
				_t = _t.getNextSibling();
				break;
			}
			case QUOTED_STRING:
			{
				AST tmp38_AST_in = (AST)_t;
				match(_t,QUOTED_STRING);
				_t = _t.getNextSibling();
				break;
			}
			case CONSTANT:
			{
				AST tmp39_AST_in = (AST)_t;
				match(_t,CONSTANT);
				_t = _t.getNextSibling();
				break;
			}
			case JAVA_CONSTANT:
			{
				AST tmp40_AST_in = (AST)_t;
				match(_t,JAVA_CONSTANT);
				_t = _t.getNextSibling();
				break;
			}
			case TRUE:
			{
				AST tmp41_AST_in = (AST)_t;
				match(_t,TRUE);
				_t = _t.getNextSibling();
				break;
			}
			case FALSE:
			{
				AST tmp42_AST_in = (AST)_t;
				match(_t,FALSE);
				_t = _t.getNextSibling();
				break;
			}
			case IDENT:
			{
				AST tmp43_AST_in = (AST)_t;
				match(_t,IDENT);
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arithmeticExpr(AST _t) throws RecognitionException {
		
		AST arithmeticExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PLUS:
			case MINUS:
			{
				additiveExpr(_t);
				_t = _retTree;
				break;
			}
			case STAR:
			case DIV:
			{
				multiplicativeExpr(_t);
				_t = _retTree;
				break;
			}
			case UNARY_MINUS:
			{
				AST __t128 = _t;
				AST tmp44_AST_in = (AST)_t;
				match(_t,UNARY_MINUS);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("-");
				}
				expr(_t);
				_t = _retTree;
				_t = __t128;
				_t = _t.getNextSibling();
				break;
			}
			case CASE:
			case CASE2:
			{
				caseExpr(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void countExpr(AST _t) throws RecognitionException {
		
		AST countExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ROW_STAR:
			{
				AST tmp45_AST_in = (AST)_t;
				match(_t,ROW_STAR);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("*");
				}
				break;
			}
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CASE2:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			{
				simpleExpr(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void simpleExpr(AST _t) throws RecognitionException {
		
		AST simpleExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST c = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case FALSE:
			case TRUE:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			{
				c = _t==ASTNULL ? null : (AST)_t;
				constant(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(c);
				}
				break;
			}
			case NULL:
			{
				AST tmp46_AST_in = (AST)_t;
				match(_t,NULL);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out("null");
				}
				break;
			}
			case DOT:
			case INDEX_OP:
			case ALIAS_REF:
			{
				addrExpr(_t);
				_t = _retTree;
				break;
			}
			case SQL_TOKEN:
			{
				sqlToken(_t);
				_t = _retTree;
				break;
			}
			case AGGREGATE:
			{
				aggregate(_t);
				_t = _retTree;
				break;
			}
			case METHOD_CALL:
			{
				methodCall(_t);
				_t = _retTree;
				break;
			}
			case COUNT:
			{
				count(_t);
				_t = _retTree;
				break;
			}
			case PARAM:
			case NAMED_PARAM:
			{
				parameter(_t);
				_t = _retTree;
				break;
			}
			case CASE:
			case CASE2:
			case UNARY_MINUS:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			{
				arithmeticExpr(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void tableJoin(AST _t,
		 AST parent 
	) throws RecognitionException {
		
		AST tableJoin_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST c = null;
		AST d = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case JOIN_FRAGMENT:
			{
				AST __t77 = _t;
				c = _t==ASTNULL ? null :(AST)_t;
				match(_t,JOIN_FRAGMENT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" "); out(c);
				}
				{
				_loop79:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT)) {
						tableJoin(_t, c );
						_t = _retTree;
					}
					else {
						break _loop79;
					}
					
				} while (true);
				}
				_t = __t77;
				_t = _t.getNextSibling();
				break;
			}
			case FROM_FRAGMENT:
			{
				AST __t80 = _t;
				d = _t==ASTNULL ? null :(AST)_t;
				match(_t,FROM_FRAGMENT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					nestedFromFragment(d,parent);
				}
				{
				_loop82:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==FROM_FRAGMENT||_t.getType()==JOIN_FRAGMENT)) {
						tableJoin(_t, d );
						_t = _retTree;
					}
					else {
						break _loop82;
					}
					
				} while (true);
				}
				_t = __t80;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void booleanOp(AST _t,
		 boolean parens 
	) throws RecognitionException {
		
		AST booleanOp_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case AND:
			{
				AST __t84 = _t;
				AST tmp47_AST_in = (AST)_t;
				match(_t,AND);
				_t = _t.getFirstChild();
				booleanExpr(_t,true);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" and ");
				}
				booleanExpr(_t,true);
				_t = _retTree;
				_t = __t84;
				_t = _t.getNextSibling();
				break;
			}
			case OR:
			{
				AST __t85 = _t;
				AST tmp48_AST_in = (AST)_t;
				match(_t,OR);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					if (parens) out("(");
				}
				booleanExpr(_t,false);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" or ");
				}
				booleanExpr(_t,false);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					if (parens) out(")");
				}
				_t = __t85;
				_t = _t.getNextSibling();
				break;
			}
			case NOT:
			{
				AST __t86 = _t;
				AST tmp49_AST_in = (AST)_t;
				match(_t,NOT);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" not (");
				}
				booleanExpr(_t,false);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(")");
				}
				_t = __t86;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void binaryComparisonExpression(AST _t) throws RecognitionException {
		
		AST binaryComparisonExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case EQ:
			{
				AST __t90 = _t;
				AST tmp50_AST_in = (AST)_t;
				match(_t,EQ);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("=");
				}
				expr(_t);
				_t = _retTree;
				_t = __t90;
				_t = _t.getNextSibling();
				break;
			}
			case NE:
			{
				AST __t91 = _t;
				AST tmp51_AST_in = (AST)_t;
				match(_t,NE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("<>");
				}
				expr(_t);
				_t = _retTree;
				_t = __t91;
				_t = _t.getNextSibling();
				break;
			}
			case GT:
			{
				AST __t92 = _t;
				AST tmp52_AST_in = (AST)_t;
				match(_t,GT);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(">");
				}
				expr(_t);
				_t = _retTree;
				_t = __t92;
				_t = _t.getNextSibling();
				break;
			}
			case GE:
			{
				AST __t93 = _t;
				AST tmp53_AST_in = (AST)_t;
				match(_t,GE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(">=");
				}
				expr(_t);
				_t = _retTree;
				_t = __t93;
				_t = _t.getNextSibling();
				break;
			}
			case LT:
			{
				AST __t94 = _t;
				AST tmp54_AST_in = (AST)_t;
				match(_t,LT);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("<");
				}
				expr(_t);
				_t = _retTree;
				_t = __t94;
				_t = _t.getNextSibling();
				break;
			}
			case LE:
			{
				AST __t95 = _t;
				AST tmp55_AST_in = (AST)_t;
				match(_t,LE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("<=");
				}
				expr(_t);
				_t = _retTree;
				_t = __t95;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void exoticComparisonExpression(AST _t) throws RecognitionException {
		
		AST exoticComparisonExpression_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case LIKE:
			{
				AST __t97 = _t;
				AST tmp56_AST_in = (AST)_t;
				match(_t,LIKE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" like ");
				}
				expr(_t);
				_t = _retTree;
				likeEscape(_t);
				_t = _retTree;
				_t = __t97;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_LIKE:
			{
				AST __t98 = _t;
				AST tmp57_AST_in = (AST)_t;
				match(_t,NOT_LIKE);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" not like ");
				}
				expr(_t);
				_t = _retTree;
				likeEscape(_t);
				_t = _retTree;
				_t = __t98;
				_t = _t.getNextSibling();
				break;
			}
			case BETWEEN:
			{
				AST __t99 = _t;
				AST tmp58_AST_in = (AST)_t;
				match(_t,BETWEEN);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" between ");
				}
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" and ");
				}
				expr(_t);
				_t = _retTree;
				_t = __t99;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_BETWEEN:
			{
				AST __t100 = _t;
				AST tmp59_AST_in = (AST)_t;
				match(_t,NOT_BETWEEN);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" not between ");
				}
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" and ");
				}
				expr(_t);
				_t = _retTree;
				_t = __t100;
				_t = _t.getNextSibling();
				break;
			}
			case IN:
			{
				AST __t101 = _t;
				AST tmp60_AST_in = (AST)_t;
				match(_t,IN);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" in");
				}
				inList(_t);
				_t = _retTree;
				_t = __t101;
				_t = _t.getNextSibling();
				break;
			}
			case NOT_IN:
			{
				AST __t102 = _t;
				AST tmp61_AST_in = (AST)_t;
				match(_t,NOT_IN);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(" not in ");
				}
				inList(_t);
				_t = _retTree;
				_t = __t102;
				_t = _t.getNextSibling();
				break;
			}
			case EXISTS:
			{
				AST __t103 = _t;
				AST tmp62_AST_in = (AST)_t;
				match(_t,EXISTS);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					optionalSpace(); out("exists ");
				}
				quantified(_t);
				_t = _retTree;
				_t = __t103;
				_t = _t.getNextSibling();
				break;
			}
			case IS_NULL:
			{
				AST __t104 = _t;
				AST tmp63_AST_in = (AST)_t;
				match(_t,IS_NULL);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				_t = __t104;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(" is null");
				}
				break;
			}
			case IS_NOT_NULL:
			{
				AST __t105 = _t;
				AST tmp64_AST_in = (AST)_t;
				match(_t,IS_NOT_NULL);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				_t = __t105;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(" is not null");
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void likeEscape(AST _t) throws RecognitionException {
		
		AST likeEscape_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case ESCAPE:
			{
				AST __t108 = _t;
				AST tmp65_AST_in = (AST)_t;
				match(_t,ESCAPE);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out(" escape ");
				}
				expr(_t);
				_t = _retTree;
				_t = __t108;
				_t = _t.getNextSibling();
				break;
			}
			case 3:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void inList(AST _t) throws RecognitionException {
		
		AST inList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			AST __t110 = _t;
			AST tmp66_AST_in = (AST)_t;
			match(_t,IN_LIST);
			_t = _t.getFirstChild();
			if ( inputState.guessing==0 ) {
				out(" ");
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SELECT:
			{
				parenSelect(_t);
				_t = _retTree;
				break;
			}
			case 3:
			case COUNT:
			case DOT:
			case FALSE:
			case NULL:
			case TRUE:
			case CASE:
			case AGGREGATE:
			case CASE2:
			case INDEX_OP:
			case METHOD_CALL:
			case UNARY_MINUS:
			case CONSTANT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case JAVA_CONSTANT:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			case ALIAS_REF:
			case SQL_TOKEN:
			case NAMED_PARAM:
			{
				simpleExprList(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			_t = __t110;
			_t = _t.getNextSibling();
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void quantified(AST _t) throws RecognitionException {
		
		AST quantified_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				out("(");
			}
			{
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case SQL_TOKEN:
			{
				sqlToken(_t);
				_t = _retTree;
				break;
			}
			case SELECT:
			{
				selectStatement(_t);
				_t = _retTree;
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
			}
			if ( inputState.guessing==0 ) {
				out(")");
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void parenSelect(AST _t) throws RecognitionException {
		
		AST parenSelect_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				out("(");
			}
			selectStatement(_t);
			_t = _retTree;
			if ( inputState.guessing==0 ) {
				out(")");
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void simpleExprList(AST _t) throws RecognitionException {
		
		AST simpleExprList_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST e = null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				out("(");
			}
			{
			_loop114:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_4.member(_t.getType()))) {
					e = _t==ASTNULL ? null : (AST)_t;
					simpleExpr(_t);
					_t = _retTree;
					if ( inputState.guessing==0 ) {
						separator(e," , ");
					}
				}
				else {
					break _loop114;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				out(")");
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void addrExpr(AST _t) throws RecognitionException {
		
		AST addrExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST r = null;
		AST i = null;
		AST j = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case DOT:
			{
				AST __t166 = _t;
				r = _t==ASTNULL ? null :(AST)_t;
				match(_t,DOT);
				_t = _t.getFirstChild();
				AST tmp67_AST_in = (AST)_t;
				if ( _t==null ) throw new MismatchedTokenException();
				_t = _t.getNextSibling();
				AST tmp68_AST_in = (AST)_t;
				if ( _t==null ) throw new MismatchedTokenException();
				_t = _t.getNextSibling();
				_t = __t166;
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(r);
				}
				break;
			}
			case ALIAS_REF:
			{
				i = (AST)_t;
				match(_t,ALIAS_REF);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(i);
				}
				break;
			}
			case INDEX_OP:
			{
				j = (AST)_t;
				match(_t,INDEX_OP);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(j);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void parameter(AST _t) throws RecognitionException {
		
		AST parameter_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		AST n = null;
		AST p = null;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case NAMED_PARAM:
			{
				n = (AST)_t;
				match(_t,NAMED_PARAM);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(n);
				}
				break;
			}
			case PARAM:
			{
				p = (AST)_t;
				match(_t,PARAM);
				_t = _t.getNextSibling();
				if ( inputState.guessing==0 ) {
					out(p);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void additiveExpr(AST _t) throws RecognitionException {
		
		AST additiveExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case PLUS:
			{
				AST __t130 = _t;
				AST tmp69_AST_in = (AST)_t;
				match(_t,PLUS);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("+");
				}
				expr(_t);
				_t = _retTree;
				_t = __t130;
				_t = _t.getNextSibling();
				break;
			}
			case MINUS:
			{
				AST __t131 = _t;
				AST tmp70_AST_in = (AST)_t;
				match(_t,MINUS);
				_t = _t.getFirstChild();
				expr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("-");
				}
				nestedExprAfterMinusDiv(_t);
				_t = _retTree;
				_t = __t131;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void multiplicativeExpr(AST _t) throws RecognitionException {
		
		AST multiplicativeExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case STAR:
			{
				AST __t133 = _t;
				AST tmp71_AST_in = (AST)_t;
				match(_t,STAR);
				_t = _t.getFirstChild();
				nestedExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("*");
				}
				nestedExpr(_t);
				_t = _retTree;
				_t = __t133;
				_t = _t.getNextSibling();
				break;
			}
			case DIV:
			{
				AST __t134 = _t;
				AST tmp72_AST_in = (AST)_t;
				match(_t,DIV);
				_t = _t.getFirstChild();
				nestedExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out("/");
				}
				nestedExprAfterMinusDiv(_t);
				_t = _retTree;
				_t = __t134;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void caseExpr(AST _t) throws RecognitionException {
		
		AST caseExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			if (_t==null) _t=ASTNULL;
			switch ( _t.getType()) {
			case CASE:
			{
				AST __t142 = _t;
				AST tmp73_AST_in = (AST)_t;
				match(_t,CASE);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("case");
				}
				{
				int _cnt145=0;
				_loop145:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==WHEN)) {
						AST __t144 = _t;
						AST tmp74_AST_in = (AST)_t;
						match(_t,WHEN);
						_t = _t.getFirstChild();
						if ( inputState.guessing==0 ) {
							out( " when ");
						}
						booleanExpr(_t,false);
						_t = _retTree;
						if ( inputState.guessing==0 ) {
							out(" then ");
						}
						expr(_t);
						_t = _retTree;
						_t = __t144;
						_t = _t.getNextSibling();
					}
					else {
						if ( _cnt145>=1 ) { break _loop145; } else {throw new NoViableAltException(_t);}
					}
					
					_cnt145++;
				} while (true);
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ELSE:
				{
					AST __t147 = _t;
					AST tmp75_AST_in = (AST)_t;
					match(_t,ELSE);
					_t = _t.getFirstChild();
					if ( inputState.guessing==0 ) {
						out(" else ");
					}
					expr(_t);
					_t = _retTree;
					_t = __t147;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				if ( inputState.guessing==0 ) {
					out(" end");
				}
				_t = __t142;
				_t = _t.getNextSibling();
				break;
			}
			case CASE2:
			{
				AST __t148 = _t;
				AST tmp76_AST_in = (AST)_t;
				match(_t,CASE2);
				_t = _t.getFirstChild();
				if ( inputState.guessing==0 ) {
					out("case ");
				}
				expr(_t);
				_t = _retTree;
				{
				int _cnt151=0;
				_loop151:
				do {
					if (_t==null) _t=ASTNULL;
					if ((_t.getType()==WHEN)) {
						AST __t150 = _t;
						AST tmp77_AST_in = (AST)_t;
						match(_t,WHEN);
						_t = _t.getFirstChild();
						if ( inputState.guessing==0 ) {
							out( " when ");
						}
						expr(_t);
						_t = _retTree;
						if ( inputState.guessing==0 ) {
							out(" then ");
						}
						expr(_t);
						_t = _retTree;
						_t = __t150;
						_t = _t.getNextSibling();
					}
					else {
						if ( _cnt151>=1 ) { break _loop151; } else {throw new NoViableAltException(_t);}
					}
					
					_cnt151++;
				} while (true);
				}
				{
				if (_t==null) _t=ASTNULL;
				switch ( _t.getType()) {
				case ELSE:
				{
					AST __t153 = _t;
					AST tmp78_AST_in = (AST)_t;
					match(_t,ELSE);
					_t = _t.getFirstChild();
					if ( inputState.guessing==0 ) {
						out(" else ");
					}
					expr(_t);
					_t = _retTree;
					_t = __t153;
					_t = _t.getNextSibling();
					break;
				}
				case 3:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(_t);
				}
				}
				}
				if ( inputState.guessing==0 ) {
					out(" end");
				}
				_t = __t148;
				_t = _t.getNextSibling();
				break;
			}
			default:
			{
				throw new NoViableAltException(_t);
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nestedExprAfterMinusDiv(AST _t) throws RecognitionException {
		
		AST nestedExprAfterMinusDiv_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			boolean synPredMatched140 = false;
			if (_t==null) _t=ASTNULL;
			if (((_tokenSet_5.member(_t.getType())))) {
				AST __t140 = _t;
				synPredMatched140 = true;
				inputState.guessing++;
				try {
					{
					arithmeticExpr(_t);
					_t = _retTree;
					}
				}
				catch (RecognitionException pe) {
					synPredMatched140 = false;
				}
				_t = __t140;
inputState.guessing--;
			}
			if ( synPredMatched140 ) {
				if ( inputState.guessing==0 ) {
					out("(");
				}
				arithmeticExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(")");
				}
			}
			else if ((_tokenSet_3.member(_t.getType()))) {
				expr(_t);
				_t = _retTree;
			}
			else {
				throw new NoViableAltException(_t);
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void nestedExpr(AST _t) throws RecognitionException {
		
		AST nestedExpr_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			boolean synPredMatched137 = false;
			if (_t==null) _t=ASTNULL;
			if (((_t.getType()==PLUS||_t.getType()==MINUS))) {
				AST __t137 = _t;
				synPredMatched137 = true;
				inputState.guessing++;
				try {
					{
					additiveExpr(_t);
					_t = _retTree;
					}
				}
				catch (RecognitionException pe) {
					synPredMatched137 = false;
				}
				_t = __t137;
inputState.guessing--;
			}
			if ( synPredMatched137 ) {
				if ( inputState.guessing==0 ) {
					out("(");
				}
				additiveExpr(_t);
				_t = _retTree;
				if ( inputState.guessing==0 ) {
					out(")");
				}
			}
			else if ((_tokenSet_3.member(_t.getType()))) {
				expr(_t);
				_t = _retTree;
			}
			else {
				throw new NoViableAltException(_t);
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	public final void arguments(AST _t) throws RecognitionException {
		
		AST arguments_AST_in = (_t == ASTNULL) ? null : (AST)_t;
		
		try {      // for error handling
			expr(_t);
			_t = _retTree;
			{
			_loop163:
			do {
				if (_t==null) _t=ASTNULL;
				if ((_tokenSet_3.member(_t.getType()))) {
					if ( inputState.guessing==0 ) {
						commaBetweenParameters(", ");
					}
					expr(_t);
					_t = _retTree;
				}
				else {
					break _loop163;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				if (_t!=null) {_t = _t.getNextSibling();}
			} else {
			  throw ex;
			}
		}
		_retTree = _t;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"all\"",
		"\"any\"",
		"\"and\"",
		"\"as\"",
		"\"asc\"",
		"\"avg\"",
		"\"between\"",
		"\"class\"",
		"\"count\"",
		"\"delete\"",
		"\"desc\"",
		"DOT",
		"\"distinct\"",
		"\"elements\"",
		"\"escape\"",
		"\"exists\"",
		"\"false\"",
		"\"fetch\"",
		"\"from\"",
		"\"full\"",
		"\"group\"",
		"\"having\"",
		"\"in\"",
		"\"indices\"",
		"\"inner\"",
		"\"insert\"",
		"\"into\"",
		"\"is\"",
		"\"join\"",
		"\"left\"",
		"\"like\"",
		"\"max\"",
		"\"min\"",
		"\"new\"",
		"\"not\"",
		"\"null\"",
		"\"or\"",
		"\"order\"",
		"\"outer\"",
		"\"properties\"",
		"\"right\"",
		"\"select\"",
		"\"set\"",
		"\"some\"",
		"\"sum\"",
		"\"true\"",
		"\"union\"",
		"\"update\"",
		"\"versioned\"",
		"\"where\"",
		"\"case\"",
		"\"end\"",
		"\"else\"",
		"\"then\"",
		"\"when\"",
		"\"on\"",
		"\"with\"",
		"\"both\"",
		"\"empty\"",
		"\"leading\"",
		"\"member\"",
		"\"object\"",
		"\"of\"",
		"\"trailing\"",
		"AGGREGATE",
		"ALIAS",
		"CONSTRUCTOR",
		"CASE2",
		"EXPR_LIST",
		"FILTER_ENTITY",
		"IN_LIST",
		"INDEX_OP",
		"IS_NOT_NULL",
		"IS_NULL",
		"METHOD_CALL",
		"NOT_BETWEEN",
		"NOT_IN",
		"NOT_LIKE",
		"ORDER_ELEMENT",
		"QUERY",
		"RANGE",
		"ROW_STAR",
		"SELECT_FROM",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"VECTOR_EXPR",
		"WEIRD_IDENT",
		"CONSTANT",
		"NUM_DOUBLE",
		"NUM_FLOAT",
		"NUM_LONG",
		"JAVA_CONSTANT",
		"COMMA",
		"EQ",
		"OPEN",
		"CLOSE",
		"\"by\"",
		"\"ascending\"",
		"\"descending\"",
		"NE",
		"SQL_NE",
		"LT",
		"GT",
		"LE",
		"GE",
		"CONCAT",
		"PLUS",
		"MINUS",
		"STAR",
		"DIV",
		"OPEN_BRACKET",
		"CLOSE_BRACKET",
		"COLON",
		"PARAM",
		"NUM_INT",
		"QUOTED_STRING",
		"IDENT",
		"ID_START_LETTER",
		"ID_LETTER",
		"ESCqs",
		"WS",
		"HEX_DIGIT",
		"EXPONENT",
		"FLOAT_SUFFIX",
		"FROM_FRAGMENT",
		"IMPLIED_FROM",
		"JOIN_FRAGMENT",
		"SELECT_CLAUSE",
		"LEFT_OUTER",
		"RIGHT_OUTER",
		"ALIAS_REF",
		"PROPERTY_REF",
		"SQL_TOKEN",
		"SELECT_COLUMNS",
		"SELECT_EXPR",
		"THETA_JOINS",
		"FILTERS",
		"METHOD_NAME",
		"NAMED_PARAM",
		"BOGUS",
		"SQL_NODE"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 18612532836077568L, 136163524152934608L, 66880L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 17247503360L, 33543694823424L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 1391637038144L, 33543694823424L, 256L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 18753820080246832L, 136163524186491024L, 16704L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 18577898219802624L, 136163524152936592L, 16704L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 18014398509481984L, 1055531171053696L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	}
	

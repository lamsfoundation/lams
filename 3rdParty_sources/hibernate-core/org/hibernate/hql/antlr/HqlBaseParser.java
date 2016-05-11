// $ANTLR 2.7.6 (2005-12-22): "hql.g" -> "HqlBaseParser.java"$



package org.hibernate.hql.antlr;

import org.hibernate.hql.ast.*;
import org.hibernate.hql.ast.util.*;


import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

/**
 * Hibernate Query Language Grammar
 * <br>
 * This grammar parses the query language for Hibernate (an Open Source, Object-Relational
 * mapping library).  A partial BNF grammar description is available for reference here:
 * http://www.hibernate.org/Documentation/HQLBNF
 *
 * Text from the original reference BNF is prefixed with '//##'.
 * @author Joshua Davis (pgmjsd@sourceforge.net)
 */
public class HqlBaseParser extends antlr.LLkParser       implements HqlTokenTypes
 {

    /** True if this is a filter query (allow no FROM clause). **/
	private boolean filter = false;

	/**
	 * Sets the filter flag.
	 * @param f True for a filter query, false for a normal query.
	 */
	public void setFilter(boolean f) {
		filter = f;
	}

	/**
	 * Returns true if this is a filter query, false if not.
	 * @return true if this is a filter query, false if not.
	 */
	public boolean isFilter() {
		return filter;
	}

	/**
	 * This method is overriden in the sub class in order to provide the
	 * 'keyword as identifier' hack.
	 * @param token The token to retry as an identifier.
	 * @param ex The exception to throw if it cannot be retried as an identifier.
	 */
	public AST handleIdentifierError(Token token,RecognitionException ex) throws RecognitionException, TokenStreamException {
		// Base implementation: Just re-throw the exception.
		throw ex;
	}

    /**
     * This method looks ahead and converts . <token> into . IDENT when
     * appropriate.
     */
    public void handleDotIdent() throws TokenStreamException {
    }

	/**
	 * Returns the negated equivalent of the expression.
	 * @param x The expression to negate.
	 */
	public AST negateNode(AST x) {
		// Just create a 'not' parent for the default behavior.
		return ASTUtil.createParent(astFactory, NOT, "not", x);
	}

	/**
	 * Returns the 'cleaned up' version of a comparison operator sub-tree.
	 * @param x The comparison operator to clean up.
	 */
	public AST processEqualityExpression(AST x) throws RecognitionException {
		return x;
	}

	public void weakKeywords() throws TokenStreamException { }

	public void processMemberOf(Token n,AST p,ASTPair currentAST) { }


protected HqlBaseParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public HqlBaseParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected HqlBaseParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public HqlBaseParser(TokenStream lexer) {
  this(lexer,3);
}

public HqlBaseParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void statement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST statement_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case UPDATE:
			{
				updateStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case DELETE:
			{
				deleteStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FROM:
			case GROUP:
			case ORDER:
			case SELECT:
			case WHERE:
			{
				selectStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case INSERT:
			{
				insertStatement();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			statement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = statement_AST;
	}
	
	public final void updateStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST updateStatement_AST = null;
		
		try {      // for error handling
			AST tmp1_AST = null;
			tmp1_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp1_AST);
			match(UPDATE);
			{
			switch ( LA(1)) {
			case VERSIONED:
			{
				AST tmp2_AST = null;
				tmp2_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp2_AST);
				match(VERSIONED);
				break;
			}
			case FROM:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			optionalFromTokenFromClause();
			astFactory.addASTChild(currentAST, returnAST);
			setClause();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case WHERE:
			{
				whereClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			updateStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = updateStatement_AST;
	}
	
	public final void deleteStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST deleteStatement_AST = null;
		
		try {      // for error handling
			AST tmp3_AST = null;
			tmp3_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp3_AST);
			match(DELETE);
			{
			optionalFromTokenFromClause();
			astFactory.addASTChild(currentAST, returnAST);
			}
			{
			switch ( LA(1)) {
			case WHERE:
			{
				whereClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			deleteStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = deleteStatement_AST;
	}
	
	public final void selectStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectStatement_AST = null;
		
		try {      // for error handling
			queryRule();
			astFactory.addASTChild(currentAST, returnAST);
			selectStatement_AST = (AST)currentAST.root;
			
					selectStatement_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(QUERY,"query")).add(selectStatement_AST));
				
			currentAST.root = selectStatement_AST;
			currentAST.child = selectStatement_AST!=null &&selectStatement_AST.getFirstChild()!=null ?
				selectStatement_AST.getFirstChild() : selectStatement_AST;
			currentAST.advanceChildToEnd();
			selectStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = selectStatement_AST;
	}
	
	public final void insertStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST insertStatement_AST = null;
		
		try {      // for error handling
			AST tmp4_AST = null;
			tmp4_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp4_AST);
			match(INSERT);
			intoClause();
			astFactory.addASTChild(currentAST, returnAST);
			selectStatement();
			astFactory.addASTChild(currentAST, returnAST);
			insertStatement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		returnAST = insertStatement_AST;
	}
	
	public final void optionalFromTokenFromClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST optionalFromTokenFromClause_AST = null;
		AST f_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case FROM:
			{
				match(FROM);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			path();
			f_AST = (AST)returnAST;
			{
			switch ( LA(1)) {
			case AS:
			case IDENT:
			{
				asAlias();
				a_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case SET:
			case WHERE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			optionalFromTokenFromClause_AST = (AST)currentAST.root;
			
					AST range = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(RANGE,"RANGE")).add(f_AST).add(a_AST));
					optionalFromTokenFromClause_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FROM,"FROM")).add(range));
				
			currentAST.root = optionalFromTokenFromClause_AST;
			currentAST.child = optionalFromTokenFromClause_AST!=null &&optionalFromTokenFromClause_AST.getFirstChild()!=null ?
				optionalFromTokenFromClause_AST.getFirstChild() : optionalFromTokenFromClause_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		returnAST = optionalFromTokenFromClause_AST;
	}
	
	public final void setClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST setClause_AST = null;
		
		try {      // for error handling
			{
			AST tmp6_AST = null;
			tmp6_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp6_AST);
			match(SET);
			assignment();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop9:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					assignment();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop9;
				}
				
			} while (true);
			}
			}
			setClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		returnAST = setClause_AST;
	}
	
	public final void whereClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST whereClause_AST = null;
		
		try {      // for error handling
			AST tmp8_AST = null;
			tmp8_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp8_AST);
			match(WHERE);
			logicalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			whereClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		returnAST = whereClause_AST;
	}
	
	public final void assignment() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignment_AST = null;
		
		try {      // for error handling
			stateField();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp9_AST = null;
			tmp9_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp9_AST);
			match(EQ);
			newValue();
			astFactory.addASTChild(currentAST, returnAST);
			assignment_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		returnAST = assignment_AST;
	}
	
	public final void stateField() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST stateField_AST = null;
		
		try {      // for error handling
			path();
			astFactory.addASTChild(currentAST, returnAST);
			stateField_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		returnAST = stateField_AST;
	}
	
	public final void newValue() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newValue_AST = null;
		
		try {      // for error handling
			concatenation();
			astFactory.addASTChild(currentAST, returnAST);
			newValue_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		returnAST = newValue_AST;
	}
	
	public final void path() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST path_AST = null;
		
		try {      // for error handling
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop190:
			do {
				if ((LA(1)==DOT)) {
					AST tmp10_AST = null;
					tmp10_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp10_AST);
					match(DOT);
					weakKeywords();
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop190;
				}
				
			} while (true);
			}
			path_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		returnAST = path_AST;
	}
	
	public final void concatenation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST concatenation_AST = null;
		Token  c = null;
		AST c_AST = null;
		
		try {      // for error handling
			additiveExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case CONCAT:
			{
				c = LT(1);
				c_AST = astFactory.create(c);
				astFactory.makeASTRoot(currentAST, c_AST);
				match(CONCAT);
				c_AST.setType(EXPR_LIST); c_AST.setText("concatList");
				additiveExpression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop118:
				do {
					if ((LA(1)==CONCAT)) {
						match(CONCAT);
						additiveExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop118;
					}
					
				} while (true);
				}
				concatenation_AST = (AST)currentAST.root;
				concatenation_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(METHOD_CALL,"||")).add((AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(IDENT,"concat")))).add(c_AST));
				currentAST.root = concatenation_AST;
				currentAST.child = concatenation_AST!=null &&concatenation_AST.getFirstChild()!=null ?
					concatenation_AST.getFirstChild() : concatenation_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case EOF:
			case AND:
			case AS:
			case ASCENDING:
			case BETWEEN:
			case DESCENDING:
			case ESCAPE:
			case FROM:
			case FULL:
			case GROUP:
			case HAVING:
			case IN:
			case INNER:
			case IS:
			case JOIN:
			case LEFT:
			case LIKE:
			case NOT:
			case OR:
			case ORDER:
			case RIGHT:
			case UNION:
			case WHERE:
			case THEN:
			case MEMBER:
			case COMMA:
			case EQ:
			case CLOSE:
			case LITERAL_ascending:
			case LITERAL_descending:
			case NE:
			case SQL_NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case CLOSE_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			concatenation_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		returnAST = concatenation_AST;
	}
	
	public final void asAlias() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST asAlias_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case AS:
			{
				match(AS);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			alias();
			astFactory.addASTChild(currentAST, returnAST);
			asAlias_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_8);
		}
		returnAST = asAlias_AST;
	}
	
	public final void queryRule() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST queryRule_AST = null;
		
		try {      // for error handling
			selectFrom();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case WHERE:
			{
				whereClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case GROUP:
			case ORDER:
			case UNION:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case GROUP:
			{
				groupByClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case ORDER:
			case UNION:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ORDER:
			{
				orderByClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case UNION:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			queryRule_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
		returnAST = queryRule_AST;
	}
	
	public final void intoClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST intoClause_AST = null;
		
		try {      // for error handling
			AST tmp13_AST = null;
			tmp13_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp13_AST);
			match(INTO);
			path();
			astFactory.addASTChild(currentAST, returnAST);
			weakKeywords();
			insertablePropertySpec();
			astFactory.addASTChild(currentAST, returnAST);
			intoClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		returnAST = intoClause_AST;
	}
	
	public final void insertablePropertySpec() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST insertablePropertySpec_AST = null;
		
		try {      // for error handling
			match(OPEN);
			primaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop24:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					primaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop24;
				}
				
			} while (true);
			}
			match(CLOSE);
			insertablePropertySpec_AST = (AST)currentAST.root;
			
					// Just need *something* to distinguish this on the hql-sql.g side
					insertablePropertySpec_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(RANGE,"column-spec")).add(insertablePropertySpec_AST));
				
			currentAST.root = insertablePropertySpec_AST;
			currentAST.child = insertablePropertySpec_AST!=null &&insertablePropertySpec_AST.getFirstChild()!=null ?
				insertablePropertySpec_AST.getFirstChild() : insertablePropertySpec_AST;
			currentAST.advanceChildToEnd();
			insertablePropertySpec_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_10);
		}
		returnAST = insertablePropertySpec_AST;
	}
	
	public final void primaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST primaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case AVG:
			case COUNT:
			case ELEMENTS:
			case INDICES:
			case MAX:
			case MIN:
			case SUM:
			case IDENT:
			{
				identPrimary();
				astFactory.addASTChild(currentAST, returnAST);
				{
				if ((LA(1)==DOT) && (LA(2)==CLASS)) {
					AST tmp17_AST = null;
					tmp17_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp17_AST);
					match(DOT);
					AST tmp18_AST = null;
					tmp18_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp18_AST);
					match(CLASS);
				}
				else if ((_tokenSet_11.member(LA(1))) && (_tokenSet_12.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case FALSE:
			case NULL:
			case TRUE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_INT:
			case QUOTED_STRING:
			{
				constant();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case COLON:
			{
				AST tmp19_AST = null;
				tmp19_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp19_AST);
				match(COLON);
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case OPEN:
			{
				match(OPEN);
				{
				switch ( LA(1)) {
				case ALL:
				case ANY:
				case AVG:
				case COUNT:
				case ELEMENTS:
				case EXISTS:
				case FALSE:
				case INDICES:
				case MAX:
				case MIN:
				case NOT:
				case NULL:
				case SOME:
				case SUM:
				case TRUE:
				case CASE:
				case EMPTY:
				case NUM_DOUBLE:
				case NUM_FLOAT:
				case NUM_LONG:
				case OPEN:
				case PLUS:
				case MINUS:
				case COLON:
				case PARAM:
				case NUM_INT:
				case QUOTED_STRING:
				case IDENT:
				{
					expressionOrVector();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case FROM:
				case GROUP:
				case ORDER:
				case SELECT:
				case UNION:
				case WHERE:
				case CLOSE:
				{
					subQuery();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(CLOSE);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case PARAM:
			{
				AST tmp22_AST = null;
				tmp22_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp22_AST);
				match(PARAM);
				{
				switch ( LA(1)) {
				case NUM_INT:
				{
					AST tmp23_AST = null;
					tmp23_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp23_AST);
					match(NUM_INT);
					break;
				}
				case EOF:
				case AND:
				case AS:
				case ASCENDING:
				case BETWEEN:
				case DESCENDING:
				case DOT:
				case ESCAPE:
				case FROM:
				case FULL:
				case GROUP:
				case HAVING:
				case IN:
				case INNER:
				case IS:
				case JOIN:
				case LEFT:
				case LIKE:
				case NOT:
				case OR:
				case ORDER:
				case RIGHT:
				case UNION:
				case WHERE:
				case END:
				case ELSE:
				case THEN:
				case WHEN:
				case MEMBER:
				case COMMA:
				case EQ:
				case CLOSE:
				case LITERAL_ascending:
				case LITERAL_descending:
				case NE:
				case SQL_NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case CONCAT:
				case PLUS:
				case MINUS:
				case STAR:
				case DIV:
				case OPEN_BRACKET:
				case CLOSE_BRACKET:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		returnAST = primaryExpression_AST;
	}
	
	public final void union() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST union_AST = null;
		
		try {      // for error handling
			queryRule();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop27:
			do {
				if ((LA(1)==UNION)) {
					AST tmp24_AST = null;
					tmp24_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp24_AST);
					match(UNION);
					queryRule();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop27;
				}
				
			} while (true);
			}
			union_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		returnAST = union_AST;
	}
	
	public final void selectFrom() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectFrom_AST = null;
		AST s_AST = null;
		AST f_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SELECT:
			{
				selectClause();
				s_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case FROM:
			case GROUP:
			case ORDER:
			case UNION:
			case WHERE:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case FROM:
			{
				fromClause();
				f_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case GROUP:
			case ORDER:
			case UNION:
			case WHERE:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			selectFrom_AST = (AST)currentAST.root;
			
					// If there was no FROM clause and this is a filter query, create a from clause.  Otherwise, throw
					// an exception because non-filter queries must have a FROM clause.
					if (f_AST == null) {
						if (filter) {
							f_AST = (AST)astFactory.make( (new ASTArray(1)).add(astFactory.create(FROM,"{filter-implied FROM}")));
						}
						else
							throw new SemanticException("FROM expected (non-filter queries must contain a FROM clause)");
					}
						
					// Create an artificial token so the 'FROM' can be placed
					// before the SELECT in the tree to make tree processing
					// simpler.
					selectFrom_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(SELECT_FROM,"SELECT_FROM")).add(f_AST).add(s_AST));
				
			currentAST.root = selectFrom_AST;
			currentAST.child = selectFrom_AST!=null &&selectFrom_AST.getFirstChild()!=null ?
				selectFrom_AST.getFirstChild() : selectFrom_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_14);
		}
		returnAST = selectFrom_AST;
	}
	
	public final void groupByClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST groupByClause_AST = null;
		
		try {      // for error handling
			AST tmp25_AST = null;
			tmp25_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp25_AST);
			match(GROUP);
			match(LITERAL_by);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop67:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop67;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case HAVING:
			{
				havingClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case ORDER:
			case UNION:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			groupByClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = groupByClause_AST;
	}
	
	public final void orderByClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orderByClause_AST = null;
		
		try {      // for error handling
			AST tmp28_AST = null;
			tmp28_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp28_AST);
			match(ORDER);
			match(LITERAL_by);
			orderElement();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop71:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					orderElement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop71;
				}
				
			} while (true);
			}
			orderByClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
		returnAST = orderByClause_AST;
	}
	
	public final void selectClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectClause_AST = null;
		
		try {      // for error handling
			AST tmp31_AST = null;
			tmp31_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp31_AST);
			match(SELECT);
			weakKeywords();
			{
			switch ( LA(1)) {
			case DISTINCT:
			{
				AST tmp32_AST = null;
				tmp32_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp32_AST);
				match(DISTINCT);
				break;
			}
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NEW:
			case NOT:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case OBJECT:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case OPEN:
			case PLUS:
			case MINUS:
			case COLON:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NOT:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case OPEN:
			case PLUS:
			case MINUS:
			case COLON:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			{
				selectedPropertiesList();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case NEW:
			{
				newExpression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case OBJECT:
			{
				selectObject();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			selectClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_16);
		}
		returnAST = selectClause_AST;
	}
	
	public final void fromClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromClause_AST = null;
		
		try {      // for error handling
			AST tmp33_AST = null;
			tmp33_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp33_AST);
			match(FROM);
			weakKeywords();
			fromRange();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop43:
			do {
				switch ( LA(1)) {
				case FULL:
				case INNER:
				case JOIN:
				case LEFT:
				case RIGHT:
				{
					fromJoin();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case COMMA:
				{
					match(COMMA);
					weakKeywords();
					fromRange();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					break _loop43;
				}
				}
			} while (true);
			}
			fromClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_14);
		}
		returnAST = fromClause_AST;
	}
	
	public final void selectedPropertiesList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectedPropertiesList_AST = null;
		
		try {      // for error handling
			aliasedExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop81:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					aliasedExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop81;
				}
				
			} while (true);
			}
			selectedPropertiesList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_16);
		}
		returnAST = selectedPropertiesList_AST;
	}
	
	public final void newExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST newExpression_AST = null;
		Token  op = null;
		AST op_AST = null;
		
		try {      // for error handling
			{
			match(NEW);
			path();
			astFactory.addASTChild(currentAST, returnAST);
			}
			op = LT(1);
			op_AST = astFactory.create(op);
			astFactory.makeASTRoot(currentAST, op_AST);
			match(OPEN);
			op_AST.setType(CONSTRUCTOR);
			selectedPropertiesList();
			astFactory.addASTChild(currentAST, returnAST);
			match(CLOSE);
			newExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_16);
		}
		returnAST = newExpression_AST;
	}
	
	public final void selectObject() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST selectObject_AST = null;
		
		try {      // for error handling
			AST tmp38_AST = null;
			tmp38_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp38_AST);
			match(OBJECT);
			match(OPEN);
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			match(CLOSE);
			selectObject_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_16);
		}
		returnAST = selectObject_AST;
	}
	
	public final void identifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identifier_AST = null;
		
		try {      // for error handling
			AST tmp41_AST = null;
			tmp41_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp41_AST);
			match(IDENT);
			identifier_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			
					identifier_AST = handleIdentifierError(LT(1),ex);
				
		}
		returnAST = identifier_AST;
	}
	
	public final void fromRange() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromRange_AST = null;
		
		try {      // for error handling
			if ((LA(1)==IDENT) && (_tokenSet_17.member(LA(2)))) {
				fromClassOrOuterQueryPath();
				astFactory.addASTChild(currentAST, returnAST);
				fromRange_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IDENT) && (LA(2)==IN) && (LA(3)==CLASS)) {
				inClassDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				fromRange_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IN)) {
				inCollectionDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				fromRange_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==IDENT) && (LA(2)==IN) && (LA(3)==ELEMENTS)) {
				inCollectionElementsDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				fromRange_AST = (AST)currentAST.root;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = fromRange_AST;
	}
	
	public final void fromJoin() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromJoin_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LEFT:
			case RIGHT:
			{
				{
				{
				switch ( LA(1)) {
				case LEFT:
				{
					AST tmp42_AST = null;
					tmp42_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp42_AST);
					match(LEFT);
					break;
				}
				case RIGHT:
				{
					AST tmp43_AST = null;
					tmp43_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp43_AST);
					match(RIGHT);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case OUTER:
				{
					AST tmp44_AST = null;
					tmp44_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp44_AST);
					match(OUTER);
					break;
				}
				case JOIN:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				}
				break;
			}
			case FULL:
			{
				AST tmp45_AST = null;
				tmp45_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp45_AST);
				match(FULL);
				break;
			}
			case INNER:
			{
				AST tmp46_AST = null;
				tmp46_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp46_AST);
				match(INNER);
				break;
			}
			case JOIN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			AST tmp47_AST = null;
			tmp47_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp47_AST);
			match(JOIN);
			{
			switch ( LA(1)) {
			case FETCH:
			{
				AST tmp48_AST = null;
				tmp48_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp48_AST);
				match(FETCH);
				break;
			}
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			path();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case AS:
			case IDENT:
			{
				asAlias();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FETCH:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case UNION:
			case WHERE:
			case WITH:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case FETCH:
			{
				propertyFetch();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case UNION:
			case WHERE:
			case WITH:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case WITH:
			{
				withClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case UNION:
			case WHERE:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			fromJoin_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = fromJoin_AST;
	}
	
	public final void propertyFetch() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST propertyFetch_AST = null;
		
		try {      // for error handling
			AST tmp49_AST = null;
			tmp49_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp49_AST);
			match(FETCH);
			match(ALL);
			match(PROPERTIES);
			propertyFetch_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_19);
		}
		returnAST = propertyFetch_AST;
	}
	
	public final void withClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST withClause_AST = null;
		
		try {      // for error handling
			AST tmp52_AST = null;
			tmp52_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp52_AST);
			match(WITH);
			logicalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			withClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = withClause_AST;
	}
	
	public final void logicalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalExpression_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			logicalExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_20);
		}
		returnAST = logicalExpression_AST;
	}
	
	public final void fromClassOrOuterQueryPath() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST fromClassOrOuterQueryPath_AST = null;
		AST c_AST = null;
		AST a_AST = null;
		AST p_AST = null;
		
		try {      // for error handling
			path();
			c_AST = (AST)returnAST;
			weakKeywords();
			{
			switch ( LA(1)) {
			case AS:
			case IDENT:
			{
				asAlias();
				a_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case FETCH:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case UNION:
			case WHERE:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case FETCH:
			{
				propertyFetch();
				p_AST = (AST)returnAST;
				break;
			}
			case EOF:
			case FULL:
			case GROUP:
			case INNER:
			case JOIN:
			case LEFT:
			case ORDER:
			case RIGHT:
			case UNION:
			case WHERE:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			fromClassOrOuterQueryPath_AST = (AST)currentAST.root;
			
					fromClassOrOuterQueryPath_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(RANGE,"RANGE")).add(c_AST).add(a_AST).add(p_AST));
				
			currentAST.root = fromClassOrOuterQueryPath_AST;
			currentAST.child = fromClassOrOuterQueryPath_AST!=null &&fromClassOrOuterQueryPath_AST.getFirstChild()!=null ?
				fromClassOrOuterQueryPath_AST.getFirstChild() : fromClassOrOuterQueryPath_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = fromClassOrOuterQueryPath_AST;
	}
	
	public final void inClassDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inClassDeclaration_AST = null;
		AST a_AST = null;
		AST c_AST = null;
		
		try {      // for error handling
			alias();
			a_AST = (AST)returnAST;
			match(IN);
			match(CLASS);
			path();
			c_AST = (AST)returnAST;
			inClassDeclaration_AST = (AST)currentAST.root;
			
					inClassDeclaration_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(RANGE,"RANGE")).add(c_AST).add(a_AST));
				
			currentAST.root = inClassDeclaration_AST;
			currentAST.child = inClassDeclaration_AST!=null &&inClassDeclaration_AST.getFirstChild()!=null ?
				inClassDeclaration_AST.getFirstChild() : inClassDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = inClassDeclaration_AST;
	}
	
	public final void inCollectionDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inCollectionDeclaration_AST = null;
		AST p_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			match(IN);
			match(OPEN);
			path();
			p_AST = (AST)returnAST;
			match(CLOSE);
			alias();
			a_AST = (AST)returnAST;
			inCollectionDeclaration_AST = (AST)currentAST.root;
			
			inCollectionDeclaration_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(JOIN,"join")).add(astFactory.create(INNER,"inner")).add(p_AST).add(a_AST));
				
			currentAST.root = inCollectionDeclaration_AST;
			currentAST.child = inCollectionDeclaration_AST!=null &&inCollectionDeclaration_AST.getFirstChild()!=null ?
				inCollectionDeclaration_AST.getFirstChild() : inCollectionDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = inCollectionDeclaration_AST;
	}
	
	public final void inCollectionElementsDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inCollectionElementsDeclaration_AST = null;
		AST a_AST = null;
		AST p_AST = null;
		
		try {      // for error handling
			alias();
			a_AST = (AST)returnAST;
			match(IN);
			match(ELEMENTS);
			match(OPEN);
			path();
			p_AST = (AST)returnAST;
			match(CLOSE);
			inCollectionElementsDeclaration_AST = (AST)currentAST.root;
			
			inCollectionElementsDeclaration_AST = (AST)astFactory.make( (new ASTArray(4)).add(astFactory.create(JOIN,"join")).add(astFactory.create(INNER,"inner")).add(p_AST).add(a_AST));
				
			currentAST.root = inCollectionElementsDeclaration_AST;
			currentAST.child = inCollectionElementsDeclaration_AST!=null &&inCollectionElementsDeclaration_AST.getFirstChild()!=null ?
				inCollectionElementsDeclaration_AST.getFirstChild() : inCollectionElementsDeclaration_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_18);
		}
		returnAST = inCollectionElementsDeclaration_AST;
	}
	
	public final void alias() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST alias_AST = null;
		AST a_AST = null;
		
		try {      // for error handling
			identifier();
			a_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			a_AST.setType(ALIAS);
			alias_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		returnAST = alias_AST;
	}
	
	public final void expression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expression_AST = null;
		
		try {      // for error handling
			logicalOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			expression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_22);
		}
		returnAST = expression_AST;
	}
	
	public final void havingClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST havingClause_AST = null;
		
		try {      // for error handling
			AST tmp62_AST = null;
			tmp62_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp62_AST);
			match(HAVING);
			logicalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			havingClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		returnAST = havingClause_AST;
	}
	
	public final void orderElement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST orderElement_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case ASCENDING:
			case DESCENDING:
			case LITERAL_ascending:
			case LITERAL_descending:
			{
				ascendingOrDescending();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case UNION:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			orderElement_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_23);
		}
		returnAST = orderElement_AST;
	}
	
	public final void ascendingOrDescending() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST ascendingOrDescending_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ASCENDING:
			case LITERAL_ascending:
			{
				{
				switch ( LA(1)) {
				case ASCENDING:
				{
					AST tmp63_AST = null;
					tmp63_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp63_AST);
					match(ASCENDING);
					break;
				}
				case LITERAL_ascending:
				{
					AST tmp64_AST = null;
					tmp64_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp64_AST);
					match(LITERAL_ascending);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				ascendingOrDescending_AST = (AST)currentAST.root;
				ascendingOrDescending_AST.setType(ASCENDING);
				ascendingOrDescending_AST = (AST)currentAST.root;
				break;
			}
			case DESCENDING:
			case LITERAL_descending:
			{
				{
				switch ( LA(1)) {
				case DESCENDING:
				{
					AST tmp65_AST = null;
					tmp65_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp65_AST);
					match(DESCENDING);
					break;
				}
				case LITERAL_descending:
				{
					AST tmp66_AST = null;
					tmp66_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp66_AST);
					match(LITERAL_descending);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				ascendingOrDescending_AST = (AST)currentAST.root;
				ascendingOrDescending_AST.setType(DESCENDING);
				ascendingOrDescending_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_23);
		}
		returnAST = ascendingOrDescending_AST;
	}
	
	public final void aliasedExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aliasedExpression_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case AS:
			{
				AST tmp67_AST = null;
				tmp67_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp67_AST);
				match(AS);
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FROM:
			case GROUP:
			case ORDER:
			case UNION:
			case WHERE:
			case COMMA:
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			aliasedExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_24);
		}
		returnAST = aliasedExpression_AST;
	}
	
	public final void logicalOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalOrExpression_AST = null;
		
		try {      // for error handling
			logicalAndExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop88:
			do {
				if ((LA(1)==OR)) {
					AST tmp68_AST = null;
					tmp68_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp68_AST);
					match(OR);
					logicalAndExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop88;
				}
				
			} while (true);
			}
			logicalOrExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_22);
		}
		returnAST = logicalOrExpression_AST;
	}
	
	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalAndExpression_AST = null;
		
		try {      // for error handling
			negatedExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop91:
			do {
				if ((LA(1)==AND)) {
					AST tmp69_AST = null;
					tmp69_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp69_AST);
					match(AND);
					negatedExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop91;
				}
				
			} while (true);
			}
			logicalAndExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_25);
		}
		returnAST = logicalAndExpression_AST;
	}
	
	public final void negatedExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST negatedExpression_AST = null;
		AST x_AST = null;
		AST y_AST = null;
		weakKeywords();
		
		try {      // for error handling
			switch ( LA(1)) {
			case NOT:
			{
				AST tmp70_AST = null;
				tmp70_AST = astFactory.create(LT(1));
				match(NOT);
				negatedExpression();
				x_AST = (AST)returnAST;
				negatedExpression_AST = (AST)currentAST.root;
				negatedExpression_AST = negateNode(x_AST);
				currentAST.root = negatedExpression_AST;
				currentAST.child = negatedExpression_AST!=null &&negatedExpression_AST.getFirstChild()!=null ?
					negatedExpression_AST.getFirstChild() : negatedExpression_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case OPEN:
			case PLUS:
			case MINUS:
			case COLON:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			{
				equalityExpression();
				y_AST = (AST)returnAST;
				negatedExpression_AST = (AST)currentAST.root;
				negatedExpression_AST = y_AST;
				currentAST.root = negatedExpression_AST;
				currentAST.child = negatedExpression_AST!=null &&negatedExpression_AST.getFirstChild()!=null ?
					negatedExpression_AST.getFirstChild() : negatedExpression_AST;
				currentAST.advanceChildToEnd();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_26);
		}
		returnAST = negatedExpression_AST;
	}
	
	public final void equalityExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression_AST = null;
		AST x_AST = null;
		Token  is = null;
		AST is_AST = null;
		Token  ne = null;
		AST ne_AST = null;
		AST y_AST = null;
		
		try {      // for error handling
			relationalExpression();
			x_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop97:
			do {
				if ((_tokenSet_27.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case EQ:
					{
						AST tmp71_AST = null;
						tmp71_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp71_AST);
						match(EQ);
						break;
					}
					case IS:
					{
						is = LT(1);
						is_AST = astFactory.create(is);
						astFactory.makeASTRoot(currentAST, is_AST);
						match(IS);
						is_AST.setType(EQ);
						{
						switch ( LA(1)) {
						case NOT:
						{
							match(NOT);
							is_AST.setType(NE);
							break;
						}
						case ALL:
						case ANY:
						case AVG:
						case COUNT:
						case ELEMENTS:
						case EXISTS:
						case FALSE:
						case INDICES:
						case MAX:
						case MIN:
						case NULL:
						case SOME:
						case SUM:
						case TRUE:
						case CASE:
						case EMPTY:
						case NUM_DOUBLE:
						case NUM_FLOAT:
						case NUM_LONG:
						case OPEN:
						case PLUS:
						case MINUS:
						case COLON:
						case PARAM:
						case NUM_INT:
						case QUOTED_STRING:
						case IDENT:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						break;
					}
					case NE:
					{
						AST tmp73_AST = null;
						tmp73_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp73_AST);
						match(NE);
						break;
					}
					case SQL_NE:
					{
						ne = LT(1);
						ne_AST = astFactory.create(ne);
						astFactory.makeASTRoot(currentAST, ne_AST);
						match(SQL_NE);
						ne_AST.setType(NE);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					relationalExpression();
					y_AST = (AST)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop97;
				}
				
			} while (true);
			}
			equalityExpression_AST = (AST)currentAST.root;
			
						// Post process the equality expression to clean up 'is null', etc.
						equalityExpression_AST = processEqualityExpression(equalityExpression_AST);
					
			currentAST.root = equalityExpression_AST;
			currentAST.child = equalityExpression_AST!=null &&equalityExpression_AST.getFirstChild()!=null ?
				equalityExpression_AST.getFirstChild() : equalityExpression_AST;
			currentAST.advanceChildToEnd();
			equalityExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_26);
		}
		returnAST = equalityExpression_AST;
	}
	
	public final void relationalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST relationalExpression_AST = null;
		Token  n = null;
		AST n_AST = null;
		Token  i = null;
		AST i_AST = null;
		Token  b = null;
		AST b_AST = null;
		Token  l = null;
		AST l_AST = null;
		AST p_AST = null;
		
		try {      // for error handling
			concatenation();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case EOF:
			case AND:
			case AS:
			case ASCENDING:
			case DESCENDING:
			case FROM:
			case FULL:
			case GROUP:
			case HAVING:
			case INNER:
			case IS:
			case JOIN:
			case LEFT:
			case OR:
			case ORDER:
			case RIGHT:
			case UNION:
			case WHERE:
			case THEN:
			case COMMA:
			case EQ:
			case CLOSE:
			case LITERAL_ascending:
			case LITERAL_descending:
			case NE:
			case SQL_NE:
			case LT:
			case GT:
			case LE:
			case GE:
			case CLOSE_BRACKET:
			{
				{
				{
				_loop103:
				do {
					if (((LA(1) >= LT && LA(1) <= GE))) {
						{
						switch ( LA(1)) {
						case LT:
						{
							AST tmp74_AST = null;
							tmp74_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp74_AST);
							match(LT);
							break;
						}
						case GT:
						{
							AST tmp75_AST = null;
							tmp75_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp75_AST);
							match(GT);
							break;
						}
						case LE:
						{
							AST tmp76_AST = null;
							tmp76_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp76_AST);
							match(LE);
							break;
						}
						case GE:
						{
							AST tmp77_AST = null;
							tmp77_AST = astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp77_AST);
							match(GE);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						additiveExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop103;
					}
					
				} while (true);
				}
				}
				break;
			}
			case BETWEEN:
			case IN:
			case LIKE:
			case NOT:
			case MEMBER:
			{
				{
				switch ( LA(1)) {
				case NOT:
				{
					n = LT(1);
					n_AST = astFactory.create(n);
					match(NOT);
					break;
				}
				case BETWEEN:
				case IN:
				case LIKE:
				case MEMBER:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				switch ( LA(1)) {
				case IN:
				{
					{
					i = LT(1);
					i_AST = astFactory.create(i);
					astFactory.makeASTRoot(currentAST, i_AST);
					match(IN);
					
										i_AST.setType( (n == null) ? IN : NOT_IN);
										i_AST.setText( (n == null) ? "in" : "not in");
									
					inList();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case BETWEEN:
				{
					{
					b = LT(1);
					b_AST = astFactory.create(b);
					astFactory.makeASTRoot(currentAST, b_AST);
					match(BETWEEN);
					
										b_AST.setType( (n == null) ? BETWEEN : NOT_BETWEEN);
										b_AST.setText( (n == null) ? "between" : "not between");
									
					betweenList();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case LIKE:
				{
					{
					l = LT(1);
					l_AST = astFactory.create(l);
					astFactory.makeASTRoot(currentAST, l_AST);
					match(LIKE);
					
										l_AST.setType( (n == null) ? LIKE : NOT_LIKE);
										l_AST.setText( (n == null) ? "like" : "not like");
									
					concatenation();
					astFactory.addASTChild(currentAST, returnAST);
					likeEscape();
					astFactory.addASTChild(currentAST, returnAST);
					}
					break;
				}
				case MEMBER:
				{
					{
					match(MEMBER);
					{
					switch ( LA(1)) {
					case OF:
					{
						match(OF);
						break;
					}
					case IDENT:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					path();
					p_AST = (AST)returnAST;
					
									processMemberOf(n,p_AST,currentAST);
								
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			relationalExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_28);
		}
		returnAST = relationalExpression_AST;
	}
	
	public final void additiveExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression_AST = null;
		
		try {      // for error handling
			multiplyExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop122:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS)) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						AST tmp80_AST = null;
						tmp80_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp80_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						AST tmp81_AST = null;
						tmp81_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp81_AST);
						match(MINUS);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					multiplyExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop122;
				}
				
			} while (true);
			}
			additiveExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_29);
		}
		returnAST = additiveExpression_AST;
	}
	
	public final void inList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inList_AST = null;
		AST x_AST = null;
		
		try {      // for error handling
			compoundExpr();
			x_AST = (AST)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			inList_AST = (AST)currentAST.root;
			inList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(IN_LIST,"inList")).add(inList_AST));
			currentAST.root = inList_AST;
			currentAST.child = inList_AST!=null &&inList_AST.getFirstChild()!=null ?
				inList_AST.getFirstChild() : inList_AST;
			currentAST.advanceChildToEnd();
			inList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_28);
		}
		returnAST = inList_AST;
	}
	
	public final void betweenList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST betweenList_AST = null;
		
		try {      // for error handling
			concatenation();
			astFactory.addASTChild(currentAST, returnAST);
			match(AND);
			concatenation();
			astFactory.addASTChild(currentAST, returnAST);
			betweenList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_28);
		}
		returnAST = betweenList_AST;
	}
	
	public final void likeEscape() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST likeEscape_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case ESCAPE:
			{
				AST tmp83_AST = null;
				tmp83_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp83_AST);
				match(ESCAPE);
				concatenation();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case AND:
			case AS:
			case ASCENDING:
			case DESCENDING:
			case FROM:
			case FULL:
			case GROUP:
			case HAVING:
			case INNER:
			case IS:
			case JOIN:
			case LEFT:
			case OR:
			case ORDER:
			case RIGHT:
			case UNION:
			case WHERE:
			case THEN:
			case COMMA:
			case EQ:
			case CLOSE:
			case LITERAL_ascending:
			case LITERAL_descending:
			case NE:
			case SQL_NE:
			case CLOSE_BRACKET:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			likeEscape_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_28);
		}
		returnAST = likeEscape_AST;
	}
	
	public final void compoundExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST compoundExpr_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ELEMENTS:
			case INDICES:
			{
				collectionExpr();
				astFactory.addASTChild(currentAST, returnAST);
				compoundExpr_AST = (AST)currentAST.root;
				break;
			}
			case IDENT:
			{
				path();
				astFactory.addASTChild(currentAST, returnAST);
				compoundExpr_AST = (AST)currentAST.root;
				break;
			}
			case OPEN:
			{
				{
				match(OPEN);
				{
				switch ( LA(1)) {
				case ALL:
				case ANY:
				case AVG:
				case COUNT:
				case ELEMENTS:
				case EXISTS:
				case FALSE:
				case INDICES:
				case MAX:
				case MIN:
				case NOT:
				case NULL:
				case SOME:
				case SUM:
				case TRUE:
				case CASE:
				case EMPTY:
				case NUM_DOUBLE:
				case NUM_FLOAT:
				case NUM_LONG:
				case OPEN:
				case PLUS:
				case MINUS:
				case COLON:
				case PARAM:
				case NUM_INT:
				case QUOTED_STRING:
				case IDENT:
				{
					{
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					{
					_loop179:
					do {
						if ((LA(1)==COMMA)) {
							match(COMMA);
							expression();
							astFactory.addASTChild(currentAST, returnAST);
						}
						else {
							break _loop179;
						}
						
					} while (true);
					}
					}
					break;
				}
				case FROM:
				case GROUP:
				case ORDER:
				case SELECT:
				case UNION:
				case WHERE:
				case CLOSE:
				{
					subQuery();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(CLOSE);
				}
				compoundExpr_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_28);
		}
		returnAST = compoundExpr_AST;
	}
	
	public final void multiplyExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplyExpression_AST = null;
		
		try {      // for error handling
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop126:
			do {
				if ((LA(1)==STAR||LA(1)==DIV)) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp87_AST = null;
						tmp87_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp87_AST);
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp88_AST = null;
						tmp88_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp88_AST);
						match(DIV);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop126;
				}
				
			} while (true);
			}
			multiplyExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_30);
		}
		returnAST = multiplyExpression_AST;
	}
	
	public final void unaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case MINUS:
			{
				AST tmp89_AST = null;
				tmp89_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp89_AST);
				match(MINUS);
				tmp89_AST.setType(UNARY_MINUS);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case PLUS:
			{
				AST tmp90_AST = null;
				tmp90_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp90_AST);
				match(PLUS);
				tmp90_AST.setType(UNARY_PLUS);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case CASE:
			{
				caseExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case ALL:
			case ANY:
			case EXISTS:
			case SOME:
			{
				quantifiedExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case AVG:
			case COUNT:
			case ELEMENTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NULL:
			case SUM:
			case TRUE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case OPEN:
			case COLON:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			{
				atom();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_31);
		}
		returnAST = unaryExpression_AST;
	}
	
	public final void caseExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST caseExpression_AST = null;
		
		try {      // for error handling
			if ((LA(1)==CASE) && (LA(2)==WHEN)) {
				AST tmp91_AST = null;
				tmp91_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp91_AST);
				match(CASE);
				{
				int _cnt130=0;
				_loop130:
				do {
					if ((LA(1)==WHEN)) {
						whenClause();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						if ( _cnt130>=1 ) { break _loop130; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt130++;
				} while (true);
				}
				{
				switch ( LA(1)) {
				case ELSE:
				{
					elseClause();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case END:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(END);
				caseExpression_AST = (AST)currentAST.root;
			}
			else if ((LA(1)==CASE) && (_tokenSet_32.member(LA(2)))) {
				AST tmp93_AST = null;
				tmp93_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp93_AST);
				match(CASE);
				tmp93_AST.setType(CASE2);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				int _cnt133=0;
				_loop133:
				do {
					if ((LA(1)==WHEN)) {
						altWhenClause();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						if ( _cnt133>=1 ) { break _loop133; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt133++;
				} while (true);
				}
				{
				switch ( LA(1)) {
				case ELSE:
				{
					elseClause();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case END:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(END);
				caseExpression_AST = (AST)currentAST.root;
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_31);
		}
		returnAST = caseExpression_AST;
	}
	
	public final void quantifiedExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST quantifiedExpression_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SOME:
			{
				AST tmp95_AST = null;
				tmp95_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp95_AST);
				match(SOME);
				break;
			}
			case EXISTS:
			{
				AST tmp96_AST = null;
				tmp96_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp96_AST);
				match(EXISTS);
				break;
			}
			case ALL:
			{
				AST tmp97_AST = null;
				tmp97_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp97_AST);
				match(ALL);
				break;
			}
			case ANY:
			{
				AST tmp98_AST = null;
				tmp98_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp98_AST);
				match(ANY);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case IDENT:
			{
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case ELEMENTS:
			case INDICES:
			{
				collectionExpr();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case OPEN:
			{
				{
				match(OPEN);
				{
				subQuery();
				astFactory.addASTChild(currentAST, returnAST);
				}
				match(CLOSE);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			quantifiedExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_31);
		}
		returnAST = quantifiedExpression_AST;
	}
	
	public final void atom() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST atom_AST = null;
		Token  op = null;
		AST op_AST = null;
		Token  lb = null;
		AST lb_AST = null;
		
		try {      // for error handling
			primaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop150:
			do {
				switch ( LA(1)) {
				case DOT:
				{
					AST tmp101_AST = null;
					tmp101_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp101_AST);
					match(DOT);
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
					{
					switch ( LA(1)) {
					case OPEN:
					{
						{
						op = LT(1);
						op_AST = astFactory.create(op);
						astFactory.makeASTRoot(currentAST, op_AST);
						match(OPEN);
						op_AST.setType(METHOD_CALL);
						exprList();
						astFactory.addASTChild(currentAST, returnAST);
						match(CLOSE);
						}
						break;
					}
					case EOF:
					case AND:
					case AS:
					case ASCENDING:
					case BETWEEN:
					case DESCENDING:
					case DOT:
					case ESCAPE:
					case FROM:
					case FULL:
					case GROUP:
					case HAVING:
					case IN:
					case INNER:
					case IS:
					case JOIN:
					case LEFT:
					case LIKE:
					case NOT:
					case OR:
					case ORDER:
					case RIGHT:
					case UNION:
					case WHERE:
					case END:
					case ELSE:
					case THEN:
					case WHEN:
					case MEMBER:
					case COMMA:
					case EQ:
					case CLOSE:
					case LITERAL_ascending:
					case LITERAL_descending:
					case NE:
					case SQL_NE:
					case LT:
					case GT:
					case LE:
					case GE:
					case CONCAT:
					case PLUS:
					case MINUS:
					case STAR:
					case DIV:
					case OPEN_BRACKET:
					case CLOSE_BRACKET:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case OPEN_BRACKET:
				{
					lb = LT(1);
					lb_AST = astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(OPEN_BRACKET);
					lb_AST.setType(INDEX_OP);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					match(CLOSE_BRACKET);
					break;
				}
				default:
				{
					break _loop150;
				}
				}
			} while (true);
			}
			atom_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_31);
		}
		returnAST = atom_AST;
	}
	
	public final void whenClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST whenClause_AST = null;
		
		try {      // for error handling
			{
			AST tmp104_AST = null;
			tmp104_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp104_AST);
			match(WHEN);
			logicalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			match(THEN);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			}
			whenClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_33);
		}
		returnAST = whenClause_AST;
	}
	
	public final void elseClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST elseClause_AST = null;
		
		try {      // for error handling
			{
			AST tmp106_AST = null;
			tmp106_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp106_AST);
			match(ELSE);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			}
			elseClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_34);
		}
		returnAST = elseClause_AST;
	}
	
	public final void altWhenClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST altWhenClause_AST = null;
		
		try {      // for error handling
			{
			AST tmp107_AST = null;
			tmp107_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp107_AST);
			match(WHEN);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			match(THEN);
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			}
			altWhenClause_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_33);
		}
		returnAST = altWhenClause_AST;
	}
	
	public final void collectionExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST collectionExpr_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case ELEMENTS:
			{
				AST tmp109_AST = null;
				tmp109_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp109_AST);
				match(ELEMENTS);
				break;
			}
			case INDICES:
			{
				AST tmp110_AST = null;
				tmp110_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp110_AST);
				match(INDICES);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(OPEN);
			path();
			astFactory.addASTChild(currentAST, returnAST);
			match(CLOSE);
			collectionExpr_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		returnAST = collectionExpr_AST;
	}
	
	public final void subQuery() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subQuery_AST = null;
		
		try {      // for error handling
			union();
			astFactory.addASTChild(currentAST, returnAST);
			subQuery_AST = (AST)currentAST.root;
			subQuery_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(QUERY,"query")).add(subQuery_AST));
			currentAST.root = subQuery_AST;
			currentAST.child = subQuery_AST!=null &&subQuery_AST.getFirstChild()!=null ?
				subQuery_AST.getFirstChild() : subQuery_AST;
			currentAST.advanceChildToEnd();
			subQuery_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		returnAST = subQuery_AST;
	}
	
	public final void exprList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exprList_AST = null;
		Token  t = null;
		AST t_AST = null;
		Token  l = null;
		AST l_AST = null;
		Token  b = null;
		AST b_AST = null;
		
		AST trimSpec = null;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case TRAILING:
			{
				t = LT(1);
				t_AST = astFactory.create(t);
				astFactory.addASTChild(currentAST, t_AST);
				match(TRAILING);
				trimSpec = t_AST;
				break;
			}
			case LEADING:
			{
				l = LT(1);
				l_AST = astFactory.create(l);
				astFactory.addASTChild(currentAST, l_AST);
				match(LEADING);
				trimSpec = l_AST;
				break;
			}
			case BOTH:
			{
				b = LT(1);
				b_AST = astFactory.create(b);
				astFactory.addASTChild(currentAST, b_AST);
				match(BOTH);
				trimSpec = b_AST;
				break;
			}
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case FROM:
			case INDICES:
			case MAX:
			case MIN:
			case NOT:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case OPEN:
			case CLOSE:
			case PLUS:
			case MINUS:
			case COLON:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if(trimSpec != null) trimSpec.setType(IDENT);
			{
			switch ( LA(1)) {
			case ALL:
			case ANY:
			case AVG:
			case COUNT:
			case ELEMENTS:
			case EXISTS:
			case FALSE:
			case INDICES:
			case MAX:
			case MIN:
			case NOT:
			case NULL:
			case SOME:
			case SUM:
			case TRUE:
			case CASE:
			case EMPTY:
			case NUM_DOUBLE:
			case NUM_FLOAT:
			case NUM_LONG:
			case OPEN:
			case PLUS:
			case MINUS:
			case COLON:
			case PARAM:
			case NUM_INT:
			case QUOTED_STRING:
			case IDENT:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COMMA:
				{
					{
					int _cnt186=0;
					_loop186:
					do {
						if ((LA(1)==COMMA)) {
							match(COMMA);
							expression();
							astFactory.addASTChild(currentAST, returnAST);
						}
						else {
							if ( _cnt186>=1 ) { break _loop186; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt186++;
					} while (true);
					}
					break;
				}
				case FROM:
				{
					AST tmp114_AST = null;
					tmp114_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp114_AST);
					match(FROM);
					tmp114_AST.setType(IDENT);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case AS:
				{
					match(AS);
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case CLOSE:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case FROM:
			{
				AST tmp116_AST = null;
				tmp116_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp116_AST);
				match(FROM);
				tmp116_AST.setType(IDENT);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			exprList_AST = (AST)currentAST.root;
			exprList_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR_LIST,"exprList")).add(exprList_AST));
			currentAST.root = exprList_AST;
			currentAST.child = exprList_AST!=null &&exprList_AST.getFirstChild()!=null ?
				exprList_AST.getFirstChild() : exprList_AST;
			currentAST.advanceChildToEnd();
			exprList_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		returnAST = exprList_AST;
	}
	
	public final void identPrimary() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST identPrimary_AST = null;
		Token  o = null;
		AST o_AST = null;
		Token  op = null;
		AST op_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				handleDotIdent();
				{
				_loop163:
				do {
					if ((LA(1)==DOT) && (LA(2)==ELEMENTS||LA(2)==OBJECT||LA(2)==IDENT) && (_tokenSet_35.member(LA(3)))) {
						AST tmp117_AST = null;
						tmp117_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp117_AST);
						match(DOT);
						{
						switch ( LA(1)) {
						case IDENT:
						{
							identifier();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case ELEMENTS:
						{
							AST tmp118_AST = null;
							tmp118_AST = astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp118_AST);
							match(ELEMENTS);
							break;
						}
						case OBJECT:
						{
							o = LT(1);
							o_AST = astFactory.create(o);
							astFactory.addASTChild(currentAST, o_AST);
							match(OBJECT);
							o_AST.setType(IDENT);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
					}
					else {
						break _loop163;
					}
					
				} while (true);
				}
				{
				switch ( LA(1)) {
				case OPEN:
				{
					{
					op = LT(1);
					op_AST = astFactory.create(op);
					astFactory.makeASTRoot(currentAST, op_AST);
					match(OPEN);
					op_AST.setType(METHOD_CALL);
					exprList();
					astFactory.addASTChild(currentAST, returnAST);
					match(CLOSE);
					}
					break;
				}
				case EOF:
				case AND:
				case AS:
				case ASCENDING:
				case BETWEEN:
				case DESCENDING:
				case DOT:
				case ESCAPE:
				case FROM:
				case FULL:
				case GROUP:
				case HAVING:
				case IN:
				case INNER:
				case IS:
				case JOIN:
				case LEFT:
				case LIKE:
				case NOT:
				case OR:
				case ORDER:
				case RIGHT:
				case UNION:
				case WHERE:
				case END:
				case ELSE:
				case THEN:
				case WHEN:
				case MEMBER:
				case COMMA:
				case EQ:
				case CLOSE:
				case LITERAL_ascending:
				case LITERAL_descending:
				case NE:
				case SQL_NE:
				case LT:
				case GT:
				case LE:
				case GE:
				case CONCAT:
				case PLUS:
				case MINUS:
				case STAR:
				case DIV:
				case OPEN_BRACKET:
				case CLOSE_BRACKET:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				identPrimary_AST = (AST)currentAST.root;
				break;
			}
			case AVG:
			case COUNT:
			case ELEMENTS:
			case INDICES:
			case MAX:
			case MIN:
			case SUM:
			{
				aggregate();
				astFactory.addASTChild(currentAST, returnAST);
				identPrimary_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		returnAST = identPrimary_AST;
	}
	
	public final void constant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST constant_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUM_INT:
			{
				AST tmp120_AST = null;
				tmp120_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp120_AST);
				match(NUM_INT);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NUM_FLOAT:
			{
				AST tmp121_AST = null;
				tmp121_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp121_AST);
				match(NUM_FLOAT);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NUM_LONG:
			{
				AST tmp122_AST = null;
				tmp122_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp122_AST);
				match(NUM_LONG);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NUM_DOUBLE:
			{
				AST tmp123_AST = null;
				tmp123_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp123_AST);
				match(NUM_DOUBLE);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case QUOTED_STRING:
			{
				AST tmp124_AST = null;
				tmp124_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp124_AST);
				match(QUOTED_STRING);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case NULL:
			{
				AST tmp125_AST = null;
				tmp125_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp125_AST);
				match(NULL);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case TRUE:
			{
				AST tmp126_AST = null;
				tmp126_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp126_AST);
				match(TRUE);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case FALSE:
			{
				AST tmp127_AST = null;
				tmp127_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp127_AST);
				match(FALSE);
				constant_AST = (AST)currentAST.root;
				break;
			}
			case EMPTY:
			{
				AST tmp128_AST = null;
				tmp128_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp128_AST);
				match(EMPTY);
				constant_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		returnAST = constant_AST;
	}
	
	public final void expressionOrVector() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expressionOrVector_AST = null;
		AST e_AST = null;
		AST v_AST = null;
		
		try {      // for error handling
			expression();
			e_AST = (AST)returnAST;
			{
			switch ( LA(1)) {
			case COMMA:
			{
				vectorExpr();
				v_AST = (AST)returnAST;
				break;
			}
			case CLOSE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expressionOrVector_AST = (AST)currentAST.root;
			
					// If this is a vector expression, create a parent node for it.
					if (v_AST != null)
						expressionOrVector_AST = (AST)astFactory.make( (new ASTArray(3)).add(astFactory.create(VECTOR_EXPR,"{vector}")).add(e_AST).add(v_AST));
					else
						expressionOrVector_AST = e_AST;
				
			currentAST.root = expressionOrVector_AST;
			currentAST.child = expressionOrVector_AST!=null &&expressionOrVector_AST.getFirstChild()!=null ?
				expressionOrVector_AST.getFirstChild() : expressionOrVector_AST;
			currentAST.advanceChildToEnd();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		returnAST = expressionOrVector_AST;
	}
	
	public final void vectorExpr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST vectorExpr_AST = null;
		
		try {      // for error handling
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop159:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop159;
				}
				
			} while (true);
			}
			vectorExpr_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_13);
		}
		returnAST = vectorExpr_AST;
	}
	
	public final void aggregate() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST aggregate_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case AVG:
			case MAX:
			case MIN:
			case SUM:
			{
				{
				switch ( LA(1)) {
				case SUM:
				{
					AST tmp131_AST = null;
					tmp131_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp131_AST);
					match(SUM);
					break;
				}
				case AVG:
				{
					AST tmp132_AST = null;
					tmp132_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp132_AST);
					match(AVG);
					break;
				}
				case MAX:
				{
					AST tmp133_AST = null;
					tmp133_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp133_AST);
					match(MAX);
					break;
				}
				case MIN:
				{
					AST tmp134_AST = null;
					tmp134_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp134_AST);
					match(MIN);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(OPEN);
				additiveExpression();
				astFactory.addASTChild(currentAST, returnAST);
				match(CLOSE);
				aggregate_AST = (AST)currentAST.root;
				aggregate_AST.setType(AGGREGATE);
				aggregate_AST = (AST)currentAST.root;
				break;
			}
			case COUNT:
			{
				AST tmp137_AST = null;
				tmp137_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp137_AST);
				match(COUNT);
				match(OPEN);
				{
				switch ( LA(1)) {
				case STAR:
				{
					AST tmp139_AST = null;
					tmp139_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp139_AST);
					match(STAR);
					tmp139_AST.setType(ROW_STAR);
					break;
				}
				case ALL:
				case DISTINCT:
				case ELEMENTS:
				case INDICES:
				case IDENT:
				{
					{
					{
					switch ( LA(1)) {
					case DISTINCT:
					{
						AST tmp140_AST = null;
						tmp140_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp140_AST);
						match(DISTINCT);
						break;
					}
					case ALL:
					{
						AST tmp141_AST = null;
						tmp141_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp141_AST);
						match(ALL);
						break;
					}
					case ELEMENTS:
					case INDICES:
					case IDENT:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					{
					switch ( LA(1)) {
					case IDENT:
					{
						path();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case ELEMENTS:
					case INDICES:
					{
						collectionExpr();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					}
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(CLOSE);
				aggregate_AST = (AST)currentAST.root;
				break;
			}
			case ELEMENTS:
			case INDICES:
			{
				collectionExpr();
				astFactory.addASTChild(currentAST, returnAST);
				aggregate_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		returnAST = aggregate_AST;
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
		"FLOAT_SUFFIX"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 9077567998918658L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 9007199254740994L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 1128098946875394L, 34359738368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 9007199254740994L, 4294967296L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 0L, 8589934592L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 1307261066675241410L, 74311519860424704L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 154269485447267778L, 2286893991460865L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 1163144776902508546L, 38654705664L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 1125899906842626L, 34359738368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 9044582671056898L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 550586252655904194L, 4503509433057281L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 5181312067402913778L, 144115185659936773L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 0L, 34359738368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 10135298201616386L, 34359738368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 1128098930098178L, 34359738368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 10135298205810690L, 34359738368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 10152903551516802L, 72057632692633600L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 10152903549386754L, 38654705664L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 1163074408156233730L, 38654705664L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 154268091625242626L, 38654705664L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 1163144776969617410L, 38654705664L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 154268091663008130L, 2252250785251328L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 1125899906842626L, 38654705664L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 10135298205810690L, 38654705664L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 154269191174635906L, 2252250785251328L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 154269191174635970L, 2252250785251328L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 2147483648L, 1657857376256L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 154269193322119618L, 2253908642627584L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 154269485447267778L, 2322078363549697L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 154269485447267778L, 2533184596082689L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 550586252655871426L, 3377609526214657L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 4630686232326312496L, 139822713739935744L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { 396316767208603648L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 36028797018963968L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 550586252655904194L, 4503526612926465L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	
	}

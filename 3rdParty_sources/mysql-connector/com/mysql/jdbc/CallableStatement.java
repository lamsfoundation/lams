/*
 Copyright (C) 2002-2007 MySQL AB

 This program is free software; you can redistribute it and/or modify
 it under the terms of version 2 of the GNU General Public License as 
 published by the Free Software Foundation.

 There are special exceptions to the terms and conditions of the GPL 
 as it is applied to this software. View the full text of the 
 exception in file EXCEPTIONS-CONNECTOR-J in the directory of this 
 software distribution.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
 */
package com.mysql.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import java.math.BigDecimal;

import java.net.URL;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Representation of stored procedures for JDBC
 * 
 * @author Mark Matthews
 *
 *          Exp $
 */
public class CallableStatement extends PreparedStatement implements
		java.sql.CallableStatement {
	class CallableStatementParam {
		int desiredJdbcType;

		int index;

		int inOutModifier;

		boolean isIn;

		boolean isOut;

		int jdbcType;

		short nullability;

		String paramName;

		int precision;

		int scale;

		String typeName;

		CallableStatementParam(String name, int idx, boolean in, boolean out,
				int jdbcType, String typeName, int precision, int scale,
				short nullability, int inOutModifier) {
			this.paramName = name;
			this.isIn = in;
			this.isOut = out;
			this.index = idx;

			this.jdbcType = jdbcType;
			this.typeName = typeName;
			this.precision = precision;
			this.scale = scale;
			this.nullability = nullability;
			this.inOutModifier = inOutModifier;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#clone()
		 */
		protected Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}

	class CallableStatementParamInfo {
		String catalogInUse;

		boolean isFunctionCall;

		String nativeSql;

		int numParameters;

		List parameterList;

		Map parameterMap;

		/**
		 * Constructor that converts a full list of parameter metadata into one
		 * that only represents the placeholders present in the {CALL ()}.
		 * 
		 * @param fullParamInfo the metadata for all parameters for this stored 
		 * procedure or function.
		 */
		CallableStatementParamInfo(CallableStatementParamInfo fullParamInfo) {
			this.nativeSql = originalSql;
			this.catalogInUse = currentCatalog;
			isFunctionCall = fullParamInfo.isFunctionCall;
			int[] localParameterMap = placeholderToParameterIndexMap;
			int parameterMapLength = localParameterMap.length;
			
			parameterList = new ArrayList(fullParamInfo.numParameters);
			parameterMap = new HashMap(fullParamInfo.numParameters);
			
			if (isFunctionCall) {
				// Take the return value
				parameterList.add(fullParamInfo.parameterList.get(0));
			}
			
			int offset = isFunctionCall ? 1 : 0;
			
			for (int i = 0; i < parameterMapLength; i++) {
				if (localParameterMap[i] != 0) {
					CallableStatementParam param = (CallableStatementParam)fullParamInfo.parameterList.get(localParameterMap[i] + offset);
					
					parameterList.add(param);
					parameterMap.put(param.paramName, param);
				}
			}
			
			this.numParameters = parameterList.size();
		}
		
		CallableStatementParamInfo(java.sql.ResultSet paramTypesRs)
				throws SQLException {
			boolean hadRows = paramTypesRs.last();

			this.nativeSql = originalSql;
			this.catalogInUse = currentCatalog;
			isFunctionCall = callingStoredFunction;

			if (hadRows) {
				this.numParameters = paramTypesRs.getRow();

				this.parameterList = new ArrayList(this.numParameters);
				this.parameterMap = new HashMap(this.numParameters);

				paramTypesRs.beforeFirst();

				addParametersFromDBMD(paramTypesRs);
			} else {
				this.numParameters = 0;
			}
			
			if (isFunctionCall) {
				this.numParameters += 1;
			}
		}

		private void addParametersFromDBMD(java.sql.ResultSet paramTypesRs)
				throws SQLException {
			int i = 0;

			while (paramTypesRs.next()) {
				String paramName = paramTypesRs.getString(4);
				int inOutModifier = paramTypesRs.getInt(5);

				boolean isOutParameter = false;
				boolean isInParameter = false;

				if (i == 0 && isFunctionCall) {
					isOutParameter = true;
					isInParameter = false;
				} else if (inOutModifier == DatabaseMetaData.procedureColumnInOut) {
					isOutParameter = true;
					isInParameter = true;
				} else if (inOutModifier == DatabaseMetaData.procedureColumnIn) {
					isOutParameter = false;
					isInParameter = true;
				} else if (inOutModifier == DatabaseMetaData.procedureColumnOut) {
					isOutParameter = true;
					isInParameter = false;
				}

				int jdbcType = paramTypesRs.getInt(6);
				String typeName = paramTypesRs.getString(7);
				int precision = paramTypesRs.getInt(8);
				int scale = paramTypesRs.getInt(10);
				short nullability = paramTypesRs.getShort(12);

				CallableStatementParam paramInfoToAdd = new CallableStatementParam(
						paramName, i++, isInParameter, isOutParameter,
						jdbcType, typeName, precision, scale, nullability,
						inOutModifier);

				this.parameterList.add(paramInfoToAdd);
				this.parameterMap.put(paramName, paramInfoToAdd);
			}
		}

		protected void checkBounds(int paramIndex) throws SQLException {
			int localParamIndex = paramIndex - 1;

			if ((paramIndex < 0) || (localParamIndex >= this.numParameters)) {
				throw SQLError.createSQLException(
						Messages.getString("CallableStatement.11") + paramIndex //$NON-NLS-1$
								+ Messages.getString("CallableStatement.12") + numParameters //$NON-NLS-1$
								+ Messages.getString("CallableStatement.13"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT); //$NON-NLS-1$
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#clone()
		 */
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}

		CallableStatementParam getParameter(int index) {
			return (CallableStatementParam) this.parameterList.get(index);
		}

		CallableStatementParam getParameter(String name) {
			return (CallableStatementParam) this.parameterMap.get(name);
		}

		public String getParameterClassName(int arg0) throws SQLException {
			String mysqlTypeName = getParameterTypeName(arg0);
			
			boolean isBinaryOrBlob = StringUtils.indexOfIgnoreCase(mysqlTypeName, "BLOB") != -1 || 
				StringUtils.indexOfIgnoreCase(mysqlTypeName, "BINARY") != -1;
			
			boolean isUnsigned = StringUtils.indexOfIgnoreCase(mysqlTypeName, "UNSIGNED") != -1;
			
			int mysqlTypeIfKnown = 0;
			
			if (StringUtils.startsWithIgnoreCase(mysqlTypeName, "MEDIUMINT")) {
				mysqlTypeIfKnown = MysqlDefs.FIELD_TYPE_INT24;
			}
			
			return ResultSetMetaData.getClassNameForJavaType(getParameterType(arg0), 
					isUnsigned, mysqlTypeIfKnown, isBinaryOrBlob, false);
		}

		public int getParameterCount() throws SQLException {
			if (this.parameterList == null) {
				return 0;
			}

			return this.parameterList.size();
		}

		public int getParameterMode(int arg0) throws SQLException {
			checkBounds(arg0);

			return getParameter(arg0 - 1).inOutModifier;
		}

		public int getParameterType(int arg0) throws SQLException {
			checkBounds(arg0);

			return getParameter(arg0 - 1).jdbcType;
		}

		public String getParameterTypeName(int arg0) throws SQLException {
			checkBounds(arg0);

			return getParameter(arg0 - 1).typeName;
		}

		public int getPrecision(int arg0) throws SQLException {
			checkBounds(arg0);

			return getParameter(arg0 - 1).precision;
		}

		public int getScale(int arg0) throws SQLException {
			checkBounds(arg0);

			return getParameter(arg0 - 1).scale;
		}

		public int isNullable(int arg0) throws SQLException {
			checkBounds(arg0);

			return getParameter(arg0 - 1).nullability;
		}

		public boolean isSigned(int arg0) throws SQLException {
			checkBounds(arg0);

			return false;
		}

		Iterator iterator() {
			return this.parameterList.iterator();
		}

		int numberOfParameters() {
			return this.numParameters;
		}
	}

	/**
	 * Can't implement this directly, as then you can't use callable statements
	 * on JDK-1.3.1, which unfortunately isn't EOL'd yet, and still present
	 * quite a bit out there in the wild (Websphere, FreeBSD, anyone?)
	 */

	class CallableStatementParamInfoJDBC3 extends CallableStatementParamInfo
			implements ParameterMetaData {

		CallableStatementParamInfoJDBC3(java.sql.ResultSet paramTypesRs)
				throws SQLException {
			super(paramTypesRs);
		}

		public CallableStatementParamInfoJDBC3(CallableStatementParamInfo paramInfo) {
			super(paramInfo);
		}
	}

	private final static int NOT_OUTPUT_PARAMETER_INDICATOR = Integer.MIN_VALUE;

	private final static String PARAMETER_NAMESPACE_PREFIX = "@com_mysql_jdbc_outparam_"; //$NON-NLS-1$

	private static String mangleParameterName(String origParameterName) {
		if (origParameterName == null) {
			return null;
		}

		int offset = 0;

		if (origParameterName.length() > 0
				&& origParameterName.charAt(0) == '@') {
			offset = 1;
		}

		StringBuffer paramNameBuf = new StringBuffer(PARAMETER_NAMESPACE_PREFIX
				.length()
				+ origParameterName.length());
		paramNameBuf.append(PARAMETER_NAMESPACE_PREFIX);
		paramNameBuf.append(origParameterName.substring(offset));

		return paramNameBuf.toString();
	}

	private boolean callingStoredFunction = false;

	private ResultSet functionReturnValueResults;

	private boolean hasOutputParams = false;

	// private List parameterList;
	// private Map parameterMap;
	private ResultSet outputParameterResults;

	private boolean outputParamWasNull = false;

	private int[] parameterIndexToRsIndex;

	protected CallableStatementParamInfo paramInfo;

	private CallableStatementParam returnValueParam;

	/**
	 * Creates a new CallableStatement
	 * 
	 * @param conn
	 *            the connection creating this statement
	 * @param paramInfo
	 *            the SQL to prepare
	 * 
	 * @throws SQLException
	 *             if an error occurs
	 */
	public CallableStatement(Connection conn,
			CallableStatementParamInfo paramInfo) throws SQLException {
		super(conn, paramInfo.nativeSql, paramInfo.catalogInUse);

		this.paramInfo = paramInfo;
		this.callingStoredFunction = this.paramInfo.isFunctionCall;
		
		if (this.callingStoredFunction) {
			this.parameterCount += 1;
		}
	}

	/**
	 * Creates a new CallableStatement
	 * 
	 * @param conn
	 *            the connection creating this statement
	 * @param catalog
	 *            catalog the current catalog
	 * 
	 * @throws SQLException
	 *             if an error occurs
	 */
	public CallableStatement(Connection conn, String catalog)
			throws SQLException {
		super(conn, catalog, null);

		determineParameterTypes();
		generateParameterMap();
		
		if (this.callingStoredFunction) {
			this.parameterCount += 1;
		}
	}

	private int[] placeholderToParameterIndexMap;
	
	
	private void generateParameterMap() throws SQLException {
		// if the user specified some parameters as literals, we need to
		// provide a map from the specified placeholders to the actual
		// parameter numbers
		
		int parameterCountFromMetaData = this.paramInfo.getParameterCount();
		
		// Ignore the first ? if this is a stored function, it doesn't count
		
		if (this.callingStoredFunction) {
			parameterCountFromMetaData--;
		}
		
		if (this.paramInfo != null &&
				this.parameterCount != parameterCountFromMetaData) {
			this.placeholderToParameterIndexMap = new int[this.parameterCount];
			
			int startPos = this.callingStoredFunction ? StringUtils.indexOfIgnoreCase(this.originalSql, 
			"SELECT") : StringUtils.indexOfIgnoreCase(this.originalSql, "CALL");
			
			if (startPos != -1) {
				int parenOpenPos = this.originalSql.indexOf('(', startPos + 4);
				
				if (parenOpenPos != -1) {
					int parenClosePos = StringUtils.indexOfIgnoreCaseRespectQuotes(parenOpenPos, 
							this.originalSql, ")", '\'', true);
					
					if (parenClosePos != -1) {
						List parsedParameters = StringUtils.split(this.originalSql.substring(parenOpenPos + 1, parenClosePos), ",", "'\"", "'\"", true);
						
						int numParsedParameters = parsedParameters.size();
						
						// sanity check
						
						if (numParsedParameters != this.parameterCount) {
							// bail?
						}
						
						int placeholderCount = 0;
						
						for (int i = 0; i < numParsedParameters; i++) {
							if (((String)parsedParameters.get(i)).equals("?")) {
								this.placeholderToParameterIndexMap[placeholderCount++] = i;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Creates a new CallableStatement
	 * 
	 * @param conn
	 *            the connection creating this statement
	 * @param sql
	 *            the SQL to prepare
	 * @param catalog
	 *            the current catalog
	 * 
	 * @throws SQLException
	 *             if an error occurs
	 */
	public CallableStatement(Connection conn, String sql, String catalog,
			boolean isFunctionCall) throws SQLException {
		super(conn, sql, catalog);

		this.callingStoredFunction = isFunctionCall;

		determineParameterTypes();
		generateParameterMap();
		
		if (this.callingStoredFunction) {
			this.parameterCount += 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	public void addBatch() throws SQLException {
		setOutParams();

		super.addBatch();
	}

	private CallableStatementParam checkIsOutputParam(int paramIndex)
			throws SQLException {

		if (this.callingStoredFunction) {
			if (paramIndex == 1) {

				if (this.returnValueParam == null) {
					this.returnValueParam = new CallableStatementParam("", 0,
							false, true, Types.VARCHAR, "VARCHAR", 0, 0,
							DatabaseMetaData.attributeNullableUnknown,
							DatabaseMetaData.procedureColumnReturn);
				}

				return this.returnValueParam;
			}

			// Move to position in output result set
			paramIndex--;
		}

		checkParameterIndexBounds(paramIndex);

		int localParamIndex = paramIndex - 1;

		if (this.placeholderToParameterIndexMap != null) {
			localParamIndex = this.placeholderToParameterIndexMap[localParamIndex];
		}
		
		CallableStatementParam paramDescriptor = this.paramInfo
				.getParameter(localParamIndex);

		// We don't have reliable metadata in this case, trust
		// the caller
		
		if (this.connection.getNoAccessToProcedureBodies()) {
			paramDescriptor.isOut = true;
			paramDescriptor.isIn = true;
			paramDescriptor.inOutModifier = DatabaseMetaData.procedureColumnInOut;
		} else if (!paramDescriptor.isOut) {
			throw SQLError.createSQLException(
					Messages.getString("CallableStatement.9") + paramIndex //$NON-NLS-1$
							+ Messages.getString("CallableStatement.10"), //$NON-NLS-1$
					SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
		}

		this.hasOutputParams = true;

		return paramDescriptor;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param paramIndex
	 * 
	 * @throws SQLException
	 */
	private void checkParameterIndexBounds(int paramIndex) throws SQLException {
		this.paramInfo.checkBounds(paramIndex);
	}

	/**
	 * Checks whether or not this statement is supposed to be providing
	 * streamable result sets...If output parameters are registered, the driver
	 * can not stream the results.
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	private void checkStreamability() throws SQLException {
		if (this.hasOutputParams && createStreamingResultSet()) {
			throw SQLError.createSQLException(Messages.getString("CallableStatement.14"), //$NON-NLS-1$
					SQLError.SQL_STATE_DRIVER_NOT_CAPABLE);
		}
	}

	public synchronized void clearParameters() throws SQLException {
		super.clearParameters();

		try {
			if (this.outputParameterResults != null) {
				this.outputParameterResults.close();
			}
		} finally {
			this.outputParameterResults = null;
		}
	}

	/**
	 * Used to fake up some metadata when we don't have access to 
	 * SHOW CREATE PROCEDURE or mysql.proc.
	 * 
	 * @throws SQLException if we can't build the metadata.
	 */
	private void fakeParameterTypes() throws SQLException {
		Field[] fields = new Field[13];

		fields[0] = new Field("", "PROCEDURE_CAT", Types.CHAR, 0);
		fields[1] = new Field("", "PROCEDURE_SCHEM", Types.CHAR, 0);
		fields[2] = new Field("", "PROCEDURE_NAME", Types.CHAR, 0);
		fields[3] = new Field("", "COLUMN_NAME", Types.CHAR, 0);
		fields[4] = new Field("", "COLUMN_TYPE", Types.CHAR, 0);
		fields[5] = new Field("", "DATA_TYPE", Types.SMALLINT, 0);
		fields[6] = new Field("", "TYPE_NAME", Types.CHAR, 0);
		fields[7] = new Field("", "PRECISION", Types.INTEGER, 0);
		fields[8] = new Field("", "LENGTH", Types.INTEGER, 0);
		fields[9] = new Field("", "SCALE", Types.SMALLINT, 0);
		fields[10] = new Field("", "RADIX", Types.SMALLINT, 0);
		fields[11] = new Field("", "NULLABLE", Types.SMALLINT, 0);
		fields[12] = new Field("", "REMARKS", Types.CHAR, 0);

		String procName = extractProcedureName();

		byte[] procNameAsBytes = null;

		try {
			procNameAsBytes = procName.getBytes("UTF-8");
		} catch (UnsupportedEncodingException ueEx) {
			procNameAsBytes = StringUtils.s2b(procName, this.connection);
		}

		ArrayList resultRows = new ArrayList();

		for (int i = 0; i < this.parameterCount; i++) {
			byte[][] row = new byte[13][];
			row[0] = null; // PROCEDURE_CAT
			row[1] = null; // PROCEDURE_SCHEM
			row[2] = procNameAsBytes; // PROCEDURE/NAME
			row[3] = StringUtils.s2b(String.valueOf(i), this.connection); // COLUMN_NAME

			row[4] = StringUtils.s2b(String
					.valueOf(DatabaseMetaData.procedureColumnIn),
					this.connection);

			row[5] = StringUtils.s2b(String.valueOf(Types.VARCHAR),
					this.connection); // DATA_TYPE
			row[6] = StringUtils.s2b("VARCHAR", this.connection); // TYPE_NAME
			row[7] = StringUtils.s2b(Integer.toString(65535), this.connection); // PRECISION
			row[8] = StringUtils.s2b(Integer.toString(65535), this.connection); // LENGTH
			row[9] = StringUtils.s2b(Integer.toString(0), this.connection); // SCALE
			row[10] = StringUtils.s2b(Integer.toString(10), this.connection); // RADIX

			row[11] = StringUtils.s2b(Integer
					.toString(DatabaseMetaData.procedureNullableUnknown),
					this.connection); // nullable

			row[12] = null;

			resultRows.add(row);
		}

		java.sql.ResultSet paramTypesRs = DatabaseMetaData.buildResultSet(
				fields, resultRows, this.connection);

		convertGetProcedureColumnsToInternalDescriptors(paramTypesRs);
	}
	
	private void determineParameterTypes() throws SQLException {
		if (this.connection.getNoAccessToProcedureBodies()) {
			fakeParameterTypes();
			
			return;
		}
		
		java.sql.ResultSet paramTypesRs = null;

		try {
			String procName = extractProcedureName();

			java.sql.DatabaseMetaData dbmd = this.connection.getMetaData();

			boolean useCatalog = false;

			if (procName.indexOf(".") == -1) {
				useCatalog = true;
			}

			paramTypesRs = dbmd.getProcedureColumns(this.connection
					.versionMeetsMinimum(5, 0, 2)
					&& useCatalog ? this.currentCatalog : null, null, procName,
					"%"); //$NON-NLS-1$

			convertGetProcedureColumnsToInternalDescriptors(paramTypesRs);
		} finally {
			SQLException sqlExRethrow = null;

			if (paramTypesRs != null) {
				try {
					paramTypesRs.close();
				} catch (SQLException sqlEx) {
					sqlExRethrow = sqlEx;
				}

				paramTypesRs = null;
			}

			if (sqlExRethrow != null) {
				throw sqlExRethrow;
			}
		}
	}

	private void convertGetProcedureColumnsToInternalDescriptors(java.sql.ResultSet paramTypesRs) throws SQLException {
		if (!this.connection.isRunningOnJDK13()) {
			this.paramInfo = new CallableStatementParamInfoJDBC3(
					paramTypesRs);
		} else {
			this.paramInfo = new CallableStatementParamInfo(paramTypesRs);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException {
		boolean returnVal = false;

		checkClosed();

		checkStreamability();

		synchronized (this.connection.getMutex()) {
			setInOutParamsOnServer();
			setOutParams();

			returnVal = super.execute();

			if (this.callingStoredFunction) {
				this.functionReturnValueResults = this.results;
				this.functionReturnValueResults.next();
				this.results = null;
			}

			retrieveOutParams();
		}

		if (!this.callingStoredFunction) {
			return returnVal;
		}

		// Functions can't return results
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	public java.sql.ResultSet executeQuery() throws SQLException {
		checkClosed();

		checkStreamability();

		java.sql.ResultSet execResults = null;

		synchronized (this.connection.getMutex()) {
			setInOutParamsOnServer();
			setOutParams();

			execResults = super.executeQuery();

			retrieveOutParams();
		}

		return execResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws SQLException {
		int returnVal = -1;

		checkClosed();

		checkStreamability();

		if (this.callingStoredFunction) {
			execute();

			return -1;
		}

		synchronized (this.connection.getMutex()) {
			setInOutParamsOnServer();
			setOutParams();

			returnVal = super.executeUpdate();

			retrieveOutParams();
		}

		return returnVal;
	}

	private String extractProcedureName() throws SQLException {
		String sanitizedSql = StringUtils.stripComments(this.originalSql, 
				"`\"'", "`\"'", true, false, true, true);
		
		// TODO: Do this with less memory allocation
		int endCallIndex = StringUtils.indexOfIgnoreCase(sanitizedSql,
				"CALL "); //$NON-NLS-1$
		int offset = 5;

		if (endCallIndex == -1) {
			endCallIndex = StringUtils.indexOfIgnoreCase(sanitizedSql,
					"SELECT ");
			offset = 7;
		}

		if (endCallIndex != -1) {
			StringBuffer nameBuf = new StringBuffer();

			String trimmedStatement = sanitizedSql.substring(
					endCallIndex + offset).trim();

			int statementLength = trimmedStatement.length();

			for (int i = 0; i < statementLength; i++) {
				char c = trimmedStatement.charAt(i);

				if (Character.isWhitespace(c) || (c == '(') || (c == '?')) {
					break;
				}
				nameBuf.append(c);

			}

			return nameBuf.toString();
		}
		
		throw SQLError.createSQLException(Messages.getString("CallableStatement.1"), //$NON-NLS-1$
				SQLError.SQL_STATE_GENERAL_ERROR);
	}

	/**
	 * Adds 'at' symbol to beginning of parameter names if needed.
	 * 
	 * @param paramNameIn
	 *            the parameter name to 'fix'
	 * 
	 * @return the parameter name with an 'a' prepended, if needed
	 * 
	 * @throws SQLException
	 *             if the parameter name is null or empty.
	 */
	private String fixParameterName(String paramNameIn) throws SQLException {
		if ((paramNameIn == null) || (paramNameIn.length() == 0)) {
			throw SQLError.createSQLException(
					((Messages.getString("CallableStatement.0") + paramNameIn) == null) //$NON-NLS-1$
							? Messages.getString("CallableStatement.15") : Messages.getString("CallableStatement.16"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (this.connection.getNoAccessToProcedureBodies()) {
			throw SQLError.createSQLException("No access to parameters by name when connection has been configured not to access procedure bodies",
					SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
		}
		
		return mangleParameterName(paramNameIn);

		/*
		 * if (paramNameIn.startsWith("@")) { return paramNameIn; } else {
		 * StringBuffer paramNameBuf = new StringBuffer("@");
		 * paramNameBuf.append(paramNameIn);
		 * 
		 * return paramNameBuf.toString(); }
		 */
	}

	/**
	 * @see java.sql.CallableStatement#getArray(int)
	 */
	public synchronized Array getArray(int i) throws SQLException {
		ResultSet rs = getOutputParameters(i);

		Array retValue = rs.getArray(mapOutputParameterIndexToRsIndex(i));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getArray(java.lang.String)
	 */
	public synchronized Array getArray(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Array retValue = rs.getArray(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getBigDecimal(int)
	 */
	public synchronized BigDecimal getBigDecimal(int parameterIndex)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		BigDecimal retValue = rs
				.getBigDecimal(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param parameterIndex
	 *            DOCUMENT ME!
	 * @param scale
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 * 
	 * @see java.sql.CallableStatement#getBigDecimal(int, int)
	 * @deprecated
	 */
	public synchronized BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		BigDecimal retValue = rs.getBigDecimal(
				mapOutputParameterIndexToRsIndex(parameterIndex), scale);

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
	 */
	public synchronized BigDecimal getBigDecimal(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		BigDecimal retValue = rs.getBigDecimal(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getBlob(int)
	 */
	public synchronized Blob getBlob(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Blob retValue = rs
				.getBlob(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getBlob(java.lang.String)
	 */
	public synchronized Blob getBlob(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Blob retValue = rs.getBlob(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getBoolean(int)
	 */
	public synchronized boolean getBoolean(int parameterIndex)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		boolean retValue = rs
				.getBoolean(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getBoolean(java.lang.String)
	 */
	public synchronized boolean getBoolean(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		boolean retValue = rs.getBoolean(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getByte(int)
	 */
	public synchronized byte getByte(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		byte retValue = rs
				.getByte(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getByte(java.lang.String)
	 */
	public synchronized byte getByte(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		byte retValue = rs.getByte(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getBytes(int)
	 */
	public synchronized byte[] getBytes(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		byte[] retValue = rs
				.getBytes(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getBytes(java.lang.String)
	 */
	public synchronized byte[] getBytes(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		byte[] retValue = rs.getBytes(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getClob(int)
	 */
	public synchronized Clob getClob(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Clob retValue = rs
				.getClob(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getClob(java.lang.String)
	 */
	public synchronized Clob getClob(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Clob retValue = rs.getClob(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getDate(int)
	 */
	public synchronized Date getDate(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Date retValue = rs
				.getDate(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
	 */
	public synchronized Date getDate(int parameterIndex, Calendar cal)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Date retValue = rs.getDate(
				mapOutputParameterIndexToRsIndex(parameterIndex), cal);

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getDate(java.lang.String)
	 */
	public synchronized Date getDate(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Date retValue = rs.getDate(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getDate(java.lang.String,
	 *      java.util.Calendar)
	 */
	public synchronized Date getDate(String parameterName, Calendar cal)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Date retValue = rs.getDate(fixParameterName(parameterName), cal);

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getDouble(int)
	 */
	public synchronized double getDouble(int parameterIndex)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		double retValue = rs
				.getDouble(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getDouble(java.lang.String)
	 */
	public synchronized double getDouble(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		double retValue = rs.getDouble(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getFloat(int)
	 */
	public synchronized float getFloat(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		float retValue = rs
				.getFloat(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getFloat(java.lang.String)
	 */
	public synchronized float getFloat(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		float retValue = rs.getFloat(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getInt(int)
	 */
	public synchronized int getInt(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		int retValue = rs
				.getInt(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getInt(java.lang.String)
	 */
	public synchronized int getInt(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		int retValue = rs.getInt(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getLong(int)
	 */
	public synchronized long getLong(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		long retValue = rs
				.getLong(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getLong(java.lang.String)
	 */
	public synchronized long getLong(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		long retValue = rs.getLong(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	private int getNamedParamIndex(String paramName, boolean forOut)
	throws SQLException {
		if (this.connection.getNoAccessToProcedureBodies()) {
			throw SQLError.createSQLException("No access to parameters by name when connection has been configured not to access procedure bodies",
					SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
		}
		
		if ((paramName == null) || (paramName.length() == 0)) {
			throw SQLError.createSQLException(Messages.getString("CallableStatement.2"), //$NON-NLS-1$
					SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
		}

		CallableStatementParam namedParamInfo = this.paramInfo
		.getParameter(paramName);

		if (this.paramInfo == null) {
			throw SQLError.createSQLException(
					Messages.getString("CallableStatement.3") + paramName + Messages.getString("CallableStatement.4"), //$NON-NLS-1$ //$NON-NLS-2$
					SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
		}

		if (forOut && !namedParamInfo.isOut) {
			throw SQLError.createSQLException(
					Messages.getString("CallableStatement.5") + paramName //$NON-NLS-1$
					+ Messages.getString("CallableStatement.6"), //$NON-NLS-1$
					SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
		}


		if (this.placeholderToParameterIndexMap == null) {
			return namedParamInfo.index + 1; // JDBC indices are 1-based
		} 

		for (int i = 0; i < this.placeholderToParameterIndexMap.length; i++) {
			if (this.placeholderToParameterIndexMap[i] == namedParamInfo.index) {
				return i + 1;
			}
		}

		throw SQLError.createSQLException("Can't find local placeholder mapping for parameter named \"" + 
				paramName + "\".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
	}

	/**
	 * @see java.sql.CallableStatement#getObject(int)
	 */
	public synchronized Object getObject(int parameterIndex)
			throws SQLException {
		CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);

		ResultSet rs = getOutputParameters(parameterIndex);

		Object retVal = rs.getObjectStoredProc(
				mapOutputParameterIndexToRsIndex(parameterIndex),
				paramDescriptor.desiredJdbcType);

		this.outputParamWasNull = rs.wasNull();

		return retVal;
	}

	/**
	 * @see java.sql.CallableStatement#getObject(int, java.util.Map)
	 */
	public synchronized Object getObject(int parameterIndex, Map map)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Object retVal = rs.getObject(
				mapOutputParameterIndexToRsIndex(parameterIndex), map);

		this.outputParamWasNull = rs.wasNull();

		return retVal;
	}

	/**
	 * @see java.sql.CallableStatement#getObject(java.lang.String)
	 */
	public synchronized Object getObject(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Object retValue = rs.getObject(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getObject(java.lang.String,
	 *      java.util.Map)
	 */
	public synchronized Object getObject(String parameterName, Map map)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Object retValue = rs.getObject(fixParameterName(parameterName), map);

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * Returns the ResultSet that holds the output parameters, or throws an
	 * appropriate exception if none exist, or they weren't returned.
	 * 
	 * @return the ResultSet that holds the output parameters
	 * 
	 * @throws SQLException
	 *             if no output parameters were defined, or if no output
	 *             parameters were returned.
	 */
	private ResultSet getOutputParameters(int paramIndex) throws SQLException {
		this.outputParamWasNull = false;

		if (paramIndex == 1 && this.callingStoredFunction
				&& this.returnValueParam != null) {
			return this.functionReturnValueResults;
		}

		if (this.outputParameterResults == null) {
			if (this.paramInfo.numberOfParameters() == 0) {
				throw SQLError.createSQLException(Messages
						.getString("CallableStatement.7"), //$NON-NLS-1$
						SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
			}
			throw SQLError.createSQLException(Messages.getString("CallableStatement.8"), //$NON-NLS-1$
					SQLError.SQL_STATE_GENERAL_ERROR);
		}

		return this.outputParameterResults;

	}

	public synchronized ParameterMetaData getParameterMetaData()
			throws SQLException {
		if (this.placeholderToParameterIndexMap == null) {
			return (CallableStatementParamInfoJDBC3) this.paramInfo;
		} else {
			return new CallableStatementParamInfoJDBC3(this.paramInfo);
		}
	}

	/**
	 * @see java.sql.CallableStatement#getRef(int)
	 */
	public synchronized Ref getRef(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Ref retValue = rs
				.getRef(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getRef(java.lang.String)
	 */
	public synchronized Ref getRef(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Ref retValue = rs.getRef(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getShort(int)
	 */
	public synchronized short getShort(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		short retValue = rs
				.getShort(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getShort(java.lang.String)
	 */
	public synchronized short getShort(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		short retValue = rs.getShort(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getString(int)
	 */
	public synchronized String getString(int parameterIndex)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		String retValue = rs
				.getString(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getString(java.lang.String)
	 */
	public synchronized String getString(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		String retValue = rs.getString(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getTime(int)
	 */
	public synchronized Time getTime(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Time retValue = rs
				.getTime(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
	 */
	public synchronized Time getTime(int parameterIndex, Calendar cal)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Time retValue = rs.getTime(
				mapOutputParameterIndexToRsIndex(parameterIndex), cal);

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getTime(java.lang.String)
	 */
	public synchronized Time getTime(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Time retValue = rs.getTime(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getTime(java.lang.String,
	 *      java.util.Calendar)
	 */
	public synchronized Time getTime(String parameterName, Calendar cal)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Time retValue = rs.getTime(fixParameterName(parameterName), cal);

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getTimestamp(int)
	 */
	public synchronized Timestamp getTimestamp(int parameterIndex)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Timestamp retValue = rs
				.getTimestamp(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
	 */
	public synchronized Timestamp getTimestamp(int parameterIndex, Calendar cal)
			throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		Timestamp retValue = rs.getTimestamp(
				mapOutputParameterIndexToRsIndex(parameterIndex), cal);

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
	 */
	public synchronized Timestamp getTimestamp(String parameterName)
			throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Timestamp retValue = rs.getTimestamp(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getTimestamp(java.lang.String,
	 *      java.util.Calendar)
	 */
	public synchronized Timestamp getTimestamp(String parameterName,
			Calendar cal) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		Timestamp retValue = rs.getTimestamp(fixParameterName(parameterName),
				cal);

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getURL(int)
	 */
	public synchronized URL getURL(int parameterIndex) throws SQLException {
		ResultSet rs = getOutputParameters(parameterIndex);

		URL retValue = rs
				.getURL(mapOutputParameterIndexToRsIndex(parameterIndex));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	/**
	 * @see java.sql.CallableStatement#getURL(java.lang.String)
	 */
	public synchronized URL getURL(String parameterName) throws SQLException {
		ResultSet rs = getOutputParameters(0); // definitely not going to be
		// from ?=

		URL retValue = rs.getURL(fixParameterName(parameterName));

		this.outputParamWasNull = rs.wasNull();

		return retValue;
	}

	private int mapOutputParameterIndexToRsIndex(int paramIndex)
			throws SQLException {

		if (this.returnValueParam != null && paramIndex == 1) {
			return 1;
		}

		checkParameterIndexBounds(paramIndex);

		int localParamIndex = paramIndex - 1;

		if (this.placeholderToParameterIndexMap != null) {
			localParamIndex = this.placeholderToParameterIndexMap[localParamIndex];
		}

		int rsIndex = this.parameterIndexToRsIndex[localParamIndex];

		if (rsIndex == NOT_OUTPUT_PARAMETER_INDICATOR) {
			throw SQLError.createSQLException(
					Messages.getString("CallableStatement.21") + paramIndex //$NON-NLS-1$
							+ Messages.getString("CallableStatement.22"), //$NON-NLS-1$
					SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
		}

		return rsIndex + 1;
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(int, int)
	 */
	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException {
		CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);
		paramDescriptor.desiredJdbcType = sqlType;
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
	 */
	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException {
		registerOutParameter(parameterIndex, sqlType);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(int, int,
	 *      java.lang.String)
	 */
	public void registerOutParameter(int parameterIndex, int sqlType,
			String typeName) throws SQLException {
		checkIsOutputParam(parameterIndex);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      int)
	 */
	public synchronized void registerOutParameter(String parameterName,
			int sqlType) throws SQLException {
		registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      int, int)
	 */
	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException {
		registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
	}

	/**
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      int, java.lang.String)
	 */
	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException {
		registerOutParameter(getNamedParamIndex(parameterName, true), sqlType,
				typeName);
	}

	/**
	 * Issues a second query to retrieve all output parameters.
	 * 
	 * @throws SQLException
	 *             if an error occurs.
	 */
	private void retrieveOutParams() throws SQLException {
		int numParameters = this.paramInfo.numberOfParameters();

		this.parameterIndexToRsIndex = new int[numParameters];

		for (int i = 0; i < numParameters; i++) {
			this.parameterIndexToRsIndex[i] = NOT_OUTPUT_PARAMETER_INDICATOR;
		}

		int localParamIndex = 0;

		if (numParameters > 0) {
			StringBuffer outParameterQuery = new StringBuffer("SELECT "); //$NON-NLS-1$

			boolean firstParam = true;
			boolean hadOutputParams = false;

			for (Iterator paramIter = this.paramInfo.iterator(); paramIter
					.hasNext();) {
				CallableStatementParam retrParamInfo = (CallableStatementParam) paramIter
						.next();

				if (retrParamInfo.isOut) {
					hadOutputParams = true;

					this.parameterIndexToRsIndex[retrParamInfo.index] = localParamIndex++;

					String outParameterName = mangleParameterName(retrParamInfo.paramName);

					if (!firstParam) {
						outParameterQuery.append(","); //$NON-NLS-1$
					} else {
						firstParam = false;
					}

					if (!outParameterName.startsWith("@")) { //$NON-NLS-1$
						outParameterQuery.append('@');
					}

					outParameterQuery.append(outParameterName);
				}
			}

			if (hadOutputParams) {
				// We can't use 'ourself' to execute this query, or any
				// pending result sets would be overwritten
				java.sql.Statement outParameterStmt = null;
				java.sql.ResultSet outParamRs = null;

				try {
					outParameterStmt = this.connection.createStatement();
					outParamRs = outParameterStmt
							.executeQuery(outParameterQuery.toString());
					this.outputParameterResults = ((com.mysql.jdbc.ResultSet) outParamRs)
							.copy();

					if (!this.outputParameterResults.next()) {
						this.outputParameterResults.close();
						this.outputParameterResults = null;
					}
				} finally {
					if (outParameterStmt != null) {
						outParameterStmt.close();
					}
				}
			} else {
				this.outputParameterResults = null;
			}
		} else {
			this.outputParameterResults = null;
		}
	}

	/**
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
	 *      java.io.InputStream, int)
	 */
	public void setAsciiStream(String parameterName, InputStream x, int length)
			throws SQLException {
		setAsciiStream(getNamedParamIndex(parameterName, false), x, length);
	}

	/**
	 * @see java.sql.CallableStatement#setBigDecimal(java.lang.String,
	 *      java.math.BigDecimal)
	 */
	public void setBigDecimal(String parameterName, BigDecimal x)
			throws SQLException {
		setBigDecimal(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
	 *      java.io.InputStream, int)
	 */
	public void setBinaryStream(String parameterName, InputStream x, int length)
			throws SQLException {
		setBinaryStream(getNamedParamIndex(parameterName, false), x, length);
	}

	/**
	 * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
	 */
	public void setBoolean(String parameterName, boolean x) throws SQLException {
		setBoolean(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
	 */
	public void setByte(String parameterName, byte x) throws SQLException {
		setByte(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
	 */
	public void setBytes(String parameterName, byte[] x) throws SQLException {
		setBytes(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
	 *      java.io.Reader, int)
	 */
	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException {
		setCharacterStream(getNamedParamIndex(parameterName, false), reader,
				length);
	}

	/**
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
	 */
	public void setDate(String parameterName, Date x) throws SQLException {
		setDate(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date,
	 *      java.util.Calendar)
	 */
	public void setDate(String parameterName, Date x, Calendar cal)
			throws SQLException {
		setDate(getNamedParamIndex(parameterName, false), x, cal);
	}

	/**
	 * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
	 */
	public void setDouble(String parameterName, double x) throws SQLException {
		setDouble(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
	 */
	public void setFloat(String parameterName, float x) throws SQLException {
		setFloat(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * 
	 */
	private void setInOutParamsOnServer() throws SQLException {
		if (this.paramInfo.numParameters > 0) {
			int parameterIndex = 0;

			for (Iterator paramIter = this.paramInfo.iterator(); paramIter
					.hasNext();) {

				CallableStatementParam inParamInfo = (CallableStatementParam) paramIter
						.next();

				if (inParamInfo.isOut && inParamInfo.isIn) {
					String inOutParameterName = mangleParameterName(inParamInfo.paramName);
					StringBuffer queryBuf = new StringBuffer(
							4 + inOutParameterName.length() + 1 + 1);
					queryBuf.append("SET "); //$NON-NLS-1$
					queryBuf.append(inOutParameterName);
					queryBuf.append("=?"); //$NON-NLS-1$

					PreparedStatement setPstmt = null;

					try {
						setPstmt = this.connection
								.clientPrepareStatement(queryBuf.toString());

						byte[] parameterAsBytes = getBytesRepresentation(
								inParamInfo.index);

						if (parameterAsBytes != null) {
							if (parameterAsBytes.length > 8
									&& parameterAsBytes[0] == '_'
									&& parameterAsBytes[1] == 'b'
									&& parameterAsBytes[2] == 'i'
									&& parameterAsBytes[3] == 'n'
									&& parameterAsBytes[4] == 'a'
									&& parameterAsBytes[5] == 'r'
									&& parameterAsBytes[6] == 'y'
									&& parameterAsBytes[7] == '\'') {
								setPstmt.setBytesNoEscapeNoQuotes(1,
										parameterAsBytes);
							} else {
								int sqlType = inParamInfo.desiredJdbcType;
								
								switch (sqlType) {
								case Types.BIT:
								case Types.BINARY: 
								case Types.BLOB: 
								case Types.JAVA_OBJECT:
								case Types.LONGVARBINARY: 
								case Types.VARBINARY:
									setPstmt.setBytes(1, parameterAsBytes);
									break;
								default:
									// the inherited PreparedStatement methods
									// have already escaped and quoted these parameters
									setPstmt.setBytesNoEscape(1, parameterAsBytes); 
								}
							}
						} else {
							setPstmt.setNull(1, Types.NULL);
						}

						setPstmt.executeUpdate();
					} finally {
						if (setPstmt != null) {
							setPstmt.close();
						}
					}
				}

				parameterIndex++;
			}
		}
	}

	/**
	 * @see java.sql.CallableStatement#setInt(java.lang.String, int)
	 */
	public void setInt(String parameterName, int x) throws SQLException {
		setInt(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setLong(java.lang.String, long)
	 */
	public void setLong(String parameterName, long x) throws SQLException {
		setLong(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int)
	 */
	public void setNull(String parameterName, int sqlType) throws SQLException {
		setNull(getNamedParamIndex(parameterName, false), sqlType);
	}

	/**
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int,
	 *      java.lang.String)
	 */
	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {
		setNull(getNamedParamIndex(parameterName, false), sqlType, typeName);
	}

	/**
	 * @see java.sql.CallableStatement#setObject(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setObject(String parameterName, Object x) throws SQLException {
		setObject(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setObject(java.lang.String,
	 *      java.lang.Object, int)
	 */
	public void setObject(String parameterName, Object x, int targetSqlType)
			throws SQLException {
		setObject(getNamedParamIndex(parameterName, false), x, targetSqlType);
	}

	/**
	 * @see java.sql.CallableStatement#setObject(java.lang.String,
	 *      java.lang.Object, int, int)
	 */
	public void setObject(String parameterName, Object x, int targetSqlType,
			int scale) throws SQLException {
	}

	private void setOutParams() throws SQLException {
		if (this.paramInfo.numParameters > 0) {
			for (Iterator paramIter = this.paramInfo.iterator(); paramIter
					.hasNext();) {
				CallableStatementParam outParamInfo = (CallableStatementParam) paramIter
						.next();

				if (!this.callingStoredFunction && outParamInfo.isOut) {
					String outParameterName = mangleParameterName(outParamInfo.paramName);

					int outParamIndex;
					
					if (this.placeholderToParameterIndexMap == null) { 
							outParamIndex = outParamInfo.index + 1;
					} else {
							outParamIndex = this.placeholderToParameterIndexMap[outParamInfo.index - 1 /* JDBC is 1-based */];
					}
					
					this.setBytesNoEscapeNoQuotes(outParamIndex,
							StringUtils.getBytes(outParameterName,
									this.charConverter, this.charEncoding,
									this.connection
											.getServerCharacterEncoding(),
									this.connection.parserKnowsUnicode()));
				}
			}
		}
	}

	/**
	 * @see java.sql.CallableStatement#setShort(java.lang.String, short)
	 */
	public void setShort(String parameterName, short x) throws SQLException {
		setShort(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setString(java.lang.String,
	 *      java.lang.String)
	 */
	public void setString(String parameterName, String x) throws SQLException {
		setString(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
	 */
	public void setTime(String parameterName, Time x) throws SQLException {
		setTime(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time,
	 *      java.util.Calendar)
	 */
	public void setTime(String parameterName, Time x, Calendar cal)
			throws SQLException {
		setTime(getNamedParamIndex(parameterName, false), x, cal);
	}

	/**
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
	 *      java.sql.Timestamp)
	 */
	public void setTimestamp(String parameterName, Timestamp x)
			throws SQLException {
		setTimestamp(getNamedParamIndex(parameterName, false), x);
	}

	/**
	 * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
	 *      java.sql.Timestamp, java.util.Calendar)
	 */
	public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
			throws SQLException {
		setTimestamp(getNamedParamIndex(parameterName, false), x, cal);
	}

	/**
	 * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
	 */
	public void setURL(String parameterName, URL val) throws SQLException {
		setURL(getNamedParamIndex(parameterName, false), val);
	}

	/**
	 * @see java.sql.CallableStatement#wasNull()
	 */
	public synchronized boolean wasNull() throws SQLException {
		return this.outputParamWasNull;
	}

	public int[] executeBatch() throws SQLException {
		if (this.hasOutputParams) {
			throw SQLError.createSQLException("Can't call executeBatch() on CallableStatement with OUTPUT parameters",
					SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
		}
		
		return super.executeBatch();
	}

	protected int getParameterIndexOffset() {
		if (this.callingStoredFunction) {
			return -1;
		}
		
		return super.getParameterIndexOffset();
	}
}

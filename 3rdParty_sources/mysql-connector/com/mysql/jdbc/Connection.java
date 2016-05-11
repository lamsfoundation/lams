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

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.LogFactory;
import com.mysql.jdbc.log.NullLogger;
import com.mysql.jdbc.profiler.ProfileEventSink;
import com.mysql.jdbc.profiler.ProfilerEvent;
import com.mysql.jdbc.util.LRUCache;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import java.net.URL;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Time;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TreeMap;

/**
 * A Connection represents a session with a specific database. Within the
 * context of a Connection, SQL statements are executed and results are
 * returned.
 * <P>
 * A Connection's database is able to provide information describing its tables,
 * its supported SQL grammar, its stored procedures, the capabilities of this
 * connection, etc. This information is obtained with the getMetaData method.
 * </p>
 * 
 * @author Mark Matthews
 *
 * @see java.sql.Connection
 */
public class Connection extends ConnectionProperties implements
		java.sql.Connection {
	private static final String JDBC_LOCAL_CHARACTER_SET_RESULTS = "jdbc.local.character_set_results";

	/**
	 * Used as a key for caching callable statements which (may) depend on
	 * current catalog...In 5.0.x, they don't (currently), but stored procedure
	 * names soon will, so current catalog is a (hidden) component of the name.
	 */
	class CompoundCacheKey {
		String componentOne;

		String componentTwo;

		int hashCode;

		CompoundCacheKey(String partOne, String partTwo) {
			this.componentOne = partOne;
			this.componentTwo = partTwo;

			// Handle first component (in most cases, currentCatalog)
			// being NULL....
			this.hashCode = (((this.componentOne != null) ? this.componentOne
					: "") + this.componentTwo).hashCode();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (obj instanceof CompoundCacheKey) {
				CompoundCacheKey another = (CompoundCacheKey) obj;

				boolean firstPartEqual = false;

				if (this.componentOne == null) {
					firstPartEqual = (another.componentOne == null);
				} else {
					firstPartEqual = this.componentOne
							.equals(another.componentOne);
				}

				return (firstPartEqual && this.componentTwo
						.equals(another.componentTwo));
			}

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return this.hashCode;
		}
	}

	/**
	 * Wrapper class for UltraDev CallableStatements that are really
	 * PreparedStatments. Nice going, UltraDev developers.
	 */
	class UltraDevWorkAround implements java.sql.CallableStatement {
		private java.sql.PreparedStatement delegate = null;

		UltraDevWorkAround(java.sql.PreparedStatement pstmt) {
			this.delegate = pstmt;
		}

		public void addBatch() throws SQLException {
			this.delegate.addBatch();
		}

		public void addBatch(java.lang.String p1) throws SQLException {
			this.delegate.addBatch(p1);
		}

		public void cancel() throws SQLException {
			this.delegate.cancel();
		}

		public void clearBatch() throws SQLException {
			this.delegate.clearBatch();
		}

		public void clearParameters() throws SQLException {
			this.delegate.clearParameters();
		}

		public void clearWarnings() throws SQLException {
			this.delegate.clearWarnings();
		}

		public void close() throws SQLException {
			this.delegate.close();
		}

		public boolean execute() throws SQLException {
			return this.delegate.execute();
		}

		public boolean execute(java.lang.String p1) throws SQLException {
			return this.delegate.execute(p1);
		}

		/**
		 * @see Statement#execute(String, int)
		 */
		public boolean execute(String arg0, int arg1) throws SQLException {
			return this.delegate.execute(arg0, arg1);
		}

		/**
		 * @see Statement#execute(String, int[])
		 */
		public boolean execute(String arg0, int[] arg1) throws SQLException {
			return this.delegate.execute(arg0, arg1);
		}

		/**
		 * @see Statement#execute(String, String[])
		 */
		public boolean execute(String arg0, String[] arg1) throws SQLException {
			return this.delegate.execute(arg0, arg1);
		}

		public int[] executeBatch() throws SQLException {
			return this.delegate.executeBatch();
		}

		public java.sql.ResultSet executeQuery() throws SQLException {
			return this.delegate.executeQuery();
		}

		public java.sql.ResultSet executeQuery(java.lang.String p1)
				throws SQLException {
			return this.delegate.executeQuery(p1);
		}

		public int executeUpdate() throws SQLException {
			return this.delegate.executeUpdate();
		}

		public int executeUpdate(java.lang.String p1) throws SQLException {
			return this.delegate.executeUpdate(p1);
		}

		/**
		 * @see Statement#executeUpdate(String, int)
		 */
		public int executeUpdate(String arg0, int arg1) throws SQLException {
			return this.delegate.executeUpdate(arg0, arg1);
		}

		/**
		 * @see Statement#executeUpdate(String, int[])
		 */
		public int executeUpdate(String arg0, int[] arg1) throws SQLException {
			return this.delegate.executeUpdate(arg0, arg1);
		}

		/**
		 * @see Statement#executeUpdate(String, String[])
		 */
		public int executeUpdate(String arg0, String[] arg1)
				throws SQLException {
			return this.delegate.executeUpdate(arg0, arg1);
		}

		public java.sql.Array getArray(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getArray(String)
		 */
		public java.sql.Array getArray(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public java.math.BigDecimal getBigDecimal(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * DOCUMENT ME!
		 * 
		 * @param p1
		 *            DOCUMENT ME!
		 * @param p2
		 *            DOCUMENT ME!
		 * @return DOCUMENT ME!
		 * @throws SQLException
		 *             DOCUMENT ME!
		 * @deprecated
		 */
		public java.math.BigDecimal getBigDecimal(int p1, int p2)
				throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getBigDecimal(String)
		 */
		public BigDecimal getBigDecimal(String arg0) throws SQLException {
			return null;
		}

		public java.sql.Blob getBlob(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getBlob(String)
		 */
		public java.sql.Blob getBlob(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public boolean getBoolean(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getBoolean(String)
		 */
		public boolean getBoolean(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public byte getByte(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getByte(String)
		 */
		public byte getByte(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public byte[] getBytes(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getBytes(String)
		 */
		public byte[] getBytes(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public java.sql.Clob getClob(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getClob(String)
		 */
		public Clob getClob(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public java.sql.Connection getConnection() throws SQLException {
			return this.delegate.getConnection();
		}

		public java.sql.Date getDate(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public java.sql.Date getDate(int p1, final Calendar p2)
				throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getDate(String)
		 */
		public Date getDate(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#getDate(String, Calendar)
		 */
		public Date getDate(String arg0, Calendar arg1) throws SQLException {
			throw new NotImplemented();
		}

		public double getDouble(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getDouble(String)
		 */
		public double getDouble(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public int getFetchDirection() throws SQLException {
			return this.delegate.getFetchDirection();
		}

		public int getFetchSize() throws java.sql.SQLException {
			return this.delegate.getFetchSize();
		}

		public float getFloat(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getFloat(String)
		 */
		public float getFloat(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see Statement#getGeneratedKeys()
		 */
		public java.sql.ResultSet getGeneratedKeys() throws SQLException {
			return this.delegate.getGeneratedKeys();
		}

		public int getInt(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getInt(String)
		 */
		public int getInt(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public long getLong(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getLong(String)
		 */
		public long getLong(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public int getMaxFieldSize() throws SQLException {
			return this.delegate.getMaxFieldSize();
		}

		public int getMaxRows() throws SQLException {
			return this.delegate.getMaxRows();
		}

		public java.sql.ResultSetMetaData getMetaData() throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public boolean getMoreResults() throws SQLException {
			return this.delegate.getMoreResults();
		}

		/**
		 * @see Statement#getMoreResults(int)
		 */
		public boolean getMoreResults(int arg0) throws SQLException {
			return this.delegate.getMoreResults();
		}

		public java.lang.Object getObject(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public java.lang.Object getObject(int p1, final java.util.Map p2)
				throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getObject(String)
		 */
		public Object getObject(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#getObject(String, Map)
		 */
		public Object getObject(String arg0, Map arg1) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see PreparedStatement#getParameterMetaData()
		 */
		public ParameterMetaData getParameterMetaData() throws SQLException {
			return this.delegate.getParameterMetaData();
		}

		public int getQueryTimeout() throws SQLException {
			return this.delegate.getQueryTimeout();
		}

		public java.sql.Ref getRef(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getRef(String)
		 */
		public Ref getRef(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public java.sql.ResultSet getResultSet() throws SQLException {
			return this.delegate.getResultSet();
		}

		public int getResultSetConcurrency() throws SQLException {
			return this.delegate.getResultSetConcurrency();
		}

		/**
		 * @see Statement#getResultSetHoldability()
		 */
		public int getResultSetHoldability() throws SQLException {
			return this.delegate.getResultSetHoldability();
		}

		public int getResultSetType() throws SQLException {
			return this.delegate.getResultSetType();
		}

		public short getShort(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getShort(String)
		 */
		public short getShort(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public java.lang.String getString(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getString(String)
		 */
		public String getString(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public java.sql.Time getTime(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public java.sql.Time getTime(int p1, final java.util.Calendar p2)
				throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getTime(String)
		 */
		public Time getTime(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#getTime(String, Calendar)
		 */
		public Time getTime(String arg0, Calendar arg1) throws SQLException {
			throw new NotImplemented();
		}

		public java.sql.Timestamp getTimestamp(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public java.sql.Timestamp getTimestamp(int p1,
				final java.util.Calendar p2) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#getTimestamp(String)
		 */
		public Timestamp getTimestamp(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#getTimestamp(String, Calendar)
		 */
		public Timestamp getTimestamp(String arg0, Calendar arg1)
				throws SQLException {
			throw new NotImplemented();
		}

		public int getUpdateCount() throws SQLException {
			return this.delegate.getUpdateCount();
		}

		/**
		 * @see CallableStatement#getURL(int)
		 */
		public URL getURL(int arg0) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#getURL(String)
		 */
		public URL getURL(String arg0) throws SQLException {
			throw new NotImplemented();
		}

		public java.sql.SQLWarning getWarnings() throws SQLException {
			return this.delegate.getWarnings();
		}

		public void registerOutParameter(int p1, int p2) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public void registerOutParameter(int p1, int p2, int p3)
				throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public void registerOutParameter(int p1, int p2, java.lang.String p3)
				throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		/**
		 * @see CallableStatement#registerOutParameter(String, int)
		 */
		public void registerOutParameter(String arg0, int arg1)
				throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#registerOutParameter(String, int, int)
		 */
		public void registerOutParameter(String arg0, int arg1, int arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#registerOutParameter(String, int, String)
		 */
		public void registerOutParameter(String arg0, int arg1, String arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setArray(int p1, final java.sql.Array p2)
				throws SQLException {
			this.delegate.setArray(p1, p2);
		}

		public void setAsciiStream(int p1, final java.io.InputStream p2, int p3)
				throws SQLException {
			this.delegate.setAsciiStream(p1, p2, p3);
		}

		/**
		 * @see CallableStatement#setAsciiStream(String, InputStream, int)
		 */
		public void setAsciiStream(String arg0, InputStream arg1, int arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setBigDecimal(int p1, final java.math.BigDecimal p2)
				throws SQLException {
			this.delegate.setBigDecimal(p1, p2);
		}

		/**
		 * @see CallableStatement#setBigDecimal(String, BigDecimal)
		 */
		public void setBigDecimal(String arg0, BigDecimal arg1)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setBinaryStream(int p1, final java.io.InputStream p2, int p3)
				throws SQLException {
			this.delegate.setBinaryStream(p1, p2, p3);
		}

		/**
		 * @see CallableStatement#setBinaryStream(String, InputStream, int)
		 */
		public void setBinaryStream(String arg0, InputStream arg1, int arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setBlob(int p1, final java.sql.Blob p2) throws SQLException {
			this.delegate.setBlob(p1, p2);
		}

		public void setBoolean(int p1, boolean p2) throws SQLException {
			this.delegate.setBoolean(p1, p2);
		}

		/**
		 * @see CallableStatement#setBoolean(String, boolean)
		 */
		public void setBoolean(String arg0, boolean arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setByte(int p1, byte p2) throws SQLException {
			this.delegate.setByte(p1, p2);
		}

		/**
		 * @see CallableStatement#setByte(String, byte)
		 */
		public void setByte(String arg0, byte arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setBytes(int p1, byte[] p2) throws SQLException {
			this.delegate.setBytes(p1, p2);
		}

		/**
		 * @see CallableStatement#setBytes(String, byte[])
		 */
		public void setBytes(String arg0, byte[] arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setCharacterStream(int p1, final java.io.Reader p2, int p3)
				throws SQLException {
			this.delegate.setCharacterStream(p1, p2, p3);
		}

		/**
		 * @see CallableStatement#setCharacterStream(String, Reader, int)
		 */
		public void setCharacterStream(String arg0, Reader arg1, int arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setClob(int p1, final java.sql.Clob p2) throws SQLException {
			this.delegate.setClob(p1, p2);
		}

		public void setCursorName(java.lang.String p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public void setDate(int p1, final java.sql.Date p2) throws SQLException {
			this.delegate.setDate(p1, p2);
		}

		public void setDate(int p1, final java.sql.Date p2,
				final java.util.Calendar p3) throws SQLException {
			this.delegate.setDate(p1, p2, p3);
		}

		/**
		 * @see CallableStatement#setDate(String, Date)
		 */
		public void setDate(String arg0, Date arg1) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#setDate(String, Date, Calendar)
		 */
		public void setDate(String arg0, Date arg1, Calendar arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setDouble(int p1, double p2) throws SQLException {
			this.delegate.setDouble(p1, p2);
		}

		/**
		 * @see CallableStatement#setDouble(String, double)
		 */
		public void setDouble(String arg0, double arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setEscapeProcessing(boolean p1) throws SQLException {
			this.delegate.setEscapeProcessing(p1);
		}

		public void setFetchDirection(int p1) throws SQLException {
			this.delegate.setFetchDirection(p1);
		}

		public void setFetchSize(int p1) throws SQLException {
			this.delegate.setFetchSize(p1);
		}

		public void setFloat(int p1, float p2) throws SQLException {
			this.delegate.setFloat(p1, p2);
		}

		/**
		 * @see CallableStatement#setFloat(String, float)
		 */
		public void setFloat(String arg0, float arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setInt(int p1, int p2) throws SQLException {
			this.delegate.setInt(p1, p2);
		}

		/**
		 * @see CallableStatement#setInt(String, int)
		 */
		public void setInt(String arg0, int arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setLong(int p1, long p2) throws SQLException {
			this.delegate.setLong(p1, p2);
		}

		/**
		 * @see CallableStatement#setLong(String, long)
		 */
		public void setLong(String arg0, long arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setMaxFieldSize(int p1) throws SQLException {
			this.delegate.setMaxFieldSize(p1);
		}

		public void setMaxRows(int p1) throws SQLException {
			this.delegate.setMaxRows(p1);
		}

		public void setNull(int p1, int p2) throws SQLException {
			this.delegate.setNull(p1, p2);
		}

		public void setNull(int p1, int p2, java.lang.String p3)
				throws SQLException {
			this.delegate.setNull(p1, p2, p3);
		}

		/**
		 * @see CallableStatement#setNull(String, int)
		 */
		public void setNull(String arg0, int arg1) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#setNull(String, int, String)
		 */
		public void setNull(String arg0, int arg1, String arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setObject(int p1, final java.lang.Object p2)
				throws SQLException {
			this.delegate.setObject(p1, p2);
		}

		public void setObject(int p1, final java.lang.Object p2, int p3)
				throws SQLException {
			this.delegate.setObject(p1, p2, p3);
		}

		public void setObject(int p1, final java.lang.Object p2, int p3, int p4)
				throws SQLException {
			this.delegate.setObject(p1, p2, p3, p4);
		}

		/**
		 * @see CallableStatement#setObject(String, Object)
		 */
		public void setObject(String arg0, Object arg1) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#setObject(String, Object, int)
		 */
		public void setObject(String arg0, Object arg1, int arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#setObject(String, Object, int, int)
		 */
		public void setObject(String arg0, Object arg1, int arg2, int arg3)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setQueryTimeout(int p1) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public void setRef(int p1, final Ref p2) throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}

		public void setShort(int p1, short p2) throws SQLException {
			this.delegate.setShort(p1, p2);
		}

		/**
		 * @see CallableStatement#setShort(String, short)
		 */
		public void setShort(String arg0, short arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setString(int p1, java.lang.String p2)
				throws java.sql.SQLException {
			this.delegate.setString(p1, p2);
		}

		/**
		 * @see CallableStatement#setString(String, String)
		 */
		public void setString(String arg0, String arg1) throws SQLException {
			throw new NotImplemented();
		}

		public void setTime(int p1, final java.sql.Time p2) throws SQLException {
			this.delegate.setTime(p1, p2);
		}

		public void setTime(int p1, final java.sql.Time p2,
				final java.util.Calendar p3) throws SQLException {
			this.delegate.setTime(p1, p2, p3);
		}

		/**
		 * @see CallableStatement#setTime(String, Time)
		 */
		public void setTime(String arg0, Time arg1) throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#setTime(String, Time, Calendar)
		 */
		public void setTime(String arg0, Time arg1, Calendar arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		public void setTimestamp(int p1, final java.sql.Timestamp p2)
				throws SQLException {
			this.delegate.setTimestamp(p1, p2);
		}

		public void setTimestamp(int p1, final java.sql.Timestamp p2,
				final java.util.Calendar p3) throws SQLException {
			this.delegate.setTimestamp(p1, p2, p3);
		}

		/**
		 * @see CallableStatement#setTimestamp(String, Timestamp)
		 */
		public void setTimestamp(String arg0, Timestamp arg1)
				throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * @see CallableStatement#setTimestamp(String, Timestamp, Calendar)
		 */
		public void setTimestamp(String arg0, Timestamp arg1, Calendar arg2)
				throws SQLException {
			throw new NotImplemented();
		}

		/**
		 * DOCUMENT ME!
		 * 
		 * @param p1
		 *            DOCUMENT ME!
		 * @param p2
		 *            DOCUMENT ME!
		 * @param p3
		 *            DOCUMENT ME!
		 * @throws SQLException
		 *             DOCUMENT ME!
		 * @deprecated
		 */
		public void setUnicodeStream(int p1, final java.io.InputStream p2,
				int p3) throws SQLException {
			this.delegate.setUnicodeStream(p1, p2, p3);
		}

		/**
		 * @see PreparedStatement#setURL(int, URL)
		 */
		public void setURL(int arg0, URL arg1) throws SQLException {
			this.delegate.setURL(arg0, arg1);
		}

		/**
		 * @see CallableStatement#setURL(String, URL)
		 */
		public void setURL(String arg0, URL arg1) throws SQLException {
			throw new NotImplemented();
		}

		public boolean wasNull() throws SQLException {
			throw SQLError.createSQLException("Not supported");
		}
	}

	/**
	 * Marker for character set converter not being available (not written,
	 * multibyte, etc) Used to prevent multiple instantiation requests.
	 */
	private static final Object CHARSET_CONVERTER_NOT_AVAILABLE_MARKER = new Object();

	/**
	 * The mapping between MySQL charset names and Java charset names.
	 * Initialized by loadCharacterSetMapping()
	 */
	public static Map charsetMap;

	/** Default logger class name */
	protected static final String DEFAULT_LOGGER_CLASS = "com.mysql.jdbc.log.StandardLogger";

	private final static int HISTOGRAM_BUCKETS = 20;

	/** Logger instance name */
	private static final String LOGGER_INSTANCE_NAME = "MySQL";

	/**
	 * Map mysql transaction isolation level name to
	 * java.sql.Connection.TRANSACTION_XXX
	 */
	private static Map mapTransIsolationNameToValue = null;

	/** Null logger shared by all connections at startup */
	private static final Log NULL_LOGGER = new NullLogger(LOGGER_INSTANCE_NAME);

	private static Map roundRobinStatsMap;

	private static final Map serverCollationByUrl = new HashMap();

	private static final Map serverConfigByUrl = new HashMap();

	private static Timer cancelTimer;

	static {
		mapTransIsolationNameToValue = new HashMap(8);
		mapTransIsolationNameToValue.put("READ-UNCOMMITED", new Integer(
				TRANSACTION_READ_UNCOMMITTED));
		mapTransIsolationNameToValue.put("READ-UNCOMMITTED", new Integer(
				TRANSACTION_READ_UNCOMMITTED));
		mapTransIsolationNameToValue.put("READ-COMMITTED", new Integer(
				TRANSACTION_READ_COMMITTED));
		mapTransIsolationNameToValue.put("REPEATABLE-READ", new Integer(
				TRANSACTION_REPEATABLE_READ));
		mapTransIsolationNameToValue.put("SERIALIZABLE", new Integer(
				TRANSACTION_SERIALIZABLE));
		
		boolean createdNamedTimer = false;
		
		// Use reflection magic to try this on JDK's 1.5 and newer, fallback to non-named
		// timer on older VMs.
		try {
			Constructor ctr = Timer.class.getConstructor(new Class[] {String.class, Boolean.TYPE});
			
			cancelTimer = (Timer)ctr.newInstance(new Object[] { "MySQL Statement Cancellation Timer", Boolean.TRUE});
			createdNamedTimer = true;
		} catch (Throwable t) {
			createdNamedTimer = false;
		}
		
		if (!createdNamedTimer) {
			cancelTimer = new Timer(true);
		}
	}

	protected static SQLException appendMessageToException(SQLException sqlEx,
			String messageToAppend) {
		String origMessage = sqlEx.getMessage();
		String sqlState = sqlEx.getSQLState();
		int vendorErrorCode = sqlEx.getErrorCode();

		StringBuffer messageBuf = new StringBuffer(origMessage.length()
				+ messageToAppend.length());
		messageBuf.append(origMessage);
		messageBuf.append(messageToAppend);

		SQLException sqlExceptionWithNewMessage = SQLError.createSQLException(messageBuf
				.toString(), sqlState, vendorErrorCode);

		//
		// Try and maintain the original stack trace,
		// only works on JDK-1.4 and newer
		//

		try {
			// Have to do this with reflection, otherwise older JVMs croak
			Method getStackTraceMethod = null;
			Method setStackTraceMethod = null;
			Object theStackTraceAsObject = null;

			Class stackTraceElementClass = Class
					.forName("java.lang.StackTraceElement");
			Class stackTraceElementArrayClass = Array.newInstance(
					stackTraceElementClass, new int[] { 0 }).getClass();

			getStackTraceMethod = Throwable.class.getMethod("getStackTrace",
					new Class[] {});

			setStackTraceMethod = Throwable.class.getMethod("setStackTrace",
					new Class[] { stackTraceElementArrayClass });

			if (getStackTraceMethod != null && setStackTraceMethod != null) {
				theStackTraceAsObject = getStackTraceMethod.invoke(sqlEx,
						new Object[0]);
				setStackTraceMethod.invoke(sqlExceptionWithNewMessage,
						new Object[] { theStackTraceAsObject });
			}
		} catch (NoClassDefFoundError noClassDefFound) {

		} catch (NoSuchMethodException noSuchMethodEx) {

		} catch (Throwable catchAll) {

		}

		return sqlExceptionWithNewMessage;
	}

	protected static Timer getCancelTimer() {
		return cancelTimer;
	}

	private static synchronized int getNextRoundRobinHostIndex(String url,
			List hostList) {
		if (roundRobinStatsMap == null) {
			roundRobinStatsMap = new HashMap();
		}

		int[] index = (int[]) roundRobinStatsMap.get(url);

		if (index == null) {
			index = new int[1];
			index[0] = -1;

			roundRobinStatsMap.put(url, index);
		}

		index[0]++;

		if (index[0] >= hostList.size()) {
			index[0] = 0;
		}

		return index[0];
	}

	private static boolean nullSafeCompare(String s1, String s2) {
		if (s1 == null && s2 == null) {
			return true;
		}

		if (s1 == null && s2 != null) {
			return false;
		}

		return s1.equals(s2);
	}

	/** Are we in autoCommit mode? */
	private boolean autoCommit = true;

	/** A map of SQL to parsed prepared statement parameters. */
	private Map cachedPreparedStatementParams;

	/**
	 * For servers > 4.1.0, what character set is the metadata returned in?
	 */
	private String characterSetMetadata = null;

	/**
	 * The character set we want results and result metadata returned in (null ==
	 * results in any charset, metadata in UTF-8).
	 */
	private String characterSetResultsOnServer = null;

	/**
	 * Holds cached mappings to charset converters to avoid static
	 * synchronization and at the same time save memory (each charset converter
	 * takes approx 65K of static data).
	 */
	private Map charsetConverterMap = new HashMap(CharsetMapping
			.getNumberOfCharsetsConfigured());

	/**
	 * The mapping between MySQL charset names and the max number of chars in
	 * them. Lazily instantiated via getMaxBytesPerChar().
	 */
	private Map charsetToNumBytesMap;

	/** The point in time when this connection was created */
	private long connectionCreationTimeMillis = 0;

	/** ID used when profiling */
	private long connectionId;

	/** The database we're currently using (called Catalog in JDBC terms). */
	private String database = null;

	/** Internal DBMD to use for various database-version specific features */
	private DatabaseMetaData dbmd = null;

	private TimeZone defaultTimeZone;

	/** The event sink to use for profiling */
	private ProfileEventSink eventSink;

	private boolean executingFailoverReconnect = false;

	/** Are we failed-over to a non-master host */
	private boolean failedOver = false;

	/** Why was this connection implicitly closed, if known? (for diagnostics) */
	private Throwable forceClosedReason;

	/** Where was this connection implicitly closed? (for diagnostics) */
	private Throwable forcedClosedLocation;

	/** Does the server suuport isolation levels? */
	private boolean hasIsolationLevels = false;

	/** Does this version of MySQL support quoted identifiers? */
	private boolean hasQuotedIdentifiers = false;

	/** The hostname we're connected to */
	private String host = null;

	/** The list of host(s) to try and connect to */
	private List hostList = null;

	/** How many hosts are in the host list? */
	private int hostListSize = 0;

	/**
	 * We need this 'bootstrapped', because 4.1 and newer will send fields back
	 * with this even before we fill this dynamically from the server.
	 */
	private String[] indexToCharsetMapping = CharsetMapping.INDEX_TO_CHARSET;

	/** The I/O abstraction interface (network conn to MySQL server */
	private MysqlIO io = null;
	
	private boolean isClientTzUTC = false;
	
	/** Has this connection been closed? */
	private boolean isClosed = true;

	/** Is this connection associated with a global tx? */
	private boolean isInGlobalTx = false;

	/** Is this connection running inside a JDK-1.3 VM? */
	private boolean isRunningOnJDK13 = false;

	/** isolation level */
	private int isolationLevel = java.sql.Connection.TRANSACTION_READ_COMMITTED;

	private boolean isServerTzUTC = false;

	/** When did the last query finish? */
	private long lastQueryFinishedTime = 0;

	/** The logger we're going to use */
	private Log log = NULL_LOGGER;

	/**
	 * If gathering metrics, what was the execution time of the longest query so
	 * far ?
	 */
	private long longestQueryTimeMs = 0;

	/** Is the server configured to use lower-case table names only? */
	private boolean lowerCaseTableNames = false;

	/** When did the master fail? */
	private long masterFailTimeMillis = 0L;

	/**
	 * The largest packet we can send (changed once we know what the server
	 * supports, we get this at connection init).
	 */
	private int maxAllowedPacket = 65536;

	private long maximumNumberTablesAccessed = 0;

	/** Has the max-rows setting been changed from the default? */
	private boolean maxRowsChanged = false;

	/** When was the last time we reported metrics? */
	private long metricsLastReportedMs;

	private long minimumNumberTablesAccessed = Long.MAX_VALUE;

	/** Mutex */
	private final Object mutex = new Object();

	/** The JDBC URL we're using */
	private String myURL = null;

	/** Does this connection need to be tested? */
	private boolean needsPing = false;

	private int netBufferLength = 16384;

	private boolean noBackslashEscapes = false;

	private long numberOfPreparedExecutes = 0;

	private long numberOfPrepares = 0;

	private long numberOfQueriesIssued = 0;

	private long numberOfResultSetsCreated = 0;

	private long[] numTablesMetricsHistBreakpoints;

	private int[] numTablesMetricsHistCounts;

	private long[] oldHistBreakpoints = null;

	private int[] oldHistCounts = null;

	/** A map of currently open statements */
	private Map openStatements;

	private LRUCache parsedCallableStatementCache;

	private boolean parserKnowsUnicode = false;

	/** The password we used */
	private String password = null;

	private long[] perfMetricsHistBreakpoints;

	private int[] perfMetricsHistCounts;

	/** Point of origin where this Connection was created */
	private Throwable pointOfOrigin;

	/** The port number we're connected to (defaults to 3306) */
	private int port = 3306;

	/**
	 * Used only when testing failover functionality for regressions, causes the
	 * failover code to not retry the master first
	 */
	private boolean preferSlaveDuringFailover = false;

	/** Properties for this connection specified by user */
	private Properties props = null;

	/** Number of queries we've issued since the master failed */
	private long queriesIssuedFailedOver = 0;

	/** Should we retrieve 'info' messages from the server? */
	private boolean readInfoMsg = false;

	/** Are we in read-only mode? */
	private boolean readOnly = false;
	
	/** Cache of ResultSet metadata */
	protected LRUCache resultSetMetadataCache;
	
	/** The timezone of the server */
	private TimeZone serverTimezoneTZ = null;

	/** The map of server variables that we retrieve at connection init. */
	private Map serverVariables = null;

	private long shortestQueryTimeMs = Long.MAX_VALUE;

	/** A map of statements that have had setMaxRows() called on them */
	private Map statementsUsingMaxRows;

	private double totalQueryTimeMs = 0;

	/** Are transactions supported by the MySQL server we are connected to? */
	private boolean transactionsSupported = false;

	/**
	 * The type map for UDTs (not implemented, but used by some third-party
	 * vendors, most notably IBM WebSphere)
	 */
	private Map typeMap;

	/** Has ANSI_QUOTES been enabled on the server? */
	private boolean useAnsiQuotes = false;

	/** The user we're connected as */
	private String user = null;

	/**
	 * Should we use server-side prepared statements? (auto-detected, but can be
	 * disabled by user)
	 */
	private boolean useServerPreparedStmts = false;
	
	private LRUCache serverSideStatementCheckCache;

	private LRUCache serverSideStatementCache;
	private Calendar sessionCalendar;
	private Calendar utcCalendar;
	
	private String origHostToConnectTo;
	
	private int origPortToConnectTo;

	// we don't want to be able to publicly clone this...
	
	private String origDatabaseToConnectTo;

	private String errorMessageEncoding = "Cp1252"; // to begin with, changes after we talk to the server

	private boolean usePlatformCharsetConverters;
	
	
	/**
	 * Creates a connection to a MySQL Server.
	 * 
	 * @param hostToConnectTo
	 *            the hostname of the database server
	 * @param portToConnectTo
	 *            the port number the server is listening on
	 * @param info
	 *            a Properties[] list holding the user and password
	 * @param databaseToConnectTo
	 *            the database to connect to
	 * @param url
	 *            the URL of the connection
	 * @param d
	 *            the Driver instantation of the connection
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	Connection(String hostToConnectTo, int portToConnectTo, Properties info,
			String databaseToConnectTo, String url)
			throws SQLException {
		this.charsetToNumBytesMap = new HashMap();
		
		this.connectionCreationTimeMillis = System.currentTimeMillis();
		this.pointOfOrigin = new Throwable();
		
		// Stash away for later, used to clone this connection for Statement.cancel
		// and Statement.setQueryTimeout().
		//
		
		this.origHostToConnectTo = hostToConnectTo;
		this.origPortToConnectTo = portToConnectTo;
		this.origDatabaseToConnectTo = databaseToConnectTo;

		try {
			Blob.class.getMethod("truncate", new Class[] {Long.TYPE});
			
			this.isRunningOnJDK13 = false;
		} catch (NoSuchMethodException nsme) {
			this.isRunningOnJDK13 = true;
		}
		
		this.sessionCalendar = new GregorianCalendar();
		this.utcCalendar = new GregorianCalendar();
		this.utcCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		//
		// Normally, this code would be in initializeDriverProperties,
		// but we need to do this as early as possible, so we can start
		// logging to the 'correct' place as early as possible...this.log
		// points to 'NullLogger' for every connection at startup to avoid
		// NPEs and the overhead of checking for NULL at every logging call.
		//
		// We will reset this to the configured logger during properties
		// initialization.
		//
		this.log = LogFactory.getLogger(getLogger(), LOGGER_INSTANCE_NAME);

		// We store this per-connection, due to static synchronization
		// issues in Java's built-in TimeZone class...
		this.defaultTimeZone = Util.getDefaultTimeZone();
		
		if ("GMT".equalsIgnoreCase(this.defaultTimeZone.getID())) {
			this.isClientTzUTC = true;
		} else {
			this.isClientTzUTC = false;
		}

		this.openStatements = new HashMap();
		this.serverVariables = new HashMap();
		this.hostList = new ArrayList();

		if (hostToConnectTo == null) {
			this.host = "localhost";
			this.hostList.add(this.host);
		} else if (hostToConnectTo.indexOf(",") != -1) {
			// multiple hosts separated by commas (failover)
			StringTokenizer hostTokenizer = new StringTokenizer(
					hostToConnectTo, ",", false);

			while (hostTokenizer.hasMoreTokens()) {
				this.hostList.add(hostTokenizer.nextToken().trim());
			}
		} else {
			this.host = hostToConnectTo;
			this.hostList.add(this.host);
		}

		this.hostListSize = this.hostList.size();
		this.port = portToConnectTo;

		if (databaseToConnectTo == null) {
			databaseToConnectTo = "";
		}

		this.database = databaseToConnectTo;
		this.myURL = url;
		this.user = info.getProperty(NonRegisteringDriver.USER_PROPERTY_KEY);
		this.password = info
				.getProperty(NonRegisteringDriver.PASSWORD_PROPERTY_KEY);

		if ((this.user == null) || this.user.equals("")) {
			this.user = "";
		}

		if (this.password == null) {
			this.password = "";
		}

		this.props = info;
		initializeDriverProperties(info);

		try {
			createNewIO(false);
			this.dbmd = new DatabaseMetaData(this, this.database);
		} catch (SQLException ex) {
			cleanup(ex);

			// don't clobber SQL exceptions
			throw ex;
		} catch (Exception ex) {
			cleanup(ex);

			StringBuffer mesg = new StringBuffer();

			if (getParanoid()) {
				mesg.append("Cannot connect to MySQL server on ");
				mesg.append(this.host);
				mesg.append(":");
				mesg.append(this.port);
				mesg.append(".\n\n");
				mesg.append("Make sure that there is a MySQL server ");
				mesg.append("running on the machine/port you are trying ");
				mesg
						.append("to connect to and that the machine this software is "
								+ "running on ");
				mesg.append("is able to connect to this host/port "
						+ "(i.e. not firewalled). ");
				mesg
						.append("Also make sure that the server has not been started "
								+ "with the --skip-networking ");
				mesg.append("flag.\n\n");
			} else {
				mesg.append("Unable to connect to database.");
			}

			mesg.append("Underlying exception: \n\n");
			mesg.append(ex.getClass().getName());

			if (!getParanoid()) {
				mesg.append(Util.stackTraceToString(ex));
			}

			throw SQLError.createSQLException(mesg.toString(),
					SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE);
		}
	}

	private void addToHistogram(int[] histogramCounts,
			long[] histogramBreakpoints, long value, int numberOfTimes,
			long currentLowerBound, long currentUpperBound) {
		if (histogramCounts == null) {
			createInitialHistogram(histogramBreakpoints,
					currentLowerBound, currentUpperBound);
		}

		for (int i = 0; i < HISTOGRAM_BUCKETS; i++) {
			if (histogramBreakpoints[i] >= value) {
				histogramCounts[i] += numberOfTimes;

				break;
			}
		}
	}

	private void addToPerformanceHistogram(long value, int numberOfTimes) {
		checkAndCreatePerformanceHistogram();

		addToHistogram(this.perfMetricsHistCounts,
				this.perfMetricsHistBreakpoints, value, numberOfTimes,
				this.shortestQueryTimeMs == Long.MAX_VALUE ? 0
						: this.shortestQueryTimeMs, this.longestQueryTimeMs);
	}

	private void addToTablesAccessedHistogram(long value, int numberOfTimes) {
		checkAndCreateTablesAccessedHistogram();

		addToHistogram(this.numTablesMetricsHistCounts,
				this.numTablesMetricsHistBreakpoints, value, numberOfTimes,
				this.minimumNumberTablesAccessed == Long.MAX_VALUE ? 0
						: this.minimumNumberTablesAccessed,
				this.maximumNumberTablesAccessed);
	}

	/**
	 * Builds the map needed for 4.1.0 and newer servers that maps field-level
	 * charset/collation info to a java character encoding name.
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	private void buildCollationMapping() throws SQLException {
		if (versionMeetsMinimum(4, 1, 0)) {

			TreeMap sortedCollationMap = null;

			if (getCacheServerConfiguration()) {
				synchronized (serverConfigByUrl) {
					sortedCollationMap = (TreeMap) serverCollationByUrl
							.get(getURL());
				}
			}

			com.mysql.jdbc.Statement stmt = null;
			com.mysql.jdbc.ResultSet results = null;

			try {
				if (sortedCollationMap == null) {
					sortedCollationMap = new TreeMap();

					stmt = (com.mysql.jdbc.Statement) createStatement();

					if (stmt.getMaxRows() != 0) {
						stmt.setMaxRows(0);
					}

					results = (com.mysql.jdbc.ResultSet) stmt
							.executeQuery("SHOW COLLATION");

					while (results.next()) {
						String charsetName = results.getString(2);
						Integer charsetIndex = new Integer(results.getInt(3));

						sortedCollationMap.put(charsetIndex, charsetName);
					}

					if (getCacheServerConfiguration()) {
						synchronized (serverConfigByUrl) {
							serverCollationByUrl.put(getURL(),
									sortedCollationMap);
						}
					}

				}

				// Now, merge with what we already know
				int highestIndex = ((Integer) sortedCollationMap.lastKey())
						.intValue();

				if (CharsetMapping.INDEX_TO_CHARSET.length > highestIndex) {
					highestIndex = CharsetMapping.INDEX_TO_CHARSET.length;
				}

				this.indexToCharsetMapping = new String[highestIndex + 1];

				for (int i = 0; i < CharsetMapping.INDEX_TO_CHARSET.length; i++) {
					this.indexToCharsetMapping[i] = CharsetMapping.INDEX_TO_CHARSET[i];
				}

				for (Iterator indexIter = sortedCollationMap.entrySet()
						.iterator(); indexIter.hasNext();) {
					Map.Entry indexEntry = (Map.Entry) indexIter.next();

					String mysqlCharsetName = (String) indexEntry.getValue();

					this.indexToCharsetMapping[((Integer) indexEntry.getKey())
							.intValue()] = CharsetMapping
							.getJavaEncodingForMysqlEncoding(mysqlCharsetName,
									this);
				}
			} catch (java.sql.SQLException e) {
				throw e;
			} finally {
				if (results != null) {
					try {
						results.close();
					} catch (java.sql.SQLException sqlE) {
						;
					}
				}

				if (stmt != null) {
					try {
						stmt.close();
					} catch (java.sql.SQLException sqlE) {
						;
					}
				}
			}
		} else {
			// Safety, we already do this as an initializer, but this makes
			// the intent more clear
			this.indexToCharsetMapping = CharsetMapping.INDEX_TO_CHARSET;
		}
	}
	
	private boolean canHandleAsServerPreparedStatement(String sql) 
		throws SQLException {
		if (sql == null || sql.length() == 0) {
			return true;
		}

		if (getCachePreparedStatements()) {
			synchronized (this.serverSideStatementCheckCache) {
				Boolean flag = (Boolean)this.serverSideStatementCheckCache.get(sql);
				
				if (flag != null) {
					return flag.booleanValue();
				}
					
				boolean canHandle = canHandleAsServerPreparedStatementNoCache(sql);
				
				if (sql.length() < getPreparedStatementCacheSqlLimit()) {
					this.serverSideStatementCheckCache.put(sql, 
							canHandle ? Boolean.TRUE : Boolean.FALSE);
				}
					
				return canHandle;
			}
		}
		
		return canHandleAsServerPreparedStatementNoCache(sql);
	}

	private boolean canHandleAsServerPreparedStatementNoCache(String sql) 
		throws SQLException {
		
		// Can't use server-side prepare for CALL
		if (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "CALL")) {
			return false;
		}
		
		boolean canHandleAsStatement = true;
		
		if (!versionMeetsMinimum(5, 0, 7) && 
				(StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "SELECT")
				|| StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql,
						"DELETE")
				|| StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql,
						"INSERT")
				|| StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql,
						"UPDATE")
				|| StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql,
						"REPLACE"))) {

			// check for limit ?[,?]

			/*
			 * The grammar for this (from the server) is: ULONG_NUM | ULONG_NUM
			 * ',' ULONG_NUM | ULONG_NUM OFFSET_SYM ULONG_NUM
			 */

			int currentPos = 0;
			int statementLength = sql.length();
			int lastPosToLook = statementLength - 7; // "LIMIT ".length()
			boolean allowBackslashEscapes = !this.noBackslashEscapes;
			char quoteChar = this.useAnsiQuotes ? '"' : '\'';
			boolean foundLimitWithPlaceholder = false;

			while (currentPos < lastPosToLook) {
				int limitStart = StringUtils.indexOfIgnoreCaseRespectQuotes(
						currentPos, sql, "LIMIT ", quoteChar,
						allowBackslashEscapes);

				if (limitStart == -1) {
					break;
				}

				currentPos = limitStart + 7;

				while (currentPos < statementLength) {
					char c = sql.charAt(currentPos);

					//
					// Have we reached the end
					// of what can be in a LIMIT clause?
					//

					if (!Character.isDigit(c) && !Character.isWhitespace(c)
							&& c != ',' && c != '?') {
						break;
					}

					if (c == '?') {
						foundLimitWithPlaceholder = true;
						break;
					}

					currentPos++;
				}
			}

			canHandleAsStatement = !foundLimitWithPlaceholder;
		} else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "CREATE TABLE")) {
			canHandleAsStatement = false;
		} else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "DO")) {
			canHandleAsStatement = false;
		} else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "SET")) {
			canHandleAsStatement = false;
		}

		
		
		return canHandleAsStatement;
	}

	/**
	 * Changes the user on this connection by performing a re-authentication. If
	 * authentication fails, the connection will remain under the context of the
	 * current user.
	 * 
	 * @param userName
	 *            the username to authenticate with
	 * @param newPassword
	 *            the password to authenticate with
	 * @throws SQLException
	 *             if authentication fails, or some other error occurs while
	 *             performing the command.
	 */
	public void changeUser(String userName, String newPassword)
			throws SQLException {
		if ((userName == null) || userName.equals("")) {
			userName = "";
		}

		if (newPassword == null) {
			newPassword = "";
		}

		this.io.changeUser(userName, newPassword, this.database);
		this.user = userName;
		this.password = newPassword;

		if (versionMeetsMinimum(4, 1, 0)) {
			configureClientCharacterSet();
		}
		
		setupServerForTruncationChecks();
	}

	private void checkAndCreatePerformanceHistogram() {
		if (this.perfMetricsHistCounts == null) {
			this.perfMetricsHistCounts = new int[HISTOGRAM_BUCKETS];
		}

		if (this.perfMetricsHistBreakpoints == null) {
			this.perfMetricsHistBreakpoints = new long[HISTOGRAM_BUCKETS];
		}
	}

	private void checkAndCreateTablesAccessedHistogram() {
		if (this.numTablesMetricsHistCounts == null) {
			this.numTablesMetricsHistCounts = new int[HISTOGRAM_BUCKETS];
		}

		if (this.numTablesMetricsHistBreakpoints == null) {
			this.numTablesMetricsHistBreakpoints = new long[HISTOGRAM_BUCKETS];
		}
	}

	private void checkClosed() throws SQLException {
		if (this.isClosed) {
			StringBuffer messageBuf = new StringBuffer(
					"No operations allowed after connection closed.");

			if (this.forcedClosedLocation != null || this.forceClosedReason != null) {
				messageBuf
				.append("Connection was implicitly closed ");
			}
			
			if (this.forcedClosedLocation != null) {
				messageBuf.append("\n\n");
				messageBuf
						.append(" at (stack trace):\n");
				messageBuf.append(Util
						.stackTraceToString(this.forcedClosedLocation));
			}

			if (this.forceClosedReason != null) {
				if (this.forcedClosedLocation != null) {
					messageBuf.append("\n\nDue ");
				} else {
					messageBuf.append("due ");
				}
				
				messageBuf.append("to underlying exception/error:\n");
				messageBuf.append(Util
						.stackTraceToString(this.forceClosedReason));
			}

			throw SQLError.createSQLException(messageBuf.toString(),
					SQLError.SQL_STATE_CONNECTION_NOT_OPEN);
		}
	}

	/**
	 * If useUnicode flag is set and explicit client character encoding isn't
	 * specified then assign encoding from server if any.
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	private void checkServerEncoding() throws SQLException {
		if (getUseUnicode() && (getEncoding() != null)) {
			// spec'd by client, don't map
			return;
		}

		String serverEncoding = (String) this.serverVariables
				.get("character_set");

		if (serverEncoding == null) {
			// must be 4.1.1 or newer?
			serverEncoding = (String) this.serverVariables
					.get("character_set_server");
		}

		String mappedServerEncoding = null;

		if (serverEncoding != null) {
			mappedServerEncoding = CharsetMapping
					.getJavaEncodingForMysqlEncoding(serverEncoding
							.toUpperCase(Locale.ENGLISH), this);
		}

		//
		// First check if we can do the encoding ourselves
		//
		if (!getUseUnicode() && (mappedServerEncoding != null)) {
			SingleByteCharsetConverter converter = getCharsetConverter(mappedServerEncoding);

			if (converter != null) { // we know how to convert this ourselves
				setUseUnicode(true); // force the issue
				setEncoding(mappedServerEncoding);

				return;
			}
		}

		//
		// Now, try and find a Java I/O converter that can do
		// the encoding for us
		//
		if (serverEncoding != null) {
			if (mappedServerEncoding == null) {
				// We don't have a mapping for it, so try
				// and canonicalize the name....
				if (Character.isLowerCase(serverEncoding.charAt(0))) {
					char[] ach = serverEncoding.toCharArray();
					ach[0] = Character.toUpperCase(serverEncoding.charAt(0));
					setEncoding(new String(ach));
				}
			}

			if (mappedServerEncoding == null) {
				throw SQLError.createSQLException("Unknown character encoding on server '"
						+ serverEncoding
						+ "', use 'characterEncoding=' property "
						+ " to provide correct mapping",
						SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE);
			}

			//
			// Attempt to use the encoding, and bail out if it
			// can't be used
			//
			try {
				"abc".getBytes(mappedServerEncoding);
				setEncoding(mappedServerEncoding);
				setUseUnicode(true);
			} catch (UnsupportedEncodingException UE) {
				throw SQLError.createSQLException(
						"The driver can not map the character encoding '"
								+ getEncoding()
								+ "' that your server is using "
								+ "to a character encoding your JVM understands. You "
								+ "can specify this mapping manually by adding \"useUnicode=true\" "
								+ "as well as \"characterEncoding=[an_encoding_your_jvm_understands]\" "
								+ "to your JDBC URL.", "0S100");
			}
		}
	}

	/**
	 * Set transaction isolation level to the value received from server if any.
	 * Is called by connectionInit(...)
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	private void checkTransactionIsolationLevel() throws SQLException {
		String txIsolationName = null;

		if (versionMeetsMinimum(4, 0, 3)) {
			txIsolationName = "tx_isolation";
		} else {
			txIsolationName = "transaction_isolation";
		}

		String s = (String) this.serverVariables.get(txIsolationName);

		if (s != null) {
			Integer intTI = (Integer) mapTransIsolationNameToValue.get(s);

			if (intTI != null) {
				this.isolationLevel = intTI.intValue();
			}
		}
	}

	/**
	 * Destroys this connection and any underlying resources
	 * 
	 * @param fromWhere
	 *            DOCUMENT ME!
	 * @param whyCleanedUp
	 *            DOCUMENT ME!
	 */
	private void cleanup(Throwable whyCleanedUp) {
		try {
			if ((this.io != null) && !isClosed()) {
				realClose(false, false, false, whyCleanedUp);
			} else if (this.io != null) {
				this.io.forceClose();
			}
		} catch (SQLException sqlEx) {
			// ignore, we're going away.
			;
		}

		this.isClosed = true;
	}

	/**
	 * After this call, getWarnings returns null until a new warning is reported
	 * for this connection.
	 * 
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public void clearWarnings() throws SQLException {
		// firstWarning = null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param sql
	 *            DOCUMENT ME!
	 * @return DOCUMENT ME!
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public PreparedStatement clientPrepareStatement(String sql)
			throws SQLException {
		return clientPrepareStatement(sql,
				java.sql.ResultSet.TYPE_SCROLL_SENSITIVE,
				java.sql.ResultSet.CONCUR_READ_ONLY);
	}
	
	/**
	 * @see Connection#prepareStatement(String, int)
	 */
	public java.sql.PreparedStatement clientPrepareStatement(String sql,
			int autoGenKeyIndex) throws SQLException {
		java.sql.PreparedStatement pStmt = clientPrepareStatement(sql);

		((com.mysql.jdbc.PreparedStatement) pStmt)
				.setRetrieveGeneratedKeys(autoGenKeyIndex == java.sql.Statement.RETURN_GENERATED_KEYS);

		return pStmt;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param sql
	 *            DOCUMENT ME!
	 * @param resultSetType
	 *            DOCUMENT ME!
	 * @param resultSetConcurrency
	 *            DOCUMENT ME!
	 * @return DOCUMENT ME!
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public PreparedStatement clientPrepareStatement(String sql,
			int resultSetType, int resultSetConcurrency) throws SQLException {
		return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
	}
	
	protected PreparedStatement clientPrepareStatement(String sql,
			int resultSetType, int resultSetConcurrency, 
			boolean processEscapeCodesIfNeeded) throws SQLException {
		checkClosed();

		String nativeSql = processEscapeCodesIfNeeded && getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql): sql;
		
		PreparedStatement pStmt = null;

		if (getCachePreparedStatements()) {
			synchronized (this.cachedPreparedStatementParams) {
				PreparedStatement.ParseInfo pStmtInfo = (PreparedStatement.ParseInfo) this.cachedPreparedStatementParams
						.get(nativeSql);
	
				if (pStmtInfo == null) {
					pStmt = new com.mysql.jdbc.PreparedStatement(this, nativeSql,
							this.database);
	
					PreparedStatement.ParseInfo parseInfo = pStmt.getParseInfo();
	
					if (parseInfo.statementLength < getPreparedStatementCacheSqlLimit()) {
						if (this.cachedPreparedStatementParams.size() >= getPreparedStatementCacheSize()) {
							Iterator oldestIter = this.cachedPreparedStatementParams
									.keySet().iterator();
							long lruTime = Long.MAX_VALUE;
							String oldestSql = null;
	
							while (oldestIter.hasNext()) {
								String sqlKey = (String) oldestIter.next();
								PreparedStatement.ParseInfo lruInfo = (PreparedStatement.ParseInfo) this.cachedPreparedStatementParams
										.get(sqlKey);
	
								if (lruInfo.lastUsed < lruTime) {
									lruTime = lruInfo.lastUsed;
									oldestSql = sqlKey;
								}
							}
	
							if (oldestSql != null) {
								this.cachedPreparedStatementParams
										.remove(oldestSql);
							}
						}
	
						this.cachedPreparedStatementParams.put(nativeSql, pStmt
								.getParseInfo());
					}
				} else {
					pStmtInfo.lastUsed = System.currentTimeMillis();
					pStmt = new com.mysql.jdbc.PreparedStatement(this, nativeSql,
							this.database, pStmtInfo);
				}
			}
		} else {
			pStmt = new com.mysql.jdbc.PreparedStatement(this, nativeSql,
					this.database);
		}

		pStmt.setResultSetType(resultSetType);
		pStmt.setResultSetConcurrency(resultSetConcurrency);

		return pStmt;
	}

	
	public void close() throws SQLException {
		realClose(true, true, false, null);
	}

	/**
	 * Closes all currently open statements.
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	private void closeAllOpenStatements() throws SQLException {
		SQLException postponedException = null;

		if (this.openStatements != null) {
			List currentlyOpenStatements = new ArrayList(); // we need this to
			// avoid
			// ConcurrentModificationEx

			for (Iterator iter = this.openStatements.keySet().iterator(); iter
					.hasNext();) {
				currentlyOpenStatements.add(iter.next());
			}

			int numStmts = currentlyOpenStatements.size();

			for (int i = 0; i < numStmts; i++) {
				Statement stmt = (Statement) currentlyOpenStatements.get(i);

				try {
					stmt.realClose(false, true);
				} catch (SQLException sqlEx) {
					postponedException = sqlEx; // throw it later, cleanup all
					// statements first
				}
			}

			if (postponedException != null) {
				throw postponedException;
			}
		}
	}

	private void closeStatement(java.sql.Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException sqlEx) {
				; // ignore
			}

			stmt = null;
		}
	}

	// --------------------------JDBC 2.0-----------------------------

	/**
	 * The method commit() makes all changes made since the previous
	 * commit/rollback permanent and releases any database locks currently held
	 * by the Connection. This method should only be used when auto-commit has
	 * been disabled.
	 * <p>
	 * <b>Note:</b> MySQL does not support transactions, so this method is a
	 * no-op.
	 * </p>
	 * 
	 * @exception SQLException
	 *                if a database access error occurs
	 * @see setAutoCommit
	 */
	public void commit() throws SQLException {
		synchronized (getMutex()) {
			checkClosed();
	
			try {
				// no-op if _relaxAutoCommit == true
				if (this.autoCommit && !getRelaxAutoCommit()) {
					throw SQLError.createSQLException("Can't call commit when autocommit=true");
				} else if (this.transactionsSupported) {
					if (getUseLocalSessionState() && versionMeetsMinimum(5, 0, 0)) {
						if (!this.io.inTransactionOnServer()) {
							return; // effectively a no-op
						}
					}
					
					execSQL(null, "commit", -1, null,
							java.sql.ResultSet.TYPE_FORWARD_ONLY,
							java.sql.ResultSet.CONCUR_READ_ONLY, false,
							this.database, true,
							false);
				}
			} catch (SQLException sqlException) {
				if (SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE
						.equals(sqlException.getSQLState())) {
					throw SQLError.createSQLException(
							"Communications link failure during commit(). Transaction resolution unknown.",
							SQLError.SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN);
				}
	
				throw sqlException;
			} finally {
				this.needsPing = this.getReconnectAtTxEnd();
			}
	
			return;
		}
	}

	/**
	 * Configures client-side properties for character set information.
	 * 
	 * @throws SQLException
	 *             if unable to configure the specified character set.
	 */
	private void configureCharsetProperties() throws SQLException {
		if (getEncoding() != null) {
			// Attempt to use the encoding, and bail out if it
			// can't be used
			try {
				String testString = "abc";
				testString.getBytes(getEncoding());
			} catch (UnsupportedEncodingException UE) {
				// Try the MySQL character encoding, then....
				String oldEncoding = getEncoding();

				setEncoding(CharsetMapping.getJavaEncodingForMysqlEncoding(
						oldEncoding, this));

				if (getEncoding() == null) {
					throw SQLError.createSQLException(
							"Java does not support the MySQL character encoding "
									+ " " + "encoding '" + oldEncoding + "'.",
							SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE);
				}

				try {
					String testString = "abc";
					testString.getBytes(getEncoding());
				} catch (UnsupportedEncodingException encodingEx) {
					throw SQLError.createSQLException("Unsupported character "
							+ "encoding '" + getEncoding() + "'.",
							SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE);
				}
			}
		}
	}

	/**
	 * Sets up client character set for MySQL-4.1 and newer if the user This
	 * must be done before any further communication with the server!
	 * 
	 * @return true if this routine actually configured the client character
	 *         set, or false if the driver needs to use 'older' methods to
	 *         detect the character set, as it is connected to a MySQL server
	 *         older than 4.1.0
	 * @throws SQLException
	 *             if an exception happens while sending 'SET NAMES' to the
	 *             server, or the server sends character set information that
	 *             the client doesn't know about.
	 */
	private boolean configureClientCharacterSet() throws SQLException {
		String realJavaEncoding = getEncoding();
		boolean characterSetAlreadyConfigured = false;

		try {
			if (versionMeetsMinimum(4, 1, 0)) {
				characterSetAlreadyConfigured = true;

				setUseUnicode(true);

				configureCharsetProperties();
				realJavaEncoding = getEncoding(); // we need to do this again
				// to grab this for
				// versions > 4.1.0

				try {

					// Fault injection for testing server character set indices
		            
		            if (props != null && props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex") != null) {
		            	this.io.serverCharsetIndex = Integer.parseInt(
		            			props.getProperty(
		            					"com.mysql.jdbc.faultInjection.serverCharsetIndex"));	
		            }
		            
					String serverEncodingToSet = 
						CharsetMapping.INDEX_TO_CHARSET[this.io.serverCharsetIndex];
					
					if (serverEncodingToSet == null || serverEncodingToSet.length() == 0) {
						if (realJavaEncoding != null) {
							// user knows best, try it
							setEncoding(realJavaEncoding);
						} else {
							throw SQLError.createSQLException(
									"Unknown initial character set index '"
											+ this.io.serverCharsetIndex
											+ "' received from server. Initial client character set can be forced via the 'characterEncoding' property.",
									SQLError.SQL_STATE_GENERAL_ERROR);
						}
					}
					
					// "latin1" on MySQL-4.1.0+ is actually CP1252, not ISO8859_1
					if (versionMeetsMinimum(4, 1, 0) && 
							"ISO8859_1".equalsIgnoreCase(serverEncodingToSet)) {
						serverEncodingToSet = "Cp1252";
					}
					
					setEncoding(serverEncodingToSet);
				
				} catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
					if (realJavaEncoding != null) {
						// user knows best, try it
						setEncoding(realJavaEncoding);
					} else {
						throw SQLError.createSQLException(
								"Unknown initial character set index '"
										+ this.io.serverCharsetIndex
										+ "' received from server. Initial client character set can be forced via the 'characterEncoding' property.",
								SQLError.SQL_STATE_GENERAL_ERROR);
					}
				}

				if (getEncoding() == null) {
					// punt?
					setEncoding("ISO8859_1");
				}

				//
				// Has the user has 'forced' the character encoding via
				// driver properties?
				//
				if (getUseUnicode()) {
					if (realJavaEncoding != null) {

						//
						// Now, inform the server what character set we
						// will be using from now-on...
						//
						if (realJavaEncoding.equalsIgnoreCase("UTF-8")
								|| realJavaEncoding.equalsIgnoreCase("UTF8")) {
							// charset names are case-sensitive

							if (!getUseOldUTF8Behavior()) {
								if (!characterSetNamesMatches("utf8")) {
									execSQL(null, "SET NAMES utf8", -1, null,
											java.sql.ResultSet.TYPE_FORWARD_ONLY,
											java.sql.ResultSet.CONCUR_READ_ONLY,
											false, this.database, true, false);
								}
							}

							setEncoding(realJavaEncoding);
						} /* not utf-8 */else {
							String mysqlEncodingName = CharsetMapping
									.getMysqlEncodingForJavaEncoding(
											realJavaEncoding
													.toUpperCase(Locale.ENGLISH),
											this);

							/*
							 * if ("koi8_ru".equals(mysqlEncodingName)) { //
							 * This has a _different_ name in 4.1...
							 * mysqlEncodingName = "ko18r"; } else if
							 * ("euc_kr".equals(mysqlEncodingName)) { //
							 * Different name in 4.1 mysqlEncodingName =
							 * "euckr"; }
							 */

							if (mysqlEncodingName != null) {
								
								if (!characterSetNamesMatches(mysqlEncodingName)) {
									execSQL(null, "SET NAMES " + mysqlEncodingName,
										-1, null,
										java.sql.ResultSet.TYPE_FORWARD_ONLY,
										java.sql.ResultSet.CONCUR_READ_ONLY,
										false, this.database, true, false);
								}
							}

							// Switch driver's encoding now, since the server
							// knows what we're sending...
							//
							setEncoding(realJavaEncoding);
						}
					} else if (getEncoding() != null) {
						// Tell the server we'll use the server default charset
						// to send our
						// queries from now on....
						String mysqlEncodingName = CharsetMapping
								.getMysqlEncodingForJavaEncoding(getEncoding()
										.toUpperCase(Locale.ENGLISH), this);

						if (!characterSetNamesMatches(mysqlEncodingName)) {
							execSQL(null, "SET NAMES " + mysqlEncodingName, -1,
								null, java.sql.ResultSet.TYPE_FORWARD_ONLY,
								java.sql.ResultSet.CONCUR_READ_ONLY, false,
								this.database, true, false);
						}

						realJavaEncoding = getEncoding();
					}

				}

				//
				// We know how to deal with any charset coming back from
				// the database, so tell the server not to do conversion
				// if the user hasn't 'forced' a result-set character set
				//
				
				String onServer = null;
				boolean isNullOnServer = false;
				
				if (this.serverVariables != null) {
					onServer = (String)this.serverVariables.get("character_set_results");
					
					isNullOnServer = onServer == null || "NULL".equalsIgnoreCase(onServer) || onServer.length() == 0;
				}
				
				if (getCharacterSetResults() == null) {
					
					//
					// Only send if needed, if we're caching server variables
					// we -have- to send, because we don't know what it was
					// before we cached them.
					//
					if (!isNullOnServer) {
						execSQL(null, "SET character_set_results = NULL", -1, null,
								java.sql.ResultSet.TYPE_FORWARD_ONLY,
								java.sql.ResultSet.CONCUR_READ_ONLY, false,
								this.database, true, 
								false);
						if (!this.usingCachedConfig) {
							this.serverVariables.put(JDBC_LOCAL_CHARACTER_SET_RESULTS, null);
						}
					} else {
						if (!this.usingCachedConfig) {
							this.serverVariables.put(JDBC_LOCAL_CHARACTER_SET_RESULTS, onServer);
						}
					}
				} else {
					String charsetResults = getCharacterSetResults();
					String mysqlEncodingName = null;

					if ("UTF-8".equalsIgnoreCase(charsetResults)
							|| "UTF8".equalsIgnoreCase(charsetResults)) {
						mysqlEncodingName = "utf8";
					} else {
						mysqlEncodingName = CharsetMapping
								.getMysqlEncodingForJavaEncoding(charsetResults
										.toUpperCase(Locale.ENGLISH), this);
					}

					//
					// Only change the value if needed
					//
					
					if (!mysqlEncodingName.equalsIgnoreCase(
							(String)this.serverVariables.get("character_set_results"))) {
						StringBuffer setBuf = new StringBuffer(
								"SET character_set_results = ".length()
										+ mysqlEncodingName.length());
						setBuf.append("SET character_set_results = ").append(
								mysqlEncodingName);
						
						execSQL(null, setBuf.toString(), -1, null,
								java.sql.ResultSet.TYPE_FORWARD_ONLY,
								java.sql.ResultSet.CONCUR_READ_ONLY, false,
								this.database, true, false);
						
						if (!this.usingCachedConfig) {
							this.serverVariables.put(JDBC_LOCAL_CHARACTER_SET_RESULTS, 
								mysqlEncodingName);
						}
					} else {
						if (!this.usingCachedConfig) {
							this.serverVariables.put(JDBC_LOCAL_CHARACTER_SET_RESULTS, onServer);
						}
					}
				}

				if (getConnectionCollation() != null) {
					StringBuffer setBuf = new StringBuffer(
							"SET collation_connection = ".length()
									+ getConnectionCollation().length());
					setBuf.append("SET collation_connection = ").append(
							getConnectionCollation());

					execSQL(null, setBuf.toString(), -1, null,
							java.sql.ResultSet.TYPE_FORWARD_ONLY,
							java.sql.ResultSet.CONCUR_READ_ONLY, false,
							this.database, true, false);
				}
			} else {
				// Use what the server has specified
				realJavaEncoding = getEncoding(); // so we don't get
				// swapped out in the finally
				// block....
			}
		} finally {
			// Failsafe, make sure that the driver's notion of character
			// encoding matches what the user has specified.
			//
			setEncoding(realJavaEncoding);
		}

		return characterSetAlreadyConfigured;
	}

	private boolean characterSetNamesMatches(String mysqlEncodingName) {
		// set names is equivalent to character_set_client ..._results and ..._connection,
		// but we set _results later, so don't check it here.
		
		return (mysqlEncodingName != null && 
				mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_client")) &&
				mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_connection")));
	}
	
	/**
	 * Configures the client's timezone if required.
	 * 
	 * @throws SQLException
	 *             if the timezone the server is configured to use can't be
	 *             mapped to a Java timezone.
	 */
	private void configureTimezone() throws SQLException {
		String configuredTimeZoneOnServer = (String) this.serverVariables
				.get("timezone");

		if (configuredTimeZoneOnServer == null) {
			configuredTimeZoneOnServer = (String) this.serverVariables
					.get("time_zone");

			if ("SYSTEM".equalsIgnoreCase(configuredTimeZoneOnServer)) {
				configuredTimeZoneOnServer = (String) this.serverVariables
						.get("system_time_zone");
			}
		}

		if (getUseTimezone() && configuredTimeZoneOnServer != null) {
			// user can specify/override as property
			String canoncicalTimezone = getServerTimezone();

			if ((canoncicalTimezone == null)
					|| (canoncicalTimezone.length() == 0)) {
				String serverTimezoneStr = configuredTimeZoneOnServer;

				try {
					canoncicalTimezone = TimeUtil
							.getCanoncialTimezone(serverTimezoneStr);

					if (canoncicalTimezone == null) {
						throw SQLError.createSQLException("Can't map timezone '"
								+ serverTimezoneStr + "' to "
								+ " canonical timezone.",
								SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
					}
				} catch (IllegalArgumentException iae) {
					throw SQLError.createSQLException(iae.getMessage(),
							SQLError.SQL_STATE_GENERAL_ERROR);
				}
			}

			this.serverTimezoneTZ = TimeZone.getTimeZone(canoncicalTimezone);

			//
			// The Calendar class has the behavior of mapping
			// unknown timezones to 'GMT' instead of throwing an
			// exception, so we must check for this...
			//
			if (!canoncicalTimezone.equalsIgnoreCase("GMT")
					&& this.serverTimezoneTZ.getID().equals("GMT")) {
				throw SQLError.createSQLException("No timezone mapping entry for '"
						+ canoncicalTimezone + "'",
						SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
			}

			if ("GMT".equalsIgnoreCase(this.serverTimezoneTZ.getID())) {
				this.isServerTzUTC = true;
			} else {
				this.isServerTzUTC = false;
			}
		}
	}

	private void createInitialHistogram(long[] breakpoints,
			long lowerBound, long upperBound) {

		double bucketSize = (((double) upperBound - (double) lowerBound) / HISTOGRAM_BUCKETS) * 1.25;

		if (bucketSize < 1) {
			bucketSize = 1;
		}

		for (int i = 0; i < HISTOGRAM_BUCKETS; i++) {
			breakpoints[i] = lowerBound;
			lowerBound += bucketSize;
		}
	}

	/**
	 * Creates an IO channel to the server
	 * 
	 * @param isForReconnect
	 *            is this request for a re-connect
	 * @return a new MysqlIO instance connected to a server
	 * @throws SQLException
	 *             if a database access error occurs
	 * @throws CommunicationsException
	 *             DOCUMENT ME!
	 */
	protected com.mysql.jdbc.MysqlIO createNewIO(boolean isForReconnect)
			throws SQLException {
		MysqlIO newIo = null;

		Properties mergedProps = new Properties();

		mergedProps = exposeAsProperties(this.props);

		long queriesIssuedFailedOverCopy = this.queriesIssuedFailedOver;
		this.queriesIssuedFailedOver = 0;

		try {
			if (!getHighAvailability() && !this.failedOver) {
				boolean connectionGood = false;
				Exception connectionNotEstablishedBecause = null;
				
				int hostIndex = 0;

				//
				// TODO: Eventually, when there's enough metadata
				// on the server to support it, we should come up
				// with a smarter way to pick what server to connect
				// to...perhaps even making it 'pluggable'
				//
				if (getRoundRobinLoadBalance()) {
					hostIndex = getNextRoundRobinHostIndex(getURL(),
							this.hostList);
				}

				for (; hostIndex < this.hostListSize; hostIndex++) {

					if (hostIndex == 0) {
						this.hasTriedMasterFlag = true;
					}
					
					try {
						String newHostPortPair = (String) this.hostList
								.get(hostIndex);

						int newPort = 3306;

						String[] hostPortPair = NonRegisteringDriver
								.parseHostPortPair(newHostPortPair);
						String newHost = hostPortPair[NonRegisteringDriver.HOST_NAME_INDEX];

						if (newHost == null || newHost.trim().length() == 0) {
							newHost = "localhost";
						}

						if (hostPortPair[NonRegisteringDriver.PORT_NUMBER_INDEX] != null) {
							try {
								newPort = Integer
										.parseInt(hostPortPair[NonRegisteringDriver.PORT_NUMBER_INDEX]);
							} catch (NumberFormatException nfe) {
								throw SQLError.createSQLException(
										"Illegal connection port value '"
												+ hostPortPair[NonRegisteringDriver.PORT_NUMBER_INDEX]
												+ "'",
										SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE);
							}
						}

						this.io = new MysqlIO(newHost, newPort, mergedProps,
								getSocketFactoryClassName(), this,
								getSocketTimeout());
	
						this.io.doHandshake(this.user, this.password,
								this.database);
						this.connectionId = this.io.getThreadId();
						this.isClosed = false;

						// save state from old connection
						boolean oldAutoCommit = getAutoCommit();
						int oldIsolationLevel = this.isolationLevel;
						boolean oldReadOnly = isReadOnly();
						String oldCatalog = getCatalog();

						// Server properties might be different
						// from previous connection, so initialize
						// again...
						initializePropsFromServer();

						if (isForReconnect) {
							// Restore state from old connection
							setAutoCommit(oldAutoCommit);

							if (this.hasIsolationLevels) {
								setTransactionIsolation(oldIsolationLevel);
							}

							setCatalog(oldCatalog);
						}

						if (hostIndex != 0) {
							setFailedOverState();
							queriesIssuedFailedOverCopy = 0;
						} else {
							this.failedOver = false;
							queriesIssuedFailedOverCopy = 0;

							if (this.hostListSize > 1) {
								setReadOnlyInternal(false);
							} else {
								setReadOnlyInternal(oldReadOnly);
							}
						}

						connectionGood = true;
						
						break; // low-level connection succeeded
					} catch (Exception EEE) {
						if (this.io != null) {
							this.io.forceClose();
						}

						connectionNotEstablishedBecause = EEE;
						
						connectionGood = false;
						
						if (EEE instanceof SQLException) {
							SQLException sqlEx = (SQLException)EEE;
						
							String sqlState = sqlEx.getSQLState();
	
							// If this isn't a communications failure, it will probably never succeed, so
							// give up right here and now ....
							if ((sqlState == null)
									|| !sqlState
											.equals(SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE)) {
								throw sqlEx;
							}
						}

						// Check next host, it might be up...
						if (getRoundRobinLoadBalance()) {
							hostIndex = getNextRoundRobinHostIndex(getURL(),
									this.hostList) - 1 /* incremented by for loop next time around */;
						} else if ((this.hostListSize - 1) == hostIndex) {
							throw new CommunicationsException(this,
									(this.io != null) ? this.io
											.getLastPacketSentTimeMs() : 0,
											EEE);
						}
					}
				}
				
				if (!connectionGood) {
					// We've really failed!
					throw SQLError.createSQLException(
							"Could not create connection to database server due to underlying exception: '"
									+ connectionNotEstablishedBecause
									+ "'."
									+ (getParanoid() ? ""
											: Util
													.stackTraceToString(connectionNotEstablishedBecause)),
							SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE);
				}
			} else {
				double timeout = getInitialTimeout();
				boolean connectionGood = false;

				Exception connectionException = null;

				int hostIndex = 0;

				if (getRoundRobinLoadBalance()) {
					hostIndex = getNextRoundRobinHostIndex(getURL(),
							this.hostList);
				}

				for (; (hostIndex < this.hostListSize) && !connectionGood; hostIndex++) {
					if (hostIndex == 0) {
						this.hasTriedMasterFlag = true;
					}
					
					if (this.preferSlaveDuringFailover && hostIndex == 0) {
						hostIndex++;
					}

					for (int attemptCount = 0; (attemptCount < getMaxReconnects())
							&& !connectionGood; attemptCount++) {
						try {
							if (this.io != null) {
								this.io.forceClose();
							}

							String newHostPortPair = (String) this.hostList
									.get(hostIndex);

							int newPort = 3306;

							String[] hostPortPair = NonRegisteringDriver
									.parseHostPortPair(newHostPortPair);
							String newHost = hostPortPair[NonRegisteringDriver.HOST_NAME_INDEX];

							if (newHost == null || newHost.trim().length() == 0) {
								newHost = "localhost";
							}

							if (hostPortPair[NonRegisteringDriver.PORT_NUMBER_INDEX] != null) {
								try {
									newPort = Integer
											.parseInt(hostPortPair[NonRegisteringDriver.PORT_NUMBER_INDEX]);
								} catch (NumberFormatException nfe) {
									throw SQLError.createSQLException(
											"Illegal connection port value '"
													+ hostPortPair[NonRegisteringDriver.PORT_NUMBER_INDEX]
													+ "'",
											SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE);
								}
							}

							this.io = new MysqlIO(newHost, newPort,
									mergedProps, getSocketFactoryClassName(),
									this, getSocketTimeout());
							this.io.doHandshake(this.user, this.password,
									this.database);

							pingInternal(false);
							this.connectionId = this.io.getThreadId();
							this.isClosed = false;

							// save state from old connection
							boolean oldAutoCommit = getAutoCommit();
							int oldIsolationLevel = this.isolationLevel;
							boolean oldReadOnly = isReadOnly();
							String oldCatalog = getCatalog();

							// Server properties might be different
							// from previous connection, so initialize
							// again...
							initializePropsFromServer();

							if (isForReconnect) {
								// Restore state from old connection
								setAutoCommit(oldAutoCommit);

								if (this.hasIsolationLevels) {
									setTransactionIsolation(oldIsolationLevel);
								}

								setCatalog(oldCatalog);
							}

							connectionGood = true;

							if (hostIndex != 0) {
								setFailedOverState();
								queriesIssuedFailedOverCopy = 0;
							} else {
								this.failedOver = false;
								queriesIssuedFailedOverCopy = 0;

								if (this.hostListSize > 1) {
									setReadOnlyInternal(false);
								} else {
									setReadOnlyInternal(oldReadOnly);
								}
							}

							break;
						} catch (Exception EEE) {
							connectionException = EEE;
							connectionGood = false;
							
							// Check next host, it might be up...
							if (getRoundRobinLoadBalance()) {
								hostIndex = getNextRoundRobinHostIndex(getURL(),
										this.hostList) - 1 /* incremented by for loop next time around */;
							}
						}

						if (connectionGood) {
							break;
						}

						if (attemptCount > 0) {
							try {
								Thread.sleep((long) timeout * 1000);
							} catch (InterruptedException IE) {
								;
							}
						}
					} // end attempts for a single host
				} // end iterator for list of hosts

				if (!connectionGood) {
					// We've really failed!
					throw SQLError.createSQLException(
							"Server connection failure during transaction. Due to underlying exception: '"
									+ connectionException
									+ "'."
									+ (getParanoid() ? ""
											: Util
													.stackTraceToString(connectionException))
									+ "\nAttempted reconnect "
									+ getMaxReconnects() + " times. Giving up.",
							SQLError.SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE);
				}
			}

			if (getParanoid() && !getHighAvailability()
					&& (this.hostListSize <= 1)) {
				this.password = null;
				this.user = null;
			}

			if (isForReconnect) {
				//
				// Retrieve any 'lost' prepared statements if re-connecting
				//
				Iterator statementIter = this.openStatements.values()
						.iterator();

				//
				// We build a list of these outside the map of open statements,
				// because
				// in the process of re-preparing, we might end up having to
				// close
				// a prepared statement, thus removing it from the map, and
				// generating
				// a ConcurrentModificationException
				//
				Stack serverPreparedStatements = null;

				while (statementIter.hasNext()) {
					Object statementObj = statementIter.next();

					if (statementObj instanceof ServerPreparedStatement) {
						if (serverPreparedStatements == null) {
							serverPreparedStatements = new Stack();
						}

						serverPreparedStatements.add(statementObj);
					}
				}

				if (serverPreparedStatements != null) {
					while (!serverPreparedStatements.isEmpty()) {
						((ServerPreparedStatement) serverPreparedStatements
								.pop()).rePrepare();
					}
				}
			}

			return newIo;
		} finally {
			this.queriesIssuedFailedOver = queriesIssuedFailedOverCopy;
		}
	}

	private void createPreparedStatementCaches() {
		int cacheSize = getPreparedStatementCacheSize();
		
		this.cachedPreparedStatementParams = new HashMap(cacheSize);
		
		this.serverSideStatementCheckCache = new LRUCache(cacheSize);
		
		this.serverSideStatementCache = new LRUCache(cacheSize) {
			protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
				if (this.maxElements <= 1) {
					return false;
				}
				
				boolean removeIt = super.removeEldestEntry(eldest);
				
				if (removeIt) {
					ServerPreparedStatement ps = 
						(ServerPreparedStatement)eldest.getValue();
					ps.isCached = false;
					ps.setClosed(false);
					
					try {
						ps.close();
					} catch (SQLException sqlEx) {
						// punt
					}
				}
				
				return removeIt;
			}
		};
	}

	/**
	 * SQL statements without parameters are normally executed using Statement
	 * objects. If the same SQL statement is executed many times, it is more
	 * efficient to use a PreparedStatement
	 * 
	 * @return a new Statement object
	 * @throws SQLException
	 *             passed through from the constructor
	 */
	public java.sql.Statement createStatement() throws SQLException {
		return createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
				java.sql.ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * JDBC 2.0 Same as createStatement() above, but allows the default result
	 * set type and result set concurrency type to be overridden.
	 * 
	 * @param resultSetType
	 *            a result set type, see ResultSet.TYPE_XXX
	 * @param resultSetConcurrency
	 *            a concurrency type, see ResultSet.CONCUR_XXX
	 * @return a new Statement object
	 * @exception SQLException
	 *                if a database-access error occurs.
	 */
	public java.sql.Statement createStatement(int resultSetType,
			int resultSetConcurrency) throws SQLException {
		checkClosed();

		Statement stmt = new com.mysql.jdbc.Statement(this, this.database);
		stmt.setResultSetType(resultSetType);
		stmt.setResultSetConcurrency(resultSetConcurrency);

		return stmt;
	}

	/**
	 * @see Connection#createStatement(int, int, int)
	 */
	public java.sql.Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		if (getPedantic()) {
			if (resultSetHoldability != java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT) {
				throw SQLError.createSQLException(
						"HOLD_CUSRORS_OVER_COMMIT is only supported holdability level",
						SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
			}
		}

		return createStatement(resultSetType, resultSetConcurrency);
	}

	protected void dumpTestcaseQuery(String query) {
		System.err.println(query);
	}

	protected Connection duplicate() throws SQLException {
		return new Connection(	this.origHostToConnectTo, 
				this.origPortToConnectTo,
				this.props,
				this.origDatabaseToConnectTo,
				this.myURL);
	}

	/**
	 * Send a query to the server. Returns one of the ResultSet objects. This is
	 * synchronized, so Statement's queries will be serialized.
	 * 
	 * @param callingStatement
	 *            DOCUMENT ME!
	 * @param sql
	 *            the SQL statement to be executed
	 * @param maxRows
	 *            DOCUMENT ME!
	 * @param packet
	 *            DOCUMENT ME!
	 * @param resultSetType
	 *            DOCUMENT ME!
	 * @param resultSetConcurrency
	 *            DOCUMENT ME!
	 * @param streamResults
	 *            DOCUMENT ME!
	 * @param queryIsSelectOnly
	 *            DOCUMENT ME!
	 * @param catalog
	 *            DOCUMENT ME!
	 * @param unpackFields
	 *            DOCUMENT ME!
	 * @return a ResultSet holding the results
	 * @exception SQLException
	 *                if a database error occurs
	 */

	// ResultSet execSQL(Statement callingStatement, String sql,
	// int maxRowsToRetreive, String catalog) throws SQLException {
	// return execSQL(callingStatement, sql, maxRowsToRetreive, null,
	// java.sql.ResultSet.TYPE_FORWARD_ONLY,
	// java.sql.ResultSet.CONCUR_READ_ONLY, catalog);
	// }
	// ResultSet execSQL(Statement callingStatement, String sql, int maxRows,
	// int resultSetType, int resultSetConcurrency, boolean streamResults,
	// boolean queryIsSelectOnly, String catalog, boolean unpackFields) throws
	// SQLException {
	// return execSQL(callingStatement, sql, maxRows, null, resultSetType,
	// resultSetConcurrency, streamResults, queryIsSelectOnly, catalog,
	// unpackFields);
	// }
	ResultSet execSQL(Statement callingStatement, String sql, int maxRows,
			Buffer packet, int resultSetType, int resultSetConcurrency,
			boolean streamResults, String catalog,
			boolean unpackFields) throws SQLException {
		return execSQL(callingStatement, sql, maxRows, packet, resultSetType,
				resultSetConcurrency, streamResults,
				catalog, unpackFields, false);
	}

	ResultSet execSQL(Statement callingStatement, String sql, int maxRows,
			Buffer packet, int resultSetType, int resultSetConcurrency,
			boolean streamResults, String catalog,
			boolean unpackFields,
			boolean isBatch) throws SQLException {
		//
		// Fall-back if the master is back online if we've
		// issued queriesBeforeRetryMaster queries since
		// we failed over
		//
		synchronized (this.mutex) {
			long queryStartTime = 0;

			int endOfQueryPacketPosition = 0;

			if (packet != null) {
				endOfQueryPacketPosition = packet.getPosition();
			}

			if (getGatherPerformanceMetrics()) {
				queryStartTime = System.currentTimeMillis();
			}

			this.lastQueryFinishedTime = 0; // we're busy!

			if (this.failedOver && this.autoCommit && !isBatch) {
				if (shouldFallBack() && !this.executingFailoverReconnect) {
					try {
						this.executingFailoverReconnect = true;

						createNewIO(true);

						String connectedHost = this.io.getHost();

						if ((connectedHost != null)
								&& this.hostList.get(0).equals(connectedHost)) {
							this.failedOver = false;
							this.queriesIssuedFailedOver = 0;
							setReadOnlyInternal(false);
						}
					} finally {
						this.executingFailoverReconnect = false;
					}
				}
			}

			if ((getHighAvailability() || this.failedOver)
					&& (this.autoCommit || getAutoReconnectForPools())
					&& this.needsPing && !isBatch) {
				try {
					pingInternal(false);

					this.needsPing = false;
				} catch (Exception Ex) {
					createNewIO(true);
				}
			}

			try {
				if (packet == null) {
					String encoding = null;

					if (getUseUnicode()) {
						encoding = getEncoding();
					}

					return this.io.sqlQueryDirect(callingStatement, sql,
							encoding, null, maxRows, this, resultSetType,
							resultSetConcurrency, streamResults, catalog,
							unpackFields);
				}

				return this.io.sqlQueryDirect(callingStatement, null, null,
						packet, maxRows, this, resultSetType,
						resultSetConcurrency, streamResults, catalog,
						unpackFields);
			} catch (java.sql.SQLException sqlE) {
				// don't clobber SQL exceptions

				if (getDumpQueriesOnException()) {
					String extractedSql = extractSqlFromPacket(sql, packet,
							endOfQueryPacketPosition);
					StringBuffer messageBuf = new StringBuffer(extractedSql
							.length() + 32);
					messageBuf
							.append("\n\nQuery being executed when exception was thrown:\n\n");
					messageBuf.append(extractedSql);

					sqlE = appendMessageToException(sqlE, messageBuf.toString());
				}

				if ((getHighAvailability() || this.failedOver)) {
					this.needsPing = true;
				} else {
					String sqlState = sqlE.getSQLState();

					if ((sqlState != null)
							&& sqlState
									.equals(SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE)) {
						cleanup(sqlE);
					}
				}

				throw sqlE;
			} catch (Exception ex) {
				if ((getHighAvailability() || this.failedOver)) {
					this.needsPing = true;
				} else if (ex instanceof IOException) {
					cleanup(ex);
				}

				String exceptionType = ex.getClass().getName();
				String exceptionMessage = ex.getMessage();

				if (!getParanoid()) {
					exceptionMessage += "\n\nNested Stack Trace:\n";
					exceptionMessage += Util.stackTraceToString(ex);
				}

				throw new java.sql.SQLException(
						"Error during query: Unexpected Exception: "
								+ exceptionType + " message given: "
								+ exceptionMessage,
						SQLError.SQL_STATE_GENERAL_ERROR);
			} finally {
				if (getMaintainTimeStats()) {
					this.lastQueryFinishedTime = System.currentTimeMillis();
				}

				if (this.failedOver) {
					this.queriesIssuedFailedOver++;
				}

				if (getGatherPerformanceMetrics()) {
					long queryTime = System.currentTimeMillis()
							- queryStartTime;

					registerQueryExecutionTime(queryTime);
				}
			}
		}
	}

	protected String extractSqlFromPacket(String possibleSqlQuery,
			Buffer queryPacket, int endOfQueryPacketPosition)
			throws SQLException {

		String extractedSql = null;

		if (possibleSqlQuery != null) {
			if (possibleSqlQuery.length() > getMaxQuerySizeToLog()) {
				StringBuffer truncatedQueryBuf = new StringBuffer(
						possibleSqlQuery.substring(0, getMaxQuerySizeToLog()));
				truncatedQueryBuf.append(Messages.getString("MysqlIO.25"));
				extractedSql = truncatedQueryBuf.toString();
			} else {
				extractedSql = possibleSqlQuery;
			}
		}

		if (extractedSql == null) {
			// This is probably from a client-side prepared
			// statement

			int extractPosition = endOfQueryPacketPosition;

			boolean truncated = false;

			if (endOfQueryPacketPosition > getMaxQuerySizeToLog()) {
				extractPosition = getMaxQuerySizeToLog();
				truncated = true;
			}

			extractedSql = new String(queryPacket.getByteBuffer(), 5,
					(extractPosition - 5));

			if (truncated) {
				extractedSql += Messages.getString("MysqlIO.25"); //$NON-NLS-1$
			}
		}

		return extractedSql;

	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Throwable
	 *             DOCUMENT ME!
	 */
	protected void finalize() throws Throwable {
		cleanup(null);
	}

	protected StringBuffer generateConnectionCommentBlock(StringBuffer buf) {
		buf.append("/* conn id ");
		buf.append(getId());
		buf.append(" */ ");

		return buf;
	}

	public int getActiveStatementCount() {
		// Might not have one of these if
		// not tracking open resources
		if (this.openStatements != null) {
			synchronized (this.openStatements) {
				return this.openStatements.size();
			}
		}

		return 0;
	}

	/**
	 * Gets the current auto-commit state
	 * 
	 * @return Current state of auto-commit
	 * @exception SQLException
	 *                if an error occurs
	 * @see setAutoCommit
	 */
	public boolean getAutoCommit() throws SQLException {
		return this.autoCommit;
	}

	/**
	 * Optimization to only use one calendar per-session, or calculate it for
	 * each call, depending on user configuration
	 */
	protected Calendar getCalendarInstanceForSessionOrNew() {
		if (getDynamicCalendars()) {
			return Calendar.getInstance();
		}

		return getSessionLockedCalendar();
	}

	/**
	 * Return the connections current catalog name, or null if no catalog name
	 * is set, or we dont support catalogs.
	 * <p>
	 * <b>Note:</b> MySQL's notion of catalogs are individual databases.
	 * </p>
	 * 
	 * @return the current catalog name or null
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public String getCatalog() throws SQLException {
		return this.database;
	}

	/**
	 * @return Returns the characterSetMetadata.
	 */
	protected String getCharacterSetMetadata() {
		return characterSetMetadata;
	}

	/**
	 * Returns the locally mapped instance of a charset converter (to avoid
	 * overhead of static synchronization).
	 * 
	 * @param javaEncodingName
	 *            the encoding name to retrieve
	 * @return a character converter, or null if one couldn't be mapped.
	 */
	SingleByteCharsetConverter getCharsetConverter(
			String javaEncodingName) throws SQLException {
		if (javaEncodingName == null) {
			return null;
		}

		if (this.usePlatformCharsetConverters) {
			return null; // we'll use Java's built-in routines for this
			             // they're finally fast enough
		}
		
		SingleByteCharsetConverter converter = null;
		
		synchronized (this.charsetConverterMap) {
			Object asObject = this.charsetConverterMap
			.get(javaEncodingName);

			if (asObject == CHARSET_CONVERTER_NOT_AVAILABLE_MARKER) {
				return null;
			}
			
			converter = (SingleByteCharsetConverter)asObject;
			
			if (converter == null) {
				try {
					converter = SingleByteCharsetConverter.getInstance(
							javaEncodingName, this);

					if (converter == null) {
						this.charsetConverterMap.put(javaEncodingName,
								CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
					} else {
						this.charsetConverterMap.put(javaEncodingName, converter);
					}
				} catch (UnsupportedEncodingException unsupEncEx) {
					this.charsetConverterMap.put(javaEncodingName,
							CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);

					converter = null;
				}
			}
		}

		return converter;
	}

	/**
	 * Returns the Java character encoding name for the given MySQL server
	 * charset index
	 * 
	 * @param charsetIndex
	 * @return the Java character encoding name for the given MySQL server
	 *         charset index
	 * @throws SQLException
	 *             if the character set index isn't known by the driver
	 */
	protected String getCharsetNameForIndex(int charsetIndex)
			throws SQLException {
		String charsetName = null;

		if (getUseOldUTF8Behavior()) {
			return getEncoding();
		}

		if (charsetIndex != MysqlDefs.NO_CHARSET_INFO) {
			try {
				charsetName = this.indexToCharsetMapping[charsetIndex];

				if ("sjis".equalsIgnoreCase(charsetName) || 
						"MS932".equalsIgnoreCase(charsetName) /* for JDK6 */) {
					// Use our encoding so that code pages like Cp932 work
					if (CharsetMapping.isAliasForSjis(getEncoding())) {
						charsetName = getEncoding();
					}
				}
			} catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
				throw SQLError.createSQLException(
						"Unknown character set index for field '"
								+ charsetIndex + "' received from server.",
						SQLError.SQL_STATE_GENERAL_ERROR);
			}

			// Punt
			if (charsetName == null) {
				charsetName = getEncoding();
			}
		} else {
			charsetName = getEncoding();
		}

		return charsetName;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return Returns the defaultTimeZone.
	 */
	protected TimeZone getDefaultTimeZone() {
		return this.defaultTimeZone;
	}

	/**
	 * @see Connection#getHoldability()
	 */
	public int getHoldability() throws SQLException {
		return java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	long getId() {
		return this.connectionId;
	}

	/**
	 * NOT JDBC-Compliant, but clients can use this method to determine how long
	 * this connection has been idle. This time (reported in milliseconds) is
	 * updated once a query has completed.
	 * 
	 * @return number of ms that this connection has been idle, 0 if the driver
	 *         is busy retrieving results.
	 */
	public long getIdleFor() {
		if (this.lastQueryFinishedTime == 0) {
			return 0;
		}

		long now = System.currentTimeMillis();
		long idleTime = now - this.lastQueryFinishedTime;

		return idleTime;
	}

	/**
	 * Returns the IO channel to the server
	 * 
	 * @return the IO channel to the server
	 * @throws SQLException
	 *             if the connection is closed.
	 */
	protected MysqlIO getIO() throws SQLException {
		if ((this.io == null) || this.isClosed) {
			throw SQLError.createSQLException(
					"Operation not allowed on closed connection",
					SQLError.SQL_STATE_CONNECTION_NOT_OPEN);
		}

		return this.io;
	}

	/**
	 * Returns the log mechanism that should be used to log information from/for
	 * this Connection.
	 * 
	 * @return the Log instance to use for logging messages.
	 * @throws SQLException
	 *             if an error occurs
	 */
	public Log getLog() throws SQLException {
		return this.log;
	}

	/**
	 * Returns the maximum packet size the MySQL server will accept
	 * 
	 * @return DOCUMENT ME!
	 */
	int getMaxAllowedPacket() {
		return this.maxAllowedPacket;
	}

	protected int getMaxBytesPerChar(String javaCharsetName)
			throws SQLException {
		// TODO: Check if we can actually run this query at this point in time
		String charset = CharsetMapping.getMysqlEncodingForJavaEncoding(
				javaCharsetName, this);

		if (versionMeetsMinimum(4, 1, 0)) {
			Map mapToCheck = null;
			
			if (!getUseDynamicCharsetInfo()) {
				mapToCheck = CharsetMapping.STATIC_CHARSET_TO_NUM_BYTES_MAP;
			} else {
				mapToCheck = this.charsetToNumBytesMap;
			
				synchronized (this.charsetToNumBytesMap) {
					if (this.charsetToNumBytesMap.isEmpty()) {
						
						java.sql.Statement stmt = null;
						java.sql.ResultSet rs = null;
		
						try {
							stmt = getMetadataSafeStatement();
		
							rs = stmt.executeQuery("SHOW CHARACTER SET");
		
							while (rs.next()) {
								this.charsetToNumBytesMap.put(rs.getString("Charset"),
										new Integer(rs.getInt("Maxlen")));
							}
		
							rs.close();
							rs = null;
		
							stmt.close();
		
							stmt = null;
						} finally {
							if (rs != null) {
								rs.close();
								rs = null;
							}
		
							if (stmt != null) {
								stmt.close();
								stmt = null;
							}
						}
					}
				}
			}

			Integer mbPerChar = (Integer) mapToCheck.get(charset);

			if (mbPerChar != null) {
				return mbPerChar.intValue();
			}

			return 1; // we don't know
		}

		return 1; // we don't know
	}

	/**
	 * A connection's database is able to provide information describing its
	 * tables, its supported SQL grammar, its stored procedures, the
	 * capabilities of this connection, etc. This information is made available
	 * through a DatabaseMetaData object.
	 * 
	 * @return a DatabaseMetaData object for this connection
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.sql.DatabaseMetaData getMetaData() throws SQLException {
		checkClosed();

		if (getUseInformationSchema() &&
				this.versionMeetsMinimum(5, 0, 7)) {
			return new DatabaseMetaDataUsingInfoSchema(this, this.database);
		}
			
		return new DatabaseMetaData(this, this.database);
	}

	protected java.sql.Statement getMetadataSafeStatement() throws SQLException {
		java.sql.Statement stmt = createStatement();

		if (stmt.getMaxRows() != 0) {
			stmt.setMaxRows(0);
		}

		stmt.setEscapeProcessing(false);

		return stmt;
	}

	/**
	 * Returns the Mutex all queries are locked against
	 * 
	 * @return DOCUMENT ME!
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	Object getMutex() throws SQLException {
		if (this.io == null) {
			throw SQLError.createSQLException(
					"Connection.close() has already been called. Invalid operation in this state.",
					SQLError.SQL_STATE_CONNECTION_NOT_OPEN);
		}

		reportMetricsIfNeeded();

		return this.mutex;
	}

	/**
	 * Returns the packet buffer size the MySQL server reported upon connection
	 * 
	 * @return DOCUMENT ME!
	 */
	int getNetBufferLength() {
		return this.netBufferLength;
	}

	/**
	 * Returns the server's character set
	 * 
	 * @return the server's character set.
	 */
	protected String getServerCharacterEncoding() {
		if (this.io.versionMeetsMinimum(4, 1, 0)) {
			return (String) this.serverVariables.get("character_set_server");
		} else {
			return (String) this.serverVariables.get("character_set");
		}
	}

	int getServerMajorVersion() {
		return this.io.getServerMajorVersion();
	}

	int getServerMinorVersion() {
		return this.io.getServerMinorVersion();
	}

	int getServerSubMinorVersion() {
		return this.io.getServerSubMinorVersion();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public TimeZone getServerTimezoneTZ() {
		return this.serverTimezoneTZ;
	}

	String getServerVariable(String variableName) {
		if (this.serverVariables != null) {
			return (String) this.serverVariables.get(variableName);
		}

		return null;
	}

	String getServerVersion() {
		return this.io.getServerVersion();
	}

	protected Calendar getSessionLockedCalendar() {
	
		return this.sessionCalendar;
	}
	
	
	/**
	 * Get this Connection's current transaction isolation mode.
	 * 
	 * @return the current TRANSACTION_ mode value
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public int getTransactionIsolation() throws SQLException {

		if (this.hasIsolationLevels && !getUseLocalSessionState()) {
			java.sql.Statement stmt = null;
			java.sql.ResultSet rs = null;

			try {
				stmt = getMetadataSafeStatement();

				String query = null;

				int offset = 0;
				
				if (versionMeetsMinimum(4, 0, 3)) {
					query = "SELECT @@session.tx_isolation";
					offset = 1;
				} else {
					query = "SHOW VARIABLES LIKE 'transaction_isolation'";
					offset = 2;
				}

				rs = stmt.executeQuery(query);

				if (rs.next()) {
					String s = rs.getString(offset);

					if (s != null) {
						Integer intTI = (Integer) mapTransIsolationNameToValue
								.get(s);

						if (intTI != null) {
							return intTI.intValue();
						}
					}

					throw SQLError.createSQLException(
							"Could not map transaction isolation '" + s
									+ " to a valid JDBC level.",
							SQLError.SQL_STATE_GENERAL_ERROR);
				}

				throw SQLError.createSQLException(
						"Could not retrieve transaction isolation level from server",
						SQLError.SQL_STATE_GENERAL_ERROR);

			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (Exception ex) {
						// ignore
						;
					}

					rs = null;
				}

				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception ex) {
						// ignore
						;
					}

					stmt = null;
				}
			}
		}

		return this.isolationLevel;
	}

	/**
	 * JDBC 2.0 Get the type-map object associated with this connection. By
	 * default, the map returned is empty.
	 * 
	 * @return the type map
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public synchronized java.util.Map getTypeMap() throws SQLException {
		if (this.typeMap == null) {
			this.typeMap = new HashMap();
		}

		return this.typeMap;
	}

	String getURL() {
		return this.myURL;
	}

	String getUser() {
		return this.user;
	}

	protected Calendar getUtcCalendar() {
		return this.utcCalendar;
	}

	/**
	 * The first warning reported by calls on this Connection is returned.
	 * <B>Note:</B> Sebsequent warnings will be changed to this
	 * java.sql.SQLWarning
	 * 
	 * @return the first java.sql.SQLWarning or null
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	public boolean hasSameProperties(Connection c) {
		return this.props.equals(c.props);
	}

	protected void incrementNumberOfPreparedExecutes() {
		if (getGatherPerformanceMetrics()) {
			this.numberOfPreparedExecutes++;

			// We need to increment this, because
			// server-side prepared statements bypass
			// any execution by the connection itself...
			this.numberOfQueriesIssued++;
		}
	}
	
	protected void incrementNumberOfPrepares() {
		if (getGatherPerformanceMetrics()) {
			this.numberOfPrepares++;
		}
	}

	protected void incrementNumberOfResultSetsCreated() {
		if (getGatherPerformanceMetrics()) {
			this.numberOfResultSetsCreated++;
		}
	}

	/**
	 * Initializes driver properties that come from URL or properties passed to
	 * the driver manager.
	 * 
	 * @param info
	 *            DOCUMENT ME!
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	private void initializeDriverProperties(Properties info)
			throws SQLException {
		initializeProperties(info);
		
		this.usePlatformCharsetConverters = getUseJvmCharsetConverters();

		this.log = LogFactory.getLogger(getLogger(), LOGGER_INSTANCE_NAME);

		if (getProfileSql() || getUseUsageAdvisor()) {
			this.eventSink = ProfileEventSink.getInstance(this);
		}

		if (getCachePreparedStatements()) {
			createPreparedStatementCaches();		
		}

		if (getNoDatetimeStringSync() && getUseTimezone()) {
			throw SQLError.createSQLException(
					"Can't enable noDatetimeSync and useTimezone configuration "
							+ "properties at the same time",
					SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE);
		}
		
		if (getCacheCallableStatements()) {
			this.parsedCallableStatementCache = new LRUCache(
					getCallableStatementCacheSize());
		}
		
		if (getAllowMultiQueries()) {
			setCacheResultSetMetadata(false); // we don't handle this yet
		}
		
		if (getCacheResultSetMetadata()) {
			this.resultSetMetadataCache = new LRUCache(
					getMetadataCacheSize());
		}
	}

	/**
	 * Sets varying properties that depend on server information. Called once we
	 * have connected to the server.
	 * 
	 * @param info
	 *            DOCUMENT ME!
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	private void initializePropsFromServer() throws SQLException {
		setSessionVariables();

		//
		// the "boolean" type didn't come along until MySQL-4.1
		//

		if (!versionMeetsMinimum(4, 1, 0)) {
			setTransformedBitIsBoolean(false);
		}

		this.parserKnowsUnicode = versionMeetsMinimum(4, 1, 0);

		//
		// Users can turn off detection of server-side prepared statements
		//
		if (getUseServerPreparedStmts() && versionMeetsMinimum(4, 1, 0)) {
			this.useServerPreparedStmts = true;

			if (versionMeetsMinimum(5, 0, 0) && !versionMeetsMinimum(5, 0, 3)) {
				this.useServerPreparedStmts = false; // 4.1.2+ style prepared
				// statements
				// don't work on these versions
			}
		}

		this.serverVariables.clear();

		//
		// If version is greater than 3.21.22 get the server
		// variables.
		if (versionMeetsMinimum(3, 21, 22)) {
			loadServerVariables();

			buildCollationMapping();

			LicenseConfiguration.checkLicenseType(this.serverVariables);

			String lowerCaseTables = (String) this.serverVariables
					.get("lower_case_table_names");

			this.lowerCaseTableNames = "on".equalsIgnoreCase(lowerCaseTables)
					|| "1".equalsIgnoreCase(lowerCaseTables)
					|| "2".equalsIgnoreCase(lowerCaseTables);

			configureTimezone();

			if (this.serverVariables.containsKey("max_allowed_packet")) {
				this.maxAllowedPacket = getServerVariableAsInt("max_allowed_packet", 1024 * 1024);
				
				int preferredBlobSendChunkSize = getBlobSendChunkSize();
				
				int allowedBlobSendChunkSize = Math.min(preferredBlobSendChunkSize, 
						this.maxAllowedPacket) - 
						ServerPreparedStatement.BLOB_STREAM_READ_BUF_SIZE 
						- 11 /* LONG_DATA and MySQLIO packet header size */;
				
				setBlobSendChunkSize(String.valueOf(allowedBlobSendChunkSize));
			}

			if (this.serverVariables.containsKey("net_buffer_length")) {
				this.netBufferLength = getServerVariableAsInt("net_buffer_length", 16 * 1024);
			}

			checkTransactionIsolationLevel();
			
			if (!versionMeetsMinimum(4, 1, 0)) {
				checkServerEncoding();
			}

			this.io.checkForCharsetMismatch();

			if (this.serverVariables.containsKey("sql_mode")) {
				int sqlMode = 0;

				String sqlModeAsString = (String) this.serverVariables
						.get("sql_mode");
				try {
					sqlMode = Integer.parseInt(sqlModeAsString);
				} catch (NumberFormatException nfe) {
					// newer versions of the server has this as a string-y
					// list...
					sqlMode = 0;

					if (sqlModeAsString != null) {
						if (sqlModeAsString.indexOf("ANSI_QUOTES") != -1) {
							sqlMode |= 4;
						}

						if (sqlModeAsString.indexOf("NO_BACKSLASH_ESCAPES") != -1) {
							this.noBackslashEscapes = true;
						}
					}
				}

				if ((sqlMode & 4) > 0) {
					this.useAnsiQuotes = true;
				} else {
					this.useAnsiQuotes = false;
				}
			}
		}
		
		this.errorMessageEncoding = 
			CharsetMapping.getCharacterEncodingForErrorMessages(this);
		
		
		boolean overrideDefaultAutocommit = isAutoCommitNonDefaultOnServer();
	
		configureClientCharacterSet();

		if (versionMeetsMinimum(3, 23, 15)) {
			this.transactionsSupported = true;
			
			if (!overrideDefaultAutocommit) {
				setAutoCommit(true); // to override anything
				// the server is set to...reqd
				// by JDBC spec.
			}
		} else {
			this.transactionsSupported = false;
		}
		

		if (versionMeetsMinimum(3, 23, 36)) {
			this.hasIsolationLevels = true;
		} else {
			this.hasIsolationLevels = false;
		}

		this.hasQuotedIdentifiers = versionMeetsMinimum(3, 23, 6);

		this.io.resetMaxBuf();

		//
		// If we're using MySQL 4.1.0 or newer, we need to figure
		// out what character set metadata will be returned in,
		// and then map that to a Java encoding name.
		//
		// We've already set it, and it might be different than what
		// was originally on the server, which is why we use the
		// "special" key to retrieve it
		if (this.io.versionMeetsMinimum(4, 1, 0)) {
			String characterSetResultsOnServerMysql = (String) this.serverVariables
					.get(JDBC_LOCAL_CHARACTER_SET_RESULTS);

			if (characterSetResultsOnServerMysql == null
					|| StringUtils.startsWithIgnoreCaseAndWs(
							characterSetResultsOnServerMysql, "NULL")
					|| characterSetResultsOnServerMysql.length() == 0) {
				String defaultMetadataCharsetMysql = (String) this.serverVariables
						.get("character_set_system");
				String defaultMetadataCharset = null;

				if (defaultMetadataCharsetMysql != null) {
					defaultMetadataCharset = CharsetMapping
							.getJavaEncodingForMysqlEncoding(
									defaultMetadataCharsetMysql, this);
				} else {
					defaultMetadataCharset = "UTF-8";
				}

				this.characterSetMetadata = defaultMetadataCharset;
			} else {
				this.characterSetResultsOnServer = CharsetMapping
						.getJavaEncodingForMysqlEncoding(
								characterSetResultsOnServerMysql, this);
				this.characterSetMetadata = this.characterSetResultsOnServer;
			}
		}

		//
		// Query cache is broken wrt. multi-statements before MySQL-4.1.10
		//

		if (this.versionMeetsMinimum(4, 1, 0)
				&& !this.versionMeetsMinimum(4, 1, 10)
				&& getAllowMultiQueries()) {
			if ("ON".equalsIgnoreCase((String) this.serverVariables
					.get("query_cache_type"))
					&& !"0".equalsIgnoreCase((String) this.serverVariables
							.get("query_cache_size"))) {
				setAllowMultiQueries(false);
			}
		}
		
		//
		// Server can do this more efficiently for us
		//
		
		setupServerForTruncationChecks();
	}

	private int getServerVariableAsInt(String variableName, int fallbackValue)
			throws SQLException {
		try {
			return Integer.parseInt((String) this.serverVariables
					.get(variableName));
		} catch (NumberFormatException nfe) {
			getLog().logWarn(Messages.getString("Connection.BadValueInServerVariables", new Object[] {variableName, 
					this.serverVariables.get(variableName), new Integer(fallbackValue)}));
			
			return fallbackValue;
		}
	}
	
	/**
	 * Has the default autocommit value of 0 been changed on the server
	 * via init_connect?
	 * 
	 * @return true if autocommit is not the default of '0' on the server.
	 * 
	 * @throws SQLException
	 */
	private boolean isAutoCommitNonDefaultOnServer() throws SQLException {
		boolean overrideDefaultAutocommit = false;
		
		String initConnectValue = (String) this.serverVariables
		.get("init_connect");

		if (versionMeetsMinimum(4, 1, 2) && initConnectValue != null
				&& initConnectValue.length() > 0) {
			if (!getElideSetAutoCommits()) {
				// auto-commit might have changed
				java.sql.ResultSet rs = null;
				java.sql.Statement stmt = null;
				
				try {
					stmt = getMetadataSafeStatement();
					
					rs = stmt.executeQuery("SELECT @@session.autocommit");
					
					if (rs.next()) {
						this.autoCommit = rs.getBoolean(1);
						if (this.autoCommit != true) {
							overrideDefaultAutocommit = true;
						}
					}
					
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException sqlEx) {
							// do nothing
						}
					}
					
					if (stmt != null) {
						try {
							stmt.close();
						} catch (SQLException sqlEx) {
							// do nothing
						}
					}
				}
			} else {
				if (this.getIO().isSetNeededForAutoCommitMode(true)) {
					// we're not in standard autocommit=true mode
					this.autoCommit = false;
					overrideDefaultAutocommit = true;
				}
			}
		}
		
		return overrideDefaultAutocommit;
	}

	private void setupServerForTruncationChecks() throws SQLException {
		if (getJdbcCompliantTruncation()) {
			if (versionMeetsMinimum(5, 0, 2)) {
				
				String currentSqlMode = 
					(String)this.serverVariables.get("sql_mode");
				
				boolean strictTransTablesIsSet = StringUtils.indexOfIgnoreCase(currentSqlMode, "STRICT_TRANS_TABLES") != -1;
				
				if (currentSqlMode == null ||
						currentSqlMode.length() == 0 || !strictTransTablesIsSet) {
					StringBuffer commandBuf = new StringBuffer("SET sql_mode='");
					
					if (currentSqlMode != null && currentSqlMode.length() > 0) {
						commandBuf.append(currentSqlMode);
						commandBuf.append(",");
					}
					
					commandBuf.append("STRICT_TRANS_TABLES'");
					
					execSQL(null,  commandBuf.toString(), -1, null,
							java.sql.ResultSet.TYPE_FORWARD_ONLY,
							java.sql.ResultSet.CONCUR_READ_ONLY, false,
							this.database, true, false);
					
					setJdbcCompliantTruncation(false); // server's handling this for us now
				} else if (strictTransTablesIsSet) {
					// We didn't set it, but someone did, so we piggy back on it
					setJdbcCompliantTruncation(false); // server's handling this for us now
				}
				
			}
		}
	}

	protected boolean isClientTzUTC() {
		return this.isClientTzUTC;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isClosed() {
		return this.isClosed;
	}

	protected boolean isCursorFetchEnabled() throws SQLException {
		return (versionMeetsMinimum(5, 0, 2) && getUseCursorFetch());
	}

	public boolean isInGlobalTx() {
		return this.isInGlobalTx;
	}

	/**
	 * Is this connection connected to the first host in the list if
	 * there is a list of servers in the URL?
	 * 
	 * @return true if this connection is connected to the first in 
	 * the list.
	 */
	public synchronized boolean isMasterConnection() {
		return !this.failedOver;
	}

	/**
	 * Is the server in a sql_mode that doesn't allow us to use \\ to escape
	 * things?
	 * 
	 * @return Returns the noBackslashEscapes.
	 */
	public boolean isNoBackslashEscapesSet() {
		return this.noBackslashEscapes;
	}

	boolean isReadInfoMsgEnabled() {
		return this.readInfoMsg;
	}

	/**
	 * Tests to see if the connection is in Read Only Mode. Note that we cannot
	 * really put the database in read only mode, but we pretend we can by
	 * returning the value of the readOnly flag
	 * 
	 * @return true if the connection is read only
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public boolean isReadOnly() throws SQLException {
		return this.readOnly;
	}

	protected boolean isRunningOnJDK13() {
		return this.isRunningOnJDK13;
	}

	public synchronized boolean isSameResource(Connection otherConnection) {
		if (otherConnection == null) {
			return false;
		}
		
		boolean directCompare = true;
		
		String otherHost = otherConnection.origHostToConnectTo;
		String otherOrigDatabase = otherConnection.origDatabaseToConnectTo;
		String otherCurrentCatalog = otherConnection.database;
		
		if (!nullSafeCompare(otherHost, this.origHostToConnectTo)) {
			directCompare = false;
		} else if (otherHost != null && otherHost.indexOf(",") == -1 && 
				otherHost.indexOf(":") == -1) {
			// need to check port numbers
			directCompare = (otherConnection.origPortToConnectTo == 
				this.origPortToConnectTo);
		}
		
		if (directCompare) {
			if (!nullSafeCompare(otherOrigDatabase, this.origDatabaseToConnectTo)) {			directCompare = false;
				directCompare = false;
			} else if (!nullSafeCompare(otherCurrentCatalog, this.database)) {
				directCompare = false;
			}
		}

		if (directCompare) {
			return true;
		}
		
		// Has the user explicitly set a resourceId?
		String otherResourceId = otherConnection.getResourceId();
		String myResourceId = getResourceId();
		
		if (otherResourceId != null || myResourceId != null) {
			directCompare = nullSafeCompare(otherResourceId, myResourceId);
			
			if (directCompare) {
				return true;
			}
		}
		
		return false;	
	}

	protected boolean isServerTzUTC() {
		return this.isServerTzUTC;
	}

	private boolean usingCachedConfig = false;
	
	/**
	 * Loads the result of 'SHOW VARIABLES' into the serverVariables field so
	 * that the driver can configure itself.
	 * 
	 * @throws SQLException
	 *             if the 'SHOW VARIABLES' query fails for any reason.
	 */
	private void loadServerVariables() throws SQLException {

		if (getCacheServerConfiguration()) {
			synchronized (serverConfigByUrl) {
				Map cachedVariableMap = (Map) serverConfigByUrl.get(getURL());

				if (cachedVariableMap != null) {
					this.serverVariables = cachedVariableMap;
					this.usingCachedConfig = true;
					
					return;
				}
			}
		}

		com.mysql.jdbc.Statement stmt = null;
		com.mysql.jdbc.ResultSet results = null;

		try {
			stmt = (com.mysql.jdbc.Statement) createStatement();
			stmt.setEscapeProcessing(false);

			results = (com.mysql.jdbc.ResultSet) stmt
					.executeQuery("SHOW SESSION VARIABLES");

			while (results.next()) {
				this.serverVariables.put(results.getString(1), results
						.getString(2));
			}

			if (getCacheServerConfiguration()) {
				synchronized (serverConfigByUrl) {
					serverConfigByUrl.put(getURL(), this.serverVariables);
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException sqlE) {
					;
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlE) {
					;
				}
			}
		}
	}

	/**
	 * Is the server configured to use lower-case table names only?
	 * 
	 * @return true if lower_case_table_names is 'on'
	 */
	public boolean lowerCaseTableNames() {
		return this.lowerCaseTableNames;
	}

	/**
	 * Has the maxRows value changed?
	 * 
	 * @param stmt
	 *            DOCUMENT ME!
	 */
	void maxRowsChanged(Statement stmt) {
		synchronized (this.mutex) {
			if (this.statementsUsingMaxRows == null) {
				this.statementsUsingMaxRows = new HashMap();
			}

			this.statementsUsingMaxRows.put(stmt, stmt);

			this.maxRowsChanged = true;
		}
	}

	/**
	 * A driver may convert the JDBC sql grammar into its system's native SQL
	 * grammar prior to sending it; nativeSQL returns the native form of the
	 * statement that the driver would have sent.
	 * 
	 * @param sql
	 *            a SQL statement that may contain one or more '?' parameter
	 *            placeholders
	 * @return the native form of this statement
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public String nativeSQL(String sql) throws SQLException {
		if (sql == null) {
			return null;
		}

		Object escapedSqlResult = EscapeProcessor.escapeSQL(sql,
				serverSupportsConvertFn(),
				this);

		if (escapedSqlResult instanceof String) {
			return (String) escapedSqlResult;
		}

		return ((EscapeProcessorResult) escapedSqlResult).escapedSql;
	}

	private CallableStatement parseCallableStatement(String sql)
			throws SQLException {
		Object escapedSqlResult = EscapeProcessor.escapeSQL(sql,
				serverSupportsConvertFn(), this);

		boolean isFunctionCall = false;
		String parsedSql = null;

		if (escapedSqlResult instanceof EscapeProcessorResult) {
			parsedSql = ((EscapeProcessorResult) escapedSqlResult).escapedSql;
			isFunctionCall = ((EscapeProcessorResult) escapedSqlResult).callingStoredFunction;
		} else {
			parsedSql = (String) escapedSqlResult;
			isFunctionCall = false;
		}

		return new CallableStatement(this, parsedSql, this.database,
				isFunctionCall);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean parserKnowsUnicode() {
		return this.parserKnowsUnicode;
	}

	/**
	 * Detect if the connection is still good
	 * 
	 * @throws SQLException
	 *             if the ping fails
	 */
	public void ping() throws SQLException {
		pingInternal(true);
	}

	private void pingInternal(boolean checkForClosedConnection)
			throws SQLException {
		if (checkForClosedConnection) {
			checkClosed();
		}

		// Need MySQL-3.22.1, but who uses anything older!?
		this.io.sendCommand(MysqlDefs.PING, null, null, false, null);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param sql
	 *            DOCUMENT ME!
	 * @return DOCUMENT ME!
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public java.sql.CallableStatement prepareCall(String sql)
			throws SQLException {
		if (this.getUseUltraDevWorkAround()) {
			return new UltraDevWorkAround(prepareStatement(sql));
		}

		return prepareCall(sql, java.sql.ResultSet.TYPE_FORWARD_ONLY,
				java.sql.ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * JDBC 2.0 Same as prepareCall() above, but allows the default result set
	 * type and result set concurrency type to be overridden.
	 * 
	 * @param sql
	 *            the SQL representing the callable statement
	 * @param resultSetType
	 *            a result set type, see ResultSet.TYPE_XXX
	 * @param resultSetConcurrency
	 *            a concurrency type, see ResultSet.CONCUR_XXX
	 * @return a new CallableStatement object containing the pre-compiled SQL
	 *         statement
	 * @exception SQLException
	 *                if a database-access error occurs.
	 */
	public java.sql.CallableStatement prepareCall(String sql,
			int resultSetType, int resultSetConcurrency) throws SQLException {
		if (versionMeetsMinimum(5, 0, 0)) {
			CallableStatement cStmt = null;

			if (!getCacheCallableStatements()) {

				cStmt = parseCallableStatement(sql);
			} else {
				synchronized (this.parsedCallableStatementCache) {
					CompoundCacheKey key = new CompoundCacheKey(getCatalog(), sql);
	
					CallableStatement.CallableStatementParamInfo cachedParamInfo = (CallableStatement.CallableStatementParamInfo) this.parsedCallableStatementCache
							.get(key);
	
					if (cachedParamInfo != null) {
						cStmt = new CallableStatement(this, cachedParamInfo);
					} else {
						cStmt = parseCallableStatement(sql);
	
						cachedParamInfo = cStmt.paramInfo;
	
						this.parsedCallableStatementCache.put(key, cachedParamInfo);
					}
				}
			}

			cStmt.setResultSetType(resultSetType);
			cStmt.setResultSetConcurrency(resultSetConcurrency);

			return cStmt;
		}

		throw SQLError.createSQLException("Callable statements not " + "supported.",
				SQLError.SQL_STATE_DRIVER_NOT_CAPABLE);
	}

	/**
	 * @see Connection#prepareCall(String, int, int, int)
	 */
	public java.sql.CallableStatement prepareCall(String sql,
			int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		if (getPedantic()) {
			if (resultSetHoldability != java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT) {
				throw SQLError.createSQLException(
						"HOLD_CUSRORS_OVER_COMMIT is only supported holdability level",
						SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
			}
		}

		CallableStatement cStmt = (com.mysql.jdbc.CallableStatement) prepareCall(
				sql, resultSetType, resultSetConcurrency);

		return cStmt;
	}

	/**
	 * A SQL statement with or without IN parameters can be pre-compiled and
	 * stored in a PreparedStatement object. This object can then be used to
	 * efficiently execute this statement multiple times.
	 * <p>
	 * <B>Note:</B> This method is optimized for handling parametric SQL
	 * statements that benefit from precompilation if the driver supports
	 * precompilation. In this case, the statement is not sent to the database
	 * until the PreparedStatement is executed. This has no direct effect on
	 * users; however it does affect which method throws certain
	 * java.sql.SQLExceptions
	 * </p>
	 * <p>
	 * MySQL does not support precompilation of statements, so they are handled
	 * by the driver.
	 * </p>
	 * 
	 * @param sql
	 *            a SQL statement that may contain one or more '?' IN parameter
	 *            placeholders
	 * @return a new PreparedStatement object containing the pre-compiled
	 *         statement.
	 * @exception SQLException
	 *                if a database access error occurs.
	 */
	public java.sql.PreparedStatement prepareStatement(String sql)
			throws SQLException {
		return prepareStatement(sql, java.sql.ResultSet.TYPE_FORWARD_ONLY,
				java.sql.ResultSet.CONCUR_READ_ONLY);
	}

	/**
	 * @see Connection#prepareStatement(String, int)
	 */
	public java.sql.PreparedStatement prepareStatement(String sql,
			int autoGenKeyIndex) throws SQLException {
		java.sql.PreparedStatement pStmt = prepareStatement(sql);

		((com.mysql.jdbc.PreparedStatement) pStmt)
				.setRetrieveGeneratedKeys(autoGenKeyIndex == java.sql.Statement.RETURN_GENERATED_KEYS);

		return pStmt;
	}

	/**
	 * JDBC 2.0 Same as prepareStatement() above, but allows the default result
	 * set type and result set concurrency type to be overridden.
	 * 
	 * @param sql
	 *            the SQL query containing place holders
	 * @param resultSetType
	 *            a result set type, see ResultSet.TYPE_XXX
	 * @param resultSetConcurrency
	 *            a concurrency type, see ResultSet.CONCUR_XXX
	 * @return a new PreparedStatement object containing the pre-compiled SQL
	 *         statement
	 * @exception SQLException
	 *                if a database-access error occurs.
	 */
	public java.sql.PreparedStatement prepareStatement(String sql,
			int resultSetType, int resultSetConcurrency) throws SQLException {
		checkClosed();

		//
		// FIXME: Create warnings if can't create results of the given
		// type or concurrency
		//
		PreparedStatement pStmt = null;
		
		boolean canServerPrepare = true;
		
		String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql): sql;
		
		if (getEmulateUnsupportedPstmts()) {
			canServerPrepare = canHandleAsServerPreparedStatement(nativeSql);
		}
		
		if (this.useServerPreparedStmts && canServerPrepare) {
			if (this.getCachePreparedStatements()) {
				synchronized (this.serverSideStatementCache) {
					pStmt = (com.mysql.jdbc.ServerPreparedStatement)this.serverSideStatementCache.remove(sql);
					
					if (pStmt != null) {
						((com.mysql.jdbc.ServerPreparedStatement)pStmt).setClosed(false);
						pStmt.clearParameters();
					}

					if (pStmt == null) {
						try {
							pStmt = new com.mysql.jdbc.ServerPreparedStatement(this, nativeSql,
									this.database, resultSetType, resultSetConcurrency);
							if (sql.length() < getPreparedStatementCacheSqlLimit()) {
								((com.mysql.jdbc.ServerPreparedStatement)pStmt).isCached = true;
							}
						} catch (SQLException sqlEx) {
							// Punt, if necessary
							if (getEmulateUnsupportedPstmts()) {
								pStmt = clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
								
								if (sql.length() < getPreparedStatementCacheSqlLimit()) {
									this.serverSideStatementCheckCache.put(sql, Boolean.FALSE);
								}
							} else {
								throw sqlEx;
							}
						}
					}
				}
			} else {
				try {
					pStmt = new com.mysql.jdbc.ServerPreparedStatement(this, nativeSql,
							this.database, resultSetType, resultSetConcurrency);
				} catch (SQLException sqlEx) {
					// Punt, if necessary
					if (getEmulateUnsupportedPstmts()) {
						pStmt = clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
					} else {
						throw sqlEx;
					}
				}
			}
		} else {
			pStmt = clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
		}

		return pStmt;
	}

	/**
	 * @see Connection#prepareStatement(String, int, int, int)
	 */
	public java.sql.PreparedStatement prepareStatement(String sql,
			int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		if (getPedantic()) {
			if (resultSetHoldability != java.sql.ResultSet.HOLD_CURSORS_OVER_COMMIT) {
				throw SQLError.createSQLException(
						"HOLD_CUSRORS_OVER_COMMIT is only supported holdability level",
						SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
			}
		}

		return prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	/**
	 * @see Connection#prepareStatement(String, int[])
	 */
	public java.sql.PreparedStatement prepareStatement(String sql,
			int[] autoGenKeyIndexes) throws SQLException {
		java.sql.PreparedStatement pStmt = prepareStatement(sql);

		((com.mysql.jdbc.PreparedStatement) pStmt)
				.setRetrieveGeneratedKeys((autoGenKeyIndexes != null)
						&& (autoGenKeyIndexes.length > 0));

		return pStmt;
	}

	/**
	 * @see Connection#prepareStatement(String, String[])
	 */
	public java.sql.PreparedStatement prepareStatement(String sql,
			String[] autoGenKeyColNames) throws SQLException {
		java.sql.PreparedStatement pStmt = prepareStatement(sql);

		((com.mysql.jdbc.PreparedStatement) pStmt)
				.setRetrieveGeneratedKeys((autoGenKeyColNames != null)
						&& (autoGenKeyColNames.length > 0));

		return pStmt;
	}

	/**
	 * Closes connection and frees resources.
	 * 
	 * @param calledExplicitly
	 *            is this being called from close()
	 * @param issueRollback
	 *            should a rollback() be issued?
	 * @throws SQLException
	 *             if an error occurs
	 */
	protected void realClose(boolean calledExplicitly, boolean issueRollback,
			boolean skipLocalTeardown, Throwable reason) throws SQLException {
		SQLException sqlEx = null;

		if (this.isClosed()) {
			return;
		}
		
		this.forceClosedReason = reason;
		
		try {
			if (!skipLocalTeardown) {
				if (!getAutoCommit() && issueRollback) {
					try {
						rollback();
					} catch (SQLException ex) {
						sqlEx = ex;
					}
				}

				reportMetrics();

				if (getUseUsageAdvisor()) {
					if (!calledExplicitly) {
						String message = "Connection implicitly closed by Driver. You should call Connection.close() from your code to free resources more efficiently and avoid resource leaks.";

						this.eventSink.consumeEvent(new ProfilerEvent(
								ProfilerEvent.TYPE_WARN, "", //$NON-NLS-1$
								this.getCatalog(), this.getId(), -1, -1, System
										.currentTimeMillis(), 0, Constants.MILLIS_I18N,
										null,
								this.pointOfOrigin, message));
					}

					long connectionLifeTime = System.currentTimeMillis()
							- this.connectionCreationTimeMillis;

					if (connectionLifeTime < 500) {
						String message = "Connection lifetime of < .5 seconds. You might be un-necessarily creating short-lived connections and should investigate connection pooling to be more efficient.";

						this.eventSink.consumeEvent(new ProfilerEvent(
								ProfilerEvent.TYPE_WARN, "", //$NON-NLS-1$
								this.getCatalog(), this.getId(), -1, -1, System
										.currentTimeMillis(), 0, Constants.MILLIS_I18N,
										null,
								this.pointOfOrigin, message));
					}
				}

				try {
					closeAllOpenStatements();
				} catch (SQLException ex) {
					sqlEx = ex;
				}

				if (this.io != null) {
					try {
						this.io.quit();
					} catch (Exception e) {
						;
					}

				}
			} else {
				this.io.forceClose();
			}
		} finally {
			this.openStatements = null;
			this.io = null;
			ProfileEventSink.removeInstance(this);
			this.isClosed = true;
		}

		if (sqlEx != null) {
			throw sqlEx;
		}

	}

	protected void recachePreparedStatement(ServerPreparedStatement pstmt) {
		synchronized (this.serverSideStatementCache) {
			this.serverSideStatementCache.put(pstmt.originalSql, pstmt);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param queryTimeMs
	 */
	protected void registerQueryExecutionTime(long queryTimeMs) {
		if (queryTimeMs > this.longestQueryTimeMs) {
			this.longestQueryTimeMs = queryTimeMs;

			repartitionPerformanceHistogram();
		}

		addToPerformanceHistogram(queryTimeMs, 1);

		if (queryTimeMs < this.shortestQueryTimeMs) {
			this.shortestQueryTimeMs = (queryTimeMs == 0) ? 1 : queryTimeMs;
		}

		this.numberOfQueriesIssued++;

		this.totalQueryTimeMs += queryTimeMs;
	}

	/**
	 * Register a Statement instance as open.
	 * 
	 * @param stmt
	 *            the Statement instance to remove
	 */
	void registerStatement(Statement stmt) {
		synchronized (this.openStatements) {
			this.openStatements.put(stmt, stmt);
		}
	}

	/**
	 * @see Connection#releaseSavepoint(Savepoint)
	 */
	public void releaseSavepoint(Savepoint arg0) throws SQLException {
		// this is a no-op
	}

	private void repartitionHistogram(int[] histCounts, long[] histBreakpoints,
			long currentLowerBound, long currentUpperBound) {

		if (oldHistCounts == null) {
			oldHistCounts = new int[histCounts.length];
			oldHistBreakpoints = new long[histBreakpoints.length];
		}

		for (int i = 0; i < histCounts.length; i++) {
			oldHistCounts[i] = histCounts[i];
		}

		for (int i = 0; i < oldHistBreakpoints.length; i++) {
			oldHistBreakpoints[i] = histBreakpoints[i];
		}

		createInitialHistogram(histBreakpoints, currentLowerBound,
				currentUpperBound);

		for (int i = 0; i < HISTOGRAM_BUCKETS; i++) {
			addToHistogram(histCounts, histBreakpoints, oldHistBreakpoints[i],
					oldHistCounts[i], currentLowerBound, currentUpperBound);
		}
	}

	private void repartitionPerformanceHistogram() {
		checkAndCreatePerformanceHistogram();

		repartitionHistogram(this.perfMetricsHistCounts,
				this.perfMetricsHistBreakpoints,
				this.shortestQueryTimeMs == Long.MAX_VALUE ? 0
						: this.shortestQueryTimeMs, this.longestQueryTimeMs);
	}

	private void repartitionTablesAccessedHistogram() {
		checkAndCreateTablesAccessedHistogram();

		repartitionHistogram(this.numTablesMetricsHistCounts,
				this.numTablesMetricsHistBreakpoints,
				this.minimumNumberTablesAccessed == Long.MAX_VALUE ? 0
						: this.minimumNumberTablesAccessed,
				this.maximumNumberTablesAccessed);
	}

	private void reportMetrics() {
		if (getGatherPerformanceMetrics()) {
			StringBuffer logMessage = new StringBuffer(256);

			logMessage.append("** Performance Metrics Report **\n");
			logMessage.append("\nLongest reported query: "
					+ this.longestQueryTimeMs + " ms");
			logMessage.append("\nShortest reported query: "
					+ this.shortestQueryTimeMs + " ms");
			logMessage
					.append("\nAverage query execution time: "
							+ (this.totalQueryTimeMs / this.numberOfQueriesIssued)
							+ " ms");
			logMessage.append("\nNumber of statements executed: "
					+ this.numberOfQueriesIssued);
			logMessage.append("\nNumber of result sets created: "
					+ this.numberOfResultSetsCreated);
			logMessage.append("\nNumber of statements prepared: "
					+ this.numberOfPrepares);
			logMessage.append("\nNumber of prepared statement executions: "
					+ this.numberOfPreparedExecutes);

			if (this.perfMetricsHistBreakpoints != null) {
				logMessage.append("\n\n\tTiming Histogram:\n");
				int maxNumPoints = 20;
				int highestCount = Integer.MIN_VALUE;

				for (int i = 0; i < (HISTOGRAM_BUCKETS); i++) {
					if (this.perfMetricsHistCounts[i] > highestCount) {
						highestCount = this.perfMetricsHistCounts[i];
					}
				}

				if (highestCount == 0) {
					highestCount = 1; // avoid DIV/0
				}

				for (int i = 0; i < (HISTOGRAM_BUCKETS - 1); i++) {

					if (i == 0) {
						logMessage.append("\n\tless than "
								+ this.perfMetricsHistBreakpoints[i + 1]
								+ " ms: \t" + this.perfMetricsHistCounts[i]);
					} else {
						logMessage.append("\n\tbetween "
								+ this.perfMetricsHistBreakpoints[i] + " and "
								+ this.perfMetricsHistBreakpoints[i + 1]
								+ " ms: \t" + this.perfMetricsHistCounts[i]);
					}

					logMessage.append("\t");

					int numPointsToGraph = (int) (maxNumPoints * ((double) this.perfMetricsHistCounts[i] / (double) highestCount));

					for (int j = 0; j < numPointsToGraph; j++) {
						logMessage.append("*");
					}

					if (this.longestQueryTimeMs < this.perfMetricsHistCounts[i + 1]) {
						break;
					}
				}

				if (this.perfMetricsHistBreakpoints[HISTOGRAM_BUCKETS - 2] < this.longestQueryTimeMs) {
					logMessage.append("\n\tbetween ");
					logMessage
							.append(this.perfMetricsHistBreakpoints[HISTOGRAM_BUCKETS - 2]);
					logMessage.append(" and ");
					logMessage
							.append(this.perfMetricsHistBreakpoints[HISTOGRAM_BUCKETS - 1]);
					logMessage.append(" ms: \t");
					logMessage
							.append(this.perfMetricsHistCounts[HISTOGRAM_BUCKETS - 1]);
				}
			}

			if (this.numTablesMetricsHistBreakpoints != null) {
				logMessage.append("\n\n\tTable Join Histogram:\n");
				int maxNumPoints = 20;
				int highestCount = Integer.MIN_VALUE;

				for (int i = 0; i < (HISTOGRAM_BUCKETS); i++) {
					if (this.numTablesMetricsHistCounts[i] > highestCount) {
						highestCount = this.numTablesMetricsHistCounts[i];
					}
				}

				if (highestCount == 0) {
					highestCount = 1; // avoid DIV/0
				}

				for (int i = 0; i < (HISTOGRAM_BUCKETS - 1); i++) {

					if (i == 0) {
						logMessage.append("\n\t"
								+ this.numTablesMetricsHistBreakpoints[i + 1]
								+ " tables or less: \t\t"
								+ this.numTablesMetricsHistCounts[i]);
					} else {
						logMessage.append("\n\tbetween "
								+ this.numTablesMetricsHistBreakpoints[i]
								+ " and "
								+ this.numTablesMetricsHistBreakpoints[i + 1]
								+ " tables: \t"
								+ this.numTablesMetricsHistCounts[i]);
					}

					logMessage.append("\t");

					int numPointsToGraph = (int) (maxNumPoints * ((double) this.numTablesMetricsHistCounts[i] / (double) highestCount));

					for (int j = 0; j < numPointsToGraph; j++) {
						logMessage.append("*");
					}

					if (this.maximumNumberTablesAccessed < this.numTablesMetricsHistBreakpoints[i + 1]) {
						break;
					}
				}

				if (this.numTablesMetricsHistBreakpoints[HISTOGRAM_BUCKETS - 2] < this.maximumNumberTablesAccessed) {
					logMessage.append("\n\tbetween ");
					logMessage
							.append(this.numTablesMetricsHistBreakpoints[HISTOGRAM_BUCKETS - 2]);
					logMessage.append(" and ");
					logMessage
							.append(this.numTablesMetricsHistBreakpoints[HISTOGRAM_BUCKETS - 1]);
					logMessage.append(" tables: ");
					logMessage
							.append(this.numTablesMetricsHistCounts[HISTOGRAM_BUCKETS - 1]);
				}
			}

			this.log.logInfo(logMessage);

			this.metricsLastReportedMs = System.currentTimeMillis();
		}
	}

	/**
	 * Reports currently collected metrics if this feature is enabled and the
	 * timeout has passed.
	 */
	private void reportMetricsIfNeeded() {
		if (getGatherPerformanceMetrics()) {
			if ((System.currentTimeMillis() - this.metricsLastReportedMs) > getReportMetricsIntervalMillis()) {
				reportMetrics();
			}
		}
	}

	protected void reportNumberOfTablesAccessed(int numTablesAccessed) {
		if (numTablesAccessed < this.minimumNumberTablesAccessed) {
			this.minimumNumberTablesAccessed = numTablesAccessed;
		}

		if (numTablesAccessed > this.maximumNumberTablesAccessed) {
			this.maximumNumberTablesAccessed = numTablesAccessed;

			repartitionTablesAccessedHistogram();
		}

		addToTablesAccessedHistogram(numTablesAccessed, 1);
	}

	/**
	 * Resets the server-side state of this connection. Doesn't work for MySQL
	 * versions older than 4.0.6 or if isParanoid() is set (it will become a
	 * no-op in these cases). Usually only used from connection pooling code.
	 * 
	 * @throws SQLException
	 *             if the operation fails while resetting server state.
	 */
	public void resetServerState() throws SQLException {
		if (!getParanoid()
				&& ((this.io != null) && versionMeetsMinimum(4, 0, 6))) {
			changeUser(this.user, this.password);
		}
	}

	/**
	 * The method rollback() drops all changes made since the previous
	 * commit/rollback and releases any database locks currently held by the
	 * Connection.
	 * 
	 * @exception SQLException
	 *                if a database access error occurs
	 * @see commit
	 */
	public void rollback() throws SQLException {
		synchronized (getMutex()) {
			checkClosed();
	
			try {
				// no-op if _relaxAutoCommit == true
				if (this.autoCommit && !getRelaxAutoCommit()) {
					throw SQLError.createSQLException(
							"Can't call rollback when autocommit=true",
							SQLError.SQL_STATE_CONNECTION_NOT_OPEN);
				} else if (this.transactionsSupported) {
					try {
						rollbackNoChecks();
					} catch (SQLException sqlEx) {
						// We ignore non-transactional tables if told to do so
						if (getIgnoreNonTxTables()
								&& (sqlEx.getErrorCode() != SQLError.ER_WARNING_NOT_COMPLETE_ROLLBACK)) {
							throw sqlEx;
						}
					}
				}
			} catch (SQLException sqlException) {
				if (SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE
						.equals(sqlException.getSQLState())) {
					throw SQLError.createSQLException(
							"Communications link failure during rollback(). Transaction resolution unknown.",
							SQLError.SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN);
				}
	
				throw sqlException;
			} finally {
				this.needsPing = this.getReconnectAtTxEnd();
			}
		}
	}

	/**
	 * @see Connection#rollback(Savepoint)
	 */
	public void rollback(Savepoint savepoint) throws SQLException {

		if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
			synchronized (getMutex()) {
				checkClosed();
	
				try {
					StringBuffer rollbackQuery = new StringBuffer(
							"ROLLBACK TO SAVEPOINT ");
					rollbackQuery.append('`');
					rollbackQuery.append(savepoint.getSavepointName());
					rollbackQuery.append('`');
	
					java.sql.Statement stmt = null;
	
					try {
						stmt = createStatement();
	
						stmt.executeUpdate(rollbackQuery.toString());
					} catch (SQLException sqlEx) {
						int errno = sqlEx.getErrorCode();
	
						if (errno == 1181) {
							String msg = sqlEx.getMessage();
	
							if (msg != null) {
								int indexOfError153 = msg.indexOf("153");
	
								if (indexOfError153 != -1) {
									throw SQLError.createSQLException("Savepoint '"
											+ savepoint.getSavepointName()
											+ "' does not exist",
											SQLError.SQL_STATE_ILLEGAL_ARGUMENT,
											errno);
								}
							}
						}
	
						// We ignore non-transactional tables if told to do so
						if (getIgnoreNonTxTables()
								&& (sqlEx.getErrorCode() != SQLError.ER_WARNING_NOT_COMPLETE_ROLLBACK)) {
							throw sqlEx;
						}
	
						if (SQLError.SQL_STATE_COMMUNICATION_LINK_FAILURE
								.equals(sqlEx.getSQLState())) {
							throw SQLError.createSQLException(
									"Communications link failure during rollback(). Transaction resolution unknown.",
									SQLError.SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN);
						}
	
						throw sqlEx;
					} finally {
						closeStatement(stmt);
					}
				} finally {
					this.needsPing = this.getReconnectAtTxEnd();
				}
			}
		} else {
			throw new NotImplemented();
		}
	}

	private void rollbackNoChecks() throws SQLException {
		if (getUseLocalSessionState() && versionMeetsMinimum(5, 0, 0)) {
			if (!this.io.inTransactionOnServer()) {
				return; // effectively a no-op
			}
		}
		
		execSQL(null, "rollback", -1, null,
				java.sql.ResultSet.TYPE_FORWARD_ONLY,
				java.sql.ResultSet.CONCUR_READ_ONLY, false,
				this.database, true, false);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param sql
	 *            DOCUMENT ME!
	 * @return DOCUMENT ME!
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public ServerPreparedStatement serverPrepare(String sql)
			throws SQLException {

		String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql): sql;
		
		return new ServerPreparedStatement(this, nativeSql, this.getCatalog(),
				java.sql.ResultSet.TYPE_SCROLL_SENSITIVE,
				java.sql.ResultSet.CONCUR_READ_ONLY);
	}

	protected boolean serverSupportsConvertFn() throws SQLException {
		return versionMeetsMinimum(4, 0, 2);
	}

	/**
	 * If a connection is in auto-commit mode, than all its SQL statements will
	 * be executed and committed as individual transactions. Otherwise, its SQL
	 * statements are grouped into transactions that are terminated by either
	 * commit() or rollback(). By default, new connections are in auto- commit
	 * mode. The commit occurs when the statement completes or the next execute
	 * occurs, whichever comes first. In the case of statements returning a
	 * ResultSet, the statement completes when the last row of the ResultSet has
	 * been retrieved or the ResultSet has been closed. In advanced cases, a
	 * single statement may return multiple results as well as output parameter
	 * values. Here the commit occurs when all results and output param values
	 * have been retrieved.
	 * <p>
	 * <b>Note:</b> MySQL does not support transactions, so this method is a
	 * no-op.
	 * </p>
	 * 
	 * @param autoCommitFlag -
	 *            true enables auto-commit; false disables it
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public void setAutoCommit(boolean autoCommitFlag) throws SQLException {
		synchronized (getMutex()) {
			checkClosed();
	
			if (getAutoReconnectForPools()) {
				setHighAvailability(true);
			}
	
			try {
				if (this.transactionsSupported) {
	
					boolean needsSetOnServer = true;
	
					if (this.getUseLocalSessionState()
							&& this.autoCommit == autoCommitFlag) {
						needsSetOnServer = false;
					} else if (!this.getHighAvailability()) {
						needsSetOnServer = this.getIO()
								.isSetNeededForAutoCommitMode(autoCommitFlag);
					}
	
					// this internal value must be set first as failover depends on
					// it
					// being set to true to fail over (which is done by most
					// app servers and connection pools at the end of
					// a transaction), and the driver issues an implicit set
					// based on this value when it (re)-connects to a server
					// so the value holds across connections
					this.autoCommit = autoCommitFlag;
	
					if (needsSetOnServer) {
						execSQL(null, autoCommitFlag ? "SET autocommit=1"
								: "SET autocommit=0", -1, null,
								java.sql.ResultSet.TYPE_FORWARD_ONLY,
								java.sql.ResultSet.CONCUR_READ_ONLY, false,
								this.database, true, false);
					}
	
				} else {
					if ((autoCommitFlag == false) && !getRelaxAutoCommit()) {
						throw SQLError.createSQLException("MySQL Versions Older than 3.23.15 "
								+ "do not support transactions",
								SQLError.SQL_STATE_CONNECTION_NOT_OPEN);
					}
	
					this.autoCommit = autoCommitFlag;
				}
			} finally {
				if (this.getAutoReconnectForPools()) {
					setHighAvailability(false);
				}
			}
	
			return;
		}
	}

	/**
	 * A sub-space of this Connection's database may be selected by setting a
	 * catalog name. If the driver does not support catalogs, it will silently
	 * ignore this request
	 * <p>
	 * <b>Note:</b> MySQL's notion of catalogs are individual databases.
	 * </p>
	 * 
	 * @param catalog
	 *            the database for this connection to use
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public void setCatalog(String catalog) throws SQLException {
		synchronized (getMutex()) {
			checkClosed();
	
			if (catalog == null) {
				throw SQLError.createSQLException("Catalog can not be null",
						SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
			}
			
			if (getUseLocalSessionState()) {
				if (this.lowerCaseTableNames) {
					if (this.database.equalsIgnoreCase(catalog)) {
						return;
					}
				} else {
					if (this.database.equals(catalog)) {
						return;
					}
				}
			}
			
			String quotedId = this.dbmd.getIdentifierQuoteString();
	
			if ((quotedId == null) || quotedId.equals(" ")) {
				quotedId = "";
			}
	
			StringBuffer query = new StringBuffer("USE ");
			query.append(quotedId);
			query.append(catalog);
			query.append(quotedId);
	
			execSQL(null, query.toString(), -1, null,
					java.sql.ResultSet.TYPE_FORWARD_ONLY,
					java.sql.ResultSet.CONCUR_READ_ONLY, false,
					this.database, true, false);
			
			this.database = catalog;
		}
	}

	/**
	 * @param failedOver
	 *            The failedOver to set.
	 */
	public synchronized void setFailedOver(boolean flag) {
		this.failedOver = flag;
	}

	/**
	 * Sets state for a failed-over connection
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	private void setFailedOverState() throws SQLException {
		if (getFailOverReadOnly()) {
			setReadOnlyInternal(true);
		}

		this.queriesIssuedFailedOver = 0;
		this.failedOver = true;
		this.masterFailTimeMillis = System.currentTimeMillis();
	}

	/**
	 * @see Connection#setHoldability(int)
	 */
	public void setHoldability(int arg0) throws SQLException {
		// do nothing
	}

	public void setInGlobalTx(boolean flag) {
		this.isInGlobalTx = flag;
	}

	// exposed for testing
	/**
	 * @param preferSlaveDuringFailover
	 *            The preferSlaveDuringFailover to set.
	 */
	public void setPreferSlaveDuringFailover(boolean flag) {
		this.preferSlaveDuringFailover = flag;
	}

	void setReadInfoMsgEnabled(boolean flag) {
		this.readInfoMsg = flag;
	}

	/**
	 * You can put a connection in read-only mode as a hint to enable database
	 * optimizations <B>Note:</B> setReadOnly cannot be called while in the
	 * middle of a transaction
	 * 
	 * @param readOnlyFlag -
	 *            true enables read-only mode; false disables it
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public void setReadOnly(boolean readOnlyFlag) throws SQLException {
		checkClosed();
		
		// Ignore calls to this method if we're failed over and
		// we're configured to fail over read-only.
		if (this.failedOver && getFailOverReadOnly() && !readOnlyFlag) {
			return;
		}
	
		setReadOnlyInternal(readOnlyFlag);
	}
	
	protected void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
		this.readOnly = readOnlyFlag;
	}

	/**
	 * @see Connection#setSavepoint()
	 */
	public java.sql.Savepoint setSavepoint() throws SQLException {
		MysqlSavepoint savepoint = new MysqlSavepoint();

		setSavepoint(savepoint);

		return savepoint;
	}

	private void setSavepoint(MysqlSavepoint savepoint) throws SQLException {

		if (versionMeetsMinimum(4, 0, 14) || versionMeetsMinimum(4, 1, 1)) {
			synchronized (getMutex()) {
				checkClosed();
	
				StringBuffer savePointQuery = new StringBuffer("SAVEPOINT ");
				savePointQuery.append('`');
				savePointQuery.append(savepoint.getSavepointName());
				savePointQuery.append('`');
	
				java.sql.Statement stmt = null;
	
				try {
					stmt = createStatement();
	
					stmt.executeUpdate(savePointQuery.toString());
				} finally {
					closeStatement(stmt);
				}
			}
		} else {
			throw new NotImplemented();
		}
	}

	/**
	 * @see Connection#setSavepoint(String)
	 */
	public synchronized java.sql.Savepoint setSavepoint(String name) throws SQLException {
		MysqlSavepoint savepoint = new MysqlSavepoint(name);

		setSavepoint(savepoint);

		return savepoint;
	}

	/**
	 * 
	 */
	private void setSessionVariables() throws SQLException {
		if (this.versionMeetsMinimum(4, 0, 0) && getSessionVariables() != null) {
			List variablesToSet = StringUtils.split(getSessionVariables(), ",", "\"'", "\"'",
					false);

			int numVariablesToSet = variablesToSet.size();

			java.sql.Statement stmt = null;

			try {
				stmt = getMetadataSafeStatement();

				for (int i = 0; i < numVariablesToSet; i++) {
					String variableValuePair = (String) variablesToSet.get(i);

					if (variableValuePair.startsWith("@")) {
						stmt.executeUpdate("SET " + variableValuePair);
					} else {
						stmt.executeUpdate("SET SESSION " + variableValuePair);
					}
				}
			} finally {
				if (stmt != null) {
					stmt.close();
				}
			}
		}

	}

	/**
     * Attempts to change the transaction isolation level for this
     * <code>Connection</code> object to the one given.
     * The constants defined in the interface <code>Connection</code>
     * are the possible transaction isolation levels.
     * <P>
     * <B>Note:</B> If this method is called during a transaction, the result
     * is implementation-defined.
     *
     * @param level one of the following <code>Connection</code> constants:
     *        <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
     *        <code>Connection.TRANSACTION_READ_COMMITTED</code>,
     *        <code>Connection.TRANSACTION_REPEATABLE_READ</code>, or
     *        <code>Connection.TRANSACTION_SERIALIZABLE</code>.
     *        (Note that <code>Connection.TRANSACTION_NONE</code> cannot be used 
     *        because it specifies that transactions are not supported.)
     * @exception SQLException if a database access error occurs
     *            or the given parameter is not one of the <code>Connection</code>
     *            constants
     * @see DatabaseMetaData#supportsTransactionIsolationLevel 
     * @see #getTransactionIsolation
     */
	public synchronized void setTransactionIsolation(int level) throws SQLException {
		checkClosed();

		if (this.hasIsolationLevels) {
			String sql = null;

			boolean shouldSendSet = false;

			if (getAlwaysSendSetIsolation()) {
				shouldSendSet = true;
			} else {
				if (level != this.isolationLevel) {
					shouldSendSet = true;
				}
			}

			if (getUseLocalSessionState()) {
				shouldSendSet = this.isolationLevel != level;
			}

			if (shouldSendSet) {
				switch (level) {
				case java.sql.Connection.TRANSACTION_NONE:
					throw SQLError.createSQLException("Transaction isolation level "
							+ "NONE not supported by MySQL");

				case java.sql.Connection.TRANSACTION_READ_COMMITTED:
					sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED";

					break;

				case java.sql.Connection.TRANSACTION_READ_UNCOMMITTED:
					sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED";

					break;

				case java.sql.Connection.TRANSACTION_REPEATABLE_READ:
					sql = "SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ";

					break;

				case java.sql.Connection.TRANSACTION_SERIALIZABLE:
					sql = "SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE";

					break;

				default:
					throw SQLError.createSQLException("Unsupported transaction "
							+ "isolation level '" + level + "'",
							SQLError.SQL_STATE_DRIVER_NOT_CAPABLE);
				}

				execSQL(null, sql, -1, null,
						java.sql.ResultSet.TYPE_FORWARD_ONLY,
						java.sql.ResultSet.CONCUR_READ_ONLY,false,
						this.database, true, false);

				this.isolationLevel = level;
			}
		} else {
			throw SQLError.createSQLException("Transaction Isolation Levels are "
					+ "not supported on MySQL versions older than 3.23.36.",
					SQLError.SQL_STATE_DRIVER_NOT_CAPABLE);
		}
	}
	
	/**
	 * JDBC 2.0 Install a type-map object as the default type-map for this
	 * connection
	 * 
	 * @param map
	 *            the type mapping
	 * @throws SQLException
	 *             if a database error occurs.
	 */
	public synchronized void setTypeMap(java.util.Map map) throws SQLException {
		this.typeMap = map;
	}
	
	/**
	 * Should we try to connect back to the master? We try when we've been
	 * failed over >= this.secondsBeforeRetryMaster _or_ we've issued >
	 * this.queriesIssuedFailedOver
	 * 
	 * @return DOCUMENT ME!
	 */
	private boolean shouldFallBack() {
		long secondsSinceFailedOver = (System.currentTimeMillis() - this.masterFailTimeMillis) / 1000;

		// Done this way so we can set a condition in the debugger
		boolean tryFallback = ((secondsSinceFailedOver >= getSecondsBeforeRetryMaster()) || (this.queriesIssuedFailedOver >= getQueriesBeforeRetryMaster()));

		return tryFallback;
	}
	
	/**
	 * Used by MiniAdmin to shutdown a MySQL server
	 * 
	 * @throws SQLException
	 *             if the command can not be issued.
	 */
	public void shutdownServer() throws SQLException {
		try {
			this.io.sendCommand(MysqlDefs.SHUTDOWN, null, null, false, null);
		} catch (Exception ex) {
			throw SQLError.createSQLException("Unhandled exception '" + ex.toString()
					+ "'", SQLError.SQL_STATE_GENERAL_ERROR);
		}
	}
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean supportsIsolationLevel() {
		return this.hasIsolationLevels;
	}
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean supportsQuotedIdentifiers() {
		return this.hasQuotedIdentifiers;
	}
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean supportsTransactions() {
		return this.transactionsSupported;
	}
	
	/**
	 * Remove the given statement from the list of open statements
	 * 
	 * @param stmt
	 *            the Statement instance to remove
	 */
	void unregisterStatement(Statement stmt) {
		if (this.openStatements != null) {
			synchronized (this.openStatements) {
				this.openStatements.remove(stmt);
			}
		}
	}

	/**
	 * Called by statements on their .close() to let the connection know when it
	 * is safe to set the connection back to 'default' row limits.
	 * 
	 * @param stmt
	 *            the statement releasing it's max-rows requirement
	 * @throws SQLException
	 *             if a database error occurs issuing the statement that sets
	 *             the limit default.
	 */
	void unsetMaxRows(Statement stmt) throws SQLException {
		synchronized (this.mutex) {
			if (this.statementsUsingMaxRows != null) {
				Object found = this.statementsUsingMaxRows.remove(stmt);

				if ((found != null)
						&& (this.statementsUsingMaxRows.size() == 0)) {
					execSQL(null, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1,
							null, java.sql.ResultSet.TYPE_FORWARD_ONLY,
							java.sql.ResultSet.CONCUR_READ_ONLY, false, 
							this.database, true, false);

					this.maxRowsChanged = false;
				}
			}
		}
	}
	
	boolean useAnsiQuotedIdentifiers() {
		return this.useAnsiQuotes;
	}

	/**
	 * Has maxRows() been set?
	 * 
	 * @return DOCUMENT ME!
	 */
	boolean useMaxRows() {
		synchronized (this.mutex) {
			return this.maxRowsChanged;
		}
	}

	public boolean versionMeetsMinimum(int major, int minor, int subminor)
			throws SQLException {
		checkClosed();

		return this.io.versionMeetsMinimum(major, minor, subminor);
	}

	protected String getErrorMessageEncoding() {
		return errorMessageEncoding;
	}
	
	/*
	 * For testing failover scenarios
	 */
	private boolean hasTriedMasterFlag = false;
	
	public void clearHasTriedMaster() {
		this.hasTriedMasterFlag = false;
	}
	
	public boolean hasTriedMaster() {
		return this.hasTriedMasterFlag;
	}
	
	/**
	 * Returns cached metadata (or null if not cached) for the given query,
	 * which must match _exactly_.	 
	 *  
	 * This method is synchronized by the caller on getMutex(), so if
	 * calling this method from internal code in the driver, make sure it's
	 * synchronized on the mutex that guards communication with the server.
	 * 
	 * @param sql
	 *            the query that is the key to the cache
	 * 
	 * @return metadata cached for the given SQL, or none if it doesn't
	 *                  exist.
	 */
	protected CachedResultSetMetaData getCachedMetaData(String sql) {
		if (this.resultSetMetadataCache != null) {
			synchronized (this.resultSetMetadataCache) {
				return (CachedResultSetMetaData) this.resultSetMetadataCache
						.get(sql);
			}
		}

		return null; // no cache exists
	}

	/**
	 * Caches CachedResultSetMetaData that has been placed in the cache using
	 * the given SQL as a key.
	 * 
	 * This method is synchronized by the caller on getMutex(), so if
	 * calling this method from internal code in the driver, make sure it's
	 * synchronized on the mutex that guards communication with the server.
	 * 
	 * @param sql the query that the metadata pertains too.
	 * @param cachedMetaData metadata (if it exists) to populate the cache.
	 * @param resultSet the result set to retreive metadata from, or apply to.
	 *
	 * @throws SQLException
	 */
	protected void initializeResultsMetadataFromCache(String sql,
			CachedResultSetMetaData cachedMetaData, ResultSet resultSet)
			throws SQLException {

		if (cachedMetaData == null) {
			
			// read from results
			cachedMetaData = new CachedResultSetMetaData();
			cachedMetaData.fields = resultSet.fields;

			// assume that users will use named-based
			// lookups
			resultSet.buildIndexMapping();
			resultSet.initializeWithMetadata();
			
			if (resultSet instanceof UpdatableResultSet) {
				((UpdatableResultSet)resultSet).checkUpdatability();
			}

			cachedMetaData.columnNameToIndex = resultSet.columnNameToIndex;
			cachedMetaData.fullColumnNameToIndex = resultSet.fullColumnNameToIndex;

			cachedMetaData.metadata = resultSet.getMetaData();

			this.resultSetMetadataCache.put(sql, cachedMetaData);
		} else {
			// initialize results from cached data
			resultSet.fields = cachedMetaData.fields;
			resultSet.columnNameToIndex = cachedMetaData.columnNameToIndex;
			resultSet.fullColumnNameToIndex = cachedMetaData.fullColumnNameToIndex;
			resultSet.hasBuiltIndexMapping = true;
			resultSet.initializeWithMetadata();
			
			if (resultSet instanceof UpdatableResultSet) {
				((UpdatableResultSet)resultSet).checkUpdatability();
			}
		}
	}
}
/*
 Copyright (C) 2002-2006 MySQL AB

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

import java.sql.SQLException;

import com.mysql.jdbc.profiler.ProfileEventSink;
import com.mysql.jdbc.profiler.ProfilerEvent;

/**
 * Allows streaming of MySQL data.
 * 
 * @author dgan
 *
 */
public class RowDataDynamic implements RowData {
	// ~ Instance fields
	// --------------------------------------------------------

	class OperationNotSupportedException extends SQLException {
		OperationNotSupportedException() {
			super(
					Messages.getString("RowDataDynamic.10"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT); //$NON-NLS-1$
		}
	}

	private int columnCount;

	private Field[] fields;

	private int index = -1;

	private MysqlIO io;

	private boolean isAfterEnd = false;

	private boolean isAtEnd = false;

	private boolean isBinaryEncoded = false;

	private Object[] nextRow;

	private ResultSet owner;

	private boolean streamerClosed = false;
	
	private boolean wasEmpty = false; // we don't know until we attempt to traverse

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * Creates a new RowDataDynamic object.
	 * 
	 * @param io
	 *            the connection to MySQL that this data is coming from
	 * @param fields
	 *            the fields that describe this data
	 * @param isBinaryEncoded
	 *            is this data in native format?
	 * @param colCount
	 *            the number of columns
	 * @throws SQLException
	 *             if the next record can not be found
	 */
	public RowDataDynamic(MysqlIO io, int colCount, Field[] fields,
			boolean isBinaryEncoded) throws SQLException {
		this.io = io;
		this.columnCount = colCount;
		this.isBinaryEncoded = isBinaryEncoded;
		this.fields = fields;
		nextRecord();
	}

	/**
	 * Adds a row to this row data.
	 * 
	 * @param row
	 *            the row to add
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public void addRow(byte[][] row) throws SQLException {
		notSupported();
	}

	/**
	 * Moves to after last.
	 * 
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public void afterLast() throws SQLException {
		notSupported();
	}

	/**
	 * Moves to before first.
	 * 
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public void beforeFirst() throws SQLException {
		notSupported();
	}

	/**
	 * Moves to before last so next el is the last el.
	 * 
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public void beforeLast() throws SQLException {
		notSupported();
	}

	/**
	 * We're done.
	 * 
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public void close() throws SQLException {

		boolean hadMore = false;
		int howMuchMore = 0;

		// drain the rest of the records.
		while (this.hasNext()) {
			this.next();
			hadMore = true;
			howMuchMore++;

			if (howMuchMore % 100 == 0) {
				Thread.yield();
			}
		}

		if (this.owner != null) {
			Connection conn = this.owner.connection;

			if (conn != null && conn.getUseUsageAdvisor()) {
				if (hadMore) {

					ProfileEventSink eventSink = ProfileEventSink
							.getInstance(conn);

					eventSink
							.consumeEvent(new ProfilerEvent(
									ProfilerEvent.TYPE_WARN,
									"", //$NON-NLS-1$
									this.owner.owningStatement == null ? "N/A" : this.owner.owningStatement.currentCatalog, //$NON-NLS-1$
									this.owner.connectionId,
									this.owner.owningStatement == null ? -1
											: this.owner.owningStatement
													.getId(),
									-1,
									System.currentTimeMillis(),
									0,
									Constants.MILLIS_I18N,
									null,
									null,
									Messages.getString("RowDataDynamic.2") //$NON-NLS-1$
											+ howMuchMore
											+ Messages
													.getString("RowDataDynamic.3") //$NON-NLS-1$
											+ Messages
													.getString("RowDataDynamic.4") //$NON-NLS-1$
											+ Messages
													.getString("RowDataDynamic.5") //$NON-NLS-1$
											+ Messages
													.getString("RowDataDynamic.6") //$NON-NLS-1$
											+ this.owner.pointOfOrigin));
				}
			}
		}

		this.fields = null;
		this.owner = null;
	}

	/**
	 * Only works on non dynamic result sets.
	 * 
	 * @param index
	 *            row number to get at
	 * @return row data at index
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public Object[] getAt(int ind) throws SQLException {
		notSupported();

		return null;
	}

	/**
	 * Returns the current position in the result set as a row number.
	 * 
	 * @return the current row number
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public int getCurrentRowNumber() throws SQLException {
		notSupported();

		return -1;
	}

	/**
	 * @see com.mysql.jdbc.RowData#getOwner()
	 */
	public ResultSet getOwner() {
		return this.owner;
	}

	/**
	 * Returns true if another row exsists.
	 * 
	 * @return true if more rows
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public boolean hasNext() throws SQLException {
		boolean hasNext = (this.nextRow != null);

		if (!hasNext && !this.streamerClosed) {
			this.io.closeStreamer(this);
			this.streamerClosed = true;
		}

		return hasNext;
	}

	/**
	 * Returns true if we got the last element.
	 * 
	 * @return true if after last row
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public boolean isAfterLast() throws SQLException {
		return this.isAfterEnd;
	}

	/**
	 * Returns if iteration has not occured yet.
	 * 
	 * @return true if before first row
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public boolean isBeforeFirst() throws SQLException {
		return this.index < 0;
	}

	/**
	 * Returns true if the result set is dynamic.
	 * 
	 * This means that move back and move forward won't work because we do not
	 * hold on to the records.
	 * 
	 * @return true if this result set is streaming from the server
	 */
	public boolean isDynamic() {
		return true;
	}

	/**
	 * Has no records.
	 * 
	 * @return true if no records
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public boolean isEmpty() throws SQLException {
		notSupported();

		return false;
	}

	/**
	 * Are we on the first row of the result set?
	 * 
	 * @return true if on first row
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public boolean isFirst() throws SQLException {
		notSupported();

		return false;
	}

	/**
	 * Are we on the last row of the result set?
	 * 
	 * @return true if on last row
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public boolean isLast() throws SQLException {
		notSupported();

		return false;
	}

	/**
	 * Moves the current position relative 'rows' from the current position.
	 * 
	 * @param rows
	 *            the relative number of rows to move
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public void moveRowRelative(int rows) throws SQLException {
		notSupported();
	}

	/**
	 * Returns the next row.
	 * 
	 * @return the next row value
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public Object[] next() throws SQLException {
		if (this.index != Integer.MAX_VALUE) {
			this.index++;
		}

		Object[] ret = this.nextRow;
		nextRecord();

		return ret;
	}


	private void nextRecord() throws SQLException {

		try {
			if (!this.isAtEnd) {

				this.nextRow = this.io.nextRow(this.fields, this.columnCount,
						this.isBinaryEncoded,
						java.sql.ResultSet.CONCUR_READ_ONLY);

				if (this.nextRow == null) {
					this.isAtEnd = true;
					
					if (this.index == -1) {
						this.wasEmpty = true;
					}
				}
			} else {
				this.isAfterEnd = true;
			}
		} catch (CommunicationsException comEx) {
			// Give a better error message
			comEx.setWasStreamingResults();
			
			throw comEx;
		} catch (SQLException sqlEx) {
			// don't wrap SQLExceptions
			throw sqlEx;
		} catch (Exception ex) {
			String exceptionType = ex.getClass().getName();
			String exceptionMessage = ex.getMessage();

			exceptionMessage += Messages.getString("RowDataDynamic.7"); //$NON-NLS-1$
			exceptionMessage += Util.stackTraceToString(ex);

			throw new java.sql.SQLException(
					Messages.getString("RowDataDynamic.8") //$NON-NLS-1$
							+ exceptionType
							+ Messages.getString("RowDataDynamic.9") + exceptionMessage, SQLError.SQL_STATE_GENERAL_ERROR); //$NON-NLS-1$
		}
	}

	private void notSupported() throws SQLException {
		throw new OperationNotSupportedException();
	}

	/**
	 * Removes the row at the given index.
	 * 
	 * @param index
	 *            the row to move to
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public void removeRow(int ind) throws SQLException {
		notSupported();
	}

	// ~ Inner Classes
	// ----------------------------------------------------------

	/**
	 * Moves the current position in the result set to the given row number.
	 * 
	 * @param rowNumber
	 *            row to move to
	 * @throws SQLException
	 *             if a database error occurs
	 */
	public void setCurrentRow(int rowNumber) throws SQLException {
		notSupported();
	}

	/**
	 * @see com.mysql.jdbc.RowData#setOwner(com.mysql.jdbc.ResultSet)
	 */
	public void setOwner(ResultSet rs) {
		this.owner = rs;
	}

	/**
	 * Only works on non dynamic result sets.
	 * 
	 * @return the size of this row data
	 */
	public int size() {
		return RESULT_SET_SIZE_UNKNOWN;
	}

	public boolean wasEmpty() {
		return this.wasEmpty;
	}
	
	

}

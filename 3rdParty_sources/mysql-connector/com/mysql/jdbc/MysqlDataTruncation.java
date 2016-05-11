/*
 Copyright (C) 2002-2004 MySQL AB

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

import java.sql.DataTruncation;

/**
 * MySQL wrapper for DataTruncation until the server can support sending all
 * needed information.
 * 
 * @author Mark Matthews
 * 
 *
 *          mmatthews Exp $
 */
public class MysqlDataTruncation extends DataTruncation {

	private String message;

	/**
	 * Creates a new MysqlDataTruncation exception/warning.
	 * 
	 * @param message
	 *            the message from the server
	 * @param index
	 *            of column or parameter
	 * @param parameter
	 *            was a parameter?
	 * @param read
	 *            was truncated on read?
	 * @param dataSize
	 *            size requested
	 * @param transferSize
	 *            size actually used
	 */
	public MysqlDataTruncation(String message, int index, boolean parameter,
			boolean read, int dataSize, int transferSize) {
		super(index, parameter, read, dataSize, transferSize);

		this.message = message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage() + ": " + this.message; //$NON-NLS-1$
	}
}

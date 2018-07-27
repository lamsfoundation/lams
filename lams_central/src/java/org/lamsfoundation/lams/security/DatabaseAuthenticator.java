/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.HashUtil;

/**
 * Validates user password against an entry in the LAMS database.
 */
public class DatabaseAuthenticator {
    private static Logger log = Logger.getLogger(DatabaseAuthenticator.class);

    private static DataSource dataSource;

    private static final String GET_PASSWORD_QUERY = "SELECT password FROM lams_user WHERE login=?";
    private static final String GET_SALT_QUERY = "SELECT salt FROM lams_user WHERE login=?";
    private static final String UPDATE_PASSWORD_QUERY = "UPDATE lams_user SET password=?, salt=? WHERE login=?";

    public DatabaseAuthenticator(String dsJndiName) throws NamingException {
	if (DatabaseAuthenticator.dataSource == null) {
	    InitialContext ctx = new InitialContext();
	    DatabaseAuthenticator.dataSource = (DataSource) ctx.lookup(dsJndiName);
	}
    }

    public boolean authenticate(String userName, String inputPassword) {
	try (Connection conn = DatabaseAuthenticator.dataSource.getConnection()) {
	    String databasePassword = DatabaseAuthenticator.getDatabasePassword(conn, userName);

	    // password should never be blank in the DB (?)
	    if (StringUtils.isBlank(databasePassword)) {
		DatabaseAuthenticator.log.warn("Password in database is blank for user: " + userName);
		return false;
	    }
	    // is it still SHA1 password?
	    if (databasePassword.length() == HashUtil.SHA1_HEX_LENGTH) {
		String inputPasswordHash = HashUtil.sha1(inputPassword);
		if (inputPasswordHash.equals(databasePassword)) {
		    // update the password with SHA256 + salt
		    return DatabaseAuthenticator.convertPasswordToSha256(conn, userName, inputPassword);
		}
		return false;
	    }
	    // is it already SHA256 password
	    if (databasePassword.length() == HashUtil.SHA256_HEX_LENGTH) {
		String salt = DatabaseAuthenticator.getDatabaseSalt(conn, userName);
		if (StringUtils.isBlank(salt) || (salt.length() != HashUtil.SALT_HEX_LENGTH)) {
		    DatabaseAuthenticator.log.error("Salt does not have correct format for user: " + userName);
		    return false;
		}
		String inputPasswordHash = HashUtil.sha256(inputPassword, salt);

		return inputPasswordHash.equals(databasePassword);
	    }

	    DatabaseAuthenticator.log.error("Password in database does not have correct format for user: " + userName);
	} catch (SQLException e) {
	    DatabaseAuthenticator.log.error(e);
	}

	return false;
    }

    /**
     * Fetches user password from the database.
     */
    private static String getDatabasePassword(Connection conn, String userName) throws SQLException {
	ResultSet resultSet = null;

	try (PreparedStatement preparedStatement = conn.prepareStatement(DatabaseAuthenticator.GET_PASSWORD_QUERY)) {
	    preparedStatement.setString(1, userName);
	    resultSet = preparedStatement.executeQuery();
	    return resultSet.next() ? resultSet.getString(1).trim() : null;
	} finally {
	    if (resultSet != null) {
		try {
		    resultSet.close();
		} catch (SQLException e) {
		    DatabaseAuthenticator.log.error(e);
		}
	    }
	}
    }

    private static String getDatabaseSalt(Connection conn, String userName) throws SQLException {
	ResultSet resultSet = null;

	try (PreparedStatement preparedStatement = conn.prepareStatement(DatabaseAuthenticator.GET_SALT_QUERY)) {
	    preparedStatement.setString(1, userName);
	    resultSet = preparedStatement.executeQuery();
	    return resultSet.next() ? resultSet.getString(1).trim() : null;
	} finally {
	    if (resultSet != null) {
		try {
		    resultSet.close();
		} catch (SQLException e) {
		    DatabaseAuthenticator.log.error(e);
		}
	    }
	}
    }

    /**
     * Updates the user password with sha256 hash with salt.
     */
    private static boolean convertPasswordToSha256(Connection conn, String userName, String inputPassword)
	    throws SQLException {
	if (DatabaseAuthenticator.log.isDebugEnabled()) {
	    DatabaseAuthenticator.log.debug("Converting password to SHA256 for user: " + userName);
	}
	String salt = HashUtil.salt();
	String inputPasswordHash = HashUtil.sha256(inputPassword, salt);

	try (PreparedStatement preparedStatement = conn.prepareStatement(DatabaseAuthenticator.UPDATE_PASSWORD_QUERY)) {
	    preparedStatement.setString(1, inputPasswordHash);
	    preparedStatement.setString(2, salt);
	    preparedStatement.setString(3, userName);
	    int result = preparedStatement.executeUpdate();
	    if (result == 1) {
		return true;
	    } else {
		DatabaseAuthenticator.log.error("Error while converting password to SHA256 for user: " + userName
			+ ". The update query returned result: " + result);
	    }
	}

	return false;
    }
}
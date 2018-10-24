/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.security.auth.callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.vault.SecurityVaultException;
import org.jboss.security.vault.SecurityVaultUtil;

/**
 * <p>
 * A {@code CallbackHandler} that uses a DB.
 * </p>
 * <p>
 * <b>Configuration:</b>
 * There are two ways to specify the configuration.
 * <ol>
 * <li>Using the {@code #setConfiguration(Map)} method, which uses {@code String} based key/value pair.</li>
 * <li>Using the methods {@code #setConnectionUrl(String)}, {@code #setDbDriverName(String)}etc</li>
 * </ol>
 * </p>
 * <p>
 * Either you can specify the connection url, driver class name or you can provide the jndi name of the {@code DataSource}.
 * </p>
 * @author Anil Saldhana
 * @since Oct 31, 2011
 */
public class DatabaseCallbackHandler extends AbstractCallbackHandler implements CallbackHandler 
{
	public static final String CONNECTION_URL = "connectionURL";
	public static final String DS_JNDI_NAME = "dsJndiName";
	public static final String DB_DRIVERNAME = "dbDriverName";
	public static final String DB_USERNAME = "dbUserName";
	public static final String DB_USERPASS = "dbUserPass";
	public static final String PRINCIPALS_QUERY = "principalsQuery";

	/**
	 * A DB specific connection url
	 */
	protected String connectionUrl;
	/**
	 * JNDI Name of the Datasource
	 */
	protected String dsJndiName;
	/**
	 * A DB username to connect
	 */
	protected String dsUserName;
	/**
	 * A DB password to connect
	 */
	protected String dsUserPass;
	/**
	 * A DB Driver Class Name
	 */
	protected String dbDriverName;

	/** The sql query to obtain the user password */
	protected String principalsQuery = "select Password from Principals where PrincipalID=?";

	public DatabaseCallbackHandler()
	{	
	}

	/**
	 * Get the DB specific connection URL
	 * Eg: "jdbc:hsqldb:mem:unit_test"
	 * @return
	 */
	public String getConnectionUrl() 
	{
		return connectionUrl;
	} 

	public void setConnectionUrl(String connectionUrl) 
	{
		this.connectionUrl = connectionUrl;
	} 

	/**
	 * Get the JNDI name of the SQL Datasource
	 * @return
	 */
	public String getDsJndiName() 
	{
		return dsJndiName;
	} 
	
	public void setDsJndiName(String dsJndiName) 
	{
		this.dsJndiName = dsJndiName;
	}

	/**
	 * Get the DB user name
	 * @return
	 */
	public String getDsUserName() 
	{
		return dsUserName;
	}

	public void setDsUserName(String dsUserName) 
	{
		this.dsUserName = dsUserName;
	}

	/**
	 * Get the DB user pass
	 * @return
	 */
	public String getDsUserPass() 
	{
		return dsUserPass;
	}

	public void setDsUserPass(String dsUserPass) 
	{
		this.dsUserPass = dsUserPass;
	}

	/**
	 * Get the fully qualified name of sql driver class
	 * Eg: org.hsqldb.jdbc.JDBCDriver
	 * @return
	 */
	public String getDbDriverName() 
	{
		return dbDriverName;
	}

	public void setDbDriverName(String dbDriverName) 
	{
		this.dbDriverName = dbDriverName;
	}

	public String getPrincipalsQuery() 
	{
		return principalsQuery;
	}

	public void setPrincipalsQuery(String principalsQuery) {
		this.principalsQuery = principalsQuery;
	}
 

	public String getUserName() {
		return userName;
	}

	public void setUserName(String theUserName)
	{
		if(theUserName == null)
		{
			throw PicketBoxMessages.MESSAGES.invalidNullArgument("userName");
		}
		userName = theUserName;
	}

	/**
	 * Set a {@code Map} that contains keys that are strings and values that are strings
	 * @param config
	 */
	public void setConfiguration(Map<String,String> config)
	{
		String tmp = null;
		dbDriverName = config.get(DB_DRIVERNAME);
		
		connectionUrl = config.get(CONNECTION_URL);
		if(connectionUrl == null || connectionUrl.length() == 0)
		{
			dsJndiName = config.get(DS_JNDI_NAME);
		}
		dsUserName = config.get(DB_USERNAME);
		dsUserPass = config.get(DB_USERPASS);
		if(dsUserPass != null)
		{
			if(SecurityVaultUtil.isVaultFormat(dsUserPass))
			{
				try 
				{
					dsUserPass = SecurityVaultUtil.getValueAsString(dsUserPass);
				} 
				catch (SecurityVaultException e) 
				{
					throw new RuntimeException(e);
				}
			}
		}

		tmp = config.get(PRINCIPALS_QUERY);
		if(tmp != null)
		{
			principalsQuery = tmp;
		}
	}

	/*
	 * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
	 */
	public void handle(Callback[] callbacks) throws IOException,
	UnsupportedCallbackException 
	{
		if(userName == null)
		{
			userName = getUserName(callbacks);
		}
		for (int i = 0; i < callbacks.length; i++)
		{
			Callback callback = callbacks[i];
			this.handleCallBack( callback ); 
		}
	}

	/**
	 * Handle a {@code Callback}
	 * @param c callback
	 * @throws UnsupportedCallbackException If the callback is not supported by this handler
	 * @throws IOException 
	 */
	protected void handleCallBack( Callback c ) throws UnsupportedCallbackException, IOException
	{ 
		if(c instanceof VerifyPasswordCallback)
		{
			VerifyPasswordCallback vpc = (VerifyPasswordCallback) c;
			try 
			{
				handleVerification(vpc);
			} 
			catch (LoginException e)
			{
				throw new IOException(e);
			}
		}
		if(c instanceof PasswordCallback == false)
			return;

		PasswordCallback passwdCallback = (PasswordCallback) c;

		passwdCallback.setPassword(getPassword().toCharArray());
	}
	
	protected void handleVerification(VerifyPasswordCallback vpc) throws LoginException
	{
		String userPass = vpc.getValue();
		String passwordFromDB = getPassword();
		if(userPass.equals(passwordFromDB))
		{
			vpc.setVerified(true);
		}
		else
		{
			throw new LoginException(PicketBoxMessages.MESSAGES.authenticationFailedMessage());
		}
	}
	
	private String getPassword()
	{
		String password = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
		    conn = getConnection();
			ps = conn.prepareStatement(principalsQuery);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			if( rs.next() == false )
			{
				throw PicketBoxMessages.MESSAGES.unableToFindPrincipalInDB(userName);
			}

			password = rs.getString(1);
		} 
		catch (Exception e) 
		{ 
			throw new RuntimeException(e);
		}
		finally
		{
			safeClose(rs);
			safeClose(ps);
			safeClose(conn);
		}
		return password;
	}

	private Connection getConnection() throws SQLException, NamingException
	{
		Connection conn = null;
		
		if(dbDriverName != null)
		{
			try 
			{
				Class.forName(dbDriverName);
			} 
			catch (ClassNotFoundException e) 
			{
				throw new RuntimeException(e);
			}
		}

		if(connectionUrl != null)
		{
			if(dsUserName != null)
			{
				conn = DriverManager.getConnection(connectionUrl, dsUserName, dsUserPass);
			}
			else
			{
				conn = DriverManager.getConnection(connectionUrl);
			}
		}
		else
		{
			InitialContext ic = new InitialContext();
			if(dsJndiName == null)
			{
				throw PicketBoxMessages.MESSAGES.unableToLookupDataSource();
			}
				
			DataSource ds = (DataSource) ic.lookup(dsJndiName);
			if(ds != null)
			{
				conn = ds.getConnection();
			}
		}

		return conn;
	}
	
	protected void safeClose(ResultSet rs)
	{
		if( rs != null)
		{
			try 
			{
				rs.close();
			} 
			catch (SQLException e) 
			{	
			}
		}
	}
	
	protected void safeClose(Connection conn)
	{
		if( conn != null)
		{
			try 
			{
				conn.close();
			} 
			catch (SQLException e) 
			{	
			}
		}
	}
	
	protected void safeClose(Statement stat)
	{
		if( stat != null)
		{
			try 
			{
				stat.close();
			} 
			catch (SQLException e) 
			{	
			}
		}
	}
}
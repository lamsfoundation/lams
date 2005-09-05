package org.lamsfoundation.lams.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;



public class DatabaseAuthenticator
{
	private AuthenticationMethod method;
	private String dsJndiName;
	private String principalsQuery;
	
	public DatabaseAuthenticator( AuthenticationMethod method)
	{
		this.method = method;
		this.dsJndiName = method.getParameterByName("dsJndiName").getValue();
		this.principalsQuery = method.getParameterByName("principalsQuery").getValue();
	}

	public boolean authenticate( String username, String inputPassword )
	{
		boolean isValid = false;
		if ( (inputPassword == null ) && (inputPassword.trim().length()==0) )
			return isValid;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String databasePassword = null;      
		try
		{
		   InitialContext ctx = new InitialContext();
		   DataSource ds = (DataSource) ctx.lookup(dsJndiName);
		   conn = ds.getConnection();
		   // Get the password
		   ps = conn.prepareStatement(principalsQuery);
		   ps.setString(1, username);
		   rs = ps.executeQuery();
		   if( rs.next() == false )
		   		isValid = false;
         
		   databasePassword = rs.getString(1);
		   if ( inputPassword.equals(databasePassword.trim()) )
		   		isValid = true;
		   
		}
		catch(NamingException ex)
		{
			System.out.println(ex);
		}
		catch(SQLException ex)
		{
			System.out.println(ex);
		}
		finally
		{
		   if (rs != null)
		   {
			  try
			  {
				 rs.close();
			  }
			  catch(SQLException e)
			  {}
		   }
		   if( ps != null )
		   {
			  try
			  {
				 ps.close();
			  }
			  catch(SQLException e)
			  {}
		   }
		   if( conn != null )
		   {
			  try
			  {
				 conn.close();
			  }
			  catch (SQLException ex)
			  {}
		   }
		}
		return isValid;

	}

}





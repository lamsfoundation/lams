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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
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





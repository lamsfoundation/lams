/****************************************************************
 * Copyright (C) 2007 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.ld.integration.blackboard;


import blackboard.persist.DatabaseContainer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import blackboard.platform.BbServiceManager;



public class BBDBManager
{
	// constructor
	public BBDBManager() {}
	
	// get the Bb Database Connection
	public Connection getBbDatabaseConnection() throws Exception
	{
		Connection conn = ((DatabaseContainer)BbServiceManager
				.getPersistenceService().getDbPersistenceManager().getContainer())
				.getBbDatabase().getConnectionManager().getConnection();
				 return conn;	
	}
	
	public boolean createTable(Connection connection)
	{
		return false;
	}
	
	public boolean insertLearningDesign(Connection connection, String user)
	{
		return false;
	}
	
	
	// get a list of learning designs for monitor.jsp
	public String[] learningDesigns(Connection connection, String user)
	{
		String[] learningDesigns;
		
		
		return null;
	}
	
	
}
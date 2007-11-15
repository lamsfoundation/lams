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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author jliew
 *
 */
public class Alter21Integration {

	/**
	 * Update 3rd party integration courses to classes, LDEV-1284.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Alter21Integration me = new Alter21Integration();
		
		if (args.length < 3)
		{
			System.out.println("Usage: java Alter21Integration DB_NAME DB_USERNAME DB_PASSWORD");
			System.out.println("Make sure mysql-connector-java-3.x.x-bin.jar is in your classpath.");
			System.exit(1);
		}
		
		String dbName = args[0];
		String dbUsername = args[1];
		String dbPassword = args[2];
		me.execute(dbName, dbUsername, dbPassword);
	}

	public void execute(String dbName, String dbUsername, String dbPassword) throws Exception {
		
		String dbDriverClass = "com.mysql.jdbc.Driver";
		String dbDriverUrl = "jdbc:mysql://localhost/" 
			+ dbName + "?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useUnicode=true";

		Class.forName(dbDriverClass);
        Connection conn = DriverManager.getConnection(dbDriverUrl, dbUsername, dbPassword);
        conn.setAutoCommit(false);
		
        // change integration orgs from classes to courses, with root org as parent.
		String updateToCourseType = "UPDATE lams_organisation "
				+ "SET organisation_type_id=2, "
				+ "parent_organisation_id=1 "
				+ "WHERE organisation_id IN ( "
					+ "SELECT classid " 
					+ "FROM lams_ext_course_class_map "
					+ ")";	
		
		// we want to give these newly converted courses their own workspaces;
		String getCoursesWithoutWorkspaces = "SELECT organisation_id, name "
			+ "FROM lams_organisation "
			+ "WHERE organisation_type_id=2 "
			+ "AND workspace_id IS NULL";
		
		// normal folder;
		String insertNormalFolder = "INSERT INTO lams_workspace_folder (name, user_id, create_date_time, "
			+ "last_modified_date_time, lams_workspace_folder_type_id) "
			+ "VALUES (?, ?, ?, ?, ?)";

		// and run sequence folder
		String insertRunSequencesFolder = "INSERT INTO lams_workspace_folder (name, user_id, create_date_time, "
			+ "last_modified_date_time, lams_workspace_folder_type_id, parent_folder_id) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
		
		// insert workspace, links to its folders, and link to its org 
		String insertWorkspace = "INSERT INTO lams_workspace (name, default_fld_id, def_run_seq_fld_id) "
			+ "VALUES (?, ?, ?)";
		
		String insertLinks = "INSERT INTO lams_wkspc_wkspc_folder (workspace_id, workspace_folder_id) "
			+ "VALUES (?, ?)";
		
		String updateOrg = "UPDATE lams_organisation "
			+ "SET workspace_id=? "
			+ "WHERE organisation_id=?";

        // Start update
		
		// update classes to courses
        PreparedStatement query = conn.prepareStatement(updateToCourseType);
        int numUpdatedCourses = query.executeUpdate();
        System.out.println("Set " + numUpdatedCourses + " integration organisations to courses.");
      
        // get courses without workspaces
        ArrayList<OrgDTO> orgs = new ArrayList<OrgDTO>();
        query = conn.prepareStatement(getCoursesWithoutWorkspaces);
        ResultSet results = query.executeQuery();  
        while (results.next()) {
			orgs.add(new OrgDTO(results.getLong("organisation_id"), results.getString("name")));
        }
        
        // create workspace and folders for each org
        for (OrgDTO org : orgs) {
        	// insert normal folder
        	query = conn.prepareStatement(insertNormalFolder, Statement.RETURN_GENERATED_KEYS);
        	query.setString(1, org.getOrgName());
        	query.setLong(2, new Long(1));
        	query.setDate(3, new Date(System.currentTimeMillis()));
        	query.setDate(4, new Date(System.currentTimeMillis()));
        	query.setInt(5, new Integer(1));
        	int numInsert = query.executeUpdate();
        	results = query.getGeneratedKeys();
        	long wkspcFolderId = -1;
			if (results.next())	wkspcFolderId = results.getLong(1);
        	System.out.println("Inserted " + numInsert + " workspace folders, with id " + wkspcFolderId + ".");

        	// insert run sequences folder
        	query = conn.prepareStatement(insertRunSequencesFolder, Statement.RETURN_GENERATED_KEYS);
        	query.setString(1, org.getOrgName()+" Run Sequences");
        	query.setLong(2, new Long(1));
        	query.setDate(3, new Date(System.currentTimeMillis()));
        	query.setDate(4, new Date(System.currentTimeMillis()));
        	query.setInt(5, new Integer(2));
        	query.setLong(6, wkspcFolderId);
        	numInsert = query.executeUpdate();
        	results = query.getGeneratedKeys();
        	long wkspcRunFolderId = -1;
			if (results.next()) wkspcRunFolderId = results.getLong(1);
        	System.out.println("Inserted " + numInsert + " workspace folders, with id " + wkspcRunFolderId + ".");

        	// insert workspace
        	query = conn.prepareStatement(insertWorkspace, Statement.RETURN_GENERATED_KEYS);
        	query.setString(1, org.getOrgName());
        	query.setLong(2, wkspcFolderId);
        	query.setLong(3, wkspcRunFolderId);
        	numInsert = query.executeUpdate();
        	results = query.getGeneratedKeys();
        	long wkspcId = -1;
			if (results.next()) wkspcId = results.getLong(1);
        	System.out.println("Inserted " + numInsert + " workspaces, with id " + wkspcId + ".");

        	// insert wkspc_wkspc_folder links
        	query = conn.prepareStatement(insertLinks);
        	query.setLong(1, wkspcId);
        	query.setLong(2, wkspcFolderId);
        	numInsert = query.executeUpdate();
			System.out.println("Inserted " + numInsert + " wkspc_wkspc_folder links.");
        	query.setLong(1, wkspcId);
        	query.setLong(2, wkspcRunFolderId);
        	numInsert = query.executeUpdate();
        	System.out.println("Inserted " + numInsert + " wkspc_wkspc_folder links.");

        	// update organisation with this workspace
        	query = conn.prepareStatement(updateOrg);
        	query.setLong(1, wkspcId);
        	query.setLong(2, org.getOrgId());
        	numInsert = query.executeUpdate();
        	System.out.println("Updated " + org.getOrgName() + " with workspace_id " + wkspcId + ".");
        }

		conn.commit();
        conn.close();
	}
	
	public class OrgDTO {
		private Long orgId;
		private String orgName;
		public OrgDTO(Long orgId, String orgName) {
			this.orgId = orgId;
			this.orgName = orgName;
		}
		public Long getOrgId() {
			return orgId;
		}
		public String getOrgName() {
			return orgName;
		}
	}

}


/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.integration.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lamsfoundation.integration.util.Constants;
import org.lamsfoundation.integration.webct.LamsLesson;

/**
 * Database connector for webct/lams integration
 * @author luke foxton
 *
 */
public class LamsLessonDaoJDBC implements ILamsLessonDao
{
private Map settings;
	
	private String dbUrl;
	private String dbUser;
	private String dbPass;
	private String dbDriver;
	private String dbTable;
	
	private static final Logger log = Logger.getLogger(LamsLessonDaoMySqlJDBC.class);
	
	
	/**
	 * Empty constructor
	 */
	public LamsLessonDaoJDBC() {
	}

	/**
	 * Constructor using the lamswebct Powerlink settings hashmap
	 * @param settings lamswebct Powerlink settings hashmap
	 */
	public LamsLessonDaoJDBC(Map settings) {
		this.settings = settings;
		
		this.dbUrl =  (String)settings.get(Constants.SETTING_DB_URL);
		this.dbUser = (String)settings.get(Constants.SETTING_DB_USER);
		this.dbPass = (String)settings.get(Constants.SETTING_DB_PASS);
		this.dbDriver = (String)settings.get(Constants.SETTING_DB_DRIVER);
		this.dbTable = (String)settings.get(Constants.SETTING_DB_TABLE);
		
	}

	/**
	 * Full constructor
	 */
	public LamsLessonDaoJDBC(String dbUrl, String dbDriver, String dbUser, String dbPass, String dbTable) {

		this.dbDriver = dbDriver;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		this.dbUrl = dbUrl;
		this.dbTable = dbTable;

	}
	
	public Connection getConnection()
	{
	
		Connection conn = null;
		try{
			Class.forName(dbDriver).newInstance();
			conn =  DriverManager.getConnection(dbUrl,dbUser,dbPass);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Problem getting database connection.", e);
		}
		
		
		return conn;

		
	}
	
	public ArrayList getDBLessons(long learningContextId, long ptId) throws Exception
	{
		ArrayList lessons = new ArrayList();
		
		Connection connection;
		try
		{
			connection = getConnection();
			Statement stmt = connection.createStatement();
			
			String query = "SELECT lesson_id," +
			 		"learning_context_id," +
			 		"sequence_id," +
			 		"owner_id," +
			 		"owner_first_name," +
			 		"owner_last_name," +
			 		"title," +
			 		"description," +
			 		"hidden," +
			 		"schedule," +
			 		"start_date_time," +
			 		"end_date_time " +
			 		"FROM " +dbTable+
			 		" WHERE (learning_context_id=" +learningContextId+ ") " +
			 		"AND   (pt_id=" +ptId+ ")";
			
			//System.out.println("SQL INSERT: " +query);
			
			ResultSet rs = stmt.executeQuery(query);	
			
			while (rs.next()) 
			{
			    LamsLesson lesson = new LamsLesson();
			    lesson.setLessonId(rs.getLong("lesson_id"));
			    lesson.setLearningContextId(rs.getLong("learning_context_id"));
			    lesson.setSequenceId(rs.getLong("sequence_id"));
			    lesson.setTitle(rs.getString("title"));
			    lesson.setDescription(rs.getString("description"));
			    lesson.setOwnerId(rs.getString("owner_id"));
			    lesson.setOwnerFirstName(rs.getString("owner_first_name"));
			    lesson.setOwnerLastName(rs.getString("owner_last_name"));
			    lesson.setHidden(rs.getBoolean("hidden"));
			    lesson.setSchedule(rs.getBoolean("schedule"));
			    lesson.setStartTimestamp(rs.getTimestamp("start_date_time"));
			    lesson.setEndTimestamp(rs.getTimestamp("end_date_time"));
			
			    lessons.add(lesson);
			}
		
			stmt.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Failed to get a list of LAMS lessons.", e);
			throw new Exception ("Failed to get a list of LAMS lessons.");
		}
		return lessons;
	}
	
	public ArrayList getDBLessonsForLearner(long learningContextId, 
				long ptId,
				Timestamp now) throws Exception
	{
		ArrayList lessons = new ArrayList();
		Connection connection;
		try
		{
			connection = getConnection();
			//Statement stmt = connection.createStatement();
			
			String query = "SELECT lesson_id," +
			 		"learning_context_id," +
			 		"sequence_id," +
			 		"owner_id," +
			 		"owner_first_name," +
			 		"owner_last_name," +
			 		"title," +
			 		"description," +
			 		"hidden," +
			 		"schedule," +
			 		"start_date_time," +
			 		"end_date_time " +
			 		"FROM " +dbTable+
			 		" WHERE " +
			 		"(" +
				 			"(learning_context_id=?) " +
				 			"AND (pt_id=?) " +
				 			"AND (hidden='false') " +
				 			"AND (" +
					 			"(schedule='false') " +
					 			"OR (" +
					 					"(start_date_time <=?) " +
					 					"AND (end_date_time >=?)" +
					 				")" +
					 			"OR (" +
					 					"(start_date_time <=?) " +
					 					"AND (end_date_time IS null)" +
					 				")" +
					 		")" +
					 ")";

			
			//System.out.println("SQL INSERT: " +query);
			
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setLong(1, learningContextId);
			pstmt.setLong(2, ptId);
			pstmt.setTimestamp(3, now);
			pstmt.setTimestamp(4, now);
			pstmt.setTimestamp(5, now);
			
			ResultSet rs = pstmt.executeQuery(query);	

			while (rs.next()) 
			{
			    LamsLesson lesson = new LamsLesson();
			    lesson.setLessonId(rs.getLong("lesson_id"));
			    lesson.setLearningContextId(rs.getLong("learning_context_id"));
			    lesson.setSequenceId(rs.getLong("sequence_id"));
			    lesson.setTitle(rs.getString("title"));
			    lesson.setDescription(rs.getString("description"));
			    lesson.setOwnerId(rs.getString("owner_id"));
			    lesson.setOwnerFirstName(rs.getString("owner_first_name"));
			    lesson.setOwnerLastName(rs.getString("owner_last_name"));
			    lesson.setHidden(rs.getBoolean("hidden"));
			    lesson.setSchedule(rs.getBoolean("schedule"));
			    lesson.setStartTimestamp(rs.getTimestamp("start_date_time"));
			    lesson.setEndTimestamp(rs.getTimestamp("end_date_time"));
			
			    lessons.add(lesson);
			}
		
			pstmt.close();
			connection.close();
		}
		catch (SQLException e)
		{
			log.error("Failed to get a list of LAMS lessons.", e);
			throw new Exception ("Failed to get a list of LAMS lessons.");
		}
		return lessons;
	}
	
	public boolean createDbLesson(LamsLesson lesson) throws Exception
	{
		Connection connection;
		
		try
		{
			connection = getConnection();
			
			String insert = "INSERT INTO " +dbTable+
			" (lesson_id," +
			"pt_id," +
			"learning_context_id," +
			"sequence_id," +
			"owner_id," +
			"owner_first_name," +
			"owner_last_name," +
			"title," +
			"description," +
			"hidden," +
			"schedule," +
			"start_date_time," +
			"end_date_time)" +
			"VALUES" +
			"(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement pstmt = connection.prepareStatement(insert);
			pstmt.setLong(1, lesson.getLessonId());
			pstmt.setLong(2, lesson.getPtId());
			pstmt.setLong(3, lesson.getLearningContextId());
			pstmt.setLong(4, lesson.getSequenceId());
			pstmt.setString(5, lesson.getOwnerId());
			pstmt.setString(6, lesson.getOwnerFirstName());
			pstmt.setString(7, lesson.getOwnerLastName());
			pstmt.setString(8, lesson.getTitle());
			pstmt.setString(9, lesson.getDescription());
			pstmt.setBoolean(10, lesson.getHidden());
			pstmt.setBoolean(11, lesson.getSchedule());
			pstmt.setTimestamp(12, lesson.getStartTimestamp());
			pstmt.setTimestamp(13, lesson.getEndTimestamp());
			
			int rows = pstmt.executeUpdate(insert);
			
			if (!connection.getAutoCommit())
				connection.commit();
			pstmt.close();
			connection.close();
			
			return rows>0;
		}
		catch (SQLException e)
		{			
			e.printStackTrace();
			log.error("Error inserting LAMS lesson into database.", e);
			throw new Exception ("Error inserting LAMS lesson into database. " + e);
		}
		
	}
	
	public boolean updateLesson(LamsLesson lesson)
	{
		Connection connection;
		int rows = 0;
		try
		{
			connection = getConnection();
			//Statement stmt = connection.createStatement();
			
			//int hidden=0;
			//int schedule=0;
			
			//if (lesson.getHidden()) {hidden=1;}
			//if (lesson.getSchedule()) {schedule=1;}
			
			//String startTimeStamp = convertTimestamp(lesson.getStartTimestamp());
			//String endTimeStamp = convertTimestamp(lesson.getEndTimestamp());
			
			String update="UPDATE " +dbTable+
					" SET pt_id = (?,?,?,?,?,?,?,?,?,?,?,?)" +
				    "WHERE lesson_id = ?";
			
			//System.out.println("UPDATE: " + update);
			
			PreparedStatement pstmt = connection.prepareStatement(update);
			
			pstmt.setLong(1, lesson.getPtId());
			pstmt.setLong(2, lesson.getLearningContextId());
			pstmt.setLong(3, lesson.getSequenceId());
			pstmt.setString(4, lesson.getOwnerId());
			pstmt.setString(5, lesson.getOwnerFirstName());
			pstmt.setString(6, lesson.getOwnerLastName());
			pstmt.setString(7, lesson.getTitle());
			pstmt.setString(8, lesson.getDescription());
			pstmt.setBoolean(9, lesson.getHidden());
			pstmt.setBoolean(10, lesson.getSchedule());
			pstmt.setTimestamp(11, lesson.getStartTimestamp());
			pstmt.setTimestamp(12, lesson.getEndTimestamp());
			pstmt.setLong(13, lesson.getLessonId());
			
			rows = pstmt.executeUpdate(update);
			pstmt.close();
			
			if (!connection.getAutoCommit())
				connection.commit();
			connection.close();
			
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Error updating LAMS lesson to database.", e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Error updating LAMS lesson to database.", e);
		}
		
		return rows>0;
	
	}
	
	
	public boolean deleteDbLesson(long lsId)
	{
		Connection connection;
		int rows = 0;
		try
		{
			connection = getConnection();
			Statement stmt = connection.createStatement();
			
			String delete="DELETE FROM "+dbTable+" WHERE lesson_id=" +lsId;
			//System.out.println("DELETE: " + delete);
			
			rows = stmt.executeUpdate(delete);
			stmt.close();
			
			if (!connection.getAutoCommit())
				connection.commit();
			connection.close();
			
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Error deleting LAMS lesson from database.", e);
		}
		
		return rows>0;
		
	}
	
	public LamsLesson getDBLesson(String lsId) throws Exception
	{
		LamsLesson lesson = new LamsLesson();;
		Connection connection;
		try
		{
			connection = getConnection();
			Statement stmt = connection.createStatement();
			
			String query = "SELECT lesson_id," +
			 		"pt_id," +
			 		"learning_context_id," +
			 		"sequence_id," +
			 		"owner_id," +
			 		"owner_first_name," +
			 		"owner_last_name," +
			 		"title," +
			 		"description," +
			 		"hidden," +
			 		"schedule," +
			 		"start_date_time," +
			 		"end_date_time " +
			 		"FROM " +dbTable+
			 		" WHERE (lesson_id=" +lsId+ ")";
			
			//System.out.println("GET LESSON: " +query);
			
			ResultSet rs = stmt.executeQuery(query);	
			
			rs.next();
		    
		    lesson.setLessonId(rs.getLong("lesson_id"));
		    lesson.setPtId(rs.getLong("pt_id"));
		    lesson.setLearningContextId(rs.getLong("learning_context_id"));
		    lesson.setSequenceId(rs.getLong("sequence_id"));
		    lesson.setTitle(rs.getString("title"));
		    lesson.setDescription(rs.getString("description"));
		    lesson.setOwnerId(rs.getString("owner_id"));
		    lesson.setOwnerFirstName(rs.getString("owner_first_name"));
		    lesson.setOwnerLastName(rs.getString("owner_last_name"));
		    lesson.setHidden(rs.getBoolean("hidden"));
		    lesson.setSchedule(rs.getBoolean("schedule"));
		    lesson.setStartTimestamp(rs.getTimestamp("start_date_time"));
		    lesson.setEndTimestamp(rs.getTimestamp("end_date_time"));
		
			stmt.close();
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Failed to get LAMS lesson.", e);
			throw new Exception ("Failed to get LAMS lesson.");
		}
		catch (Exception e)
		{
			throw new Exception ("Failed to get LAMS lesson.");
		}
		return lesson;
	}
	
	public String convertTimestamp(Timestamp t)
	{
		String timestampStr = "null";
		if (t!=null)
		{
			timestampStr = "\'" + t + "\'";
			//timestampStr = timestampStr.replaceAll("-", "") + "\'";
		}
		return timestampStr;
	}
	
	
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}


	public Map getSettings() {
		return settings;
	}


	public void setSettings(Map settings) {
		this.settings = settings;
	}
}

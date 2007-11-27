package org.lamsfoundation.integration.dao;

import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import org.lamsfoundation.integration.util.Constants;
import org.lamsfoundation.integration.webct.LamsLesson;



/**
 * Database connector for webct/lams integration
 * @author luke foxton
 *
 */
public class LamsLessonDaoJDBC {
	
	private Map settings;
	
	private String dbUrl;
	private String dbHost;
	private String dbPort;
	private String dbName;
	private String dbUser;
	private String dbPass;
	
	private static final Logger log = Logger.getLogger(LamsLessonDaoJDBC.class);
	
	
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
		
		dbHost = (String)settings.get(Constants.SETTING_DB_HOST);
		dbPort = (String)settings.get(Constants.SETTING_DB_PORT);
		dbName = (String)settings.get(Constants.SETTING_DB_NAME);
		dbUser = (String)settings.get(Constants.SETTING_DB_USER);
		dbPass = (String)settings.get(Constants.SETTING_DB_PASS);
		
		this.dbUrl = "jdbc:microsoft:sqlserver://" +dbHost+ 
		":" +dbPort+ "/" +dbName+ "/";
	}

	/**
	 * Full constructor
	 */
	public LamsLessonDaoJDBC(String dbHost, String dbPort,
			String dbName, String dbUser, String dbPass) {
		this.dbHost = dbHost;
		this.dbPort = dbPort;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		
		this.dbUrl = "jdbc:microsoft:sqlserver://" +dbHost+ 
					":" +dbPort+ "/" +dbName+ "/";
	}
	
	public Connection getConnection() throws SQLException
	{
	
		//DriverManager.registerDriver(new SQLServerDriver());
		//DriverManager.registerDriver(new Driver());
		//Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
		//return connection;
		
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setServerName(dbHost);
		ds.setUser(dbUser);
		ds.setPassword(dbPass);
		ds.setPortNumber(Integer.parseInt(dbPort)); 
		ds.setDatabaseName(dbName);
		return ds.getConnection();

		
	}
	
	public ArrayList getDBLessons(long learningContextId, long ptId) throws Exception
	{
		ArrayList lessons = new ArrayList();
		
		Connection connection;
		try
		{
			connection = getConnection();
			Statement stmt = connection.createStatement();
			
			String query = "SELECT [lesson_id]," +
			 		"[learning_context_id]," +
			 		"[sequence_id]," +
			 		"[owner_id]," +
			 		"[owner_first_name]," +
			 		"[owner_last_name]," +
			 		"[title]," +
			 		"[description]," +
			 		"[hidden]," +
			 		"[schedule]," +
			 		"[start_date_time]," +
			 		"[end_date_time] " +
			 		"FROM [webctdatabase].[dbo].[LAMS_LESSON] " +
			 		"WHERE (learning_context_id=" +learningContextId+ ") " +
			 		"AND   (pt_id=" +ptId+ ")";
			
			System.out.println("SQL INSERT: " +query);
			
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
			Statement stmt = connection.createStatement();
			
			String query = "SELECT [lesson_id]," +
			 		"[learning_context_id]," +
			 		"[sequence_id]," +
			 		"[owner_id]," +
			 		"[owner_first_name]," +
			 		"[owner_last_name]," +
			 		"[title]," +
			 		"[description]," +
			 		"[hidden]," +
			 		"[schedule]," +
			 		"[start_date_time]," +
			 		"[end_date_time] " +
			 		"FROM [webctdatabase].[dbo].[LAMS_LESSON] " +
			 		"WHERE " +
			 		"(" +
				 			"(learning_context_id=" +learningContextId+ ") " +
				 			"AND (pt_id=" +ptId+ ") " +
				 			"AND (hidden='false') " +
				 			"AND (" +
					 			"(schedule='false') " +
					 			"OR (" +
					 					"(start_date_time <= '" +now.toString()+ "') " +
					 					"AND (end_date_time >='" +now.toString()+ "\')" +
					 				")" +
					 			"OR (" +
					 					"(start_date_time <= '" +now.toString()+ "') " +
					 					"AND (end_date_time IS null)" +
					 				")" +
					 		")" +
					 ")";

			
			System.out.println("SQL INSERT: " +query);
			
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
			log.error("Failed to get a list of LAMS lessons.", e);
			throw new Exception ("Failed to get a list of LAMS lessons.");
		}
		return lessons;
	}
	
	public boolean createDbLesson(LamsLesson lesson) throws Exception
	{
		Connection connection;
		
		System.out.println(lesson.toString());
		
		int hidden=0;
		int schedule=0;
		
		if (lesson.getHidden()) {hidden=1;}
		if (lesson.getSchedule()) {schedule=1;}
		
		String startTimeStamp = "null";
		if (lesson.getStartTimestamp()!=null)
		{
			startTimeStamp = "\'" +lesson.getStartTimestamp();
			startTimeStamp = startTimeStamp.replaceAll("-", "") + "\'";
		}
			
		String endTimeStamp = "null";
		if (lesson.getEndTimestamp()!=null)
		{	
			endTimeStamp = "\'" + lesson.getEndTimestamp();
			endTimeStamp = endTimeStamp.replaceAll("-", "") + "\'";
		}
		
		System.out.println("START: " +startTimeStamp);
		System.out.println("END: " +endTimeStamp);
		
		
		try
		{
			connection = getConnection();
			Statement stmt = connection.createStatement();
			
			
			String insert = "INSERT INTO [webctdatabase].[dbo].[LAMS_LESSON]" +
			"([lesson_id]," +
			"[pt_id]," +
			"[learning_context_id]," +
			"[sequence_id]," +
			"[owner_id]," +
			"[owner_first_name]," +
			"[owner_last_name]," +
			"[title]," +
			"[description]," +
			"[hidden]," +
			"[schedule]," +
			"[start_date_time]," +
			"[end_date_time])" +
			"VALUES" +
			"(" +lesson.getLessonId()+ "," + 
			"" +lesson.getPtId()+ "," +
			"" +lesson.getLearningContextId()+ "," +
			"" +lesson.getSequenceId()+ "," +
			"\'" +lesson.getOwnerId()+ "\'," +
			"\'" +lesson.getOwnerFirstName()+ "\'," +
			"\'" +lesson.getOwnerLastName()+ "\'," +
			"\'" +lesson.getTitle()+ "\'," +
			"\'" +lesson.getDescription()+ "\'," +
			"" +hidden+ ","+
			"" +schedule+ "," +
			"" +startTimeStamp+ "," +
			"" +endTimeStamp+ ")";
			
			System.out.println("SQL INSERT: " +insert);

			int rows = stmt.executeUpdate(insert);
			connection.commit();
			stmt.close();
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
			Statement stmt = connection.createStatement();
			
			String startTimeStamp = "null";
			if (lesson.getStartTimestamp()!=null)
				startTimeStamp = lesson.getStartTimestamp().toString().replaceAll("-", "");
			
			String endTimeStamp = "null";
			if (lesson.getEndTimestamp()!=null)
				endTimeStamp = lesson.getEndTimestamp().toString().replaceAll("-", "");
			
			
			String update="UPDATE [webctdatabase].[dbo].[LAMS_LESSON]" +
					" SET [pt_id] = " +lesson.getPtId()+
				    ",[learning_context_id] = " +lesson.getLearningContextId()+
				    ",[sequence_id] = " + lesson.getSequenceId()+
				    ",[owner_id] = \'" + lesson.getOwnerId()+ "\'" +
				    ",[owner_first_name] = \'" +lesson.getOwnerFirstName()+ "\'" +
				    ",[owner_last_name] = \'" + lesson.getOwnerLastName()+ "\'" +
				    ",[title] = \'" +lesson.getTitle()+ "\'" +
				    ",[description] = \'" +lesson.getDescription()+ "\'" +
				    ",[hidden] = \'" +lesson.getHidden()+ "\'" + 
				    ",[schedule] = \'" +lesson.getSchedule()+ "\'" +
				    ",[start_date_time] = \'" +startTimeStamp+ "\'" +
				    ",[end_date_time] = \'" +endTimeStamp+ "\' " +
				    "WHERE [lesson_id] = " + lesson.getLessonId();
			
			System.out.println("UPDATE: " + update);
			
			rows = stmt.executeUpdate(update);
			stmt.close();
			
			connection.commit();
			connection.close();
			
			
		}
		catch (SQLException e)
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
			
			String delete="DELETE FROM [webctdatabase].[dbo].[LAMS_LESSON] WHERE lesson_id=" +lsId;
			System.out.println("DELETE: " + delete);
			
			rows = stmt.executeUpdate(delete);
			stmt.close();
			
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
			
			String query = "SELECT [lesson_id]," +
			 		"[pt_id]," +
			 		"[learning_context_id]," +
			 		"[sequence_id]," +
			 		"[owner_id]," +
			 		"[owner_first_name]," +
			 		"[owner_last_name]," +
			 		"[title]," +
			 		"[description]," +
			 		"[hidden]," +
			 		"[schedule]," +
			 		"[start_date_time]," +
			 		"[end_date_time] " +
			 		"FROM [webctdatabase].[dbo].[LAMS_LESSON] " +
			 		"WHERE (lesson_id=" +lsId+ ")";
			
			System.out.println("GET LESSON: " +query);
			
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
	
	
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
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

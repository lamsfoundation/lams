package org.lamsfoundation.integration.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.lamsfoundation.integration.webct.LamsLesson;


public interface ILamsLessonDao 
{
	
	/**
	 * Getting the connection to the database
	 * @return Connection to database
	 * @throws SQLException
	 */
	Connection getConnection() throws SQLException;
	
	/**
	 * Getting a list of lessons
	 * @param learningContextId the learning context id 
	 * @param ptId the powerlink instance id
	 * @return a list of LAMS lessons objects
	 * @throws Exception
	 */
	ArrayList getDBLessons(long learningContextId, long ptId) throws Exception;

	/**
	 * Gets a shortened list of "running" lessons for learner
	 * @param learningContextId the learning context id 
	 * @param ptId the powerlink instance id
	 * @param now an sql timestamp for the current or desired timeframe
	 * @return A list of running lessons
	 * @throws Exception
	 */
	ArrayList getDBLessonsForLearner(long learningContextId, long ptId, Timestamp now) throws Exception;
	
	/**
	 * Create a new lesson in the db
	 * @param lesson the lesson to be created
	 * @return true for succes
	 * @throws Exception 
	 */
	boolean createDbLesson(LamsLesson lesson) throws Exception;	

	/**
	 * Update the given lesson
	 * @param lesson the lesson to be updated
	 * @return true for success
	 */
	boolean updateLesson(LamsLesson lesson);
	
	/**
	 * delete a lesson from the db
	 * @param lsId the learning session id of the lesson
	 * @return true if success
	 */
	boolean deleteDbLesson(long lsId);
	
	/**
	 * Gets a specific lams lesson from the db
	 * @param lsId the unique learning session id
	 * @return the result lams lesson
	 * @throws Exception
	 */
	LamsLesson getDBLesson(String lsId) throws Exception;
}

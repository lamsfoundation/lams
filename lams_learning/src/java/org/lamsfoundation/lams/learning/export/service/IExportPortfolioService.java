/***************************************************************************
* Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
* =============================================================
* 
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
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
* ***********************************************************************/


/*
 * Created on Aug 30, 2005
 */
package org.lamsfoundation.lams.learning.export.service;

import java.util.Map;
import java.util.Vector;
import javax.servlet.http.Cookie;

import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
/**
 * @author mtruong
 *
 * <p>The algorithm for EP must work regardless of whether or not users 
 * have completed the activities or not, and that an export can be 
 * triggered at any time during the lesson.</p>
 *
 * <p>An export can be viewed from two perspectives:
 *<ul><li>	Teacher’s perspective: In which the export will export data for the
 * whole class, for all activities in the learning design. The tool content
 * id will be supplied, or it can be obtained from the learning design.</li>
 *	Student’s perspective: In which the export carried out will return the data specific to the activities the student has completed. The tool session id and the user id will be supplied.
 *<li>
 * The EP algorithm will work the same way regardless of whether the 
 * export is being carried out by a student or teacher. It will have a 
 * list of all the activities in the learning design (if export is being
 * done by a teacher) or it will contain all the activities that the 
 * student has completed. The general gist is that it will have a list 
 * of activity ids as its input and will iterate through all these activities 
 * and compile all the data returned from each tool’s export portfolio 
 * functionality and will compile this data into one large plain HTML file. 
 * </li></ul></p>
 */
public interface IExportPortfolioService {
	
    /**
	 * This is the main method that performs the export for the teacher.
	 * It will get the list of ordered activities from the learning design
	 * and will setup the portfolios for each activity. Returns an array of 
	 * Portfolio objects, in which the web layer can iterate through the array 
	 * of portfolios to display the links to each activity.
	 * @param lesson The specific instance of the LearningDesign and Class.
	 * @return Portfolio[] The array of portfolio objects.
	 */
	public Portfolio[] exportPortfolioForTeacher(Lesson lesson, Cookie[] cookies);

	/**
	 * The main method that performs the export for the student.
	 * It will get the list of activities that the user has completed 
	 * and will setup the portfolios for each activity. 
	 * @param learnerProgressId The learner progress id, which is used to determine the learner progress and the 
	 * list of activities completed by the learner.
	 * @param user The learner.
	 * @param anonymity The anonymity flag, is true, then anonymity is on, otherwise username would be visible.
	 * @return Portfolio[] The array of portfolio objects.
	 */
	public Portfolio[] exportPortfolioForStudent(Long learnerProgressId, User user, boolean anonymity, Cookie[] cookies);
	
	/**
	 * A helper method that sets up the export url for the activity. Will go through all key-value pairs
	 * in this map, and append it to the url given.
	 * @param parametersToAppend Map containing parameter name-parameter value pairs, to be appended to the end of the url
	 * @param url The url in which the parameters should be appended.
	 * @return The url with all parameters appended.
	 */
	public String setupExportUrl(Map parametersToAppend, String url);
	
	/**
	 * Obtains the Tool from the ToolActivity and creates a portfolio object with properties activityId, activityName, 
	 * activityDescription, exportURL set to the value of the ToolActivity's properties activityId, toolDisplayName 
	 * (retrieved from Tool object) , title, exportPortfolioUrl respestively.
	 * 
	 * @param activity The Tool Activity
	 * @return a Portfolio object
	 */
	public Portfolio createPortfolio(ToolActivity activity);
	
	/**
	 * This method will iterate through the list of ordered activities and create a Portfolio
	 * object for each activity. It will set up most of the properies of the Portfolio object.
	 * This method is used regardless of whether the export is being done by the teacher or
	 * the student. However, if the export is being done be the teacher, the User object should
	 * be null.
	 * 
	 * 
	 * @param orderedActivityList The ordered activity list to iterate through
	 * @param accessMode The tool access mode, either Teacher or Learner.
	 * @param user The learner, or null if export is being done by the teacher.
	 * @return the array of Portfolio objects
	 * @throws LamsToolServiceException
	 */
	public Vector setupPortfolios(Vector orderedActivityList, ToolAccessMode accessMode, User user) throws LamsToolServiceException;

	/**
	 * Returns the ordered activity list (ordered by the ActivityOrderComparator)
	 * containing all activities present in the learning design. The list will
	 * only contain Tool Activities. If it comes across a Complex Activity, it
	 * will only add its child activities in the ordered list.
	 * @param learningDesign The learning design in which the export is being done.
	 * @return Vector the ordered list of activities.
	 */
	public Vector getOrderedActivityList(LearningDesign learningDesign);

	
	/** 
	 * Returns the ordered activity list (ordered by the ActivityOrderComparator)
	 * containing the list of activities completed by the learner.
	 * The reason why the learnerProgressId is passed as a parameter (as opposed
	 * to an actual LearnerProgress object, is so that in the same transaction, we 
	 * can obtain the LearnerProgress object (from the learnerProgressId) and then
	 * copy the set of completedActivities into a Vector.
	 * The above equivalent methods which take in LearnerProgress object/Lesson object
	 * as a parameter, throws a "failed to lazily initialize collection" exception
	 * when trying to copy the set of completed activities into the vector (as it cannot be accessed)
	 * @param learnerProgressId
	 * @return
	 */
	public Vector getOrderedActivityList(Long learnerProgressId);
	
	/**
	 * Creates a directory with the name <code>directoryName</code>.
	 * 
	 * @param directoryName The name and location of the directory.
	 * @return true if the directory has been created, false otherwise
	 */
	//	public boolean createTempDirectory(String directoryName);
	
	
	/**
	 * Zips up the directory specified by <code>directoryToZip</code> and
	 * saves it in the filename given by <code>filename</code>.
	 * 
	 * @param filename
	 * @param directoryToZip
	 * @return
	 */
	public String zipPortfolio(String filename, String directoryToZip);
	
	/**
	 * Iterates through the list of portfolios, creates a subdirectory for each of the tools
	 * and appends the temporary export directory to the export url. It will then call each 
	 * tool to write its files in the subdirectory.
	 * The tool returns the name of the main html file written, the portfolio property
	 * mainFileName is set to this value.
	 * 
	 * @param portfolios
	 * @return An array of portfolios that can be used to generate the main export page.
	 */
	public Portfolio[] doExport(Vector portfolios, String tempDirectoryName, Cookie[] cookies);
	/**
	 * This method is responsible for the call to the tool to export their portfolio via the export url.
	 * It uses a HttpURLConnection to connect to the export url of each tool and returns the filename
	 * of the main export page of the tool. 
	 * 
	 * @param tool The portfolio object, which contains the exportUrl in which to connect to.
	 * @return The main file name of the tool's page.
	 */
	//commented out to use common code from HttpUrlConnectionUtil
	public String connectToToolViaExportURL(String exportURL, Cookie[] cookies);
	
	/*	public Portfolio[] exportPortfolioForStudent(LearnerProgress learnerProgress, User user, boolean anonymity);
	
	public Portfolio[] exportPortfolioForStudent(Lesson lesson, User user, boolean anonymity); 
	
	public Vector getOrderedActivityList(LearnerProgress learnerProgress); */
	
}

/*
 * Created on Aug 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.export.service;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
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
	
	public Portfolio[] exportPortfolioForTeacher(Lesson lesson);
	
	public Portfolio[] exportPortfolioForStudent(LearnerProgress learnerProgress, User user, boolean anonymity);
	
	public Portfolio[] exportPortfolioForStudent(Lesson lesson, User user, boolean anonymity);
	
	public Portfolio[] exportPortfolioForStudent(Long learnerProgressId, User user, boolean anonymity);
	
	public String setupExportUrl(Map parametersToAppend, String url);
	
	public Portfolio createPortfolio(ToolActivity activity);
	
	public Portfolio[] setupPortfolios(Vector activitySet, ToolAccessMode accessMode, User user) throws LamsToolServiceException;

	public Vector getOrderedActivityList(LearningDesign learningDesign);
	
	public Vector getOrderedActivityList(LearnerProgress learnerProgress);
	
	/** 
	 * Returns the ordered activity list containing the list of activities 
	 * completed by the leaner.
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
	
	public boolean createTempDirectory(String directoryName);
	
	public String zipPortfolio(String filename, String directoryToZip);
	
//public boolean createDirectory(
	
}

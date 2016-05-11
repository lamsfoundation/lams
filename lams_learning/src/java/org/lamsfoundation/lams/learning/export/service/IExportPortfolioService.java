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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.learning.export.service;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learning.export.Portfolio;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.tool.ToolAccessMode;

/**
 * @author mtruong
 *
 *         <p>
 *         The algorithm for EP must work regardless of whether or not users
 *         have completed the activities or not, and that an export can be
 *         triggered at any time during the lesson.
 *         </p>
 *
 *         <p>
 *         An export can be viewed from two perspectives:
 *         <ul>
 *         <li>Teacher�s perspective: In which the export will export data for the
 *         whole class, for all activities in the learning design. The tool content
 *         id will be supplied, or it can be obtained from the learning design.</li>
 *         Student�s perspective: In which the export carried out will return the data specific to the activities the
 *         student has completed. The tool session id and the user id will be supplied.
 *         <li>
 *         The EP algorithm will work the same way regardless of whether the
 *         export is being carried out by a student or teacher. It will have a
 *         list of all the activities in the learning design (if export is being
 *         done by a teacher) or it will contain all the activities that the
 *         student has completed. The general gist is that it will have a list
 *         of activity ids as its input and will iterate through all these activities
 *         and compile all the data returned from each tool�s export portfolio
 *         functionality and will compile this data into one large plain HTML file.
 *         </li>
 *         </ul>
 *         </p>
 */
public interface IExportPortfolioService {

    /**
     * This is the main method that performs the export for the teacher.
     * It will get the list of ordered activities from the learning design
     * and will setup the portfolios for each tool activity. Returns a Portfolio
     * object which contains information about the temporary export directory
     * and an array of ToolPortfolio objects.
     * 
     * If a Lesson with <code>lessonId</code> cannot be found, a Portfolio
     * object will be created with attribute exportTmpDir set, and the array
     * of ToolPortfolio objects being set to null. The MainExportServlet will
     * detect that this array of ToolPortfolio objects is null, and will generate
     * a main page indicating that the export cannot be performed.
     * 
     * @param lesson
     *            The specific instance of the LearningDesign and Class.
     * @param cookies.
     *            Are passed along to doExport, in order for access the export for other tools
     * @return Portfolio Portfolio object which contains the array of ToolPortfolio objects.
     */
    public Portfolio exportPortfolioForTeacher(Long lessonId, Cookie[] cookies);

    /**
     * The main method that performs the export for the student.
     * It will get the list of activities that the user has completed
     * and will setup the portfolios for each tool activity. Much like the
     * functionality for exportPortfolioForTeacher, this method returns
     * a Portfolio object.
     * 
     * If the learner has not yet started this sequence (a null LearnerProgress)
     * then a Portfolio object will be created with the attribute exportTmpDir set
     * and the array of ToolPortfolio objects being set to null.
     * 
     * @param userId
     *            The learner id.
     * @param lessonID
     *            The lesson id of the lesson, the learner is doing the export for
     * @param anonymity
     *            The anonymity flag, is true, then anonymity is on, otherwise username would be visible.
     * @param accessMode
     *            The access mode to set.
     * @param cookies
     *            Are passed along to doExport, in order for access the export for other tools
     * @return Portfolio Portfolio object which contains the array of ToolPortfolio objects.
     */
    public Portfolio exportPortfolioForStudent(Integer userId, Long lessonID, boolean anonymity,
	    ToolAccessMode accessMode, Cookie[] cookies);

    /**
     * Zips up the directory specified by <code>directoryToZip</code> and
     * saves it in the filename given by <code>filename</code>.
     * 
     * @param filename
     * @param directoryToZip
     * @return name of directory containing zipped up portfolio.
     */
    public String zipPortfolio(String filename, String directoryToZip);

    /** Generate the main page, given this portfolio */
    public void generateMainPage(HttpServletRequest request, Portfolio portfolio, Cookie[] cookies);

    /** Generate the main notebook page, given this portfolio */
    public void generateNotebookPage(HttpServletRequest request, Portfolio portfolio, Cookie[] cookies);

    /**
     * Gets the themes for the current user. This is used to determine the stylesheets
     * included in the export file. We need the full theme, not just the name, so we can get
     * the directory names for the images.
     */
    public Collection<Theme> getUserThemes();

}

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

/* $$Id$$ */
package org.lamsfoundation.lams.learning.export;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Competence;

/**
 * @author mtruong
 */
public class Portfolio {

    private String exportID;
    private String exportTmpDir;
    private String contentFolderID;
    private String lessonName;
    private String lessonDescription;
    private ActivityPortfolio[] activityPortfolios;
    private NotebookPortfolio[] notebookPortfolios;
    private String learnerName;
    private Date lessonStartDate;
    private Date portfolioCreatedDate;
    private String notebookLink;
    private String notebookDir;
    private Set<Competence> competencesDefined = new HashSet<Competence>();

    public Portfolio(String exportID) {
	exportTmpDir = null;
	activityPortfolios = null;
	this.exportID = exportID;
	portfolioCreatedDate = new Date();
	notebookLink = null;
	notebookDir = null;
    }

    /**
     * @return Returns the exportTmpDir.
     */
    public String getExportTmpDir() {
	return exportTmpDir;
    }

    /**
     * @param exportTmpDir
     *            The exportTmpDir to set.
     */
    public void setExportTmpDir(String exportTmpDir) {
	this.exportTmpDir = exportTmpDir;
    }

    /**
     * @return Returns the 32-character content folder name.
     */
    public String getContentFolderID() {
	return contentFolderID;
    }

    /**
     * @param contentFolderID
     *            The 32-character content folder name to set.
     */
    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public String getNotebookDir() {
	return notebookDir;
    }

    public void setNotebookDir(String notebookDir) {
	this.notebookDir = notebookDir;
    }

    /**
     * @return Returns the toolPortfolios.
     */
    public ActivityPortfolio[] getActivityPortfolios() {
	return activityPortfolios;
    }

    /**
     * @param toolPortfolios
     *            The toolPortfolios to set.
     */
    public void setActivityPortfolios(ActivityPortfolio[] activityPortfolios) {
	this.activityPortfolios = activityPortfolios;
    }

    public NotebookPortfolio[] getNotebookPortfolios() {
	return notebookPortfolios;
    }

    public void setNotebookPortfolios(NotebookPortfolio[] notebookPortfolios) {
	this.notebookPortfolios = notebookPortfolios;
    }

    public String getLessonName() {
	return lessonName;
    }

    public void setLessonName(String lessonName) {
	this.lessonName = lessonName;
    }

    public String getLessonDescription() {
	return lessonDescription;
    }

    public void setLessonDescription(String lessonDescription) {
	this.lessonDescription = lessonDescription;
    }

    /** Get the internal id generated to keep this unique. Used in the directory name */
    public String getExportID() {
	return exportID;
    }

    public void setExportID(String exportID) {
	this.exportID = exportID;
    }

    public String getLearnerName() {
	return learnerName;
    }

    public void setLearnerName(String learnerName) {
	this.learnerName = learnerName;
    }

    public Date getLessonStartDate() {
	return lessonStartDate;
    }

    public void setLessonStartDate(Date lessonStartDate) {
	this.lessonStartDate = lessonStartDate;
    }

    public Date getPortfolioCreatedDate() {
	return portfolioCreatedDate;
    }

    public void setPortfolioCreatedDate(Date portfolioCreatedDate) {
	this.portfolioCreatedDate = portfolioCreatedDate;
    }

    public String getNotebookLink() {
	return notebookLink;
    }

    public void setNotebookLink(String notebookLink) {
	this.notebookLink = notebookLink;
    }

    public Set<Competence> getCompetencesDefined() {
	return competencesDefined;
    }

    public void setCompetencesDefined(Set<Competence> competencesDefined) {
	this.competencesDefined = competencesDefined;
    }
}

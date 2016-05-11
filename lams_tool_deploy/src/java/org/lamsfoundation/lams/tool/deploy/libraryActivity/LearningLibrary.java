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


package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import java.util.ArrayList;

/**
 * @author mtruong
 */
public class LearningLibrary {

    private String libraryInsertScriptPath;
    private String templateActivityInsertScriptPath;
    private ArrayList toolActivityList;
    private long learningLibraryId;
    private long parentActivityId;
    private String languageFileDirectory;
    private String languageFileRoot;

    public LearningLibrary() {
    }

    public LearningLibrary(String libraryInsertScript, String templateActivityInsertScript, ArrayList toolActivityList,
	    String languageFileDirectory, String languageFileRoot) {
	this.libraryInsertScriptPath = libraryInsertScript;
	this.templateActivityInsertScriptPath = templateActivityInsertScript;
	this.toolActivityList = toolActivityList;
	this.languageFileDirectory = languageFileDirectory;
	this.languageFileRoot = languageFileRoot;
    }

    /**
     * @return Returns the libraryActivityInsertScriptPath.
     */
    public String getTemplateActivityInsertScriptPath() {
	return templateActivityInsertScriptPath;
    }

    /**
     * @param libraryActivityInsertScriptPath
     *            The libraryActivityInsertScriptPath to set.
     */
    public void setTemplateActivityInsertScriptPath(String libraryActivityInsertScriptPath) {
	this.templateActivityInsertScriptPath = libraryActivityInsertScriptPath;
    }

    /**
     * @return Returns the toolActivities.
     */
    public ArrayList getToolActivityList() {
	return toolActivityList;
    }

    /**
     * @param toolActivities
     *            The toolActivities to set.
     */
    public void setToolActivityList(ArrayList toolActivities) {
	this.toolActivityList = toolActivities;
    }

    /**
     * @return Returns the libraryInsertScriptPath.
     */
    public String getLibraryInsertScriptPath() {
	return libraryInsertScriptPath;
    }

    /**
     * @param libraryInsertScriptPath
     *            The libraryInsertScriptPath to set.
     */
    public void setLibraryInsertScriptPath(String libraryInsertScriptPath) {
	this.libraryInsertScriptPath = libraryInsertScriptPath;
    }

    public long getLearningLibraryId() {
	return learningLibraryId;
    }

    public void setLearningLibraryId(long learningLibraryId) {
	this.learningLibraryId = learningLibraryId;
    }

    public String getLanguageFileDirectory() {
	return languageFileDirectory;
    }

    public void setLanguageFileDirectory(String languageFileDirectory) {
	this.languageFileDirectory = languageFileDirectory;
    }

    public long getParentActivityId() {
	return parentActivityId;
    }

    public void setParentActivityId(long parentActivityId) {
	this.parentActivityId = parentActivityId;
    }

    /**
     * Get the "root" of the language file name. This will normally be "ApplicationResources".
     * This is converted into <root>_<language>_<country>.properties by Java.
     * 
     * @return
     */
    public String getLanguageFileRoot() {
	return languageFileRoot;
    }

    public void setLanguageFileRoot(String languageFileRoot) {
	this.languageFileRoot = languageFileRoot;
    }
}

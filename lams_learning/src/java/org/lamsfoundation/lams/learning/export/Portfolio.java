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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.learning.export;

import java.util.Date;



/**
 * @author mtruong
  */
public class Portfolio {
    
	private String exportID; 
    private String exportTmpDir;
    private String lessonName;
    private String lessonDescription;
    private ActivityPortfolio[] activityPortfolios;
    private String learnerName;
    private Date lessonStartDate;
    private Date portfolioCreatedDate;
    
    public Portfolio(String exportID)
    {
        this.exportTmpDir = null;
        this.activityPortfolios = null;
        this.exportID = exportID;
        this.portfolioCreatedDate = new Date();
    }

    /**
     * @return Returns the exportTmpDir.
     */
    public String getExportTmpDir() {
        return exportTmpDir;
    }
    /**
     * @param exportTmpDir The exportTmpDir to set.
     */
    public void setExportTmpDir(String exportTmpDir) {
        this.exportTmpDir = exportTmpDir;
    }
    /**
     * @return Returns the toolPortfolios.
     */
    public ActivityPortfolio[] getActivityPortfolios() {
        return activityPortfolios;
    }
    /**
     * @param toolPortfolios The toolPortfolios to set.
     */
    public void setActivityPortfolios(ActivityPortfolio[] activityPortfolios) {
        this.activityPortfolios = activityPortfolios;
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
}

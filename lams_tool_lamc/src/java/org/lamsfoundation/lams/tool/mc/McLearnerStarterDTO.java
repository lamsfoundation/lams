/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

import java.util.Date;

/**
 * <p>
 * DTO that holds properties for initial screen presentation
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McLearnerStarterDTO implements Comparable {

    protected String questionListingMode;

    protected String activityTitle;

    protected Date submissionDeadline;

    /**
     * @return Returns the activityTitle.
     */
    public String getActivityTitle() {
	return activityTitle;
    }

    /**
     * @param activityTitle
     *            The activityTitle to set.
     */
    public void setActivityTitle(String activityTitle) {
	this.activityTitle = activityTitle;
    }

    /**
     * @return Returns the questionListingMode.
     */
    public String getQuestionListingMode() {
	return questionListingMode;
    }

    /**
     * @param questionListingMode
     *            The questionListingMode to set.
     */
    public void setQuestionListingMode(String questionListingMode) {
	this.questionListingMode = questionListingMode;
    }

    /**
     * @return the submissionDeadline
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    /**
     * @param submissionDeadline
     *            the submissionDeadline to set
     */
    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    @Override
    public int compareTo(Object o) {
	McLearnerStarterDTO mcLearnerStarterDTO = (McLearnerStarterDTO) o;

	if (mcLearnerStarterDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

}

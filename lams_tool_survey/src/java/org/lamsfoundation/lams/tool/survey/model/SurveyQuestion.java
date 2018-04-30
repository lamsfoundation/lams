/****************************************************************
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
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.survey.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;

/**
 * Survey
 *
 * @author Dapeng Ni
 *
 *
 *
 */
public class SurveyQuestion implements Cloneable {
    private static final Logger log = Logger.getLogger(SurveyQuestion.class);
    private static final int SHORT_TITLE_LENGTH = 60;

    private Long uid;

    // Survey Type:1=Single Choice,2=Multiple Choice,3=Text Entry
    private short type;

    private String description;
    private int sequenceId;

    // option of Question
    private boolean appendText;
    private boolean optional;
    private boolean allowMultipleAnswer;

    private Set<SurveyOption> options;

    private Date createDate;
    private SurveyUser createBy;

    // ***********************************************
    // DTO fields:
    private String shortTitle;

    @Override
    public Object clone() {
	SurveyQuestion obj = null;
	try {
	    obj = (SurveyQuestion) super.clone();
	    // clone options
	    if (options != null) {
		Iterator iter = options.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    SurveyOption instruct = (SurveyOption) iter.next();
		    SurveyOption newInsruct = (SurveyOption) instruct.clone();
		    set.add(newInsruct);
		}
		obj.options = set;
	    }
	    obj.setUid(null);
	    // clone ReourceUser as well
	    if (this.createBy != null) {
		obj.setCreateBy((SurveyUser) this.createBy.clone());
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SurveyQuestion.class + " failed");
	}

	return obj;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     *
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     *
     *
     *
     * @return
     */
    public Set<SurveyOption> getOptions() {
	return options;
    }

    public void setOptions(Set<SurveyOption> itemInstructions) {
	this.options = itemInstructions;
    }

    /**
     *
     *
     * @return
     */
    public SurveyUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(SurveyUser createBy) {
	this.createBy = createBy;
    }

    /**
     *
     * @return
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     * @return
     */
    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }

    /**
     *
     * @return
     */
    public boolean isAppendText() {
	return appendText;
    }

    public void setAppendText(boolean appendText) {
	this.appendText = appendText;
    }

    /**
     *
     * @return
     */
    public boolean isOptional() {
	return optional;
    }

    public void setOptional(boolean compulsory) {
	this.optional = compulsory;
    }

    /**
     *
     * @return
     */
    public boolean isAllowMultipleAnswer() {
	return allowMultipleAnswer;
    }

    public void setAllowMultipleAnswer(boolean allowMultipleAnswer) {
	this.allowMultipleAnswer = allowMultipleAnswer;
    }

    /**
     *
     * @return
     */
    public int getSequenceId() {
	return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    public String getShortTitle() {
	return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
	this.shortTitle = shortTitle;
    }

    /** Set the short title from the current description but strip HTML and shorten to SHORT_TITLE_LENGTH characters. */
    public void updateShortTitleFromDescription() {
	// strip all images first in case they are base64 otherwise the next step causes a stack overflow
	// then strip out any other HTML tags
	String newInput = SurveyWebUtils.removeHTMLTags(this.description);
	this.shortTitle = StringUtils.abbreviate(newInput, SHORT_TITLE_LENGTH);
    }
}

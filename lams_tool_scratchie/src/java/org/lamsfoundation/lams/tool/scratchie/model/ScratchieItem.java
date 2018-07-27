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

package org.lamsfoundation.lams.tool.scratchie.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Tool may contain several questions. Which in turn contain answers.
 *
 * @author Andrey Balan
 */
public class ScratchieItem implements Cloneable {
    private static final Logger log = Logger.getLogger(ScratchieItem.class);

    private Long uid;

    private String title;

    private String description;

    private Integer orderId;

    private boolean isCreateByAuthor;

    private Date createDate;

    // scratchie Items
    private Set answers;

    // ***********************************************
    // DTO fields:
    private boolean isUnraveled;

    private String correctAnswer;
    private int userMark;
    private int userAttempts;
    private String firstChoiceAnswerLetter;
    private String burningQuestion;

    /**
     * Default contruction method.
     *
     */
    public ScratchieItem() {
	answers = new HashSet();
    }

    @Override
    public Object clone() {
	ScratchieItem item = null;
	try {
	    item = (ScratchieItem) super.clone();

	    item.setUid(null);

	    if (answers != null) {
		Iterator iter = answers.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    ScratchieAnswer answer = (ScratchieAnswer) iter.next();
		    ScratchieAnswer newAnswer = (ScratchieAnswer) answer.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newAnswer);
		}
		item.answers = set;
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ScratchieItem.class + " failed");
	}

	return item;
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
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
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
    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    /**
     *
     * @return
     */
    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    /**
     *
     *
     *
     *
     * @return
     */
    public Set getAnswers() {
	return answers;
    }

    public void setAnswers(Set answers) {
	this.answers = answers;
    }

    public boolean isUnraveled() {
	return isUnraveled;
    }

    public void setUnraveled(boolean isUnraveled) {
	this.isUnraveled = isUnraveled;
    }

    public String getCorrectAnswer() {
	return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
	this.correctAnswer = correctAnswer;
    }

    public int getUserMark() {
	return userMark;
    }

    public void setUserMark(int userMark) {
	this.userMark = userMark;
    }

    public int getUserAttempts() {
	return userAttempts;
    }

    public void setUserAttempts(int userAttempts) {
	this.userAttempts = userAttempts;
    }

    public String getFirstChoiceAnswerLetter() {
	return firstChoiceAnswerLetter;
    }

    public void setFirstChoiceAnswerLetter(String firstChoiceAnswerLetter) {
	this.firstChoiceAnswerLetter = firstChoiceAnswerLetter;
    }

    public String getBurningQuestion() {
	return burningQuestion;
    }

    public void setBurningQuestion(String burningQuestion) {
	this.burningQuestion = burningQuestion;
    }
}

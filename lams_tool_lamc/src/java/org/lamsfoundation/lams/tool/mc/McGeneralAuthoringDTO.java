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


package org.lamsfoundation.lams.tool.mc;

/**
 * DTO that holds authoring properties for authoring jsps
 *
 * @author Ozgur Demirtas
 */
public class McGeneralAuthoringDTO implements Comparable {

    protected String usernameVisible;
    protected String reflect;
    protected String questionsSequenced;
    protected String randomize;
    protected String displayAnswers;
    protected String reflectionSubject;
    protected String showMarks;

    protected String editableQuestionText;
    protected String editableQuestionFeedback;
    protected String sln;
    protected String retries;

    protected String markValue;
    protected String passMarkValue;

    @Override
    public int compareTo(Object o) {
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = (McGeneralAuthoringDTO) o;

	if (mcGeneralAuthoringDTO == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    /**
     * @return Returns the questionsSequenced.
     */
    public String getQuestionsSequenced() {
	return questionsSequenced;
    }

    /**
     * @param questionsSequenced
     *            The questionsSequenced to set.
     */
    public void setQuestionsSequenced(String questionsSequenced) {
	this.questionsSequenced = questionsSequenced;
    }

    /**
     * @return Returns the usernameVisible.
     */
    public String getUsernameVisible() {
	return usernameVisible;
    }

    /**
     * @param usernameVisible
     *            The usernameVisible to set.
     */
    public void setUsernameVisible(String usernameVisible) {
	this.usernameVisible = usernameVisible;
    }

    /**
     * @return Returns the editableQuestionText.
     */
    public String getEditableQuestionText() {
	return editableQuestionText;
    }

    /**
     * @param editableQuestionText
     *            The editableQuestionText to set.
     */
    public void setEditableQuestionText(String editableQuestionText) {
	this.editableQuestionText = editableQuestionText;
    }

    /**
     * @return Returns the editableQuestionFeedback.
     */
    public String getEditableQuestionFeedback() {
	return editableQuestionFeedback;
    }

    /**
     * @param editableQuestionFeedback
     *            The editableQuestionFeedback to set.
     */
    public void setEditableQuestionFeedback(String editableQuestionFeedback) {
	this.editableQuestionFeedback = editableQuestionFeedback;
    }

    /**
     * @return Returns the reflect.
     */
    public String getReflect() {
	return reflect;
    }

    /**
     * @param reflect
     *            The reflect to set.
     */
    public void setReflect(String reflect) {
	this.reflect = reflect;
    }

    /**
     * @return Returns the reflectionSubject.
     */
    public String getReflectionSubject() {
	return reflectionSubject;
    }

    /**
     * @param reflectionSubject
     *            The reflectionSubject to set.
     */
    public void setReflectionSubject(String reflectionSubject) {
	this.reflectionSubject = reflectionSubject;
    }

    /**
     * @return Returns the sln.
     */
    public String getSln() {
	return sln;
    }

    /**
     * @param sln
     *            The sln to set.
     */
    public void setSln(String sln) {
	this.sln = sln;
    }

    /**
     * @return Returns the retries.
     */
    public String getRetries() {
	return retries;
    }

    /**
     * @param retries
     *            The retries to set.
     */
    public void setRetries(String retries) {
	this.retries = retries;
    }

    /**
     * @return Returns the markValue.
     */
    public String getMarkValue() {
	return markValue;
    }

    /**
     * @param markValue
     *            The markValue to set.
     */
    public void setMarkValue(String markValue) {
	this.markValue = markValue;
    }

    /**
     * @return Returns the passMarkValue.
     */
    public String getPassMarkValue() {
	return passMarkValue;
    }

    /**
     * @param passMarkValue
     *            The passMarkValue to set.
     */
    public void setPassMarkValue(String passMarkValue) {
	this.passMarkValue = passMarkValue;
    }

    /**
     * @return Returns the showMarks.
     */
    public String getShowMarks() {
	return showMarks;
    }

    /**
     * @param showMarks
     *            The showMarks to set.
     */
    public void setShowMarks(String showMarks) {
	this.showMarks = showMarks;
    }

    /**
     * @return Returns the randomize.
     */
    public String getRandomize() {
	return randomize;
    }

    /**
     * @param randomize
     *            The randomize to set.
     */
    public void setRandomize(String randomize) {
	this.randomize = randomize;
    }

    /**
     * @return Returns the displayAnswers.
     */
    public String getDisplayAnswers() {
	return displayAnswers;
    }

    /**
     * @param randomize
     *            The randomize to set.
     */
    public void setDisplayAnswers(String displayAnswers) {
	this.displayAnswers = displayAnswers;
    }
}

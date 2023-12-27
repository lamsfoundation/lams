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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.web.form;

/**
 * ActionForm for the Authoring environment
 * 
 * @author Ozgur Demirtas
 */
public class McAuthoringForm extends McLearningForm  {
    /* form controllers */

    protected String optionIndex;

    protected String showMarks;
    protected String useSelectLeaderToolOuput;
    protected String prefixAnswersWithLetters;
    protected String randomize;
    protected String displayAnswersFeedback;

    protected String submitAllContent;

    protected String submit;

    /* basic content */
    protected String title;
    protected String instructions;

    /* advanced content */
    protected String showFeedback;
    protected String retries;
    protected String sln;

    protected String passmark;
    
    protected boolean enableConfidenceLevels;

    protected String questionsSequenced;

    protected String edit;
    private String feedback;
    private String candidateIndex;

    /**
     * @return Returns the submitAllContent.
     */
    public String getSubmitAllContent() {
	return submitAllContent;
    }

    /**
     * @param submitAllContent
     *            The submitAllContent to set.
     */
    public void setSubmitAllContent(String submitAllContent) {
	this.submitAllContent = submitAllContent;
    }

    /**
     * @return Returns the instructions.
     */
    public String getInstructions() {
	return instructions;
    }

    /**
     * @param instructions
     *            The instructions to set.
     */
    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
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
     * @return Returns the edit.
     */
    public String getEdit() {
	return edit;
    }

    /**
     * @param edit
     *            The edit to set.
     */
    public void setEdit(String edit) {
	this.edit = edit;
    }

    /**
     * @return Returns the optionIndex.
     */
    public String getOptionIndex() {
	return optionIndex;
    }

    /**
     * @param optionIndex
     *            The optionIndex to set.
     */
    public void setOptionIndex(String optionIndex) {
	this.optionIndex = optionIndex;
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
     * @return Returns the showFeedback.
     */
    public String getShowFeedback() {
	return showFeedback;
    }

    /**
     * @param showFeedback
     *            The showFeedback to set.
     */
    public void setShowFeedback(String showFeedback) {
	this.showFeedback = showFeedback;
    }

    /**
     * @return Returns the passmark.
     */
    public String getPassmark() {
	return passmark;
    }

    /**
     * @param passmark
     *            The passmark to set.
     */
    public void setPassmark(String passmark) {
	this.passmark = passmark;
    }
    
    public boolean isEnableConfidenceLevels() {
 	return enableConfidenceLevels;
    }

    public void setEnableConfidenceLevels(boolean enableConfidenceLevels) {
 	this.enableConfidenceLevels = enableConfidenceLevels;
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
     * @return Returns the submit.
     */
    public String getSubmit() {
	return submit;
    }

    /**
     * @param submit
     *            The submit to set.
     */
    public void setSubmit(String submit) {
	this.submit = submit;
    }

    /**
     * @return Returns the feedback.
     */
    public String getFeedback() {
	return feedback;
    }

    /**
     * @param feedback
     *            The feedback to set.
     */
    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    /**
     * @return Returns the candidateIndex.
     */
    public String getCandidateIndex() {
	return candidateIndex;
    }

    /**
     * @param candidateIndex
     *            The candidateIndex to set.
     */
    public void setCandidateIndex(String candidateIndex) {
	this.candidateIndex = candidateIndex;
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
     * @return Returns the useSelectLeaderToolOuput.
     */
    public String getUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    /**
     * @param useSelectLeaderToolOuput
     *            The useSelectLeaderToolOuput to set.
     */
    public void setUseSelectLeaderToolOuput(String useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    /**
     * @return Returns the prefixAnswersWithLetters.
     */
    public String getPrefixAnswersWithLetters() {
	return prefixAnswersWithLetters;
    }

    /**
     * @param prefixAnswersWithLetters
     *            The prefixAnswersWithLetters to set.
     */
    public void setPrefixAnswersWithLetters(String prefixAnswersWithLetters) {
	this.prefixAnswersWithLetters = prefixAnswersWithLetters;
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
    public String getDisplayAnswersFeedback() {
	return displayAnswersFeedback;
    }

    /**
     * @param displayAnswers
     *            The displayAnswers to set.
     */
    public void setDisplayAnswersFeedback(String displayAnswersFeedback) {
	this.displayAnswersFeedback = displayAnswersFeedback;
    }

}

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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.mc.McAppConstants;

/**
 * @author Ozgur Demirtas
 *
 * ActionForm for the Learning environment
 */
public class McLearningForm extends ActionForm implements McAppConstants {

    protected String continueOptions;
	protected String nextOptions;
	protected String continueOptionsCombined;
	protected String redoQuestions;
	protected String viewSummary;
	protected String viewAnswers;
	protected String learnerFinished;
	protected String redoQuestionsOk;
	protected String donePreview;
	protected String doneLearnerProgress;
	
	
	protected String[] checkedCa;
	protected String toolContentId;
	protected String toolContentUID;
	protected String toolSessionId;
	protected String learningMode;
	protected String currentQuestionIndex;
	
	protected String userOverPassMark;
	protected String passMarkApplicable;
	
	
	public void resetCommands()
	{
		this.setContinueOptions(null);
		this.setNextOptions(null);
		this.setContinueOptionsCombined(null);
		this.setRedoQuestions( null); 
		this.setViewSummary(null);
		this.setViewAnswers(null);
		this.setRedoQuestionsOk(null);
		this.setLearnerFinished(null);
		this.setDonePreview(null);
		this.setDoneLearnerProgress(null);
	}

	public void resetParameters()
	{
	}


	/**
	 * @return Returns the continueOptions.
	 */
	public String getContinueOptions() {
		return continueOptions;
	}
	/**
	 * @param continueOptions The continueOptions to set.
	 */
	public void setContinueOptions(String continueOptions) {
		this.continueOptions = continueOptions;
	}

	
	/**
	 * @return Returns the viewSummary.
	 */
	public String getViewSummary() {
		return viewSummary;
	}
	/**
	 * @param viewSummary The viewSummary to set.
	 */
	public void setViewSummary(String viewSummary) {
		this.viewSummary = viewSummary;
	}
	/**
	 * @return Returns the continueOptionsCombined.
	 */
	public String getContinueOptionsCombined() {
		return continueOptionsCombined;
	}
	/**
	 * @param continueOptionsCombined The continueOptionsCombined to set.
	 */
	public void setContinueOptionsCombined(String continueOptionsCombined) {
		this.continueOptionsCombined = continueOptionsCombined;
	}
	/**
	 * @return Returns the redoQuestions.
	 */
	public String getRedoQuestions() {
		return redoQuestions;
	}
	/**
	 * @param redoQuestions The redoQuestions to set.
	 */
	public void setRedoQuestions(String redoQuestions) {
		this.redoQuestions = redoQuestions;
	}
	/**
	 * @return Returns the viewAnswers.
	 */
	public String getViewAnswers() {
		return viewAnswers;
	}
	/**
	 * @param viewAnswers The viewAnswers to set.
	 */
	public void setViewAnswers(String viewAnswers) {
		this.viewAnswers = viewAnswers;
	}
	/**
	 * @return Returns the redoQuestionsOk.
	 */
	public String getRedoQuestionsOk() {
		return redoQuestionsOk;
	}
	/**
	 * @param redoQuestionsOk The redoQuestionsOk to set.
	 */
	public void setRedoQuestionsOk(String redoQuestionsOk) {
		this.redoQuestionsOk = redoQuestionsOk;
	}
	/**
	 * @return Returns the nextOptions.
	 */
	public String getNextOptions() {
		return nextOptions;
	}
	/**
	 * @param nextOptions The nextOptions to set.
	 */
	public void setNextOptions(String nextOptions) {
		this.nextOptions = nextOptions;
	}
	/**
	 * @return Returns the learnerFinished.
	 */
	public String getLearnerFinished() {
		return learnerFinished;
	}
	/**
	 * @param learnerFinished The learnerFinished to set.
	 */
	public void setLearnerFinished(String learnerFinished) {
		this.learnerFinished = learnerFinished;
	}
	/**
	 * @return Returns the donePreview.
	 */
	public String getDonePreview() {
		return donePreview;
	}
	/**
	 * @param donePreview The donePreview to set.
	 */
	public void setDonePreview(String donePreview) {
		this.donePreview = donePreview;
	}
	/**
	 * @return Returns the doneLearnerProgress.
	 */
	public String getDoneLearnerProgress() {
		return doneLearnerProgress;
	}
	/**
	 * @param doneLearnerProgress The doneLearnerProgress to set.
	 */
	public void setDoneLearnerProgress(String doneLearnerProgress) {
		this.doneLearnerProgress = doneLearnerProgress;
	}
    /**
     * @return Returns the userOverPassMark.
     */
    public String getUserOverPassMark() {
        return userOverPassMark;
    }
    /**
     * @param userOverPassMark The userOverPassMark to set.
     */
    public void setUserOverPassMark(String userOverPassMark) {
        this.userOverPassMark = userOverPassMark;
    }
    /**
     * @return Returns the passMarkApplicable.
     */
    public String getPassMarkApplicable() {
        return passMarkApplicable;
    }
    /**
     * @param passMarkApplicable The passMarkApplicable to set.
     */
    public void setPassMarkApplicable(String passMarkApplicable) {
        this.passMarkApplicable = passMarkApplicable;
    }
    /**
     * @return Returns the checkedCa.
     */
    public String[] getCheckedCa() {
        return checkedCa;
    }
    /**
     * @param checkedCa The checkedCa to set.
     */
    public void setCheckedCa(String[] checkedCa) {
        this.checkedCa = checkedCa;
    }

    /**
     * @return Returns the toolContentId.
     */
    public String getToolContentId() {
        return toolContentId;
    }
    /**
     * @param toolContentId The toolContentId to set.
     */
    public void setToolContentId(String toolContentId) {
        this.toolContentId = toolContentId;
    }
    /**
     * @return Returns the toolSessionId.
     */
    public String getToolSessionId() {
        return toolSessionId;
    }
    /**
     * @param toolSessionId The toolSessionId to set.
     */
    public void setToolSessionId(String toolSessionId) {
        this.toolSessionId = toolSessionId;
    }

    /**
     * @return Returns the learningMode.
     */
    public String getLearningMode() {
        return learningMode;
    }
    /**
     * @param learningMode The learningMode to set.
     */
    public void setLearningMode(String learningMode) {
        this.learningMode = learningMode;
    }
    
    /**
     * @return Returns the toolContentUID.
     */
    public String getToolContentUID() {
        return toolContentUID;
    }
    /**
     * @param toolContentUID The toolContentUID to set.
     */
    public void setToolContentUID(String toolContentUID) {
        this.toolContentUID = toolContentUID;
    }

    /**
     * @return Returns the currentQuestionIndex.
     */
    public String getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    /**
     * @param currentQuestionIndex The currentQuestionIndex to set.
     */
    public void setCurrentQuestionIndex(String currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }
    
}

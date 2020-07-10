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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.assessment.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Import filter class for different version of Assessment content.
 */
public class AssessmentImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 20140101 version content to 20140102 version tool server.
     */
    public void up20140101To20140102() {
	this.removeField(Assessment.class, "runOffline");
	this.removeField(Assessment.class, "onlineInstructions");
	this.removeField(Assessment.class, "offlineInstructions");
	this.removeField(Assessment.class, "attachments");
    }

    /**
     * Import 20140102 version content to 20140428 version tool server.
     */
    public void up20140102To20140428() {
	this.removeField(Assessment.class, "contentInUse");
    }

    /**
     * Import 20140428 version content to 20140707 version tool server.
     */
    public void up20140428To20140707() {
	this.renameField(AssessmentQuestion.class, "questionOptions", "options");
	this.addField(AssessmentQuestion.class, "answerRequired", "true");
    }

    /**
     * Import 20140428 version content to 20140707 version tool server.
     */
    public void up20170101To20170315() {
	this.removeField(AssessmentQuestion.class, "createDate");
	this.removeField(AssessmentQuestion.class, "createBy");
	this.removeField(AssessmentQuestion.class, "answer");
	this.removeField(AssessmentQuestion.class, "answerFloat");
	this.removeField(AssessmentQuestion.class, "answerBoolean");
	this.removeField(AssessmentQuestion.class, "questionFeedback");
	this.removeField(AssessmentQuestion.class, "responseSubmitted");
	this.removeField(AssessmentQuestion.class, "grade");
	this.removeField(AssessmentQuestion.class, "mark");
	this.removeField(AssessmentQuestion.class, "penalty");
	this.removeField(AssessmentQuestion.class, "answerTotalGrade");

//	this.removeField(AssessmentQuestionOption.class, "answerInt");
//	this.removeField(AssessmentQuestionOption.class, "answerBoolean");
    }

    /**
     * Import 20140428 version content to 20140707 version tool server.
     */
    public void up20170315To20190110() {
	this.renameField(QuestionReference.class, "defaultGrade", "maxMark");
	this.renameField(QuestionReference.class, "sequenceId", "displayOrder");
    }

    /**
     * Import 20190110 version content to 20190517 version tool server.
     */
    public void up20190110To20190517() {
	this.removeField(AssessmentQuestion.class, "questionHash");
    }

    public void up20190517To20190704() {
	this.removeField(QuestionReference.class, "title");
	this.removeField(QuestionReference.class, "type");
    }

    /**
     * Migration to Question Bank
     */
    public void up20190704To20190809(String toolFilePath) throws IOException {
	//perform all transformations from the previous methods. Do it now as long as the following commands expect Assesment model to be in its latest state
	transformXML(toolFilePath);

	// find LD's content folder ID to use it in new QB questions
	String contentFolderId = null;
	try {
	    File ldFile = new File(new File(toolFilePath).getParentFile().getParentFile(), "learning_design.xml");
	    DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = docBuilder.parse(new FileInputStream(ldFile));
	    Element ldRoot = doc.getDocumentElement();
	    contentFolderId = XMLUtil.getChildElementValue(ldRoot, "contentFolderID", null);
	} catch (Exception e) {
	    throw new IOException("Error while extracting LD content folder ID for Question Bank migration", e);
	}
	final String contentFolderIdFinal = contentFolderId;

	// tell which file to process and what to do with its root element
	transformXML(toolFilePath, toolRoot -> {
	    Document document = toolRoot.getOwnerDocument();

	    // first find questions
	    NodeList assessmentQuestions = toolRoot
		    .getElementsByTagName("org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion");
	    if (assessmentQuestions.getLength() == 0) {
		return;
	    }

	    String defaultCreateDate = new SimpleDateFormat(DateUtil.EXPORT_LD_FORMAT).format(new Date());

	    // go through each question
	    for (int assessmentQuestionIndex = 0; assessmentQuestionIndex < assessmentQuestions
		    .getLength(); assessmentQuestionIndex++) {
		Element assessmentQuestion = (Element) assessmentQuestions.item(assessmentQuestionIndex);
		// create an element for QbQuestion
		Element qbQuestion = document.createElement("qbQuestion");
		assessmentQuestion.appendChild(qbQuestion);

		// transform Assessment data into QB structure
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "type", "type", null, false, true);
		// Question ID will be filled later as it requires QbService
		XMLUtil.addTextElement(qbQuestion, "version", "1");
		XMLUtil.addTextElement(qbQuestion, "contentFolderId", contentFolderIdFinal);
		XMLUtil.rewriteTextElement(toolRoot, qbQuestion, "created", "createDate", defaultCreateDate, true,
			false);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "title", "name", null, false, true,
			QbUtils.QB_MIGRATION_CKEDITOR_CLEANER, QbUtils.QB_MIGRATION_TAG_CLEANER);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "question", "description", null, false, true,
			QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "defaultGrade", "maxMark", "1", false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "generalFeedback", "feedback", null, false,
			true, QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		XMLUtil.rewriteTextElement(assessmentQuestion, assessmentQuestion, "sequenceId", "displayOrder", null,
			false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "penaltyFactor", "penaltyFactor", "0", false,
			true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "answerRequired", "answerRequired", "false",
			false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "multipleAnswersAllowed",
			"multipleAnswersAllowed", "false", false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "incorrectAnswerNullifiesMark",
			"incorrectAnswerNullifiesMark", "false", false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "feedbackOnCorrect", "feedbackOnCorrect",
			null, false, true, QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "feedbackOnPartiallyCorrect",
			"feedbackOnPartiallyCorrect", null, false, true, QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "feedbackOnIncorrect", "feedbackOnIncorrect",
			null, false, true, QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "shuffle", "shuffle", "false", false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "prefixAnswersWithLetters",
			"prefixAnswersWithLetters", "false", false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "caseSensitive", "caseSensitive", "false",
			false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "autocompleteEnabled", "autocompleteEnabled",
			"false", false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "correctAnswer", "correctAnswer", "false",
			false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "allowRichEditor", "allowRichEditor",
			"false", false, true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "maxWordsLimit", "maxWordsLimit", "0", false,
			true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "minWordsLimit", "minWordsLimit", "0", false,
			true);
		XMLUtil.rewriteTextElement(assessmentQuestion, qbQuestion, "hedgingJustificationEnabled",
			"hedgingJustificationEnabled", "false", false, true);

		// get rid of junk
		XMLUtil.removeElement(assessmentQuestion, "questionHash");

		// now it's time for options
		NodeList assessmentOptions = assessmentQuestion
			.getElementsByTagName("org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption");
		if (assessmentOptions.getLength() == 0) {
		    continue;
		}

		boolean shiftOptionsDisplayOrder = false;
		List<Element> assessmentOptionsSequenceIds = XMLUtil
			.findChildren((Element) assessmentOptions.item(0).getParentNode(), "sequenceId");
		for (Element sequenceIdElement : assessmentOptionsSequenceIds) {
		    int sequenceId = Integer.parseInt(sequenceIdElement.getTextContent());
		    if (sequenceId == 0) {
			shiftOptionsDisplayOrder = true;
			break;
		    }
		}

		Element qbOptions = document.createElement("qbOptions");
		qbQuestion.appendChild(qbOptions);
		int maxDisplayOrder = 0;
		for (int assessmentOptionIndex = 0; assessmentOptionIndex < assessmentOptions
			.getLength(); assessmentOptionIndex++) {
		    Element assessmentOption = (Element) assessmentOptions.item(assessmentOptionIndex);
		    Element qbOption = document.createElement("org.lamsfoundation.lams.qb.model.QbOption");
		    qbOptions.appendChild(qbOption);

		    String sequenceIdString = XMLUtil.getChildElementValue(assessmentOption, "sequenceId", null);
		    Integer sequenceId = sequenceIdString == null ? null : Integer.valueOf(sequenceIdString);
		    if (sequenceId == null) {
			sequenceId = ++maxDisplayOrder;
		    }
		    if (shiftOptionsDisplayOrder) {
			sequenceId++;
		    }
		    XMLUtil.rewriteTextElement(assessmentOption, qbOption, "sequenceId", "displayOrder",
			    String.valueOf(sequenceId));
		    String correct = XMLUtil.getChildElementValue(assessmentOption, "correct", "false");
		    if (Boolean.TRUE.toString().equalsIgnoreCase(correct)) {
			XMLUtil.addTextElement(qbOption, "maxMark", "1.0");
		    } else {
			XMLUtil.rewriteTextElement(assessmentOption, qbOption, "grade", "maxMark", "0");
		    }
		    XMLUtil.rewriteTextElement(assessmentOption, qbOption, "optionString", "name", null, false, false,
			    QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		    XMLUtil.rewriteTextElement(assessmentOption, qbOption, "feedback", "feedback", null);
		    XMLUtil.rewriteTextElement(assessmentOption, qbOption, "question", "matchingPair", null, false,
			    false, QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		    XMLUtil.rewriteTextElement(assessmentOption, qbOption, "acceptedError", "acceptedError", null);
		    XMLUtil.rewriteTextElement(assessmentOption, qbOption, "optionFloat", "numericalOption", null);
		}

		// get rid of junk
		assessmentQuestion.removeChild(assessmentOptions.item(0).getParentNode());

		// now rewrite units
		NodeList assessmentUnits = assessmentQuestion
			.getElementsByTagName("org.lamsfoundation.lams.tool.assessment.model.AssessmentUnit");
		if (assessmentUnits.getLength() == 0) {
		    continue;
		}

		Element qbUnits = document.createElement("units");
		qbQuestion.appendChild(qbUnits);

		for (int assessmentUnitIndex = 0; assessmentUnitIndex < assessmentUnits
			.getLength(); assessmentUnitIndex++) {
		    Element assessmentUnit = (Element) assessmentUnits.item(assessmentUnitIndex);
		    Element qbUnit = document.createElement("org.lamsfoundation.lams.qb.model.QbQuestionUnit");
		    qbUnits.appendChild(qbUnit);

		    XMLUtil.rewriteTextElement(assessmentUnit, qbUnit, "sequenceId", "displayOrder", null);
		    XMLUtil.rewriteTextElement(assessmentUnit, qbUnit, "unit", "name", null);
		    XMLUtil.rewriteTextElement(assessmentUnit, qbUnit, "multiplier", "multiplier", null);
		}

		// remove old units section from the legacy assessment question
		assessmentQuestion.removeChild(assessmentUnits.item(0).getParentNode());
	    }
	});
    }

    public void up20191016To20191120() {
	this.addField(Assessment.class, "confidenceLevelsType", "1");
    }

    /**
     * Move "is required" from QB question to tool
     */
    public void up20191120To20200710(String toolFilePath) throws IOException {

	// tell which file to process and what to do with its root element
	transformXML(toolFilePath, toolRoot -> {
	    NodeList assessmentQuestions = toolRoot
		    .getElementsByTagName("org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion");

	    for (int assessmentQuestionIndex = 0; assessmentQuestionIndex < assessmentQuestions
		    .getLength(); assessmentQuestionIndex++) {
		Element assessmentQuestion = (Element) assessmentQuestions.item(assessmentQuestionIndex);
		Element qbQuestion = (Element) assessmentQuestion.getElementsByTagName("qbQuestion").item(0);

		XMLUtil.rewriteTextElement(qbQuestion, assessmentQuestion, "answerRequired", "answerRequired", "true",
			false, true);
	    }
	});
    }
}

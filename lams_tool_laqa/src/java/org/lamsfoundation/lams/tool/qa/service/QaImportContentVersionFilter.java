/***************************************************************************
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
 * ************************************************************************
 */
package org.lamsfoundation.lams.tool.qa.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.tool.qa.model.QaContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Import filter class for different versions of Q&A content.
 */
public class QaImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 2.0RC1 version content to 2.0RC2 version. Added lock on finish field.
     */
    public void up20061102To20061113() {
	// Change name to suit the version you give the tool.
	this.addField(QaContent.class, "lockWhenFinished", "true");
	this.addField(QaContent.class, "showOtherAnswers", "true");
    }

    public void up20081126To20101022() {
	this.removeField(QaQueContent.class, "isOptional");
	this.addField(QaQueContent.class, "required", "false");
    }

    /**
     * Import 20140101 version content to 20140102 version tool server.
     */
    public void up20140101To20140102() {
	this.removeField(QaContent.class, "runOffline");
	this.removeField(QaContent.class, "onlineInstructions");
	this.removeField(QaContent.class, "offlineInstructions");
	this.removeField(QaContent.class, "qaUploadedFiles");
    }

    /**
     * Import 20140102 version content to 20140527 version tool server.
     */
    public void up20140102To20140102() {
	this.removeField(QaContent.class, "contentLocked");
	this.removeField(QaContent.class, "synchInMonitor");
    }

    /**
     * Import 20140822 version content to 20150511 version tool server.
     */
    public void up20140822To20150511() {
	this.addField(QaContent.class, "minimumRates", "0");
	this.addField(QaContent.class, "maximumRates", "0");

	this.addField(QaQueContent.class, "minWordsLimit", "0");
    }

    public void up20170101To20181202() {
	this.renameClass("org.lamsfoundation.lams.tool.qa.QaContent",
		"org.lamsfoundation.lams.tool.qa.model.QaContent");
	this.renameClass("org.lamsfoundation.lams.tool.qa.QaCondition",
		"org.lamsfoundation.lams.tool.qa.model.QaCondition");
	this.renameClass("org.lamsfoundation.lams.tool.qa.QaQueContent",
		"org.lamsfoundation.lams.tool.qa.model.QaQueContent");
	this.renameClass("org.lamsfoundation.lams.tool.qa.QaQueUsr", "org.lamsfoundation.lams.tool.qa.model.QaQueUsr");
	this.renameClass("org.lamsfoundation.lams.tool.qa.QaSession",
		"org.lamsfoundation.lams.tool.qa.model.QaSession");
	this.renameClass("org.lamsfoundation.lams.tool.qa.QaUsrResp",
		"org.lamsfoundation.lams.tool.qa.model.QaUsrResp");
    }

    /**
     * Migration to Question Bank
     */
    public void up20190103To20190809(String toolFilePath) throws IOException {
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
	    NodeList qaQuestions = toolRoot.getElementsByTagName("org.lamsfoundation.lams.tool.qa.model.QaQueContent");
	    if (qaQuestions.getLength() == 0) {
		return;
	    }

	    // comparator class got renamed, we need to adjust XML
	    NodeList comparators = toolRoot.getElementsByTagName("comparator");
	    for (int comparatorIndex = 0; comparatorIndex < comparators.getLength(); comparatorIndex++) {
		Element comparator = (Element) comparators.item(comparatorIndex);
		String classAttribute = comparator.getAttribute("class");
		if ("org.lamsfoundation.lams.tool.qa.util.QaQueContentComparator".equals(classAttribute)) {
		    comparator.setAttribute("class", "org.lamsfoundation.lams.tool.qa.util.QaQuestionComparator");
		}
	    }

	    // go through each question
	    for (int qaQuestionIndex = 0; qaQuestionIndex < qaQuestions.getLength(); qaQuestionIndex++) {
		Element qaQuestion = (Element) qaQuestions.item(qaQuestionIndex);
		// create an element for QbQuestion
		Element qbQuestion = document.createElement("qbQuestion");
		qaQuestion.appendChild(qbQuestion);

		// transform Q&A data into QB structure
		XMLUtil.addTextElement(qbQuestion, "type", "6");
		// Question ID will be filled later as it requires QbService
		XMLUtil.addTextElement(qbQuestion, "version", "1");
		XMLUtil.addTextElement(qbQuestion, "contentFolderId", contentFolderIdFinal);
		// get the date from Q&A activity, keep attributes
		XMLUtil.rewriteTextElement(toolRoot, qbQuestion, "updateDate", "createDate", null, true, false);
		XMLUtil.rewriteTextElement(qaQuestion, qbQuestion, "question", "name", null, false, true,
			QbUtils.QB_MIGRATION_CKEDITOR_CLEANER, QbUtils.QB_MIGRATION_TAG_CLEANER);
		XMLUtil.rewriteTextElement(qaQuestion, qbQuestion, "feedback", "feedback", null, false, true,
			QbUtils.QB_MIGRATION_TRIMMER);
		XMLUtil.rewriteTextElement(qaQuestion, qbQuestion, "required", "answerRequired", "false", false, true);
		XMLUtil.rewriteTextElement(qaQuestion, qbQuestion, "minWordsLimit", "minWordsLimit", "0", false, true);
	    }
	});
    }

    /**
     * Move "is required" from QB question to tool
     */
    public void up20190809To20200710(String toolFilePath) throws IOException {

	// tell which file to process and what to do with its root element
	transformXML(toolFilePath, toolRoot -> {
	    NodeList qaQuestions = toolRoot.getElementsByTagName("org.lamsfoundation.lams.tool.qa.model.QaQueContent");

	    // go through each question
	    for (int qaQuestionIndex = 0; qaQuestionIndex < qaQuestions.getLength(); qaQuestionIndex++) {
		Element qaQuestion = (Element) qaQuestions.item(qaQuestionIndex);
		Element qbQuestion = (Element) qaQuestion.getElementsByTagName("qbQuestion").item(0);
		XMLUtil.rewriteTextElement(qbQuestion, qaQuestion, "answerRequired", "answerRequired", "true", false,
			true);
	    }
	});
    }
}
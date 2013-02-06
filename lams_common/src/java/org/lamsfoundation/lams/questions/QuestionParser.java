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

/* $Id$ */
package org.lamsfoundation.lams.questions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Extract questions and answers from various formats. They can be later used in question-based tools. Currently it
 * supports only IMS QTI but other methods can be added as needed.
 * 
 * @author Marcin Cieslak
 * 
 */
public class QuestionParser {
    private static Logger log = Logger.getLogger(QuestionParser.class);

    // just for convenience when returning from methods
    private static final Question[] QUESTION_ARRAY_TYPE = new Question[] {};
    // can be anything
    private static final String TEMP_PACKAGE_NAME_PREFIX = "QTI_PACKAGE_";

    /**
     * Extracts questions from IMS QTI zip file.
     */
    public static Question[] parseQTIPackage(InputStream packageFileStream) throws SAXParseException, IOException,
	    SAXException, ParserConfigurationException, ZipFileUtilException {
	List<Question> result = new ArrayList<Question>();

	// unique folder name
	String tempPackageName = QuestionParser.TEMP_PACKAGE_NAME_PREFIX + System.currentTimeMillis();
	String tempPackageDirPath = ZipFileUtil.expandZip(packageFileStream, tempPackageName);
	try {
	    List<File> resourceFiles = QuestionParser.getQTIResourceFiles(tempPackageDirPath);
	    if (resourceFiles.isEmpty()) {
		QuestionParser.log.warn("No resource files found in QTI package");
	    } else {
		// extract from every XML file; usually there is just one
		for (File resourceFile : resourceFiles) {
		    Question[] fileQuestions = QuestionParser.parseQTIFile(resourceFile);
		    if (fileQuestions != null) {
			Collections.addAll(result, fileQuestions);
		    }
		}
	    }
	} finally {
	    // clean up
	    ZipFileUtil.deleteDirectory(tempPackageDirPath);
	}

	return result.toArray(QuestionParser.QUESTION_ARRAY_TYPE);
    }

    /**
     * Extracts questions from IMS QTI xml file.
     */
    public static Question[] parseQTIFile(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
	List<Question> result = new ArrayList<Question>();
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = docBuilder.parse(xmlFile);

	NodeList questionItems = doc.getElementsByTagName("item");
	// yes, a label here for convenience
	questionLoop: for (int questionItemIndex = 0; questionItemIndex < questionItems.getLength(); questionItemIndex++) {
	    Element questionItem = (Element) questionItems.item(questionItemIndex);
	    NodeList questionItemTypes = questionItem.getElementsByTagName("qmd_itemtype");
	    String questionItemType = questionItemTypes.getLength() > 0 ? ((Text) questionItemTypes.item(0)
		    .getChildNodes().item(0)).getData() : null;
	    if ("Matching".equalsIgnoreCase(questionItemType)) {
		// do not support matching question type
		continue;
	    }

	    Question question = new Question();
	    Map<String, Answer> answerMap = new TreeMap<String, Answer>();

	    Element presentation = (Element) questionItem.getElementsByTagName("presentation").item(0);
	    NodeList presentationChildrenList = presentation.getChildNodes();
	    // cumberstone parsing, but there is no other way using this API
	    for (int presentationChildIndex = 0; presentationChildIndex < presentationChildrenList.getLength(); presentationChildIndex++) {
		Node presentationChild = presentationChildrenList.item(presentationChildIndex);
		// here is where question data is stored
		if ("material".equals(presentationChild.getNodeName())) {
		    Element questionElement = (Element) ((Element) presentationChild).getElementsByTagName("mattext")
			    .item(0);
		    String questionText = ((CDATASection) questionElement.getChildNodes().item(0)).getData();
		    if (questionText.trim().startsWith("<!--")) {
			// do not support algorithmic question type
			continue questionLoop;
		    }
		    question.setText(questionText);
		} else if ("response_lid".equals(presentationChild.getNodeName())) {
		    // parse answers
		    NodeList answerList = ((Element) presentationChild).getElementsByTagName("response_label");
		    for (int answerIndex = 0; answerIndex < answerList.getLength(); answerIndex++) {
			Element answerElement = (Element) answerList.item(answerIndex);
			Element textElement = (Element) answerElement.getElementsByTagName("mattext").item(0);
			String answerText = ((CDATASection) textElement.getChildNodes().item(0)).getData();
			Answer answer = new Answer();
			answer.setText(answerText);
			if (question.getAnswers() == null) {
			    question.setAnswers(new ArrayList<Answer>());
			}
			question.getAnswers().add(answer);

			// for other metadata parts to find Answer object
			String answerId = answerElement.getAttribute("ident");
			answerMap.put(answerId, answer);
		    }
		}
	    }

	    // extract score and feedback
	    Map<String, String> feedbackMap = new TreeMap<String, String>();
	    if (!answerMap.isEmpty()) {
		NodeList answerMetadatas = questionItem.getElementsByTagName("respcondition");
		for (int answerMetadataIndex = 0; answerMetadataIndex < answerMetadatas.getLength(); answerMetadataIndex++) {
		    Element answerMetadata = (Element) answerMetadatas.item(answerMetadataIndex);
		    NodeList scoreReference = answerMetadata.getElementsByTagName("varequal");
		    // find where given metadata part references to
		    String answerId = scoreReference.getLength() > 0 ? ((Text) scoreReference.item(0).getChildNodes()
			    .item(0)).getData() : null;

		    if (answerId != null) {
			// extract score
			String score = ((Text) answerMetadata.getElementsByTagName("setvar").item(0).getChildNodes()
				.item(0)).getData();
			if (!StringUtils.isBlank(score)) {
			    Answer answer = answerMap.get(answerId);
			    answer.setScore(Float.parseFloat(score));
			}

			// extract question and answer feedback references
			NodeList feedbacks = answerMetadata.getElementsByTagName("displayfeedback");
			for (int feedbackIndex = 0; feedbackIndex < feedbacks.getLength(); feedbackIndex++) {
			    Element feedbackElement = (Element) feedbacks.item(feedbackIndex);
			    String feedbackId = feedbackElement.getAttribute("linkrefid");
			    // check if it is not a question feedback
			    if (!feedbackId.endsWith("_ALL")) {
				feedbackMap.put(feedbackId, answerId);
			    }
			}
		    }
		}
	    }

	    // extract question and answer feedbacks
	    NodeList feedbacks = questionItem.getElementsByTagName("itemfeedback");
	    for (int feedbackIndex = 0; feedbackIndex < feedbacks.getLength(); feedbackIndex++) {
		Element feedbackElement = (Element) feedbacks.item(feedbackIndex);
		String feedbackText = ((CDATASection) feedbackElement.getElementsByTagName("mattext").item(0)
			.getChildNodes().item(0)).getData();
		if (!StringUtils.isBlank(feedbackText)) {
		    String feedbackType = feedbackElement.getAttribute("view");
		    // it is a question feedback
		    if ("All".equalsIgnoreCase(feedbackType)) {
			question.setFeedback(feedbackText);
		    } else {
			// it is an answer feedback
			String feedbackId = feedbackElement.getAttribute("ident");
			String answerId = feedbackMap.get(feedbackId);
			if (answerId != null) {
			    Answer answer = answerMap.get(answerId);
			    answer.setFeedback(feedbackText);
			}
		    }
		}
	    }

	    result.add(question);
	}

	return result.toArray(QuestionParser.QUESTION_ARRAY_TYPE);
    }

    /**
     * Parses query string (send by form submit or extracted otherwise) from questionChoice.jsp form.
     */
    public static Question[] parseQuestionChoiceForm(String queryString) throws UnsupportedEncodingException {
	List<Question> result = new ArrayList<Question>();

	// are answers needed at all?
	boolean chooseAnswers = Boolean.parseBoolean(WebUtil.extractParameterValue(queryString, "chooseAnswers"));
	// to know where to stop searching for question entries (disabled/unchecked form fields are not sent)
	int questionCount = Integer.parseInt(WebUtil.extractParameterValue(queryString, "questionCount"));
	for (int questionIndex = 0; questionIndex < questionCount; questionIndex++) {
	    String questionText = WebUtil.extractParameterValue(queryString, "question" + questionIndex);
	    if (!StringUtils.isBlank(questionText)) {
		Question question = new Question();
		question.setText(URLDecoder.decode(questionText, "UTF8"));
		String questionFeedback = WebUtil.extractParameterValue(queryString, "question" + questionIndex
			+ "feedback");
		// can be blank
		question.setFeedback(URLDecoder.decode(questionFeedback, "UTF8"));
		if (chooseAnswers) {
		    String answerCountParam = WebUtil.extractParameterValue(queryString, "answerCount" + questionIndex);
		    int answerCount = answerCountParam == null ? 0 : Integer.parseInt(answerCountParam);
		    if (answerCount > 0) {
			question.setAnswers(new ArrayList<Answer>());
			for (int answerIndex = 0; answerIndex < answerCount; answerIndex++) {
			    String answerId = "question" + questionIndex + "answer" + answerIndex;
			    String answerText = WebUtil.extractParameterValue(queryString, answerId);
			    String answerScore = WebUtil.extractParameterValue(queryString, answerId + "score");
			    if (!StringUtils.isBlank(answerText)) {
				Answer answer = new Answer();
				answer.setText(URLDecoder.decode(answerText, "UTF8"));
				if (!StringUtils.isBlank(answerScore)) {
				    answer.setScore(Float.parseFloat(answerScore));
				}
				
				String answerFeedback = WebUtil.extractParameterValue(queryString, answerId
					+ "feedback");
				// can be blank
				answer.setFeedback(URLDecoder.decode(answerFeedback, "UTF8"));
				
				question.getAnswers().add(answer);
			    }
			}
		    }
		}

		result.add(question);
	    }
	}

	return result.toArray(QuestionParser.QUESTION_ARRAY_TYPE);
    }

    /**
     * Find XML file list in IMS QTI package.
     */
    private static List<File> getQTIResourceFiles(String packageDirPath) throws IOException,
	    ParserConfigurationException, SAXException {
	InputStream inputStream = new FileInputStream(new File(packageDirPath, "imsmanifest.xml"));
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document manifest = docBuilder.parse(inputStream);
	NodeList resourcesList = manifest.getElementsByTagName("resources");
	List<File> resourceFiles = new LinkedList<File>();
	if (resourcesList.getLength() > 0) {
	    Element resources = (Element) resourcesList.item(0);
	    NodeList resourceList = resources.getElementsByTagName("resource");

	    for (int resourceIndex = 0; resourceIndex < resourceList.getLength(); resourceIndex++) {
		Element resource = (Element) resourceList.item(resourceIndex);
		String resourceFileName = resource.getAttribute("href");
		if (resourceFileName.endsWith(".xml")) {
		    File resourceFile = new File(packageDirPath, resourceFileName);
		    if (!resourceFile.isFile() || !resourceFile.canRead()) {
			QuestionParser.log.warn("XML resource file specified in IMS manifest can not be read: "
				+ resourceFileName);
		    } else {
			resourceFiles.add(resourceFile);
		    }
		}
	    }
	}
	return resourceFiles;
    }
}
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
import java.util.Set;
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
    public static Question[] parseQTIPackage(InputStream packageFileStream, Set<String> limitType)
	    throws SAXParseException, IOException, SAXException, ParserConfigurationException, ZipFileUtilException {
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
		    Question[] fileQuestions = QuestionParser.parseQTIFile(resourceFile, limitType);
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
    public static Question[] parseQTIFile(File xmlFile, Set<String> limitType) throws ParserConfigurationException,
	    SAXException, IOException {
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
	    Question question = new Question();
	    // check if it is "matching" question type
	    if ("Matching".equalsIgnoreCase(questionItemType)
		    && !QuestionParser.isQuestionTypeAcceptable(Question.QUESTION_TYPE_MATCHING, limitType, question)) {
		continue;
	    }
	    String questionTitle = questionItem.getAttribute("title");
	    question.setTitle(questionTitle);

	    Map<String, Answer> answerMap = new TreeMap<String, Answer>();
	    Map<String, Answer> matchAnswerMap = null;
	    boolean textBasedQuestion = false;

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
			// do not support Algorithmic question type
			continue questionLoop;
		    }
		    question.setText(WebUtil.removeHTMLtags(questionText));
		} else if ("response_lid".equals(presentationChild.getNodeName())) {
		    if (question.getAnswers() == null) {
			boolean multipleAnswersAllowed = "multiple".equalsIgnoreCase(((Element) presentationChild)
				.getAttribute("rcardinality"));
			NodeList answerList = ((Element) presentationChild).getElementsByTagName("response_label");
			// parse answers
			for (int answerIndex = 0; answerIndex < answerList.getLength(); answerIndex++) {
			    Element answerElement = (Element) answerList.item(answerIndex);
			    Element textElement = (Element) answerElement.getElementsByTagName("mattext").item(0);
			    String answerText = ((CDATASection) textElement.getChildNodes().item(0)).getData();

			    // if there are answers different thatn true/false,
			    // it is Multiple Choice or Multiple Answers
			    if ((question.getType() == null)
				    && !"true".equalsIgnoreCase(answerText)
				    && !"false".equalsIgnoreCase(answerText)
				    && !QuestionParser.isQuestionTypeAcceptable(
					    multipleAnswersAllowed ? Question.QUESTION_TYPE_MULTIPLE_RESPONSE
						    : Question.QUESTION_TYPE_MULTIPLE_CHOICE, limitType, question)) {
				continue questionLoop;
			    }

			    Answer answer = new Answer();
			    answer.setText(WebUtil.removeHTMLtags(answerText));
			    if (question.getAnswers() == null) {
				question.setAnswers(new ArrayList<Answer>());
			    }
			    question.getAnswers().add(answer);

			    // for other metadata parts to find Answer object
			    String answerId = answerElement.getAttribute("ident");
			    answerMap.put(answerId, answer);
			}

			// if there are only true/false answers, set the question type
			if ((question.getType() == null)
				&& (question.getAnswers() != null)
				&& (question.getAnswers().size() == 2)
				&& !multipleAnswersAllowed
				&& !QuestionParser.isQuestionTypeAcceptable(Question.QUESTION_TYPE_TRUE_FALSE,
					limitType, question)) {
			    continue questionLoop;
			}
		    }

		    if (Question.QUESTION_TYPE_MATCHING.equals(question.getType())) {
			if (question.getMatchAnswers() == null) {
			    // the parsed answers are actually the second part of matching
			    // move collections accordingly
			    question.setMatchAnswers(question.getAnswers());
			    question.setAnswers(new ArrayList<Answer>());

			    matchAnswerMap = answerMap;
			    answerMap = new TreeMap<String, Answer>();
			}

			NodeList responseLidChildrenList = presentationChild.getChildNodes();
			for (int responseLidChildIndex = 0; responseLidChildIndex < responseLidChildrenList.getLength(); responseLidChildIndex++) {
			    // parse answers for first part of matching
			    Node responseLidChild = responseLidChildrenList.item(responseLidChildIndex);
			    if ("material".equals(responseLidChild.getNodeName())) {
				Element answerElement = (Element) ((Element) presentationChild).getElementsByTagName(
					"mattext").item(0);
				String answerText = ((CDATASection) answerElement.getChildNodes().item(0)).getData();

				Answer answer = new Answer();
				answer.setText(WebUtil.removeHTMLtags(answerText));
				question.getAnswers().add(answer);

				String answerId = ((Element) presentationChild).getAttribute("ident");
				answerMap.put(answerId, answer);
			    }
			}
		    }
		} else if ("response_str".equals(presentationChild.getNodeName())) {
		    // Essay or Fill-in-Blank question types
		    textBasedQuestion = true;
		}
	    }

	    // extract score and feedback
	    Map<String, String> feedbackMap = new TreeMap<String, String>();
	    if (textBasedQuestion || !answerMap.isEmpty()) {
		NodeList answerMetadatas = questionItem.getElementsByTagName("respcondition");
		for (int answerMetadataIndex = 0; answerMetadataIndex < answerMetadatas.getLength(); answerMetadataIndex++) {
		    Element answerMetadata = (Element) answerMetadatas.item(answerMetadataIndex);
		    NodeList scoreReference = answerMetadata.getElementsByTagName("varequal");
		    // find where given metadata part references to
		    String answerId = scoreReference.getLength() > 0 ? ((Text) scoreReference.item(0).getChildNodes()
			    .item(0)).getData() : null;
		    if (answerId == null) {
			// no answers at all, so it is Essay type
			if ((question.getType() == null)
				&& textBasedQuestion
				&& !QuestionParser.isQuestionTypeAcceptable(Question.QUESTION_TYPE_ESSAY, limitType,
					question)) {
			    continue questionLoop;
			}
		    } else {
			if ((question.getType() == null)
				&& textBasedQuestion
				&& !QuestionParser.isQuestionTypeAcceptable(Question.QUESTION_TYPE_FILL_IN_BLANK,
					limitType, question)) {
			    continue questionLoop;
			}

			Answer answer = null;
			if (textBasedQuestion) {
			    // for Fill-in-Blank answer parsing can be done only here
			    answer = new Answer();
			    answer.setText(WebUtil.removeHTMLtags(answerId));
			    if (question.getAnswers() == null) {
				question.setAnswers(new ArrayList<Answer>());
			    }
			    question.getAnswers().add(answer);
			}
			// extract score
			Element setVarElement = (Element) answerMetadata.getElementsByTagName("setvar").item(0);
			String score = ((Text) setVarElement.getChildNodes().item(0)).getData();
			if (!StringUtils.isBlank(score)) {
			    if (Question.QUESTION_TYPE_MATCHING.equals(question.getType())) {
				// map matching answers to each other, plus set the score
				if (setVarElement.getAttribute("varname").endsWith("_Correct")) {
				    Answer matchAnswer = matchAnswerMap.get(answerId);
				    answerId = ((Element) scoreReference.item(0)).getAttribute("respident");
				    answer = answerMap.get(answerId);
				    answer.setScore(Float.parseFloat(score));

				    if (question.getMatchMap() == null) {
					question.setMatchMap(new TreeMap<Integer, Integer>());
				    }
				    Integer answerIndex = question.getAnswers().indexOf(answer);
				    Integer matchAnswerIndex = question.getMatchAnswers().indexOf(matchAnswer);
				    question.getMatchMap().put(answerIndex, matchAnswerIndex);
				}
			    } else {
				if (answer == null) {
				    // for all types except Matching and Fill-in-Blank
				    answer = answerMap.get(answerId);
				}
				answer.setScore(Float.parseFloat(score));
			    }
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
			question.setFeedback(WebUtil.removeHTMLtags(feedbackText));
		    } else {
			// it is an answer feedback
			String feedbackId = feedbackElement.getAttribute("ident");
			String answerId = feedbackMap.get(feedbackId);
			if (answerId != null) {
			    Answer answer = answerMap.get(answerId);
			    if (answer != null) {
				answer.setFeedback(WebUtil.removeHTMLtags(feedbackText));
			    }
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

	// to know where to stop searching for question entries (disabled/unchecked form fields are not sent)
	int questionCount = Integer.parseInt(WebUtil.extractParameterValue(queryString, "questionCount"));
	for (int questionIndex = 0; questionIndex < questionCount; questionIndex++) {
	    String questionType = WebUtil.extractParameterValue(queryString, "question" + questionIndex + "type");
	    Question question = new Question();
	    // if question was not checked on the form, type is NULL and it is not accepted
	    if (QuestionParser.isQuestionTypeAcceptable(questionType, null, question)) {
		String questionTitle = WebUtil.extractParameterValue(queryString, "question" + questionIndex);
		if (!StringUtils.isBlank(questionTitle)) {
		    question.setTitle(URLDecoder.decode(questionTitle, "UTF8"));
		}
		String questionText = WebUtil.extractParameterValue(queryString, "question" + questionIndex + "text");
		if (!StringUtils.isBlank(questionText)) {
		    question.setText(URLDecoder.decode(questionText, "UTF8"));
		}
		String questionFeedback = WebUtil.extractParameterValue(queryString, "question" + questionIndex
			+ "feedback");
		// can be blank
		if (!StringUtils.isBlank(questionFeedback)) {
		    question.setFeedback(URLDecoder.decode(questionFeedback, "UTF8"));
		}

		boolean isMatching = Question.QUESTION_TYPE_MATCHING.equals(question.getType());

		// extract answers
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

			    String answerFeedback = WebUtil.extractParameterValue(queryString, answerId + "feedback");
			    // can be blank
			    if (!StringUtils.isBlank(answerFeedback)) {
				answer.setFeedback(URLDecoder.decode(answerFeedback, "UTF8"));
			    }
			    question.getAnswers().add(answer);

			    if (isMatching) {
				// map indexes of answers
				String matchAnswerIndex = WebUtil.extractParameterValue(queryString, "question"
					+ questionIndex + "match" + answerIndex);
				if (!StringUtils.isBlank(matchAnswerIndex)) {
				    if (question.getMatchMap() == null) {
					question.setMatchMap(new TreeMap<Integer, Integer>());
				    }
				    question.getMatchMap().put(answerIndex, Integer.valueOf(matchAnswerIndex));
				}
			    }
			}
		    }
		}

		// extract match answers
		if (isMatching) {
		    String matchAnswerCountParam = WebUtil.extractParameterValue(queryString, "matchAnswerCount"
			    + questionIndex);
		    int matchAnswerCount = matchAnswerCountParam == null ? 0 : Integer.parseInt(matchAnswerCountParam);
		    if (matchAnswerCount > 0) {
			question.setMatchAnswers(new ArrayList<Answer>());
			for (int matchAnswerIndex = 0; matchAnswerIndex < matchAnswerCount; matchAnswerIndex++) {
			    String matchAnswerId = "question" + questionIndex + "matchAnswer" + matchAnswerIndex;
			    String matchAnswerText = WebUtil.extractParameterValue(queryString, matchAnswerId);

			    if (!StringUtils.isBlank(matchAnswerText)) {
				Answer matchAnswer = new Answer();
				matchAnswer.setText(URLDecoder.decode(matchAnswerText, "UTF8"));

				question.getMatchAnswers().add(matchAnswer);
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

    /**
     * Checks if given type has a correct value and should be processed. Also sets question property for convenience.
     */
    private static boolean isQuestionTypeAcceptable(String type, Set<String> limitType, Question question) {
	if (type == null || !Question.QUESTION_TYPES.contains(type)) {
	    return false;
	}
	question.setType(type);
	return (limitType == null) || limitType.isEmpty() || limitType.contains(type);
    }
}
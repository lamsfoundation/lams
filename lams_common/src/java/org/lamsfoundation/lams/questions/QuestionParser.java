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

package org.lamsfoundation.lams.questions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.UploadFileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
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

    // can be anything
    private static final String TEMP_PACKAGE_NAME_PREFIX = "QTI_PACKAGE_";
    private static final Pattern IMAGE_PATTERN = Pattern.compile("\\[IMAGE: (.*)\\]");

    public static final String UUID_LABEL_PREFIX = "lams-qb-uuid-";

    /**
     * Extracts questions from IMS QTI zip file.
     */
    public static Question[] parseQTIPackage(InputStream packageFileStream, Set<String> limitType)
	    throws SAXParseException, IOException, SAXException, ParserConfigurationException, ZipFileUtilException {
	List<Question> result = new ArrayList<>();

	// unique folder name
	String tempPackageName = TEMP_PACKAGE_NAME_PREFIX + System.currentTimeMillis();
	String tempPackageDirPath = ZipFileUtil.expandZip(packageFileStream, tempPackageName);
	try {
	    List<File> resourceFiles = QuestionParser.getQTIResourceFiles(tempPackageDirPath);
	    if (resourceFiles.isEmpty()) {
		log.warn("No resource files found in QTI package");
	    } else {
		// extract from every XML file; usually there is just one
		for (File resourceFile : resourceFiles) {
		    FileInputStream xmlFileStream = new FileInputStream(resourceFile);
		    Question[] fileQuestions = null;
		    try {
			fileQuestions = QuestionParser.parseQTIFile(xmlFileStream, tempPackageDirPath, limitType);
		    } finally {
			xmlFileStream.close();
		    }
		    if (fileQuestions != null) {
			Collections.addAll(result, fileQuestions);
		    }
		}
	    }
	} finally {
	    // clean up
	    packageFileStream.close();

	    // if there are any images attached, do not delete the exploded ZIP
	    // unfortunately, in this case it stays there until OS does temp dir clean up
	    boolean tempFolderStillNeeded = false;
	    if (result != null) {
		for (Question question : result) {
		    if (!StringUtils.isBlank(question.getResourcesFolderPath())) {
			tempFolderStillNeeded = true;
			break;
		    }
		}
	    }
	    if (!tempFolderStillNeeded) {
		ZipFileUtil.deleteDirectory(tempPackageDirPath);
	    }
	}

	return result.toArray(Question.QUESTION_ARRAY_TYPE);
    }

    /**
     * Extracts questions from IMS QTI xml file.
     */
    public static Question[] parseQTIFile(InputStream xmlFileStream, String resourcesFolderPath, Set<String> limitType)
	    throws ParserConfigurationException, SAXException, IOException {
	List<Question> result = new ArrayList<>();
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = docBuilder.parse(xmlFileStream);

	NodeList questionItems = doc.getElementsByTagName("item");
	// yes, a label here for convenience
	questionLoop: for (int questionItemIndex = 0; questionItemIndex < questionItems
		.getLength(); questionItemIndex++) {
	    Element questionItem = (Element) questionItems.item(questionItemIndex);
	    NodeList questionItemTypes = questionItem.getElementsByTagName("qmd_itemtype");
	    String questionItemType = questionItemTypes.getLength() > 0
		    ? ((Text) questionItemTypes.item(0).getChildNodes().item(0)).getData()
		    : null;
	    Question question = new Question();
	    // check if it is "matching" question type
	    if ("Matching".equalsIgnoreCase(questionItemType)
		    && !QuestionParser.isQuestionTypeAcceptable(Question.QUESTION_TYPE_MATCHING, limitType, question)) {
		continue;
	    }
	    String questionTitle = questionItem.getAttribute("title");
	    question.setTitle(questionTitle);
	    String questionLabel = questionItem.getAttribute("label");
	    if (StringUtils.isNotBlank(questionLabel)) {
		question.setLabel(questionLabel);
	    }

	    Map<String, Answer> answerMap = new TreeMap<>();
	    Map<String, Answer> matchAnswerMap = null;
	    boolean textBasedQuestion = false;

	    Element presentation = (Element) questionItem.getElementsByTagName("presentation").item(0);
	    NodeList presentationChildrenList = presentation.getChildNodes();
	    // cumberstone parsing, but there is no other way using this API
	    for (int presentationChildIndex = 0; presentationChildIndex < presentationChildrenList
		    .getLength(); presentationChildIndex++) {
		Node presentationChild = presentationChildrenList.item(presentationChildIndex);
		// here is where question data is stored
		if ("material".equals(presentationChild.getNodeName())) {
		    String questionText = QuestionParser.parseMaterialElement(presentationChild, question,
			    resourcesFolderPath);
		    if (questionText.trim().startsWith("<!--")) {
			// do not support Algorithmic question type
			continue questionLoop;
		    }
		    question.setText(questionText);
		} else if ("response_lid".equals(presentationChild.getNodeName())) {
		    if (question.getAnswers() == null) {
			boolean multipleAnswersAllowed = "multiple"
				.equalsIgnoreCase(((Element) presentationChild).getAttribute("rcardinality"));
			NodeList answerList = ((Element) presentationChild).getElementsByTagName("response_label");
			// parse answers
			for (int answerIndex = 0; answerIndex < answerList.getLength(); answerIndex++) {
			    Element answerElement = (Element) answerList.item(answerIndex);
			    Element materialElement = (Element) answerElement.getElementsByTagName("material").item(0);
			    String answerText = QuestionParser.parseMaterialElement(materialElement, question,
				    resourcesFolderPath);

			    // if there are answers different thatn true/false,
			    // it is Multiple Choice or Multiple Answers
			    if ((question.getType() == null) && !"true".equalsIgnoreCase(answerText)
				    && !"false".equalsIgnoreCase(answerText)
				    && !QuestionParser
					    .isQuestionTypeAcceptable(
						    multipleAnswersAllowed ? Question.QUESTION_TYPE_MULTIPLE_RESPONSE
							    : Question.QUESTION_TYPE_MULTIPLE_CHOICE,
						    limitType, question)) {
				continue questionLoop;
			    }

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

			// if there are only true/false answers, set the question type
			if ((question.getType() == null) && (question.getAnswers() != null)
				&& (question.getAnswers().size() == 2) && !multipleAnswersAllowed
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
			    answerMap = new TreeMap<>();
			}

			NodeList responseLidChildrenList = presentationChild.getChildNodes();
			for (int responseLidChildIndex = 0; responseLidChildIndex < responseLidChildrenList
				.getLength(); responseLidChildIndex++) {
			    // parse answers for first part of matching
			    Node responseLidChild = responseLidChildrenList.item(responseLidChildIndex);
			    if ("material".equals(responseLidChild.getNodeName())) {
				String answerText = QuestionParser.parseMaterialElement(responseLidChild, question,
					resourcesFolderPath);

				Answer answer = new Answer();
				answer.setText(answerText);
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
	    Map<String, String> feedbackMap = new TreeMap<>();
	    if (textBasedQuestion || !answerMap.isEmpty()) {
		NodeList answerMetadatas = questionItem.getElementsByTagName("respcondition");
		// if no answers at all, it is Essay type
		if ((answerMetadatas.getLength() == 0) && (question.getType() == null) && textBasedQuestion
			&& !QuestionParser.isQuestionTypeAcceptable(Question.QUESTION_TYPE_ESSAY, limitType,
				question)) {
		    continue questionLoop;
		}
		for (int answerMetadataIndex = 0; answerMetadataIndex < answerMetadatas
			.getLength(); answerMetadataIndex++) {
		    Element answerMetadata = (Element) answerMetadatas.item(answerMetadataIndex);
		    NodeList scoreReference = answerMetadata.getElementsByTagName("varequal");
		    // find where given metadata part references to
		    String answerId = scoreReference.getLength() > 0
			    ? ((Text) scoreReference.item(0).getChildNodes().item(0)).getData()
			    : null;
		    if (answerId == null) {
			// no answers at all, so it is Essay type
			if ((question.getType() == null) && textBasedQuestion && !QuestionParser
				.isQuestionTypeAcceptable(Question.QUESTION_TYPE_ESSAY, limitType, question)) {
			    continue questionLoop;
			}
		    } else {
			if ((question.getType() == null) && textBasedQuestion && !QuestionParser
				.isQuestionTypeAcceptable(Question.QUESTION_TYPE_FILL_IN_BLANK, limitType, question)) {
			    continue questionLoop;
			}

			Answer answer = null;
			if (textBasedQuestion) {
			    // for Fill-in-Blank answer parsing can be done only here
			    answer = new Answer();
			    answer.setText(answerId);
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
		Node materialElement = feedbackElement.getElementsByTagName("material").item(0);
		String feedbackText = QuestionParser.parseMaterialElement(materialElement, question,
			resourcesFolderPath);
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
			    if (answer != null) {
				answer.setFeedback(feedbackText);
			    }
			}
		    }
		}
	    }

	    result.add(question);
	}

	return result.toArray(Question.QUESTION_ARRAY_TYPE);
    }

    /**
     * Parses query string (send by form submit or extracted otherwise) from questionChoice.jsp form.
     */
    public static Question[] parseQuestionChoiceForm(HttpServletRequest request) throws UnsupportedEncodingException {
	List<Question> result = new ArrayList<>();

	// to know where to stop searching for question entries (disabled/unchecked form fields are not sent)

	int questionCount = WebUtil.readIntParam(request, "questionCount");
	for (int questionIndex = 0; questionIndex < questionCount; questionIndex++) {
	    String questionType = request.getParameter("question" + questionIndex + "type");
	    Question question = new Question();
	    // if question was not checked on the form, type is NULL and it is not accepted
	    if (QuestionParser.isQuestionTypeAcceptable(questionType, null, question)) {
		String questionTitle = request.getParameter("question" + questionIndex);
		if (!StringUtils.isBlank(questionTitle)) {
		    question.setTitle(questionTitle);
		}
		String questionLabel = request.getParameter("question" + questionIndex + "label");
		if (!StringUtils.isBlank(questionLabel)) {
		    question.setLabel(questionLabel);
		}
		String questionText = request.getParameter("question" + questionIndex + "text");
		if (!StringUtils.isBlank(questionText)) {
		    question.setText(questionText);
		}
		String questionFeedback = request.getParameter("question" + questionIndex + "feedback");
		// can be blank
		if (!StringUtils.isBlank(questionFeedback)) {
		    question.setFeedback(questionFeedback);
		}

		String questionResourcesFolderPath = request
			.getParameter("question" + questionIndex + "resourcesFolder");
		if (!StringUtils.isBlank(questionResourcesFolderPath)) {
		    question.setResourcesFolderPath(questionResourcesFolderPath);
		}

		boolean isMatching = Question.QUESTION_TYPE_MATCHING.equals(question.getType());

		// extract answers
		String answerCountParam = request.getParameter("answerCount" + questionIndex);
		int answerCount = answerCountParam == null ? 0 : Integer.parseInt(answerCountParam);
		if (answerCount > 0) {
		    question.setAnswers(new ArrayList<Answer>());
		    for (int answerIndex = 0; answerIndex < answerCount; answerIndex++) {
			String answerId = "question" + questionIndex + "answer" + answerIndex;
			String answerText = request.getParameter(answerId);
			String answerScore = request.getParameter(answerId + "score");
			if (!StringUtils.isBlank(answerText)) {
			    Answer answer = new Answer();
			    answer.setText(answerText);
			    if (!StringUtils.isBlank(answerScore)) {
				answer.setScore(Float.parseFloat(answerScore));
			    }

			    String answerFeedback = request.getParameter(answerId + "feedback");
			    // can be blank
			    if (!StringUtils.isBlank(answerFeedback)) {
				answer.setFeedback(answerFeedback);
			    }
			    question.getAnswers().add(answer);

			    if (isMatching) {
				// map indexes of answers
				String matchAnswerIndex = request
					.getParameter("question" + questionIndex + "match" + answerIndex);
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
		    String matchAnswerCountParam = request.getParameter("matchAnswerCount" + questionIndex);
		    int matchAnswerCount = matchAnswerCountParam == null ? 0 : Integer.parseInt(matchAnswerCountParam);
		    if (matchAnswerCount > 0) {
			question.setMatchAnswers(new ArrayList<Answer>());
			for (int matchAnswerIndex = 0; matchAnswerIndex < matchAnswerCount; matchAnswerIndex++) {
			    String matchAnswerId = "question" + questionIndex + "matchAnswer" + matchAnswerIndex;
			    String matchAnswerText = request.getParameter(matchAnswerId);

			    if (!StringUtils.isBlank(matchAnswerText)) {
				Answer matchAnswer = new Answer();
				matchAnswer.setText(matchAnswerText);

				question.getMatchAnswers().add(matchAnswer);
			    }
			}
		    }
		}

		result.add(question);
	    }
	}

	return result.toArray(Question.QUESTION_ARRAY_TYPE);
    }

    /**
     * Adapts QTI form field for Tools to use. Some Tools do not accept HTML, so it gets converted to plain text. Also
     * image placeholders get resolved here.
     */
    public static String processHTMLField(String fieldText, boolean forcePlainText, String contentFolderID,
	    String resourcesFolderPath) {
	String result = forcePlainText ? WebUtil.removeHTMLtags(fieldText) : fieldText;

	if (!StringUtils.isBlank(result)) {
	    Matcher imageMatcher = IMAGE_PATTERN.matcher(result);
	    StringBuffer resultBuilder = new StringBuffer();

	    // find image placeholders
	    while (imageMatcher.find()) {
		String imageAttributesStr = imageMatcher.group(1);

		List<String> imageAttributes = new ArrayList<>();
		Collections.addAll(imageAttributes, imageAttributesStr.split("\\|"));
		String fileName = imageAttributes.get(0);
		imageAttributes.remove(0);

		// if it is plain text or something goes wrong, the placeholder simply gets removed
		String replacement = "";
		if (!forcePlainText) {
		    if (resourcesFolderPath == null) {
			log.warn("Image " + fileName + " declaration found but its location is unknown.");
		    } else {
			File sourceFile = new File(resourcesFolderPath, fileName);
			if (sourceFile.canRead()) {
			    // copy the image from exploded IMS zip to secure dir in lams-www
			    File uploadDir = UploadFileUtil.getUploadDir(contentFolderID, "Image");
			    String destinationFileName = UploadFileUtil.getUploadFileName(uploadDir, fileName);
			    File destinationFile = new File(uploadDir, destinationFileName);
			    String uploadWebPath = UploadFileUtil.getUploadWebPath(contentFolderID, "Image") + '/'
				    + fileName;
			    try {
				FileUtils.copyFile(sourceFile, destinationFile);
				replacement = "<img src=\"" + uploadWebPath + "\" " + String.join("", imageAttributes)
					+ " />";
			    } catch (IOException e) {
				log.error("Could not store image " + fileName);
			    }
			} else {
			    log.warn("Image " + fileName + " declaration found but it can not be read.");
			}
		    }
		}
		imageMatcher.appendReplacement(resultBuilder, replacement);
	    }
	    imageMatcher.appendTail(resultBuilder);
	    result = resultBuilder.toString();
	}

	return StringUtils.isBlank(result) ? null : result;
    }

    /**
     * Find XML file list in IMS QTI package.
     */
    private static List<File> getQTIResourceFiles(String packageDirPath)
	    throws IOException, ParserConfigurationException, SAXException {
	InputStream inputStream = new FileInputStream(new File(packageDirPath, "imsmanifest.xml"));
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document manifest = docBuilder.parse(inputStream);
	NodeList resourcesList = manifest.getElementsByTagName("resources");
	List<File> resourceFiles = new LinkedList<>();
	if (resourcesList.getLength() > 0) {
	    Element resources = (Element) resourcesList.item(0);
	    NodeList resourceList = resources.getElementsByTagName("resource");

	    for (int resourceIndex = 0; resourceIndex < resourceList.getLength(); resourceIndex++) {
		Element resource = (Element) resourceList.item(resourceIndex);
		String resourceFileName = resource.getAttribute("href");
		if (resourceFileName.endsWith(".xml")) {
		    File resourceFile = new File(packageDirPath, resourceFileName);
		    if (!resourceFile.isFile() || !resourceFile.canRead()) {
			log.warn("XML resource file specified in IMS manifest can not be read: " + resourceFileName);
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
	if ((type == null) || !Question.QUESTION_TYPES.contains(type)) {
	    return false;
	}
	question.setType(type);
	return (limitType == null) || limitType.isEmpty() || limitType.contains(type);
    }

    /**
     * Constructs HTML text out of "material" IMS QTI tags. Images get replaced by placeholders for further processing.
     */
    private static String parseMaterialElement(Node materialElement, Question question, String resourcesFolderPath) {
	StringBuilder result = new StringBuilder();

	NodeList questionElements = materialElement.getChildNodes();
	for (int questionElementIndex = 0; questionElementIndex < questionElements
		.getLength(); questionElementIndex++) {
	    Node questionElement = questionElements.item(questionElementIndex);
	    String elementName = questionElement.getNodeName();
	    if ("mattext".equalsIgnoreCase(elementName) && questionElement.getChildNodes().getLength() > 0) {
		// it is a HTML part
		NodeList questionTextChildrenList = questionElement.getChildNodes();
		for (int questionTextIndex = 0; questionTextIndex < questionTextChildrenList
			.getLength(); questionTextIndex++) {
		    Node questionTextChildNode = questionElement.getChildNodes().item(questionTextIndex);
		    result.append(((Text) questionTextChildNode).getData());
		}
	    } else if ("matimage".equalsIgnoreCase(elementName)) {
		String width = ((Element) questionElement).getAttribute("width");
		String height = ((Element) questionElement).getAttribute("height");
		String classAttr = ((Element) questionElement).getAttribute("entityref");

		String fileName = ((Element) questionElement).getAttribute("uri");
		if (resourcesFolderPath == null) {
		    log.warn("Image " + fileName + " declaration found but its location is unknown");
		} else {
		    if (question.getResourcesFolderPath() == null) {
			question.setResourcesFolderPath(resourcesFolderPath);
		    }

		    //add filename and other attributes, separated by ":" character
		    result.append("[IMAGE: ").append(fileName);
		    if (StringUtils.isNotBlank(width)) {
			result.append("| width=\"" + width + "\"");
		    }
		    if (StringUtils.isNotBlank(height)) {
			result.append("| height=\"" + height + "\"");
		    }
		    if (StringUtils.isNotBlank(classAttr)) {
			result.append("| class=\"" + classAttr + "\"");
		    }
		    result.append("]");
		}
	    }
	}

	return result.toString();
    }
}
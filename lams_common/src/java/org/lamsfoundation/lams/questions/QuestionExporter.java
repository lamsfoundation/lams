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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Packs questions and answers into files. They can be later used in question-based 3rd party applications. Currently it
 * supports only IMS QTI but other methods can be added as needed.
 *
 * @author Marcin Cieslak
 *
 */
public class QuestionExporter {
    private static final Logger log = Logger.getLogger(QuestionExporter.class);

    private static final Pattern IMAGE_PATTERN = Pattern.compile(
	    "<img.*?src=['\"]/+lams/+www/+([\\w\\.\\/\\-\\s]+).+?>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern IMAGE_ATTRIBUTES_PATTERN = Pattern
	    .compile("(<img\\b|(?!^)\\G)[^>]*?\\b(class|width|height)=([\"']?)([^\"]*)\\3", Pattern.CASE_INSENSITIVE);

    private static final String EXPORT_TEMP_FOLDER_SUFFIX = "qti";

    private static final String EAR_IMAGE_FOLDER = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator
	    + FileUtil.LAMS_WWW_DIR;
    private static final File MANIFEST_TEMPLATE_FILE = new File(
	    Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator + "lams-central.war" + File.separator
		    + "questions" + File.separator + "imsmanifest_template.xml");

    private String packageTitle = null;
    private Question[] questions = null;

    private Document doc = null;
    private Integer itemId = null;
    private Map<String, File> images = new TreeMap<>();

    public QuestionExporter(String title, Question[] questions) {
	if (StringUtils.isBlank(title)) {
	    this.packageTitle = "export";
	} else {
	    // make sure the title can be used everywhere
	    this.packageTitle = FileUtil.stripInvalidChars(title).replaceAll(" ", "_");
	}

	this.questions = questions;
    }

    /**
     * Writes the exported QTI package to HTTP response, so it can be downloaded by user via browser.
     */
    public void exportQTIPackage(HttpServletRequest request, HttpServletResponse response) {
	String packagePath = exportQTIPackage();
	File packageFile = new File(packagePath);

	try {
	    String fileName = FileUtil.getFileName(packagePath);
	    fileName = FileUtil.encodeFilenameForDownload(request, fileName);
	    response.setContentType(CommonConstants.RESPONSE_CONTENT_TYPE_DOWNLOAD);
	    response.setHeader(CommonConstants.HEADER_CONTENT_DISPOSITION,
		    CommonConstants.HEADER_CONTENT_ATTACHMENT + fileName);

	    // write out the ZIP to respose error
	    FileUtils.copyFile(packageFile, response.getOutputStream());

	    // remove the directory containing the ZIP from file system
	    FileUtils.deleteDirectory(packageFile.getParentFile());
	} catch (IOException e) {
	    QuestionExporter.log.error("Error while exporti QTI package", e);
	}
    }

    /**
     * Builds a QTI ZIP package (manifest, QTI file, resources) with the given questions.
     *
     * @return Path to the created ZIP file
     */
    public String exportQTIPackage() {
	if (QuestionExporter.log.isDebugEnabled()) {
	    QuestionExporter.log.debug("Exporting QTI ZIP package \"" + packageTitle + "\"");
	}
	try {
	    String rootDir = FileUtil.createTempDirectory(QuestionExporter.EXPORT_TEMP_FOLDER_SUFFIX);
	    File dir = new File(rootDir, "content");

	    // main QTI file
	    String xmlFileName = packageTitle + ".xml";
	    File xmlFile = new File(dir, xmlFileName);
	    String xmlFileContent = exportQTIFile();
	    FileUtils.writeStringToFile(xmlFile, xmlFileContent, "UTF-8");

	    File manifestFile = new File(dir, "imsmanifest.xml");
	    String manifestContent = createManifest();
	    FileUtils.writeStringToFile(manifestFile, manifestContent, "UTF-8");

	    // copy images used in activities from lams-www to ZIP folder
	    for (String imageName : images.keySet()) {
		File imageFile = new File(dir, imageName);
		FileUtils.copyFile(images.get(imageName), imageFile);
	    }

	    String targetZipFileName = "lams_qti_" + packageTitle + ".zip";
	    String zipPath = ZipFileUtil.createZipFile(targetZipFileName, dir.getAbsolutePath(), rootDir);

	    // remove the directory containing the original sources from file system
	    FileUtils.deleteDirectory(dir);

	    return zipPath;
	} catch (Exception e) {
	    QuestionExporter.log.error("Error while exporti QTI package", e);
	}

	return null;
    }

    /**
     * Builds QTI XML file containing structured questions & answers content.
     *
     * @return XML file content
     * @throws IOException
     */
    public String exportQTIFile() throws IOException {
	itemId = 1000;
	DocumentBuilder docBuilder;
	try {
	    docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    doc = docBuilder.newDocument();
	} catch (ParserConfigurationException e) {
	    QuestionExporter.log.error("Error while instantinating Document Builder", e);
	    return null;
	}

	Element rootElem = (Element) doc.appendChild(doc.createElement("questestinterop"));
	Element assessmentElem = (Element) rootElem.appendChild(doc.createElement("assessment"));
	assessmentElem.setAttribute("title", packageTitle);
	assessmentElem.setAttribute("ident", "A1001");
	Element sectionElem = (Element) assessmentElem.appendChild(doc.createElement("section"));
	sectionElem.setAttribute("title", "Main");
	sectionElem.setAttribute("ident", "S1002");

	for (Question question : questions) {
	    Element itemElem = null;
	    if (Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType())
		    || Question.QUESTION_TYPE_TRUE_FALSE.equals(question.getType())
		    || Question.QUESTION_TYPE_MULTIPLE_RESPONSE.equals(question.getType())) {
		itemElem = exportMultipleChoiceQuestion(question);
	    } else if (Question.QUESTION_TYPE_FILL_IN_BLANK.equals(question.getType())) {
		itemElem = exportFillInBlankQuestion(question);
	    } else if (Question.QUESTION_TYPE_MATCHING.equals(question.getType())) {
		itemElem = exportMatchingPairsQuestion(question);
	    } else if (Question.QUESTION_TYPE_ESSAY.equals(question.getType())) {
		itemElem = exportEssayQuestion(question);
	    }

	    if (itemElem == null) {
		QuestionExporter.log
			.warn("Unknow type \"" + question.getType() + "\" of question \"" + question.getTitle() + "\"");
	    } else {
		sectionElem.appendChild(itemElem);
	    }
	}

	return FileUtil.writeXMLtoString(doc);
    }

    /**
     * Creates a XML element with contents of a single multiple choice or true/false or multiple response question.
     */
    private Element exportMultipleChoiceQuestion(Question question) {
	Element itemElem = doc.createElement("item");
	itemElem.setAttribute("title", question.getTitle());
	if (question.getLabel() != null) {
	    itemElem.setAttribute("label", question.getLabel());
	}
	itemId++;
	itemElem.setAttribute("ident", "QUE_" + itemId);

	// question text
	Element presentationElem = (Element) itemElem.appendChild(doc.createElement("presentation"));
	if (!StringUtils.isBlank(question.getText())) {
	    Element materialElem = (Element) presentationElem.appendChild(doc.createElement("material"));
	    appendMaterialElements(materialElem, question.getText());
	}

	itemId++;
	String responseLidIdentifier = "QUE_" + itemId + "_RL";
	Element responseLidElem = (Element) presentationElem.appendChild(doc.createElement("response_lid"));
	responseLidElem.setAttribute("ident", responseLidIdentifier);
	responseLidElem.setAttribute("rcardinality",
		Question.QUESTION_TYPE_MULTIPLE_RESPONSE.equals(question.getType()) ? "Multiple" : "Single");
	responseLidElem.setAttribute("rtiming", "No");

	// question feedback (displayed no matter what answer was choosed)
	List<Element> feedbackList = new ArrayList<>();
	String correctFeedbackLabel = null;
	String incorrectFeedbackLabel = null;
	Element overallFeedbackElem = null;
	if (!StringUtils.isBlank(question.getFeedback())) {
	    overallFeedbackElem = createFeedbackElem("_ALL", question.getFeedback(), "All");
	    feedbackList.add(overallFeedbackElem);
	}

	Element renderChoiceElem = (Element) responseLidElem.appendChild(doc.createElement("render_choice"));
	short answerId = 0;
	List<Element> respconditionList = new ArrayList<>(question.getAnswers().size());

	// iterate through answers, collecting some info along the way
	for (Answer answer : question.getAnswers()) {
	    // just labels for feedback for correct/incorrect answer
	    boolean isCorrect = answer.getScore() > 0;
	    if (!StringUtils.isBlank(answer.getFeedback())) {
		if (!isCorrect && (incorrectFeedbackLabel == null)) {
		    Element feedbackElem = createFeedbackElem("_IC", answer.getFeedback(), "Candidate");
		    feedbackList.add(feedbackElem);
		    incorrectFeedbackLabel = feedbackElem.getAttribute("ident");
		} else if (isCorrect && (correctFeedbackLabel == null)) {
		    Element feedbackElem = createFeedbackElem("_C", answer.getFeedback(), "Candidate");
		    feedbackList.add(feedbackElem);
		    correctFeedbackLabel = feedbackElem.getAttribute("ident");
		}
	    }
	}

	// proper iteration
	for (Answer answer : question.getAnswers()) {
	    Element responseLabelElem = (Element) renderChoiceElem.appendChild(doc.createElement("response_label"));
	    itemId++;
	    answerId++;
	    String answerLabel = "QUE_" + itemId + "_A" + answerId;
	    responseLabelElem.setAttribute("ident", answerLabel);

	    // answer text
	    if (!StringUtils.isBlank(answer.getText())) {
		Element materialElem = (Element) responseLabelElem.appendChild(doc.createElement("material"));
		appendMaterialElements(materialElem, answer.getText());
	    }

	    // mark which answer is correct by setting score for each of them
	    Element respconditionElem = doc.createElement("respcondition");
	    Element conditionvarElem = (Element) respconditionElem.appendChild(doc.createElement("conditionvar"));
	    Element varequalElem = (Element) conditionvarElem.appendChild(doc.createElement("varequal"));
	    varequalElem.setAttribute("respident", responseLidIdentifier);
	    varequalElem.setTextContent(answerLabel);

	    boolean isCorrect = answer.getScore() > 0;
	    Element setvarElem = (Element) respconditionElem.appendChild(doc.createElement("setvar"));
	    setvarElem.setAttribute("varname", "que_score");
	    setvarElem.setAttribute("action",
		    isCorrect && !Question.QUESTION_TYPE_MULTIPLE_RESPONSE.equals(question.getType()) ? "Set" : "Add");
	    setvarElem.setTextContent(String.valueOf(answer.getScore()));

	    // link feedback for correct/incorrect answer
	    if (isCorrect) {
		Element displayfeedbackElem = (Element) respconditionElem
			.appendChild(doc.createElement("displayfeedback"));
		displayfeedbackElem.setAttribute("feedbacktype", "Response");
		displayfeedbackElem.setAttribute("linkrefid", correctFeedbackLabel);
	    } else {
		Element displayfeedbackElem = (Element) respconditionElem
			.appendChild(doc.createElement("displayfeedback"));
		displayfeedbackElem.setAttribute("feedbacktype", "Response");
		displayfeedbackElem.setAttribute("linkrefid", incorrectFeedbackLabel);
	    }

	    if (overallFeedbackElem != null) {
		Element displayfeedbackElem = (Element) respconditionElem
			.appendChild(doc.createElement("displayfeedback"));
		displayfeedbackElem.setAttribute("feedbacktype", "Response");
		displayfeedbackElem.setAttribute("linkrefid", overallFeedbackElem.getAttribute("ident"));
	    }

	    respconditionList.add(respconditionElem);
	}

	if (overallFeedbackElem != null) {
	    Element respconditionElem = doc.createElement("respcondition");
	    Element conditionvarElem = (Element) respconditionElem.appendChild(doc.createElement("conditionvar"));
	    conditionvarElem.appendChild(doc.createElement("other"));
	    Element displayfeedbackElem = (Element) respconditionElem.appendChild(doc.createElement("displayfeedback"));
	    displayfeedbackElem.setAttribute("feedbacktype", "Response");
	    displayfeedbackElem.setAttribute("linkrefid", overallFeedbackElem.getAttribute("ident"));

	    respconditionList.add(respconditionElem);
	}

	Element resprocessingElem = (Element) itemElem.appendChild(doc.createElement("resprocessing"));
	Element outcomesElem = (Element) resprocessingElem.appendChild(doc.createElement("outcomes"));
	Element decvarElem = (Element) outcomesElem.appendChild(doc.createElement("decvar"));
	decvarElem.setAttribute("vartype", "decimal");
	decvarElem.setAttribute("defaultval", "0");
	decvarElem.setAttribute("varname", "que_score");

	// write out elements collected during answer iteration
	for (Element respconditionElem : respconditionList) {
	    resprocessingElem.appendChild(respconditionElem);
	}

	for (Element feedbackElem : feedbackList) {
	    itemElem.appendChild(feedbackElem);
	}

	return itemElem;
    }

    /**
     * Creates a XML element with contents of a single matchin pairs question.
     */
    private Element exportMatchingPairsQuestion(Question question) {
	Element itemElem = doc.createElement("item");
	itemElem.setAttribute("title", question.getTitle());
	if (question.getLabel() != null) {
	    itemElem.setAttribute("label", question.getLabel());
	}
	itemId++;
	itemElem.setAttribute("ident", "QUE_" + itemId);

	// QTI metadata indicating that this is a matching pairs question
	Element itemmetadataElem = (Element) itemElem.appendChild(doc.createElement("itemmetadata"));
	Element itemtypeElem = (Element) itemmetadataElem.appendChild(doc.createElement("qmd_itemtype"));
	itemtypeElem.setTextContent("Matching");

	// question text
	Element presentationElem = (Element) itemElem.appendChild(doc.createElement("presentation"));
	if (!StringUtils.isBlank(question.getText())) {
	    Element materialElem = (Element) presentationElem.appendChild(doc.createElement("material"));
	    appendMaterialElements(materialElem, question.getText());
	}

	int answerIndex = 0;
	List<String> matchAnswerIdents = new ArrayList<>(question.getAnswers().size());
	List<Element> respconditionElems = new ArrayList<>(question.getAnswers().size());
	for (Answer answer : question.getAnswers()) {
	    itemId++;
	    String responseLidIdentifier = "QUE_" + itemId + "_RL";
	    Element responseLidElem = (Element) presentationElem.appendChild(doc.createElement("response_lid"));
	    responseLidElem.setAttribute("ident", responseLidIdentifier);
	    // answer text
	    Element materialElem = (Element) responseLidElem.appendChild(doc.createElement("material"));
	    appendMaterialElements(materialElem, answer.getText());

	    Element renderchoiceElem = (Element) responseLidElem.appendChild(doc.createElement("render_choice"));
	    renderchoiceElem.setAttribute("shuffle", "No");

	    // find matching answers
	    int matchAnswerIndex = 0;
	    for (Answer matchAnswer : question.getMatchAnswers()) {
		Element responselabelElem = (Element) renderchoiceElem.appendChild(doc.createElement("response_label"));

		// repeat each matchin answer for each answer, keeping the same IDs
		String matchAnswerIdent = null;
		if (matchAnswerIdents.size() > matchAnswerIndex) {
		    matchAnswerIdent = matchAnswerIdents.get(matchAnswerIndex);
		} else {
		    itemId++;
		    matchAnswerIdent = "QUE_" + itemId + "_A" + (matchAnswerIndex + 1);
		    matchAnswerIdents.add(matchAnswerIdent);
		}
		responselabelElem.setAttribute("ident", matchAnswerIdent);

		materialElem = (Element) responselabelElem.appendChild(doc.createElement("material"));
		appendMaterialElements(materialElem, matchAnswer.getText());

		if (matchAnswerIndex == question.getMatchMap().get(answerIndex)) {
		    Element respconditionElem = doc.createElement("respcondition");
		    respconditionElem.setAttribute("title", "Matching " + responseLidIdentifier + " Resp Condition 1");
		    Element conditionvarElem = (Element) respconditionElem
			    .appendChild(doc.createElement("conditionvar"));
		    Element varequalElem = (Element) conditionvarElem.appendChild(doc.createElement("varequal"));
		    varequalElem.setAttribute("respident", responseLidIdentifier);
		    varequalElem.setTextContent(matchAnswerIdent);

		    Element setvarElem = (Element) respconditionElem.appendChild(doc.createElement("setvar"));
		    setvarElem.setAttribute("varname", "que_score");
		    setvarElem.setAttribute("action", "Add");
		    setvarElem.setTextContent(String.valueOf(answer.getScore()));

		    respconditionElems.add(respconditionElem);
		}

		matchAnswerIndex++;
	    }

	    answerIndex++;
	}

	Element resprocessingElem = (Element) itemElem.appendChild(doc.createElement("resprocessing"));
	Element outcomesElem = (Element) resprocessingElem.appendChild(doc.createElement("outcomes"));
	Element decvarElem = (Element) outcomesElem.appendChild(doc.createElement("decvar"));
	decvarElem.setAttribute("vartype", "decimal");
	decvarElem.setAttribute("defaultval", "0");
	decvarElem.setAttribute("varname", "que_score");

	for (Element respconditionElem : respconditionElems) {
	    resprocessingElem.appendChild(respconditionElem);
	}

	// just a single feedback element
	if (!StringUtils.isBlank(question.getFeedback())) {
	    Element overallFeedbackElem = createFeedbackElem("_ALL", question.getFeedback(), "All");

	    Element respconditionElem = (Element) resprocessingElem.appendChild(doc.createElement("respcondition"));
	    Element conditionvarElem = (Element) respconditionElem.appendChild(doc.createElement("conditionvar"));
	    conditionvarElem.appendChild(doc.createElement("other"));
	    Element displayfeedbackElem = (Element) respconditionElem.appendChild(doc.createElement("displayfeedback"));
	    displayfeedbackElem.setAttribute("feedbacktype", "Response");
	    displayfeedbackElem.setAttribute("linkrefid", overallFeedbackElem.getAttribute("ident"));

	    itemElem.appendChild(overallFeedbackElem);
	}

	return itemElem;
    }

    /**
     * Creates a XML element with contents of a single essay question.
     */
    private Element exportEssayQuestion(Question question) {
	Element itemElem = doc.createElement("item");
	itemElem.setAttribute("title", question.getTitle());
	if (question.getLabel() != null) {
	    itemElem.setAttribute("label", question.getLabel());
	}
	itemId++;
	itemElem.setAttribute("ident", "QUE_" + itemId);

	// question text
	Element presentationElem = (Element) itemElem.appendChild(doc.createElement("presentation"));
	if (!StringUtils.isBlank(question.getText())) {
	    Element materialElem = (Element) presentationElem.appendChild(doc.createElement("material"));
	    appendMaterialElements(materialElem, question.getText());
	}

	// just a single response element
	itemId++;
	Element responseStrElem = (Element) presentationElem.appendChild(doc.createElement("response_str"));
	responseStrElem.setAttribute("ident", "QUE_" + itemId + "_RS");
	Element renderFibElem = (Element) responseStrElem.appendChild(doc.createElement("render_fib"));
	renderFibElem.setAttribute("fibtype", "String");
	renderFibElem.setAttribute("prompt", "Box");
	renderFibElem.setAttribute("rows", "5");
	renderFibElem.setAttribute("columns", "50");
	itemId++;
	Element responseLabelElem = (Element) renderFibElem.appendChild(doc.createElement("response_label"));
	responseLabelElem.setAttribute("ident", "QUE_" + itemId + "_ANS");

	// just a single feedback element
	if (!StringUtils.isBlank(question.getFeedback())) {
	    Element overallFeedbackElem = createFeedbackElem("_ALL", question.getFeedback(), "All");

	    Element resprocessingElem = (Element) itemElem.appendChild(doc.createElement("resprocessing"));
	    Element respconditionElem = (Element) resprocessingElem.appendChild(doc.createElement("respcondition"));
	    Element conditionvarElem = (Element) respconditionElem.appendChild(doc.createElement("conditionvar"));
	    conditionvarElem.appendChild(doc.createElement("other"));
	    Element displayfeedbackElem = (Element) respconditionElem.appendChild(doc.createElement("displayfeedback"));
	    displayfeedbackElem.setAttribute("feedbacktype", "Response");
	    displayfeedbackElem.setAttribute("linkrefid", overallFeedbackElem.getAttribute("ident"));

	    itemElem.appendChild(overallFeedbackElem);
	}

	return itemElem;
    }

    /**
     * Creates a XML element with contents of a single fill in blanks question.
     */
    private Element exportFillInBlankQuestion(Question question) {
	Element itemElem = doc.createElement("item");
	itemElem.setAttribute("title", question.getTitle());
	if (question.getLabel() != null) {
	    itemElem.setAttribute("label", question.getLabel());
	}
	itemId++;
	itemElem.setAttribute("ident", "QUE_" + itemId);

	// question text
	Element presentationElem = (Element) itemElem.appendChild(doc.createElement("presentation"));
	if (!StringUtils.isBlank(question.getText())) {
	    Element materialElem = (Element) presentationElem.appendChild(doc.createElement("material"));
	    appendMaterialElements(materialElem, question.getText());
	}

	// just a single response element
	itemId++;
	Element responseStrElem = (Element) presentationElem.appendChild(doc.createElement("response_str"));
	responseStrElem.setAttribute("ident", "QUE_" + itemId + "_RS");
	Element renderFibElem = (Element) responseStrElem.appendChild(doc.createElement("render_fib"));
	renderFibElem.setAttribute("fibtype", "String");
	renderFibElem.setAttribute("prompt", "Box");
	itemId++;
	Element responseLabelElem = (Element) renderFibElem.appendChild(doc.createElement("response_label"));
	responseLabelElem.setAttribute("ident", "QUE_" + itemId + "_ANS");

	// just a single feedback element
	Element overallFeedbackElem = null;
	if (!StringUtils.isBlank(question.getFeedback())) {
	    overallFeedbackElem = createFeedbackElem("_ALL", question.getFeedback(), "All");
	}

	Element resprocessingElem = (Element) itemElem.appendChild(doc.createElement("resprocessing"));
	Element outcomesElem = (Element) resprocessingElem.appendChild(doc.createElement("outcomes"));
	Element decvarElem = (Element) outcomesElem.appendChild(doc.createElement("decvar"));
	decvarElem.setAttribute("vartype", "decimal");
	decvarElem.setAttribute("defaultval", "0");
	decvarElem.setAttribute("varname", "que_score");

	for (Answer answer : question.getAnswers()) {
	    // mark which answer is correct by setting score for each of them
	    Element respconditionElem = (Element) resprocessingElem.appendChild(doc.createElement("respcondition"));
	    Element conditionvarElem = (Element) respconditionElem.appendChild(doc.createElement("conditionvar"));
	    Element varequalElem = (Element) conditionvarElem.appendChild(doc.createElement("varequal"));
	    varequalElem.setAttribute("respident", responseStrElem.getAttribute("ident"));
	    varequalElem.setTextContent(answer.getText());

	    Element setvarElem = (Element) respconditionElem.appendChild(doc.createElement("setvar"));
	    setvarElem.setAttribute("varname", "que_score");
	    setvarElem.setAttribute("action", "Add");
	    setvarElem.setTextContent(String.valueOf(answer.getScore()));

	    if (overallFeedbackElem != null) {
		Element displayfeedbackElem = (Element) respconditionElem
			.appendChild(doc.createElement("displayfeedback"));
		displayfeedbackElem.setAttribute("feedbacktype", "Response");
		displayfeedbackElem.setAttribute("linkrefid", overallFeedbackElem.getAttribute("ident"));
	    }
	}

	if (overallFeedbackElem != null) {
	    itemElem.appendChild(overallFeedbackElem);
	}

	return itemElem;
    }

    /**
     * Creates a feedback XML element.
     */
    private Element createFeedbackElem(String labelSuffix, String feedback, String type) {
	itemId++;
	String label = "QUE_" + itemId + labelSuffix;
	Element feedbackElem = doc.createElement("itemfeedback");
	feedbackElem.setAttribute("ident", label);
	feedbackElem.setAttribute("view", type);
	Element materialElem = (Element) feedbackElem.appendChild(doc.createElement("material"));
	appendMaterialElements(materialElem, feedback);

	return feedbackElem;
    }

    /**
     * Appends material XML element, i.e. list of HTML & image parts
     */
    private void appendMaterialElements(Element materialElem, String text) {
	int index = 0;
	// looks for images stored in LAMS WWW secure folder
	Matcher imageTagMatcher = QuestionExporter.IMAGE_PATTERN.matcher(text);

	while (imageTagMatcher.find()) {
	    // add HTML which is before the image
	    appendTextElement(materialElem, text.substring(index, imageTagMatcher.start()));
	    index = imageTagMatcher.end();

	    // find the image in file system
	    String relativePath = imageTagMatcher.group(1);
	    File image = new File(QuestionExporter.EAR_IMAGE_FOLDER, relativePath);
	    if (!image.isFile() || !image.canRead()) {
		log.warn("Image could not be parsed: " + imageTagMatcher.group());
		continue;
	    }

	    // was it already added?
	    String imageName = null;
	    for (String key : images.keySet()) {
		if (image.equals(images.get(key))) {
		    imageName = key;
		    break;
		}
	    }

	    // if it wasn't added, store the reference so ZIP packing knows where to copy from and how to name the file
	    if (imageName == null) {
		String baseImageName = FileUtil.getFileName(relativePath);
		imageName = baseImageName;
		short prefix = 1;
		while (images.containsKey(imageName)) {
		    // if the name is the same, use an arbitrary prefix
		    imageName = prefix + "_" + baseImageName;
		    prefix++;
		}

		images.put(imageName, image);
	    }

	    //append image element
	    String imageType = "image/" + FileUtil.getFileExtension(imageName);
	    Element matimageElem = (Element) materialElem.appendChild(doc.createElement("matimage"));
	    matimageElem.setAttribute("imagtype", imageType);
	    matimageElem.setAttribute("uri", imageName);

	    //set image attributes: width, length, and class
	    Matcher attributesMatcher = IMAGE_ATTRIBUTES_PATTERN.matcher(imageTagMatcher.group(0));
	    while (attributesMatcher.find()) {
		String attributeName = attributesMatcher.group(2);
		String attributeValue = attributesMatcher.group(4);

		if ("class".equals(attributeName)) {
		    matimageElem.setAttribute("entityref", attributeValue);
		} else {
		    matimageElem.setAttribute(attributeName, attributeValue);
		}
	    }
	}

	// write out the rest of HTML text
	if (index < text.length()) {
	    appendTextElement(materialElem, text.substring(index));
	}
    }

    /**
     * Appends mattext element to materialElem. Used in appendMaterialElements(...) method only
     */
    private void appendTextElement(Element materialElem, String text) {
	if (StringUtils.isNotBlank(text)) {
	    Element mattextElem = (Element) materialElem.appendChild(doc.createElement("mattext"));
	    mattextElem.setAttribute("texttype", "text/html");
	    mattextElem.appendChild(doc.createCDATASection(text));
	}
    }

    /**
     * Fill the existing template file with current data.
     *
     * @return contents of XML template file
     */
    private String createManifest() throws IOException {
	String id = UUID.randomUUID().toString();
	String fileName = packageTitle + ".xml";

	StringBuilder resourceEntries = new StringBuilder("<file href=\"").append(fileName).append("\" />\n");
	for (String imageName : images.keySet()) {
	    resourceEntries.append("<file href=\"").append(imageName).append("\" />").append("\n");
	}

	String manifest = FileUtils.readFileToString(QuestionExporter.MANIFEST_TEMPLATE_FILE);
	manifest = manifest.replace("[ID]", id).replace("[TITLE]", packageTitle).replace("[FILE_NAME]", fileName)
		.replace("[FILE_LIST]", resourceEntries);
	return manifest;
    }
}
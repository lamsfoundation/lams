package org.lamsfoundation.lams.learningdesign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Import filter class for different version of MC content.
 *
 * @author steven
 */
public class McqImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 1.0 version content to 1.1 version tool server.
     *
     */
    public void up10To20061015() {
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McQueContent", "weight");
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McUsrAttempt", "timeZone");

    }

    public void up20061015To20061113() {
	// Change name to suit the version you give the tool.
	this.addField("org.lamsfoundation.lams.tool.mc.model.McContent", "showMarks", "false");
	this.addField("org.lamsfoundation.lams.tool.mc.model.McContent", "randomize", "false");
	this.addField("org.lamsfoundation.lams.tool.mc.model.McOptsContent", "displayOrder", "0");
    }

    public void up20061113To20070820() {
	// Adds displayAnswers LDEV-1156
	this.addField("org.lamsfoundation.lams.tool.mc.model.McContent", "displayAnswers", "true");
    }

    /**
     * Import 20140101 version content to 20140102 version tool server.
     */
    public void up20140101To20140102() {
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McContent", "runOffline");
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McContent", "onlineInstructions");
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McContent", "offlineInstructions");
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McContent", "mcAttachments");
    }

    /**
     * Import 20131212 version content to 20140512 version tool server.
     */
    public void up20140102To20140505() {
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McContent", "contentInUse");
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McQueContent", "mcUsrAttempts");
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McOptsContent", "mcUsrAttempts");
    }

    public void up20180724To20181202() {
	this.renameClass("org.lamsfoundation.lams.tool.mc.pojos.", "org.lamsfoundation.lams.tool.mc.model.");
    }

    /**
     * Import 20190110 version content to 20190517 version tool server.
     */
    public void up20181202To20190517() {
	this.removeField("org.lamsfoundation.lams.tool.mc.model.McQueContent", "questionHash");
    }

    /**
     * Migration to Question Bank
     */
    public void up20190517To20190809(String toolFilePath) throws IOException {
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
	    NodeList mcQuestions = toolRoot.getElementsByTagName("org.lamsfoundation.lams.tool.mc.model.McQueContent");
	    if (mcQuestions.getLength() == 0) {
		return;
	    }

	    // get create date from MCQ content rather than from each question separately
	    String createDate = XMLUtil.getChildElementValue(toolRoot, "createDate", null);
	    if (createDate == null) {
		createDate = new SimpleDateFormat(DateUtil.EXPORT_LD_FORMAT).format(new Date());
	    }

	    // go through each question
	    for (int mcQuestionIndex = 0; mcQuestionIndex < mcQuestions.getLength(); mcQuestionIndex++) {
		Element mcQuestion = (Element) mcQuestions.item(mcQuestionIndex);
		// create an element for QbQuestion
		Element qbQuestion = document.createElement("qbQuestion");
		mcQuestion.appendChild(qbQuestion);

		// transform MCQ data into QB structure
		XMLUtil.addTextElement(qbQuestion, "type", "1");
		// Question ID will be filled later as it requires QbService
		XMLUtil.addTextElement(qbQuestion, "version", "1");
		XMLUtil.addTextElement(qbQuestion, "contentFolderId", contentFolderIdFinal);
		XMLUtil.addTextElement(qbQuestion, "createDate", createDate);
		XMLUtil.rewriteTextElement(mcQuestion, qbQuestion, "mark", "maxMark", "1", false, true);
		XMLUtil.rewriteTextElement(mcQuestion, qbQuestion, "feedback", "feedback", null, false, true,
			QbUtils.QB_MIGRATION_TRIMMER);
		String description = XMLUtil.rewriteTextElement(mcQuestion, qbQuestion, "question", "description", null,
			false, true, QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		// get name out of description as there were no descriptions in MCQ before
		if (description != null) {
		    XMLUtil.addTextElement(qbQuestion, "name",
			    QbUtils.QB_MIGRATION_QUESTION_NAME_GENERATOR.apply(description));
		}

		// now it's time for options
		NodeList mcOptions = mcQuestion
			.getElementsByTagName("org.lamsfoundation.lams.tool.mc.model.McOptsContent");
		if (mcOptions.getLength() == 0) {
		    continue;
		}

		Element qbOptions = document.createElement("qbOptions");
		qbQuestion.appendChild(qbOptions);
		int maxDisplayOrder = 0;
		for (int mcOptionIndex = 0; mcOptionIndex < mcOptions.getLength(); mcOptionIndex++) {
		    Element mcOption = (Element) mcOptions.item(mcOptionIndex);
		    Element qbOption = document.createElement("org.lamsfoundation.lams.qb.model.QbOption");
		    qbOptions.appendChild(qbOption);

		    // MCQ had correct/incorrect indicator. In QB we have 1/0 max mark
		    boolean correctOption = Boolean
			    .valueOf(XMLUtil.getChildElementValue(mcOption, "correctOption", "false"));
		    XMLUtil.addTextElement(qbOption, "maxMark", correctOption ? "1" : "0");

		    maxDisplayOrder = Math.max(Integer.valueOf(XMLUtil.rewriteTextElement(mcOption, qbOption,
			    "displayOrder", "displayOrder", String.valueOf(maxDisplayOrder + 1))), maxDisplayOrder);
		    XMLUtil.rewriteTextElement(mcOption, qbOption, "mcQueOptionText", "name", null, false, false,
			    QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		}

		// get rid of junk
		mcQuestion.removeChild(mcOptions.item(0).getParentNode());
	    }
	});
    }
    
    /**
     * Migration to Assessment tool
     */
    public void up20200119To20200120(String toolFilePath) throws IOException {
	//perform all transformations from the previous methods. Do it now as long as we're going to rename classes which can intervene with previous changes 
	transformXML(toolFilePath);
	
	//transform McContent into Assessment
	this.renameClass("org.lamsfoundation.lams.tool.mc.model.McContent", "org.lamsfoundation.lams.tool.assessment.model.Assessment");
	//start referring to McContent as "org.lamsfoundation.lams.tool.assessment.model.Assessment", as class renaming occurs before all other commands 
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "mcContentId", "contentId");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "reflect", "reflectOnActivity");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "reflectionSubject", "reflectInstructions");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "creationDate", "created");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "updateDate", "updated");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "randomize", "shuffled");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "displayAnswers", "displaySummary");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "displayFeedbackOnly", "allowOverallFeedbackAfterQuestion");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "showMarks", "allowGradesAfterAttempt");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "prefixAnswersWithLetters", "numbered");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "passMark", "passingMark");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "timeLimit", "0");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "allowQuestionFeedback", "true");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "allowDiscloseAnswers", "true");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "allowRightAnswersAfterQuestion", "false");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "allowWrongAnswersAfterQuestion", "false");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "allowHistoryResponses", "false");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "notifyTeachersOnAttemptCompletion", "false");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "confidenceLevelsType", "1");
	//assign null createdBy property for simplicity. Assessment can handle null user
	this.removeField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "createdBy");
	this.removeField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "showReport");
	this.renameField("org.lamsfoundation.lams.tool.assessment.model.Assessment", "mcQueContents", "questions");
	
	//transform McQueContent into AssessmentQuestion
	this.renameClass("org.lamsfoundation.lams.tool.mc.model.McQueContent", "org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion", "randomQuestion", "false");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion", "correctAnswersDisclosed", "false");
	this.addField("org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion", "groupsAnswersDisclosed", "false");
	this.removeField("org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion", "mcContent");
	transformXML(toolFilePath);
	
	// perform the rest of transformations, which require more sophisticated manipulations
	transformXML(toolFilePath, assessment -> {
	    Document document = assessment.getOwnerDocument();

	    // at first, find questions
	    NodeList mcQuestions = assessment.getElementsByTagName("org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion");
	    if (mcQuestions.getLength() == 0) {
		return;
	    }
	    
	    //change mcContent's retries to !retries (as Assessments attemptsAllowed is a negation of McContent's retries)
	    String retries = XMLUtil.getChildElementValue(assessment, "retries", "true");
	    XMLUtil.removeElement(assessment, "retries");
	    XMLUtil.addTextElement(assessment, "attemptsAllowed", Boolean.parseBoolean(retries) ? "1" : "0");
	    
	    //change mcContent's questionsSequenced to Assessment's questionsPerPage
	    String questionsSequenced = XMLUtil.getChildElementValue(assessment, "questionsSequenced", "false");
	    XMLUtil.removeElement(assessment, "questionsSequenced");
	    XMLUtil.addTextElement(assessment, "questionsPerPage", Boolean.parseBoolean(questionsSequenced) ? "1" : "0");
	    
	    // create QuestionReferences
	    Element questionReferences = document.createElement("questionReferences");
	    questionReferences.setAttribute("class", "sorted-set");
	    assessment.appendChild(questionReferences);
	    
	    Element comparator = document.createElement("comparator");
	    comparator.setAttribute("class", "org.lamsfoundation.lams.tool.assessment.util.SequencableComparator");
	    questionReferences.appendChild(comparator);

	    // go through each question
	    for (int mcQuestionIndex = 0; mcQuestionIndex < mcQuestions.getLength(); mcQuestionIndex++) {
		Element mcQuestion = (Element) mcQuestions.item(mcQuestionIndex);
		// create an element for QbQuestion
		Element questionReference = document.createElement("org.lamsfoundation.lams.tool.assessment.model.QuestionReference");
		questionReferences.appendChild(questionReference);

		// transform MCQ data into QB structure
		XMLUtil.addTextElement(questionReference, "sequenceId", String.valueOf(mcQuestionIndex+1));
		XMLUtil.addTextElement(questionReference, "maxMark", "1");
		XMLUtil.addTextElement(questionReference, "randomQuestion", "false");
		
		Element questionReferenceQuestion = document.createElement("question");
		String questionReferenceCount = mcQuestionIndex == 0 ? "" : "[" + (mcQuestionIndex + 1) + "]";
		questionReferenceQuestion.setAttribute("reference",
			"../../../questions/org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion"
				+ questionReferenceCount);
		questionReference.appendChild(questionReferenceQuestion);
	    }
	});
    }
}

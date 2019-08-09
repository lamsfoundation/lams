package org.lamsfoundation.lams.tool.mc.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McOptsContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;
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
public class McImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 1.0 version content to 1.1 version tool server.
     *
     */
    public void up10To20061015() {
	this.removeField(McQueContent.class, "weight");
	this.removeField(McUsrAttempt.class, "timeZone");

    }

    public void up20061015To20061113() {
	// Change name to suit the version you give the tool.
	this.addField(McContent.class, "showMarks", "false");
	this.addField(McContent.class, "randomize", "false");
	this.addField(McOptsContent.class, "displayOrder", "0");
    }

    public void up20061113To20070820() {
	// Adds displayAnswers LDEV-1156
	this.addField(McContent.class, "displayAnswers", "true");
    }

    /**
     * Import 20140101 version content to 20140102 version tool server.
     */
    public void up20140101To20140102() {
	this.removeField(McContent.class, "runOffline");
	this.removeField(McContent.class, "onlineInstructions");
	this.removeField(McContent.class, "offlineInstructions");
	this.removeField(McContent.class, "mcAttachments");
    }

    /**
     * Import 20131212 version content to 20140512 version tool server.
     */
    public void up20140102To20140505() {
	this.removeField(McContent.class, "contentInUse");
	this.removeField(McQueContent.class, "mcUsrAttempts");
	this.removeField(McOptsContent.class, "mcUsrAttempts");
    }

    public void up20180724To20181202() {
	this.renameClass("org.lamsfoundation.lams.tool.mc.pojos.", "org.lamsfoundation.lams.tool.mc.model.");
    }

    /**
     * Import 20190110 version content to 20190517 version tool server.
     */
    public void up20181202To20190517() {
	this.removeField(McQueContent.class, "questionHash");
    }

    public void up20190517To20190809(String toolFilePath) throws IOException {
	// tell which file to process and what to do with its root element
	transformXML(toolFilePath, root -> {
	    Document document = root.getOwnerDocument();

	    // first find questions
	    NodeList mcQuestions = root.getElementsByTagName("org.lamsfoundation.lams.tool.mc.model.McQueContent");
	    if (mcQuestions.getLength() == 0) {
		return;
	    }

	    // get create date from MCQ content rather than from each question separately
	    String createDate = XMLUtil.getChildElementValue(root, "createDate", null);
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
		XMLUtil.addTextElement(qbQuestion, "createDate", createDate);
		XMLUtil.rewriteTextElement(mcQuestion, qbQuestion, "mark", "maxMark", "1");
		XMLUtil.rewriteTextElement(mcQuestion, qbQuestion, "feedback", "feedback", null);
		String description = XMLUtil.rewriteTextElement(mcQuestion, qbQuestion, "question", "description",
			null);
		// get name out of description as there were no descriptions in MCQ before
		if (description != null) {
		    description = description.trim();
		    XMLUtil.addTextElement(qbQuestion, "name",
			    description.substring(0, Math.min(description.length(), 80)));
		}

		// now it's time for options
		NodeList mcOptions = mcQuestion
			.getElementsByTagName("org.lamsfoundation.lams.tool.mc.model.McOptsContent");
		if (mcOptions.getLength() == 0) {
		    continue;
		}

		Element qbOptions = document.createElement("qbOptions");
		qbQuestion.appendChild(qbOptions);
		for (int mcOptionIndex = 0; mcOptionIndex < mcQuestions.getLength(); mcOptionIndex++) {
		    Element mcOption = (Element) mcOptions.item(mcOptionIndex);
		    Element qbOption = document.createElement("org.lamsfoundation.lams.qb.model.QbOption");
		    qbOptions.appendChild(qbOption);

		    // MCQ had correct/incorrect indicator. In QB we have 1/0 max mark
		    boolean correctOption = Boolean
			    .valueOf(XMLUtil.getChildElementValue(mcOption, "correctOption", "false"));
		    XMLUtil.addTextElement(qbOption, "maxMark", correctOption ? "1" : "0");

		    XMLUtil.rewriteTextElement(mcOption, qbOption, "displayOrder", "displayOrder", "1");
		    XMLUtil.rewriteTextElement(mcOption, qbOption, "mcQueOptionText", "name", null);
		}

		// get rid of junk
		mcQuestion.removeChild(mcOptions.item(0).getParentNode());
	    }
	});
    }
}
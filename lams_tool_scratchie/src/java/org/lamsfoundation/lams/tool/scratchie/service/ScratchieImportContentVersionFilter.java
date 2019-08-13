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

package org.lamsfoundation.lams.tool.scratchie.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.tool.scratchie.dto.QbOptionDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Import filter class for different version of Scratchie content.
 *
 */
public class ScratchieImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 20131130 version content to 20131212 version tool server.
     */
    public void up20131130To20131212() {
	this.removeField(ScratchieUser.class, "totalAttempts");
	this.removeField(ScratchieUser.class, "scratchingFinished");
	this.removeField(ScratchieUser.class, "mark");

	this.removeField(Scratchie.class, "createdBy");

	this.removeField(QbOptionDTO.class, "scratchieItem");

	this.addField(ScratchieSession.class, "mark", "0");
	this.addField(ScratchieSession.class, "scratchingFinished", "0");
    }

    /**
     * Import 20131212 version content to 20140102 version tool server.
     */
    public void up20131212To20140102() {
	this.removeField(Scratchie.class, "runOffline");
	this.removeField(Scratchie.class, "onlineInstructions");
	this.removeField(Scratchie.class, "offlineInstructions");
	this.removeField(Scratchie.class, "attachments");
    }

    /**
     * Import 20131212 version content to 20140102 version tool server.
     */
    public void up20140102To20140505() {
	this.removeField(Scratchie.class, "contentInUse");
    }

    /**
     * Import 20140613 version content to 20150206 version tool server.
     */
    public void up20140613To20150206() {
	this.addField(Scratchie.class, "burningQuestionsEnabled", "1");
    }

    /**
     * Import 20140613 version content to 20150206 version tool server.
     */
    public void up20180425To20180828() {
	this.removeField(ScratchieItem.class, "correctAnswer");
	this.removeField(ScratchieItem.class, "firstChoiceAnswerLetter");
	this.removeField(ScratchieItem.class, "userMark");
	this.removeField(ScratchieItem.class, "userAttempts");
    }

    public void up20190103To20190809(String toolFilePath) throws IOException {
	// tell which file to process and what to do with its root element
	transformXML(toolFilePath, root -> {
	    Document document = root.getOwnerDocument();

	    // first find questions
	    NodeList scratchieQuestions = root
		    .getElementsByTagName("org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem");
	    if (scratchieQuestions.getLength() == 0) {
		return;
	    }

	    // go through each question
	    for (int scratchieQuestionIndex = 0; scratchieQuestionIndex < scratchieQuestions
		    .getLength(); scratchieQuestionIndex++) {
		Element scratchieQuestion = (Element) scratchieQuestions.item(scratchieQuestionIndex);
		// create an element for QbQuestion
		Element qbQuestion = document.createElement("qbQuestion");
		scratchieQuestion.appendChild(qbQuestion);

		// transform Scratchie data into QB structure
		XMLUtil.addTextElement(qbQuestion, "type", "1");
		// Question ID will be filled later as it requires QbService
		XMLUtil.addTextElement(qbQuestion, "version", "1");
		XMLUtil.rewriteTextElement(scratchieQuestion, qbQuestion, "createDate", "createDate",
			new SimpleDateFormat(DateUtil.EXPORT_LD_FORMAT).format(new Date()), true, true);
		XMLUtil.rewriteTextElement(scratchieQuestion, qbQuestion, "title", "name", null, false, true,
			QbUtils.QB_MIGRATION_CKEDITOR_CLEANER, QbUtils.QB_MIGRATION_TAG_CLEANER);
		XMLUtil.rewriteTextElement(scratchieQuestion, qbQuestion, "description", "description", null, false,
			true, QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		XMLUtil.rewriteTextElement(scratchieQuestion, scratchieQuestion, "orderId", "displayOrder", null, false,
			true);

		// now it's time for options
		NodeList scratchieOptions = scratchieQuestion
			.getElementsByTagName("org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer");
		if (scratchieOptions.getLength() == 0) {
		    continue;
		}

		Element qbOptions = document.createElement("qbOptions");
		qbQuestion.appendChild(qbOptions);
		int maxDisplayOrder = 0;
		for (int scratchieOptionIndex = 0; scratchieOptionIndex < scratchieOptions
			.getLength(); scratchieOptionIndex++) {
		    Element scratchieOption = (Element) scratchieOptions.item(scratchieOptionIndex);
		    Element qbOption = document.createElement("org.lamsfoundation.lams.qb.model.QbOption");
		    qbOptions.appendChild(qbOption);

		    // Scratchie had correct/incorrect indicator. In QB we have 1/0 max mark
		    boolean correctOption = Boolean
			    .valueOf(XMLUtil.getChildElementValue(scratchieOption, "correct", "false"));
		    XMLUtil.addTextElement(qbOption, "maxMark", correctOption ? "1" : "0");

		    maxDisplayOrder = Math.max(Integer.valueOf(XMLUtil.rewriteTextElement(scratchieOption, qbOption,
			    "orderId", "displayOrder", String.valueOf(maxDisplayOrder + 1))), maxDisplayOrder);

		    XMLUtil.rewriteTextElement(scratchieOption, qbOption, "description", "name", null, false, false,
			    QbUtils.QB_MIGRATION_CKEDITOR_CLEANER);
		}

		// get rid of junk
		scratchieQuestion.removeChild(scratchieOptions.item(0).getParentNode());
		XMLUtil.removeElement(scratchieQuestion, "isCreateByAuthor");
	    }
	});
    }
}

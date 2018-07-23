/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.tool.qa.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.tool.qa.QaConfigItem;
import org.lamsfoundation.lams.tool.qa.QaWizardCategory;
import org.lamsfoundation.lams.tool.qa.QaWizardCognitiveSkill;
import org.lamsfoundation.lams.tool.qa.QaWizardQuestion;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.web.form.QaAdminForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.SunUnsafeReflectionProvider;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * Handles the admin page for question and answer which includes the settings
 * and items for the q&a question wizard
 *
 * @author lfoxton
 *
 *
 */
@Controller
@RequestMapping("/laqa11admin")
public class QaAdminController {

    private static Logger logger = Logger.getLogger(QaAdminController.class.getName());

    public static final String ATTR_CATEGORIES = "categories";
    public static final String ATTR_CATEGORY = "category";
    public static final String ATTR_QUESTION = "question";
    public static final String ATTR_SKILL = "skill";
    public static final String ATTR_TITLE = "title";
    public static final String ATTR_UID = "uid";
    public static final String NULL = "null";
    public static final String FILE_EXPORT = "qa-wizard.xml";

    @Autowired
    private IQaService qaService;

    /**
     * Sets up the admin page
     */
    @RequestMapping("/")
    public String unspecified(QaAdminForm adminForm, HttpServletRequest request) {

	QaConfigItem enableQaWizard = qaService.getConfigItem(QaConfigItem.KEY_ENABLE_QAWIZARD);
	if (enableQaWizard != null) {
	    adminForm.setQaWizardEnabled(enableQaWizard.getConfigValue());
	}

	request.setAttribute("error", false);
	request.setAttribute(ATTR_CATEGORIES, getQaWizardCategories());

	return "admin/config";
    }

    /**
     * Saves admin page, if the wizard is enabled, saves the wizard content
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/saveContent")
    public String saveContent(QaAdminForm adminForm, HttpServletRequest request) {

	QaConfigItem enableQaWizard = qaService.getConfigItem(QaConfigItem.KEY_ENABLE_QAWIZARD);

	if (adminForm.getQaWizardEnabled() != null && adminForm.getQaWizardEnabled()) {
	    enableQaWizard.setConfigValue(QaAdminForm.TRUE);

	    // get the wizard content and save
	    if (adminForm.getSerialiseXML() != null && !adminForm.getSerialiseXML().trim().equals("")) {
		updateWizardFromXML(adminForm.getSerialiseXML().trim());
	    }

	    // remove any wizard items that were removed
	    removeWizardItems(adminForm.getDeleteCategoriesCSV(), adminForm.getDeleteSkillsCSV(),
		    adminForm.getDeleteQuestionsCSV());
	} else {
	    enableQaWizard.setConfigValue(QaAdminForm.FALSE);
	}
	qaService.saveOrUpdateConfigItem(enableQaWizard);

	request.setAttribute(ATTR_CATEGORIES, getQaWizardCategories());
	request.setAttribute("savedSuccess", true);
	return "admin/config";

    }

    /**
     * Gets the complete set of wizard categories
     *
     * @return
     */
    public SortedSet<QaWizardCategory> getQaWizardCategories() {
	return qaService.getWizardCategories();
    }

    /**
     * Removes all the removed wizard items from the db using CSV values
     *
     * @param categoriesCSV
     * @param skillsCSV
     * @param questionsCSV
     */
    public void removeWizardItems(String categoriesCSV, String skillsCSV, String questionsCSV) {

	// remove categories
	if (categoriesCSV != null && !categoriesCSV.equals("")) {
	    String categoryUIDs[] = categoriesCSV.split(",");
	    for (int i = 0; i < categoryUIDs.length; i++) {
		qaService.deleteWizardCategoryByUID(Long.parseLong(categoryUIDs[i]));
	    }
	}

	// remove skills
	if (skillsCSV != null && !skillsCSV.equals("")) {
	    String skillUIDs[] = skillsCSV.split(",");
	    for (int i = 0; i < skillUIDs.length; i++) {
		qaService.deleteWizardSkillByUID(Long.parseLong(skillUIDs[i]));
	    }
	}

	// remove questions
	if (questionsCSV != null && !questionsCSV.equals("")) {
	    String questionUIDs[] = questionsCSV.split(",");
	    for (int i = 0; i < questionUIDs.length; i++) {
		qaService.deleteWizardQuestionByUID(Long.parseLong(questionUIDs[i]));
	    }
	}
    }

    /**
     * Saves all the wizard items from the xml serialisation sent from the form
     *
     * @param xmlStr
     */
    @SuppressWarnings("unchecked")
    public void updateWizardFromXML(String xmlStr) {
	//SortedSet<QaWizardCategory> currentCategories = getQaWizardCategories();
	SortedSet<QaWizardCategory> newCategories = new TreeSet<>();
	try {
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(new InputSource(new StringReader(xmlStr)));

	    // Get a list of category nodes
	    NodeList categoryNodeList = document.getElementsByTagName(ATTR_CATEGORY);

	    for (int i = 0; i < categoryNodeList.getLength(); i++) {

		Element categoryElement = (Element) categoryNodeList.item(i);

		// Get the attributes for this category
		NamedNodeMap categoryNamedNode = categoryNodeList.item(i).getAttributes();

		QaWizardCategory category = new QaWizardCategory();
		category.setTitle(categoryNamedNode.getNamedItem(ATTR_TITLE).getNodeValue());
		category.setCognitiveSkills(new TreeSet<QaWizardCognitiveSkill>());

		if (categoryNamedNode.getNamedItem(ATTR_UID).getNodeValue() != null
			&& !categoryNamedNode.getNamedItem(ATTR_UID).getNodeValue().equals(NULL)) {
		    category.setUid(Long.parseLong(categoryNamedNode.getNamedItem(ATTR_UID).getNodeValue()));
		}

		// Get a list of cognitive skill nodes
		NodeList skillNodeList = categoryElement.getElementsByTagName(ATTR_SKILL);
		for (int j = 0; j < skillNodeList.getLength(); j++) {
		    Element skillElement = (Element) skillNodeList.item(j);

		    // Get the attributes for this skill
		    NamedNodeMap skillNamedNode = skillNodeList.item(j).getAttributes();

		    // Create the skill and add attributes from the node
		    QaWizardCognitiveSkill skill = new QaWizardCognitiveSkill();
		    skill.setCategory(category);
		    skill.setTitle(skillNamedNode.getNamedItem(ATTR_TITLE).getNodeValue());
		    skill.setQuestions(new TreeSet<QaWizardQuestion>());

		    if (skillNamedNode.getNamedItem(ATTR_UID).getNodeValue() != null
			    && !skillNamedNode.getNamedItem(ATTR_UID).getNodeValue().equals(NULL)) {
			skill.setUid(Long.parseLong(skillNamedNode.getNamedItem(ATTR_UID).getNodeValue()));
		    }

		    // add the skill to the parent category
		    category.getCognitiveSkills().add(skill);

		    // Get a list of questions for this skill
		    NodeList questionNodeList = skillElement.getElementsByTagName(ATTR_QUESTION);
		    for (int k = 0; k < questionNodeList.getLength(); k++) {
			// Get the attributes for this question
			NamedNodeMap questionNamedNode = questionNodeList.item(k).getAttributes();

			// Create the question, and add attributes from the node
			QaWizardQuestion question = new QaWizardQuestion();
			question.setQuestion(questionNamedNode.getNamedItem(ATTR_QUESTION).getNodeValue());

			if (questionNamedNode.getNamedItem(ATTR_UID).getNodeValue() != null
				&& !questionNamedNode.getNamedItem(ATTR_UID).getNodeValue().equals(NULL)) {
			    question.setUid(Long.parseLong(questionNamedNode.getNamedItem(ATTR_UID).getNodeValue()));
			}

			// add the question to the parent cognitive skill
			skill.getQuestions().add(question);
		    }
		}
		newCategories.add(category);
	    }

	} catch (ParserConfigurationException e) {
	    logger.error("Could not parse wizard serialise xml", e);
	} catch (SAXException e) {
	    logger.error("Could not parse wizard serialise xml", e);
	} catch (IOException e) {
	    logger.error("Could not parse wizard serialise xml", e);
	}

	qaService.saveOrUpdateQaWizardCategories(newCategories);
    }

    /**
     * Exports the wizard categories list so it can be imported elsewhere The
     * export format is the same xml format used by the export ld servlet
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportWizard")
    public String exportWizard(HttpServletResponse response) throws Exception {

	// now start the export
	SortedSet<QaWizardCategory> exportCategories = new TreeSet<>();
	for (QaWizardCategory category : getQaWizardCategories()) {
	    exportCategories.add((QaWizardCategory) category.clone());
	}

	// exporting XML
	XStream designXml = new XStream(new SunUnsafeReflectionProvider());
	designXml.addPermission(AnyTypePermission.ANY);
	String exportXml = designXml.toXML(exportCategories);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + FILE_EXPORT);
	OutputStream out = null;
	try {
	    out = response.getOutputStream();
	    out.write(exportXml.getBytes());
	    response.setContentLength(exportXml.getBytes().length);
	    out.flush();
	} catch (Exception e) {
	    log.error("Exception occured writing out file:" + e.getMessage());
	    throw new ExportToolContentException(e);
	} finally {
	    try {
		if (out != null) {
		    out.close();
		}
	    } catch (Exception e) {
		log.error("Error Closing file. File already written out - no exception being thrown.", e);
	    }
	}

	return null;
    }

    /**
     * Imports the wizard model from an xml file and replaces the current model
     * First, saves the configurations, then performs the import using xstream
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    public ActionForward importWizard(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	QaAdminForm adminForm = (QaAdminForm) form;

	// First save the config items
	QaConfigItem enableQaWizard = qaService.getConfigItem(QaConfigItem.KEY_ENABLE_QAWIZARD);

	if (adminForm.getQaWizardEnabled() != null && adminForm.getQaWizardEnabled()) {
	    enableQaWizard.setConfigValue(QaAdminForm.TRUE);

	    // get the wizard content and save
	    if (adminForm.getSerialiseXML() != null && !adminForm.getSerialiseXML().trim().equals("")) {
		updateWizardFromXML(adminForm.getSerialiseXML().trim());
	    }

	    // remove any wizard items that were removed
	    removeWizardItems(adminForm.getDeleteCategoriesCSV(), adminForm.getDeleteSkillsCSV(),
		    adminForm.getDeleteQuestionsCSV());
	} else {
	    enableQaWizard.setConfigValue(QaAdminForm.FALSE);
	}
	qaService.saveOrUpdateConfigItem(enableQaWizard);

	// Now perform the import
	try {
	    String xml = new String(adminForm.getImportFile().getFileData());
	    XStream conversionXml = new XStream(new SunUnsafeReflectionProvider());
	    conversionXml.addPermission(AnyTypePermission.ANY);
	    SortedSet<QaWizardCategory> exportCategories = (SortedSet<QaWizardCategory>) conversionXml.fromXML(xml);

	    qaService.deleteAllWizardCategories();
	    qaService.saveOrUpdateQaWizardCategories(exportCategories);
	} catch (Exception e) {
	    logger.error("Failed to import wizard model", e);
	    request.setAttribute("error", true);
	    request.setAttribute("errorKey", "wizard.import.error");
	    request.setAttribute(ATTR_CATEGORIES, getQaWizardCategories());
	    return mapping.findForward("config");
	}

	request.setAttribute(ATTR_CATEGORIES, getQaWizardCategories());
	request.setAttribute("savedSuccess", true);
	return mapping.findForward("config");

    }

}

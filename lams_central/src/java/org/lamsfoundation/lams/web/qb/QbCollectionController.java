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

package org.lamsfoundation.lams.web.qb;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.outcome.service.IOutcomeService;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Controller
@RequestMapping("/qb/collection")
public class QbCollectionController {
    private static Logger log = Logger.getLogger(QbCollectionController.class);

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @Autowired
    private IQbService qbService;

    @Autowired
    private IOutcomeService outcomeService;

    @RequestMapping("/show")
    public String showUserCollections(Model model) throws Exception {
	Integer userId = getUserId();

	Collection<QbCollection> collections = qbService.getUserCollections(userId);
	model.addAttribute("collections", collections);

	Map<Long, Integer> questionCount = collections.stream().collect(
		Collectors.toMap(QbCollection::getUid, c -> qbService.getCountCollectionQuestions(c.getUid(), null)));
	model.addAttribute("questionCount", questionCount);

	model.addAttribute("createCollectionAllowed",
		Configuration.getAsBoolean(ConfigurationKeys.QB_COLLECTIONS_CREATE_ALLOW));

	return "qb/collectionList";
    }

    @RequestMapping("/showOne")
    public String showOneCollection(@RequestParam long collectionUid, Model model) throws Exception {
	model.addAttribute("collection", qbService.getCollection(collectionUid));
	model.addAttribute("availableOrganisations",
		qbService.getShareableWithOrganisations(collectionUid, getUserId()));
	model.addAttribute("questionCount", qbService.getCountCollectionQuestions(collectionUid, null));
	model.addAttribute("isQtiExportEnabled", Configuration.getAsBoolean(ConfigurationKeys.QB_QTI_ENABLE));
	return "qb/collection";
    }

    @RequestMapping(path = "/getCollectionGridData")
    @ResponseBody
    public String getCollectionGridData(@RequestParam long collectionUid, @RequestParam String view,
	    HttpServletRequest request, HttpServletResponse response) {
	response.setContentType("application/xml;charset=UTF-8");

	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, CommonConstants.PARAM_SEARCH);
	String searchString = isSearch ? WebUtil.readStrParam(request, "name", true) : null;

	int offset = (page - 1) * rowLimit;
	List<QbQuestion> questions = qbService.getCollectionQuestions(collectionUid, offset, rowLimit, sortBy,
		sortOrder, searchString);
	int total = qbService.getCountCollectionQuestions(collectionUid, searchString);
	int maxPages = total / rowLimit + 1;
	return toGridXML(questions, page, maxPages, total, view);
    }

    @RequestMapping(path = "/getQuestionVersionGridData", produces = "application/xml;charset=utf-8")
    @ResponseBody
    public String getQuestionVersionGridData(@RequestParam int qbQuestionId, @RequestParam String view,
	    HttpServletResponse response) {
	response.setContentType("application/xml;charset=UTF-8");

	List<QbQuestion> questions = qbService.getQuestionsByQuestionId(qbQuestionId);
	questions = questions.subList(1, questions.size());
	return toGridXML(questions, 1, 1, questions.size(), view);
    }

    private String toGridXML(List<QbQuestion> questions, int page, int maxPages, int totalCount, String view) {
	try {
	    Document document = WebUtil.getDocument();

	    // root element
	    Element rootElement = document.createElement(CommonConstants.ELEMENT_ROWS);

	    Element pageElement = document.createElement(CommonConstants.ELEMENT_PAGE);
	    pageElement.appendChild(document.createTextNode(String.valueOf(page)));
	    rootElement.appendChild(pageElement);

	    Element totalPageElement = document.createElement(CommonConstants.ELEMENT_TOTAL);
	    totalPageElement.appendChild(document.createTextNode(String.valueOf(maxPages)));
	    rootElement.appendChild(totalPageElement);

	    Element recordsElement = document.createElement(CommonConstants.ELEMENT_RECORDS);
	    recordsElement.appendChild(document.createTextNode(String.valueOf(totalCount)));
	    rootElement.appendChild(recordsElement);

	    for (QbQuestion question : questions) {
		String uid = question.getUid().toString();
		Element rowElement = document.createElement(CommonConstants.ELEMENT_ROW);
		rowElement.setAttribute(CommonConstants.ELEMENT_ID, uid);

		// the last cell is for creating stats button
		String usage = !view.equalsIgnoreCase("list") ? String.valueOf(
			view.equalsIgnoreCase("version") ? qbService.getCountQuestionActivitiesByUid(question.getUid())
				: qbService.getCountQuestionActivitiesByQuestionId(question.getQuestionId()))
			: null;
		boolean hasVersions = qbService.countQuestionVersions(question.getQuestionId()) > 1;
		String learningOutcomes = view.equalsIgnoreCase("single")
			? outcomeService.getOutcomeMappings(null, null, null, question.getQuestionId()).stream()
				.map(m -> m.getOutcome().getName()).collect(Collectors.joining("<br>"))
			: null;

		String[] data = { question.getQuestionId().toString(),
			WebUtil.removeHTMLtags(question.getName()).trim(),
			view.equalsIgnoreCase("version") ? null : question.getType().toString(),
			question.getVersion().toString(), learningOutcomes, usage, uid, String.valueOf(hasVersions) };

		for (String cell : data) {
		    Element cellElement = document.createElement(CommonConstants.ELEMENT_CELL);
		    if (cell == null) {
			cell = "";
		    }
		    cellElement.appendChild(document.createTextNode(cell));
		    rowElement.appendChild(cellElement);
		}
		rootElement.appendChild(rowElement);
	    }

	    document.appendChild(rootElement);

	    return WebUtil.getStringFromDocument(document);

	} catch (Exception e) {
	    log.error("Error while generating Question Bank collection jqGrid XML data", e);
	}

	return null;
    }

    @RequestMapping(path = "/removeCollectionQuestion", method = RequestMethod.POST)
    @ResponseBody
    public void removeCollectionQuestion(@RequestParam long collectionUid, @RequestParam int qbQuestionId) {
	qbService.removeQuestionFromCollectionByQuestionId(collectionUid, qbQuestionId, true);
    }

    @RequestMapping(path = "/removeCollectionQuestions", method = RequestMethod.POST)
    @ResponseBody
    public String removeCollectionQuestionS(@RequestParam long collectionUid,
	    @RequestParam("qbQuestionIds[]") int[] qbQuestionIds) {
	boolean allQuestionsRemoved = true;
	for (int qbQuestionId : qbQuestionIds) {
	    allQuestionsRemoved &= qbService.removeQuestionFromCollectionByQuestionId(collectionUid, qbQuestionId,
		    true);
	}
	// if some questions could not be removed because they are in sequences, we need to let the user know
	return allQuestionsRemoved ? "ok" : "fail";
    }

    @RequestMapping(path = "/addCollectionQuestion", method = RequestMethod.POST)
    @ResponseBody
    public void addCollectionQuestion(@RequestParam long targetCollectionUid, @RequestParam boolean copy,
	    @RequestParam int qbQuestionId) {
	if (!Configuration.getAsBoolean(ConfigurationKeys.QB_COLLECTIONS_TRANSFER_ALLOW)) {
	    throw new SecurityException("Transfering questions between collections is disabled");
	}
	qbService.addQuestionToCollection(targetCollectionUid, qbQuestionId, copy);
    }

    @RequestMapping(path = "/addCollection", method = RequestMethod.POST)
    @ResponseBody
    public void addCollection(@RequestParam String name) {
	if (!Configuration.getAsBoolean(ConfigurationKeys.QB_COLLECTIONS_CREATE_ALLOW)) {
	    throw new SecurityException("New collections are disabled");
	}
	qbService.addCollection(getUserId(), name);
    }

    @RequestMapping(path = "/changeCollectionName", method = RequestMethod.POST)
    @ResponseBody
    public String changeCollectionName(@RequestParam(name = "pk") long collectionUid,
	    @RequestParam(name = "value") String name) {
	Collection<QbCollection> collections = qbService.getUserCollections(getUserId());
	name = name.trim();
	for (QbCollection collection : collections) {
	    if (collection.getUid().equals(collectionUid)) {
		if (collection.getUserId() == null) {
		    // can not change public collection name
		    return "false";
		}
	    } else if (name.equalsIgnoreCase(collection.getName())) {
		// a collection with the same name already exists
		return "false";
	    }
	}
	qbService.changeCollectionName(collectionUid, name);
	return "true";
    }

    @RequestMapping(path = "/removeCollection", method = RequestMethod.POST)
    @ResponseBody
    public void removeCollection(@RequestParam long collectionUid) {
	qbService.removeCollection(collectionUid);
    }

    @RequestMapping(path = "/shareCollection", method = RequestMethod.POST)
    @ResponseBody
    public void shareCollection(@RequestParam long collectionUid, @RequestParam int organisationId) {
	qbService.shareCollection(collectionUid, organisationId);
    }

    @RequestMapping(path = "/unshareCollection", method = RequestMethod.POST)
    @ResponseBody
    public void unshareCollection(@RequestParam long collectionUid, @RequestParam int organisationId) {
	qbService.unshareCollection(collectionUid, organisationId);
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}
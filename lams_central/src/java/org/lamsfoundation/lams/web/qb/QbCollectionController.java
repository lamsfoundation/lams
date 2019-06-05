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

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fasterxml.jackson.databind.node.ArrayNode;

@Controller
@RequestMapping("/qb/collection")
public class QbCollectionController {
    private static Logger log = Logger.getLogger(QbCollectionController.class);

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @Autowired
    private IQbService qbService;

    @RequestMapping("/show")
    public String showUserCollections(Model model) throws Exception {
	Integer userId = getUserId();
	List<QbCollection> collections = qbService.getUserCollections(userId);

	QbCollection privateCollection = qbService.getUserPrivateCollection(userId);
	collections.add(privateCollection);

	QbCollection publicCollection = qbService.getPublicCollection();
	collections.add(publicCollection);

	model.addAttribute("collections", collections);
	return "qb/collection";
    }

    @RequestMapping("/getCollectionGridData")
    @ResponseBody
    public String getCollectionGridData(@RequestParam long collectionUid, HttpServletRequest request,
	    HttpServletResponse response) {
	response.setContentType("text/xml; charset=utf-8");

	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, CommonConstants.PARAM_SEARCH);
	String searchString = isSearch ? WebUtil.readStrParam(request, "name", true) : null;

	int offset = (page - 1) * rowLimit;
	List<QbQuestion> questions = qbService.getCollectionQuestions(collectionUid, offset, rowLimit, sortBy,
		sortOrder, searchString);
	int total = qbService.countCollectionQuestions(collectionUid, searchString);
	int maxPages = total / rowLimit + 1;
	return QbCollectionController.toGridXML(questions, page, maxPages);
    }

    private static String toGridXML(List<QbQuestion> questions, int page, int maxPages) {
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
	    recordsElement.appendChild(document.createTextNode(String.valueOf(questions.size())));
	    rootElement.appendChild(recordsElement);

	    for (QbQuestion question : questions) {
		String uid = question.getUid().toString();
		Element rowElement = document.createElement(CommonConstants.ELEMENT_ROW);
		rowElement.setAttribute(CommonConstants.ELEMENT_ID, uid);

		String[] data = { uid, question.getName(), uid };

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

    @RequestMapping("/removeCollectionQuestions")
    @ResponseBody
    public void removeCollectionQuestions(@RequestParam long collectionUid, @RequestParam String included,
	    @RequestParam String excluded) throws IOException {
	if (StringUtils.isBlank(excluded)) {
	    ArrayNode includedJSON = JsonUtil.readArray(included);
	    for (int index = 0; index < includedJSON.size(); index++) {
		qbService.removeQuestionFromCollection(collectionUid, includedJSON.get(index).asLong());
	    }
	} else {
	    ArrayNode excludedJSON = JsonUtil.readArray(excluded);
	    Set<Long> excludedSet = new HashSet<>();
	    for (int index = 0; index < excludedJSON.size(); index++) {
		excludedSet.add(excludedJSON.get(index).asLong());
	    }
	    qbService.removeCollectionQuestion(collectionUid, excludedSet);
	}
    }

    @RequestMapping("/addCollection")
    @ResponseBody
    public void removeCollectionQuestions(@RequestParam String name) {
	qbService.addCollection(getUserId(), name);
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}
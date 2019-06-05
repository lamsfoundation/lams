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

import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
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
    public String getCollectionGridData(@RequestParam long collectionUid, HttpServletResponse response) {
	response.setContentType("text/xml; charset=utf-8");

	List<QbQuestion> questions = qbService.getCollectionQuestions(collectionUid, 0, 10);
	return QbCollectionController.toGridXML(questions);
    }

    private static String toGridXML(List<QbQuestion> questions) {
	try {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document document = builder.newDocument();

	    // root element
	    Element rootElement = document.createElement(CommonConstants.ELEMENT_ROWS);

	    Element pageElement = document.createElement(CommonConstants.ELEMENT_PAGE);
	    pageElement.appendChild(document.createTextNode("" + 1));
	    rootElement.appendChild(pageElement);

	    Element totalPageElement = document.createElement(CommonConstants.ELEMENT_TOTAL);
	    totalPageElement.appendChild(document.createTextNode("" + 1));
	    rootElement.appendChild(totalPageElement);

	    Element recordsElement = document.createElement(CommonConstants.ELEMENT_RECORDS);
	    recordsElement.appendChild(document.createTextNode("" + questions.size()));
	    rootElement.appendChild(recordsElement);

	    for (QbQuestion question : questions) {
		Element rowElement = document.createElement(CommonConstants.ELEMENT_ROW);
		rowElement.setAttribute(CommonConstants.ELEMENT_ID, question.getUid().toString());

		String[] data = { question.getUid().toString(), question.getName(), question.getUid().toString() };

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
	    DOMSource domSource = new DOMSource(document);
	    StringWriter writer = new StringWriter();
	    StreamResult result = new StreamResult(writer);
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.transform(domSource, result);
	    return writer.toString();

	} catch (Exception e) {
	    log.error("Error while generating Question Bank collection jqGrid XML data", e);
	}

	return null;
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}
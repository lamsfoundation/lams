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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.deploy;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Task to add a web application entry to an EAR application XML
 *
 * @author chris
 */
public class AddWebAppToApplicationXmlTask extends UpdateApplicationXmlTask {

    /** Creates a new instance of AddWebAppToApplicationXmlTask */
    public AddWebAppToApplicationXmlTask() {
    }

    /**
     * Add the web uri and context root elements ot the Application xml
     */
    @Override
    protected void updateApplicationXml(Document doc) throws DeployException {

	// find & remove web uri element
	Element moduleElement = findElementWithWebURI(doc);
	if (moduleElement != null) {
	    doc.getDocumentElement().removeChild(moduleElement);
	}

	// create new module
	moduleElement = doc.createElement("module");
	Element webElement = doc.createElement("web");
	moduleElement.appendChild(webElement);
	// create new web-uri element in the web element
	Element webUriElement = doc.createElement("web-uri");
	webUriElement.appendChild(doc.createTextNode(webUri));
	webElement.appendChild(webUriElement);

	// create new context root element in the web element
	Element contextRootElement = doc.createElement("context-root");
	contextRootElement.appendChild(doc.createTextNode(contextRoot));
	webElement.appendChild(contextRootElement);

	// insert new module in the correct position, before "security-role" elements

	NodeList docNodes = doc.getDocumentElement().getChildNodes();
	for (int nodeIndex = 0; nodeIndex < docNodes.getLength(); nodeIndex++) {
	    Node node = docNodes.item(nodeIndex);
	    if ("security-role".equalsIgnoreCase(node.getNodeName())) {
		doc.getDocumentElement().insertBefore(moduleElement, node);
		break;
	    }
	}
    }
}
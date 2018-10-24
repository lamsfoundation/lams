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

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Base class of ant tasks that change the application xml
 * 
 * @author chris
 */
public abstract class UpdateApplicationXmlTask implements Task {

    /**
     * The application.xml file.
     */
    protected String lamsEarPath;

    /**
     * The value of the web-uri element.
     */
    protected String webUri;

    /**
     * The value of the module element
     */
    protected String module;

    /**
     * The value of the context root element.
     */
    protected String contextRoot;

    private String applicationXmlPath;

    /** Creates a new instance of UpdateApplicationXmlTask */
    public UpdateApplicationXmlTask() {
    }

    /**
     * Sets the location of the application xml file to be modified.
     * 
     * @param appxml
     *            New value of property appxml.
     */
    public void setLamsEarPath(final String lamsEarPath) {
	this.lamsEarPath = lamsEarPath;
	this.applicationXmlPath = lamsEarPath + "/META-INF/application.xml";
    }

    /**
     * Sets the uri of the web app to be added.
     * 
     * @param weburi
     *            New value of property weburi.
     */
    public void setWebUri(final java.lang.String webUri) {
	this.webUri = webUri;
    }

    /**
     * Sets the value of the module to be added
     * 
     * @param module
     *            New value of the property module
     */
    public void setModule(final java.lang.String module) {
	this.module = module;
    }

    /**
     * Sets the context root of the web app to be added.
     * 
     * @param contextroot
     *            New value of property contextroot.
     */
    public void setContextRoot(final java.lang.String contextRoot) {
	this.contextRoot = contextRoot;
    }

    /**
     * Execute the task
     * 
     * @throws org.apache.tools.ant.DeployException
     *             In case of any problems
     */
    @Override
    public void execute() throws DeployException {
	Document doc = parseApplicationXml();

	updateApplicationXml(doc);

	writeApplicationXml(doc);
    }

    /**
     * Writes the modified application back out to the file system.
     * 
     * @param doc
     *            The application.xml DOM Document
     * @throws org.apache.tools.ant.DeployException
     *             in case of any problems
     */
    protected void writeApplicationXml(final Document doc) throws DeployException {
	System.out.println("Writing out doc " + doc);
	try {
	    doc.normalize();

	    // Prepare the DOM document for writing
	    DOMSource source = new DOMSource(doc);

	    // Prepare the output file
	    StreamResult result = new StreamResult(applicationXmlPath);

	    // Write the DOM document to the file
	    // Get Transformer
	    Transformer xformer = TransformerFactory.newInstance().newTransformer();
	    // Write to a file
	    xformer.transform(source, result);
	} catch (TransformerException tex) {
	    throw new DeployException("Error writing out modified application xml", tex);
	}
    }

    /**
     * Finds the web element in the application xml
     * 
     * @param doc
     *            The DOM Document to search
     * @throws org.apache.tools.ant.DeployException
     *             In case of errors
     * @return the web DOM Element
     */
    protected Element findWebElement(final Document doc) throws DeployException {
	//get <web> Element
	NodeList webNodesList = doc.getElementsByTagName("web");
	if ((webNodesList == null) || (webNodesList.getLength() < 1)) {
	    throw new DeployException("LAMS Application XML does not have a web element");
	} else if (webNodesList.getLength() > 1) {
	    throw new DeployException("LAMS Application XML should only have one web element");
	}
	return (Element) webNodesList.item(0);
    }

    /**
     * Parses the application xml into a Dom document
     * 
     * @throws org.apache.tools.ant.DeployException
     *             in case of errors
     * @return A DOM Document of the application xml
     */
    protected Document parseApplicationXml() throws DeployException {
	try {
	    //get application xml as dom doc
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    return builder.parse(applicationXmlPath);
	} catch (ParserConfigurationException pex) {
	    throw new DeployException("Could not configure parser", pex);
	} catch (SAXException saxex) {
	    throw new DeployException("Error parsing application xml", saxex);
	} catch (IOException ioex) {
	    throw new DeployException("Error reading application xml", ioex);
	}
    }

    /**
     * Finds an element with matching text content in the NodeList
     * 
     * @param text
     *            the text to match
     * @param nodeList
     *            the nodeList to look in
     * @throws org.apache.tools.ant.DeployException
     *             in case of errors
     * @return the matching Element
     */
    protected Element findElementWithMatchingText(String text, NodeList nodeList) throws DeployException {
	Element matchedElement = null;
	for (int i = 0, length = nodeList.getLength(); i < length; i++) {
	    Node node = nodeList.item(i);
	    if (node instanceof Element) {

		if (node instanceof Element) {
		    NodeList children = node.getChildNodes();
		    if ((children != null) && (children.getLength() > 0)) {
			if ((children.item(0) instanceof Text) && (children.item(0).getNodeValue().equals(text))) {
			    matchedElement = (Element) node;
			    break;
			}
		    }
		}
		//                if ((node.getTextContent() != null)
		//                && (node.getTextContent().equals(text)))
		//                {
		//                    matchedElement = (Element) node;
		//                    break;
		//                }
	    }

	}

	return matchedElement;
    }

    /** Find a matching web element - useful for updating or deleting an existing element */
    protected Element findElementWithWebURI(Document doc) {
	NodeList webUriNodeList = doc.getElementsByTagName("web-uri");
	Element matchingWebUriElement = findElementWithMatchingText(webUri, webUriNodeList);
	if (matchingWebUriElement != null) {
	    return (Element) matchingWebUriElement.getParentNode().getParentNode();
	} else {
	    return null;
	}
    }

    /** Find a matching web element - useful for updating or deleting an existing element */
    protected Element findElementWithModule(Document doc) {
	NodeList moduleNodeList = doc.getElementsByTagName("java");
	Element matchingModuleElement = findElementWithMatchingText(module, moduleNodeList);
	if (matchingModuleElement != null) {
	    return (Element) matchingModuleElement.getParentNode();
	} else {
	    return null;
	}
    }

    /**
     * Modifies the application Xml in the required manner.
     * Abstract method to be implmented by subclasses.
     * 
     * @throws org.apache.tools.ant.DeployException
     *             in case of errors
     */
    protected abstract void updateApplicationXml(Document doc) throws DeployException;

}

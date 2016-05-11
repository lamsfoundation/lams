/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.rsrc.ims;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.FileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * SimpleContentPackageConverter contains the code required for parsing the IMS Content Package and converting the info
 * into our own structures.
 *
 * @author Fiona Malikoff, Marcin Cieslak
 */
public class SimpleContentPackageConverter {
    private static Logger log = Logger.getLogger(SimpleContentPackageConverter.class);

    private static DocumentBuilder docBuilder = null;
    private static final XPathFactory xPathFactory = XPathFactory.newInstance();
    private final XPath xPath = SimpleContentPackageConverter.xPathFactory.newXPath();

    private Document manifestDoc = null;

    private String schema = null;
    private String title = null;
    private String description = null;
    private String defaultItem = null;
    private String organzationXML = null;

    static {
	try {
	    // a single doc builder is enough
	    SimpleContentPackageConverter.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	} catch (ParserConfigurationException e) {
	    SimpleContentPackageConverter.log.error("Error while initialising XML document builder", e);
	}
    }

    public SimpleContentPackageConverter(String directoryName) throws IOException {
	// first parse the manifest XML file
	this.manifestDoc = getDocument(directoryName);

	// get the necessary data from the XML document using XPath
	String schemaText = getText("/manifest/metadata/schema/text()", manifestDoc);
	String schemaVersion = getText("/manifest/metadata/schemaversion/text()", manifestDoc);
	this.schema = (schemaText == null ? "unknown" : schemaText) + " "
		+ (schemaVersion == null ? "unknown" : schemaVersion);

	Document orgs = null;
	try {
	    orgs = buildOrganisationList();
	} catch (XPathExpressionException e) {
	    throw new IOException("Error while building organisations list", e);
	}
	if (orgs == null) {
	    throw new IOException("Unable to convert organisations from manifest file to our own structure");
	}
	this.organzationXML = FileUtil.writeXMLtoString(orgs);

	this.title = getText("/manifest/metadata/lom/general/title/langstring/text()", manifestDoc);
	this.description = getText("/manifest/metadata/lom/general/description/langstring/text()", manifestDoc);
    }

    /* Get the text for first element matching XPath */
    private String getText(String xPathString, Object context) throws IOException {
	try {
	    return xPath.evaluate(xPathString, context);
	} catch (XPathExpressionException e) {
	    throw new IOException("Error when parsing XPath expression: " + xPathString, e);
	}
    }

    private Document getDocument(String directoryName) throws IOException {
	File docFile = new File(directoryName, "imsmanifest.xml");
	Document doc;
	try {
	    doc = SimpleContentPackageConverter.docBuilder.parse(new FileInputStream(docFile));
	} catch (SAXException | IOException e) {
	    throw new IOException("Error while parsing IMS manifest", e);
	}
	return doc;
    }

    /**
     * Build an XML document which is a list of organisations/resources.
     */
    private Document buildOrganisationList() throws XPathExpressionException, IOException {
	Element orgsElem = (Element) xPath.evaluate("/manifest/organizations", manifestDoc, XPathConstants.NODE);

	// default org is for finding default item, but all orgs get processed
	String defaultOrgIdentifier = orgsElem.getAttribute("default");
	if (StringUtils.isBlank(defaultOrgIdentifier)) {
	    defaultOrgIdentifier = getText("organization/@identifier", orgsElem);
	}
	if (SimpleContentPackageConverter.log.isDebugEnabled()) {
	    SimpleContentPackageConverter.log.debug("Default organisation identifier is: " + defaultOrgIdentifier);
	}

	Document newDoc = SimpleContentPackageConverter.docBuilder.newDocument();
	Element newRootElement = newDoc.createElement("organizations");
	setAttribute(newRootElement, "version", "imscp1");
	newDoc.appendChild(newRootElement);

	NodeList orgs = (NodeList) xPath.evaluate("organization", orgsElem, XPathConstants.NODESET);
	for (int childIndex = 0; childIndex < orgs.getLength(); childIndex++) {
	    Element organizationElement = processItem(newDoc, (Element) orgs.item(childIndex), defaultOrgIdentifier,
		    null);
	    if (organizationElement != null) {
		newRootElement.appendChild(organizationElement);
	    }
	}

	if (SimpleContentPackageConverter.log.isDebugEnabled()) {
	    SimpleContentPackageConverter.log.debug("Organizations are: " + FileUtil.writeXMLtoString(newDoc));
	}

	return newDoc;
    }

    /**
     * Process the given element. Returns a newly formatted Element if the element is a visible item, null otherwise.
     * Will also set the value "defaultItemURL" while processing, if it finds the default item.
     *
     * First time through, the element will be an organization and parentOrgIdentifier will be null. After that,
     * elements are expected to be items and parentOrgIdentifier should not be null.
     *
     * parentOrgIdentifier is the parent organization of an item. An item is the default item if either: (a) the
     * defaultOrgIdentifier is null and it is the first item encountered which has a resource or (b)
     * parentOrgIdentifier==defaultOrgIdentifier and it is the first item encountered which has a resource.
     *
     * @param element
     * @return New version of element combining organization/item/resource details
     * @throws XPathExpressionException
     * @throws IOException
     */
    private Element processItem(Document doc, Element element, String defaultOrgIdentifier, String parentOrgIdentifier)
	    throws XPathExpressionException, IOException {
	// only process visible items as we are building the list for display to the user
	if (!isVisible(element.getAttribute("isVisible"))) {
	    return null;
	}
	String id = element.getAttribute("identifier");
	Element itref = doc.createElement("item");

	setAttribute(itref, "identifier", id); // mandatory
	setAttribute(itref, "parameters", element.getAttribute("parameters")); // optional
	setAttribute(itref, "title", getText("title/text()", element)); // optional
	String idRef = element.getAttribute("identifierref");
	// find matching resource
	String resourceURL = getText("/manifest/resources/resource[@identifier='" + idRef + "']/@href", manifestDoc);
	setAttribute(itref, "resource", resourceURL); // optional

	if (StringUtils.isNotBlank(resourceURL) && (this.defaultItem == null)
		&& ((defaultOrgIdentifier == null) || defaultOrgIdentifier.equals(parentOrgIdentifier))) {
	    setAttribute(itref, "default", Boolean.TRUE.toString());
	    this.defaultItem = resourceURL;
	} else {
	    setAttribute(itref, "default", Boolean.FALSE.toString());
	}

	NodeList items = (NodeList) xPath.evaluate("item", element, XPathConstants.NODESET);
	for (int childIndex = 0; childIndex < items.getLength(); childIndex++) {
	    Element itrefChild = processItem(doc, (Element) items.item(childIndex), defaultOrgIdentifier,
		    parentOrgIdentifier != null ? parentOrgIdentifier : id);
	    if (itrefChild != null) {
		itref.appendChild(itrefChild);
	    }
	}

	return itref;
    }

    /**
     * Not sure exactly what format "isVisible" will be in. In the spec, it says Boolean (True|False) yet the
     * imslipv1p0cp.zip package has a value of "1". So this code will accept anything as visible except for false (any
     * case) or 0.
     */
    private boolean isVisible(String value) {
	if (value != null) {
	    String trimmed = value.trim();
	    if (trimmed.equalsIgnoreCase("false") || trimmed.equals("0")) {
		return false;
	    }
	}
	return true;
    }

    private void setAttribute(Element element, String attributeName, String value) {
	if ((element != null) && StringUtils.isNotBlank(attributeName) && StringUtils.isNotBlank(value)) {
	    element.setAttribute(attributeName, value);
	}
    }

    /**
     * @return Returns the defaultItem.
     */
    public String getDefaultItem() {
	return defaultItem;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
	return description;
    }

    /**
     * @return Returns the organzationXML.
     */
    public String getOrganzationXML() {
	return organzationXML;
    }

    /**
     * @return Returns the schema.
     */
    public String getSchema() {
	return schema;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }
}
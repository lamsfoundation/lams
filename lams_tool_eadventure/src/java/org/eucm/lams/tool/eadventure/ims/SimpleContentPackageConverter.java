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

package org.eucm.lams.tool.eadventure.ims;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;

import uk.ac.reload.jdom.XMLDocument;
import uk.ac.reload.jdom.XMLPath;
import uk.ac.reload.jdom.XMLUtils;
import uk.ac.reload.moonunit.contentpackaging.CP_Core;

/**
 * SimpleContentPackageConverter contains the code required for
 * parsing the IMS Content Package and converting the info into our
 * own structures.
 *
 * Note: this class has instance data, so do not use it as a singleton.
 *
 * @author Fiona Malikoff
 */
public class SimpleContentPackageConverter implements IContentPackageConverter {

    private Logger log = Logger.getLogger(SimpleContentPackageConverter.class);

    // manifestDoc and cpCore are set up in the constructor. They are then used
    // to generate the rest of the values
    private XMLDocument manifestDoc = null;
    private CP_Core cpCore = null;

    private String schema = null;
    private String title = null;
    private String description = null;
    private String defaultItem = null;
    private String organzationXML = null;

    // cachedEadventureList is used to avoid building up the list
    // every time an item is parsed - otherwise there is a lot
    // of processing done by the reload code time and time again.
    private Element[] cachedEadventureList = null;

    /**
     * Set up a package converter, using the supplied directory as the package.
     * The package should be parsed automatically and the values readied for calls
     * to getSchema(), getTitle(), etc.
     * 
     * @param directoryName:
     *            directory containing an expanded IMS content package.
     * @throws IMSManifestException
     *             if there is an error in parsing the manifest file
     *             due to an error in the file or an unexpected value.
     * @throws ImscpApplicationException
     *             if there is any other error
     */
    public SimpleContentPackageConverter(String directoryName) throws IMSManifestException, ImscpApplicationException {

	this.manifestDoc = getDocument(directoryName);
	this.cpCore = new CP_Core(manifestDoc);

	// initialise the property file required for the reload code. Needed to make 
	// the metadata call work. If we remove the metadata call, then this constructor
	// may be removed.
	System.setProperty("editor.properties.file", "uk.ac.reload.editor.properties.rb");

	parsePackage();
    }

    /**
     * Parse IMS content package expanded out
     * into the supplied directory. Note: the manifest file is expected to be in the
     * root of the supplied directory.
     * 
     * @throws ImscpApplicationException
     * @throws IMSManifestException
     */
    private void parsePackage() throws IMSManifestException, ImscpApplicationException {

	String schemaText = getText(manifestDoc, "//metadata/schema");
	String schemaVersion = getText(manifestDoc, "//metadata/schemaversion");
	this.schema = (schemaText != null ? schemaText : "unknown") + " "
		+ (schemaVersion != null ? schemaVersion : "unknown");

	Document orgs = buildOrganisationList();
	if (orgs == null) {
	    String error = "Unable to convert organizations from manifest file to our own structure. Reason unknown - buildOrganisationList returned null";
	    log.error(error);
	    throw new ImscpApplicationException(error);
	}

	try {
	    this.organzationXML = XMLUtils.write2XMLString(orgs);
	} catch (IOException ioe) {
	    String error = "Exception thrown converting organization structure (as document) to an XML string."
		    + ioe.getMessage();
	    log.error(error, ioe);
	    throw new ImscpApplicationException(error, ioe);
	}

	XMLPath xmlPath = new XMLPath("//metadata/*:lom/*:general/*:title/*:langstring");
	this.title = getMetaValue(xmlPath);
	if (this.title == null) {
	    // try the old root name - untested
	    xmlPath = new XMLPath("//metadata/*:record/*:general/*:title/*:langstring");
	    this.title = getMetaValue(xmlPath);
	}

	xmlPath = new XMLPath("//metadata/*:lom/*:general/*:description/*:langstring");
	this.description = getMetaValue(xmlPath);
	if (this.description == null) {
	    // try the old root name - untested
	    xmlPath = new XMLPath("//metadata/*:record/*:general/*:description/*:langstring");
	    this.description = getMetaValue(xmlPath);
	}

    }

    /**
     * Finds a value at the given xmlPath. If only one element, uses that value.
     * If more than one element, tries to find an English value.
     * 
     * @param xmlPath
     * @return Value of the element found at xmlPath.
     */
    private String getMetaValue(XMLPath xmlPath) {
	Element[] elList = manifestDoc.getElements(xmlPath);
	String value = null;
	if (elList != null) {
	    if (elList.length == 0) {
		value = "Unknown";
	    } else if (elList.length == 1) {
		value = elList[0].getTextTrim();
	    } else {
		value = null;
		// TODO check if it is really testing for english
		for (int i = 0; value == null && i < elList.length; i++) {
		    // grab the first English one
		    Element el = elList[i];
		    String attrValue = el.getAttributeValue("lang", Namespace.XML_NAMESPACE);
		    if (attrValue != null && attrValue.startsWith("en")) {
			value = el.getTextTrim();
		    }
		}
		if (value == null) {
		    // can't seem to find an English one, just pick the first
		    value = elList[0].getTextTrim();
		}
	    }
	}
	return value;
    }

    /* Get the text for this element - expect only 1 */
    private String getText(XMLDocument document, String xmlPathString) {
	XMLPath xmlPath = new XMLPath(xmlPathString);
	Element el = document.getElement(xmlPath);
	return el != null ? el.getTextTrim() : null;
    }

    private String debug(XMLDocument document, String param, String xmlPathString) {
	XMLPath xmlPath = new XMLPath(xmlPathString);
	Element[] elList = document.getElements(xmlPath);
	if (elList != null) {
	    log.error(param + " xp: length " + elList.length + " el " + elList);
	    if (elList.length >= 1) {
		log.error("text is " + elList[0].getTextTrim());
		return elList[0].getTextTrim();
	    }
	} else {
	    log.error(param + " xp: el is null");
	}
	return null;
    }

    /**
     * @param directoryName
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private XMLDocument getDocument(String directoryName) throws IMSManifestException {
	try {
	    XMLDocument doc = new XMLDocument();
	    doc.loadDocument(new File(directoryName, "imsmanifest.xml"));
	    return doc;
	} catch (JDOMException je) {
	    String error = "Parsing error occured while loading imsmanifest.xml file. Contents of file may be invalid. "
		    + je.getMessage();
	    log.error(error, je);
	    throw new IMSManifestException(error, je);
	} catch (FileNotFoundException e) {
	    String error = "Unable to find imsmanifest file in the package." + e.getMessage();
	    log.error(error, e);
	    throw new IMSManifestException(error, e);
	} catch (IOException ioe) {
	    String error = "IOException occured while loading imsmanifest file. " + ioe.getMessage();
	    log.error(error, ioe);
	    throw new IMSManifestException(error, ioe);
	}
    }

    /**
     * Built an XML document which is a list of organisations/eadventure.
     */
    private Document buildOrganisationList() throws IMSManifestException {

	Namespace nm = cpCore.getRootManifestElement().getNamespace();

	Element rootElement = cpCore.getRootManifestElement();
	Element orgsElement = rootElement.getChild(CP_Core.ORGANIZATIONS, nm);

	// set up a list of all the eadventure

	// now get all the organizations and set up the new XML document, combining
	// the organization and the eadventure.
	Element defaultOrg = cpCore.getDefaultOrganization(orgsElement);
	String defaultOrgIdentifier = null;
	if (defaultOrg != null) {
	    defaultOrgIdentifier = defaultOrg.getAttributeValue("identifier");
	}

	log.debug("cpCore default org id: " + defaultOrgIdentifier);

	Element newRootElement = new Element("organizations");
	setAttribute(newRootElement, "version", "imscp1");
	Document doc = new Document(newRootElement);

	Element[] orgs = cpCore.getOrganizationsAllowed(orgsElement);
	Element initOrganizationElement = null;
	for (int i = 0; i < orgs.length; i++) {

	    Element organizationElement = processItem(orgs[i], nm, defaultOrgIdentifier, null);
	    if (initOrganizationElement == null) {
		initOrganizationElement = organizationElement;
	    }

	    newRootElement.addContent(organizationElement);
	}

	if (log.isDebugEnabled()) {
	    try {
		log.debug("Organizations are: " + XMLUtils.write2XMLString(doc));
	    } catch (IOException e) {
		log.debug("Unable to convert organizations to XML for log. Organizations are: " + doc);
	    }
	}

	return doc;
    }

    /**
     * Process the given element. Returns a newly formatted Element if the element
     * is a visible item, null otherwise. Will also set the value "defaultItemURL"
     * while processing, if it finds the default item.
     * 
     * First time through, the element will be an organization and parentOrgIdentifier
     * will be null. After that, elements are expected to be items and parentOrgIdentifier
     * should not be null.
     * 
     * parentOrgIdentifier is the parent organization of an item. An item is
     * the default item if either:
     * (a) the defaultOrgIdentifier is null and it is the first item encountered
     * which has a eadventure or
     * (b) parentOrgIdentifier==defaultOrgIdentifier and it is the first item encountered
     * which has a eadventure.
     *
     * @param element
     * @return New version of element combining organization/item/eadventure details
     */
    private Element processItem(Element element, Namespace nm, String defaultOrgIdentifier, String parentOrgIdentifier)
	    throws IMSManifestException {

	String isVisibleString = element.getAttributeValue(CP_Core.ISVISIBLE);
	if (isVisible(isVisibleString)) {

	    String id = element.getAttributeValue(CP_Core.IDENTIFIER);
	    // only process visible items as we are building the list for display to the user
	    Element itref = new Element(OrganizationXMLDef.ITEM);

	    setAttribute(itref, OrganizationXMLDef.IDENTIFIER, id); // mandatory
	    setAttribute(itref, OrganizationXMLDef.PARAMETERS, element.getAttributeValue(CP_Core.PARAMETERS)); // optional
	    setAttribute(itref, OrganizationXMLDef.TITLE, element.getChildText(CP_Core.TITLE, nm)); // optional
	    String eadventureURL = getEadventureURL(element);
	    if (eadventureURL != null) {
		setAttribute(itref, OrganizationXMLDef.RESOURCE, eadventureURL); // optional 
	    }

	    if (eadventureURL != null && this.defaultItem == null
		    && (defaultOrgIdentifier == null || defaultOrgIdentifier.equals(parentOrgIdentifier))) {
		setAttribute(itref, OrganizationXMLDef.DEFAULT, Boolean.TRUE.toString());
		this.defaultItem = eadventureURL;
	    } else {
		setAttribute(itref, OrganizationXMLDef.DEFAULT, Boolean.FALSE.toString());
	    }

	    List items = element.getChildren(CP_Core.ITEM, nm);
	    Iterator iter = items.iterator();
	    while (iter.hasNext()) {
		Element itrefChild = processItem((Element) iter.next(), nm, defaultOrgIdentifier,
			parentOrgIdentifier != null ? parentOrgIdentifier : id);
		if (itrefChild != null) {
		    itref.addContent(itrefChild);
		}
	    }

	    return itref;
	}
	return null;

    }

    /**
     * Not sure exactly what format "isVisible" will be in. In the spec, it says
     * Boolean (True|False) yet the imslipv1p0cp.zip package has a value of "1".
     * So this code will accept anything as visible except for false (any case)
     * or 0.
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
	if (element != null && attributeName != null && value != null) {
	    element.setAttribute(attributeName, value);
	}
    }

    /*
     * Get the eadventure relating to this item. First time called it will
     * get a list of allowed eadventure it will cache the value in
     * cachedEadventureList. After that, it will go to the cache. If
     * it doesn't find the eadventure, then it will regenerate the list
     * of eadventure.
     * 
     * This is done for performance - it is assumed that most items will
     * have access to the same eadventure. This may not be true if sub-manifests
     * are used, hence the regneration of the array if the eadventure
     * isn't found in the cache.
     * 
     * Note: assumes all reference ids are unique across the whole of the
     * manifest file.
     * 
     * @param item Element of type ITEM
     * 
     * @return relative path of the eadventure, null if no IDENTIFIERREF found
     * 
     * @throws IMSManifestException if IDENTIFIERREF is not null but the eadventure
     * could not found.
     */
    private String getEadventureURL(Element itemElement) throws IMSManifestException {

	if (cachedEadventureList == null) {
	    cachedEadventureList = cpCore.getReferencedElementsAllowed(itemElement);
	    if (log.isDebugEnabled()) {
		log.debug("Eadventure are " + cachedEadventureList);
	    }
	}

	String identifierRef = itemElement.getAttributeValue(CP_Core.IDENTIFIERREF);

	if (identifierRef == null) {

	    return null;

	} else {

	    Element eadventure = getEadventure2(identifierRef);

	    if (eadventure == null) {

		// We failed to find a matching eadventure so try generating the list again. 
		// Note: this may case the list to be generated twice first time if the 
		//eadventure is missing. Too bad!
		cachedEadventureList = cpCore.getReferencedElementsAllowed(itemElement);
		if (log.isDebugEnabled()) {
		    log.debug("Eadventure are " + cachedEadventureList);
		}

		eadventure = getEadventure2(identifierRef);
	    }

	    if (eadventure != null) {
		return cpCore.getRelativeURL(eadventure);
	    } else {
		throw new IMSManifestException("Unable to find eadventure for item element "
			+ itemElement.getAttributeValue(CP_Core.TITLE) + " looking for identifier " + identifierRef);
	    }

	}

    }

    /**
     * @param identifierRef
     * @return
     */
    private Element getEadventure2(String identifierRef) {
	for (int i = 0; i < cachedEadventureList.length; i++) {
	    Element eadventure = cachedEadventureList[i];
	    if (identifierRef != null && identifierRef.equals(eadventure.getAttributeValue(CP_Core.IDENTIFIER))) {
		return eadventure;
	    }
	}
	return null;
    }

    /**
     * @return Returns the defaultItem.
     */
    @Override
    public String getDefaultItem() {
	return defaultItem;
    }

    /**
     * @param defaultItem
     *            The defaultItem to set.
     */
    public void setDefaultItem(String defaultItem) {
	this.defaultItem = defaultItem;
    }

    /**
     * @return Returns the description.
     */
    @Override
    public String getDescription() {
	return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @return Returns the organzationXML.
     */
    @Override
    public String getOrganzationXML() {
	return organzationXML;
    }

    /**
     * @param organzationXML
     *            The organzationXML to set.
     */
    public void setOrganzationXML(String organzationXML) {
	this.organzationXML = organzationXML;
    }

    /**
     * @return Returns the schema.
     */
    @Override
    public String getSchema() {
	return schema;
    }

    /**
     * @param schema
     *            The schema to set.
     */
    public void setSchema(String schema) {
	this.schema = schema;
    }

    /**
     * @return Returns the title.
     */
    @Override
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

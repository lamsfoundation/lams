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


package org.lamsfoundation.lams.tool.commonCartridge.ims;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.imsglobal.lti.BasicLTIUtil;
import org.imsglobal.lti.XMLMap;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * SimpleContentPackageConverter contains the code required for parsing the IMS Content Package and converting the info
 * into our own structures.
 *
 * Note: this class has instance data, so do not use it as a singleton.
 *
 * @author Fiona Malikoff, Marcin Cieslak
 */
public class SimpleCommonCartridgeConverter {

    private static final Logger log = Logger.getLogger(SimpleCommonCartridgeConverter.class);

    public static final String DEFAULT = "default";
    public static final String ITEM = "item";
    public static final String PARAMETERS = "parameters";
    public static final String HREF = "href";
    public static final String IDENTIFIER = "identifier";
    public static final String RESOURCE = "commonCartridge";
    public static final String TITLE = "title";

    private static DocumentBuilder docBuilder = null;

    private String schema = null;
    private String title = null;
    private String description = null;
    private String defaultItem = null;
    private String organzationXML = null;

    private Properties types = new Properties();
    private Properties hrefs = new Properties();
    private Map<String, Object> manifestFullMap = null;
    private String directoryName = null;
    private List<CommonCartridgeItem> basicLTIItems = null;

    static {
	try {
	    // a single doc builder is enough
	    SimpleCommonCartridgeConverter.docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	} catch (ParserConfigurationException e) {
	    SimpleCommonCartridgeConverter.log.error("Error while initialising XML document builder", e);
	}
    }

    /**
     * Set up a package converter, using the supplied directory as the package. The package should be parsed
     * automatically and the values readied for calls to getSchema(), getTitle(), etc.
     *
     * @param directoryName
     *            : directory containing an expanded IMS content package.
     * @throws IOException
     */
    public SimpleCommonCartridgeConverter(String directoryName) throws IOException {
	this.directoryName = directoryName;

	parseManifestFullMap();
	parsePackage();
    }

    /**
     * Parse IMS content package expanded out into the supplied directory. Note: the manifest file is expected to be in
     * the root of the supplied directory.
     *
     * @throws ImscpApplicationException
     * @throws IMSManifestException
     */
    private void parsePackage() {

	List<Map<String, Object>> resources = XMLMap.getList(manifestFullMap, "/manifest/resources/resource");
	for (Map<String, Object> resource : resources) {
	    String identifier = XMLMap.getString(resource, "/!identifier");
	    if (identifier == null) {
		continue;
	    }
	    String type = XMLMap.getString(resource, "/!type");
	    if (type != null) {
		types.setProperty(identifier, type);
	    }
	    String href = XMLMap.getString(resource, "/file!href");
	    if (href != null) {
		hrefs.setProperty(identifier, href);
	    }
	}

	basicLTIItems = new LinkedList<CommonCartridgeItem>();
	try {
	    List<Map<String, Object>> topItem = XMLMap.getList(manifestFullMap,
		    "/manifest/organizations/organization/item");
	    parseItem(topItem.get(0));
	} catch (IOException e) {
	    SimpleCommonCartridgeConverter.log
		    .error("<p><b>Error displaying navigation from manifest:" + e.getMessage());
	    SimpleCommonCartridgeConverter.log.error("</div>");
	    return;
	}
    }

    private void parseItem(Map<String, Object> item) throws IOException {
	String id = XMLMap.getString(item, "/!identifier");
	String ref = XMLMap.getString(item, "/!identifierref");
	String title = XMLMap.getString(item, "/title");

	// Handle this item
	if (ref != null) {
	    String type = types.getProperty(ref);
	    String href = hrefs.getProperty(ref);
	    if ((type != null) && (href != null)) {
		File basciLTIXml = new File(directoryName, href);

		if (type.equals("webcontent")) {
		    // content = rawcontent;
		} else if (type.equals("imswl_xmlv1p0")) {
		    // Map<String, Object> xml = null;
		    // content = "<p><b>Error Parsing Web Link XML</b></p>" + content;
		    // try {
		    // xml = XMLMap.getFullMap(rawcontent.trim());
		    // String frameref = XMLMap.getString(xml, "/wl:webLink/url!href");
		    // if (frameref != null) {
		    // content = "<iframe src=\"" + frameref + "\" width=\"100%\" height=\"1200\"></iframe>\n";
		    // }
		    // } catch (Exception e) {
		    // content = "<p><b>Error Parsing Web Link XML:" + e.getMessage() + "</b></p>" + content;
		    // }
		} else if (type.equals("imsbasiclti_xmlv1p0")) {
		    String rawcontent = FileUtils.readFileToString(basciLTIXml, "UTF-8");
		    Map<String, String> info = new HashMap<String, String>();
		    Map<String, String> postProp = new HashMap<String, String>();
		    if (BasicLTIUtil.parseDescriptor(info, postProp, rawcontent)) {
			CommonCartridgeItem basicLTI = new CommonCartridgeItem();

			title = (title != null) ? title : id;
			basicLTI.setTitle(title);
			basicLTI.setLaunchUrl(info.get("launch_url"));
			basicLTI.setSecureLaunchUrl(info.get("secure_launch_url"));
			basicLTI.setKey(info.get("key"));
			basicLTI.setSecret(info.get("secret"));

			String customStr = "";
			for (String key : postProp.keySet()) {
			    customStr += key + "=" + postProp.get(key) + "\n";
			}
			basicLTI.setCustomStr(customStr);

			basicLTIItems.add(basicLTI);
		    }
		}
	    }
	}

	// Descend recursively to child items
	for (Map<String, Object> subItem : XMLMap.getList(item, "/item")) {
	    parseItem(subItem);
	}

    }

    /**
     * @param directoryName
     */
    private void parseManifestFullMap() throws IOException {
	// Parsing manifest
	Document document = null;
	try {
	    document = SimpleCommonCartridgeConverter.docBuilder.parse(new File(directoryName, "imsmanifest.xml"));
	} catch (SAXException e) {
	    SimpleCommonCartridgeConverter.log.error("Error parsing manifest XML", e);
	}
	if (document == null) {
	    throw new IOException("Error parsing manifest XML");
	}

	this.manifestFullMap = XMLMap.getFullMap(document);
	if (manifestFullMap == null) {
	    throw new IOException("Error parsing manifest XML");
	}
    }

    /**
     * @return Returns the defaultItem.
     */
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

    /**
     * @return Returns the basicLTIItems.
     */
    public List<CommonCartridgeItem> getBasicLTIItems() {
	return basicLTIItems;
    }
}
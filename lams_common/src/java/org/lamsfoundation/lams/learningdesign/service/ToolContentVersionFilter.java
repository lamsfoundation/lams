package org.lamsfoundation.lams.learningdesign.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.FileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Super class for all Import content Version Filter. The child class method must follow name conversion.
 * <ol>
 * <li>upXXXToXXX</li>
 * <li>downXXXToXXX</li>
 * </ol>
 * 
 * The XXX must be integer format, which is Tool version number.
 * <p>
 * For more detail, in
 * <a href="http://wiki.lamsfoundation.org/display/lams/How+to+implement+export+and+import+tool+content">wiki</a>.
 * 
 * @author Dapeng.Ni
 * 
 */
public class ToolContentVersionFilter {

    private static final Logger log = Logger.getLogger(ToolContentVersionFilter.class);

    private List<RemovedField> removedFieldList;
    private List<AddedField> addedFieldList;
    private List<RenamedField> renamedFieldList;

    public ToolContentVersionFilter() {
	removedFieldList = new ArrayList<RemovedField>();
	addedFieldList = new ArrayList<AddedField>();
	renamedFieldList = new ArrayList<RenamedField>();
    }

    // container class for removed class
    private class RemovedField {
	private Class ownerClass;
	private String fieldname;

	private RemovedField(Class ownerClass, String fieldname) {
	    this.ownerClass = ownerClass;
	    this.fieldname = fieldname;
	}
    }

    // container class for added class
    private class AddedField {
	private Class ownerClass;
	private String fieldname;
	private String defaultValue;

	private AddedField(Class ownerClass, String fieldname, String defaultValue) {
	    this.ownerClass = ownerClass;
	    this.fieldname = fieldname;
	    this.defaultValue = defaultValue;
	}
    }

    // container class for renamed class
    private class RenamedField {
	private Class ownerClass;
	private String oldFieldname;
	private String newFieldname;

	private RenamedField(Class ownerClass2, String oldFieldname2, String newFieldname2) {
	    ownerClass = ownerClass2;
	    oldFieldname = oldFieldname2;
	    newFieldname = newFieldname2;
	}
    }

    /**
     * When a field is removed to tool Hibernate POJO class, this method must be call in upXXXToYYY()/downXXXToYYY()
     * methods.
     * 
     * @param ownerClass
     * @param fieldname
     */
    public void removeField(Class ownerClass, String fieldname) {
	removedFieldList.add(new RemovedField(ownerClass, fieldname));
    }

    /**
     * When a field is added to tool Hibernate POJO class, this method is optional in upXXXToYYY()/downXXXToYYY()
     * methods. It could set default value for this added fields.
     * 
     * @param ownerClass
     * @param fieldname
     */
    public void addField(Class ownerClass, String fieldname, String defaultValue) {
	addedFieldList.add(new AddedField(ownerClass, fieldname, defaultValue));
    }

    /**
     * When a field is renamed in tool Hibernate POJO class, this method must be call in upXXXToYYY()/downXXXToYYY()
     * methods.
     * 
     * @param ownerClass
     * @param fieldname
     */
    public void renameField(Class ownerClass, String oldFieldname, String newFieldname) {
	renamedFieldList.add(new RenamedField(ownerClass, oldFieldname, newFieldname));
    }

    /**
     * Call by lams import tool service core. Do not use it in tool version filter class.
     * 
     * @param toolFilePath
     * @throws IOException
     */
    public void transformXML(String toolFilePath) throws IOException {
	File toolFile = new File(toolFilePath);

	try {
	    DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = docBuilder.parse(new FileInputStream(toolFile));
	    Element root = doc.getDocumentElement();
	    retrieveXML(root);

	    toolFile.renameTo(new File(toolFilePath + "_oldver"));
	    File newToolFile = new File(toolFilePath);
	    FileUtil.writeXMLtoFile(doc, newToolFile);
	} catch (Exception e) {
	    throw new IOException("Error while transforming XML", e);
	}
    }

    private void retrieveXML(Element root) throws IOException {
	for (RemovedField remove : removedFieldList) {
	    if (StringUtils.equals(root.getNodeName(), remove.ownerClass.getName())
		    || StringUtils.equals(root.getAttribute("class"), remove.ownerClass.getName())) {

		Node node = root.getFirstChild();
		while (node != null) {
		    Node oldNode = node;
		    node = node.getNextSibling();
		    if (oldNode.getNodeName().equals(remove.fieldname)) {
			root.removeChild(oldNode);
			ToolContentVersionFilter.log.debug("Field " + remove.fieldname + " in class "
				+ remove.ownerClass.getName() + " was removed.");
		    }
		}
	    }
	}

	// add all new fields for this class
	for (AddedField added : addedFieldList) {
	    if (StringUtils.equals(root.getNodeName(), added.ownerClass.getName())) {
		Element element = root.getOwnerDocument().createElement(added.fieldname);
		element.setTextContent(added.defaultValue);
		root.appendChild(element);

		ToolContentVersionFilter.log.debug("Field " + added.fieldname + " in class "
			+ added.ownerClass.getName() + " was added by value " + added.defaultValue);
	    }
	}

	// rename all marked fields for this class
	for (RenamedField renamed : renamedFieldList) {
	    if (StringUtils.equals(root.getNodeName(), renamed.ownerClass.getName())) {
		Node node = root.getFirstChild();
		while (node != null) {
		    Node oldNode = node;
		    node = node.getNextSibling();
		    if (oldNode.getNodeName().equals(renamed.oldFieldname)) {
			Element newElement = root.getOwnerDocument().createElement(renamed.newFieldname);
			newElement.setTextContent(oldNode.getTextContent());
			root.replaceChild(newElement, oldNode);
			ToolContentVersionFilter.log.debug("Field " + renamed.oldFieldname + " in class "
				+ renamed.ownerClass.getName() + " was renamed to " + renamed.newFieldname);
		    }
		}
	    }
	}

	// remove fields
	NodeList children = root.getChildNodes();
	for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
	    Node node = children.item(childIndex);
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		retrieveXML((Element) node);
	    }
	}
    }
}
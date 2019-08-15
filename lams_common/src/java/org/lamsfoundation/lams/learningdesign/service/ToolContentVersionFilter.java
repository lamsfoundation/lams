package org.lamsfoundation.lams.learningdesign.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.FileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
    private Map<String, String> renamedClassMap;

    public ToolContentVersionFilter() {
	clearChanges();
    }

    protected void clearChanges() {
	removedFieldList = new ArrayList<>();
	addedFieldList = new ArrayList<>();
	renamedFieldList = new ArrayList<>();
	renamedClassMap = new HashMap<>();
    }

    // container class for removed class
    private class RemovedField {
	private String ownerClass;
	private String fieldname;

	private RemovedField(Class ownerClass, String fieldname) {
	    this.ownerClass = ownerClass.getName();
	    this.fieldname = fieldname;
	}

	private RemovedField(String ownerClass, String fieldname) {
	    this.ownerClass = ownerClass;
	    this.fieldname = fieldname;
	}
    }

    // container class for added class
    private class AddedField {
	private String ownerClass;
	private String fieldname;
	private String defaultValue;

	private AddedField(Class ownerClass, String fieldname, String defaultValue) {
	    this.ownerClass = ownerClass.getName();
	    this.fieldname = fieldname;
	    this.defaultValue = defaultValue;
	}

	private AddedField(String ownerClass, String fieldname, String defaultValue) {
	    this.ownerClass = ownerClass;
	    this.fieldname = fieldname;
	    this.defaultValue = defaultValue;
	}
    }

    // container class for renamed class
    private class RenamedField {
	private String ownerClass;
	private String oldFieldname;
	private String newFieldname;

	private RenamedField(Class ownerClass, String oldFieldname, String newFieldname) {
	    this.ownerClass = ownerClass.getName();
	    this.oldFieldname = oldFieldname;
	    this.newFieldname = newFieldname;
	}

	private RenamedField(String ownerClass, String oldFieldname, String newFieldname) {
	    this.ownerClass = ownerClass;
	    this.oldFieldname = oldFieldname;
	    this.newFieldname = newFieldname;
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

    public void removeField(String ownerClass, String fieldname) {
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

    public void addField(String ownerClass, String fieldname, String defaultValue) {
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

    public void renameField(String ownerClass, String oldFieldname, String newFieldname) {
	renamedFieldList.add(new RenamedField(ownerClass, oldFieldname, newFieldname));
    }

    public void renameClass(String oldClassName, String newClassName) {
	renamedClassMap.put(oldClassName, newClassName);
    }

    /**
     * Call by lams import tool service core. Do not use it in tool version filter class.
     *
     * @param toolFilePath
     * @throws IOException
     */
    public void transformXML(String toolFilePath) throws IOException {
	transformXML(toolFilePath, root -> retrieveXML(root));
    }

    /**
     * Reads a XML file from given path and applies a transforming method on it
     */
    protected void transformXML(String toolFilePath, Consumer<Element> converter) throws IOException {
	File toolFile = new File(toolFilePath);

	try {
	    DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = docBuilder.parse(new FileInputStream(toolFile));
	    Element root = doc.getDocumentElement();
	    converter.accept(root);

	    toolFile.renameTo(new File(toolFilePath + "_oldver"));
	    File newToolFile = new File(toolFilePath);
	    FileUtil.writeXMLtoFile(doc, newToolFile);
	} catch (Exception e) {
	    throw new IOException("Error while transforming XML", e);
	}
    }

    private void retrieveXML(Element root) {
	for (Entry<String, String> renamed : renamedClassMap.entrySet()) {
	    // if root element was replaced in one of tool.xml files, it needs to be fetched here for further processing
	    root = (Element) ToolContentVersionFilter.renameClass(root, renamed.getKey(), renamed.getValue());
	}

	for (RemovedField remove : removedFieldList) {
	    if (StringUtils.equals(root.getNodeName(), remove.ownerClass)
		    || StringUtils.equals(root.getAttribute("class"), remove.ownerClass)) {

		Node node = root.getFirstChild();
		while (node != null) {
		    Node oldNode = node;
		    node = node.getNextSibling();
		    if (oldNode.getNodeName().equals(remove.fieldname)) {
			root.removeChild(oldNode);
			ToolContentVersionFilter.log.debug(
				"Field " + remove.fieldname + " in class " + remove.ownerClass + " was removed.");
		    }
		}
	    }
	}

	// add all new fields for this class
	for (AddedField added : addedFieldList) {
	    if (StringUtils.equals(root.getNodeName(), added.ownerClass)) {
		Element element = root.getOwnerDocument().createElement(added.fieldname);
		element.setTextContent(added.defaultValue);
		root.appendChild(element);

		ToolContentVersionFilter.log.debug("Field " + added.fieldname + " in class " + added.ownerClass
			+ " was added by value " + added.defaultValue);
	    }
	}

	// rename all marked fields for this class
	for (RenamedField renamed : renamedFieldList) {
	    if (StringUtils.equals(root.getNodeName(), renamed.ownerClass)) {
		NodeList children = root.getChildNodes();
		for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
		    Node childNode = children.item(childIndex);
		    ToolContentVersionFilter.renameNode(childNode, renamed.oldFieldname, renamed.newFieldname);
		}
	    }
	}

	NodeList children = root.getChildNodes();
	for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
	    Node node = children.item(childIndex);
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
		retrieveXML((Element) node);
	    }
	}
    }

    /**
     * Renames an entity name or package.
     */
    private static Node renameClass(Node node, String oldName, String newName) {
	String currentName = node.getNodeName();
	String exactNewName = null;
	String exactOldName = null;
	// if names provided in renameClassMap end with ".", we rename whole packages
	if (oldName.endsWith(".")) {
	    if (currentName.startsWith(oldName)) {
		exactNewName = currentName.replace(oldName, newName);
		exactOldName = currentName;
	    }
	} else if (StringUtils.equals(currentName, oldName)) {
	    // this is renaming of a single class
	    exactNewName = newName;
	    exactOldName = oldName;
	}

	if (exactOldName != null) {
	    node = ToolContentVersionFilter.renameNode(node, exactOldName, exactNewName);
	}

	return node;
    }

    private static Node renameNode(Node oldNode, String oldName, String newName) {
	if (oldNode.getNodeName().equals(oldName)) {
	    Element newNode = oldNode.getOwnerDocument().createElement(newName);

	    //copy attributes
	    if (oldNode.getAttributes() != null) {
		NamedNodeMap attributes = oldNode.getAttributes();
		for (int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
		    Node clonedAttribute = attributes.item(attrIndex).cloneNode(true);
		    String value = clonedAttribute.getTextContent();
		    if (value != null && value.contains(oldName)) {
			value = value.replace(oldName, newName);
			clonedAttribute.setTextContent(value);
		    }
		    newNode.getAttributes().setNamedItem(clonedAttribute);
		}
	    }

	    if (oldNode.hasChildNodes()) {
		//copy child nodes
		NodeList children = oldNode.getChildNodes();
		for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
		    Node clonedChildNode = children.item(childIndex).cloneNode(true);
		    newNode.appendChild(clonedChildNode);
		}
	    } else {
		newNode.setTextContent(oldNode.getTextContent());
	    }
	    oldNode.getParentNode().replaceChild(newNode, oldNode);
	    ToolContentVersionFilter.log.debug("Node " + oldName + " was renamed to " + newName);
	    return newNode;
	}
	return oldNode;
    }
}
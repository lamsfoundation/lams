package org.lamsfoundation.lams.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtil {
    /**
     * Finds the first descendant of parentElement with childElementName and returns its text value.
     * Otherwise returns defaultValue.
     */
    public static String getChildElementValue(Element parentElement, String childElementName, String defaultValue) {
	Element childElement = XMLUtil.findChild(parentElement, childElementName);
	return childElement == null ? defaultValue : childElement.getTextContent();
    }

    /**
     * Find an element in source, takes its value and puts it under a different name in target.
     * If no value is found, default value is used.
     */
    public static String rewriteTextElement(Element sourceParent, Element targetParent, String sourceElementName,
	    String targetElementName, String defaultValue) {
	return XMLUtil.rewriteTextElement(sourceParent, targetParent, sourceElementName, targetElementName,
		defaultValue, false, false);
    }

    /**
     * Find an element in source, takes its value and puts it under a different name in target.
     * If no value is found, default value is used.
     * Attributes can be copied to the target element, depending on the parameter.
     * The source source element can be removed, depending on the parameter.
     * If valueConverter function is provided, it is applied on value or defaultValue before setting it in target.
     */
    @SafeVarargs
    public static String rewriteTextElement(Element sourceParent, Element targetParent, String sourceElementName,
	    String targetElementName, String defaultValue, boolean preserveAttributes, boolean removeSource,
	    Function<String, String>... valueConverters) {
	String value = defaultValue;
	Element foundElement = null;
	// find the element we are looking for
	List<Element> foundElements = XMLUtil.findChildren(sourceParent, sourceElementName);
	if (foundElements.size() == 1) {
	    foundElement = foundElements.get(0);
	    value = foundElement.getTextContent();
	} else if (foundElements.size() > 1) {
	    throw new InvalidParameterException(
		    "There is more than one element with name \"" + sourceElementName + "\" in its parent");
	}
	// if we start with working with target elements, removing source and working with attributes is broken
	// so first prepare data, modify DOM and only then work with target
	Map<String, String> attributes = null;
	if (preserveAttributes && foundElement != null) {
	    NamedNodeMap nodeMap = foundElement.getAttributes();
	    if (nodeMap.getLength() > 0) {
		attributes = new HashMap<>();
		for (int attributeIndex = 0; attributeIndex < nodeMap.getLength(); attributeIndex++) {
		    Node attribute = nodeMap.item(attributeIndex);
		    attributes.put(attribute.getNodeName(), attribute.getTextContent());
		}
	    }
	}
	if (removeSource && foundElement != null) {
	    sourceParent.removeChild(foundElement);
	}
	if (StringUtils.isNotBlank(value)) {
	    Document document = targetParent.getOwnerDocument();
	    Element field = document.createElement(targetElementName);
	    if (valueConverters != null) {
		for (Function<String, String> function : valueConverters) {
		    value = function.apply(value);
		}
	    }
	    field.setTextContent(value);
	    if (attributes != null) {
		for (Entry<String, String> attributeEntry : attributes.entrySet()) {
		    field.setAttribute(attributeEntry.getKey(), attributeEntry.getValue());
		}
	    }
	    targetParent.appendChild(field);
	}
	return value;
    }

    /**
     * Adds an element with value in target parent element.
     */
    public static String addTextElement(Element targetParent, String targetElementName, String value) {
	return XMLUtil.rewriteTextElement(null, targetParent, null, targetElementName, value);
    }

    public static void removeElement(Element parentElement, String childElementName) {
	if (parentElement == null || childElementName == null) {
	    throw new InvalidParameterException("Neither parent nor child name must not be null");
	}
	NodeList nodeList = parentElement.getElementsByTagName(childElementName);
	if (nodeList.getLength() == 0) {
	    return;
	}
	if (nodeList.getLength() == 1) {
	    parentElement.removeChild(nodeList.item(0));
	    return;
	}
	throw new InvalidParameterException(
		"There is more than one element with name \"" + childElementName + "\" in its parent");
    }

    /**
     * Finds immediate children of a given element with given name.
     */
    public static List<Element> findChildren(Element parentElement, String childElementName) {
	List<Element> result = new ArrayList<>();
	if (parentElement == null || childElementName == null) {
	    return result;
	}
	NodeList nodeList = parentElement.getChildNodes();
	for (int childIndex = 0; childIndex < nodeList.getLength(); childIndex++) {
	    Node childNode = nodeList.item(childIndex);
	    if (childNode instanceof Element) {
		Element childElement = (Element) childNode;
		if (childElement.getTagName().equalsIgnoreCase(childElementName)) {
		    result.add(childElement);
		}
	    }
	}
	return result;
    }

    /**
     * Finds immediate child of a given element with given name.
     */
    public static Element findChild(Element parentElement, String childElementName) {
	if (parentElement == null || childElementName == null) {
	    return null;
	}
	List<Element> foundElements = XMLUtil.findChildren(parentElement, childElementName);
	if (foundElements.size() == 0) {
	    return null;
	}
	if (foundElements.size() == 1) {
	    return foundElements.get(0);
	}
	throw new InvalidParameterException(
		"There is more than one element with name \"" + childElementName + "\" in its parent");
    }
}
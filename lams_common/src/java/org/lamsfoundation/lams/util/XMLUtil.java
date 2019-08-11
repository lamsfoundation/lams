package org.lamsfoundation.lams.util;

import java.security.InvalidParameterException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLUtil {
    /**
     * Finds the first descendant of parentElement with childElementName and returns its text value.
     * Otherwise returns defaultValue.
     */
    public static String getChildElementValue(Element parentElement, String childElementName, String defaultValue) {
	if (parentElement == null || childElementName == null) {
	    return defaultValue;
	}
	NodeList nodeList = parentElement.getElementsByTagName(childElementName);
	if (nodeList.getLength() == 0) {
	    return defaultValue;
	}
	if (nodeList.getLength() == 1) {
	    return nodeList.item(0).getTextContent();
	}
	throw new InvalidParameterException(
		"There is more than one element with name \"" + childElementName + "\" in its parent");
    }

    /**
     * Find an element in source, takes its value and puts it under a different name in target.
     * If no value is found, default value is used.
     */
    public static String rewriteTextElement(Element sourceParent, Element targetParent, String sourceElementName,
	    String targetElementName, String defaultValue) {
	return XMLUtil.rewriteTextElement(sourceParent, targetParent, sourceElementName, targetElementName,
		defaultValue, false);
    }

    /**
     * Find an element in source, takes its value and puts it under a different name in target.
     * If no value is found, default value is used.
     * The source source element can be removed, depending on the parameter.
     */
    public static String rewriteTextElement(Element sourceParent, Element targetParent, String sourceElementName,
	    String targetElementName, String defaultValue, boolean removeSource) {
	String value = XMLUtil.getChildElementValue(sourceParent, sourceElementName, defaultValue);
	if (removeSource) {
	    if (sourceParent == null || sourceElementName == null) {
		throw new InvalidParameterException("Can not remove an element if its name or parent are null");
	    }
	    NodeList nodeList = sourceParent.getElementsByTagName(sourceElementName);
	    if (nodeList.getLength() == 1) {
		sourceParent.removeChild(nodeList.item(0));
	    }
	}
	if (StringUtils.isNotBlank(value)) {
	    Document document = targetParent.getOwnerDocument();
	    Element field = document.createElement(targetElementName);
	    field.setTextContent(value);
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
}
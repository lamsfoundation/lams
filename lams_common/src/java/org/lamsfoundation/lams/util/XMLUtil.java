package org.lamsfoundation.lams.util;

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
	return nodeList.getLength() == 0 ? defaultValue : ((Element) nodeList.item(0)).getTextContent();
    }

    /**
     * Find an element in source, takes its value and puts it under a different name in target.
     * If no value is found, default value is used.
     */
    public static String rewriteTextElement(Element sourceParent, Element targetParent, String sourceElementName,
	    String targetElementName, String defaultValue) {
	String value = XMLUtil.getChildElementValue(sourceParent, sourceElementName, defaultValue);
	if (value != null) {
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
}
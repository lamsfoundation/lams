/*
 * XML Type:  TransformType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.TransformType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML TransformType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface TransformType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.TransformType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "transformtype550btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "XPath" elements
     */
    java.util.List<java.lang.String> getXPathList();

    /**
     * Gets array of all "XPath" elements
     */
    java.lang.String[] getXPathArray();

    /**
     * Gets ith "XPath" element
     */
    java.lang.String getXPathArray(int i);

    /**
     * Gets (as xml) a List of "XPath" elements
     */
    java.util.List<org.apache.xmlbeans.XmlString> xgetXPathList();

    /**
     * Gets (as xml) array of all "XPath" elements
     */
    org.apache.xmlbeans.XmlString[] xgetXPathArray();

    /**
     * Gets (as xml) ith "XPath" element
     */
    org.apache.xmlbeans.XmlString xgetXPathArray(int i);

    /**
     * Returns number of "XPath" element
     */
    int sizeOfXPathArray();

    /**
     * Sets array of all "XPath" element
     */
    void setXPathArray(java.lang.String[] xPathArray);

    /**
     * Sets ith "XPath" element
     */
    void setXPathArray(int i, java.lang.String xPath);

    /**
     * Sets (as xml) array of all "XPath" element
     */
    void xsetXPathArray(org.apache.xmlbeans.XmlString[] xPathArray);

    /**
     * Sets (as xml) ith "XPath" element
     */
    void xsetXPathArray(int i, org.apache.xmlbeans.XmlString xPath);

    /**
     * Inserts the value as the ith "XPath" element
     */
    void insertXPath(int i, java.lang.String xPath);

    /**
     * Appends the value as the last "XPath" element
     */
    void addXPath(java.lang.String xPath);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "XPath" element
     */
    org.apache.xmlbeans.XmlString insertNewXPath(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "XPath" element
     */
    org.apache.xmlbeans.XmlString addNewXPath();

    /**
     * Removes the ith "XPath" element
     */
    void removeXPath(int i);

    /**
     * Gets the "Algorithm" attribute
     */
    java.lang.String getAlgorithm();

    /**
     * Gets (as xml) the "Algorithm" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetAlgorithm();

    /**
     * Sets the "Algorithm" attribute
     */
    void setAlgorithm(java.lang.String algorithm);

    /**
     * Sets (as xml) the "Algorithm" attribute
     */
    void xsetAlgorithm(org.apache.xmlbeans.XmlAnyURI algorithm);
}

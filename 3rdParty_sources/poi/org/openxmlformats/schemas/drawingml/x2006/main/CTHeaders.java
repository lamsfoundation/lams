/*
 * XML Type:  CT_Headers
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Headers(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHeaders extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctheaders1637type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "header" elements
     */
    java.util.List<java.lang.String> getHeaderList();

    /**
     * Gets array of all "header" elements
     */
    java.lang.String[] getHeaderArray();

    /**
     * Gets ith "header" element
     */
    java.lang.String getHeaderArray(int i);

    /**
     * Gets (as xml) a List of "header" elements
     */
    java.util.List<org.apache.xmlbeans.XmlString> xgetHeaderList();

    /**
     * Gets (as xml) array of all "header" elements
     */
    org.apache.xmlbeans.XmlString[] xgetHeaderArray();

    /**
     * Gets (as xml) ith "header" element
     */
    org.apache.xmlbeans.XmlString xgetHeaderArray(int i);

    /**
     * Returns number of "header" element
     */
    int sizeOfHeaderArray();

    /**
     * Sets array of all "header" element
     */
    void setHeaderArray(java.lang.String[] headerArray);

    /**
     * Sets ith "header" element
     */
    void setHeaderArray(int i, java.lang.String header);

    /**
     * Sets (as xml) array of all "header" element
     */
    void xsetHeaderArray(org.apache.xmlbeans.XmlString[] headerArray);

    /**
     * Sets (as xml) ith "header" element
     */
    void xsetHeaderArray(int i, org.apache.xmlbeans.XmlString header);

    /**
     * Inserts the value as the ith "header" element
     */
    void insertHeader(int i, java.lang.String header);

    /**
     * Appends the value as the last "header" element
     */
    void addHeader(java.lang.String header);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "header" element
     */
    org.apache.xmlbeans.XmlString insertNewHeader(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "header" element
     */
    org.apache.xmlbeans.XmlString addNewHeader();

    /**
     * Removes the ith "header" element
     */
    void removeHeader(int i);
}

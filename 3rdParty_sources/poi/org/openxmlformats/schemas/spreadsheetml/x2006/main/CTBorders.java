/*
 * XML Type:  CT_Borders
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Borders(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBorders extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctborders0d66type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "border" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder> getBorderList();

    /**
     * Gets array of all "border" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder[] getBorderArray();

    /**
     * Gets ith "border" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder getBorderArray(int i);

    /**
     * Returns number of "border" element
     */
    int sizeOfBorderArray();

    /**
     * Sets array of all "border" element
     */
    void setBorderArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder[] borderArray);

    /**
     * Sets ith "border" element
     */
    void setBorderArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder border);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "border" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder insertNewBorder(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "border" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder addNewBorder();

    /**
     * Removes the ith "border" element
     */
    void removeBorder(int i);

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();
}

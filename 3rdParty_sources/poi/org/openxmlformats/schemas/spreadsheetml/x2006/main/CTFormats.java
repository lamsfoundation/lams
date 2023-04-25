/*
 * XML Type:  CT_Formats
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Formats(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFormats extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctformatseebbtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "format" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormat> getFormatList();

    /**
     * Gets array of all "format" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormat[] getFormatArray();

    /**
     * Gets ith "format" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormat getFormatArray(int i);

    /**
     * Returns number of "format" element
     */
    int sizeOfFormatArray();

    /**
     * Sets array of all "format" element
     */
    void setFormatArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormat[] formatArray);

    /**
     * Sets ith "format" element
     */
    void setFormatArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormat format);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "format" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormat insertNewFormat(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "format" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormat addNewFormat();

    /**
     * Removes the ith "format" element
     */
    void removeFormat(int i);

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

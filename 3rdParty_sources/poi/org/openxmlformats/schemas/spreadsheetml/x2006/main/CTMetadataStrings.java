/*
 * XML Type:  CT_MetadataStrings
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MetadataStrings(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMetadataStrings extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmetadatastrings65f2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "s" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement> getSList();

    /**
     * Gets array of all "s" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement[] getSArray();

    /**
     * Gets ith "s" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement getSArray(int i);

    /**
     * Returns number of "s" element
     */
    int sizeOfSArray();

    /**
     * Sets array of all "s" element
     */
    void setSArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement[] sArray);

    /**
     * Sets ith "s" element
     */
    void setSArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement s);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "s" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement insertNewS(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "s" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement addNewS();

    /**
     * Removes the ith "s" element
     */
    void removeS(int i);

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

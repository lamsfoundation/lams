/*
 * XML Type:  CT_ProtectedRanges
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ProtectedRanges(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTProtectedRanges extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctprotectedranges7e83type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "protectedRange" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange> getProtectedRangeList();

    /**
     * Gets array of all "protectedRange" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange[] getProtectedRangeArray();

    /**
     * Gets ith "protectedRange" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange getProtectedRangeArray(int i);

    /**
     * Returns number of "protectedRange" element
     */
    int sizeOfProtectedRangeArray();

    /**
     * Sets array of all "protectedRange" element
     */
    void setProtectedRangeArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange[] protectedRangeArray);

    /**
     * Sets ith "protectedRange" element
     */
    void setProtectedRangeArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange protectedRange);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "protectedRange" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange insertNewProtectedRange(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "protectedRange" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRange addNewProtectedRange();

    /**
     * Removes the ith "protectedRange" element
     */
    void removeProtectedRange(int i);
}

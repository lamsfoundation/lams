/*
 * XML Type:  CT_SmartTagTypes
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SmartTagTypes(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSmartTagTypes extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsmarttagtypes9207type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "smartTagType" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType> getSmartTagTypeList();

    /**
     * Gets array of all "smartTagType" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType[] getSmartTagTypeArray();

    /**
     * Gets ith "smartTagType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType getSmartTagTypeArray(int i);

    /**
     * Returns number of "smartTagType" element
     */
    int sizeOfSmartTagTypeArray();

    /**
     * Sets array of all "smartTagType" element
     */
    void setSmartTagTypeArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType[] smartTagTypeArray);

    /**
     * Sets ith "smartTagType" element
     */
    void setSmartTagTypeArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType smartTagType);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "smartTagType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType insertNewSmartTagType(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "smartTagType" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType addNewSmartTagType();

    /**
     * Removes the ith "smartTagType" element
     */
    void removeSmartTagType(int i);
}

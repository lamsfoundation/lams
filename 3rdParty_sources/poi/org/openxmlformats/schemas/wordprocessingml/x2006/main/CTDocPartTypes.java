/*
 * XML Type:  CT_DocPartTypes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartTypes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocPartTypes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocPartTypes extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartTypes> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocparttypesc33atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "type" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType> getTypeList();

    /**
     * Gets array of all "type" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType[] getTypeArray();

    /**
     * Gets ith "type" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType getTypeArray(int i);

    /**
     * Returns number of "type" element
     */
    int sizeOfTypeArray();

    /**
     * Sets array of all "type" element
     */
    void setTypeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType[] typeArray);

    /**
     * Sets ith "type" element
     */
    void setTypeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType type);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "type" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType insertNewType(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "type" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType addNewType();

    /**
     * Removes the ith "type" element
     */
    void removeType(int i);

    /**
     * Gets the "all" attribute
     */
    java.lang.Object getAll();

    /**
     * Gets (as xml) the "all" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetAll();

    /**
     * True if has "all" attribute
     */
    boolean isSetAll();

    /**
     * Sets the "all" attribute
     */
    void setAll(java.lang.Object all);

    /**
     * Sets (as xml) the "all" attribute
     */
    void xsetAll(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff all);

    /**
     * Unsets the "all" attribute
     */
    void unsetAll();
}

/*
 * XML Type:  CT_DefinedNames
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DefinedNames(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDefinedNames extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdefinednamesce48type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "definedName" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName> getDefinedNameList();

    /**
     * Gets array of all "definedName" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName[] getDefinedNameArray();

    /**
     * Gets ith "definedName" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName getDefinedNameArray(int i);

    /**
     * Returns number of "definedName" element
     */
    int sizeOfDefinedNameArray();

    /**
     * Sets array of all "definedName" element
     */
    void setDefinedNameArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName[] definedNameArray);

    /**
     * Sets ith "definedName" element
     */
    void setDefinedNameArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName definedName);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "definedName" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName insertNewDefinedName(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "definedName" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName addNewDefinedName();

    /**
     * Removes the ith "definedName" element
     */
    void removeDefinedName(int i);
}

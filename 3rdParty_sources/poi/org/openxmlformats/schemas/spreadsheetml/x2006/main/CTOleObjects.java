/*
 * XML Type:  CT_OleObjects
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OleObjects(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOleObjects extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoleobjects1455type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "oleObject" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject> getOleObjectList();

    /**
     * Gets array of all "oleObject" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject[] getOleObjectArray();

    /**
     * Gets ith "oleObject" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject getOleObjectArray(int i);

    /**
     * Returns number of "oleObject" element
     */
    int sizeOfOleObjectArray();

    /**
     * Sets array of all "oleObject" element
     */
    void setOleObjectArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject[] oleObjectArray);

    /**
     * Sets ith "oleObject" element
     */
    void setOleObjectArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject oleObject);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oleObject" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject insertNewOleObject(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "oleObject" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject addNewOleObject();

    /**
     * Removes the ith "oleObject" element
     */
    void removeOleObject(int i);
}

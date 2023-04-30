/*
 * XML Type:  CT_DdeItems
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItems
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DdeItems(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDdeItems extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItems> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctddeitemse1e4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "ddeItem" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem> getDdeItemList();

    /**
     * Gets array of all "ddeItem" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem[] getDdeItemArray();

    /**
     * Gets ith "ddeItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem getDdeItemArray(int i);

    /**
     * Returns number of "ddeItem" element
     */
    int sizeOfDdeItemArray();

    /**
     * Sets array of all "ddeItem" element
     */
    void setDdeItemArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem[] ddeItemArray);

    /**
     * Sets ith "ddeItem" element
     */
    void setDdeItemArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem ddeItem);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ddeItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem insertNewDdeItem(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "ddeItem" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItem addNewDdeItem();

    /**
     * Removes the ith "ddeItem" element
     */
    void removeDdeItem(int i);
}

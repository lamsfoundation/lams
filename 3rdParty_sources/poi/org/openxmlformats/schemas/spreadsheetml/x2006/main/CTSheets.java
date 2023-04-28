/*
 * XML Type:  CT_Sheets
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Sheets(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSheets extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsheets49fdtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "sheet" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet> getSheetList();

    /**
     * Gets array of all "sheet" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet[] getSheetArray();

    /**
     * Gets ith "sheet" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet getSheetArray(int i);

    /**
     * Returns number of "sheet" element
     */
    int sizeOfSheetArray();

    /**
     * Sets array of all "sheet" element
     */
    void setSheetArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet[] sheetArray);

    /**
     * Sets ith "sheet" element
     */
    void setSheetArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet sheet);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sheet" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet insertNewSheet(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sheet" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet addNewSheet();

    /**
     * Removes the ith "sheet" element
     */
    void removeSheet(int i);
}

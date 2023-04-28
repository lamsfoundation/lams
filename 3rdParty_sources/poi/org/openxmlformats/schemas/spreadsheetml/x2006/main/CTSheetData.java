/*
 * XML Type:  CT_SheetData
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SheetData(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSheetData extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsheetdata8408type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "row" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow> getRowList();

    /**
     * Gets array of all "row" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow[] getRowArray();

    /**
     * Gets ith "row" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow getRowArray(int i);

    /**
     * Returns number of "row" element
     */
    int sizeOfRowArray();

    /**
     * Sets array of all "row" element
     */
    void setRowArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow[] rowArray);

    /**
     * Sets ith "row" element
     */
    void setRowArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow row);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "row" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow insertNewRow(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "row" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRow addNewRow();

    /**
     * Removes the ith "row" element
     */
    void removeRow(int i);
}

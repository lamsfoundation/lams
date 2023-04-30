/*
 * XML Type:  CT_SheetIdMap
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetIdMap
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SheetIdMap(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSheetIdMap extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetIdMap> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsheetidmape72btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "sheetId" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetId> getSheetIdList();

    /**
     * Gets array of all "sheetId" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetId[] getSheetIdArray();

    /**
     * Gets ith "sheetId" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetId getSheetIdArray(int i);

    /**
     * Returns number of "sheetId" element
     */
    int sizeOfSheetIdArray();

    /**
     * Sets array of all "sheetId" element
     */
    void setSheetIdArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetId[] sheetIdArray);

    /**
     * Sets ith "sheetId" element
     */
    void setSheetIdArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetId sheetId);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sheetId" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetId insertNewSheetId(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sheetId" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetId addNewSheetId();

    /**
     * Removes the ith "sheetId" element
     */
    void removeSheetId(int i);

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

/*
 * XML Type:  CT_SheetViews
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SheetViews(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSheetViews extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsheetviewsb918type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "sheetView" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView> getSheetViewList();

    /**
     * Gets array of all "sheetView" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView[] getSheetViewArray();

    /**
     * Gets ith "sheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView getSheetViewArray(int i);

    /**
     * Returns number of "sheetView" element
     */
    int sizeOfSheetViewArray();

    /**
     * Sets array of all "sheetView" element
     */
    void setSheetViewArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView[] sheetViewArray);

    /**
     * Sets ith "sheetView" element
     */
    void setSheetViewArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView sheetView);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView insertNewSheetView(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView addNewSheetView();

    /**
     * Removes the ith "sheetView" element
     */
    void removeSheetView(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}

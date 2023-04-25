/*
 * XML Type:  CT_CustomSheetViews
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CustomSheetViews(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCustomSheetViews extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcustomsheetviewsc069type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "customSheetView" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView> getCustomSheetViewList();

    /**
     * Gets array of all "customSheetView" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView[] getCustomSheetViewArray();

    /**
     * Gets ith "customSheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView getCustomSheetViewArray(int i);

    /**
     * Returns number of "customSheetView" element
     */
    int sizeOfCustomSheetViewArray();

    /**
     * Sets array of all "customSheetView" element
     */
    void setCustomSheetViewArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView[] customSheetViewArray);

    /**
     * Sets ith "customSheetView" element
     */
    void setCustomSheetViewArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView customSheetView);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customSheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView insertNewCustomSheetView(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "customSheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetView addNewCustomSheetView();

    /**
     * Removes the ith "customSheetView" element
     */
    void removeCustomSheetView(int i);
}

/*
 * XML Type:  CT_CustomChartsheetViews
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CustomChartsheetViews(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCustomChartsheetViews extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcustomchartsheetviews931btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "customSheetView" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView> getCustomSheetViewList();

    /**
     * Gets array of all "customSheetView" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView[] getCustomSheetViewArray();

    /**
     * Gets ith "customSheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView getCustomSheetViewArray(int i);

    /**
     * Returns number of "customSheetView" element
     */
    int sizeOfCustomSheetViewArray();

    /**
     * Sets array of all "customSheetView" element
     */
    void setCustomSheetViewArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView[] customSheetViewArray);

    /**
     * Sets ith "customSheetView" element
     */
    void setCustomSheetViewArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView customSheetView);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customSheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView insertNewCustomSheetView(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "customSheetView" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView addNewCustomSheetView();

    /**
     * Removes the ith "customSheetView" element
     */
    void removeCustomSheetView(int i);
}

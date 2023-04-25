/*
 * XML Type:  CT_Marker
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Marker(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public interface CTMarker extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmarkeree8etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "col" element
     */
    int getCol();

    /**
     * Gets (as xml) the "col" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID xgetCol();

    /**
     * Sets the "col" element
     */
    void setCol(int col);

    /**
     * Sets (as xml) the "col" element
     */
    void xsetCol(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID col);

    /**
     * Gets the "colOff" element
     */
    java.lang.Object getColOff();

    /**
     * Gets (as xml) the "colOff" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetColOff();

    /**
     * Sets the "colOff" element
     */
    void setColOff(java.lang.Object colOff);

    /**
     * Sets (as xml) the "colOff" element
     */
    void xsetColOff(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate colOff);

    /**
     * Gets the "row" element
     */
    int getRow();

    /**
     * Gets (as xml) the "row" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID xgetRow();

    /**
     * Sets the "row" element
     */
    void setRow(int row);

    /**
     * Sets (as xml) the "row" element
     */
    void xsetRow(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID row);

    /**
     * Gets the "rowOff" element
     */
    java.lang.Object getRowOff();

    /**
     * Gets (as xml) the "rowOff" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetRowOff();

    /**
     * Sets the "rowOff" element
     */
    void setRowOff(java.lang.Object rowOff);

    /**
     * Sets (as xml) the "rowOff" element
     */
    void xsetRowOff(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate rowOff);
}

/*
 * XML Type:  CT_Marker
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chartDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chartDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Marker(@http://schemas.openxmlformats.org/drawingml/2006/chartDrawing).
 *
 * This is a complex type.
 */
public interface CTMarker extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmarker2f3ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "x" element
     */
    double getX();

    /**
     * Gets (as xml) the "x" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate xgetX();

    /**
     * Sets the "x" element
     */
    void setX(double x);

    /**
     * Sets (as xml) the "x" element
     */
    void xsetX(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate x);

    /**
     * Gets the "y" element
     */
    double getY();

    /**
     * Gets (as xml) the "y" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate xgetY();

    /**
     * Sets the "y" element
     */
    void setY(double y);

    /**
     * Sets (as xml) the "y" element
     */
    void xsetY(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate y);
}

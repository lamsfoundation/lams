/*
 * An XML document type.
 * Localname: userShapes
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.UserShapesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one userShapes(@http://schemas.openxmlformats.org/drawingml/2006/chart) element.
 *
 * This is a complex type.
 */
public interface UserShapesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.UserShapesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "usershapesd66bdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "userShapes" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing getUserShapes();

    /**
     * Sets the "userShapes" element
     */
    void setUserShapes(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing userShapes);

    /**
     * Appends and returns a new empty "userShapes" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing addNewUserShapes();
}

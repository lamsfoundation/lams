/*
 * An XML document type.
 * Localname: lockedCanvas
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/lockedCanvas
 * Java type: org.openxmlformats.schemas.drawingml.x2006.lockedCanvas.LockedCanvasDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.lockedCanvas;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one lockedCanvas(@http://schemas.openxmlformats.org/drawingml/2006/lockedCanvas) element.
 *
 * This is a complex type.
 */
public interface LockedCanvasDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.lockedCanvas.LockedCanvasDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "lockedcanvas3d3adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "lockedCanvas" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape getLockedCanvas();

    /**
     * Sets the "lockedCanvas" element
     */
    void setLockedCanvas(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape lockedCanvas);

    /**
     * Appends and returns a new empty "lockedCanvas" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape addNewLockedCanvas();
}

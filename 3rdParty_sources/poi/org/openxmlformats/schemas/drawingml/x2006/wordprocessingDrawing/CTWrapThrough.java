/*
 * XML Type:  CT_WrapThrough
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapThrough
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WrapThrough(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTWrapThrough extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapThrough> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwrapthrough8b4etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "wrapPolygon" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapPath getWrapPolygon();

    /**
     * Sets the "wrapPolygon" element
     */
    void setWrapPolygon(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapPath wrapPolygon);

    /**
     * Appends and returns a new empty "wrapPolygon" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapPath addNewWrapPolygon();

    /**
     * Gets the "wrapText" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText.Enum getWrapText();

    /**
     * Gets (as xml) the "wrapText" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText xgetWrapText();

    /**
     * Sets the "wrapText" attribute
     */
    void setWrapText(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText.Enum wrapText);

    /**
     * Sets (as xml) the "wrapText" attribute
     */
    void xsetWrapText(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText wrapText);

    /**
     * Gets the "distL" attribute
     */
    long getDistL();

    /**
     * Gets (as xml) the "distL" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistL();

    /**
     * True if has "distL" attribute
     */
    boolean isSetDistL();

    /**
     * Sets the "distL" attribute
     */
    void setDistL(long distL);

    /**
     * Sets (as xml) the "distL" attribute
     */
    void xsetDistL(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distL);

    /**
     * Unsets the "distL" attribute
     */
    void unsetDistL();

    /**
     * Gets the "distR" attribute
     */
    long getDistR();

    /**
     * Gets (as xml) the "distR" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistR();

    /**
     * True if has "distR" attribute
     */
    boolean isSetDistR();

    /**
     * Sets the "distR" attribute
     */
    void setDistR(long distR);

    /**
     * Sets (as xml) the "distR" attribute
     */
    void xsetDistR(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distR);

    /**
     * Unsets the "distR" attribute
     */
    void unsetDistR();
}

/*
 * XML Type:  CT_AnchorClientData
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AnchorClientData(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public interface CTAnchorClientData extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctanchorclientdata02betype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fLocksWithSheet" attribute
     */
    boolean getFLocksWithSheet();

    /**
     * Gets (as xml) the "fLocksWithSheet" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFLocksWithSheet();

    /**
     * True if has "fLocksWithSheet" attribute
     */
    boolean isSetFLocksWithSheet();

    /**
     * Sets the "fLocksWithSheet" attribute
     */
    void setFLocksWithSheet(boolean fLocksWithSheet);

    /**
     * Sets (as xml) the "fLocksWithSheet" attribute
     */
    void xsetFLocksWithSheet(org.apache.xmlbeans.XmlBoolean fLocksWithSheet);

    /**
     * Unsets the "fLocksWithSheet" attribute
     */
    void unsetFLocksWithSheet();

    /**
     * Gets the "fPrintsWithSheet" attribute
     */
    boolean getFPrintsWithSheet();

    /**
     * Gets (as xml) the "fPrintsWithSheet" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFPrintsWithSheet();

    /**
     * True if has "fPrintsWithSheet" attribute
     */
    boolean isSetFPrintsWithSheet();

    /**
     * Sets the "fPrintsWithSheet" attribute
     */
    void setFPrintsWithSheet(boolean fPrintsWithSheet);

    /**
     * Sets (as xml) the "fPrintsWithSheet" attribute
     */
    void xsetFPrintsWithSheet(org.apache.xmlbeans.XmlBoolean fPrintsWithSheet);

    /**
     * Unsets the "fPrintsWithSheet" attribute
     */
    void unsetFPrintsWithSheet();
}

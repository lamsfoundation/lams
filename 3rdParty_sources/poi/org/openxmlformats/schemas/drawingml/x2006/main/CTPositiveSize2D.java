/*
 * XML Type:  CT_PositiveSize2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PositiveSize2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPositiveSize2D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpositivesize2d0147type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cx" attribute
     */
    long getCx();

    /**
     * Gets (as xml) the "cx" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetCx();

    /**
     * Sets the "cx" attribute
     */
    void setCx(long cx);

    /**
     * Sets (as xml) the "cx" attribute
     */
    void xsetCx(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate cx);

    /**
     * Gets the "cy" attribute
     */
    long getCy();

    /**
     * Gets (as xml) the "cy" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetCy();

    /**
     * Sets the "cy" attribute
     */
    void setCy(long cy);

    /**
     * Sets (as xml) the "cy" attribute
     */
    void xsetCy(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate cy);
}

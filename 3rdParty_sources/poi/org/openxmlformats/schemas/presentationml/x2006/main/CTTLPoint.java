/*
 * XML Type:  CT_TLPoint
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLPoint(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLPoint extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlpoint25b7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "x" attribute
     */
    java.lang.Object getX();

    /**
     * Gets (as xml) the "x" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetX();

    /**
     * Sets the "x" attribute
     */
    void setX(java.lang.Object x);

    /**
     * Sets (as xml) the "x" attribute
     */
    void xsetX(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage x);

    /**
     * Gets the "y" attribute
     */
    java.lang.Object getY();

    /**
     * Gets (as xml) the "y" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetY();

    /**
     * Sets the "y" attribute
     */
    void setY(java.lang.Object y);

    /**
     * Sets (as xml) the "y" attribute
     */
    void xsetY(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage y);
}

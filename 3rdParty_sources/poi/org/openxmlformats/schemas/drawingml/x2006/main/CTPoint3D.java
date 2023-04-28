/*
 * XML Type:  CT_Point3D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Point3D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPoint3D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpoint3dd472type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "x" attribute
     */
    java.lang.Object getX();

    /**
     * Gets (as xml) the "x" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetX();

    /**
     * Sets the "x" attribute
     */
    void setX(java.lang.Object x);

    /**
     * Sets (as xml) the "x" attribute
     */
    void xsetX(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate x);

    /**
     * Gets the "y" attribute
     */
    java.lang.Object getY();

    /**
     * Gets (as xml) the "y" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetY();

    /**
     * Sets the "y" attribute
     */
    void setY(java.lang.Object y);

    /**
     * Sets (as xml) the "y" attribute
     */
    void xsetY(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate y);

    /**
     * Gets the "z" attribute
     */
    java.lang.Object getZ();

    /**
     * Gets (as xml) the "z" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetZ();

    /**
     * Sets the "z" attribute
     */
    void setZ(java.lang.Object z);

    /**
     * Sets (as xml) the "z" attribute
     */
    void xsetZ(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate z);
}

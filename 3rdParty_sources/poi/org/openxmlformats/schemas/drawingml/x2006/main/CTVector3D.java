/*
 * XML Type:  CT_Vector3D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Vector3D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTVector3D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvector3d7c8ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dx" attribute
     */
    java.lang.Object getDx();

    /**
     * Gets (as xml) the "dx" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetDx();

    /**
     * Sets the "dx" attribute
     */
    void setDx(java.lang.Object dx);

    /**
     * Sets (as xml) the "dx" attribute
     */
    void xsetDx(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate dx);

    /**
     * Gets the "dy" attribute
     */
    java.lang.Object getDy();

    /**
     * Gets (as xml) the "dy" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetDy();

    /**
     * Sets the "dy" attribute
     */
    void setDy(java.lang.Object dy);

    /**
     * Sets (as xml) the "dy" attribute
     */
    void xsetDy(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate dy);

    /**
     * Gets the "dz" attribute
     */
    java.lang.Object getDz();

    /**
     * Gets (as xml) the "dz" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetDz();

    /**
     * Sets the "dz" attribute
     */
    void setDz(java.lang.Object dz);

    /**
     * Sets (as xml) the "dz" attribute
     */
    void xsetDz(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate dz);
}

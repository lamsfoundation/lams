/*
 * XML Type:  CT_ConnectionSite
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ConnectionSite(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTConnectionSite extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconnectionsite6660type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pos" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D getPos();

    /**
     * Sets the "pos" element
     */
    void setPos(org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D pos);

    /**
     * Appends and returns a new empty "pos" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D addNewPos();

    /**
     * Gets the "ang" attribute
     */
    java.lang.Object getAng();

    /**
     * Gets (as xml) the "ang" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle xgetAng();

    /**
     * Sets the "ang" attribute
     */
    void setAng(java.lang.Object ang);

    /**
     * Sets (as xml) the "ang" attribute
     */
    void xsetAng(org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle ang);
}

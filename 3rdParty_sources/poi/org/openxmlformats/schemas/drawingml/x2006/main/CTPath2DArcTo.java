/*
 * XML Type:  CT_Path2DArcTo
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Path2DArcTo(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPath2DArcTo extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DArcTo> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpath2darctodaa7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "wR" attribute
     */
    java.lang.Object getWR();

    /**
     * Gets (as xml) the "wR" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetWR();

    /**
     * Sets the "wR" attribute
     */
    void setWR(java.lang.Object wr);

    /**
     * Sets (as xml) the "wR" attribute
     */
    void xsetWR(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate wr);

    /**
     * Gets the "hR" attribute
     */
    java.lang.Object getHR();

    /**
     * Gets (as xml) the "hR" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetHR();

    /**
     * Sets the "hR" attribute
     */
    void setHR(java.lang.Object hr);

    /**
     * Sets (as xml) the "hR" attribute
     */
    void xsetHR(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate hr);

    /**
     * Gets the "stAng" attribute
     */
    java.lang.Object getStAng();

    /**
     * Gets (as xml) the "stAng" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle xgetStAng();

    /**
     * Sets the "stAng" attribute
     */
    void setStAng(java.lang.Object stAng);

    /**
     * Sets (as xml) the "stAng" attribute
     */
    void xsetStAng(org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle stAng);

    /**
     * Gets the "swAng" attribute
     */
    java.lang.Object getSwAng();

    /**
     * Gets (as xml) the "swAng" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle xgetSwAng();

    /**
     * Sets the "swAng" attribute
     */
    void setSwAng(java.lang.Object swAng);

    /**
     * Sets (as xml) the "swAng" attribute
     */
    void xsetSwAng(org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle swAng);
}

/*
 * XML Type:  CT_XYAdjustHandle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_XYAdjustHandle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTXYAdjustHandle extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctxyadjusthandlefaf3type");
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
     * Gets the "gdRefX" attribute
     */
    java.lang.String getGdRefX();

    /**
     * Gets (as xml) the "gdRefX" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName xgetGdRefX();

    /**
     * True if has "gdRefX" attribute
     */
    boolean isSetGdRefX();

    /**
     * Sets the "gdRefX" attribute
     */
    void setGdRefX(java.lang.String gdRefX);

    /**
     * Sets (as xml) the "gdRefX" attribute
     */
    void xsetGdRefX(org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName gdRefX);

    /**
     * Unsets the "gdRefX" attribute
     */
    void unsetGdRefX();

    /**
     * Gets the "minX" attribute
     */
    java.lang.Object getMinX();

    /**
     * Gets (as xml) the "minX" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetMinX();

    /**
     * True if has "minX" attribute
     */
    boolean isSetMinX();

    /**
     * Sets the "minX" attribute
     */
    void setMinX(java.lang.Object minX);

    /**
     * Sets (as xml) the "minX" attribute
     */
    void xsetMinX(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate minX);

    /**
     * Unsets the "minX" attribute
     */
    void unsetMinX();

    /**
     * Gets the "maxX" attribute
     */
    java.lang.Object getMaxX();

    /**
     * Gets (as xml) the "maxX" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetMaxX();

    /**
     * True if has "maxX" attribute
     */
    boolean isSetMaxX();

    /**
     * Sets the "maxX" attribute
     */
    void setMaxX(java.lang.Object maxX);

    /**
     * Sets (as xml) the "maxX" attribute
     */
    void xsetMaxX(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate maxX);

    /**
     * Unsets the "maxX" attribute
     */
    void unsetMaxX();

    /**
     * Gets the "gdRefY" attribute
     */
    java.lang.String getGdRefY();

    /**
     * Gets (as xml) the "gdRefY" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName xgetGdRefY();

    /**
     * True if has "gdRefY" attribute
     */
    boolean isSetGdRefY();

    /**
     * Sets the "gdRefY" attribute
     */
    void setGdRefY(java.lang.String gdRefY);

    /**
     * Sets (as xml) the "gdRefY" attribute
     */
    void xsetGdRefY(org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName gdRefY);

    /**
     * Unsets the "gdRefY" attribute
     */
    void unsetGdRefY();

    /**
     * Gets the "minY" attribute
     */
    java.lang.Object getMinY();

    /**
     * Gets (as xml) the "minY" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetMinY();

    /**
     * True if has "minY" attribute
     */
    boolean isSetMinY();

    /**
     * Sets the "minY" attribute
     */
    void setMinY(java.lang.Object minY);

    /**
     * Sets (as xml) the "minY" attribute
     */
    void xsetMinY(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate minY);

    /**
     * Unsets the "minY" attribute
     */
    void unsetMinY();

    /**
     * Gets the "maxY" attribute
     */
    java.lang.Object getMaxY();

    /**
     * Gets (as xml) the "maxY" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate xgetMaxY();

    /**
     * True if has "maxY" attribute
     */
    boolean isSetMaxY();

    /**
     * Sets the "maxY" attribute
     */
    void setMaxY(java.lang.Object maxY);

    /**
     * Sets (as xml) the "maxY" attribute
     */
    void xsetMaxY(org.openxmlformats.schemas.drawingml.x2006.main.STAdjCoordinate maxY);

    /**
     * Unsets the "maxY" attribute
     */
    void unsetMaxY();
}

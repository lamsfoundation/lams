/*
 * XML Type:  CT_Transform2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Transform2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTransform2D extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttransform2d5deftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "off" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D getOff();

    /**
     * True if has "off" element
     */
    boolean isSetOff();

    /**
     * Sets the "off" element
     */
    void setOff(org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D off);

    /**
     * Appends and returns a new empty "off" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D addNewOff();

    /**
     * Unsets the "off" element
     */
    void unsetOff();

    /**
     * Gets the "ext" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D getExt();

    /**
     * True if has "ext" element
     */
    boolean isSetExt();

    /**
     * Sets the "ext" element
     */
    void setExt(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D ext);

    /**
     * Appends and returns a new empty "ext" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D addNewExt();

    /**
     * Unsets the "ext" element
     */
    void unsetExt();

    /**
     * Gets the "rot" attribute
     */
    int getRot();

    /**
     * Gets (as xml) the "rot" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetRot();

    /**
     * True if has "rot" attribute
     */
    boolean isSetRot();

    /**
     * Sets the "rot" attribute
     */
    void setRot(int rot);

    /**
     * Sets (as xml) the "rot" attribute
     */
    void xsetRot(org.openxmlformats.schemas.drawingml.x2006.main.STAngle rot);

    /**
     * Unsets the "rot" attribute
     */
    void unsetRot();

    /**
     * Gets the "flipH" attribute
     */
    boolean getFlipH();

    /**
     * Gets (as xml) the "flipH" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFlipH();

    /**
     * True if has "flipH" attribute
     */
    boolean isSetFlipH();

    /**
     * Sets the "flipH" attribute
     */
    void setFlipH(boolean flipH);

    /**
     * Sets (as xml) the "flipH" attribute
     */
    void xsetFlipH(org.apache.xmlbeans.XmlBoolean flipH);

    /**
     * Unsets the "flipH" attribute
     */
    void unsetFlipH();

    /**
     * Gets the "flipV" attribute
     */
    boolean getFlipV();

    /**
     * Gets (as xml) the "flipV" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFlipV();

    /**
     * True if has "flipV" attribute
     */
    boolean isSetFlipV();

    /**
     * Sets the "flipV" attribute
     */
    void setFlipV(boolean flipV);

    /**
     * Sets (as xml) the "flipV" attribute
     */
    void xsetFlipV(org.apache.xmlbeans.XmlBoolean flipV);

    /**
     * Unsets the "flipV" attribute
     */
    void unsetFlipV();
}

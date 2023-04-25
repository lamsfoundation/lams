/*
 * XML Type:  CT_GradientFillProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GradientFillProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGradientFillProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgradientfillproperties81c1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "gsLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList getGsLst();

    /**
     * True if has "gsLst" element
     */
    boolean isSetGsLst();

    /**
     * Sets the "gsLst" element
     */
    void setGsLst(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList gsLst);

    /**
     * Appends and returns a new empty "gsLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList addNewGsLst();

    /**
     * Unsets the "gsLst" element
     */
    void unsetGsLst();

    /**
     * Gets the "lin" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties getLin();

    /**
     * True if has "lin" element
     */
    boolean isSetLin();

    /**
     * Sets the "lin" element
     */
    void setLin(org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties lin);

    /**
     * Appends and returns a new empty "lin" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLinearShadeProperties addNewLin();

    /**
     * Unsets the "lin" element
     */
    void unsetLin();

    /**
     * Gets the "path" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties getPath();

    /**
     * True if has "path" element
     */
    boolean isSetPath();

    /**
     * Sets the "path" element
     */
    void setPath(org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties path);

    /**
     * Appends and returns a new empty "path" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties addNewPath();

    /**
     * Unsets the "path" element
     */
    void unsetPath();

    /**
     * Gets the "tileRect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect getTileRect();

    /**
     * True if has "tileRect" element
     */
    boolean isSetTileRect();

    /**
     * Sets the "tileRect" element
     */
    void setTileRect(org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect tileRect);

    /**
     * Appends and returns a new empty "tileRect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect addNewTileRect();

    /**
     * Unsets the "tileRect" element
     */
    void unsetTileRect();

    /**
     * Gets the "flip" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode.Enum getFlip();

    /**
     * Gets (as xml) the "flip" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode xgetFlip();

    /**
     * True if has "flip" attribute
     */
    boolean isSetFlip();

    /**
     * Sets the "flip" attribute
     */
    void setFlip(org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode.Enum flip);

    /**
     * Sets (as xml) the "flip" attribute
     */
    void xsetFlip(org.openxmlformats.schemas.drawingml.x2006.main.STTileFlipMode flip);

    /**
     * Unsets the "flip" attribute
     */
    void unsetFlip();

    /**
     * Gets the "rotWithShape" attribute
     */
    boolean getRotWithShape();

    /**
     * Gets (as xml) the "rotWithShape" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetRotWithShape();

    /**
     * True if has "rotWithShape" attribute
     */
    boolean isSetRotWithShape();

    /**
     * Sets the "rotWithShape" attribute
     */
    void setRotWithShape(boolean rotWithShape);

    /**
     * Sets (as xml) the "rotWithShape" attribute
     */
    void xsetRotWithShape(org.apache.xmlbeans.XmlBoolean rotWithShape);

    /**
     * Unsets the "rotWithShape" attribute
     */
    void unsetRotWithShape();
}

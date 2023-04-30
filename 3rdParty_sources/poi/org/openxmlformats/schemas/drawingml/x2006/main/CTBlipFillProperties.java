/*
 * XML Type:  CT_BlipFillProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BlipFillProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBlipFillProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctblipfillproperties0382type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "blip" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlip getBlip();

    /**
     * True if has "blip" element
     */
    boolean isSetBlip();

    /**
     * Sets the "blip" element
     */
    void setBlip(org.openxmlformats.schemas.drawingml.x2006.main.CTBlip blip);

    /**
     * Appends and returns a new empty "blip" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlip addNewBlip();

    /**
     * Unsets the "blip" element
     */
    void unsetBlip();

    /**
     * Gets the "srcRect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect getSrcRect();

    /**
     * True if has "srcRect" element
     */
    boolean isSetSrcRect();

    /**
     * Sets the "srcRect" element
     */
    void setSrcRect(org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect srcRect);

    /**
     * Appends and returns a new empty "srcRect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect addNewSrcRect();

    /**
     * Unsets the "srcRect" element
     */
    void unsetSrcRect();

    /**
     * Gets the "tile" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties getTile();

    /**
     * True if has "tile" element
     */
    boolean isSetTile();

    /**
     * Sets the "tile" element
     */
    void setTile(org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties tile);

    /**
     * Appends and returns a new empty "tile" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties addNewTile();

    /**
     * Unsets the "tile" element
     */
    void unsetTile();

    /**
     * Gets the "stretch" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties getStretch();

    /**
     * True if has "stretch" element
     */
    boolean isSetStretch();

    /**
     * Sets the "stretch" element
     */
    void setStretch(org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties stretch);

    /**
     * Appends and returns a new empty "stretch" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties addNewStretch();

    /**
     * Unsets the "stretch" element
     */
    void unsetStretch();

    /**
     * Gets the "dpi" attribute
     */
    long getDpi();

    /**
     * Gets (as xml) the "dpi" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetDpi();

    /**
     * True if has "dpi" attribute
     */
    boolean isSetDpi();

    /**
     * Sets the "dpi" attribute
     */
    void setDpi(long dpi);

    /**
     * Sets (as xml) the "dpi" attribute
     */
    void xsetDpi(org.apache.xmlbeans.XmlUnsignedInt dpi);

    /**
     * Unsets the "dpi" attribute
     */
    void unsetDpi();

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

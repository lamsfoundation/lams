/*
 * XML Type:  CT_TileInfoProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TileInfoProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTileInfoProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTileInfoProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttileinfoproperties2featype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tx" attribute
     */
    java.lang.Object getTx();

    /**
     * Gets (as xml) the "tx" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetTx();

    /**
     * True if has "tx" attribute
     */
    boolean isSetTx();

    /**
     * Sets the "tx" attribute
     */
    void setTx(java.lang.Object tx);

    /**
     * Sets (as xml) the "tx" attribute
     */
    void xsetTx(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate tx);

    /**
     * Unsets the "tx" attribute
     */
    void unsetTx();

    /**
     * Gets the "ty" attribute
     */
    java.lang.Object getTy();

    /**
     * Gets (as xml) the "ty" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetTy();

    /**
     * True if has "ty" attribute
     */
    boolean isSetTy();

    /**
     * Sets the "ty" attribute
     */
    void setTy(java.lang.Object ty);

    /**
     * Sets (as xml) the "ty" attribute
     */
    void xsetTy(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate ty);

    /**
     * Unsets the "ty" attribute
     */
    void unsetTy();

    /**
     * Gets the "sx" attribute
     */
    java.lang.Object getSx();

    /**
     * Gets (as xml) the "sx" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetSx();

    /**
     * True if has "sx" attribute
     */
    boolean isSetSx();

    /**
     * Sets the "sx" attribute
     */
    void setSx(java.lang.Object sx);

    /**
     * Sets (as xml) the "sx" attribute
     */
    void xsetSx(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage sx);

    /**
     * Unsets the "sx" attribute
     */
    void unsetSx();

    /**
     * Gets the "sy" attribute
     */
    java.lang.Object getSy();

    /**
     * Gets (as xml) the "sy" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetSy();

    /**
     * True if has "sy" attribute
     */
    boolean isSetSy();

    /**
     * Sets the "sy" attribute
     */
    void setSy(java.lang.Object sy);

    /**
     * Sets (as xml) the "sy" attribute
     */
    void xsetSy(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage sy);

    /**
     * Unsets the "sy" attribute
     */
    void unsetSy();

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
     * Gets the "algn" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STRectAlignment.Enum getAlgn();

    /**
     * Gets (as xml) the "algn" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STRectAlignment xgetAlgn();

    /**
     * True if has "algn" attribute
     */
    boolean isSetAlgn();

    /**
     * Sets the "algn" attribute
     */
    void setAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STRectAlignment.Enum algn);

    /**
     * Sets (as xml) the "algn" attribute
     */
    void xsetAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STRectAlignment algn);

    /**
     * Unsets the "algn" attribute
     */
    void unsetAlgn();
}

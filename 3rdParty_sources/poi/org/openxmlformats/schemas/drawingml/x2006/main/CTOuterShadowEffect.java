/*
 * XML Type:  CT_OuterShadowEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OuterShadowEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOuterShadowEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoutershadoweffect7b5dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "scrgbClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor getScrgbClr();

    /**
     * True if has "scrgbClr" element
     */
    boolean isSetScrgbClr();

    /**
     * Sets the "scrgbClr" element
     */
    void setScrgbClr(org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor scrgbClr);

    /**
     * Appends and returns a new empty "scrgbClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor addNewScrgbClr();

    /**
     * Unsets the "scrgbClr" element
     */
    void unsetScrgbClr();

    /**
     * Gets the "srgbClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor getSrgbClr();

    /**
     * True if has "srgbClr" element
     */
    boolean isSetSrgbClr();

    /**
     * Sets the "srgbClr" element
     */
    void setSrgbClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor srgbClr);

    /**
     * Appends and returns a new empty "srgbClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor addNewSrgbClr();

    /**
     * Unsets the "srgbClr" element
     */
    void unsetSrgbClr();

    /**
     * Gets the "hslClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor getHslClr();

    /**
     * True if has "hslClr" element
     */
    boolean isSetHslClr();

    /**
     * Sets the "hslClr" element
     */
    void setHslClr(org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor hslClr);

    /**
     * Appends and returns a new empty "hslClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor addNewHslClr();

    /**
     * Unsets the "hslClr" element
     */
    void unsetHslClr();

    /**
     * Gets the "sysClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor getSysClr();

    /**
     * True if has "sysClr" element
     */
    boolean isSetSysClr();

    /**
     * Sets the "sysClr" element
     */
    void setSysClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor sysClr);

    /**
     * Appends and returns a new empty "sysClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor addNewSysClr();

    /**
     * Unsets the "sysClr" element
     */
    void unsetSysClr();

    /**
     * Gets the "schemeClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor getSchemeClr();

    /**
     * True if has "schemeClr" element
     */
    boolean isSetSchemeClr();

    /**
     * Sets the "schemeClr" element
     */
    void setSchemeClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor schemeClr);

    /**
     * Appends and returns a new empty "schemeClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor addNewSchemeClr();

    /**
     * Unsets the "schemeClr" element
     */
    void unsetSchemeClr();

    /**
     * Gets the "prstClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor getPrstClr();

    /**
     * True if has "prstClr" element
     */
    boolean isSetPrstClr();

    /**
     * Sets the "prstClr" element
     */
    void setPrstClr(org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor prstClr);

    /**
     * Appends and returns a new empty "prstClr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor addNewPrstClr();

    /**
     * Unsets the "prstClr" element
     */
    void unsetPrstClr();

    /**
     * Gets the "blurRad" attribute
     */
    long getBlurRad();

    /**
     * Gets (as xml) the "blurRad" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetBlurRad();

    /**
     * True if has "blurRad" attribute
     */
    boolean isSetBlurRad();

    /**
     * Sets the "blurRad" attribute
     */
    void setBlurRad(long blurRad);

    /**
     * Sets (as xml) the "blurRad" attribute
     */
    void xsetBlurRad(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate blurRad);

    /**
     * Unsets the "blurRad" attribute
     */
    void unsetBlurRad();

    /**
     * Gets the "dist" attribute
     */
    long getDist();

    /**
     * Gets (as xml) the "dist" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetDist();

    /**
     * True if has "dist" attribute
     */
    boolean isSetDist();

    /**
     * Sets the "dist" attribute
     */
    void setDist(long dist);

    /**
     * Sets (as xml) the "dist" attribute
     */
    void xsetDist(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate dist);

    /**
     * Unsets the "dist" attribute
     */
    void unsetDist();

    /**
     * Gets the "dir" attribute
     */
    int getDir();

    /**
     * Gets (as xml) the "dir" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle xgetDir();

    /**
     * True if has "dir" attribute
     */
    boolean isSetDir();

    /**
     * Sets the "dir" attribute
     */
    void setDir(int dir);

    /**
     * Sets (as xml) the "dir" attribute
     */
    void xsetDir(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle dir);

    /**
     * Unsets the "dir" attribute
     */
    void unsetDir();

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
     * Gets the "kx" attribute
     */
    int getKx();

    /**
     * Gets (as xml) the "kx" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle xgetKx();

    /**
     * True if has "kx" attribute
     */
    boolean isSetKx();

    /**
     * Sets the "kx" attribute
     */
    void setKx(int kx);

    /**
     * Sets (as xml) the "kx" attribute
     */
    void xsetKx(org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle kx);

    /**
     * Unsets the "kx" attribute
     */
    void unsetKx();

    /**
     * Gets the "ky" attribute
     */
    int getKy();

    /**
     * Gets (as xml) the "ky" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle xgetKy();

    /**
     * True if has "ky" attribute
     */
    boolean isSetKy();

    /**
     * Sets the "ky" attribute
     */
    void setKy(int ky);

    /**
     * Sets (as xml) the "ky" attribute
     */
    void xsetKy(org.openxmlformats.schemas.drawingml.x2006.main.STFixedAngle ky);

    /**
     * Unsets the "ky" attribute
     */
    void unsetKy();

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

/*
 * XML Type:  CT_ReflectionEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ReflectionEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTReflectionEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctreflectioneffect9c37type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
     * Gets the "stA" attribute
     */
    java.lang.Object getStA();

    /**
     * Gets (as xml) the "stA" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetStA();

    /**
     * True if has "stA" attribute
     */
    boolean isSetStA();

    /**
     * Sets the "stA" attribute
     */
    void setStA(java.lang.Object stA);

    /**
     * Sets (as xml) the "stA" attribute
     */
    void xsetStA(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage stA);

    /**
     * Unsets the "stA" attribute
     */
    void unsetStA();

    /**
     * Gets the "stPos" attribute
     */
    java.lang.Object getStPos();

    /**
     * Gets (as xml) the "stPos" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetStPos();

    /**
     * True if has "stPos" attribute
     */
    boolean isSetStPos();

    /**
     * Sets the "stPos" attribute
     */
    void setStPos(java.lang.Object stPos);

    /**
     * Sets (as xml) the "stPos" attribute
     */
    void xsetStPos(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage stPos);

    /**
     * Unsets the "stPos" attribute
     */
    void unsetStPos();

    /**
     * Gets the "endA" attribute
     */
    java.lang.Object getEndA();

    /**
     * Gets (as xml) the "endA" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetEndA();

    /**
     * True if has "endA" attribute
     */
    boolean isSetEndA();

    /**
     * Sets the "endA" attribute
     */
    void setEndA(java.lang.Object endA);

    /**
     * Sets (as xml) the "endA" attribute
     */
    void xsetEndA(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage endA);

    /**
     * Unsets the "endA" attribute
     */
    void unsetEndA();

    /**
     * Gets the "endPos" attribute
     */
    java.lang.Object getEndPos();

    /**
     * Gets (as xml) the "endPos" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetEndPos();

    /**
     * True if has "endPos" attribute
     */
    boolean isSetEndPos();

    /**
     * Sets the "endPos" attribute
     */
    void setEndPos(java.lang.Object endPos);

    /**
     * Sets (as xml) the "endPos" attribute
     */
    void xsetEndPos(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage endPos);

    /**
     * Unsets the "endPos" attribute
     */
    void unsetEndPos();

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
     * Gets the "fadeDir" attribute
     */
    int getFadeDir();

    /**
     * Gets (as xml) the "fadeDir" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle xgetFadeDir();

    /**
     * True if has "fadeDir" attribute
     */
    boolean isSetFadeDir();

    /**
     * Sets the "fadeDir" attribute
     */
    void setFadeDir(int fadeDir);

    /**
     * Sets (as xml) the "fadeDir" attribute
     */
    void xsetFadeDir(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle fadeDir);

    /**
     * Unsets the "fadeDir" attribute
     */
    void unsetFadeDir();

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

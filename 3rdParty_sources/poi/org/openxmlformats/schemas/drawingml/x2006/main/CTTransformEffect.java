/*
 * XML Type:  CT_TransformEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTransformEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TransformEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTransformEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTransformEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttransformeffect948etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
}

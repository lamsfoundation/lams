/*
 * XML Type:  CT_FillOverlayEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FillOverlayEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFillOverlayEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfilloverlayeffecte42ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "noFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties getNoFill();

    /**
     * True if has "noFill" element
     */
    boolean isSetNoFill();

    /**
     * Sets the "noFill" element
     */
    void setNoFill(org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties noFill);

    /**
     * Appends and returns a new empty "noFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties addNewNoFill();

    /**
     * Unsets the "noFill" element
     */
    void unsetNoFill();

    /**
     * Gets the "solidFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties getSolidFill();

    /**
     * True if has "solidFill" element
     */
    boolean isSetSolidFill();

    /**
     * Sets the "solidFill" element
     */
    void setSolidFill(org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties solidFill);

    /**
     * Appends and returns a new empty "solidFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties addNewSolidFill();

    /**
     * Unsets the "solidFill" element
     */
    void unsetSolidFill();

    /**
     * Gets the "gradFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties getGradFill();

    /**
     * True if has "gradFill" element
     */
    boolean isSetGradFill();

    /**
     * Sets the "gradFill" element
     */
    void setGradFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties gradFill);

    /**
     * Appends and returns a new empty "gradFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties addNewGradFill();

    /**
     * Unsets the "gradFill" element
     */
    void unsetGradFill();

    /**
     * Gets the "blipFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties getBlipFill();

    /**
     * True if has "blipFill" element
     */
    boolean isSetBlipFill();

    /**
     * Sets the "blipFill" element
     */
    void setBlipFill(org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties blipFill);

    /**
     * Appends and returns a new empty "blipFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties addNewBlipFill();

    /**
     * Unsets the "blipFill" element
     */
    void unsetBlipFill();

    /**
     * Gets the "pattFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties getPattFill();

    /**
     * True if has "pattFill" element
     */
    boolean isSetPattFill();

    /**
     * Sets the "pattFill" element
     */
    void setPattFill(org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties pattFill);

    /**
     * Appends and returns a new empty "pattFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties addNewPattFill();

    /**
     * Unsets the "pattFill" element
     */
    void unsetPattFill();

    /**
     * Gets the "grpFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties getGrpFill();

    /**
     * True if has "grpFill" element
     */
    boolean isSetGrpFill();

    /**
     * Sets the "grpFill" element
     */
    void setGrpFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties grpFill);

    /**
     * Appends and returns a new empty "grpFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties addNewGrpFill();

    /**
     * Unsets the "grpFill" element
     */
    void unsetGrpFill();

    /**
     * Gets the "blend" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode.Enum getBlend();

    /**
     * Gets (as xml) the "blend" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode xgetBlend();

    /**
     * Sets the "blend" attribute
     */
    void setBlend(org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode.Enum blend);

    /**
     * Sets (as xml) the "blend" attribute
     */
    void xsetBlend(org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode blend);
}

/*
 * XML Type:  CT_PosV
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTPosV
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PosV(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTPosV extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTPosV> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctposv63ddtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "align" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV.Enum getAlign();

    /**
     * Gets (as xml) the "align" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV xgetAlign();

    /**
     * True if has "align" element
     */
    boolean isSetAlign();

    /**
     * Sets the "align" element
     */
    void setAlign(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV.Enum align);

    /**
     * Sets (as xml) the "align" element
     */
    void xsetAlign(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV align);

    /**
     * Unsets the "align" element
     */
    void unsetAlign();

    /**
     * Gets the "posOffset" element
     */
    int getPosOffset();

    /**
     * Gets (as xml) the "posOffset" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset xgetPosOffset();

    /**
     * True if has "posOffset" element
     */
    boolean isSetPosOffset();

    /**
     * Sets the "posOffset" element
     */
    void setPosOffset(int posOffset);

    /**
     * Sets (as xml) the "posOffset" element
     */
    void xsetPosOffset(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STPositionOffset posOffset);

    /**
     * Unsets the "posOffset" element
     */
    void unsetPosOffset();

    /**
     * Gets the "relativeFrom" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum getRelativeFrom();

    /**
     * Gets (as xml) the "relativeFrom" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV xgetRelativeFrom();

    /**
     * Sets the "relativeFrom" attribute
     */
    void setRelativeFrom(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum relativeFrom);

    /**
     * Sets (as xml) the "relativeFrom" attribute
     */
    void xsetRelativeFrom(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV relativeFrom);
}

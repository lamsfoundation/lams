/*
 * XML Type:  CT_WordprocessingContentPart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WordprocessingContentPart(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTWordprocessingContentPart extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwordprocessingcontentpart78fatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "nvContentPartPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual getNvContentPartPr();

    /**
     * True if has "nvContentPartPr" element
     */
    boolean isSetNvContentPartPr();

    /**
     * Sets the "nvContentPartPr" element
     */
    void setNvContentPartPr(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual nvContentPartPr);

    /**
     * Appends and returns a new empty "nvContentPartPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual addNewNvContentPartPr();

    /**
     * Unsets the "nvContentPartPr" element
     */
    void unsetNvContentPartPr();

    /**
     * Gets the "xfrm" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D getXfrm();

    /**
     * True if has "xfrm" element
     */
    boolean isSetXfrm();

    /**
     * Sets the "xfrm" element
     */
    void setXfrm(org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D xfrm);

    /**
     * Appends and returns a new empty "xfrm" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D addNewXfrm();

    /**
     * Unsets the "xfrm" element
     */
    void unsetXfrm();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "bwMode" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum getBwMode();

    /**
     * Gets (as xml) the "bwMode" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode xgetBwMode();

    /**
     * True if has "bwMode" attribute
     */
    boolean isSetBwMode();

    /**
     * Sets the "bwMode" attribute
     */
    void setBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum bwMode);

    /**
     * Sets (as xml) the "bwMode" attribute
     */
    void xsetBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode bwMode);

    /**
     * Unsets the "bwMode" attribute
     */
    void unsetBwMode();

    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);
}

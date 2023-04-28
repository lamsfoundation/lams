/*
 * XML Type:  CT_WordprocessingShape
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WordprocessingShape(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTWordprocessingShape extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwordprocessingshape9945type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cNvPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps getCNvPr();

    /**
     * True if has "cNvPr" element
     */
    boolean isSetCNvPr();

    /**
     * Sets the "cNvPr" element
     */
    void setCNvPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps cNvPr);

    /**
     * Appends and returns a new empty "cNvPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps addNewCNvPr();

    /**
     * Unsets the "cNvPr" element
     */
    void unsetCNvPr();

    /**
     * Gets the "cNvSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps getCNvSpPr();

    /**
     * True if has "cNvSpPr" element
     */
    boolean isSetCNvSpPr();

    /**
     * Sets the "cNvSpPr" element
     */
    void setCNvSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps cNvSpPr);

    /**
     * Appends and returns a new empty "cNvSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps addNewCNvSpPr();

    /**
     * Unsets the "cNvSpPr" element
     */
    void unsetCNvSpPr();

    /**
     * Gets the "cNvCnPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties getCNvCnPr();

    /**
     * True if has "cNvCnPr" element
     */
    boolean isSetCNvCnPr();

    /**
     * Sets the "cNvCnPr" element
     */
    void setCNvCnPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties cNvCnPr);

    /**
     * Appends and returns a new empty "cNvCnPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties addNewCNvCnPr();

    /**
     * Unsets the "cNvCnPr" element
     */
    void unsetCNvCnPr();

    /**
     * Gets the "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr();

    /**
     * Sets the "spPr" element
     */
    void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr);

    /**
     * Appends and returns a new empty "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr();

    /**
     * Gets the "style" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle getStyle();

    /**
     * True if has "style" element
     */
    boolean isSetStyle();

    /**
     * Sets the "style" element
     */
    void setStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle style);

    /**
     * Appends and returns a new empty "style" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle addNewStyle();

    /**
     * Unsets the "style" element
     */
    void unsetStyle();

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
     * Gets the "txbx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo getTxbx();

    /**
     * True if has "txbx" element
     */
    boolean isSetTxbx();

    /**
     * Sets the "txbx" element
     */
    void setTxbx(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo txbx);

    /**
     * Appends and returns a new empty "txbx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo addNewTxbx();

    /**
     * Unsets the "txbx" element
     */
    void unsetTxbx();

    /**
     * Gets the "linkedTxbx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation getLinkedTxbx();

    /**
     * True if has "linkedTxbx" element
     */
    boolean isSetLinkedTxbx();

    /**
     * Sets the "linkedTxbx" element
     */
    void setLinkedTxbx(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation linkedTxbx);

    /**
     * Appends and returns a new empty "linkedTxbx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation addNewLinkedTxbx();

    /**
     * Unsets the "linkedTxbx" element
     */
    void unsetLinkedTxbx();

    /**
     * Gets the "bodyPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties getBodyPr();

    /**
     * Sets the "bodyPr" element
     */
    void setBodyPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties bodyPr);

    /**
     * Appends and returns a new empty "bodyPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties addNewBodyPr();

    /**
     * Gets the "normalEastAsianFlow" attribute
     */
    boolean getNormalEastAsianFlow();

    /**
     * Gets (as xml) the "normalEastAsianFlow" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetNormalEastAsianFlow();

    /**
     * True if has "normalEastAsianFlow" attribute
     */
    boolean isSetNormalEastAsianFlow();

    /**
     * Sets the "normalEastAsianFlow" attribute
     */
    void setNormalEastAsianFlow(boolean normalEastAsianFlow);

    /**
     * Sets (as xml) the "normalEastAsianFlow" attribute
     */
    void xsetNormalEastAsianFlow(org.apache.xmlbeans.XmlBoolean normalEastAsianFlow);

    /**
     * Unsets the "normalEastAsianFlow" attribute
     */
    void unsetNormalEastAsianFlow();
}

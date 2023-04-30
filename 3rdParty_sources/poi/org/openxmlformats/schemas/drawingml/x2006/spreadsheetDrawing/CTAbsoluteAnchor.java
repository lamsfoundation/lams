/*
 * XML Type:  CT_AbsoluteAnchor
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AbsoluteAnchor(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public interface CTAbsoluteAnchor extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctabsoluteanchore360type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pos" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D getPos();

    /**
     * Sets the "pos" element
     */
    void setPos(org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D pos);

    /**
     * Appends and returns a new empty "pos" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D addNewPos();

    /**
     * Gets the "ext" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D getExt();

    /**
     * Sets the "ext" element
     */
    void setExt(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D ext);

    /**
     * Appends and returns a new empty "ext" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D addNewExt();

    /**
     * Gets the "sp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape getSp();

    /**
     * True if has "sp" element
     */
    boolean isSetSp();

    /**
     * Sets the "sp" element
     */
    void setSp(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape sp);

    /**
     * Appends and returns a new empty "sp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape addNewSp();

    /**
     * Unsets the "sp" element
     */
    void unsetSp();

    /**
     * Gets the "grpSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape getGrpSp();

    /**
     * True if has "grpSp" element
     */
    boolean isSetGrpSp();

    /**
     * Sets the "grpSp" element
     */
    void setGrpSp(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape grpSp);

    /**
     * Appends and returns a new empty "grpSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape addNewGrpSp();

    /**
     * Unsets the "grpSp" element
     */
    void unsetGrpSp();

    /**
     * Gets the "graphicFrame" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame getGraphicFrame();

    /**
     * True if has "graphicFrame" element
     */
    boolean isSetGraphicFrame();

    /**
     * Sets the "graphicFrame" element
     */
    void setGraphicFrame(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame graphicFrame);

    /**
     * Appends and returns a new empty "graphicFrame" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame addNewGraphicFrame();

    /**
     * Unsets the "graphicFrame" element
     */
    void unsetGraphicFrame();

    /**
     * Gets the "cxnSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector getCxnSp();

    /**
     * True if has "cxnSp" element
     */
    boolean isSetCxnSp();

    /**
     * Sets the "cxnSp" element
     */
    void setCxnSp(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector cxnSp);

    /**
     * Appends and returns a new empty "cxnSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector addNewCxnSp();

    /**
     * Unsets the "cxnSp" element
     */
    void unsetCxnSp();

    /**
     * Gets the "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture getPic();

    /**
     * True if has "pic" element
     */
    boolean isSetPic();

    /**
     * Sets the "pic" element
     */
    void setPic(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture pic);

    /**
     * Appends and returns a new empty "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture addNewPic();

    /**
     * Unsets the "pic" element
     */
    void unsetPic();

    /**
     * Gets the "contentPart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel getContentPart();

    /**
     * True if has "contentPart" element
     */
    boolean isSetContentPart();

    /**
     * Sets the "contentPart" element
     */
    void setContentPart(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel contentPart);

    /**
     * Appends and returns a new empty "contentPart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel addNewContentPart();

    /**
     * Unsets the "contentPart" element
     */
    void unsetContentPart();

    /**
     * Gets the "clientData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData getClientData();

    /**
     * Sets the "clientData" element
     */
    void setClientData(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData clientData);

    /**
     * Appends and returns a new empty "clientData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData addNewClientData();
}

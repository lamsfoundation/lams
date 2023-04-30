/*
 * XML Type:  CT_WordprocessingGroup
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WordprocessingGroup(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTWordprocessingGroup extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwordprocessinggroup0ca7type");
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
     * Gets the "cNvGrpSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps getCNvGrpSpPr();

    /**
     * Sets the "cNvGrpSpPr" element
     */
    void setCNvGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps cNvGrpSpPr);

    /**
     * Appends and returns a new empty "cNvGrpSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps addNewCNvGrpSpPr();

    /**
     * Gets the "grpSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties getGrpSpPr();

    /**
     * Sets the "grpSpPr" element
     */
    void setGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties grpSpPr);

    /**
     * Appends and returns a new empty "grpSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties addNewGrpSpPr();

    /**
     * Gets a List of "wsp" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape> getWspList();

    /**
     * Gets array of all "wsp" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape[] getWspArray();

    /**
     * Gets ith "wsp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape getWspArray(int i);

    /**
     * Returns number of "wsp" element
     */
    int sizeOfWspArray();

    /**
     * Sets array of all "wsp" element
     */
    void setWspArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape[] wspArray);

    /**
     * Sets ith "wsp" element
     */
    void setWspArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape wsp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "wsp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape insertNewWsp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "wsp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape addNewWsp();

    /**
     * Removes the ith "wsp" element
     */
    void removeWsp(int i);

    /**
     * Gets a List of "grpSp" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup> getGrpSpList();

    /**
     * Gets array of all "grpSp" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[] getGrpSpArray();

    /**
     * Gets ith "grpSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup getGrpSpArray(int i);

    /**
     * Returns number of "grpSp" element
     */
    int sizeOfGrpSpArray();

    /**
     * Sets array of all "grpSp" element
     */
    void setGrpSpArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[] grpSpArray);

    /**
     * Sets ith "grpSp" element
     */
    void setGrpSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup grpSp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "grpSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup insertNewGrpSp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "grpSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup addNewGrpSp();

    /**
     * Removes the ith "grpSp" element
     */
    void removeGrpSp(int i);

    /**
     * Gets a List of "graphicFrame" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame> getGraphicFrameList();

    /**
     * Gets array of all "graphicFrame" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame[] getGraphicFrameArray();

    /**
     * Gets ith "graphicFrame" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame getGraphicFrameArray(int i);

    /**
     * Returns number of "graphicFrame" element
     */
    int sizeOfGraphicFrameArray();

    /**
     * Sets array of all "graphicFrame" element
     */
    void setGraphicFrameArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame[] graphicFrameArray);

    /**
     * Sets ith "graphicFrame" element
     */
    void setGraphicFrameArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame graphicFrame);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "graphicFrame" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame insertNewGraphicFrame(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "graphicFrame" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame addNewGraphicFrame();

    /**
     * Removes the ith "graphicFrame" element
     */
    void removeGraphicFrame(int i);

    /**
     * Gets a List of "pic" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture> getPicList();

    /**
     * Gets array of all "pic" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture[] getPicArray();

    /**
     * Gets ith "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture getPicArray(int i);

    /**
     * Returns number of "pic" element
     */
    int sizeOfPicArray();

    /**
     * Sets array of all "pic" element
     */
    void setPicArray(org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture[] picArray);

    /**
     * Sets ith "pic" element
     */
    void setPicArray(int i, org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture pic);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture insertNewPic(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture addNewPic();

    /**
     * Removes the ith "pic" element
     */
    void removePic(int i);

    /**
     * Gets a List of "contentPart" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart> getContentPartList();

    /**
     * Gets array of all "contentPart" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart[] getContentPartArray();

    /**
     * Gets ith "contentPart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart getContentPartArray(int i);

    /**
     * Returns number of "contentPart" element
     */
    int sizeOfContentPartArray();

    /**
     * Sets array of all "contentPart" element
     */
    void setContentPartArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart[] contentPartArray);

    /**
     * Sets ith "contentPart" element
     */
    void setContentPartArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart contentPart);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "contentPart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart insertNewContentPart(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "contentPart" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart addNewContentPart();

    /**
     * Removes the ith "contentPart" element
     */
    void removeContentPart(int i);

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
}

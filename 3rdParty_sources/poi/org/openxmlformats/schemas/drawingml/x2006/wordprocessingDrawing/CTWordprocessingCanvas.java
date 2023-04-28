/*
 * XML Type:  CT_WordprocessingCanvas
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingCanvas
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WordprocessingCanvas(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public interface CTWordprocessingCanvas extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingCanvas> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwordprocessingcanvas11aatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bg" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting getBg();

    /**
     * True if has "bg" element
     */
    boolean isSetBg();

    /**
     * Sets the "bg" element
     */
    void setBg(org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting bg);

    /**
     * Appends and returns a new empty "bg" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting addNewBg();

    /**
     * Unsets the "bg" element
     */
    void unsetBg();

    /**
     * Gets the "whole" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting getWhole();

    /**
     * True if has "whole" element
     */
    boolean isSetWhole();

    /**
     * Sets the "whole" element
     */
    void setWhole(org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting whole);

    /**
     * Appends and returns a new empty "whole" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting addNewWhole();

    /**
     * Unsets the "whole" element
     */
    void unsetWhole();

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
     * Gets a List of "wgp" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup> getWgpList();

    /**
     * Gets array of all "wgp" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[] getWgpArray();

    /**
     * Gets ith "wgp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup getWgpArray(int i);

    /**
     * Returns number of "wgp" element
     */
    int sizeOfWgpArray();

    /**
     * Sets array of all "wgp" element
     */
    void setWgpArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[] wgpArray);

    /**
     * Sets ith "wgp" element
     */
    void setWgpArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup wgp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "wgp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup insertNewWgp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "wgp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup addNewWgp();

    /**
     * Removes the ith "wgp" element
     */
    void removeWgp(int i);

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

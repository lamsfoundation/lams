/*
 * XML Type:  CT_GroupShape
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GroupShape(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGroupShape extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgroupshape5b43type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "nvGrpSpPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShapeNonVisual getNvGrpSpPr();

    /**
     * Sets the "nvGrpSpPr" element
     */
    void setNvGrpSpPr(org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShapeNonVisual nvGrpSpPr);

    /**
     * Appends and returns a new empty "nvGrpSpPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShapeNonVisual addNewNvGrpSpPr();

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
     * Gets a List of "sp" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTShape> getSpList();

    /**
     * Gets array of all "sp" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTShape[] getSpArray();

    /**
     * Gets ith "sp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTShape getSpArray(int i);

    /**
     * Returns number of "sp" element
     */
    int sizeOfSpArray();

    /**
     * Sets array of all "sp" element
     */
    void setSpArray(org.openxmlformats.schemas.presentationml.x2006.main.CTShape[] spArray);

    /**
     * Sets ith "sp" element
     */
    void setSpArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTShape sp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTShape insertNewSp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTShape addNewSp();

    /**
     * Removes the ith "sp" element
     */
    void removeSp(int i);

    /**
     * Gets a List of "grpSp" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape> getGrpSpList();

    /**
     * Gets array of all "grpSp" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape[] getGrpSpArray();

    /**
     * Gets ith "grpSp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape getGrpSpArray(int i);

    /**
     * Returns number of "grpSp" element
     */
    int sizeOfGrpSpArray();

    /**
     * Sets array of all "grpSp" element
     */
    void setGrpSpArray(org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape[] grpSpArray);

    /**
     * Sets ith "grpSp" element
     */
    void setGrpSpArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape grpSp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "grpSp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape insertNewGrpSp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "grpSp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape addNewGrpSp();

    /**
     * Removes the ith "grpSp" element
     */
    void removeGrpSp(int i);

    /**
     * Gets a List of "graphicFrame" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame> getGraphicFrameList();

    /**
     * Gets array of all "graphicFrame" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame[] getGraphicFrameArray();

    /**
     * Gets ith "graphicFrame" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame getGraphicFrameArray(int i);

    /**
     * Returns number of "graphicFrame" element
     */
    int sizeOfGraphicFrameArray();

    /**
     * Sets array of all "graphicFrame" element
     */
    void setGraphicFrameArray(org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame[] graphicFrameArray);

    /**
     * Sets ith "graphicFrame" element
     */
    void setGraphicFrameArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame graphicFrame);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "graphicFrame" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame insertNewGraphicFrame(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "graphicFrame" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame addNewGraphicFrame();

    /**
     * Removes the ith "graphicFrame" element
     */
    void removeGraphicFrame(int i);

    /**
     * Gets a List of "cxnSp" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTConnector> getCxnSpList();

    /**
     * Gets array of all "cxnSp" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTConnector[] getCxnSpArray();

    /**
     * Gets ith "cxnSp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTConnector getCxnSpArray(int i);

    /**
     * Returns number of "cxnSp" element
     */
    int sizeOfCxnSpArray();

    /**
     * Sets array of all "cxnSp" element
     */
    void setCxnSpArray(org.openxmlformats.schemas.presentationml.x2006.main.CTConnector[] cxnSpArray);

    /**
     * Sets ith "cxnSp" element
     */
    void setCxnSpArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTConnector cxnSp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cxnSp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTConnector insertNewCxnSp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cxnSp" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTConnector addNewCxnSp();

    /**
     * Removes the ith "cxnSp" element
     */
    void removeCxnSp(int i);

    /**
     * Gets a List of "pic" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTPicture> getPicList();

    /**
     * Gets array of all "pic" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTPicture[] getPicArray();

    /**
     * Gets ith "pic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTPicture getPicArray(int i);

    /**
     * Returns number of "pic" element
     */
    int sizeOfPicArray();

    /**
     * Sets array of all "pic" element
     */
    void setPicArray(org.openxmlformats.schemas.presentationml.x2006.main.CTPicture[] picArray);

    /**
     * Sets ith "pic" element
     */
    void setPicArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTPicture pic);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTPicture insertNewPic(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTPicture addNewPic();

    /**
     * Removes the ith "pic" element
     */
    void removePic(int i);

    /**
     * Gets a List of "contentPart" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTRel> getContentPartList();

    /**
     * Gets array of all "contentPart" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTRel[] getContentPartArray();

    /**
     * Gets ith "contentPart" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTRel getContentPartArray(int i);

    /**
     * Returns number of "contentPart" element
     */
    int sizeOfContentPartArray();

    /**
     * Sets array of all "contentPart" element
     */
    void setContentPartArray(org.openxmlformats.schemas.presentationml.x2006.main.CTRel[] contentPartArray);

    /**
     * Sets ith "contentPart" element
     */
    void setContentPartArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTRel contentPart);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "contentPart" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTRel insertNewContentPart(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "contentPart" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTRel addNewContentPart();

    /**
     * Removes the ith "contentPart" element
     */
    void removeContentPart(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}

/*
 * XML Type:  CT_GvmlGroupShape
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GvmlGroupShape(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGvmlGroupShape extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgvmlgroupshape6aabtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "nvGrpSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual getNvGrpSpPr();

    /**
     * Sets the "nvGrpSpPr" element
     */
    void setNvGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual nvGrpSpPr);

    /**
     * Appends and returns a new empty "nvGrpSpPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual addNewNvGrpSpPr();

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
     * Gets a List of "txSp" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape> getTxSpList();

    /**
     * Gets array of all "txSp" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape[] getTxSpArray();

    /**
     * Gets ith "txSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape getTxSpArray(int i);

    /**
     * Returns number of "txSp" element
     */
    int sizeOfTxSpArray();

    /**
     * Sets array of all "txSp" element
     */
    void setTxSpArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape[] txSpArray);

    /**
     * Sets ith "txSp" element
     */
    void setTxSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape txSp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "txSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape insertNewTxSp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "txSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape addNewTxSp();

    /**
     * Removes the ith "txSp" element
     */
    void removeTxSp(int i);

    /**
     * Gets a List of "sp" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape> getSpList();

    /**
     * Gets array of all "sp" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape[] getSpArray();

    /**
     * Gets ith "sp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape getSpArray(int i);

    /**
     * Returns number of "sp" element
     */
    int sizeOfSpArray();

    /**
     * Sets array of all "sp" element
     */
    void setSpArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape[] spArray);

    /**
     * Sets ith "sp" element
     */
    void setSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape sp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape insertNewSp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape addNewSp();

    /**
     * Removes the ith "sp" element
     */
    void removeSp(int i);

    /**
     * Gets a List of "cxnSp" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector> getCxnSpList();

    /**
     * Gets array of all "cxnSp" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector[] getCxnSpArray();

    /**
     * Gets ith "cxnSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector getCxnSpArray(int i);

    /**
     * Returns number of "cxnSp" element
     */
    int sizeOfCxnSpArray();

    /**
     * Sets array of all "cxnSp" element
     */
    void setCxnSpArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector[] cxnSpArray);

    /**
     * Sets ith "cxnSp" element
     */
    void setCxnSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector cxnSp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cxnSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector insertNewCxnSp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cxnSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector addNewCxnSp();

    /**
     * Removes the ith "cxnSp" element
     */
    void removeCxnSp(int i);

    /**
     * Gets a List of "pic" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture> getPicList();

    /**
     * Gets array of all "pic" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture[] getPicArray();

    /**
     * Gets ith "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture getPicArray(int i);

    /**
     * Returns number of "pic" element
     */
    int sizeOfPicArray();

    /**
     * Sets array of all "pic" element
     */
    void setPicArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture[] picArray);

    /**
     * Sets ith "pic" element
     */
    void setPicArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture pic);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture insertNewPic(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture addNewPic();

    /**
     * Removes the ith "pic" element
     */
    void removePic(int i);

    /**
     * Gets a List of "graphicFrame" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame> getGraphicFrameList();

    /**
     * Gets array of all "graphicFrame" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame[] getGraphicFrameArray();

    /**
     * Gets ith "graphicFrame" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame getGraphicFrameArray(int i);

    /**
     * Returns number of "graphicFrame" element
     */
    int sizeOfGraphicFrameArray();

    /**
     * Sets array of all "graphicFrame" element
     */
    void setGraphicFrameArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame[] graphicFrameArray);

    /**
     * Sets ith "graphicFrame" element
     */
    void setGraphicFrameArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame graphicFrame);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "graphicFrame" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame insertNewGraphicFrame(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "graphicFrame" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame addNewGraphicFrame();

    /**
     * Removes the ith "graphicFrame" element
     */
    void removeGraphicFrame(int i);

    /**
     * Gets a List of "grpSp" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape> getGrpSpList();

    /**
     * Gets array of all "grpSp" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape[] getGrpSpArray();

    /**
     * Gets ith "grpSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape getGrpSpArray(int i);

    /**
     * Returns number of "grpSp" element
     */
    int sizeOfGrpSpArray();

    /**
     * Sets array of all "grpSp" element
     */
    void setGrpSpArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape[] grpSpArray);

    /**
     * Sets ith "grpSp" element
     */
    void setGrpSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape grpSp);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "grpSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape insertNewGrpSp(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "grpSp" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape addNewGrpSp();

    /**
     * Removes the ith "grpSp" element
     */
    void removeGrpSp(int i);

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

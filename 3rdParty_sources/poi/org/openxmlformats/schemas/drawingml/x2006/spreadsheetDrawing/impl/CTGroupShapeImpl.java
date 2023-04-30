/*
 * XML Type:  CT_GroupShape
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GroupShape(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public class CTGroupShapeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape {
    private static final long serialVersionUID = 1L;

    public CTGroupShapeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "nvGrpSpPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "grpSpPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "sp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "grpSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "graphicFrame"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "cxnSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "pic"),
    };


    /**
     * Gets the "nvGrpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShapeNonVisual getNvGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShapeNonVisual target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShapeNonVisual)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "nvGrpSpPr" element
     */
    @Override
    public void setNvGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShapeNonVisual nvGrpSpPr) {
        generatedSetterHelperImpl(nvGrpSpPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "nvGrpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShapeNonVisual addNewNvGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShapeNonVisual target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShapeNonVisual)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "grpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties getGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "grpSpPr" element
     */
    @Override
    public void setGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties grpSpPr) {
        generatedSetterHelperImpl(grpSpPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "grpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties addNewGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets a List of "sp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape> getSpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSpArray,
                this::setSpArray,
                this::insertNewSp,
                this::removeSp,
                this::sizeOfSpArray
            );
        }
    }

    /**
     * Gets array of all "sp" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape[] getSpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape[0]);
    }

    /**
     * Gets ith "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape getSpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sp" element
     */
    @Override
    public int sizeOfSpArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "sp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSpArray(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape[] spArray) {
        check_orphaned();
        arraySetterHelper(spArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "sp" element
     */
    @Override
    public void setSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape sp) {
        generatedSetterHelperImpl(sp, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape insertNewSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape addNewSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "sp" element
     */
    @Override
    public void removeSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "grpSp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape> getGrpSpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGrpSpArray,
                this::setGrpSpArray,
                this::insertNewGrpSp,
                this::removeGrpSp,
                this::sizeOfGrpSpArray
            );
        }
    }

    /**
     * Gets array of all "grpSp" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape[] getGrpSpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape[0]);
    }

    /**
     * Gets ith "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape getGrpSpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "grpSp" element
     */
    @Override
    public int sizeOfGrpSpArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "grpSp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGrpSpArray(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape[] grpSpArray) {
        check_orphaned();
        arraySetterHelper(grpSpArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "grpSp" element
     */
    @Override
    public void setGrpSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape grpSp) {
        generatedSetterHelperImpl(grpSp, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape insertNewGrpSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape addNewGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "grpSp" element
     */
    @Override
    public void removeGrpSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "graphicFrame" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame> getGraphicFrameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGraphicFrameArray,
                this::setGraphicFrameArray,
                this::insertNewGraphicFrame,
                this::removeGraphicFrame,
                this::sizeOfGraphicFrameArray
            );
        }
    }

    /**
     * Gets array of all "graphicFrame" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame[] getGraphicFrameArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame[0]);
    }

    /**
     * Gets ith "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame getGraphicFrameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "graphicFrame" element
     */
    @Override
    public int sizeOfGraphicFrameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "graphicFrame" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGraphicFrameArray(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame[] graphicFrameArray) {
        check_orphaned();
        arraySetterHelper(graphicFrameArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "graphicFrame" element
     */
    @Override
    public void setGraphicFrameArray(int i, org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame graphicFrame) {
        generatedSetterHelperImpl(graphicFrame, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame insertNewGraphicFrame(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame addNewGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "graphicFrame" element
     */
    @Override
    public void removeGraphicFrame(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "cxnSp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector> getCxnSpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCxnSpArray,
                this::setCxnSpArray,
                this::insertNewCxnSp,
                this::removeCxnSp,
                this::sizeOfCxnSpArray
            );
        }
    }

    /**
     * Gets array of all "cxnSp" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector[] getCxnSpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector[0]);
    }

    /**
     * Gets ith "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector getCxnSpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cxnSp" element
     */
    @Override
    public int sizeOfCxnSpArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "cxnSp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCxnSpArray(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector[] cxnSpArray) {
        check_orphaned();
        arraySetterHelper(cxnSpArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "cxnSp" element
     */
    @Override
    public void setCxnSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector cxnSp) {
        generatedSetterHelperImpl(cxnSp, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector insertNewCxnSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector addNewCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "cxnSp" element
     */
    @Override
    public void removeCxnSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "pic" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture> getPicList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPicArray,
                this::setPicArray,
                this::insertNewPic,
                this::removePic,
                this::sizeOfPicArray
            );
        }
    }

    /**
     * Gets array of all "pic" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture[] getPicArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture[0]);
    }

    /**
     * Gets ith "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture getPicArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pic" element
     */
    @Override
    public int sizeOfPicArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "pic" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPicArray(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture[] picArray) {
        check_orphaned();
        arraySetterHelper(picArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "pic" element
     */
    @Override
    public void setPicArray(int i, org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture pic) {
        generatedSetterHelperImpl(pic, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture insertNewPic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture addNewPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "pic" element
     */
    @Override
    public void removePic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }
}

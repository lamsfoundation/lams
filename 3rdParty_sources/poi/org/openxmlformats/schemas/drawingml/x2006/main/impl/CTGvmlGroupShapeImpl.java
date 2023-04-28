/*
 * XML Type:  CT_GvmlGroupShape
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GvmlGroupShape(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGvmlGroupShapeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape {
    private static final long serialVersionUID = 1L;

    public CTGvmlGroupShapeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "nvGrpSpPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "grpSpPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "txSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cxnSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pic"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "graphicFrame"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "grpSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
    };


    /**
     * Gets the "nvGrpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual getNvGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "nvGrpSpPr" element
     */
    @Override
    public void setNvGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual nvGrpSpPr) {
        generatedSetterHelperImpl(nvGrpSpPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "nvGrpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual addNewNvGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual)get_store().add_element_user(PROPERTY_QNAME[0]);
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
     * Gets a List of "txSp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape> getTxSpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTxSpArray,
                this::setTxSpArray,
                this::insertNewTxSp,
                this::removeTxSp,
                this::sizeOfTxSpArray
            );
        }
    }

    /**
     * Gets array of all "txSp" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape[] getTxSpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape[0]);
    }

    /**
     * Gets ith "txSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape getTxSpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "txSp" element
     */
    @Override
    public int sizeOfTxSpArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "txSp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTxSpArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape[] txSpArray) {
        check_orphaned();
        arraySetterHelper(txSpArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "txSp" element
     */
    @Override
    public void setTxSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape txSp) {
        generatedSetterHelperImpl(txSp, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "txSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape insertNewTxSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "txSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape addNewTxSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "txSp" element
     */
    @Override
    public void removeTxSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "sp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape> getSpList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape[] getSpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape[0]);
    }

    /**
     * Gets ith "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape getSpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape)get_store().find_element_user(PROPERTY_QNAME[3], i);
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
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "sp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSpArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape[] spArray) {
        check_orphaned();
        arraySetterHelper(spArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "sp" element
     */
    @Override
    public void setSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape sp) {
        generatedSetterHelperImpl(sp, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape insertNewSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape addNewSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlShape)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "cxnSp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector> getCxnSpList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector[] getCxnSpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector[0]);
    }

    /**
     * Gets ith "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector getCxnSpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector)get_store().find_element_user(PROPERTY_QNAME[4], i);
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
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "cxnSp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCxnSpArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector[] cxnSpArray) {
        check_orphaned();
        arraySetterHelper(cxnSpArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "cxnSp" element
     */
    @Override
    public void setCxnSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector cxnSp) {
        generatedSetterHelperImpl(cxnSp, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector insertNewCxnSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector addNewCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlConnector)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "pic" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture> getPicList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture[] getPicArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture[0]);
    }

    /**
     * Gets ith "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture getPicArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture)get_store().find_element_user(PROPERTY_QNAME[5], i);
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
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "pic" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPicArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture[] picArray) {
        check_orphaned();
        arraySetterHelper(picArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "pic" element
     */
    @Override
    public void setPicArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture pic) {
        generatedSetterHelperImpl(pic, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture insertNewPic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture addNewPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlPicture)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "graphicFrame" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame> getGraphicFrameList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame[] getGraphicFrameArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame[0]);
    }

    /**
     * Gets ith "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame getGraphicFrameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame)get_store().find_element_user(PROPERTY_QNAME[6], i);
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
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "graphicFrame" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGraphicFrameArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame[] graphicFrameArray) {
        check_orphaned();
        arraySetterHelper(graphicFrameArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "graphicFrame" element
     */
    @Override
    public void setGraphicFrameArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame graphicFrame) {
        generatedSetterHelperImpl(graphicFrame, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame insertNewGraphicFrame(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame addNewGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGraphicalObjectFrame)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "grpSp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape> getGrpSpList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape[] getGrpSpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape[0]);
    }

    /**
     * Gets ith "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape getGrpSpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape)get_store().find_element_user(PROPERTY_QNAME[7], i);
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
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "grpSp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGrpSpArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape[] grpSpArray) {
        check_orphaned();
        arraySetterHelper(grpSpArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "grpSp" element
     */
    @Override
    public void setGrpSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape grpSp) {
        generatedSetterHelperImpl(grpSp, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape insertNewGrpSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape addNewGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }
}

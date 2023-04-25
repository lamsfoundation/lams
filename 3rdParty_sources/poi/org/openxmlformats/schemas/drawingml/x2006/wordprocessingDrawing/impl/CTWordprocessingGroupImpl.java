/*
 * XML Type:  CT_WordprocessingGroup
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_WordprocessingGroup(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public class CTWordprocessingGroupImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup {
    private static final long serialVersionUID = 1L;

    public CTWordprocessingGroupImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "cNvPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "cNvGrpSpPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "grpSpPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "wsp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "grpSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "graphicFrame"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/picture", "pic"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "contentPart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "extLst"),
    };


    /**
     * Gets the "cNvPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps getCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cNvPr" element
     */
    @Override
    public boolean isSetCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "cNvPr" element
     */
    @Override
    public void setCNvPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps cNvPr) {
        generatedSetterHelperImpl(cNvPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps addNewCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "cNvPr" element
     */
    @Override
    public void unsetCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "cNvGrpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps getCNvGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cNvGrpSpPr" element
     */
    @Override
    public void setCNvGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps cNvGrpSpPr) {
        generatedSetterHelperImpl(cNvGrpSpPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvGrpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps addNewCNvGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "grpSpPr" element
     */
    @Override
    public void setGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties grpSpPr) {
        generatedSetterHelperImpl(grpSpPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "grpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties addNewGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets a List of "wsp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape> getWspList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getWspArray,
                this::setWspArray,
                this::insertNewWsp,
                this::removeWsp,
                this::sizeOfWspArray
            );
        }
    }

    /**
     * Gets array of all "wsp" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape[] getWspArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape[0]);
    }

    /**
     * Gets ith "wsp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape getWspArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "wsp" element
     */
    @Override
    public int sizeOfWspArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "wsp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWspArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape[] wspArray) {
        check_orphaned();
        arraySetterHelper(wspArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "wsp" element
     */
    @Override
    public void setWspArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape wsp) {
        generatedSetterHelperImpl(wsp, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "wsp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape insertNewWsp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "wsp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape addNewWsp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "wsp" element
     */
    @Override
    public void removeWsp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "grpSp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup> getGrpSpList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[] getGrpSpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[0]);
    }

    /**
     * Gets ith "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup getGrpSpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup)get_store().find_element_user(PROPERTY_QNAME[4], i);
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
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "grpSp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGrpSpArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[] grpSpArray) {
        check_orphaned();
        arraySetterHelper(grpSpArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "grpSp" element
     */
    @Override
    public void setGrpSpArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup grpSp) {
        generatedSetterHelperImpl(grpSp, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup insertNewGrpSp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup addNewGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "graphicFrame" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame> getGraphicFrameList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame[] getGraphicFrameArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame[0]);
    }

    /**
     * Gets ith "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame getGraphicFrameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame)get_store().find_element_user(PROPERTY_QNAME[5], i);
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
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "graphicFrame" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGraphicFrameArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame[] graphicFrameArray) {
        check_orphaned();
        arraySetterHelper(graphicFrameArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "graphicFrame" element
     */
    @Override
    public void setGraphicFrameArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame graphicFrame) {
        generatedSetterHelperImpl(graphicFrame, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame insertNewGraphicFrame(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame addNewGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "pic" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture> getPicList() {
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
    public org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture[] getPicArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture[0]);
    }

    /**
     * Gets ith "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture getPicArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture)get_store().find_element_user(PROPERTY_QNAME[6], i);
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
    public void setPicArray(org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture[] picArray) {
        check_orphaned();
        arraySetterHelper(picArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "pic" element
     */
    @Override
    public void setPicArray(int i, org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture pic) {
        generatedSetterHelperImpl(pic, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture insertNewPic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture addNewPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture)get_store().add_element_user(PROPERTY_QNAME[6]);
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

    /**
     * Gets a List of "contentPart" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart> getContentPartList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getContentPartArray,
                this::setContentPartArray,
                this::insertNewContentPart,
                this::removeContentPart,
                this::sizeOfContentPartArray
            );
        }
    }

    /**
     * Gets array of all "contentPart" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart[] getContentPartArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart[0]);
    }

    /**
     * Gets ith "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart getContentPartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "contentPart" element
     */
    @Override
    public int sizeOfContentPartArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "contentPart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setContentPartArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart[] contentPartArray) {
        check_orphaned();
        arraySetterHelper(contentPartArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "contentPart" element
     */
    @Override
    public void setContentPartArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart contentPart) {
        generatedSetterHelperImpl(contentPart, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart insertNewContentPart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart addNewContentPart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "contentPart" element
     */
    @Override
    public void removeContentPart(int i) {
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

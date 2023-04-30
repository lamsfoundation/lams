/*
 * XML Type:  CT_WordprocessingCanvas
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingCanvas
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_WordprocessingCanvas(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public class CTWordprocessingCanvasImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingCanvas {
    private static final long serialVersionUID = 1L;

    public CTWordprocessingCanvasImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "bg"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "whole"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "wsp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/picture", "pic"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "contentPart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "wgp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "graphicFrame"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "extLst"),
    };


    /**
     * Gets the "bg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting getBg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bg" element
     */
    @Override
    public boolean isSetBg() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "bg" element
     */
    @Override
    public void setBg(org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting bg) {
        generatedSetterHelperImpl(bg, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting addNewBg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "bg" element
     */
    @Override
    public void unsetBg() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "whole" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting getWhole() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "whole" element
     */
    @Override
    public boolean isSetWhole() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "whole" element
     */
    @Override
    public void setWhole(org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting whole) {
        generatedSetterHelperImpl(whole, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "whole" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting addNewWhole() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "whole" element
     */
    @Override
    public void unsetWhole() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape[0]);
    }

    /**
     * Gets ith "wsp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape getWspArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape)get_store().find_element_user(PROPERTY_QNAME[2], i);
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
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "wsp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWspArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape[] wspArray) {
        check_orphaned();
        arraySetterHelper(wspArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "wsp" element
     */
    @Override
    public void setWspArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape wsp) {
        generatedSetterHelperImpl(wsp, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "wsp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape insertNewWsp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape)get_store().insert_element_user(PROPERTY_QNAME[2], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture[0]);
    }

    /**
     * Gets ith "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture getPicArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture)get_store().find_element_user(PROPERTY_QNAME[3], i);
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
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "pic" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPicArray(org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture[] picArray) {
        check_orphaned();
        arraySetterHelper(picArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "pic" element
     */
    @Override
    public void setPicArray(int i, org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture pic) {
        generatedSetterHelperImpl(pic, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture insertNewPic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture)get_store().insert_element_user(PROPERTY_QNAME[3], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart[0]);
    }

    /**
     * Gets ith "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart getContentPartArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart)get_store().find_element_user(PROPERTY_QNAME[4], i);
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
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "contentPart" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setContentPartArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart[] contentPartArray) {
        check_orphaned();
        arraySetterHelper(contentPartArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "contentPart" element
     */
    @Override
    public void setContentPartArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart contentPart) {
        generatedSetterHelperImpl(contentPart, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart insertNewContentPart(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart)get_store().insert_element_user(PROPERTY_QNAME[4], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "wgp" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup> getWgpList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getWgpArray,
                this::setWgpArray,
                this::insertNewWgp,
                this::removeWgp,
                this::sizeOfWgpArray
            );
        }
    }

    /**
     * Gets array of all "wgp" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[] getWgpArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[0]);
    }

    /**
     * Gets ith "wgp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup getWgpArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "wgp" element
     */
    @Override
    public int sizeOfWgpArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "wgp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWgpArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup[] wgpArray) {
        check_orphaned();
        arraySetterHelper(wgpArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "wgp" element
     */
    @Override
    public void setWgpArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup wgp) {
        generatedSetterHelperImpl(wgp, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "wgp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup insertNewWgp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "wgp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup addNewWgp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "wgp" element
     */
    @Override
    public void removeWgp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
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
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame[0]);
    }

    /**
     * Gets ith "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame getGraphicFrameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame)get_store().find_element_user(PROPERTY_QNAME[6], i);
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
    public void setGraphicFrameArray(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame[] graphicFrameArray) {
        check_orphaned();
        arraySetterHelper(graphicFrameArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "graphicFrame" element
     */
    @Override
    public void setGraphicFrameArray(int i, org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame graphicFrame) {
        generatedSetterHelperImpl(graphicFrame, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame insertNewGraphicFrame(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame)get_store().insert_element_user(PROPERTY_QNAME[6], i);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTGraphicFrame)get_store().add_element_user(PROPERTY_QNAME[6]);
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
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[7], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }
}

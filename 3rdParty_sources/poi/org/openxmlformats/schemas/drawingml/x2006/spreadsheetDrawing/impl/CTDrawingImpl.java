/*
 * XML Type:  CT_Drawing
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Drawing(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public class CTDrawingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing {
    private static final long serialVersionUID = 1L;

    public CTDrawingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "twoCellAnchor"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "oneCellAnchor"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "absoluteAnchor"),
    };


    /**
     * Gets a List of "twoCellAnchor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor> getTwoCellAnchorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTwoCellAnchorArray,
                this::setTwoCellAnchorArray,
                this::insertNewTwoCellAnchor,
                this::removeTwoCellAnchor,
                this::sizeOfTwoCellAnchorArray
            );
        }
    }

    /**
     * Gets array of all "twoCellAnchor" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor[] getTwoCellAnchorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor[0]);
    }

    /**
     * Gets ith "twoCellAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor getTwoCellAnchorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "twoCellAnchor" element
     */
    @Override
    public int sizeOfTwoCellAnchorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "twoCellAnchor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTwoCellAnchorArray(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor[] twoCellAnchorArray) {
        check_orphaned();
        arraySetterHelper(twoCellAnchorArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "twoCellAnchor" element
     */
    @Override
    public void setTwoCellAnchorArray(int i, org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor twoCellAnchor) {
        generatedSetterHelperImpl(twoCellAnchor, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "twoCellAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor insertNewTwoCellAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "twoCellAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor addNewTwoCellAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "twoCellAnchor" element
     */
    @Override
    public void removeTwoCellAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "oneCellAnchor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor> getOneCellAnchorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getOneCellAnchorArray,
                this::setOneCellAnchorArray,
                this::insertNewOneCellAnchor,
                this::removeOneCellAnchor,
                this::sizeOfOneCellAnchorArray
            );
        }
    }

    /**
     * Gets array of all "oneCellAnchor" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor[] getOneCellAnchorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor[0]);
    }

    /**
     * Gets ith "oneCellAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor getOneCellAnchorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "oneCellAnchor" element
     */
    @Override
    public int sizeOfOneCellAnchorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "oneCellAnchor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setOneCellAnchorArray(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor[] oneCellAnchorArray) {
        check_orphaned();
        arraySetterHelper(oneCellAnchorArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "oneCellAnchor" element
     */
    @Override
    public void setOneCellAnchorArray(int i, org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor oneCellAnchor) {
        generatedSetterHelperImpl(oneCellAnchor, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oneCellAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor insertNewOneCellAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "oneCellAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor addNewOneCellAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "oneCellAnchor" element
     */
    @Override
    public void removeOneCellAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "absoluteAnchor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor> getAbsoluteAnchorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAbsoluteAnchorArray,
                this::setAbsoluteAnchorArray,
                this::insertNewAbsoluteAnchor,
                this::removeAbsoluteAnchor,
                this::sizeOfAbsoluteAnchorArray
            );
        }
    }

    /**
     * Gets array of all "absoluteAnchor" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor[] getAbsoluteAnchorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor[0]);
    }

    /**
     * Gets ith "absoluteAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor getAbsoluteAnchorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "absoluteAnchor" element
     */
    @Override
    public int sizeOfAbsoluteAnchorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "absoluteAnchor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAbsoluteAnchorArray(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor[] absoluteAnchorArray) {
        check_orphaned();
        arraySetterHelper(absoluteAnchorArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "absoluteAnchor" element
     */
    @Override
    public void setAbsoluteAnchorArray(int i, org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor absoluteAnchor) {
        generatedSetterHelperImpl(absoluteAnchor, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "absoluteAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor insertNewAbsoluteAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "absoluteAnchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor addNewAbsoluteAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "absoluteAnchor" element
     */
    @Override
    public void removeAbsoluteAnchor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }
}

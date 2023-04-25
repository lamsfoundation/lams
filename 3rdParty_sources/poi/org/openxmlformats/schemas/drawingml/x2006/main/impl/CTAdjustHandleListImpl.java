/*
 * XML Type:  CT_AdjustHandleList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AdjustHandleList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAdjustHandleListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList {
    private static final long serialVersionUID = 1L;

    public CTAdjustHandleListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ahXY"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ahPolar"),
    };


    /**
     * Gets a List of "ahXY" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle> getAhXYList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAhXYArray,
                this::setAhXYArray,
                this::insertNewAhXY,
                this::removeAhXY,
                this::sizeOfAhXYArray
            );
        }
    }

    /**
     * Gets array of all "ahXY" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle[] getAhXYArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle[0]);
    }

    /**
     * Gets ith "ahXY" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle getAhXYArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ahXY" element
     */
    @Override
    public int sizeOfAhXYArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ahXY" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAhXYArray(org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle[] ahXYArray) {
        check_orphaned();
        arraySetterHelper(ahXYArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ahXY" element
     */
    @Override
    public void setAhXYArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle ahXY) {
        generatedSetterHelperImpl(ahXY, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ahXY" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle insertNewAhXY(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ahXY" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle addNewAhXY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTXYAdjustHandle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ahXY" element
     */
    @Override
    public void removeAhXY(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "ahPolar" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle> getAhPolarList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAhPolarArray,
                this::setAhPolarArray,
                this::insertNewAhPolar,
                this::removeAhPolar,
                this::sizeOfAhPolarArray
            );
        }
    }

    /**
     * Gets array of all "ahPolar" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle[] getAhPolarArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle[0]);
    }

    /**
     * Gets ith "ahPolar" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle getAhPolarArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ahPolar" element
     */
    @Override
    public int sizeOfAhPolarArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "ahPolar" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAhPolarArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle[] ahPolarArray) {
        check_orphaned();
        arraySetterHelper(ahPolarArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "ahPolar" element
     */
    @Override
    public void setAhPolarArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle ahPolar) {
        generatedSetterHelperImpl(ahPolar, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ahPolar" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle insertNewAhPolar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ahPolar" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle addNewAhPolar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPolarAdjustHandle)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "ahPolar" element
     */
    @Override
    public void removeAhPolar(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }
}

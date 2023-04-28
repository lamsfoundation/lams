/*
 * XML Type:  CT_GradientStopList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GradientStopList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGradientStopListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStopList {
    private static final long serialVersionUID = 1L;

    public CTGradientStopListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gs"),
    };


    /**
     * Gets a List of "gs" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop> getGsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGsArray,
                this::setGsArray,
                this::insertNewGs,
                this::removeGs,
                this::sizeOfGsArray
            );
        }
    }

    /**
     * Gets array of all "gs" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop[] getGsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop[0]);
    }

    /**
     * Gets ith "gs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop getGsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "gs" element
     */
    @Override
    public int sizeOfGsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "gs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGsArray(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop[] gsArray) {
        check_orphaned();
        arraySetterHelper(gsArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "gs" element
     */
    @Override
    public void setGsArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop gs) {
        generatedSetterHelperImpl(gs, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "gs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop insertNewGs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "gs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop addNewGs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientStop)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "gs" element
     */
    @Override
    public void removeGs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

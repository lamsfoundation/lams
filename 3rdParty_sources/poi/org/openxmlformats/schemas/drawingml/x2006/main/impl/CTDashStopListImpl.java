/*
 * XML Type:  CT_DashStopList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DashStopList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDashStopListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList {
    private static final long serialVersionUID = 1L;

    public CTDashStopListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ds"),
    };


    /**
     * Gets a List of "ds" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop> getDsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDsArray,
                this::setDsArray,
                this::insertNewDs,
                this::removeDs,
                this::sizeOfDsArray
            );
        }
    }

    /**
     * Gets array of all "ds" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop[] getDsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop[0]);
    }

    /**
     * Gets ith "ds" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop getDsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ds" element
     */
    @Override
    public int sizeOfDsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ds" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDsArray(org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop[] dsArray) {
        check_orphaned();
        arraySetterHelper(dsArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ds" element
     */
    @Override
    public void setDsArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop ds) {
        generatedSetterHelperImpl(ds, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ds" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop insertNewDs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ds" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop addNewDs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDashStop)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ds" element
     */
    @Override
    public void removeDs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

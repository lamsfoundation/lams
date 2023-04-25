/*
 * XML Type:  CT_CxnList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CxnList(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTCxnListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList {
    private static final long serialVersionUID = 1L;

    public CTCxnListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "cxn"),
    };


    /**
     * Gets a List of "cxn" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn> getCxnList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCxnArray,
                this::setCxnArray,
                this::insertNewCxn,
                this::removeCxn,
                this::sizeOfCxnArray
            );
        }
    }

    /**
     * Gets array of all "cxn" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn[] getCxnArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn[0]);
    }

    /**
     * Gets ith "cxn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn getCxnArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cxn" element
     */
    @Override
    public int sizeOfCxnArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cxn" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCxnArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn[] cxnArray) {
        check_orphaned();
        arraySetterHelper(cxnArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cxn" element
     */
    @Override
    public void setCxnArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn cxn) {
        generatedSetterHelperImpl(cxn, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cxn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn insertNewCxn(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cxn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn addNewCxn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cxn" element
     */
    @Override
    public void removeCxn(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

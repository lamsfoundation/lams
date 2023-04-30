/*
 * XML Type:  CT_CustomColorList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CustomColorList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTCustomColorListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList {
    private static final long serialVersionUID = 1L;

    public CTCustomColorListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "custClr"),
    };


    /**
     * Gets a List of "custClr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor> getCustClrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustClrArray,
                this::setCustClrArray,
                this::insertNewCustClr,
                this::removeCustClr,
                this::sizeOfCustClrArray
            );
        }
    }

    /**
     * Gets array of all "custClr" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor[] getCustClrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor[0]);
    }

    /**
     * Gets ith "custClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor getCustClrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "custClr" element
     */
    @Override
    public int sizeOfCustClrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "custClr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustClrArray(org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor[] custClrArray) {
        check_orphaned();
        arraySetterHelper(custClrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "custClr" element
     */
    @Override
    public void setCustClrArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor custClr) {
        generatedSetterHelperImpl(custClr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "custClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor insertNewCustClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "custClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor addNewCustClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "custClr" element
     */
    @Override
    public void removeCustClr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

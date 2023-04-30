/*
 * XML Type:  CT_Divs
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Divs(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDivsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivs {
    private static final long serialVersionUID = 1L;

    public CTDivsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "div"),
    };


    /**
     * Gets a List of "div" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv> getDivList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDivArray,
                this::setDivArray,
                this::insertNewDiv,
                this::removeDiv,
                this::sizeOfDivArray
            );
        }
    }

    /**
     * Gets array of all "div" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv[] getDivArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv[0]);
    }

    /**
     * Gets ith "div" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv getDivArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "div" element
     */
    @Override
    public int sizeOfDivArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "div" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDivArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv[] divArray) {
        check_orphaned();
        arraySetterHelper(divArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "div" element
     */
    @Override
    public void setDivArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv div) {
        generatedSetterHelperImpl(div, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "div" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv insertNewDiv(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "div" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv addNewDiv() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDiv)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "div" element
     */
    @Override
    public void removeDiv(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

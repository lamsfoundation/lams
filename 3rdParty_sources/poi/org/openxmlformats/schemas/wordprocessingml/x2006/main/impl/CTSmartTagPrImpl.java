/*
 * XML Type:  CT_SmartTagPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SmartTagPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTSmartTagPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagPr {
    private static final long serialVersionUID = 1L;

    public CTSmartTagPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "attr"),
    };


    /**
     * Gets a List of "attr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr> getAttrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAttrArray,
                this::setAttrArray,
                this::insertNewAttr,
                this::removeAttr,
                this::sizeOfAttrArray
            );
        }
    }

    /**
     * Gets array of all "attr" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr[] getAttrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr[0]);
    }

    /**
     * Gets ith "attr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr getAttrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "attr" element
     */
    @Override
    public int sizeOfAttrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "attr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAttrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr[] attrArray) {
        check_orphaned();
        arraySetterHelper(attrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "attr" element
     */
    @Override
    public void setAttrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr attr) {
        generatedSetterHelperImpl(attr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "attr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr insertNewAttr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "attr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr addNewAttr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "attr" element
     */
    @Override
    public void removeAttr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

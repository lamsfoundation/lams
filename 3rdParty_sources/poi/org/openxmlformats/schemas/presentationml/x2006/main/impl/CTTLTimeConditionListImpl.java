/*
 * XML Type:  CT_TLTimeConditionList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTimeConditionList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTimeConditionListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList {
    private static final long serialVersionUID = 1L;

    public CTTLTimeConditionListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cond"),
    };


    /**
     * Gets a List of "cond" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition> getCondList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCondArray,
                this::setCondArray,
                this::insertNewCond,
                this::removeCond,
                this::sizeOfCondArray
            );
        }
    }

    /**
     * Gets array of all "cond" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition[] getCondArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition[0]);
    }

    /**
     * Gets ith "cond" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition getCondArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cond" element
     */
    @Override
    public int sizeOfCondArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cond" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCondArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition[] condArray) {
        check_orphaned();
        arraySetterHelper(condArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cond" element
     */
    @Override
    public void setCondArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition cond) {
        generatedSetterHelperImpl(cond, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cond" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition insertNewCond(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cond" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition addNewCond() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cond" element
     */
    @Override
    public void removeCond(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

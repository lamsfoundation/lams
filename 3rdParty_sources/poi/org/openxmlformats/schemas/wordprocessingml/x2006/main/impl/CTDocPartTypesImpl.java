/*
 * XML Type:  CT_DocPartTypes
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartTypes
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocPartTypes(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocPartTypesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartTypes {
    private static final long serialVersionUID = 1L;

    public CTDocPartTypesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "type"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "all"),
    };


    /**
     * Gets a List of "type" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType> getTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTypeArray,
                this::setTypeArray,
                this::insertNewType,
                this::removeType,
                this::sizeOfTypeArray
            );
        }
    }

    /**
     * Gets array of all "type" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType[] getTypeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType[0]);
    }

    /**
     * Gets ith "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType getTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "type" element
     */
    @Override
    public int sizeOfTypeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "type" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTypeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType[] typeArray) {
        check_orphaned();
        arraySetterHelper(typeArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "type" element
     */
    @Override
    public void setTypeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType type) {
        generatedSetterHelperImpl(type, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType insertNewType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType addNewType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "type" element
     */
    @Override
    public void removeType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "all" attribute
     */
    @Override
    public java.lang.Object getAll() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "all" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetAll() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "all" attribute
     */
    @Override
    public boolean isSetAll() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "all" attribute
     */
    @Override
    public void setAll(java.lang.Object all) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(all);
        }
    }

    /**
     * Sets (as xml) the "all" attribute
     */
    @Override
    public void xsetAll(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff all) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(all);
        }
    }

    /**
     * Unsets the "all" attribute
     */
    @Override
    public void unsetAll() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

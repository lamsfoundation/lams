/*
 * XML Type:  UnsignedDataObjectPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML UnsignedDataObjectPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class UnsignedDataObjectPropertiesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType {
    private static final long serialVersionUID = 1L;

    public UnsignedDataObjectPropertiesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "UnsignedDataObjectProperty"),
        new QName("", "Id"),
    };


    /**
     * Gets a List of "UnsignedDataObjectProperty" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.AnyType> getUnsignedDataObjectPropertyList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getUnsignedDataObjectPropertyArray,
                this::setUnsignedDataObjectPropertyArray,
                this::insertNewUnsignedDataObjectProperty,
                this::removeUnsignedDataObjectProperty,
                this::sizeOfUnsignedDataObjectPropertyArray
            );
        }
    }

    /**
     * Gets array of all "UnsignedDataObjectProperty" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType[] getUnsignedDataObjectPropertyArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.AnyType[0]);
    }

    /**
     * Gets ith "UnsignedDataObjectProperty" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType getUnsignedDataObjectPropertyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "UnsignedDataObjectProperty" element
     */
    @Override
    public int sizeOfUnsignedDataObjectPropertyArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "UnsignedDataObjectProperty" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setUnsignedDataObjectPropertyArray(org.etsi.uri.x01903.v13.AnyType[] unsignedDataObjectPropertyArray) {
        check_orphaned();
        arraySetterHelper(unsignedDataObjectPropertyArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "UnsignedDataObjectProperty" element
     */
    @Override
    public void setUnsignedDataObjectPropertyArray(int i, org.etsi.uri.x01903.v13.AnyType unsignedDataObjectProperty) {
        generatedSetterHelperImpl(unsignedDataObjectProperty, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "UnsignedDataObjectProperty" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType insertNewUnsignedDataObjectProperty(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "UnsignedDataObjectProperty" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType addNewUnsignedDataObjectProperty() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "UnsignedDataObjectProperty" element
     */
    @Override
    public void removeUnsignedDataObjectProperty(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "Id" attribute
     */
    @Override
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Id" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlID xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "Id" attribute
     */
    @Override
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "Id" attribute
     */
    @Override
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "Id" attribute
     */
    @Override
    public void xsetId(org.apache.xmlbeans.XmlID id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "Id" attribute
     */
    @Override
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

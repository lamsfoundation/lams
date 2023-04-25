/*
 * XML Type:  UnsignedPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML UnsignedPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class UnsignedPropertiesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.UnsignedPropertiesType {
    private static final long serialVersionUID = 1L;

    public UnsignedPropertiesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "UnsignedSignatureProperties"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "UnsignedDataObjectProperties"),
        new QName("", "Id"),
    };


    /**
     * Gets the "UnsignedSignatureProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType getUnsignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "UnsignedSignatureProperties" element
     */
    @Override
    public boolean isSetUnsignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "UnsignedSignatureProperties" element
     */
    @Override
    public void setUnsignedSignatureProperties(org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType unsignedSignatureProperties) {
        generatedSetterHelperImpl(unsignedSignatureProperties, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "UnsignedSignatureProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType addNewUnsignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "UnsignedSignatureProperties" element
     */
    @Override
    public void unsetUnsignedSignatureProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "UnsignedDataObjectProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType getUnsignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "UnsignedDataObjectProperties" element
     */
    @Override
    public boolean isSetUnsignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "UnsignedDataObjectProperties" element
     */
    @Override
    public void setUnsignedDataObjectProperties(org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType unsignedDataObjectProperties) {
        generatedSetterHelperImpl(unsignedDataObjectProperties, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "UnsignedDataObjectProperties" element
     */
    @Override
    public org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType addNewUnsignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType target = null;
            target = (org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "UnsignedDataObjectProperties" element
     */
    @Override
    public void unsetUnsignedDataObjectProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[2]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

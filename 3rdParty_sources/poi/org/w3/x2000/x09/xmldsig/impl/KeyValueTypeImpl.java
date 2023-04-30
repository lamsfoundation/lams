/*
 * XML Type:  KeyValueType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.KeyValueType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML KeyValueType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class KeyValueTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.KeyValueType {
    private static final long serialVersionUID = 1L;

    public KeyValueTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValue"),
    };


    /**
     * Gets the "DSAKeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DSAKeyValueType getDSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DSAKeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.DSAKeyValueType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "DSAKeyValue" element
     */
    @Override
    public boolean isSetDSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "DSAKeyValue" element
     */
    @Override
    public void setDSAKeyValue(org.w3.x2000.x09.xmldsig.DSAKeyValueType dsaKeyValue) {
        generatedSetterHelperImpl(dsaKeyValue, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DSAKeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DSAKeyValueType addNewDSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DSAKeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.DSAKeyValueType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "DSAKeyValue" element
     */
    @Override
    public void unsetDSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "RSAKeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.RSAKeyValueType getRSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.RSAKeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.RSAKeyValueType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "RSAKeyValue" element
     */
    @Override
    public boolean isSetRSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "RSAKeyValue" element
     */
    @Override
    public void setRSAKeyValue(org.w3.x2000.x09.xmldsig.RSAKeyValueType rsaKeyValue) {
        generatedSetterHelperImpl(rsaKeyValue, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "RSAKeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.RSAKeyValueType addNewRSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.RSAKeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.RSAKeyValueType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "RSAKeyValue" element
     */
    @Override
    public void unsetRSAKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

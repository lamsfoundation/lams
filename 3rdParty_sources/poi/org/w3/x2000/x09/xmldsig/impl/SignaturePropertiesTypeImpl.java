/*
 * XML Type:  SignaturePropertiesType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignaturePropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignaturePropertiesType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class SignaturePropertiesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignaturePropertiesType {
    private static final long serialVersionUID = 1L;

    public SignaturePropertiesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperty"),
        new QName("", "Id"),
    };


    /**
     * Gets a List of "SignatureProperty" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.SignaturePropertyType> getSignaturePropertyList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSignaturePropertyArray,
                this::setSignaturePropertyArray,
                this::insertNewSignatureProperty,
                this::removeSignatureProperty,
                this::sizeOfSignaturePropertyArray
            );
        }
    }

    /**
     * Gets array of all "SignatureProperty" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignaturePropertyType[] getSignaturePropertyArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.w3.x2000.x09.xmldsig.SignaturePropertyType[0]);
    }

    /**
     * Gets ith "SignatureProperty" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignaturePropertyType getSignaturePropertyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignaturePropertyType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignaturePropertyType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "SignatureProperty" element
     */
    @Override
    public int sizeOfSignaturePropertyArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "SignatureProperty" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSignaturePropertyArray(org.w3.x2000.x09.xmldsig.SignaturePropertyType[] signaturePropertyArray) {
        check_orphaned();
        arraySetterHelper(signaturePropertyArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "SignatureProperty" element
     */
    @Override
    public void setSignaturePropertyArray(int i, org.w3.x2000.x09.xmldsig.SignaturePropertyType signatureProperty) {
        generatedSetterHelperImpl(signatureProperty, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SignatureProperty" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignaturePropertyType insertNewSignatureProperty(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignaturePropertyType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignaturePropertyType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "SignatureProperty" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignaturePropertyType addNewSignatureProperty() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignaturePropertyType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignaturePropertyType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "SignatureProperty" element
     */
    @Override
    public void removeSignatureProperty(int i) {
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

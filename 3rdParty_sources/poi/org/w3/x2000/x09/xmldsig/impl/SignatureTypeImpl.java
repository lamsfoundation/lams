/*
 * XML Type:  SignatureType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignatureType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignatureType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class SignatureTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignatureType {
    private static final long serialVersionUID = 1L;

    public SignatureTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "Object"),
        new QName("", "Id"),
    };


    /**
     * Gets the "SignedInfo" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignedInfoType getSignedInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignedInfo" element
     */
    @Override
    public void setSignedInfo(org.w3.x2000.x09.xmldsig.SignedInfoType signedInfo) {
        generatedSetterHelperImpl(signedInfo, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignedInfo" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignedInfoType addNewSignedInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignedInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignedInfoType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "SignatureValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureValueType getSignatureValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureValueType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignatureValue" element
     */
    @Override
    public void setSignatureValue(org.w3.x2000.x09.xmldsig.SignatureValueType signatureValue) {
        generatedSetterHelperImpl(signatureValue, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureValueType addNewSignatureValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureValueType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "KeyInfo" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyInfoType getKeyInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyInfoType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "KeyInfo" element
     */
    @Override
    public boolean isSetKeyInfo() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "KeyInfo" element
     */
    @Override
    public void setKeyInfo(org.w3.x2000.x09.xmldsig.KeyInfoType keyInfo) {
        generatedSetterHelperImpl(keyInfo, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "KeyInfo" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyInfoType addNewKeyInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyInfoType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyInfoType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "KeyInfo" element
     */
    @Override
    public void unsetKeyInfo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets a List of "Object" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.ObjectType> getObjectList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getObjectArray,
                this::setObjectArray,
                this::insertNewObject,
                this::removeObject,
                this::sizeOfObjectArray
            );
        }
    }

    /**
     * Gets array of all "Object" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ObjectType[] getObjectArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.w3.x2000.x09.xmldsig.ObjectType[0]);
    }

    /**
     * Gets ith "Object" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ObjectType getObjectArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ObjectType target = null;
            target = (org.w3.x2000.x09.xmldsig.ObjectType)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Object" element
     */
    @Override
    public int sizeOfObjectArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "Object" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setObjectArray(org.w3.x2000.x09.xmldsig.ObjectType[] objectArray) {
        check_orphaned();
        arraySetterHelper(objectArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "Object" element
     */
    @Override
    public void setObjectArray(int i, org.w3.x2000.x09.xmldsig.ObjectType object) {
        generatedSetterHelperImpl(object, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Object" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ObjectType insertNewObject(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ObjectType target = null;
            target = (org.w3.x2000.x09.xmldsig.ObjectType)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Object" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ObjectType addNewObject() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ObjectType target = null;
            target = (org.w3.x2000.x09.xmldsig.ObjectType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "Object" element
     */
    @Override
    public void removeObject(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[4]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[4]);
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
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

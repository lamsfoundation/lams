/*
 * XML Type:  SignedInfoType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SignedInfoType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignedInfoType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class SignedInfoTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.SignedInfoType {
    private static final long serialVersionUID = 1L;

    public SignedInfoTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "Reference"),
        new QName("", "Id"),
    };


    /**
     * Gets the "CanonicalizationMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CanonicalizationMethodType getCanonicalizationMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CanonicalizationMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.CanonicalizationMethodType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "CanonicalizationMethod" element
     */
    @Override
    public void setCanonicalizationMethod(org.w3.x2000.x09.xmldsig.CanonicalizationMethodType canonicalizationMethod) {
        generatedSetterHelperImpl(canonicalizationMethod, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "CanonicalizationMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CanonicalizationMethodType addNewCanonicalizationMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CanonicalizationMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.CanonicalizationMethodType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "SignatureMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureMethodType getSignatureMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureMethodType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "SignatureMethod" element
     */
    @Override
    public void setSignatureMethod(org.w3.x2000.x09.xmldsig.SignatureMethodType signatureMethod) {
        generatedSetterHelperImpl(signatureMethod, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "SignatureMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SignatureMethodType addNewSignatureMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SignatureMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.SignatureMethodType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets a List of "Reference" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.ReferenceType> getReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getReferenceArray,
                this::setReferenceArray,
                this::insertNewReference,
                this::removeReference,
                this::sizeOfReferenceArray
            );
        }
    }

    /**
     * Gets array of all "Reference" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType[] getReferenceArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.w3.x2000.x09.xmldsig.ReferenceType[0]);
    }

    /**
     * Gets ith "Reference" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType getReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Reference" element
     */
    @Override
    public int sizeOfReferenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "Reference" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setReferenceArray(org.w3.x2000.x09.xmldsig.ReferenceType[] referenceArray) {
        check_orphaned();
        arraySetterHelper(referenceArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "Reference" element
     */
    @Override
    public void setReferenceArray(int i, org.w3.x2000.x09.xmldsig.ReferenceType reference) {
        generatedSetterHelperImpl(reference, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Reference" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType insertNewReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Reference" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType addNewReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "Reference" element
     */
    @Override
    public void removeReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[3]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[3]);
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
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}

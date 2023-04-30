/*
 * XML Type:  UnsignedSignaturePropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML UnsignedSignaturePropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class UnsignedSignaturePropertiesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.UnsignedSignaturePropertiesType {
    private static final long serialVersionUID = 1L;

    public UnsignedSignaturePropertiesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CounterSignature"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SignatureTimeStamp"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CompleteCertificateRefs"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CompleteRevocationRefs"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AttributeCertificateRefs"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AttributeRevocationRefs"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "SigAndRefsTimeStamp"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "RefsOnlyTimeStamp"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CertificateValues"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "RevocationValues"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AttrAuthoritiesCertValues"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AttributeRevocationValues"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ArchiveTimeStamp"),
        new QName("", "Id"),
    };


    /**
     * Gets a List of "CounterSignature" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CounterSignatureType> getCounterSignatureList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCounterSignatureArray,
                this::setCounterSignatureArray,
                this::insertNewCounterSignature,
                this::removeCounterSignature,
                this::sizeOfCounterSignatureArray
            );
        }
    }

    /**
     * Gets array of all "CounterSignature" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CounterSignatureType[] getCounterSignatureArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.CounterSignatureType[0]);
    }

    /**
     * Gets ith "CounterSignature" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CounterSignatureType getCounterSignatureArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CounterSignatureType target = null;
            target = (org.etsi.uri.x01903.v13.CounterSignatureType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CounterSignature" element
     */
    @Override
    public int sizeOfCounterSignatureArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "CounterSignature" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCounterSignatureArray(org.etsi.uri.x01903.v13.CounterSignatureType[] counterSignatureArray) {
        check_orphaned();
        arraySetterHelper(counterSignatureArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "CounterSignature" element
     */
    @Override
    public void setCounterSignatureArray(int i, org.etsi.uri.x01903.v13.CounterSignatureType counterSignature) {
        generatedSetterHelperImpl(counterSignature, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CounterSignature" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CounterSignatureType insertNewCounterSignature(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CounterSignatureType target = null;
            target = (org.etsi.uri.x01903.v13.CounterSignatureType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CounterSignature" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CounterSignatureType addNewCounterSignature() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CounterSignatureType target = null;
            target = (org.etsi.uri.x01903.v13.CounterSignatureType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "CounterSignature" element
     */
    @Override
    public void removeCounterSignature(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "SignatureTimeStamp" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getSignatureTimeStampList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSignatureTimeStampArray,
                this::setSignatureTimeStampArray,
                this::insertNewSignatureTimeStamp,
                this::removeSignatureTimeStamp,
                this::sizeOfSignatureTimeStampArray
            );
        }
    }

    /**
     * Gets array of all "SignatureTimeStamp" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType[] getSignatureTimeStampArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.etsi.uri.x01903.v13.XAdESTimeStampType[0]);
    }

    /**
     * Gets ith "SignatureTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getSignatureTimeStampArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "SignatureTimeStamp" element
     */
    @Override
    public int sizeOfSignatureTimeStampArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "SignatureTimeStamp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSignatureTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] signatureTimeStampArray) {
        check_orphaned();
        arraySetterHelper(signatureTimeStampArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "SignatureTimeStamp" element
     */
    @Override
    public void setSignatureTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType signatureTimeStamp) {
        generatedSetterHelperImpl(signatureTimeStamp, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SignatureTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewSignatureTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "SignatureTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewSignatureTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "SignatureTimeStamp" element
     */
    @Override
    public void removeSignatureTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "CompleteCertificateRefs" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CompleteCertificateRefsType> getCompleteCertificateRefsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCompleteCertificateRefsArray,
                this::setCompleteCertificateRefsArray,
                this::insertNewCompleteCertificateRefs,
                this::removeCompleteCertificateRefs,
                this::sizeOfCompleteCertificateRefsArray
            );
        }
    }

    /**
     * Gets array of all "CompleteCertificateRefs" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType[] getCompleteCertificateRefsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.etsi.uri.x01903.v13.CompleteCertificateRefsType[0]);
    }

    /**
     * Gets ith "CompleteCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType getCompleteCertificateRefsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CompleteCertificateRefs" element
     */
    @Override
    public int sizeOfCompleteCertificateRefsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "CompleteCertificateRefs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCompleteCertificateRefsArray(org.etsi.uri.x01903.v13.CompleteCertificateRefsType[] completeCertificateRefsArray) {
        check_orphaned();
        arraySetterHelper(completeCertificateRefsArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "CompleteCertificateRefs" element
     */
    @Override
    public void setCompleteCertificateRefsArray(int i, org.etsi.uri.x01903.v13.CompleteCertificateRefsType completeCertificateRefs) {
        generatedSetterHelperImpl(completeCertificateRefs, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CompleteCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType insertNewCompleteCertificateRefs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CompleteCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType addNewCompleteCertificateRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "CompleteCertificateRefs" element
     */
    @Override
    public void removeCompleteCertificateRefs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "CompleteRevocationRefs" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CompleteRevocationRefsType> getCompleteRevocationRefsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCompleteRevocationRefsArray,
                this::setCompleteRevocationRefsArray,
                this::insertNewCompleteRevocationRefs,
                this::removeCompleteRevocationRefs,
                this::sizeOfCompleteRevocationRefsArray
            );
        }
    }

    /**
     * Gets array of all "CompleteRevocationRefs" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType[] getCompleteRevocationRefsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.etsi.uri.x01903.v13.CompleteRevocationRefsType[0]);
    }

    /**
     * Gets ith "CompleteRevocationRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType getCompleteRevocationRefsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteRevocationRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteRevocationRefsType)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CompleteRevocationRefs" element
     */
    @Override
    public int sizeOfCompleteRevocationRefsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "CompleteRevocationRefs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCompleteRevocationRefsArray(org.etsi.uri.x01903.v13.CompleteRevocationRefsType[] completeRevocationRefsArray) {
        check_orphaned();
        arraySetterHelper(completeRevocationRefsArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "CompleteRevocationRefs" element
     */
    @Override
    public void setCompleteRevocationRefsArray(int i, org.etsi.uri.x01903.v13.CompleteRevocationRefsType completeRevocationRefs) {
        generatedSetterHelperImpl(completeRevocationRefs, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CompleteRevocationRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType insertNewCompleteRevocationRefs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteRevocationRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteRevocationRefsType)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CompleteRevocationRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType addNewCompleteRevocationRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteRevocationRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteRevocationRefsType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "CompleteRevocationRefs" element
     */
    @Override
    public void removeCompleteRevocationRefs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "AttributeCertificateRefs" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CompleteCertificateRefsType> getAttributeCertificateRefsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAttributeCertificateRefsArray,
                this::setAttributeCertificateRefsArray,
                this::insertNewAttributeCertificateRefs,
                this::removeAttributeCertificateRefs,
                this::sizeOfAttributeCertificateRefsArray
            );
        }
    }

    /**
     * Gets array of all "AttributeCertificateRefs" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType[] getAttributeCertificateRefsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.etsi.uri.x01903.v13.CompleteCertificateRefsType[0]);
    }

    /**
     * Gets ith "AttributeCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType getAttributeCertificateRefsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "AttributeCertificateRefs" element
     */
    @Override
    public int sizeOfAttributeCertificateRefsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "AttributeCertificateRefs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAttributeCertificateRefsArray(org.etsi.uri.x01903.v13.CompleteCertificateRefsType[] attributeCertificateRefsArray) {
        check_orphaned();
        arraySetterHelper(attributeCertificateRefsArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "AttributeCertificateRefs" element
     */
    @Override
    public void setAttributeCertificateRefsArray(int i, org.etsi.uri.x01903.v13.CompleteCertificateRefsType attributeCertificateRefs) {
        generatedSetterHelperImpl(attributeCertificateRefs, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AttributeCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType insertNewAttributeCertificateRefs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "AttributeCertificateRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteCertificateRefsType addNewAttributeCertificateRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteCertificateRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteCertificateRefsType)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "AttributeCertificateRefs" element
     */
    @Override
    public void removeAttributeCertificateRefs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "AttributeRevocationRefs" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CompleteRevocationRefsType> getAttributeRevocationRefsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAttributeRevocationRefsArray,
                this::setAttributeRevocationRefsArray,
                this::insertNewAttributeRevocationRefs,
                this::removeAttributeRevocationRefs,
                this::sizeOfAttributeRevocationRefsArray
            );
        }
    }

    /**
     * Gets array of all "AttributeRevocationRefs" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType[] getAttributeRevocationRefsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.etsi.uri.x01903.v13.CompleteRevocationRefsType[0]);
    }

    /**
     * Gets ith "AttributeRevocationRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType getAttributeRevocationRefsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteRevocationRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteRevocationRefsType)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "AttributeRevocationRefs" element
     */
    @Override
    public int sizeOfAttributeRevocationRefsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "AttributeRevocationRefs" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAttributeRevocationRefsArray(org.etsi.uri.x01903.v13.CompleteRevocationRefsType[] attributeRevocationRefsArray) {
        check_orphaned();
        arraySetterHelper(attributeRevocationRefsArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "AttributeRevocationRefs" element
     */
    @Override
    public void setAttributeRevocationRefsArray(int i, org.etsi.uri.x01903.v13.CompleteRevocationRefsType attributeRevocationRefs) {
        generatedSetterHelperImpl(attributeRevocationRefs, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AttributeRevocationRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType insertNewAttributeRevocationRefs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteRevocationRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteRevocationRefsType)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "AttributeRevocationRefs" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CompleteRevocationRefsType addNewAttributeRevocationRefs() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CompleteRevocationRefsType target = null;
            target = (org.etsi.uri.x01903.v13.CompleteRevocationRefsType)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "AttributeRevocationRefs" element
     */
    @Override
    public void removeAttributeRevocationRefs(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "SigAndRefsTimeStamp" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getSigAndRefsTimeStampList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSigAndRefsTimeStampArray,
                this::setSigAndRefsTimeStampArray,
                this::insertNewSigAndRefsTimeStamp,
                this::removeSigAndRefsTimeStamp,
                this::sizeOfSigAndRefsTimeStampArray
            );
        }
    }

    /**
     * Gets array of all "SigAndRefsTimeStamp" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType[] getSigAndRefsTimeStampArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.etsi.uri.x01903.v13.XAdESTimeStampType[0]);
    }

    /**
     * Gets ith "SigAndRefsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getSigAndRefsTimeStampArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "SigAndRefsTimeStamp" element
     */
    @Override
    public int sizeOfSigAndRefsTimeStampArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "SigAndRefsTimeStamp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSigAndRefsTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] sigAndRefsTimeStampArray) {
        check_orphaned();
        arraySetterHelper(sigAndRefsTimeStampArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "SigAndRefsTimeStamp" element
     */
    @Override
    public void setSigAndRefsTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType sigAndRefsTimeStamp) {
        generatedSetterHelperImpl(sigAndRefsTimeStamp, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SigAndRefsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewSigAndRefsTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "SigAndRefsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewSigAndRefsTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "SigAndRefsTimeStamp" element
     */
    @Override
    public void removeSigAndRefsTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "RefsOnlyTimeStamp" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getRefsOnlyTimeStampList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRefsOnlyTimeStampArray,
                this::setRefsOnlyTimeStampArray,
                this::insertNewRefsOnlyTimeStamp,
                this::removeRefsOnlyTimeStamp,
                this::sizeOfRefsOnlyTimeStampArray
            );
        }
    }

    /**
     * Gets array of all "RefsOnlyTimeStamp" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType[] getRefsOnlyTimeStampArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.etsi.uri.x01903.v13.XAdESTimeStampType[0]);
    }

    /**
     * Gets ith "RefsOnlyTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getRefsOnlyTimeStampArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "RefsOnlyTimeStamp" element
     */
    @Override
    public int sizeOfRefsOnlyTimeStampArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "RefsOnlyTimeStamp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRefsOnlyTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] refsOnlyTimeStampArray) {
        check_orphaned();
        arraySetterHelper(refsOnlyTimeStampArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "RefsOnlyTimeStamp" element
     */
    @Override
    public void setRefsOnlyTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType refsOnlyTimeStamp) {
        generatedSetterHelperImpl(refsOnlyTimeStamp, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "RefsOnlyTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewRefsOnlyTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "RefsOnlyTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewRefsOnlyTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "RefsOnlyTimeStamp" element
     */
    @Override
    public void removeRefsOnlyTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "CertificateValues" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CertificateValuesType> getCertificateValuesList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCertificateValuesArray,
                this::setCertificateValuesArray,
                this::insertNewCertificateValues,
                this::removeCertificateValues,
                this::sizeOfCertificateValuesArray
            );
        }
    }

    /**
     * Gets array of all "CertificateValues" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType[] getCertificateValuesArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.etsi.uri.x01903.v13.CertificateValuesType[0]);
    }

    /**
     * Gets ith "CertificateValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType getCertificateValuesArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CertificateValues" element
     */
    @Override
    public int sizeOfCertificateValuesArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "CertificateValues" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCertificateValuesArray(org.etsi.uri.x01903.v13.CertificateValuesType[] certificateValuesArray) {
        check_orphaned();
        arraySetterHelper(certificateValuesArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "CertificateValues" element
     */
    @Override
    public void setCertificateValuesArray(int i, org.etsi.uri.x01903.v13.CertificateValuesType certificateValues) {
        generatedSetterHelperImpl(certificateValues, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CertificateValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType insertNewCertificateValues(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CertificateValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType addNewCertificateValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "CertificateValues" element
     */
    @Override
    public void removeCertificateValues(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "RevocationValues" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.RevocationValuesType> getRevocationValuesList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRevocationValuesArray,
                this::setRevocationValuesArray,
                this::insertNewRevocationValues,
                this::removeRevocationValues,
                this::sizeOfRevocationValuesArray
            );
        }
    }

    /**
     * Gets array of all "RevocationValues" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType[] getRevocationValuesArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.etsi.uri.x01903.v13.RevocationValuesType[0]);
    }

    /**
     * Gets ith "RevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType getRevocationValuesArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "RevocationValues" element
     */
    @Override
    public int sizeOfRevocationValuesArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "RevocationValues" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRevocationValuesArray(org.etsi.uri.x01903.v13.RevocationValuesType[] revocationValuesArray) {
        check_orphaned();
        arraySetterHelper(revocationValuesArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "RevocationValues" element
     */
    @Override
    public void setRevocationValuesArray(int i, org.etsi.uri.x01903.v13.RevocationValuesType revocationValues) {
        generatedSetterHelperImpl(revocationValues, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "RevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType insertNewRevocationValues(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "RevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType addNewRevocationValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "RevocationValues" element
     */
    @Override
    public void removeRevocationValues(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "AttrAuthoritiesCertValues" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CertificateValuesType> getAttrAuthoritiesCertValuesList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAttrAuthoritiesCertValuesArray,
                this::setAttrAuthoritiesCertValuesArray,
                this::insertNewAttrAuthoritiesCertValues,
                this::removeAttrAuthoritiesCertValues,
                this::sizeOfAttrAuthoritiesCertValuesArray
            );
        }
    }

    /**
     * Gets array of all "AttrAuthoritiesCertValues" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType[] getAttrAuthoritiesCertValuesArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.etsi.uri.x01903.v13.CertificateValuesType[0]);
    }

    /**
     * Gets ith "AttrAuthoritiesCertValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType getAttrAuthoritiesCertValuesArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "AttrAuthoritiesCertValues" element
     */
    @Override
    public int sizeOfAttrAuthoritiesCertValuesArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "AttrAuthoritiesCertValues" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAttrAuthoritiesCertValuesArray(org.etsi.uri.x01903.v13.CertificateValuesType[] attrAuthoritiesCertValuesArray) {
        check_orphaned();
        arraySetterHelper(attrAuthoritiesCertValuesArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "AttrAuthoritiesCertValues" element
     */
    @Override
    public void setAttrAuthoritiesCertValuesArray(int i, org.etsi.uri.x01903.v13.CertificateValuesType attrAuthoritiesCertValues) {
        generatedSetterHelperImpl(attrAuthoritiesCertValues, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AttrAuthoritiesCertValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType insertNewAttrAuthoritiesCertValues(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "AttrAuthoritiesCertValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CertificateValuesType addNewAttrAuthoritiesCertValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CertificateValuesType target = null;
            target = (org.etsi.uri.x01903.v13.CertificateValuesType)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "AttrAuthoritiesCertValues" element
     */
    @Override
    public void removeAttrAuthoritiesCertValues(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "AttributeRevocationValues" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.RevocationValuesType> getAttributeRevocationValuesList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAttributeRevocationValuesArray,
                this::setAttributeRevocationValuesArray,
                this::insertNewAttributeRevocationValues,
                this::removeAttributeRevocationValues,
                this::sizeOfAttributeRevocationValuesArray
            );
        }
    }

    /**
     * Gets array of all "AttributeRevocationValues" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType[] getAttributeRevocationValuesArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.etsi.uri.x01903.v13.RevocationValuesType[0]);
    }

    /**
     * Gets ith "AttributeRevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType getAttributeRevocationValuesArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "AttributeRevocationValues" element
     */
    @Override
    public int sizeOfAttributeRevocationValuesArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "AttributeRevocationValues" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAttributeRevocationValuesArray(org.etsi.uri.x01903.v13.RevocationValuesType[] attributeRevocationValuesArray) {
        check_orphaned();
        arraySetterHelper(attributeRevocationValuesArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "AttributeRevocationValues" element
     */
    @Override
    public void setAttributeRevocationValuesArray(int i, org.etsi.uri.x01903.v13.RevocationValuesType attributeRevocationValues) {
        generatedSetterHelperImpl(attributeRevocationValues, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AttributeRevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType insertNewAttributeRevocationValues(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "AttributeRevocationValues" element
     */
    @Override
    public org.etsi.uri.x01903.v13.RevocationValuesType addNewAttributeRevocationValues() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.RevocationValuesType target = null;
            target = (org.etsi.uri.x01903.v13.RevocationValuesType)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "AttributeRevocationValues" element
     */
    @Override
    public void removeAttributeRevocationValues(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "ArchiveTimeStamp" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getArchiveTimeStampList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getArchiveTimeStampArray,
                this::setArchiveTimeStampArray,
                this::insertNewArchiveTimeStamp,
                this::removeArchiveTimeStamp,
                this::sizeOfArchiveTimeStampArray
            );
        }
    }

    /**
     * Gets array of all "ArchiveTimeStamp" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType[] getArchiveTimeStampArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.etsi.uri.x01903.v13.XAdESTimeStampType[0]);
    }

    /**
     * Gets ith "ArchiveTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getArchiveTimeStampArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ArchiveTimeStamp" element
     */
    @Override
    public int sizeOfArchiveTimeStampArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "ArchiveTimeStamp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setArchiveTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] archiveTimeStampArray) {
        check_orphaned();
        arraySetterHelper(archiveTimeStampArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "ArchiveTimeStamp" element
     */
    @Override
    public void setArchiveTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType archiveTimeStamp) {
        generatedSetterHelperImpl(archiveTimeStamp, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ArchiveTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewArchiveTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ArchiveTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewArchiveTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "ArchiveTimeStamp" element
     */
    @Override
    public void removeArchiveTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[13]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[13]);
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
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }
}

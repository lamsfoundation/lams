/*
 * XML Type:  OCSPValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OCSPValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML OCSPValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class OCSPValuesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.OCSPValuesType {
    private static final long serialVersionUID = 1L;

    public OCSPValuesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "EncapsulatedOCSPValue"),
    };


    /**
     * Gets a List of "EncapsulatedOCSPValue" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getEncapsulatedOCSPValueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEncapsulatedOCSPValueArray,
                this::setEncapsulatedOCSPValueArray,
                this::insertNewEncapsulatedOCSPValue,
                this::removeEncapsulatedOCSPValue,
                this::sizeOfEncapsulatedOCSPValueArray
            );
        }
    }

    /**
     * Gets array of all "EncapsulatedOCSPValue" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getEncapsulatedOCSPValueArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[0]);
    }

    /**
     * Gets ith "EncapsulatedOCSPValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedOCSPValueArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "EncapsulatedOCSPValue" element
     */
    @Override
    public int sizeOfEncapsulatedOCSPValueArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "EncapsulatedOCSPValue" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEncapsulatedOCSPValueArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] encapsulatedOCSPValueArray) {
        check_orphaned();
        arraySetterHelper(encapsulatedOCSPValueArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "EncapsulatedOCSPValue" element
     */
    @Override
    public void setEncapsulatedOCSPValueArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedOCSPValue) {
        generatedSetterHelperImpl(encapsulatedOCSPValue, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "EncapsulatedOCSPValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewEncapsulatedOCSPValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "EncapsulatedOCSPValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedOCSPValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "EncapsulatedOCSPValue" element
     */
    @Override
    public void removeEncapsulatedOCSPValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

/*
 * XML Type:  CRLValuesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CRLValuesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CRLValuesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class CRLValuesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.CRLValuesType {
    private static final long serialVersionUID = 1L;

    public CRLValuesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "EncapsulatedCRLValue"),
    };


    /**
     * Gets a List of "EncapsulatedCRLValue" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getEncapsulatedCRLValueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEncapsulatedCRLValueArray,
                this::setEncapsulatedCRLValueArray,
                this::insertNewEncapsulatedCRLValue,
                this::removeEncapsulatedCRLValue,
                this::sizeOfEncapsulatedCRLValueArray
            );
        }
    }

    /**
     * Gets array of all "EncapsulatedCRLValue" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getEncapsulatedCRLValueArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[0]);
    }

    /**
     * Gets ith "EncapsulatedCRLValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedCRLValueArray(int i) {
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
     * Returns number of "EncapsulatedCRLValue" element
     */
    @Override
    public int sizeOfEncapsulatedCRLValueArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "EncapsulatedCRLValue" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEncapsulatedCRLValueArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] encapsulatedCRLValueArray) {
        check_orphaned();
        arraySetterHelper(encapsulatedCRLValueArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "EncapsulatedCRLValue" element
     */
    @Override
    public void setEncapsulatedCRLValueArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedCRLValue) {
        generatedSetterHelperImpl(encapsulatedCRLValue, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "EncapsulatedCRLValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewEncapsulatedCRLValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "EncapsulatedCRLValue" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedCRLValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "EncapsulatedCRLValue" element
     */
    @Override
    public void removeEncapsulatedCRLValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

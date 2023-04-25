/*
 * XML Type:  OtherTimeStampType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OtherTimeStampType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML OtherTimeStampType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class OtherTimeStampTypeImpl extends org.etsi.uri.x01903.v13.impl.GenericTimeStampTypeImpl implements org.etsi.uri.x01903.v13.OtherTimeStampType {
    private static final long serialVersionUID = 1L;

    public OtherTimeStampTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ReferenceInfo"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "EncapsulatedTimeStamp"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "XMLTimeStamp"),
    };


    /**
     * Gets a List of "ReferenceInfo" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.ReferenceInfoType> getReferenceInfoList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getReferenceInfoArray,
                this::setReferenceInfoArray,
                this::insertNewReferenceInfo,
                this::removeReferenceInfo,
                this::sizeOfReferenceInfoArray
            );
        }
    }

    /**
     * Gets array of all "ReferenceInfo" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.ReferenceInfoType[] getReferenceInfoArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.ReferenceInfoType[0]);
    }

    /**
     * Gets ith "ReferenceInfo" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ReferenceInfoType getReferenceInfoArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ReferenceInfoType target = null;
            target = (org.etsi.uri.x01903.v13.ReferenceInfoType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ReferenceInfo" element
     */
    @Override
    public int sizeOfReferenceInfoArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ReferenceInfo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setReferenceInfoArray(org.etsi.uri.x01903.v13.ReferenceInfoType[] referenceInfoArray) {
        check_orphaned();
        arraySetterHelper(referenceInfoArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ReferenceInfo" element
     */
    @Override
    public void setReferenceInfoArray(int i, org.etsi.uri.x01903.v13.ReferenceInfoType referenceInfo) {
        generatedSetterHelperImpl(referenceInfo, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ReferenceInfo" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ReferenceInfoType insertNewReferenceInfo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ReferenceInfoType target = null;
            target = (org.etsi.uri.x01903.v13.ReferenceInfoType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ReferenceInfo" element
     */
    @Override
    public org.etsi.uri.x01903.v13.ReferenceInfoType addNewReferenceInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.ReferenceInfoType target = null;
            target = (org.etsi.uri.x01903.v13.ReferenceInfoType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ReferenceInfo" element
     */
    @Override
    public void removeReferenceInfo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "EncapsulatedTimeStamp" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.EncapsulatedPKIDataType> getEncapsulatedTimeStampList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEncapsulatedTimeStampArray,
                this::setEncapsulatedTimeStampArray,
                this::insertNewEncapsulatedTimeStamp,
                this::removeEncapsulatedTimeStamp,
                this::sizeOfEncapsulatedTimeStampArray
            );
        }
    }

    /**
     * Gets array of all "EncapsulatedTimeStamp" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] getEncapsulatedTimeStampArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[0]);
    }

    /**
     * Gets ith "EncapsulatedTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType getEncapsulatedTimeStampArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "EncapsulatedTimeStamp" element
     */
    @Override
    public int sizeOfEncapsulatedTimeStampArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "EncapsulatedTimeStamp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEncapsulatedTimeStampArray(org.etsi.uri.x01903.v13.EncapsulatedPKIDataType[] encapsulatedTimeStampArray) {
        check_orphaned();
        arraySetterHelper(encapsulatedTimeStampArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "EncapsulatedTimeStamp" element
     */
    @Override
    public void setEncapsulatedTimeStampArray(int i, org.etsi.uri.x01903.v13.EncapsulatedPKIDataType encapsulatedTimeStamp) {
        generatedSetterHelperImpl(encapsulatedTimeStamp, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "EncapsulatedTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType insertNewEncapsulatedTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "EncapsulatedTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.EncapsulatedPKIDataType addNewEncapsulatedTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.EncapsulatedPKIDataType target = null;
            target = (org.etsi.uri.x01903.v13.EncapsulatedPKIDataType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "EncapsulatedTimeStamp" element
     */
    @Override
    public void removeEncapsulatedTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "XMLTimeStamp" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.AnyType> getXMLTimeStampList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getXMLTimeStampArray,
                this::setXMLTimeStampArray,
                this::insertNewXMLTimeStamp,
                this::removeXMLTimeStamp,
                this::sizeOfXMLTimeStampArray
            );
        }
    }

    /**
     * Gets array of all "XMLTimeStamp" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType[] getXMLTimeStampArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.etsi.uri.x01903.v13.AnyType[0]);
    }

    /**
     * Gets ith "XMLTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType getXMLTimeStampArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "XMLTimeStamp" element
     */
    @Override
    public int sizeOfXMLTimeStampArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "XMLTimeStamp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setXMLTimeStampArray(org.etsi.uri.x01903.v13.AnyType[] xmlTimeStampArray) {
        check_orphaned();
        arraySetterHelper(xmlTimeStampArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "XMLTimeStamp" element
     */
    @Override
    public void setXMLTimeStampArray(int i, org.etsi.uri.x01903.v13.AnyType xmlTimeStamp) {
        generatedSetterHelperImpl(xmlTimeStamp, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "XMLTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType insertNewXMLTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "XMLTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.AnyType addNewXMLTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.AnyType target = null;
            target = (org.etsi.uri.x01903.v13.AnyType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "XMLTimeStamp" element
     */
    @Override
    public void removeXMLTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }
}

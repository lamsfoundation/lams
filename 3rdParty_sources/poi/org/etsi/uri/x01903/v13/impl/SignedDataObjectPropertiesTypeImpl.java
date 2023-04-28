/*
 * XML Type:  SignedDataObjectPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SignedDataObjectPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class SignedDataObjectPropertiesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SignedDataObjectPropertiesType {
    private static final long serialVersionUID = 1L;

    public SignedDataObjectPropertiesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "DataObjectFormat"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "CommitmentTypeIndication"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "AllDataObjectsTimeStamp"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "IndividualDataObjectsTimeStamp"),
        new QName("", "Id"),
    };


    /**
     * Gets a List of "DataObjectFormat" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.DataObjectFormatType> getDataObjectFormatList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDataObjectFormatArray,
                this::setDataObjectFormatArray,
                this::insertNewDataObjectFormat,
                this::removeDataObjectFormat,
                this::sizeOfDataObjectFormatArray
            );
        }
    }

    /**
     * Gets array of all "DataObjectFormat" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.DataObjectFormatType[] getDataObjectFormatArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.etsi.uri.x01903.v13.DataObjectFormatType[0]);
    }

    /**
     * Gets ith "DataObjectFormat" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DataObjectFormatType getDataObjectFormatArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DataObjectFormatType target = null;
            target = (org.etsi.uri.x01903.v13.DataObjectFormatType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "DataObjectFormat" element
     */
    @Override
    public int sizeOfDataObjectFormatArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "DataObjectFormat" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDataObjectFormatArray(org.etsi.uri.x01903.v13.DataObjectFormatType[] dataObjectFormatArray) {
        check_orphaned();
        arraySetterHelper(dataObjectFormatArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "DataObjectFormat" element
     */
    @Override
    public void setDataObjectFormatArray(int i, org.etsi.uri.x01903.v13.DataObjectFormatType dataObjectFormat) {
        generatedSetterHelperImpl(dataObjectFormat, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "DataObjectFormat" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DataObjectFormatType insertNewDataObjectFormat(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DataObjectFormatType target = null;
            target = (org.etsi.uri.x01903.v13.DataObjectFormatType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "DataObjectFormat" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DataObjectFormatType addNewDataObjectFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DataObjectFormatType target = null;
            target = (org.etsi.uri.x01903.v13.DataObjectFormatType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "DataObjectFormat" element
     */
    @Override
    public void removeDataObjectFormat(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "CommitmentTypeIndication" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.CommitmentTypeIndicationType> getCommitmentTypeIndicationList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCommitmentTypeIndicationArray,
                this::setCommitmentTypeIndicationArray,
                this::insertNewCommitmentTypeIndication,
                this::removeCommitmentTypeIndication,
                this::sizeOfCommitmentTypeIndicationArray
            );
        }
    }

    /**
     * Gets array of all "CommitmentTypeIndication" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.CommitmentTypeIndicationType[] getCommitmentTypeIndicationArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.etsi.uri.x01903.v13.CommitmentTypeIndicationType[0]);
    }

    /**
     * Gets ith "CommitmentTypeIndication" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CommitmentTypeIndicationType getCommitmentTypeIndicationArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CommitmentTypeIndicationType target = null;
            target = (org.etsi.uri.x01903.v13.CommitmentTypeIndicationType)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CommitmentTypeIndication" element
     */
    @Override
    public int sizeOfCommitmentTypeIndicationArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "CommitmentTypeIndication" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCommitmentTypeIndicationArray(org.etsi.uri.x01903.v13.CommitmentTypeIndicationType[] commitmentTypeIndicationArray) {
        check_orphaned();
        arraySetterHelper(commitmentTypeIndicationArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "CommitmentTypeIndication" element
     */
    @Override
    public void setCommitmentTypeIndicationArray(int i, org.etsi.uri.x01903.v13.CommitmentTypeIndicationType commitmentTypeIndication) {
        generatedSetterHelperImpl(commitmentTypeIndication, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CommitmentTypeIndication" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CommitmentTypeIndicationType insertNewCommitmentTypeIndication(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CommitmentTypeIndicationType target = null;
            target = (org.etsi.uri.x01903.v13.CommitmentTypeIndicationType)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CommitmentTypeIndication" element
     */
    @Override
    public org.etsi.uri.x01903.v13.CommitmentTypeIndicationType addNewCommitmentTypeIndication() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.CommitmentTypeIndicationType target = null;
            target = (org.etsi.uri.x01903.v13.CommitmentTypeIndicationType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "CommitmentTypeIndication" element
     */
    @Override
    public void removeCommitmentTypeIndication(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "AllDataObjectsTimeStamp" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getAllDataObjectsTimeStampList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAllDataObjectsTimeStampArray,
                this::setAllDataObjectsTimeStampArray,
                this::insertNewAllDataObjectsTimeStamp,
                this::removeAllDataObjectsTimeStamp,
                this::sizeOfAllDataObjectsTimeStampArray
            );
        }
    }

    /**
     * Gets array of all "AllDataObjectsTimeStamp" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType[] getAllDataObjectsTimeStampArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.etsi.uri.x01903.v13.XAdESTimeStampType[0]);
    }

    /**
     * Gets ith "AllDataObjectsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getAllDataObjectsTimeStampArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "AllDataObjectsTimeStamp" element
     */
    @Override
    public int sizeOfAllDataObjectsTimeStampArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "AllDataObjectsTimeStamp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAllDataObjectsTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] allDataObjectsTimeStampArray) {
        check_orphaned();
        arraySetterHelper(allDataObjectsTimeStampArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "AllDataObjectsTimeStamp" element
     */
    @Override
    public void setAllDataObjectsTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType allDataObjectsTimeStamp) {
        generatedSetterHelperImpl(allDataObjectsTimeStamp, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AllDataObjectsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewAllDataObjectsTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "AllDataObjectsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewAllDataObjectsTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "AllDataObjectsTimeStamp" element
     */
    @Override
    public void removeAllDataObjectsTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "IndividualDataObjectsTimeStamp" elements
     */
    @Override
    public java.util.List<org.etsi.uri.x01903.v13.XAdESTimeStampType> getIndividualDataObjectsTimeStampList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getIndividualDataObjectsTimeStampArray,
                this::setIndividualDataObjectsTimeStampArray,
                this::insertNewIndividualDataObjectsTimeStamp,
                this::removeIndividualDataObjectsTimeStamp,
                this::sizeOfIndividualDataObjectsTimeStampArray
            );
        }
    }

    /**
     * Gets array of all "IndividualDataObjectsTimeStamp" elements
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType[] getIndividualDataObjectsTimeStampArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.etsi.uri.x01903.v13.XAdESTimeStampType[0]);
    }

    /**
     * Gets ith "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType getIndividualDataObjectsTimeStampArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public int sizeOfIndividualDataObjectsTimeStampArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "IndividualDataObjectsTimeStamp" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setIndividualDataObjectsTimeStampArray(org.etsi.uri.x01903.v13.XAdESTimeStampType[] individualDataObjectsTimeStampArray) {
        check_orphaned();
        arraySetterHelper(individualDataObjectsTimeStampArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public void setIndividualDataObjectsTimeStampArray(int i, org.etsi.uri.x01903.v13.XAdESTimeStampType individualDataObjectsTimeStamp) {
        generatedSetterHelperImpl(individualDataObjectsTimeStamp, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType insertNewIndividualDataObjectsTimeStamp(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public org.etsi.uri.x01903.v13.XAdESTimeStampType addNewIndividualDataObjectsTimeStamp() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.XAdESTimeStampType target = null;
            target = (org.etsi.uri.x01903.v13.XAdESTimeStampType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "IndividualDataObjectsTimeStamp" element
     */
    @Override
    public void removeIndividualDataObjectsTimeStamp(int i) {
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

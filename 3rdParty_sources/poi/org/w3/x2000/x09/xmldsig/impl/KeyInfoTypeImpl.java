/*
 * XML Type:  KeyInfoType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.KeyInfoType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML KeyInfoType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class KeyInfoTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.KeyInfoType {
    private static final long serialVersionUID = 1L;

    public KeyInfoTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "KeyName"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "KeyValue"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "PGPData"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "SPKIData"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "MgmtData"),
        new QName("", "Id"),
    };


    /**
     * Gets a List of "KeyName" elements
     */
    @Override
    public java.util.List<java.lang.String> getKeyNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getKeyNameArray,
                this::setKeyNameArray,
                this::insertKeyName,
                this::removeKeyName,
                this::sizeOfKeyNameArray
            );
        }
    }

    /**
     * Gets array of all "KeyName" elements
     */
    @Override
    public java.lang.String[] getKeyNameArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "KeyName" element
     */
    @Override
    public java.lang.String getKeyNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "KeyName" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlString> xgetKeyNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetKeyNameArray,
                this::xsetKeyNameArray,
                this::insertNewKeyName,
                this::removeKeyName,
                this::sizeOfKeyNameArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "KeyName" elements
     */
    @Override
    public org.apache.xmlbeans.XmlString[] xgetKeyNameArray() {
        return xgetArray(PROPERTY_QNAME[0], org.apache.xmlbeans.XmlString[]::new);
    }

    /**
     * Gets (as xml) ith "KeyName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetKeyNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "KeyName" element
     */
    @Override
    public int sizeOfKeyNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "KeyName" element
     */
    @Override
    public void setKeyNameArray(java.lang.String[] keyNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(keyNameArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "KeyName" element
     */
    @Override
    public void setKeyNameArray(int i, java.lang.String keyName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(keyName);
        }
    }

    /**
     * Sets (as xml) array of all "KeyName" element
     */
    @Override
    public void xsetKeyNameArray(org.apache.xmlbeans.XmlString[]keyNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(keyNameArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "KeyName" element
     */
    @Override
    public void xsetKeyNameArray(int i, org.apache.xmlbeans.XmlString keyName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(keyName);
        }
    }

    /**
     * Inserts the value as the ith "KeyName" element
     */
    @Override
    public void insertKeyName(int i, java.lang.String keyName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(keyName);
        }
    }

    /**
     * Appends the value as the last "KeyName" element
     */
    @Override
    public void addKeyName(java.lang.String keyName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(keyName);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "KeyName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString insertNewKeyName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "KeyName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString addNewKeyName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "KeyName" element
     */
    @Override
    public void removeKeyName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "KeyValue" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.KeyValueType> getKeyValueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getKeyValueArray,
                this::setKeyValueArray,
                this::insertNewKeyValue,
                this::removeKeyValue,
                this::sizeOfKeyValueArray
            );
        }
    }

    /**
     * Gets array of all "KeyValue" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyValueType[] getKeyValueArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.w3.x2000.x09.xmldsig.KeyValueType[0]);
    }

    /**
     * Gets ith "KeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyValueType getKeyValueArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyValueType)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "KeyValue" element
     */
    @Override
    public int sizeOfKeyValueArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "KeyValue" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setKeyValueArray(org.w3.x2000.x09.xmldsig.KeyValueType[] keyValueArray) {
        check_orphaned();
        arraySetterHelper(keyValueArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "KeyValue" element
     */
    @Override
    public void setKeyValueArray(int i, org.w3.x2000.x09.xmldsig.KeyValueType keyValue) {
        generatedSetterHelperImpl(keyValue, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "KeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyValueType insertNewKeyValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyValueType)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "KeyValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.KeyValueType addNewKeyValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.KeyValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.KeyValueType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "KeyValue" element
     */
    @Override
    public void removeKeyValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "RetrievalMethod" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.RetrievalMethodType> getRetrievalMethodList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRetrievalMethodArray,
                this::setRetrievalMethodArray,
                this::insertNewRetrievalMethod,
                this::removeRetrievalMethod,
                this::sizeOfRetrievalMethodArray
            );
        }
    }

    /**
     * Gets array of all "RetrievalMethod" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.RetrievalMethodType[] getRetrievalMethodArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.w3.x2000.x09.xmldsig.RetrievalMethodType[0]);
    }

    /**
     * Gets ith "RetrievalMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.RetrievalMethodType getRetrievalMethodArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.RetrievalMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.RetrievalMethodType)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "RetrievalMethod" element
     */
    @Override
    public int sizeOfRetrievalMethodArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "RetrievalMethod" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRetrievalMethodArray(org.w3.x2000.x09.xmldsig.RetrievalMethodType[] retrievalMethodArray) {
        check_orphaned();
        arraySetterHelper(retrievalMethodArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "RetrievalMethod" element
     */
    @Override
    public void setRetrievalMethodArray(int i, org.w3.x2000.x09.xmldsig.RetrievalMethodType retrievalMethod) {
        generatedSetterHelperImpl(retrievalMethod, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "RetrievalMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.RetrievalMethodType insertNewRetrievalMethod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.RetrievalMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.RetrievalMethodType)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "RetrievalMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.RetrievalMethodType addNewRetrievalMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.RetrievalMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.RetrievalMethodType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "RetrievalMethod" element
     */
    @Override
    public void removeRetrievalMethod(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "X509Data" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.X509DataType> getX509DataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getX509DataArray,
                this::setX509DataArray,
                this::insertNewX509Data,
                this::removeX509Data,
                this::sizeOfX509DataArray
            );
        }
    }

    /**
     * Gets array of all "X509Data" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509DataType[] getX509DataArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.w3.x2000.x09.xmldsig.X509DataType[0]);
    }

    /**
     * Gets ith "X509Data" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509DataType getX509DataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509DataType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "X509Data" element
     */
    @Override
    public int sizeOfX509DataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "X509Data" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setX509DataArray(org.w3.x2000.x09.xmldsig.X509DataType[] x509DataArray) {
        check_orphaned();
        arraySetterHelper(x509DataArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "X509Data" element
     */
    @Override
    public void setX509DataArray(int i, org.w3.x2000.x09.xmldsig.X509DataType x509Data) {
        generatedSetterHelperImpl(x509Data, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509Data" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509DataType insertNewX509Data(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509DataType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "X509Data" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509DataType addNewX509Data() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509DataType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509DataType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "X509Data" element
     */
    @Override
    public void removeX509Data(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "PGPData" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.PGPDataType> getPGPDataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPGPDataArray,
                this::setPGPDataArray,
                this::insertNewPGPData,
                this::removePGPData,
                this::sizeOfPGPDataArray
            );
        }
    }

    /**
     * Gets array of all "PGPData" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.PGPDataType[] getPGPDataArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.w3.x2000.x09.xmldsig.PGPDataType[0]);
    }

    /**
     * Gets ith "PGPData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.PGPDataType getPGPDataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.PGPDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.PGPDataType)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "PGPData" element
     */
    @Override
    public int sizeOfPGPDataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "PGPData" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPGPDataArray(org.w3.x2000.x09.xmldsig.PGPDataType[] pgpDataArray) {
        check_orphaned();
        arraySetterHelper(pgpDataArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "PGPData" element
     */
    @Override
    public void setPGPDataArray(int i, org.w3.x2000.x09.xmldsig.PGPDataType pgpData) {
        generatedSetterHelperImpl(pgpData, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "PGPData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.PGPDataType insertNewPGPData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.PGPDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.PGPDataType)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "PGPData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.PGPDataType addNewPGPData() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.PGPDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.PGPDataType)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "PGPData" element
     */
    @Override
    public void removePGPData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "SPKIData" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.SPKIDataType> getSPKIDataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSPKIDataArray,
                this::setSPKIDataArray,
                this::insertNewSPKIData,
                this::removeSPKIData,
                this::sizeOfSPKIDataArray
            );
        }
    }

    /**
     * Gets array of all "SPKIData" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SPKIDataType[] getSPKIDataArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.w3.x2000.x09.xmldsig.SPKIDataType[0]);
    }

    /**
     * Gets ith "SPKIData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SPKIDataType getSPKIDataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SPKIDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.SPKIDataType)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "SPKIData" element
     */
    @Override
    public int sizeOfSPKIDataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "SPKIData" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSPKIDataArray(org.w3.x2000.x09.xmldsig.SPKIDataType[] spkiDataArray) {
        check_orphaned();
        arraySetterHelper(spkiDataArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "SPKIData" element
     */
    @Override
    public void setSPKIDataArray(int i, org.w3.x2000.x09.xmldsig.SPKIDataType spkiData) {
        generatedSetterHelperImpl(spkiData, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SPKIData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SPKIDataType insertNewSPKIData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SPKIDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.SPKIDataType)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "SPKIData" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.SPKIDataType addNewSPKIData() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.SPKIDataType target = null;
            target = (org.w3.x2000.x09.xmldsig.SPKIDataType)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "SPKIData" element
     */
    @Override
    public void removeSPKIData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "MgmtData" elements
     */
    @Override
    public java.util.List<java.lang.String> getMgmtDataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getMgmtDataArray,
                this::setMgmtDataArray,
                this::insertMgmtData,
                this::removeMgmtData,
                this::sizeOfMgmtDataArray
            );
        }
    }

    /**
     * Gets array of all "MgmtData" elements
     */
    @Override
    public java.lang.String[] getMgmtDataArray() {
        return getObjectArray(PROPERTY_QNAME[6], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "MgmtData" element
     */
    @Override
    public java.lang.String getMgmtDataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "MgmtData" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlString> xgetMgmtDataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetMgmtDataArray,
                this::xsetMgmtDataArray,
                this::insertNewMgmtData,
                this::removeMgmtData,
                this::sizeOfMgmtDataArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "MgmtData" elements
     */
    @Override
    public org.apache.xmlbeans.XmlString[] xgetMgmtDataArray() {
        return xgetArray(PROPERTY_QNAME[6], org.apache.xmlbeans.XmlString[]::new);
    }

    /**
     * Gets (as xml) ith "MgmtData" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetMgmtDataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "MgmtData" element
     */
    @Override
    public int sizeOfMgmtDataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "MgmtData" element
     */
    @Override
    public void setMgmtDataArray(java.lang.String[] mgmtDataArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(mgmtDataArray, PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets ith "MgmtData" element
     */
    @Override
    public void setMgmtDataArray(int i, java.lang.String mgmtData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(mgmtData);
        }
    }

    /**
     * Sets (as xml) array of all "MgmtData" element
     */
    @Override
    public void xsetMgmtDataArray(org.apache.xmlbeans.XmlString[]mgmtDataArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(mgmtDataArray, PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets (as xml) ith "MgmtData" element
     */
    @Override
    public void xsetMgmtDataArray(int i, org.apache.xmlbeans.XmlString mgmtData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(mgmtData);
        }
    }

    /**
     * Inserts the value as the ith "MgmtData" element
     */
    @Override
    public void insertMgmtData(int i, java.lang.String mgmtData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            target.setStringValue(mgmtData);
        }
    }

    /**
     * Appends the value as the last "MgmtData" element
     */
    @Override
    public void addMgmtData(java.lang.String mgmtData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[6]);
            target.setStringValue(mgmtData);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "MgmtData" element
     */
    @Override
    public org.apache.xmlbeans.XmlString insertNewMgmtData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "MgmtData" element
     */
    @Override
    public org.apache.xmlbeans.XmlString addNewMgmtData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "MgmtData" element
     */
    @Override
    public void removeMgmtData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[7]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
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
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[7]);
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
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }
}

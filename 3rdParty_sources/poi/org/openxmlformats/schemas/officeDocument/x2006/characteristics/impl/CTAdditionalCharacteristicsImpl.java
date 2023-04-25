/*
 * XML Type:  CT_AdditionalCharacteristics
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/characteristics
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.characteristics.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AdditionalCharacteristics(@http://schemas.openxmlformats.org/officeDocument/2006/characteristics).
 *
 * This is a complex type.
 */
public class CTAdditionalCharacteristicsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics {
    private static final long serialVersionUID = 1L;

    public CTAdditionalCharacteristicsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/characteristics", "characteristic"),
    };


    /**
     * Gets a List of "characteristic" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic> getCharacteristicList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCharacteristicArray,
                this::setCharacteristicArray,
                this::insertNewCharacteristic,
                this::removeCharacteristic,
                this::sizeOfCharacteristicArray
            );
        }
    }

    /**
     * Gets array of all "characteristic" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic[] getCharacteristicArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic[0]);
    }

    /**
     * Gets ith "characteristic" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic getCharacteristicArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "characteristic" element
     */
    @Override
    public int sizeOfCharacteristicArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "characteristic" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCharacteristicArray(org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic[] characteristicArray) {
        check_orphaned();
        arraySetterHelper(characteristicArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "characteristic" element
     */
    @Override
    public void setCharacteristicArray(int i, org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic characteristic) {
        generatedSetterHelperImpl(characteristic, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "characteristic" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic insertNewCharacteristic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "characteristic" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic addNewCharacteristic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "characteristic" element
     */
    @Override
    public void removeCharacteristic(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

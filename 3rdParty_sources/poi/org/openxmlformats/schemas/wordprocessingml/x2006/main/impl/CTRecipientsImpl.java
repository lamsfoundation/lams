/*
 * XML Type:  CT_Recipients
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Recipients(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTRecipientsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients {
    private static final long serialVersionUID = 1L;

    public CTRecipientsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "recipientData"),
    };


    /**
     * Gets a List of "recipientData" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData> getRecipientDataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRecipientDataArray,
                this::setRecipientDataArray,
                this::insertNewRecipientData,
                this::removeRecipientData,
                this::sizeOfRecipientDataArray
            );
        }
    }

    /**
     * Gets array of all "recipientData" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData[] getRecipientDataArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData[0]);
    }

    /**
     * Gets ith "recipientData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData getRecipientDataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "recipientData" element
     */
    @Override
    public int sizeOfRecipientDataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "recipientData" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRecipientDataArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData[] recipientDataArray) {
        check_orphaned();
        arraySetterHelper(recipientDataArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "recipientData" element
     */
    @Override
    public void setRecipientDataArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData recipientData) {
        generatedSetterHelperImpl(recipientData, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "recipientData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData insertNewRecipientData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "recipientData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData addNewRecipientData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipientData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "recipientData" element
     */
    @Override
    public void removeRecipientData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

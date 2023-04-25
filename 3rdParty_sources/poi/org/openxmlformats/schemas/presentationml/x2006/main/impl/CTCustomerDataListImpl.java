/*
 * XML Type:  CT_CustomerDataList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CustomerDataList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTCustomerDataListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerDataList {
    private static final long serialVersionUID = 1L;

    public CTCustomerDataListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "custData"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tags"),
    };


    /**
     * Gets a List of "custData" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData> getCustDataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustDataArray,
                this::setCustDataArray,
                this::insertNewCustData,
                this::removeCustData,
                this::sizeOfCustDataArray
            );
        }
    }

    /**
     * Gets array of all "custData" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData[] getCustDataArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData[0]);
    }

    /**
     * Gets ith "custData" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData getCustDataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "custData" element
     */
    @Override
    public int sizeOfCustDataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "custData" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustDataArray(org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData[] custDataArray) {
        check_orphaned();
        arraySetterHelper(custDataArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "custData" element
     */
    @Override
    public void setCustDataArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData custData) {
        generatedSetterHelperImpl(custData, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "custData" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData insertNewCustData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "custData" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData addNewCustData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomerData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "custData" element
     */
    @Override
    public void removeCustData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "tags" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData getTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tags" element
     */
    @Override
    public boolean isSetTags() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "tags" element
     */
    @Override
    public void setTags(org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData tags) {
        generatedSetterHelperImpl(tags, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tags" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData addNewTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTagsData)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "tags" element
     */
    @Override
    public void unsetTags() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

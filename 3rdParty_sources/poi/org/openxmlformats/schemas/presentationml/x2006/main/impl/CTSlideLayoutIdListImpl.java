/*
 * XML Type:  CT_SlideLayoutIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SlideLayoutIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideLayoutIdListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList {
    private static final long serialVersionUID = 1L;

    public CTSlideLayoutIdListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldLayoutId"),
    };


    /**
     * Gets a List of "sldLayoutId" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry> getSldLayoutIdList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSldLayoutIdArray,
                this::setSldLayoutIdArray,
                this::insertNewSldLayoutId,
                this::removeSldLayoutId,
                this::sizeOfSldLayoutIdArray
            );
        }
    }

    /**
     * Gets array of all "sldLayoutId" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry[] getSldLayoutIdArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry[0]);
    }

    /**
     * Gets ith "sldLayoutId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry getSldLayoutIdArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sldLayoutId" element
     */
    @Override
    public int sizeOfSldLayoutIdArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "sldLayoutId" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSldLayoutIdArray(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry[] sldLayoutIdArray) {
        check_orphaned();
        arraySetterHelper(sldLayoutIdArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "sldLayoutId" element
     */
    @Override
    public void setSldLayoutIdArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry sldLayoutId) {
        generatedSetterHelperImpl(sldLayoutId, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sldLayoutId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry insertNewSldLayoutId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sldLayoutId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry addNewSldLayoutId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdListEntry)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "sldLayoutId" element
     */
    @Override
    public void removeSldLayoutId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

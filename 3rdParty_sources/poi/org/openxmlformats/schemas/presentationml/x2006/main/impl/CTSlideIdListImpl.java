/*
 * XML Type:  CT_SlideIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SlideIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideIdListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdList {
    private static final long serialVersionUID = 1L;

    public CTSlideIdListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldId"),
    };


    /**
     * Gets a List of "sldId" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry> getSldIdList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSldIdArray,
                this::setSldIdArray,
                this::insertNewSldId,
                this::removeSldId,
                this::sizeOfSldIdArray
            );
        }
    }

    /**
     * Gets array of all "sldId" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry[] getSldIdArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry[0]);
    }

    /**
     * Gets ith "sldId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry getSldIdArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sldId" element
     */
    @Override
    public int sizeOfSldIdArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "sldId" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSldIdArray(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry[] sldIdArray) {
        check_orphaned();
        arraySetterHelper(sldIdArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "sldId" element
     */
    @Override
    public void setSldIdArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry sldId) {
        generatedSetterHelperImpl(sldId, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sldId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry insertNewSldId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sldId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry addNewSldId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideIdListEntry)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "sldId" element
     */
    @Override
    public void removeSldId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

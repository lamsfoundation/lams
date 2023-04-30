/*
 * XML Type:  CT_SlideMasterIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SlideMasterIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideMasterIdListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdList {
    private static final long serialVersionUID = 1L;

    public CTSlideMasterIdListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldMasterId"),
    };


    /**
     * Gets a List of "sldMasterId" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry> getSldMasterIdList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSldMasterIdArray,
                this::setSldMasterIdArray,
                this::insertNewSldMasterId,
                this::removeSldMasterId,
                this::sizeOfSldMasterIdArray
            );
        }
    }

    /**
     * Gets array of all "sldMasterId" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry[] getSldMasterIdArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry[0]);
    }

    /**
     * Gets ith "sldMasterId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry getSldMasterIdArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sldMasterId" element
     */
    @Override
    public int sizeOfSldMasterIdArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "sldMasterId" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSldMasterIdArray(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry[] sldMasterIdArray) {
        check_orphaned();
        arraySetterHelper(sldMasterIdArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "sldMasterId" element
     */
    @Override
    public void setSldMasterIdArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry sldMasterId) {
        generatedSetterHelperImpl(sldMasterId, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sldMasterId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry insertNewSldMasterId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sldMasterId" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry addNewSldMasterId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterIdListEntry)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "sldMasterId" element
     */
    @Override
    public void removeSldMasterId(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

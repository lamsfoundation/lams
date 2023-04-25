/*
 * XML Type:  CT_MetadataBlock
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MetadataBlock(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTMetadataBlockImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlock {
    private static final long serialVersionUID = 1L;

    public CTMetadataBlockImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rc"),
    };


    /**
     * Gets a List of "rc" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord> getRcList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRcArray,
                this::setRcArray,
                this::insertNewRc,
                this::removeRc,
                this::sizeOfRcArray
            );
        }
    }

    /**
     * Gets array of all "rc" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord[] getRcArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord[0]);
    }

    /**
     * Gets ith "rc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord getRcArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "rc" element
     */
    @Override
    public int sizeOfRcArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "rc" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRcArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord[] rcArray) {
        check_orphaned();
        arraySetterHelper(rcArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "rc" element
     */
    @Override
    public void setRcArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord rc) {
        generatedSetterHelperImpl(rc, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord insertNewRc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "rc" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord addNewRc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataRecord)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "rc" element
     */
    @Override
    public void removeRc(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

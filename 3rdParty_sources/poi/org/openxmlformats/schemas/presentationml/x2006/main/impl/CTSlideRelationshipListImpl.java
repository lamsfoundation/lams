/*
 * XML Type:  CT_SlideRelationshipList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SlideRelationshipList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideRelationshipListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipList {
    private static final long serialVersionUID = 1L;

    public CTSlideRelationshipListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sld"),
    };


    /**
     * Gets a List of "sld" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry> getSldList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSldArray,
                this::setSldArray,
                this::insertNewSld,
                this::removeSld,
                this::sizeOfSldArray
            );
        }
    }

    /**
     * Gets array of all "sld" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry[] getSldArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry[0]);
    }

    /**
     * Gets ith "sld" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry getSldArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "sld" element
     */
    @Override
    public int sizeOfSldArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "sld" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSldArray(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry[] sldArray) {
        check_orphaned();
        arraySetterHelper(sldArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "sld" element
     */
    @Override
    public void setSldArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry sld) {
        generatedSetterHelperImpl(sld, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sld" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry insertNewSld(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "sld" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry addNewSld() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideRelationshipListEntry)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "sld" element
     */
    @Override
    public void removeSld(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

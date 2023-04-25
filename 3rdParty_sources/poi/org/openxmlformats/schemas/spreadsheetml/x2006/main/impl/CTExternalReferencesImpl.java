/*
 * XML Type:  CT_ExternalReferences
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ExternalReferences(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTExternalReferencesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences {
    private static final long serialVersionUID = 1L;

    public CTExternalReferencesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "externalReference"),
    };


    /**
     * Gets a List of "externalReference" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference> getExternalReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getExternalReferenceArray,
                this::setExternalReferenceArray,
                this::insertNewExternalReference,
                this::removeExternalReference,
                this::sizeOfExternalReferenceArray
            );
        }
    }

    /**
     * Gets array of all "externalReference" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference[] getExternalReferenceArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference[0]);
    }

    /**
     * Gets ith "externalReference" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference getExternalReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "externalReference" element
     */
    @Override
    public int sizeOfExternalReferenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "externalReference" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExternalReferenceArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference[] externalReferenceArray) {
        check_orphaned();
        arraySetterHelper(externalReferenceArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "externalReference" element
     */
    @Override
    public void setExternalReferenceArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference externalReference) {
        generatedSetterHelperImpl(externalReference, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "externalReference" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference insertNewExternalReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "externalReference" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference addNewExternalReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "externalReference" element
     */
    @Override
    public void removeExternalReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

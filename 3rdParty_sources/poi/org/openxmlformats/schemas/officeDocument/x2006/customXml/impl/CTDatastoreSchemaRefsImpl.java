/*
 * XML Type:  CT_DatastoreSchemaRefs
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/customXml
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customXml.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DatastoreSchemaRefs(@http://schemas.openxmlformats.org/officeDocument/2006/customXml).
 *
 * This is a complex type.
 */
public class CTDatastoreSchemaRefsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs {
    private static final long serialVersionUID = 1L;

    public CTDatastoreSchemaRefsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/customXml", "schemaRef"),
    };


    /**
     * Gets a List of "schemaRef" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef> getSchemaRefList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSchemaRefArray,
                this::setSchemaRefArray,
                this::insertNewSchemaRef,
                this::removeSchemaRef,
                this::sizeOfSchemaRefArray
            );
        }
    }

    /**
     * Gets array of all "schemaRef" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef[] getSchemaRefArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef[0]);
    }

    /**
     * Gets ith "schemaRef" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef getSchemaRefArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "schemaRef" element
     */
    @Override
    public int sizeOfSchemaRefArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "schemaRef" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSchemaRefArray(org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef[] schemaRefArray) {
        check_orphaned();
        arraySetterHelper(schemaRefArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "schemaRef" element
     */
    @Override
    public void setSchemaRefArray(int i, org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef schemaRef) {
        generatedSetterHelperImpl(schemaRef, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "schemaRef" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef insertNewSchemaRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "schemaRef" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef addNewSchemaRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "schemaRef" element
     */
    @Override
    public void removeSchemaRef(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

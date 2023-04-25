/*
 * XML Type:  CT_SchemaLibrary
 * Namespace: http://schemas.openxmlformats.org/schemaLibrary/2006/main
 * Java type: org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.schemaLibrary.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SchemaLibrary(@http://schemas.openxmlformats.org/schemaLibrary/2006/main).
 *
 * This is a complex type.
 */
public class CTSchemaLibraryImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary {
    private static final long serialVersionUID = 1L;

    public CTSchemaLibraryImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/schemaLibrary/2006/main", "schema"),
    };


    /**
     * Gets a List of "schema" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema> getSchemaList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSchemaArray,
                this::setSchemaArray,
                this::insertNewSchema,
                this::removeSchema,
                this::sizeOfSchemaArray
            );
        }
    }

    /**
     * Gets array of all "schema" elements
     */
    @Override
    public org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema[] getSchemaArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema[0]);
    }

    /**
     * Gets ith "schema" element
     */
    @Override
    public org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema getSchemaArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema target = null;
            target = (org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "schema" element
     */
    @Override
    public int sizeOfSchemaArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "schema" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSchemaArray(org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema[] schemaArray) {
        check_orphaned();
        arraySetterHelper(schemaArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "schema" element
     */
    @Override
    public void setSchemaArray(int i, org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema schema) {
        generatedSetterHelperImpl(schema, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "schema" element
     */
    @Override
    public org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema insertNewSchema(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema target = null;
            target = (org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "schema" element
     */
    @Override
    public org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema addNewSchema() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema target = null;
            target = (org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "schema" element
     */
    @Override
    public void removeSchema(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

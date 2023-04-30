/*
 * XML Type:  CT_Properties
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/custom-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customProperties.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Properties(@http://schemas.openxmlformats.org/officeDocument/2006/custom-properties).
 *
 * This is a complex type.
 */
public class CTPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties {
    private static final long serialVersionUID = 1L;

    public CTPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/custom-properties", "property"),
    };


    /**
     * Gets a List of "property" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty> getPropertyList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPropertyArray,
                this::setPropertyArray,
                this::insertNewProperty,
                this::removeProperty,
                this::sizeOfPropertyArray
            );
        }
    }

    /**
     * Gets array of all "property" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty[] getPropertyArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty[0]);
    }

    /**
     * Gets ith "property" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty getPropertyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "property" element
     */
    @Override
    public int sizeOfPropertyArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "property" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPropertyArray(org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty[] propertyArray) {
        check_orphaned();
        arraySetterHelper(propertyArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "property" element
     */
    @Override
    public void setPropertyArray(int i, org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty property) {
        generatedSetterHelperImpl(property, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "property" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty insertNewProperty(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "property" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty addNewProperty() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "property" element
     */
    @Override
    public void removeProperty(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

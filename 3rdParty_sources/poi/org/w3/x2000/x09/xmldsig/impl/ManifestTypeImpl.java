/*
 * XML Type:  ManifestType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.ManifestType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML ManifestType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class ManifestTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.ManifestType {
    private static final long serialVersionUID = 1L;

    public ManifestTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "Reference"),
        new QName("", "Id"),
    };


    /**
     * Gets a List of "Reference" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.ReferenceType> getReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getReferenceArray,
                this::setReferenceArray,
                this::insertNewReference,
                this::removeReference,
                this::sizeOfReferenceArray
            );
        }
    }

    /**
     * Gets array of all "Reference" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType[] getReferenceArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.w3.x2000.x09.xmldsig.ReferenceType[0]);
    }

    /**
     * Gets ith "Reference" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType getReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Reference" element
     */
    @Override
    public int sizeOfReferenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Reference" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setReferenceArray(org.w3.x2000.x09.xmldsig.ReferenceType[] referenceArray) {
        check_orphaned();
        arraySetterHelper(referenceArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "Reference" element
     */
    @Override
    public void setReferenceArray(int i, org.w3.x2000.x09.xmldsig.ReferenceType reference) {
        generatedSetterHelperImpl(reference, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Reference" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType insertNewReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Reference" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.ReferenceType addNewReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.ReferenceType target = null;
            target = (org.w3.x2000.x09.xmldsig.ReferenceType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Reference" element
     */
    @Override
    public void removeReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "Id" attribute
     */
    @Override
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Id" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlID xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "Id" attribute
     */
    @Override
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "Id" attribute
     */
    @Override
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "Id" attribute
     */
    @Override
    public void xsetId(org.apache.xmlbeans.XmlID id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "Id" attribute
     */
    @Override
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

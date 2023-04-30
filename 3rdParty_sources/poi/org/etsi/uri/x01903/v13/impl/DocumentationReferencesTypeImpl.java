/*
 * XML Type:  DocumentationReferencesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.DocumentationReferencesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML DocumentationReferencesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class DocumentationReferencesTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.DocumentationReferencesType {
    private static final long serialVersionUID = 1L;

    public DocumentationReferencesTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "DocumentationReference"),
    };


    /**
     * Gets a List of "DocumentationReference" elements
     */
    @Override
    public java.util.List<java.lang.String> getDocumentationReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getDocumentationReferenceArray,
                this::setDocumentationReferenceArray,
                this::insertDocumentationReference,
                this::removeDocumentationReference,
                this::sizeOfDocumentationReferenceArray
            );
        }
    }

    /**
     * Gets array of all "DocumentationReference" elements
     */
    @Override
    public java.lang.String[] getDocumentationReferenceArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "DocumentationReference" element
     */
    @Override
    public java.lang.String getDocumentationReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "DocumentationReference" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlAnyURI> xgetDocumentationReferenceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetDocumentationReferenceArray,
                this::xsetDocumentationReferenceArray,
                this::insertNewDocumentationReference,
                this::removeDocumentationReference,
                this::sizeOfDocumentationReferenceArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "DocumentationReference" elements
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI[] xgetDocumentationReferenceArray() {
        return xgetArray(PROPERTY_QNAME[0], org.apache.xmlbeans.XmlAnyURI[]::new);
    }

    /**
     * Gets (as xml) ith "DocumentationReference" element
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetDocumentationReferenceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "DocumentationReference" element
     */
    @Override
    public int sizeOfDocumentationReferenceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "DocumentationReference" element
     */
    @Override
    public void setDocumentationReferenceArray(java.lang.String[] documentationReferenceArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(documentationReferenceArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "DocumentationReference" element
     */
    @Override
    public void setDocumentationReferenceArray(int i, java.lang.String documentationReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(documentationReference);
        }
    }

    /**
     * Sets (as xml) array of all "DocumentationReference" element
     */
    @Override
    public void xsetDocumentationReferenceArray(org.apache.xmlbeans.XmlAnyURI[]documentationReferenceArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(documentationReferenceArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "DocumentationReference" element
     */
    @Override
    public void xsetDocumentationReferenceArray(int i, org.apache.xmlbeans.XmlAnyURI documentationReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(documentationReference);
        }
    }

    /**
     * Inserts the value as the ith "DocumentationReference" element
     */
    @Override
    public void insertDocumentationReference(int i, java.lang.String documentationReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(documentationReference);
        }
    }

    /**
     * Appends the value as the last "DocumentationReference" element
     */
    @Override
    public void addDocumentationReference(java.lang.String documentationReference) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(documentationReference);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "DocumentationReference" element
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI insertNewDocumentationReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "DocumentationReference" element
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI addNewDocumentationReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "DocumentationReference" element
     */
    @Override
    public void removeDocumentationReference(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

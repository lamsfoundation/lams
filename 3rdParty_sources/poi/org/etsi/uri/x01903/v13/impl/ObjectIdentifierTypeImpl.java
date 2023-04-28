/*
 * XML Type:  ObjectIdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ObjectIdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML ObjectIdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class ObjectIdentifierTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.ObjectIdentifierType {
    private static final long serialVersionUID = 1L;

    public ObjectIdentifierTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "Identifier"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "Description"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "DocumentationReferences"),
    };


    /**
     * Gets the "Identifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.IdentifierType getIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.IdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.IdentifierType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "Identifier" element
     */
    @Override
    public void setIdentifier(org.etsi.uri.x01903.v13.IdentifierType identifier) {
        generatedSetterHelperImpl(identifier, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "Identifier" element
     */
    @Override
    public org.etsi.uri.x01903.v13.IdentifierType addNewIdentifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.IdentifierType target = null;
            target = (org.etsi.uri.x01903.v13.IdentifierType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "Description" element
     */
    @Override
    public java.lang.String getDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Description" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "Description" element
     */
    @Override
    public boolean isSetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "Description" element
     */
    @Override
    public void setDescription(java.lang.String description) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(description);
        }
    }

    /**
     * Sets (as xml) the "Description" element
     */
    @Override
    public void xsetDescription(org.apache.xmlbeans.XmlString description) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(description);
        }
    }

    /**
     * Unsets the "Description" element
     */
    @Override
    public void unsetDescription() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "DocumentationReferences" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DocumentationReferencesType getDocumentationReferences() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DocumentationReferencesType target = null;
            target = (org.etsi.uri.x01903.v13.DocumentationReferencesType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "DocumentationReferences" element
     */
    @Override
    public boolean isSetDocumentationReferences() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "DocumentationReferences" element
     */
    @Override
    public void setDocumentationReferences(org.etsi.uri.x01903.v13.DocumentationReferencesType documentationReferences) {
        generatedSetterHelperImpl(documentationReferences, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DocumentationReferences" element
     */
    @Override
    public org.etsi.uri.x01903.v13.DocumentationReferencesType addNewDocumentationReferences() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.DocumentationReferencesType target = null;
            target = (org.etsi.uri.x01903.v13.DocumentationReferencesType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "DocumentationReferences" element
     */
    @Override
    public void unsetDocumentationReferences() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }
}

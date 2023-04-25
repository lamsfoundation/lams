/*
 * XML Type:  IdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML IdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is an atomic type that is a restriction of org.etsi.uri.x01903.v13.IdentifierType.
 */
public class IdentifierTypeImpl extends org.apache.xmlbeans.impl.values.JavaUriHolderEx implements org.etsi.uri.x01903.v13.IdentifierType {
    private static final long serialVersionUID = 1L;

    public IdentifierTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType, true);
    }

    protected IdentifierTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
        super(sType, b);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "Qualifier"),
    };


    /**
     * Gets the "Qualifier" attribute
     */
    @Override
    public org.etsi.uri.x01903.v13.QualifierType.Enum getQualifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.etsi.uri.x01903.v13.QualifierType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "Qualifier" attribute
     */
    @Override
    public org.etsi.uri.x01903.v13.QualifierType xgetQualifier() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.QualifierType target = null;
            target = (org.etsi.uri.x01903.v13.QualifierType)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "Qualifier" attribute
     */
    @Override
    public boolean isSetQualifier() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "Qualifier" attribute
     */
    @Override
    public void setQualifier(org.etsi.uri.x01903.v13.QualifierType.Enum qualifier) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(qualifier);
        }
    }

    /**
     * Sets (as xml) the "Qualifier" attribute
     */
    @Override
    public void xsetQualifier(org.etsi.uri.x01903.v13.QualifierType qualifier) {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.QualifierType target = null;
            target = (org.etsi.uri.x01903.v13.QualifierType)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.etsi.uri.x01903.v13.QualifierType)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(qualifier);
        }
    }

    /**
     * Unsets the "Qualifier" attribute
     */
    @Override
    public void unsetQualifier() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }
}

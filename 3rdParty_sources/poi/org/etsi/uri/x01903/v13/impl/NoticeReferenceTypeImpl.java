/*
 * XML Type:  NoticeReferenceType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.NoticeReferenceType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML NoticeReferenceType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class NoticeReferenceTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.NoticeReferenceType {
    private static final long serialVersionUID = 1L;

    public NoticeReferenceTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "Organization"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "NoticeNumbers"),
    };


    /**
     * Gets the "Organization" element
     */
    @Override
    public java.lang.String getOrganization() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Organization" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetOrganization() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "Organization" element
     */
    @Override
    public void setOrganization(java.lang.String organization) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(organization);
        }
    }

    /**
     * Sets (as xml) the "Organization" element
     */
    @Override
    public void xsetOrganization(org.apache.xmlbeans.XmlString organization) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(organization);
        }
    }

    /**
     * Gets the "NoticeNumbers" element
     */
    @Override
    public org.etsi.uri.x01903.v13.IntegerListType getNoticeNumbers() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.IntegerListType target = null;
            target = (org.etsi.uri.x01903.v13.IntegerListType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "NoticeNumbers" element
     */
    @Override
    public void setNoticeNumbers(org.etsi.uri.x01903.v13.IntegerListType noticeNumbers) {
        generatedSetterHelperImpl(noticeNumbers, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "NoticeNumbers" element
     */
    @Override
    public org.etsi.uri.x01903.v13.IntegerListType addNewNoticeNumbers() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.IntegerListType target = null;
            target = (org.etsi.uri.x01903.v13.IntegerListType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }
}

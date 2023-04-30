/*
 * XML Type:  SPUserNoticeType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SPUserNoticeType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML SPUserNoticeType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class SPUserNoticeTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.SPUserNoticeType {
    private static final long serialVersionUID = 1L;

    public SPUserNoticeTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://uri.etsi.org/01903/v1.3.2#", "NoticeRef"),
        new QName("http://uri.etsi.org/01903/v1.3.2#", "ExplicitText"),
    };


    /**
     * Gets the "NoticeRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.NoticeReferenceType getNoticeRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.NoticeReferenceType target = null;
            target = (org.etsi.uri.x01903.v13.NoticeReferenceType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "NoticeRef" element
     */
    @Override
    public boolean isSetNoticeRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "NoticeRef" element
     */
    @Override
    public void setNoticeRef(org.etsi.uri.x01903.v13.NoticeReferenceType noticeRef) {
        generatedSetterHelperImpl(noticeRef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "NoticeRef" element
     */
    @Override
    public org.etsi.uri.x01903.v13.NoticeReferenceType addNewNoticeRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.etsi.uri.x01903.v13.NoticeReferenceType target = null;
            target = (org.etsi.uri.x01903.v13.NoticeReferenceType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "NoticeRef" element
     */
    @Override
    public void unsetNoticeRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "ExplicitText" element
     */
    @Override
    public java.lang.String getExplicitText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "ExplicitText" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetExplicitText() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "ExplicitText" element
     */
    @Override
    public boolean isSetExplicitText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "ExplicitText" element
     */
    @Override
    public void setExplicitText(java.lang.String explicitText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(explicitText);
        }
    }

    /**
     * Sets (as xml) the "ExplicitText" element
     */
    @Override
    public void xsetExplicitText(org.apache.xmlbeans.XmlString explicitText) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(explicitText);
        }
    }

    /**
     * Unsets the "ExplicitText" element
     */
    @Override
    public void unsetExplicitText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

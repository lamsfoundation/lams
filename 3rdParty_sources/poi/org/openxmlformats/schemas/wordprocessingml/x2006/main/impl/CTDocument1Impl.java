/*
 * XML Type:  CT_Document
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Document(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocument1Impl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTDocumentBaseImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1 {
    private static final long serialVersionUID = 1L;

    public CTDocument1Impl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "body"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "conformance"),
    };


    /**
     * Gets the "body" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody getBody() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "body" element
     */
    @Override
    public boolean isSetBody() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "body" element
     */
    @Override
    public void setBody(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody body) {
        generatedSetterHelperImpl(body, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "body" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody addNewBody() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "body" element
     */
    @Override
    public void unsetBody() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "conformance" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum getConformance() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "conformance" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass xgetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "conformance" attribute
     */
    @Override
    public boolean isSetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "conformance" attribute
     */
    @Override
    public void setConformance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum conformance) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(conformance);
        }
    }

    /**
     * Sets (as xml) the "conformance" attribute
     */
    @Override
    public void xsetConformance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass conformance) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(conformance);
        }
    }

    /**
     * Unsets the "conformance" attribute
     */
    @Override
    public void unsetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

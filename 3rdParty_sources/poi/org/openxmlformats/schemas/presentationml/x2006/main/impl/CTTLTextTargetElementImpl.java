/*
 * XML Type:  CT_TLTextTargetElement
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTextTargetElement(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTextTargetElementImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement {
    private static final long serialVersionUID = 1L;

    public CTTLTextTargetElementImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "charRg"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "pRg"),
    };


    /**
     * Gets the "charRg" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange getCharRg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "charRg" element
     */
    @Override
    public boolean isSetCharRg() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "charRg" element
     */
    @Override
    public void setCharRg(org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange charRg) {
        generatedSetterHelperImpl(charRg, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "charRg" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange addNewCharRg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "charRg" element
     */
    @Override
    public void unsetCharRg() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "pRg" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange getPRg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pRg" element
     */
    @Override
    public boolean isSetPRg() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "pRg" element
     */
    @Override
    public void setPRg(org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange pRg) {
        generatedSetterHelperImpl(pRg, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pRg" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange addNewPRg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "pRg" element
     */
    @Override
    public void unsetPRg() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

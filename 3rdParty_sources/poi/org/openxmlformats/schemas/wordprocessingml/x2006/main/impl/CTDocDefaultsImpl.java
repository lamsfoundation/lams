/*
 * XML Type:  CT_DocDefaults
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DocDefaults(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTDocDefaultsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults {
    private static final long serialVersionUID = 1L;

    public CTDocDefaultsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rPrDefault"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pPrDefault"),
    };


    /**
     * Gets the "rPrDefault" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault getRPrDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rPrDefault" element
     */
    @Override
    public boolean isSetRPrDefault() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rPrDefault" element
     */
    @Override
    public void setRPrDefault(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault rPrDefault) {
        generatedSetterHelperImpl(rPrDefault, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rPrDefault" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault addNewRPrDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rPrDefault" element
     */
    @Override
    public void unsetRPrDefault() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "pPrDefault" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault getPPrDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pPrDefault" element
     */
    @Override
    public boolean isSetPPrDefault() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "pPrDefault" element
     */
    @Override
    public void setPPrDefault(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault pPrDefault) {
        generatedSetterHelperImpl(pPrDefault, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pPrDefault" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault addNewPPrDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "pPrDefault" element
     */
    @Override
    public void unsetPPrDefault() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

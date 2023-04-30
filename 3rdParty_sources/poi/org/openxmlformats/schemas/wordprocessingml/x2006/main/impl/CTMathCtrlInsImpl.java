/*
 * XML Type:  CT_MathCtrlIns
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlIns
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MathCtrlIns(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTMathCtrlInsImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlIns {
    private static final long serialVersionUID = 1L;

    public CTMathCtrlInsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "del"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rPr"),
    };


    /**
     * Gets the "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange getDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "del" element
     */
    @Override
    public boolean isSetDel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "del" element
     */
    @Override
    public void setDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange del) {
        generatedSetterHelperImpl(del, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange addNewDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "del" element
     */
    @Override
    public void unsetDel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr getRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rPr" element
     */
    @Override
    public boolean isSetRPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "rPr" element
     */
    @Override
    public void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr rPr) {
        generatedSetterHelperImpl(rPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr addNewRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "rPr" element
     */
    @Override
    public void unsetRPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

/*
 * XML Type:  CT_TrPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TrPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTrPrImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrPrBaseImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr {
    private static final long serialVersionUID = 1L;

    public CTTrPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ins"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "del"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "trPrChange"),
    };


    /**
     * Gets the "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ins" element
     */
    @Override
    public boolean isSetIns() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "ins" element
     */
    @Override
    public void setIns(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange ins) {
        generatedSetterHelperImpl(ins, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "ins" element
     */
    @Override
    public void unsetIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "del" element
     */
    @Override
    public void setDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange del) {
        generatedSetterHelperImpl(del, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "del" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewDel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "trPrChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange getTrPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "trPrChange" element
     */
    @Override
    public boolean isSetTrPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "trPrChange" element
     */
    @Override
    public void setTrPrChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange trPrChange) {
        generatedSetterHelperImpl(trPrChange, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "trPrChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange addNewTrPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPrChange)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "trPrChange" element
     */
    @Override
    public void unsetTrPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }
}

/*
 * XML Type:  CT_RubyPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RubyPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTRubyPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr {
    private static final long serialVersionUID = 1L;

    public CTRubyPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rubyAlign"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hps"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hpsRaise"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hpsBaseText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lid"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dirty"),
    };


    /**
     * Gets the "rubyAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign getRubyAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "rubyAlign" element
     */
    @Override
    public void setRubyAlign(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign rubyAlign) {
        generatedSetterHelperImpl(rubyAlign, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rubyAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign addNewRubyAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "hps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getHps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "hps" element
     */
    @Override
    public void setHps(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure hps) {
        generatedSetterHelperImpl(hps, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewHps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "hpsRaise" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getHpsRaise() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "hpsRaise" element
     */
    @Override
    public void setHpsRaise(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure hpsRaise) {
        generatedSetterHelperImpl(hpsRaise, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hpsRaise" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewHpsRaise() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "hpsBaseText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getHpsBaseText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "hpsBaseText" element
     */
    @Override
    public void setHpsBaseText(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure hpsBaseText) {
        generatedSetterHelperImpl(hpsBaseText, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hpsBaseText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewHpsBaseText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Gets the "lid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang getLid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "lid" element
     */
    @Override
    public void setLid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang lid) {
        generatedSetterHelperImpl(lid, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang addNewLid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Gets the "dirty" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDirty() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dirty" element
     */
    @Override
    public boolean isSetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "dirty" element
     */
    @Override
    public void setDirty(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff dirty) {
        generatedSetterHelperImpl(dirty, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dirty" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDirty() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "dirty" element
     */
    @Override
    public void unsetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }
}

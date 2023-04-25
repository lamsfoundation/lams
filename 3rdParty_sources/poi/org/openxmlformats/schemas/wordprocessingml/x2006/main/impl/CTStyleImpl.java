/*
 * XML Type:  CT_Style
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Style(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle {
    private static final long serialVersionUID = 1L;

    public CTStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "aliases"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "basedOn"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "next"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "link"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autoRedefine"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hidden"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "uiPriority"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "semiHidden"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "unhideWhenUsed"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "qFormat"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "locked"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "personal"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "personalCompose"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "personalReply"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsid"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "trPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tcPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblStylePr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "type"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "styleId"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "default"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "customStyle"),
    };


    /**
     * Gets the "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "name" element
     */
    @Override
    public boolean isSetName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "name" element
     */
    @Override
    public void setName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString name) {
        generatedSetterHelperImpl(name, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "name" element
     */
    @Override
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "aliases" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getAliases() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "aliases" element
     */
    @Override
    public boolean isSetAliases() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "aliases" element
     */
    @Override
    public void setAliases(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString aliases) {
        generatedSetterHelperImpl(aliases, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "aliases" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewAliases() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "aliases" element
     */
    @Override
    public void unsetAliases() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "basedOn" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getBasedOn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "basedOn" element
     */
    @Override
    public boolean isSetBasedOn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "basedOn" element
     */
    @Override
    public void setBasedOn(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString basedOn) {
        generatedSetterHelperImpl(basedOn, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "basedOn" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewBasedOn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "basedOn" element
     */
    @Override
    public void unsetBasedOn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "next" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getNext() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "next" element
     */
    @Override
    public boolean isSetNext() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "next" element
     */
    @Override
    public void setNext(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString next) {
        generatedSetterHelperImpl(next, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "next" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewNext() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "next" element
     */
    @Override
    public void unsetNext() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "link" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "link" element
     */
    @Override
    public boolean isSetLink() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "link" element
     */
    @Override
    public void setLink(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString link) {
        generatedSetterHelperImpl(link, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "link" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "link" element
     */
    @Override
    public void unsetLink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "autoRedefine" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAutoRedefine() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoRedefine" element
     */
    @Override
    public boolean isSetAutoRedefine() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "autoRedefine" element
     */
    @Override
    public void setAutoRedefine(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff autoRedefine) {
        generatedSetterHelperImpl(autoRedefine, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoRedefine" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAutoRedefine() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "autoRedefine" element
     */
    @Override
    public void unsetAutoRedefine() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "hidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hidden" element
     */
    @Override
    public boolean isSetHidden() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "hidden" element
     */
    @Override
    public void setHidden(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff hidden) {
        generatedSetterHelperImpl(hidden, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "hidden" element
     */
    @Override
    public void unsetHidden() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "uiPriority" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getUiPriority() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "uiPriority" element
     */
    @Override
    public boolean isSetUiPriority() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "uiPriority" element
     */
    @Override
    public void setUiPriority(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber uiPriority) {
        generatedSetterHelperImpl(uiPriority, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "uiPriority" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewUiPriority() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "uiPriority" element
     */
    @Override
    public void unsetUiPriority() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "semiHidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSemiHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "semiHidden" element
     */
    @Override
    public boolean isSetSemiHidden() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "semiHidden" element
     */
    @Override
    public void setSemiHidden(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff semiHidden) {
        generatedSetterHelperImpl(semiHidden, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "semiHidden" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSemiHidden() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "semiHidden" element
     */
    @Override
    public void unsetSemiHidden() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "unhideWhenUsed" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUnhideWhenUsed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "unhideWhenUsed" element
     */
    @Override
    public boolean isSetUnhideWhenUsed() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "unhideWhenUsed" element
     */
    @Override
    public void setUnhideWhenUsed(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff unhideWhenUsed) {
        generatedSetterHelperImpl(unhideWhenUsed, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "unhideWhenUsed" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUnhideWhenUsed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "unhideWhenUsed" element
     */
    @Override
    public void unsetUnhideWhenUsed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "qFormat" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getQFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "qFormat" element
     */
    @Override
    public boolean isSetQFormat() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "qFormat" element
     */
    @Override
    public void setQFormat(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff qFormat) {
        generatedSetterHelperImpl(qFormat, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "qFormat" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewQFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "qFormat" element
     */
    @Override
    public void unsetQFormat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "locked" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getLocked() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "locked" element
     */
    @Override
    public boolean isSetLocked() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "locked" element
     */
    @Override
    public void setLocked(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff locked) {
        generatedSetterHelperImpl(locked, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "locked" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewLocked() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "locked" element
     */
    @Override
    public void unsetLocked() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "personal" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPersonal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "personal" element
     */
    @Override
    public boolean isSetPersonal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "personal" element
     */
    @Override
    public void setPersonal(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff personal) {
        generatedSetterHelperImpl(personal, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "personal" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPersonal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "personal" element
     */
    @Override
    public void unsetPersonal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "personalCompose" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPersonalCompose() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "personalCompose" element
     */
    @Override
    public boolean isSetPersonalCompose() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "personalCompose" element
     */
    @Override
    public void setPersonalCompose(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff personalCompose) {
        generatedSetterHelperImpl(personalCompose, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "personalCompose" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPersonalCompose() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "personalCompose" element
     */
    @Override
    public void unsetPersonalCompose() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "personalReply" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPersonalReply() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "personalReply" element
     */
    @Override
    public boolean isSetPersonalReply() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "personalReply" element
     */
    @Override
    public void setPersonalReply(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff personalReply) {
        generatedSetterHelperImpl(personalReply, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "personalReply" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPersonalReply() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "personalReply" element
     */
    @Override
    public void unsetPersonalReply() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "rsid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getRsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rsid" element
     */
    @Override
    public boolean isSetRsid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "rsid" element
     */
    @Override
    public void setRsid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber rsid) {
        generatedSetterHelperImpl(rsid, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rsid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewRsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "rsid" element
     */
    @Override
    public void unsetRsid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral getPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pPr" element
     */
    @Override
    public boolean isSetPPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "pPr" element
     */
    @Override
    public void setPPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral pPr) {
        generatedSetterHelperImpl(pPr, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral addNewPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Unsets the "pPr" element
     */
    @Override
    public void unsetPPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr)get_store().find_element_user(PROPERTY_QNAME[17], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "rPr" element
     */
    @Override
    public void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr rPr) {
        generatedSetterHelperImpl(rPr, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr addNewRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr)get_store().add_element_user(PROPERTY_QNAME[17]);
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
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "tblPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase getTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblPr" element
     */
    @Override
    public boolean isSetTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "tblPr" element
     */
    @Override
    public void setTblPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase tblPr) {
        generatedSetterHelperImpl(tblPr, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase addNewTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Unsets the "tblPr" element
     */
    @Override
    public void unsetTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "trPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr getTrPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "trPr" element
     */
    @Override
    public boolean isSetTrPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "trPr" element
     */
    @Override
    public void setTrPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr trPr) {
        generatedSetterHelperImpl(trPr, PROPERTY_QNAME[19], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "trPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr addNewTrPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Unsets the "trPr" element
     */
    @Override
    public void unsetTrPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "tcPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr getTcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcPr" element
     */
    @Override
    public boolean isSetTcPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "tcPr" element
     */
    @Override
    public void setTcPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr tcPr) {
        generatedSetterHelperImpl(tcPr, PROPERTY_QNAME[20], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr addNewTcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Unsets the "tcPr" element
     */
    @Override
    public void unsetTcPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets a List of "tblStylePr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr> getTblStylePrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTblStylePrArray,
                this::setTblStylePrArray,
                this::insertNewTblStylePr,
                this::removeTblStylePr,
                this::sizeOfTblStylePrArray
            );
        }
    }

    /**
     * Gets array of all "tblStylePr" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr[] getTblStylePrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[21], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr[0]);
    }

    /**
     * Gets ith "tblStylePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr getTblStylePrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tblStylePr" element
     */
    @Override
    public int sizeOfTblStylePrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets array of all "tblStylePr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTblStylePrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr[] tblStylePrArray) {
        check_orphaned();
        arraySetterHelper(tblStylePrArray, PROPERTY_QNAME[21]);
    }

    /**
     * Sets ith "tblStylePr" element
     */
    @Override
    public void setTblStylePrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr tblStylePr) {
        generatedSetterHelperImpl(tblStylePr, PROPERTY_QNAME[21], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tblStylePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr insertNewTblStylePr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr)get_store().insert_element_user(PROPERTY_QNAME[21], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tblStylePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr addNewTblStylePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Removes the ith "tblStylePr" element
     */
    @Override
    public void removeTblStylePr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], i);
        }
    }

    /**
     * Gets the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "type" attribute
     */
    @Override
    public boolean isSetType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType.Enum type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setEnumValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType type) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(type);
        }
    }

    /**
     * Unsets the "type" attribute
     */
    @Override
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "styleId" attribute
     */
    @Override
    public java.lang.String getStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "styleId" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * True if has "styleId" attribute
     */
    @Override
    public boolean isSetStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "styleId" attribute
     */
    @Override
    public void setStyleId(java.lang.String styleId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setStringValue(styleId);
        }
    }

    /**
     * Sets (as xml) the "styleId" attribute
     */
    @Override
    public void xsetStyleId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString styleId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(styleId);
        }
    }

    /**
     * Unsets the "styleId" attribute
     */
    @Override
    public void unsetStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "default" attribute
     */
    @Override
    public java.lang.Object getDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "default" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetDefault() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * True if has "default" attribute
     */
    @Override
    public boolean isSetDefault() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "default" attribute
     */
    @Override
    public void setDefault(java.lang.Object xdefault) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setObjectValue(xdefault);
        }
    }

    /**
     * Sets (as xml) the "default" attribute
     */
    @Override
    public void xsetDefault(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xdefault) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(xdefault);
        }
    }

    /**
     * Unsets the "default" attribute
     */
    @Override
    public void unsetDefault() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "customStyle" attribute
     */
    @Override
    public java.lang.Object getCustomStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "customStyle" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetCustomStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "customStyle" attribute
     */
    @Override
    public boolean isSetCustomStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "customStyle" attribute
     */
    @Override
    public void setCustomStyle(java.lang.Object customStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setObjectValue(customStyle);
        }
    }

    /**
     * Sets (as xml) the "customStyle" attribute
     */
    @Override
    public void xsetCustomStyle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff customStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(customStyle);
        }
    }

    /**
     * Unsets the "customStyle" attribute
     */
    @Override
    public void unsetCustomStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }
}

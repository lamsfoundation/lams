/*
 * XML Type:  CT_SdtDate
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDate
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SdtDate(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTSdtDateImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDate {
    private static final long serialVersionUID = 1L;

    public CTSdtDateImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dateFormat"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lid"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "storeMappedDataAs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "calendar"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fullDate"),
    };


    /**
     * Gets the "dateFormat" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getDateFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dateFormat" element
     */
    @Override
    public boolean isSetDateFormat() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "dateFormat" element
     */
    @Override
    public void setDateFormat(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString dateFormat) {
        generatedSetterHelperImpl(dateFormat, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dateFormat" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewDateFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "dateFormat" element
     */
    @Override
    public void unsetDateFormat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lid" element
     */
    @Override
    public boolean isSetLid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "lid" element
     */
    @Override
    public void setLid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang lid) {
        generatedSetterHelperImpl(lid, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang addNewLid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "lid" element
     */
    @Override
    public void unsetLid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "storeMappedDataAs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDateMappingType getStoreMappedDataAs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDateMappingType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDateMappingType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "storeMappedDataAs" element
     */
    @Override
    public boolean isSetStoreMappedDataAs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "storeMappedDataAs" element
     */
    @Override
    public void setStoreMappedDataAs(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDateMappingType storeMappedDataAs) {
        generatedSetterHelperImpl(storeMappedDataAs, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "storeMappedDataAs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDateMappingType addNewStoreMappedDataAs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDateMappingType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDateMappingType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "storeMappedDataAs" element
     */
    @Override
    public void unsetStoreMappedDataAs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "calendar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCalendarType getCalendar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCalendarType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCalendarType)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "calendar" element
     */
    @Override
    public boolean isSetCalendar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "calendar" element
     */
    @Override
    public void setCalendar(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCalendarType calendar) {
        generatedSetterHelperImpl(calendar, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "calendar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCalendarType addNewCalendar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCalendarType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCalendarType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "calendar" element
     */
    @Override
    public void unsetCalendar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "fullDate" attribute
     */
    @Override
    public java.util.Calendar getFullDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * Gets (as xml) the "fullDate" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime xgetFullDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "fullDate" attribute
     */
    @Override
    public boolean isSetFullDate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "fullDate" attribute
     */
    @Override
    public void setFullDate(java.util.Calendar fullDate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setCalendarValue(fullDate);
        }
    }

    /**
     * Sets (as xml) the "fullDate" attribute
     */
    @Override
    public void xsetFullDate(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime fullDate) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(fullDate);
        }
    }

    /**
     * Unsets the "fullDate" attribute
     */
    @Override
    public void unsetFullDate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

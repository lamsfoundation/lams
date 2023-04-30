/*
 * XML Type:  CT_OdsoFieldMapData
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_OdsoFieldMapData(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTOdsoFieldMapDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData {
    private static final long serialVersionUID = 1L;

    public CTOdsoFieldMapDataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "type"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "name"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "mappedName"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "column"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lid"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dynamicAddress"),
    };


    /**
     * Gets the "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeOdsoFMDFieldType getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeOdsoFMDFieldType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeOdsoFMDFieldType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "type" element
     */
    @Override
    public boolean isSetType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "type" element
     */
    @Override
    public void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeOdsoFMDFieldType type) {
        generatedSetterHelperImpl(type, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeOdsoFMDFieldType addNewType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeOdsoFMDFieldType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeOdsoFMDFieldType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "type" element
     */
    @Override
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "name" element
     */
    @Override
    public void setName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString name) {
        generatedSetterHelperImpl(name, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "name" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "mappedName" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getMappedName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mappedName" element
     */
    @Override
    public boolean isSetMappedName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "mappedName" element
     */
    @Override
    public void setMappedName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString mappedName) {
        generatedSetterHelperImpl(mappedName, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mappedName" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewMappedName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "mappedName" element
     */
    @Override
    public void unsetMappedName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "column" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getColumn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "column" element
     */
    @Override
    public boolean isSetColumn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "column" element
     */
    @Override
    public void setColumn(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber column) {
        generatedSetterHelperImpl(column, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "column" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewColumn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "column" element
     */
    @Override
    public void unsetColumn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
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
     * True if has "lid" element
     */
    @Override
    public boolean isSetLid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
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
     * Unsets the "lid" element
     */
    @Override
    public void unsetLid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "dynamicAddress" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDynamicAddress() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dynamicAddress" element
     */
    @Override
    public boolean isSetDynamicAddress() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "dynamicAddress" element
     */
    @Override
    public void setDynamicAddress(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff dynamicAddress) {
        generatedSetterHelperImpl(dynamicAddress, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dynamicAddress" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDynamicAddress() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "dynamicAddress" element
     */
    @Override
    public void unsetDynamicAddress() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }
}

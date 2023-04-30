/*
 * XML Type:  CT_NameOrCorporateType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NameOrCorporateType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public class CTNameOrCorporateTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType {
    private static final long serialVersionUID = 1L;

    public CTNameOrCorporateTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "NameList"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Corporate"),
    };


    /**
     * Gets the "NameList" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType getNameList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "NameList" element
     */
    @Override
    public boolean isSetNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "NameList" element
     */
    @Override
    public void setNameList(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType nameList) {
        generatedSetterHelperImpl(nameList, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "NameList" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType addNewNameList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameListType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "NameList" element
     */
    @Override
    public void unsetNameList() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "Corporate" element
     */
    @Override
    public java.lang.String getCorporate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Corporate" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCorporate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "Corporate" element
     */
    @Override
    public boolean isSetCorporate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "Corporate" element
     */
    @Override
    public void setCorporate(java.lang.String corporate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(corporate);
        }
    }

    /**
     * Sets (as xml) the "Corporate" element
     */
    @Override
    public void xsetCorporate(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString corporate) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(corporate);
        }
    }

    /**
     * Unsets the "Corporate" element
     */
    @Override
    public void unsetCorporate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

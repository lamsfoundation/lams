/*
 * XML Type:  CT_MailMerge
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MailMerge(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTMailMergeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge {
    private static final long serialVersionUID = 1L;

    public CTMailMergeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "mainDocumentType"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "linkToQuery"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dataType"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "connectString"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "query"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dataSource"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "headerSource"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotSuppressBlankLines"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "destination"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "addressFieldName"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "mailSubject"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "mailAsAttachment"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "viewMergedData"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "activeRecord"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "checkErrors"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "odso"),
    };


    /**
     * Gets the "mainDocumentType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDocType getMainDocumentType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDocType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDocType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "mainDocumentType" element
     */
    @Override
    public void setMainDocumentType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDocType mainDocumentType) {
        generatedSetterHelperImpl(mainDocumentType, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mainDocumentType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDocType addNewMainDocumentType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDocType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDocType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "linkToQuery" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getLinkToQuery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "linkToQuery" element
     */
    @Override
    public boolean isSetLinkToQuery() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "linkToQuery" element
     */
    @Override
    public void setLinkToQuery(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff linkToQuery) {
        generatedSetterHelperImpl(linkToQuery, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "linkToQuery" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewLinkToQuery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "linkToQuery" element
     */
    @Override
    public void unsetLinkToQuery() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "dataType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDataType getDataType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDataType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDataType)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "dataType" element
     */
    @Override
    public void setDataType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDataType dataType) {
        generatedSetterHelperImpl(dataType, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dataType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDataType addNewDataType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDataType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDataType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "connectString" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getConnectString() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "connectString" element
     */
    @Override
    public boolean isSetConnectString() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "connectString" element
     */
    @Override
    public void setConnectString(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString connectString) {
        generatedSetterHelperImpl(connectString, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "connectString" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewConnectString() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "connectString" element
     */
    @Override
    public void unsetConnectString() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "query" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getQuery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "query" element
     */
    @Override
    public boolean isSetQuery() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "query" element
     */
    @Override
    public void setQuery(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString query) {
        generatedSetterHelperImpl(query, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "query" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewQuery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "query" element
     */
    @Override
    public void unsetQuery() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "dataSource" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getDataSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dataSource" element
     */
    @Override
    public boolean isSetDataSource() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "dataSource" element
     */
    @Override
    public void setDataSource(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel dataSource) {
        generatedSetterHelperImpl(dataSource, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dataSource" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewDataSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "dataSource" element
     */
    @Override
    public void unsetDataSource() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "headerSource" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getHeaderSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "headerSource" element
     */
    @Override
    public boolean isSetHeaderSource() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "headerSource" element
     */
    @Override
    public void setHeaderSource(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel headerSource) {
        generatedSetterHelperImpl(headerSource, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headerSource" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewHeaderSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "headerSource" element
     */
    @Override
    public void unsetHeaderSource() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "doNotSuppressBlankLines" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotSuppressBlankLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotSuppressBlankLines" element
     */
    @Override
    public boolean isSetDoNotSuppressBlankLines() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "doNotSuppressBlankLines" element
     */
    @Override
    public void setDoNotSuppressBlankLines(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotSuppressBlankLines) {
        generatedSetterHelperImpl(doNotSuppressBlankLines, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotSuppressBlankLines" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotSuppressBlankLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "doNotSuppressBlankLines" element
     */
    @Override
    public void unsetDoNotSuppressBlankLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "destination" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDest getDestination() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDest target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDest)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "destination" element
     */
    @Override
    public boolean isSetDestination() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "destination" element
     */
    @Override
    public void setDestination(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDest destination) {
        generatedSetterHelperImpl(destination, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "destination" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDest addNewDestination() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDest target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeDest)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "destination" element
     */
    @Override
    public void unsetDestination() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "addressFieldName" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getAddressFieldName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "addressFieldName" element
     */
    @Override
    public boolean isSetAddressFieldName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "addressFieldName" element
     */
    @Override
    public void setAddressFieldName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addressFieldName) {
        generatedSetterHelperImpl(addressFieldName, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "addressFieldName" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewAddressFieldName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "addressFieldName" element
     */
    @Override
    public void unsetAddressFieldName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "mailSubject" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getMailSubject() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mailSubject" element
     */
    @Override
    public boolean isSetMailSubject() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "mailSubject" element
     */
    @Override
    public void setMailSubject(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString mailSubject) {
        generatedSetterHelperImpl(mailSubject, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mailSubject" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewMailSubject() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "mailSubject" element
     */
    @Override
    public void unsetMailSubject() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "mailAsAttachment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getMailAsAttachment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mailAsAttachment" element
     */
    @Override
    public boolean isSetMailAsAttachment() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "mailAsAttachment" element
     */
    @Override
    public void setMailAsAttachment(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff mailAsAttachment) {
        generatedSetterHelperImpl(mailAsAttachment, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mailAsAttachment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewMailAsAttachment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "mailAsAttachment" element
     */
    @Override
    public void unsetMailAsAttachment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "viewMergedData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getViewMergedData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "viewMergedData" element
     */
    @Override
    public boolean isSetViewMergedData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "viewMergedData" element
     */
    @Override
    public void setViewMergedData(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff viewMergedData) {
        generatedSetterHelperImpl(viewMergedData, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "viewMergedData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewViewMergedData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "viewMergedData" element
     */
    @Override
    public void unsetViewMergedData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "activeRecord" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getActiveRecord() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "activeRecord" element
     */
    @Override
    public boolean isSetActiveRecord() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "activeRecord" element
     */
    @Override
    public void setActiveRecord(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber activeRecord) {
        generatedSetterHelperImpl(activeRecord, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "activeRecord" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewActiveRecord() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "activeRecord" element
     */
    @Override
    public void unsetActiveRecord() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "checkErrors" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getCheckErrors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "checkErrors" element
     */
    @Override
    public boolean isSetCheckErrors() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "checkErrors" element
     */
    @Override
    public void setCheckErrors(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber checkErrors) {
        generatedSetterHelperImpl(checkErrors, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "checkErrors" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewCheckErrors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "checkErrors" element
     */
    @Override
    public void unsetCheckErrors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "odso" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso getOdso() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "odso" element
     */
    @Override
    public boolean isSetOdso() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "odso" element
     */
    @Override
    public void setOdso(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso odso) {
        generatedSetterHelperImpl(odso, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "odso" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso addNewOdso() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "odso" element
     */
    @Override
    public void unsetOdso() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }
}

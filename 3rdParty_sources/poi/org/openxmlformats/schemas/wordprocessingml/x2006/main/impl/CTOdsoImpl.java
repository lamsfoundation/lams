/*
 * XML Type:  CT_Odso
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Odso(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTOdsoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdso {
    private static final long serialVersionUID = 1L;

    public CTOdsoImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "udl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "table"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "src"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "colDelim"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "type"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fHdr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "fieldMapData"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "recipientData"),
    };


    /**
     * Gets the "udl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getUdl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "udl" element
     */
    @Override
    public boolean isSetUdl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "udl" element
     */
    @Override
    public void setUdl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString udl) {
        generatedSetterHelperImpl(udl, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "udl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewUdl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "udl" element
     */
    @Override
    public void unsetUdl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "table" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "table" element
     */
    @Override
    public boolean isSetTable() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "table" element
     */
    @Override
    public void setTable(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString table) {
        generatedSetterHelperImpl(table, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "table" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "table" element
     */
    @Override
    public void unsetTable() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "src" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getSrc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "src" element
     */
    @Override
    public boolean isSetSrc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "src" element
     */
    @Override
    public void setSrc(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel src) {
        generatedSetterHelperImpl(src, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "src" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewSrc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "src" element
     */
    @Override
    public void unsetSrc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "colDelim" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getColDelim() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "colDelim" element
     */
    @Override
    public boolean isSetColDelim() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "colDelim" element
     */
    @Override
    public void setColDelim(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber colDelim) {
        generatedSetterHelperImpl(colDelim, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colDelim" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewColDelim() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "colDelim" element
     */
    @Override
    public void unsetColDelim() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeSourceType getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeSourceType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeSourceType)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "type" element
     */
    @Override
    public void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeSourceType type) {
        generatedSetterHelperImpl(type, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "type" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeSourceType addNewType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeSourceType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMergeSourceType)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "fHdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getFHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fHdr" element
     */
    @Override
    public boolean isSetFHdr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "fHdr" element
     */
    @Override
    public void setFHdr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff fHdr) {
        generatedSetterHelperImpl(fHdr, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fHdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewFHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "fHdr" element
     */
    @Override
    public void unsetFHdr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets a List of "fieldMapData" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData> getFieldMapDataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFieldMapDataArray,
                this::setFieldMapDataArray,
                this::insertNewFieldMapData,
                this::removeFieldMapData,
                this::sizeOfFieldMapDataArray
            );
        }
    }

    /**
     * Gets array of all "fieldMapData" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData[] getFieldMapDataArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData[0]);
    }

    /**
     * Gets ith "fieldMapData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData getFieldMapDataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "fieldMapData" element
     */
    @Override
    public int sizeOfFieldMapDataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "fieldMapData" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFieldMapDataArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData[] fieldMapDataArray) {
        check_orphaned();
        arraySetterHelper(fieldMapDataArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "fieldMapData" element
     */
    @Override
    public void setFieldMapDataArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData fieldMapData) {
        generatedSetterHelperImpl(fieldMapData, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fieldMapData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData insertNewFieldMapData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "fieldMapData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData addNewFieldMapData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOdsoFieldMapData)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "fieldMapData" element
     */
    @Override
    public void removeFieldMapData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "recipientData" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel> getRecipientDataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getRecipientDataArray,
                this::setRecipientDataArray,
                this::insertNewRecipientData,
                this::removeRecipientData,
                this::sizeOfRecipientDataArray
            );
        }
    }

    /**
     * Gets array of all "recipientData" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[] getRecipientDataArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[0]);
    }

    /**
     * Gets ith "recipientData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getRecipientDataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "recipientData" element
     */
    @Override
    public int sizeOfRecipientDataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "recipientData" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setRecipientDataArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel[] recipientDataArray) {
        check_orphaned();
        arraySetterHelper(recipientDataArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "recipientData" element
     */
    @Override
    public void setRecipientDataArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel recipientData) {
        generatedSetterHelperImpl(recipientData, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "recipientData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel insertNewRecipientData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "recipientData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewRecipientData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "recipientData" element
     */
    @Override
    public void removeRecipientData(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }
}

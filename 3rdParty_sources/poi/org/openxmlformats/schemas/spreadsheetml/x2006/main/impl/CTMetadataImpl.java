/*
 * XML Type:  CT_Metadata
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadata
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Metadata(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTMetadataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadata {
    private static final long serialVersionUID = 1L;

    public CTMetadataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "metadataTypes"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "metadataStrings"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "mdxMetadata"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "futureMetadata"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellMetadata"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "valueMetadata"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
    };


    /**
     * Gets the "metadataTypes" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes getMetadataTypes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "metadataTypes" element
     */
    @Override
    public boolean isSetMetadataTypes() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "metadataTypes" element
     */
    @Override
    public void setMetadataTypes(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes metadataTypes) {
        generatedSetterHelperImpl(metadataTypes, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "metadataTypes" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes addNewMetadataTypes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataTypes)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "metadataTypes" element
     */
    @Override
    public void unsetMetadataTypes() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "metadataStrings" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings getMetadataStrings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "metadataStrings" element
     */
    @Override
    public boolean isSetMetadataStrings() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "metadataStrings" element
     */
    @Override
    public void setMetadataStrings(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings metadataStrings) {
        generatedSetterHelperImpl(metadataStrings, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "metadataStrings" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings addNewMetadataStrings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStrings)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "metadataStrings" element
     */
    @Override
    public void unsetMetadataStrings() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "mdxMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata getMdxMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mdxMetadata" element
     */
    @Override
    public boolean isSetMdxMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "mdxMetadata" element
     */
    @Override
    public void setMdxMetadata(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata mdxMetadata) {
        generatedSetterHelperImpl(mdxMetadata, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mdxMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata addNewMdxMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "mdxMetadata" element
     */
    @Override
    public void unsetMdxMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets a List of "futureMetadata" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata> getFutureMetadataList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFutureMetadataArray,
                this::setFutureMetadataArray,
                this::insertNewFutureMetadata,
                this::removeFutureMetadata,
                this::sizeOfFutureMetadataArray
            );
        }
    }

    /**
     * Gets array of all "futureMetadata" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata[] getFutureMetadataArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata[0]);
    }

    /**
     * Gets ith "futureMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata getFutureMetadataArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "futureMetadata" element
     */
    @Override
    public int sizeOfFutureMetadataArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "futureMetadata" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFutureMetadataArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata[] futureMetadataArray) {
        check_orphaned();
        arraySetterHelper(futureMetadataArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "futureMetadata" element
     */
    @Override
    public void setFutureMetadataArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata futureMetadata) {
        generatedSetterHelperImpl(futureMetadata, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "futureMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata insertNewFutureMetadata(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "futureMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata addNewFutureMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFutureMetadata)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "futureMetadata" element
     */
    @Override
    public void removeFutureMetadata(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets the "cellMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks getCellMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cellMetadata" element
     */
    @Override
    public boolean isSetCellMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "cellMetadata" element
     */
    @Override
    public void setCellMetadata(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks cellMetadata) {
        generatedSetterHelperImpl(cellMetadata, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cellMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks addNewCellMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "cellMetadata" element
     */
    @Override
    public void unsetCellMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "valueMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks getValueMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "valueMetadata" element
     */
    @Override
    public boolean isSetValueMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "valueMetadata" element
     */
    @Override
    public void setValueMetadata(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks valueMetadata) {
        generatedSetterHelperImpl(valueMetadata, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "valueMetadata" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks addNewValueMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataBlocks)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "valueMetadata" element
     */
    @Override
    public void unsetValueMetadata() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }
}

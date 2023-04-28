/*
 * XML Type:  CT_SmartTags
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SmartTags(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTSmartTagsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags {
    private static final long serialVersionUID = 1L;

    public CTSmartTagsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellSmartTags"),
    };


    /**
     * Gets a List of "cellSmartTags" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags> getCellSmartTagsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCellSmartTagsArray,
                this::setCellSmartTagsArray,
                this::insertNewCellSmartTags,
                this::removeCellSmartTags,
                this::sizeOfCellSmartTagsArray
            );
        }
    }

    /**
     * Gets array of all "cellSmartTags" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags[] getCellSmartTagsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags[0]);
    }

    /**
     * Gets ith "cellSmartTags" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags getCellSmartTagsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cellSmartTags" element
     */
    @Override
    public int sizeOfCellSmartTagsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cellSmartTags" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCellSmartTagsArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags[] cellSmartTagsArray) {
        check_orphaned();
        arraySetterHelper(cellSmartTagsArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cellSmartTags" element
     */
    @Override
    public void setCellSmartTagsArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags cellSmartTags) {
        generatedSetterHelperImpl(cellSmartTags, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cellSmartTags" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags insertNewCellSmartTags(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cellSmartTags" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags addNewCellSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cellSmartTags" element
     */
    @Override
    public void removeCellSmartTags(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

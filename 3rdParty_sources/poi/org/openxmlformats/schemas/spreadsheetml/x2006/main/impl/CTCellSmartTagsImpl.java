/*
 * XML Type:  CT_CellSmartTags
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CellSmartTags(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCellSmartTagsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTags {
    private static final long serialVersionUID = 1L;

    public CTCellSmartTagsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellSmartTag"),
        new QName("", "r"),
    };


    /**
     * Gets a List of "cellSmartTag" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag> getCellSmartTagList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCellSmartTagArray,
                this::setCellSmartTagArray,
                this::insertNewCellSmartTag,
                this::removeCellSmartTag,
                this::sizeOfCellSmartTagArray
            );
        }
    }

    /**
     * Gets array of all "cellSmartTag" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag[] getCellSmartTagArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag[0]);
    }

    /**
     * Gets ith "cellSmartTag" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag getCellSmartTagArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cellSmartTag" element
     */
    @Override
    public int sizeOfCellSmartTagArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cellSmartTag" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCellSmartTagArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag[] cellSmartTagArray) {
        check_orphaned();
        arraySetterHelper(cellSmartTagArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cellSmartTag" element
     */
    @Override
    public void setCellSmartTagArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag cellSmartTag) {
        generatedSetterHelperImpl(cellSmartTag, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cellSmartTag" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag insertNewCellSmartTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cellSmartTag" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag addNewCellSmartTag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cellSmartTag" element
     */
    @Override
    public void removeCellSmartTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "r" attribute
     */
    @Override
    public java.lang.String getR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "r" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef xgetR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "r" attribute
     */
    @Override
    public void setR(java.lang.String r) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(r);
        }
    }

    /**
     * Sets (as xml) the "r" attribute
     */
    @Override
    public void xsetR(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef r) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellRef)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(r);
        }
    }
}

/*
 * XML Type:  CT_CellSmartTag
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CellSmartTag(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCellSmartTagImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTag {
    private static final long serialVersionUID = 1L;

    public CTCellSmartTagImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellSmartTagPr"),
        new QName("", "type"),
        new QName("", "deleted"),
        new QName("", "xmlBased"),
    };


    /**
     * Gets a List of "cellSmartTagPr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr> getCellSmartTagPrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCellSmartTagPrArray,
                this::setCellSmartTagPrArray,
                this::insertNewCellSmartTagPr,
                this::removeCellSmartTagPr,
                this::sizeOfCellSmartTagPrArray
            );
        }
    }

    /**
     * Gets array of all "cellSmartTagPr" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr[] getCellSmartTagPrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr[0]);
    }

    /**
     * Gets ith "cellSmartTagPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr getCellSmartTagPrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cellSmartTagPr" element
     */
    @Override
    public int sizeOfCellSmartTagPrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cellSmartTagPr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCellSmartTagPrArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr[] cellSmartTagPrArray) {
        check_orphaned();
        arraySetterHelper(cellSmartTagPrArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cellSmartTagPr" element
     */
    @Override
    public void setCellSmartTagPrArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr cellSmartTagPr) {
        generatedSetterHelperImpl(cellSmartTagPr, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cellSmartTagPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr insertNewCellSmartTagPr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cellSmartTagPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr addNewCellSmartTagPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellSmartTagPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cellSmartTagPr" element
     */
    @Override
    public void removeCellSmartTagPr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "type" attribute
     */
    @Override
    public long getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(long type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.apache.xmlbeans.XmlUnsignedInt type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(type);
        }
    }

    /**
     * Gets the "deleted" attribute
     */
    @Override
    public boolean getDeleted() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "deleted" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDeleted() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "deleted" attribute
     */
    @Override
    public boolean isSetDeleted() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "deleted" attribute
     */
    @Override
    public void setDeleted(boolean deleted) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(deleted);
        }
    }

    /**
     * Sets (as xml) the "deleted" attribute
     */
    @Override
    public void xsetDeleted(org.apache.xmlbeans.XmlBoolean deleted) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(deleted);
        }
    }

    /**
     * Unsets the "deleted" attribute
     */
    @Override
    public void unsetDeleted() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "xmlBased" attribute
     */
    @Override
    public boolean getXmlBased() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "xmlBased" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetXmlBased() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "xmlBased" attribute
     */
    @Override
    public boolean isSetXmlBased() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "xmlBased" attribute
     */
    @Override
    public void setXmlBased(boolean xmlBased) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(xmlBased);
        }
    }

    /**
     * Sets (as xml) the "xmlBased" attribute
     */
    @Override
    public void xsetXmlBased(org.apache.xmlbeans.XmlBoolean xmlBased) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(xmlBased);
        }
    }

    /**
     * Unsets the "xmlBased" attribute
     */
    @Override
    public void unsetXmlBased() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}

/*
 * XML Type:  CT_Filters
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Filters(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTFiltersImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters {
    private static final long serialVersionUID = 1L;

    public CTFiltersImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "filter"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dateGroupItem"),
        new QName("", "blank"),
        new QName("", "calendarType"),
    };


    /**
     * Gets a List of "filter" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter> getFilterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFilterArray,
                this::setFilterArray,
                this::insertNewFilter,
                this::removeFilter,
                this::sizeOfFilterArray
            );
        }
    }

    /**
     * Gets array of all "filter" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter[] getFilterArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter[0]);
    }

    /**
     * Gets ith "filter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter getFilterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "filter" element
     */
    @Override
    public int sizeOfFilterArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "filter" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFilterArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter[] filterArray) {
        check_orphaned();
        arraySetterHelper(filterArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "filter" element
     */
    @Override
    public void setFilterArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter filter) {
        generatedSetterHelperImpl(filter, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "filter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter insertNewFilter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "filter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter addNewFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "filter" element
     */
    @Override
    public void removeFilter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "dateGroupItem" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem> getDateGroupItemList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDateGroupItemArray,
                this::setDateGroupItemArray,
                this::insertNewDateGroupItem,
                this::removeDateGroupItem,
                this::sizeOfDateGroupItemArray
            );
        }
    }

    /**
     * Gets array of all "dateGroupItem" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem[] getDateGroupItemArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem[0]);
    }

    /**
     * Gets ith "dateGroupItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem getDateGroupItemArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "dateGroupItem" element
     */
    @Override
    public int sizeOfDateGroupItemArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "dateGroupItem" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDateGroupItemArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem[] dateGroupItemArray) {
        check_orphaned();
        arraySetterHelper(dateGroupItemArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "dateGroupItem" element
     */
    @Override
    public void setDateGroupItemArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem dateGroupItem) {
        generatedSetterHelperImpl(dateGroupItem, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dateGroupItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem insertNewDateGroupItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "dateGroupItem" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem addNewDateGroupItem() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDateGroupItem)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "dateGroupItem" element
     */
    @Override
    public void removeDateGroupItem(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "blank" attribute
     */
    @Override
    public boolean getBlank() {
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
     * Gets (as xml) the "blank" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetBlank() {
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
     * True if has "blank" attribute
     */
    @Override
    public boolean isSetBlank() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "blank" attribute
     */
    @Override
    public void setBlank(boolean blank) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(blank);
        }
    }

    /**
     * Sets (as xml) the "blank" attribute
     */
    @Override
    public void xsetBlank(org.apache.xmlbeans.XmlBoolean blank) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(blank);
        }
    }

    /**
     * Unsets the "blank" attribute
     */
    @Override
    public void unsetBlank() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "calendarType" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType.Enum getCalendarType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "calendarType" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType xgetCalendarType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "calendarType" attribute
     */
    @Override
    public boolean isSetCalendarType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "calendarType" attribute
     */
    @Override
    public void setCalendarType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType.Enum calendarType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(calendarType);
        }
    }

    /**
     * Sets (as xml) the "calendarType" attribute
     */
    @Override
    public void xsetCalendarType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType calendarType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(calendarType);
        }
    }

    /**
     * Unsets the "calendarType" attribute
     */
    @Override
    public void unsetCalendarType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}

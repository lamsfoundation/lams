/*
 * XML Type:  CT_CustomFilters
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CustomFilters(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCustomFiltersImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilters {
    private static final long serialVersionUID = 1L;

    public CTCustomFiltersImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "customFilter"),
        new QName("", "and"),
    };


    /**
     * Gets a List of "customFilter" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter> getCustomFilterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustomFilterArray,
                this::setCustomFilterArray,
                this::insertNewCustomFilter,
                this::removeCustomFilter,
                this::sizeOfCustomFilterArray
            );
        }
    }

    /**
     * Gets array of all "customFilter" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter[] getCustomFilterArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter[0]);
    }

    /**
     * Gets ith "customFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter getCustomFilterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "customFilter" element
     */
    @Override
    public int sizeOfCustomFilterArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "customFilter" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustomFilterArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter[] customFilterArray) {
        check_orphaned();
        arraySetterHelper(customFilterArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "customFilter" element
     */
    @Override
    public void setCustomFilterArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter customFilter) {
        generatedSetterHelperImpl(customFilter, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "customFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter insertNewCustomFilter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "customFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter addNewCustomFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomFilter)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "customFilter" element
     */
    @Override
    public void removeCustomFilter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "and" attribute
     */
    @Override
    public boolean getAnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "and" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "and" attribute
     */
    @Override
    public boolean isSetAnd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "and" attribute
     */
    @Override
    public void setAnd(boolean and) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setBooleanValue(and);
        }
    }

    /**
     * Sets (as xml) the "and" attribute
     */
    @Override
    public void xsetAnd(org.apache.xmlbeans.XmlBoolean and) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(and);
        }
    }

    /**
     * Unsets the "and" attribute
     */
    @Override
    public void unsetAnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

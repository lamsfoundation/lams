/*
 * XML Type:  CT_DdeValues
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValues
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DdeValues(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTDdeValuesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValues {
    private static final long serialVersionUID = 1L;

    public CTDdeValuesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "value"),
        new QName("", "rows"),
        new QName("", "cols"),
    };


    /**
     * Gets a List of "value" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue> getValueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getValueArray,
                this::setValueArray,
                this::insertNewValue,
                this::removeValue,
                this::sizeOfValueArray
            );
        }
    }

    /**
     * Gets array of all "value" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue[] getValueArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue[0]);
    }

    /**
     * Gets ith "value" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue getValueArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "value" element
     */
    @Override
    public int sizeOfValueArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "value" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setValueArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue[] valueArray) {
        check_orphaned();
        arraySetterHelper(valueArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "value" element
     */
    @Override
    public void setValueArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue value) {
        generatedSetterHelperImpl(value, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "value" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue insertNewValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "value" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue addNewValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "value" element
     */
    @Override
    public void removeValue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "rows" attribute
     */
    @Override
    public long getRows() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "rows" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetRows() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "rows" attribute
     */
    @Override
    public boolean isSetRows() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "rows" attribute
     */
    @Override
    public void setRows(long rows) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(rows);
        }
    }

    /**
     * Sets (as xml) the "rows" attribute
     */
    @Override
    public void xsetRows(org.apache.xmlbeans.XmlUnsignedInt rows) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(rows);
        }
    }

    /**
     * Unsets the "rows" attribute
     */
    @Override
    public void unsetRows() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "cols" attribute
     */
    @Override
    public long getCols() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "cols" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCols() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "cols" attribute
     */
    @Override
    public boolean isSetCols() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "cols" attribute
     */
    @Override
    public void setCols(long cols) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setLongValue(cols);
        }
    }

    /**
     * Sets (as xml) the "cols" attribute
     */
    @Override
    public void xsetCols(org.apache.xmlbeans.XmlUnsignedInt cols) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(cols);
        }
    }

    /**
     * Unsets the "cols" attribute
     */
    @Override
    public void unsetCols() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

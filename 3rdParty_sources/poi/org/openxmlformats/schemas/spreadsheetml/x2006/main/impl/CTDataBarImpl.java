/*
 * XML Type:  CT_DataBar
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBar
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DataBar(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTDataBarImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBar {
    private static final long serialVersionUID = 1L;

    public CTDataBarImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cfvo"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "color"),
        new QName("", "minLength"),
        new QName("", "maxLength"),
        new QName("", "showValue"),
    };


    /**
     * Gets a List of "cfvo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo> getCfvoList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCfvoArray,
                this::setCfvoArray,
                this::insertNewCfvo,
                this::removeCfvo,
                this::sizeOfCfvoArray
            );
        }
    }

    /**
     * Gets array of all "cfvo" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo[] getCfvoArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo[0]);
    }

    /**
     * Gets ith "cfvo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo getCfvoArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cfvo" element
     */
    @Override
    public int sizeOfCfvoArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cfvo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCfvoArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo[] cfvoArray) {
        check_orphaned();
        arraySetterHelper(cfvoArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cfvo" element
     */
    @Override
    public void setCfvoArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo cfvo) {
        generatedSetterHelperImpl(cfvo, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cfvo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo insertNewCfvo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cfvo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo addNewCfvo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cfvo" element
     */
    @Override
    public void removeCfvo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "color" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor getColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "color" element
     */
    @Override
    public void setColor(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor color) {
        generatedSetterHelperImpl(color, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "color" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor addNewColor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "minLength" attribute
     */
    @Override
    public long getMinLength() {
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
     * Gets (as xml) the "minLength" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetMinLength() {
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
     * True if has "minLength" attribute
     */
    @Override
    public boolean isSetMinLength() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "minLength" attribute
     */
    @Override
    public void setMinLength(long minLength) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setLongValue(minLength);
        }
    }

    /**
     * Sets (as xml) the "minLength" attribute
     */
    @Override
    public void xsetMinLength(org.apache.xmlbeans.XmlUnsignedInt minLength) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(minLength);
        }
    }

    /**
     * Unsets the "minLength" attribute
     */
    @Override
    public void unsetMinLength() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "maxLength" attribute
     */
    @Override
    public long getMaxLength() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "maxLength" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetMaxLength() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "maxLength" attribute
     */
    @Override
    public boolean isSetMaxLength() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "maxLength" attribute
     */
    @Override
    public void setMaxLength(long maxLength) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setLongValue(maxLength);
        }
    }

    /**
     * Sets (as xml) the "maxLength" attribute
     */
    @Override
    public void xsetMaxLength(org.apache.xmlbeans.XmlUnsignedInt maxLength) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(maxLength);
        }
    }

    /**
     * Unsets the "maxLength" attribute
     */
    @Override
    public void unsetMaxLength() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "showValue" attribute
     */
    @Override
    public boolean getShowValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showValue" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "showValue" attribute
     */
    @Override
    public boolean isSetShowValue() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "showValue" attribute
     */
    @Override
    public void setShowValue(boolean showValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(showValue);
        }
    }

    /**
     * Sets (as xml) the "showValue" attribute
     */
    @Override
    public void xsetShowValue(org.apache.xmlbeans.XmlBoolean showValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(showValue);
        }
    }

    /**
     * Unsets the "showValue" attribute
     */
    @Override
    public void unsetShowValue() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

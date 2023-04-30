/*
 * XML Type:  CT_IconSet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_IconSet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTIconSetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet {
    private static final long serialVersionUID = 1L;

    public CTIconSetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cfvo"),
        new QName("", "iconSet"),
        new QName("", "showValue"),
        new QName("", "percent"),
        new QName("", "reverse"),
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
     * Gets the "iconSet" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum getIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "iconSet" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType xgetIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "iconSet" attribute
     */
    @Override
    public boolean isSetIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "iconSet" attribute
     */
    @Override
    public void setIconSet(org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.Enum iconSet) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(iconSet);
        }
    }

    /**
     * Sets (as xml) the "iconSet" attribute
     */
    @Override
    public void xsetIconSet(org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType iconSet) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(iconSet);
        }
    }

    /**
     * Unsets the "iconSet" attribute
     */
    @Override
    public void unsetIconSet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "percent" attribute
     */
    @Override
    public boolean getPercent() {
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
     * Gets (as xml) the "percent" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPercent() {
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
     * True if has "percent" attribute
     */
    @Override
    public boolean isSetPercent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "percent" attribute
     */
    @Override
    public void setPercent(boolean percent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(percent);
        }
    }

    /**
     * Sets (as xml) the "percent" attribute
     */
    @Override
    public void xsetPercent(org.apache.xmlbeans.XmlBoolean percent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(percent);
        }
    }

    /**
     * Unsets the "percent" attribute
     */
    @Override
    public void unsetPercent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "reverse" attribute
     */
    @Override
    public boolean getReverse() {
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
     * Gets (as xml) the "reverse" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetReverse() {
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
     * True if has "reverse" attribute
     */
    @Override
    public boolean isSetReverse() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "reverse" attribute
     */
    @Override
    public void setReverse(boolean reverse) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(reverse);
        }
    }

    /**
     * Sets (as xml) the "reverse" attribute
     */
    @Override
    public void xsetReverse(org.apache.xmlbeans.XmlBoolean reverse) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(reverse);
        }
    }

    /**
     * Unsets the "reverse" attribute
     */
    @Override
    public void unsetReverse() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

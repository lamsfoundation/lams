/*
 * XML Type:  CT_MdxSet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MdxSet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTMdxSetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxSet {
    private static final long serialVersionUID = 1L;

    public CTMdxSetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "n"),
        new QName("", "ns"),
        new QName("", "c"),
        new QName("", "o"),
    };


    /**
     * Gets a List of "n" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex> getNList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getNArray,
                this::setNArray,
                this::insertNewN,
                this::removeN,
                this::sizeOfNArray
            );
        }
    }

    /**
     * Gets array of all "n" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex[] getNArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex[0]);
    }

    /**
     * Gets ith "n" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex getNArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "n" element
     */
    @Override
    public int sizeOfNArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "n" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex[] nArray) {
        check_orphaned();
        arraySetterHelper(nArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "n" element
     */
    @Override
    public void setNArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex n) {
        generatedSetterHelperImpl(n, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "n" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex insertNewN(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "n" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex addNewN() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMetadataStringIndex)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "n" element
     */
    @Override
    public void removeN(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "ns" attribute
     */
    @Override
    public long getNs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "ns" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetNs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "ns" attribute
     */
    @Override
    public void setNs(long ns) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(ns);
        }
    }

    /**
     * Sets (as xml) the "ns" attribute
     */
    @Override
    public void xsetNs(org.apache.xmlbeans.XmlUnsignedInt ns) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(ns);
        }
    }

    /**
     * Gets the "c" attribute
     */
    @Override
    public long getC() {
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
     * Gets (as xml) the "c" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetC() {
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
     * True if has "c" attribute
     */
    @Override
    public boolean isSetC() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "c" attribute
     */
    @Override
    public void setC(long c) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setLongValue(c);
        }
    }

    /**
     * Sets (as xml) the "c" attribute
     */
    @Override
    public void xsetC(org.apache.xmlbeans.XmlUnsignedInt c) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(c);
        }
    }

    /**
     * Unsets the "c" attribute
     */
    @Override
    public void unsetC() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "o" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder.Enum getO() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "o" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder xgetO() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "o" attribute
     */
    @Override
    public boolean isSetO() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "o" attribute
     */
    @Override
    public void setO(org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder.Enum o) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(o);
        }
    }

    /**
     * Sets (as xml) the "o" attribute
     */
    @Override
    public void xsetO(org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder o) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(o);
        }
    }

    /**
     * Unsets the "o" attribute
     */
    @Override
    public void unsetO() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}

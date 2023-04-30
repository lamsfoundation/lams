/*
 * XML Type:  CT_Tables
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTables
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Tables(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTTablesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTables {
    private static final long serialVersionUID = 1L;

    public CTTablesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "m"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "s"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "x"),
        new QName("", "count"),
    };


    /**
     * Gets a List of "m" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing> getMList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMArray,
                this::setMArray,
                this::insertNewM,
                this::removeM,
                this::sizeOfMArray
            );
        }
    }

    /**
     * Gets array of all "m" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing[] getMArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing[0]);
    }

    /**
     * Gets ith "m" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing getMArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "m" element
     */
    @Override
    public int sizeOfMArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "m" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing[] mArray) {
        check_orphaned();
        arraySetterHelper(mArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "m" element
     */
    @Override
    public void setMArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing m) {
        generatedSetterHelperImpl(m, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "m" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing insertNewM(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "m" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing addNewM() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableMissing)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "m" element
     */
    @Override
    public void removeM(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "s" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement> getSList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSArray,
                this::setSArray,
                this::insertNewS,
                this::removeS,
                this::sizeOfSArray
            );
        }
    }

    /**
     * Gets array of all "s" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement[] getSArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement[0]);
    }

    /**
     * Gets ith "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement getSArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "s" element
     */
    @Override
    public int sizeOfSArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "s" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement[] sArray) {
        check_orphaned();
        arraySetterHelper(sArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "s" element
     */
    @Override
    public void setSArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement s) {
        generatedSetterHelperImpl(s, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement insertNewS(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement addNewS() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXStringElement)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "s" element
     */
    @Override
    public void removeS(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "x" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex> getXList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getXArray,
                this::setXArray,
                this::insertNewX,
                this::removeX,
                this::sizeOfXArray
            );
        }
    }

    /**
     * Gets array of all "x" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex[] getXArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex[0]);
    }

    /**
     * Gets ith "x" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex getXArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "x" element
     */
    @Override
    public int sizeOfXArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "x" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setXArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex[] xArray) {
        check_orphaned();
        arraySetterHelper(xArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "x" element
     */
    @Override
    public void setXArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex x) {
        generatedSetterHelperImpl(x, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "x" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex insertNewX(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "x" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex addNewX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndex)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "x" element
     */
    @Override
    public void removeX(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets the "count" attribute
     */
    @Override
    public long getCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "count" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "count" attribute
     */
    @Override
    public boolean isSetCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "count" attribute
     */
    @Override
    public void setCount(long count) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setLongValue(count);
        }
    }

    /**
     * Sets (as xml) the "count" attribute
     */
    @Override
    public void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(count);
        }
    }

    /**
     * Unsets the "count" attribute
     */
    @Override
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}

/*
 * XML Type:  CT_PCDSDTCEntries
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PCDSDTCEntries(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTPCDSDTCEntriesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries {
    private static final long serialVersionUID = 1L;

    public CTPCDSDTCEntriesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "m"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "n"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "e"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "s"),
        new QName("", "count"),
    };


    /**
     * Gets a List of "m" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing> getMList() {
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
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing[] getMArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing[0]);
    }

    /**
     * Gets ith "m" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing getMArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing)get_store().find_element_user(PROPERTY_QNAME[0], i);
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
    public void setMArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing[] mArray) {
        check_orphaned();
        arraySetterHelper(mArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "m" element
     */
    @Override
    public void setMArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing m) {
        generatedSetterHelperImpl(m, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "m" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing insertNewM(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "m" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing addNewM() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMissing)get_store().add_element_user(PROPERTY_QNAME[0]);
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
     * Gets a List of "n" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber> getNList() {
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
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber[] getNArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber[0]);
    }

    /**
     * Gets ith "n" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber getNArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber)get_store().find_element_user(PROPERTY_QNAME[1], i);
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
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "n" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setNArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber[] nArray) {
        check_orphaned();
        arraySetterHelper(nArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "n" element
     */
    @Override
    public void setNArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber n) {
        generatedSetterHelperImpl(n, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "n" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber insertNewN(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "n" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber addNewN() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumber)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "e" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError> getEList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEArray,
                this::setEArray,
                this::insertNewE,
                this::removeE,
                this::sizeOfEArray
            );
        }
    }

    /**
     * Gets array of all "e" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError[] getEArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError[0]);
    }

    /**
     * Gets ith "e" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError getEArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "e" element
     */
    @Override
    public int sizeOfEArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "e" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError[] eArray) {
        check_orphaned();
        arraySetterHelper(eArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "e" element
     */
    @Override
    public void setEArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError e) {
        generatedSetterHelperImpl(e, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "e" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError insertNewE(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "e" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError addNewE() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTError)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "e" element
     */
    @Override
    public void removeE(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "s" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString> getSList() {
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
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString[] getSArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString[0]);
    }

    /**
     * Gets ith "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString getSArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[3], i);
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
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "s" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString[] sArray) {
        check_orphaned();
        arraySetterHelper(sArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "s" element
     */
    @Override
    public void setSArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString s) {
        generatedSetterHelperImpl(s, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString insertNewS(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "s" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString addNewS() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
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
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
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
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[4]);
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
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

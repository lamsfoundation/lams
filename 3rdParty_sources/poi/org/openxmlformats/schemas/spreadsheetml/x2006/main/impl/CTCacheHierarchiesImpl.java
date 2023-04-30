/*
 * XML Type:  CT_CacheHierarchies
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CacheHierarchies(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCacheHierarchiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies {
    private static final long serialVersionUID = 1L;

    public CTCacheHierarchiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cacheHierarchy"),
        new QName("", "count"),
    };


    /**
     * Gets a List of "cacheHierarchy" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy> getCacheHierarchyList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCacheHierarchyArray,
                this::setCacheHierarchyArray,
                this::insertNewCacheHierarchy,
                this::removeCacheHierarchy,
                this::sizeOfCacheHierarchyArray
            );
        }
    }

    /**
     * Gets array of all "cacheHierarchy" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy[] getCacheHierarchyArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy[0]);
    }

    /**
     * Gets ith "cacheHierarchy" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy getCacheHierarchyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cacheHierarchy" element
     */
    @Override
    public int sizeOfCacheHierarchyArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cacheHierarchy" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCacheHierarchyArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy[] cacheHierarchyArray) {
        check_orphaned();
        arraySetterHelper(cacheHierarchyArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cacheHierarchy" element
     */
    @Override
    public void setCacheHierarchyArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy cacheHierarchy) {
        generatedSetterHelperImpl(cacheHierarchy, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cacheHierarchy" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy insertNewCacheHierarchy(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cacheHierarchy" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy addNewCacheHierarchy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cacheHierarchy" element
     */
    @Override
    public void removeCacheHierarchy(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

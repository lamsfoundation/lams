/*
 * XML Type:  CT_PivotCaches
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PivotCaches(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTPivotCachesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches {
    private static final long serialVersionUID = 1L;

    public CTPivotCachesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotCache"),
    };


    /**
     * Gets a List of "pivotCache" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache> getPivotCacheList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPivotCacheArray,
                this::setPivotCacheArray,
                this::insertNewPivotCache,
                this::removePivotCache,
                this::sizeOfPivotCacheArray
            );
        }
    }

    /**
     * Gets array of all "pivotCache" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache[] getPivotCacheArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache[0]);
    }

    /**
     * Gets ith "pivotCache" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache getPivotCacheArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "pivotCache" element
     */
    @Override
    public int sizeOfPivotCacheArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "pivotCache" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPivotCacheArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache[] pivotCacheArray) {
        check_orphaned();
        arraySetterHelper(pivotCacheArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "pivotCache" element
     */
    @Override
    public void setPivotCacheArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache pivotCache) {
        generatedSetterHelperImpl(pivotCache, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "pivotCache" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache insertNewPivotCache(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "pivotCache" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache addNewPivotCache() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCache)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "pivotCache" element
     */
    @Override
    public void removePivotCache(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

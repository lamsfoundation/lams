/*
 * XML Type:  CT_TupleCache
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TupleCache(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTTupleCacheImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache {
    private static final long serialVersionUID = 1L;

    public CTTupleCacheImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "entries"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sets"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "queryCache"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "serverFormats"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
    };


    /**
     * Gets the "entries" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries getEntries() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "entries" element
     */
    @Override
    public boolean isSetEntries() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "entries" element
     */
    @Override
    public void setEntries(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries entries) {
        generatedSetterHelperImpl(entries, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "entries" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries addNewEntries() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDSDTCEntries)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "entries" element
     */
    @Override
    public void unsetEntries() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "sets" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets getSets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sets" element
     */
    @Override
    public boolean isSetSets() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "sets" element
     */
    @Override
    public void setSets(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets sets) {
        generatedSetterHelperImpl(sets, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sets" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets addNewSets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSets)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "sets" element
     */
    @Override
    public void unsetSets() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "queryCache" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache getQueryCache() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "queryCache" element
     */
    @Override
    public boolean isSetQueryCache() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "queryCache" element
     */
    @Override
    public void setQueryCache(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache queryCache) {
        generatedSetterHelperImpl(queryCache, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "queryCache" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache addNewQueryCache() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryCache)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "queryCache" element
     */
    @Override
    public void unsetQueryCache() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "serverFormats" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats getServerFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "serverFormats" element
     */
    @Override
    public boolean isSetServerFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "serverFormats" element
     */
    @Override
    public void setServerFormats(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats serverFormats) {
        generatedSetterHelperImpl(serverFormats, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "serverFormats" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats addNewServerFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "serverFormats" element
     */
    @Override
    public void unsetServerFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }
}

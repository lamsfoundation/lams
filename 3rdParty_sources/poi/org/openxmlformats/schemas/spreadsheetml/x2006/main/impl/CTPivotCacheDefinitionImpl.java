/*
 * XML Type:  CT_PivotCacheDefinition
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PivotCacheDefinition(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTPivotCacheDefinitionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition {
    private static final long serialVersionUID = 1L;

    public CTPivotCacheDefinitionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cacheSource"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cacheFields"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cacheHierarchies"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "kpis"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "tupleCache"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "calculatedItems"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "calculatedMembers"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dimensions"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "measureGroups"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "maps"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id"),
        new QName("", "invalid"),
        new QName("", "saveData"),
        new QName("", "refreshOnLoad"),
        new QName("", "optimizeMemory"),
        new QName("", "enableRefresh"),
        new QName("", "refreshedBy"),
        new QName("", "refreshedDate"),
        new QName("", "refreshedDateIso"),
        new QName("", "backgroundQuery"),
        new QName("", "missingItemsLimit"),
        new QName("", "createdVersion"),
        new QName("", "refreshedVersion"),
        new QName("", "minRefreshableVersion"),
        new QName("", "recordCount"),
        new QName("", "upgradeOnRefresh"),
        new QName("", "tupleCache"),
        new QName("", "supportSubquery"),
        new QName("", "supportAdvancedDrill"),
    };


    /**
     * Gets the "cacheSource" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource getCacheSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cacheSource" element
     */
    @Override
    public void setCacheSource(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource cacheSource) {
        generatedSetterHelperImpl(cacheSource, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cacheSource" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource addNewCacheSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheSource)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "cacheFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields getCacheFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cacheFields" element
     */
    @Override
    public void setCacheFields(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields cacheFields) {
        generatedSetterHelperImpl(cacheFields, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cacheFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields addNewCacheFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheFields)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "cacheHierarchies" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies getCacheHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cacheHierarchies" element
     */
    @Override
    public boolean isSetCacheHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "cacheHierarchies" element
     */
    @Override
    public void setCacheHierarchies(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies cacheHierarchies) {
        generatedSetterHelperImpl(cacheHierarchies, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cacheHierarchies" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies addNewCacheHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchies)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "cacheHierarchies" element
     */
    @Override
    public void unsetCacheHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "kpis" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs getKpis() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "kpis" element
     */
    @Override
    public boolean isSetKpis() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "kpis" element
     */
    @Override
    public void setKpis(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs kpis) {
        generatedSetterHelperImpl(kpis, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "kpis" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs addNewKpis() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPCDKPIs)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "kpis" element
     */
    @Override
    public void unsetKpis() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "tupleCache" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache getTupleCache() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tupleCache" element
     */
    @Override
    public boolean isSetTupleCache() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "tupleCache" element
     */
    @Override
    public void setTupleCache(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache tupleCache) {
        generatedSetterHelperImpl(tupleCache, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tupleCache" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache addNewTupleCache() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTupleCache)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "tupleCache" element
     */
    @Override
    public void unsetTupleCache() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "calculatedItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems getCalculatedItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "calculatedItems" element
     */
    @Override
    public boolean isSetCalculatedItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "calculatedItems" element
     */
    @Override
    public void setCalculatedItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems calculatedItems) {
        generatedSetterHelperImpl(calculatedItems, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "calculatedItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems addNewCalculatedItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedItems)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "calculatedItems" element
     */
    @Override
    public void unsetCalculatedItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "calculatedMembers" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers getCalculatedMembers() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "calculatedMembers" element
     */
    @Override
    public boolean isSetCalculatedMembers() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "calculatedMembers" element
     */
    @Override
    public void setCalculatedMembers(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers calculatedMembers) {
        generatedSetterHelperImpl(calculatedMembers, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "calculatedMembers" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers addNewCalculatedMembers() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalculatedMembers)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "calculatedMembers" element
     */
    @Override
    public void unsetCalculatedMembers() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "dimensions" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDimensions getDimensions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDimensions target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDimensions)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dimensions" element
     */
    @Override
    public boolean isSetDimensions() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "dimensions" element
     */
    @Override
    public void setDimensions(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDimensions dimensions) {
        generatedSetterHelperImpl(dimensions, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dimensions" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDimensions addNewDimensions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDimensions target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDimensions)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "dimensions" element
     */
    @Override
    public void unsetDimensions() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "measureGroups" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups getMeasureGroups() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "measureGroups" element
     */
    @Override
    public boolean isSetMeasureGroups() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "measureGroups" element
     */
    @Override
    public void setMeasureGroups(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups measureGroups) {
        generatedSetterHelperImpl(measureGroups, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "measureGroups" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups addNewMeasureGroups() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureGroups)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "measureGroups" element
     */
    @Override
    public void unsetMeasureGroups() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "maps" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps getMaps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "maps" element
     */
    @Override
    public boolean isSetMaps() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "maps" element
     */
    @Override
    public void setMaps(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps maps) {
        generatedSetterHelperImpl(maps, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "maps" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps addNewMaps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMeasureDimensionMaps)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "maps" element
     */
    @Override
    public void unsetMaps() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[10], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[10]);
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
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "id" attribute
     */
    @Override
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "id" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "id" attribute
     */
    @Override
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "id" attribute
     */
    @Override
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "id" attribute
     */
    @Override
    public void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "id" attribute
     */
    @Override
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "invalid" attribute
     */
    @Override
    public boolean getInvalid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[12]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "invalid" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetInvalid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[12]);
            }
            return target;
        }
    }

    /**
     * True if has "invalid" attribute
     */
    @Override
    public boolean isSetInvalid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "invalid" attribute
     */
    @Override
    public void setInvalid(boolean invalid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setBooleanValue(invalid);
        }
    }

    /**
     * Sets (as xml) the "invalid" attribute
     */
    @Override
    public void xsetInvalid(org.apache.xmlbeans.XmlBoolean invalid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(invalid);
        }
    }

    /**
     * Unsets the "invalid" attribute
     */
    @Override
    public void unsetInvalid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "saveData" attribute
     */
    @Override
    public boolean getSaveData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "saveData" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSaveData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return target;
        }
    }

    /**
     * True if has "saveData" attribute
     */
    @Override
    public boolean isSetSaveData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "saveData" attribute
     */
    @Override
    public void setSaveData(boolean saveData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setBooleanValue(saveData);
        }
    }

    /**
     * Sets (as xml) the "saveData" attribute
     */
    @Override
    public void xsetSaveData(org.apache.xmlbeans.XmlBoolean saveData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(saveData);
        }
    }

    /**
     * Unsets the "saveData" attribute
     */
    @Override
    public void unsetSaveData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "refreshOnLoad" attribute
     */
    @Override
    public boolean getRefreshOnLoad() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "refreshOnLoad" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRefreshOnLoad() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return target;
        }
    }

    /**
     * True if has "refreshOnLoad" attribute
     */
    @Override
    public boolean isSetRefreshOnLoad() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "refreshOnLoad" attribute
     */
    @Override
    public void setRefreshOnLoad(boolean refreshOnLoad) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setBooleanValue(refreshOnLoad);
        }
    }

    /**
     * Sets (as xml) the "refreshOnLoad" attribute
     */
    @Override
    public void xsetRefreshOnLoad(org.apache.xmlbeans.XmlBoolean refreshOnLoad) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(refreshOnLoad);
        }
    }

    /**
     * Unsets the "refreshOnLoad" attribute
     */
    @Override
    public void unsetRefreshOnLoad() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "optimizeMemory" attribute
     */
    @Override
    public boolean getOptimizeMemory() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "optimizeMemory" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetOptimizeMemory() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return target;
        }
    }

    /**
     * True if has "optimizeMemory" attribute
     */
    @Override
    public boolean isSetOptimizeMemory() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "optimizeMemory" attribute
     */
    @Override
    public void setOptimizeMemory(boolean optimizeMemory) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setBooleanValue(optimizeMemory);
        }
    }

    /**
     * Sets (as xml) the "optimizeMemory" attribute
     */
    @Override
    public void xsetOptimizeMemory(org.apache.xmlbeans.XmlBoolean optimizeMemory) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(optimizeMemory);
        }
    }

    /**
     * Unsets the "optimizeMemory" attribute
     */
    @Override
    public void unsetOptimizeMemory() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "enableRefresh" attribute
     */
    @Override
    public boolean getEnableRefresh() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "enableRefresh" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEnableRefresh() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return target;
        }
    }

    /**
     * True if has "enableRefresh" attribute
     */
    @Override
    public boolean isSetEnableRefresh() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "enableRefresh" attribute
     */
    @Override
    public void setEnableRefresh(boolean enableRefresh) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setBooleanValue(enableRefresh);
        }
    }

    /**
     * Sets (as xml) the "enableRefresh" attribute
     */
    @Override
    public void xsetEnableRefresh(org.apache.xmlbeans.XmlBoolean enableRefresh) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(enableRefresh);
        }
    }

    /**
     * Unsets the "enableRefresh" attribute
     */
    @Override
    public void unsetEnableRefresh() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "refreshedBy" attribute
     */
    @Override
    public java.lang.String getRefreshedBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "refreshedBy" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetRefreshedBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * True if has "refreshedBy" attribute
     */
    @Override
    public boolean isSetRefreshedBy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "refreshedBy" attribute
     */
    @Override
    public void setRefreshedBy(java.lang.String refreshedBy) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setStringValue(refreshedBy);
        }
    }

    /**
     * Sets (as xml) the "refreshedBy" attribute
     */
    @Override
    public void xsetRefreshedBy(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring refreshedBy) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(refreshedBy);
        }
    }

    /**
     * Unsets the "refreshedBy" attribute
     */
    @Override
    public void unsetRefreshedBy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "refreshedDate" attribute
     */
    @Override
    public double getRefreshedDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return (target == null) ? 0.0 : target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "refreshedDate" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlDouble xgetRefreshedDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * True if has "refreshedDate" attribute
     */
    @Override
    public boolean isSetRefreshedDate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "refreshedDate" attribute
     */
    @Override
    public void setRefreshedDate(double refreshedDate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setDoubleValue(refreshedDate);
        }
    }

    /**
     * Sets (as xml) the "refreshedDate" attribute
     */
    @Override
    public void xsetRefreshedDate(org.apache.xmlbeans.XmlDouble refreshedDate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(refreshedDate);
        }
    }

    /**
     * Unsets the "refreshedDate" attribute
     */
    @Override
    public void unsetRefreshedDate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "refreshedDateIso" attribute
     */
    @Override
    public java.util.Calendar getRefreshedDateIso() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * Gets (as xml) the "refreshedDateIso" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlDateTime xgetRefreshedDateIso() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * True if has "refreshedDateIso" attribute
     */
    @Override
    public boolean isSetRefreshedDateIso() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "refreshedDateIso" attribute
     */
    @Override
    public void setRefreshedDateIso(java.util.Calendar refreshedDateIso) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setCalendarValue(refreshedDateIso);
        }
    }

    /**
     * Sets (as xml) the "refreshedDateIso" attribute
     */
    @Override
    public void xsetRefreshedDateIso(org.apache.xmlbeans.XmlDateTime refreshedDateIso) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(refreshedDateIso);
        }
    }

    /**
     * Unsets the "refreshedDateIso" attribute
     */
    @Override
    public void unsetRefreshedDateIso() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "backgroundQuery" attribute
     */
    @Override
    public boolean getBackgroundQuery() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "backgroundQuery" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetBackgroundQuery() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return target;
        }
    }

    /**
     * True if has "backgroundQuery" attribute
     */
    @Override
    public boolean isSetBackgroundQuery() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "backgroundQuery" attribute
     */
    @Override
    public void setBackgroundQuery(boolean backgroundQuery) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setBooleanValue(backgroundQuery);
        }
    }

    /**
     * Sets (as xml) the "backgroundQuery" attribute
     */
    @Override
    public void xsetBackgroundQuery(org.apache.xmlbeans.XmlBoolean backgroundQuery) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(backgroundQuery);
        }
    }

    /**
     * Unsets the "backgroundQuery" attribute
     */
    @Override
    public void unsetBackgroundQuery() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "missingItemsLimit" attribute
     */
    @Override
    public long getMissingItemsLimit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "missingItemsLimit" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetMissingItemsLimit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * True if has "missingItemsLimit" attribute
     */
    @Override
    public boolean isSetMissingItemsLimit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "missingItemsLimit" attribute
     */
    @Override
    public void setMissingItemsLimit(long missingItemsLimit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setLongValue(missingItemsLimit);
        }
    }

    /**
     * Sets (as xml) the "missingItemsLimit" attribute
     */
    @Override
    public void xsetMissingItemsLimit(org.apache.xmlbeans.XmlUnsignedInt missingItemsLimit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(missingItemsLimit);
        }
    }

    /**
     * Unsets the "missingItemsLimit" attribute
     */
    @Override
    public void unsetMissingItemsLimit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "createdVersion" attribute
     */
    @Override
    public short getCreatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[22]);
            }
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "createdVersion" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetCreatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_default_attribute_value(PROPERTY_QNAME[22]);
            }
            return target;
        }
    }

    /**
     * True if has "createdVersion" attribute
     */
    @Override
    public boolean isSetCreatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "createdVersion" attribute
     */
    @Override
    public void setCreatedVersion(short createdVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setShortValue(createdVersion);
        }
    }

    /**
     * Sets (as xml) the "createdVersion" attribute
     */
    @Override
    public void xsetCreatedVersion(org.apache.xmlbeans.XmlUnsignedByte createdVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(createdVersion);
        }
    }

    /**
     * Unsets the "createdVersion" attribute
     */
    @Override
    public void unsetCreatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "refreshedVersion" attribute
     */
    @Override
    public short getRefreshedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[23]);
            }
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "refreshedVersion" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetRefreshedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_default_attribute_value(PROPERTY_QNAME[23]);
            }
            return target;
        }
    }

    /**
     * True if has "refreshedVersion" attribute
     */
    @Override
    public boolean isSetRefreshedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "refreshedVersion" attribute
     */
    @Override
    public void setRefreshedVersion(short refreshedVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setShortValue(refreshedVersion);
        }
    }

    /**
     * Sets (as xml) the "refreshedVersion" attribute
     */
    @Override
    public void xsetRefreshedVersion(org.apache.xmlbeans.XmlUnsignedByte refreshedVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(refreshedVersion);
        }
    }

    /**
     * Unsets the "refreshedVersion" attribute
     */
    @Override
    public void unsetRefreshedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "minRefreshableVersion" attribute
     */
    @Override
    public short getMinRefreshableVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[24]);
            }
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "minRefreshableVersion" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetMinRefreshableVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_default_attribute_value(PROPERTY_QNAME[24]);
            }
            return target;
        }
    }

    /**
     * True if has "minRefreshableVersion" attribute
     */
    @Override
    public boolean isSetMinRefreshableVersion() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "minRefreshableVersion" attribute
     */
    @Override
    public void setMinRefreshableVersion(short minRefreshableVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setShortValue(minRefreshableVersion);
        }
    }

    /**
     * Sets (as xml) the "minRefreshableVersion" attribute
     */
    @Override
    public void xsetMinRefreshableVersion(org.apache.xmlbeans.XmlUnsignedByte minRefreshableVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(minRefreshableVersion);
        }
    }

    /**
     * Unsets the "minRefreshableVersion" attribute
     */
    @Override
    public void unsetMinRefreshableVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "recordCount" attribute
     */
    @Override
    public long getRecordCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "recordCount" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetRecordCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "recordCount" attribute
     */
    @Override
    public boolean isSetRecordCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "recordCount" attribute
     */
    @Override
    public void setRecordCount(long recordCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setLongValue(recordCount);
        }
    }

    /**
     * Sets (as xml) the "recordCount" attribute
     */
    @Override
    public void xsetRecordCount(org.apache.xmlbeans.XmlUnsignedInt recordCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(recordCount);
        }
    }

    /**
     * Unsets the "recordCount" attribute
     */
    @Override
    public void unsetRecordCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "upgradeOnRefresh" attribute
     */
    @Override
    public boolean getUpgradeOnRefresh() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[26]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "upgradeOnRefresh" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetUpgradeOnRefresh() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[26]);
            }
            return target;
        }
    }

    /**
     * True if has "upgradeOnRefresh" attribute
     */
    @Override
    public boolean isSetUpgradeOnRefresh() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "upgradeOnRefresh" attribute
     */
    @Override
    public void setUpgradeOnRefresh(boolean upgradeOnRefresh) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setBooleanValue(upgradeOnRefresh);
        }
    }

    /**
     * Sets (as xml) the "upgradeOnRefresh" attribute
     */
    @Override
    public void xsetUpgradeOnRefresh(org.apache.xmlbeans.XmlBoolean upgradeOnRefresh) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(upgradeOnRefresh);
        }
    }

    /**
     * Unsets the "upgradeOnRefresh" attribute
     */
    @Override
    public void unsetUpgradeOnRefresh() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Gets the "tupleCache" attribute
     */
    @Override
    public boolean getTupleCache2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[27]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "tupleCache" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetTupleCache2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[27]);
            }
            return target;
        }
    }

    /**
     * True if has "tupleCache" attribute
     */
    @Override
    public boolean isSetTupleCache2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[27]) != null;
        }
    }

    /**
     * Sets the "tupleCache" attribute
     */
    @Override
    public void setTupleCache2(boolean tupleCache2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.setBooleanValue(tupleCache2);
        }
    }

    /**
     * Sets (as xml) the "tupleCache" attribute
     */
    @Override
    public void xsetTupleCache2(org.apache.xmlbeans.XmlBoolean tupleCache2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.set(tupleCache2);
        }
    }

    /**
     * Unsets the "tupleCache" attribute
     */
    @Override
    public void unsetTupleCache2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Gets the "supportSubquery" attribute
     */
    @Override
    public boolean getSupportSubquery() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[28]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "supportSubquery" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSupportSubquery() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[28]);
            }
            return target;
        }
    }

    /**
     * True if has "supportSubquery" attribute
     */
    @Override
    public boolean isSetSupportSubquery() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[28]) != null;
        }
    }

    /**
     * Sets the "supportSubquery" attribute
     */
    @Override
    public void setSupportSubquery(boolean supportSubquery) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.setBooleanValue(supportSubquery);
        }
    }

    /**
     * Sets (as xml) the "supportSubquery" attribute
     */
    @Override
    public void xsetSupportSubquery(org.apache.xmlbeans.XmlBoolean supportSubquery) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.set(supportSubquery);
        }
    }

    /**
     * Unsets the "supportSubquery" attribute
     */
    @Override
    public void unsetSupportSubquery() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Gets the "supportAdvancedDrill" attribute
     */
    @Override
    public boolean getSupportAdvancedDrill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[29]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "supportAdvancedDrill" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSupportAdvancedDrill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[29]);
            }
            return target;
        }
    }

    /**
     * True if has "supportAdvancedDrill" attribute
     */
    @Override
    public boolean isSetSupportAdvancedDrill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[29]) != null;
        }
    }

    /**
     * Sets the "supportAdvancedDrill" attribute
     */
    @Override
    public void setSupportAdvancedDrill(boolean supportAdvancedDrill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.setBooleanValue(supportAdvancedDrill);
        }
    }

    /**
     * Sets (as xml) the "supportAdvancedDrill" attribute
     */
    @Override
    public void xsetSupportAdvancedDrill(org.apache.xmlbeans.XmlBoolean supportAdvancedDrill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.set(supportAdvancedDrill);
        }
    }

    /**
     * Unsets the "supportAdvancedDrill" attribute
     */
    @Override
    public void unsetSupportAdvancedDrill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[29]);
        }
    }
}

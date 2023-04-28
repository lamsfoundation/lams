/*
 * XML Type:  CT_pivotTableDefinition
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_pivotTableDefinition(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTPivotTableDefinitionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition {
    private static final long serialVersionUID = 1L;

    public CTPivotTableDefinitionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "location"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotFields"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rowFields"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rowItems"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "colFields"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "colItems"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pageFields"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dataFields"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "formats"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "conditionalFormats"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "chartFormats"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotHierarchies"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotTableStyleInfo"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "filters"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rowHierarchiesUsage"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "colHierarchiesUsage"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
        new QName("", "name"),
        new QName("", "cacheId"),
        new QName("", "dataOnRows"),
        new QName("", "dataPosition"),
        new QName("", "autoFormatId"),
        new QName("", "applyNumberFormats"),
        new QName("", "applyBorderFormats"),
        new QName("", "applyFontFormats"),
        new QName("", "applyPatternFormats"),
        new QName("", "applyAlignmentFormats"),
        new QName("", "applyWidthHeightFormats"),
        new QName("", "dataCaption"),
        new QName("", "grandTotalCaption"),
        new QName("", "errorCaption"),
        new QName("", "showError"),
        new QName("", "missingCaption"),
        new QName("", "showMissing"),
        new QName("", "pageStyle"),
        new QName("", "pivotTableStyle"),
        new QName("", "vacatedStyle"),
        new QName("", "tag"),
        new QName("", "updatedVersion"),
        new QName("", "minRefreshableVersion"),
        new QName("", "asteriskTotals"),
        new QName("", "showItems"),
        new QName("", "editData"),
        new QName("", "disableFieldList"),
        new QName("", "showCalcMbrs"),
        new QName("", "visualTotals"),
        new QName("", "showMultipleLabel"),
        new QName("", "showDataDropDown"),
        new QName("", "showDrill"),
        new QName("", "printDrill"),
        new QName("", "showMemberPropertyTips"),
        new QName("", "showDataTips"),
        new QName("", "enableWizard"),
        new QName("", "enableDrill"),
        new QName("", "enableFieldProperties"),
        new QName("", "preserveFormatting"),
        new QName("", "useAutoFormatting"),
        new QName("", "pageWrap"),
        new QName("", "pageOverThenDown"),
        new QName("", "subtotalHiddenItems"),
        new QName("", "rowGrandTotals"),
        new QName("", "colGrandTotals"),
        new QName("", "fieldPrintTitles"),
        new QName("", "itemPrintTitles"),
        new QName("", "mergeItem"),
        new QName("", "showDropZones"),
        new QName("", "createdVersion"),
        new QName("", "indent"),
        new QName("", "showEmptyRow"),
        new QName("", "showEmptyCol"),
        new QName("", "showHeaders"),
        new QName("", "compact"),
        new QName("", "outline"),
        new QName("", "outlineData"),
        new QName("", "compactData"),
        new QName("", "published"),
        new QName("", "gridDropZones"),
        new QName("", "immersive"),
        new QName("", "multipleFieldFilters"),
        new QName("", "chartFormat"),
        new QName("", "rowHeaderCaption"),
        new QName("", "colHeaderCaption"),
        new QName("", "fieldListSortAscending"),
        new QName("", "mdxSubqueries"),
        new QName("", "customListSort"),
    };


    /**
     * Gets the "location" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation getLocation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "location" element
     */
    @Override
    public void setLocation(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation location) {
        generatedSetterHelperImpl(location, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "location" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation addNewLocation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLocation)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "pivotFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields getPivotFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pivotFields" element
     */
    @Override
    public boolean isSetPivotFields() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "pivotFields" element
     */
    @Override
    public void setPivotFields(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields pivotFields) {
        generatedSetterHelperImpl(pivotFields, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields addNewPivotFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "pivotFields" element
     */
    @Override
    public void unsetPivotFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "rowFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields getRowFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rowFields" element
     */
    @Override
    public boolean isSetRowFields() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "rowFields" element
     */
    @Override
    public void setRowFields(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields rowFields) {
        generatedSetterHelperImpl(rowFields, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rowFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields addNewRowFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowFields)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "rowFields" element
     */
    @Override
    public void unsetRowFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "rowItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowItems getRowItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowItems)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rowItems" element
     */
    @Override
    public boolean isSetRowItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "rowItems" element
     */
    @Override
    public void setRowItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowItems rowItems) {
        generatedSetterHelperImpl(rowItems, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rowItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowItems addNewRowItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowItems)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "rowItems" element
     */
    @Override
    public void unsetRowItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "colFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields getColFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "colFields" element
     */
    @Override
    public boolean isSetColFields() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "colFields" element
     */
    @Override
    public void setColFields(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields colFields) {
        generatedSetterHelperImpl(colFields, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields addNewColFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "colFields" element
     */
    @Override
    public void unsetColFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "colItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems getColItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "colItems" element
     */
    @Override
    public boolean isSetColItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "colItems" element
     */
    @Override
    public void setColItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems colItems) {
        generatedSetterHelperImpl(colItems, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems addNewColItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColItems)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "colItems" element
     */
    @Override
    public void unsetColItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "pageFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields getPageFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pageFields" element
     */
    @Override
    public boolean isSetPageFields() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "pageFields" element
     */
    @Override
    public void setPageFields(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields pageFields) {
        generatedSetterHelperImpl(pageFields, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields addNewPageFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageFields)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "pageFields" element
     */
    @Override
    public void unsetPageFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "dataFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields getDataFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dataFields" element
     */
    @Override
    public boolean isSetDataFields() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "dataFields" element
     */
    @Override
    public void setDataFields(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields dataFields) {
        generatedSetterHelperImpl(dataFields, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dataFields" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields addNewDataFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataFields)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "dataFields" element
     */
    @Override
    public void unsetDataFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "formats" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats getFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "formats" element
     */
    @Override
    public boolean isSetFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "formats" element
     */
    @Override
    public void setFormats(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats formats) {
        generatedSetterHelperImpl(formats, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "formats" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats addNewFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFormats)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "formats" element
     */
    @Override
    public void unsetFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "conditionalFormats" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats getConditionalFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "conditionalFormats" element
     */
    @Override
    public boolean isSetConditionalFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "conditionalFormats" element
     */
    @Override
    public void setConditionalFormats(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats conditionalFormats) {
        generatedSetterHelperImpl(conditionalFormats, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "conditionalFormats" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats addNewConditionalFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "conditionalFormats" element
     */
    @Override
    public void unsetConditionalFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "chartFormats" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartFormats getChartFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartFormats target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartFormats)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "chartFormats" element
     */
    @Override
    public boolean isSetChartFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "chartFormats" element
     */
    @Override
    public void setChartFormats(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartFormats chartFormats) {
        generatedSetterHelperImpl(chartFormats, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chartFormats" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartFormats addNewChartFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartFormats target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartFormats)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "chartFormats" element
     */
    @Override
    public void unsetChartFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "pivotHierarchies" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotHierarchies getPivotHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotHierarchies target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotHierarchies)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pivotHierarchies" element
     */
    @Override
    public boolean isSetPivotHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "pivotHierarchies" element
     */
    @Override
    public void setPivotHierarchies(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotHierarchies pivotHierarchies) {
        generatedSetterHelperImpl(pivotHierarchies, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotHierarchies" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotHierarchies addNewPivotHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotHierarchies target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotHierarchies)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "pivotHierarchies" element
     */
    @Override
    public void unsetPivotHierarchies() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "pivotTableStyleInfo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle getPivotTableStyleInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pivotTableStyleInfo" element
     */
    @Override
    public boolean isSetPivotTableStyleInfo() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "pivotTableStyleInfo" element
     */
    @Override
    public void setPivotTableStyleInfo(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle pivotTableStyleInfo) {
        generatedSetterHelperImpl(pivotTableStyleInfo, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotTableStyleInfo" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle addNewPivotTableStyleInfo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableStyle)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "pivotTableStyleInfo" element
     */
    @Override
    public void unsetPivotTableStyleInfo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "filters" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters getFilters() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "filters" element
     */
    @Override
    public boolean isSetFilters() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "filters" element
     */
    @Override
    public void setFilters(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters filters) {
        generatedSetterHelperImpl(filters, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "filters" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters addNewFilters() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFilters)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "filters" element
     */
    @Override
    public void unsetFilters() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "rowHierarchiesUsage" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage getRowHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rowHierarchiesUsage" element
     */
    @Override
    public boolean isSetRowHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "rowHierarchiesUsage" element
     */
    @Override
    public void setRowHierarchiesUsage(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage rowHierarchiesUsage) {
        generatedSetterHelperImpl(rowHierarchiesUsage, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rowHierarchiesUsage" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage addNewRowHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRowHierarchiesUsage)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "rowHierarchiesUsage" element
     */
    @Override
    public void unsetRowHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "colHierarchiesUsage" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColHierarchiesUsage getColHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColHierarchiesUsage target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColHierarchiesUsage)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "colHierarchiesUsage" element
     */
    @Override
    public boolean isSetColHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "colHierarchiesUsage" element
     */
    @Override
    public void setColHierarchiesUsage(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColHierarchiesUsage colHierarchiesUsage) {
        generatedSetterHelperImpl(colHierarchiesUsage, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colHierarchiesUsage" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColHierarchiesUsage addNewColHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColHierarchiesUsage target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColHierarchiesUsage)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "colHierarchiesUsage" element
     */
    @Override
    public void unsetColHierarchiesUsage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[16], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[16]);
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
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(name);
        }
    }

    /**
     * Gets the "cacheId" attribute
     */
    @Override
    public long getCacheId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "cacheId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCacheId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Sets the "cacheId" attribute
     */
    @Override
    public void setCacheId(long cacheId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setLongValue(cacheId);
        }
    }

    /**
     * Sets (as xml) the "cacheId" attribute
     */
    @Override
    public void xsetCacheId(org.apache.xmlbeans.XmlUnsignedInt cacheId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(cacheId);
        }
    }

    /**
     * Gets the "dataOnRows" attribute
     */
    @Override
    public boolean getDataOnRows() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "dataOnRows" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDataOnRows() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return target;
        }
    }

    /**
     * True if has "dataOnRows" attribute
     */
    @Override
    public boolean isSetDataOnRows() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "dataOnRows" attribute
     */
    @Override
    public void setDataOnRows(boolean dataOnRows) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setBooleanValue(dataOnRows);
        }
    }

    /**
     * Sets (as xml) the "dataOnRows" attribute
     */
    @Override
    public void xsetDataOnRows(org.apache.xmlbeans.XmlBoolean dataOnRows) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(dataOnRows);
        }
    }

    /**
     * Unsets the "dataOnRows" attribute
     */
    @Override
    public void unsetDataOnRows() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "dataPosition" attribute
     */
    @Override
    public long getDataPosition() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "dataPosition" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetDataPosition() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * True if has "dataPosition" attribute
     */
    @Override
    public boolean isSetDataPosition() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "dataPosition" attribute
     */
    @Override
    public void setDataPosition(long dataPosition) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setLongValue(dataPosition);
        }
    }

    /**
     * Sets (as xml) the "dataPosition" attribute
     */
    @Override
    public void xsetDataPosition(org.apache.xmlbeans.XmlUnsignedInt dataPosition) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(dataPosition);
        }
    }

    /**
     * Unsets the "dataPosition" attribute
     */
    @Override
    public void unsetDataPosition() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "autoFormatId" attribute
     */
    @Override
    public long getAutoFormatId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "autoFormatId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetAutoFormatId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * True if has "autoFormatId" attribute
     */
    @Override
    public boolean isSetAutoFormatId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "autoFormatId" attribute
     */
    @Override
    public void setAutoFormatId(long autoFormatId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setLongValue(autoFormatId);
        }
    }

    /**
     * Sets (as xml) the "autoFormatId" attribute
     */
    @Override
    public void xsetAutoFormatId(org.apache.xmlbeans.XmlUnsignedInt autoFormatId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(autoFormatId);
        }
    }

    /**
     * Unsets the "autoFormatId" attribute
     */
    @Override
    public void unsetAutoFormatId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "applyNumberFormats" attribute
     */
    @Override
    public boolean getApplyNumberFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "applyNumberFormats" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetApplyNumberFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "applyNumberFormats" attribute
     */
    @Override
    public boolean isSetApplyNumberFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "applyNumberFormats" attribute
     */
    @Override
    public void setApplyNumberFormats(boolean applyNumberFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setBooleanValue(applyNumberFormats);
        }
    }

    /**
     * Sets (as xml) the "applyNumberFormats" attribute
     */
    @Override
    public void xsetApplyNumberFormats(org.apache.xmlbeans.XmlBoolean applyNumberFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(applyNumberFormats);
        }
    }

    /**
     * Unsets the "applyNumberFormats" attribute
     */
    @Override
    public void unsetApplyNumberFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "applyBorderFormats" attribute
     */
    @Override
    public boolean getApplyBorderFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "applyBorderFormats" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetApplyBorderFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * True if has "applyBorderFormats" attribute
     */
    @Override
    public boolean isSetApplyBorderFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "applyBorderFormats" attribute
     */
    @Override
    public void setApplyBorderFormats(boolean applyBorderFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setBooleanValue(applyBorderFormats);
        }
    }

    /**
     * Sets (as xml) the "applyBorderFormats" attribute
     */
    @Override
    public void xsetApplyBorderFormats(org.apache.xmlbeans.XmlBoolean applyBorderFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(applyBorderFormats);
        }
    }

    /**
     * Unsets the "applyBorderFormats" attribute
     */
    @Override
    public void unsetApplyBorderFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "applyFontFormats" attribute
     */
    @Override
    public boolean getApplyFontFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "applyFontFormats" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetApplyFontFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * True if has "applyFontFormats" attribute
     */
    @Override
    public boolean isSetApplyFontFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "applyFontFormats" attribute
     */
    @Override
    public void setApplyFontFormats(boolean applyFontFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setBooleanValue(applyFontFormats);
        }
    }

    /**
     * Sets (as xml) the "applyFontFormats" attribute
     */
    @Override
    public void xsetApplyFontFormats(org.apache.xmlbeans.XmlBoolean applyFontFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(applyFontFormats);
        }
    }

    /**
     * Unsets the "applyFontFormats" attribute
     */
    @Override
    public void unsetApplyFontFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "applyPatternFormats" attribute
     */
    @Override
    public boolean getApplyPatternFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "applyPatternFormats" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetApplyPatternFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "applyPatternFormats" attribute
     */
    @Override
    public boolean isSetApplyPatternFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "applyPatternFormats" attribute
     */
    @Override
    public void setApplyPatternFormats(boolean applyPatternFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setBooleanValue(applyPatternFormats);
        }
    }

    /**
     * Sets (as xml) the "applyPatternFormats" attribute
     */
    @Override
    public void xsetApplyPatternFormats(org.apache.xmlbeans.XmlBoolean applyPatternFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(applyPatternFormats);
        }
    }

    /**
     * Unsets the "applyPatternFormats" attribute
     */
    @Override
    public void unsetApplyPatternFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "applyAlignmentFormats" attribute
     */
    @Override
    public boolean getApplyAlignmentFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "applyAlignmentFormats" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetApplyAlignmentFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * True if has "applyAlignmentFormats" attribute
     */
    @Override
    public boolean isSetApplyAlignmentFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "applyAlignmentFormats" attribute
     */
    @Override
    public void setApplyAlignmentFormats(boolean applyAlignmentFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setBooleanValue(applyAlignmentFormats);
        }
    }

    /**
     * Sets (as xml) the "applyAlignmentFormats" attribute
     */
    @Override
    public void xsetApplyAlignmentFormats(org.apache.xmlbeans.XmlBoolean applyAlignmentFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(applyAlignmentFormats);
        }
    }

    /**
     * Unsets the "applyAlignmentFormats" attribute
     */
    @Override
    public void unsetApplyAlignmentFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Gets the "applyWidthHeightFormats" attribute
     */
    @Override
    public boolean getApplyWidthHeightFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "applyWidthHeightFormats" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetApplyWidthHeightFormats() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * True if has "applyWidthHeightFormats" attribute
     */
    @Override
    public boolean isSetApplyWidthHeightFormats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[27]) != null;
        }
    }

    /**
     * Sets the "applyWidthHeightFormats" attribute
     */
    @Override
    public void setApplyWidthHeightFormats(boolean applyWidthHeightFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.setBooleanValue(applyWidthHeightFormats);
        }
    }

    /**
     * Sets (as xml) the "applyWidthHeightFormats" attribute
     */
    @Override
    public void xsetApplyWidthHeightFormats(org.apache.xmlbeans.XmlBoolean applyWidthHeightFormats) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.set(applyWidthHeightFormats);
        }
    }

    /**
     * Unsets the "applyWidthHeightFormats" attribute
     */
    @Override
    public void unsetApplyWidthHeightFormats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Gets the "dataCaption" attribute
     */
    @Override
    public java.lang.String getDataCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "dataCaption" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDataCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Sets the "dataCaption" attribute
     */
    @Override
    public void setDataCaption(java.lang.String dataCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.setStringValue(dataCaption);
        }
    }

    /**
     * Sets (as xml) the "dataCaption" attribute
     */
    @Override
    public void xsetDataCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring dataCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.set(dataCaption);
        }
    }

    /**
     * Gets the "grandTotalCaption" attribute
     */
    @Override
    public java.lang.String getGrandTotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "grandTotalCaption" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetGrandTotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * True if has "grandTotalCaption" attribute
     */
    @Override
    public boolean isSetGrandTotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[29]) != null;
        }
    }

    /**
     * Sets the "grandTotalCaption" attribute
     */
    @Override
    public void setGrandTotalCaption(java.lang.String grandTotalCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.setStringValue(grandTotalCaption);
        }
    }

    /**
     * Sets (as xml) the "grandTotalCaption" attribute
     */
    @Override
    public void xsetGrandTotalCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring grandTotalCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.set(grandTotalCaption);
        }
    }

    /**
     * Unsets the "grandTotalCaption" attribute
     */
    @Override
    public void unsetGrandTotalCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[29]);
        }
    }

    /**
     * Gets the "errorCaption" attribute
     */
    @Override
    public java.lang.String getErrorCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "errorCaption" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetErrorCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * True if has "errorCaption" attribute
     */
    @Override
    public boolean isSetErrorCaption() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[30]) != null;
        }
    }

    /**
     * Sets the "errorCaption" attribute
     */
    @Override
    public void setErrorCaption(java.lang.String errorCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[30]);
            }
            target.setStringValue(errorCaption);
        }
    }

    /**
     * Sets (as xml) the "errorCaption" attribute
     */
    @Override
    public void xsetErrorCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring errorCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[30]);
            }
            target.set(errorCaption);
        }
    }

    /**
     * Unsets the "errorCaption" attribute
     */
    @Override
    public void unsetErrorCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[30]);
        }
    }

    /**
     * Gets the "showError" attribute
     */
    @Override
    public boolean getShowError() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[31]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showError" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowError() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[31]);
            }
            return target;
        }
    }

    /**
     * True if has "showError" attribute
     */
    @Override
    public boolean isSetShowError() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[31]) != null;
        }
    }

    /**
     * Sets the "showError" attribute
     */
    @Override
    public void setShowError(boolean showError) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[31]);
            }
            target.setBooleanValue(showError);
        }
    }

    /**
     * Sets (as xml) the "showError" attribute
     */
    @Override
    public void xsetShowError(org.apache.xmlbeans.XmlBoolean showError) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[31]);
            }
            target.set(showError);
        }
    }

    /**
     * Unsets the "showError" attribute
     */
    @Override
    public void unsetShowError() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[31]);
        }
    }

    /**
     * Gets the "missingCaption" attribute
     */
    @Override
    public java.lang.String getMissingCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "missingCaption" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetMissingCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * True if has "missingCaption" attribute
     */
    @Override
    public boolean isSetMissingCaption() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[32]) != null;
        }
    }

    /**
     * Sets the "missingCaption" attribute
     */
    @Override
    public void setMissingCaption(java.lang.String missingCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[32]);
            }
            target.setStringValue(missingCaption);
        }
    }

    /**
     * Sets (as xml) the "missingCaption" attribute
     */
    @Override
    public void xsetMissingCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring missingCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[32]);
            }
            target.set(missingCaption);
        }
    }

    /**
     * Unsets the "missingCaption" attribute
     */
    @Override
    public void unsetMissingCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[32]);
        }
    }

    /**
     * Gets the "showMissing" attribute
     */
    @Override
    public boolean getShowMissing() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[33]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showMissing" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowMissing() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[33]);
            }
            return target;
        }
    }

    /**
     * True if has "showMissing" attribute
     */
    @Override
    public boolean isSetShowMissing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[33]) != null;
        }
    }

    /**
     * Sets the "showMissing" attribute
     */
    @Override
    public void setShowMissing(boolean showMissing) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[33]);
            }
            target.setBooleanValue(showMissing);
        }
    }

    /**
     * Sets (as xml) the "showMissing" attribute
     */
    @Override
    public void xsetShowMissing(org.apache.xmlbeans.XmlBoolean showMissing) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[33]);
            }
            target.set(showMissing);
        }
    }

    /**
     * Unsets the "showMissing" attribute
     */
    @Override
    public void unsetShowMissing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[33]);
        }
    }

    /**
     * Gets the "pageStyle" attribute
     */
    @Override
    public java.lang.String getPageStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "pageStyle" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetPageStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * True if has "pageStyle" attribute
     */
    @Override
    public boolean isSetPageStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[34]) != null;
        }
    }

    /**
     * Sets the "pageStyle" attribute
     */
    @Override
    public void setPageStyle(java.lang.String pageStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[34]);
            }
            target.setStringValue(pageStyle);
        }
    }

    /**
     * Sets (as xml) the "pageStyle" attribute
     */
    @Override
    public void xsetPageStyle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring pageStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[34]);
            }
            target.set(pageStyle);
        }
    }

    /**
     * Unsets the "pageStyle" attribute
     */
    @Override
    public void unsetPageStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[34]);
        }
    }

    /**
     * Gets the "pivotTableStyle" attribute
     */
    @Override
    public java.lang.String getPivotTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "pivotTableStyle" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetPivotTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * True if has "pivotTableStyle" attribute
     */
    @Override
    public boolean isSetPivotTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[35]) != null;
        }
    }

    /**
     * Sets the "pivotTableStyle" attribute
     */
    @Override
    public void setPivotTableStyle(java.lang.String pivotTableStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[35]);
            }
            target.setStringValue(pivotTableStyle);
        }
    }

    /**
     * Sets (as xml) the "pivotTableStyle" attribute
     */
    @Override
    public void xsetPivotTableStyle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring pivotTableStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[35]);
            }
            target.set(pivotTableStyle);
        }
    }

    /**
     * Unsets the "pivotTableStyle" attribute
     */
    @Override
    public void unsetPivotTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[35]);
        }
    }

    /**
     * Gets the "vacatedStyle" attribute
     */
    @Override
    public java.lang.String getVacatedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "vacatedStyle" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetVacatedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            return target;
        }
    }

    /**
     * True if has "vacatedStyle" attribute
     */
    @Override
    public boolean isSetVacatedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[36]) != null;
        }
    }

    /**
     * Sets the "vacatedStyle" attribute
     */
    @Override
    public void setVacatedStyle(java.lang.String vacatedStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[36]);
            }
            target.setStringValue(vacatedStyle);
        }
    }

    /**
     * Sets (as xml) the "vacatedStyle" attribute
     */
    @Override
    public void xsetVacatedStyle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring vacatedStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[36]);
            }
            target.set(vacatedStyle);
        }
    }

    /**
     * Unsets the "vacatedStyle" attribute
     */
    @Override
    public void unsetVacatedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[36]);
        }
    }

    /**
     * Gets the "tag" attribute
     */
    @Override
    public java.lang.String getTag() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "tag" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetTag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            return target;
        }
    }

    /**
     * True if has "tag" attribute
     */
    @Override
    public boolean isSetTag() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[37]) != null;
        }
    }

    /**
     * Sets the "tag" attribute
     */
    @Override
    public void setTag(java.lang.String tag) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[37]);
            }
            target.setStringValue(tag);
        }
    }

    /**
     * Sets (as xml) the "tag" attribute
     */
    @Override
    public void xsetTag(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring tag) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[37]);
            }
            target.set(tag);
        }
    }

    /**
     * Unsets the "tag" attribute
     */
    @Override
    public void unsetTag() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[37]);
        }
    }

    /**
     * Gets the "updatedVersion" attribute
     */
    @Override
    public short getUpdatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[38]);
            }
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "updatedVersion" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetUpdatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_default_attribute_value(PROPERTY_QNAME[38]);
            }
            return target;
        }
    }

    /**
     * True if has "updatedVersion" attribute
     */
    @Override
    public boolean isSetUpdatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[38]) != null;
        }
    }

    /**
     * Sets the "updatedVersion" attribute
     */
    @Override
    public void setUpdatedVersion(short updatedVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[38]);
            }
            target.setShortValue(updatedVersion);
        }
    }

    /**
     * Sets (as xml) the "updatedVersion" attribute
     */
    @Override
    public void xsetUpdatedVersion(org.apache.xmlbeans.XmlUnsignedByte updatedVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[38]);
            }
            target.set(updatedVersion);
        }
    }

    /**
     * Unsets the "updatedVersion" attribute
     */
    @Override
    public void unsetUpdatedVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[38]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[39]);
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
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_default_attribute_value(PROPERTY_QNAME[39]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[39]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[39]);
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
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[39]);
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
            get_store().remove_attribute(PROPERTY_QNAME[39]);
        }
    }

    /**
     * Gets the "asteriskTotals" attribute
     */
    @Override
    public boolean getAsteriskTotals() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[40]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "asteriskTotals" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAsteriskTotals() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[40]);
            }
            return target;
        }
    }

    /**
     * True if has "asteriskTotals" attribute
     */
    @Override
    public boolean isSetAsteriskTotals() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[40]) != null;
        }
    }

    /**
     * Sets the "asteriskTotals" attribute
     */
    @Override
    public void setAsteriskTotals(boolean asteriskTotals) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[40]);
            }
            target.setBooleanValue(asteriskTotals);
        }
    }

    /**
     * Sets (as xml) the "asteriskTotals" attribute
     */
    @Override
    public void xsetAsteriskTotals(org.apache.xmlbeans.XmlBoolean asteriskTotals) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[40]);
            }
            target.set(asteriskTotals);
        }
    }

    /**
     * Unsets the "asteriskTotals" attribute
     */
    @Override
    public void unsetAsteriskTotals() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[40]);
        }
    }

    /**
     * Gets the "showItems" attribute
     */
    @Override
    public boolean getShowItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[41]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[41]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showItems" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[41]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[41]);
            }
            return target;
        }
    }

    /**
     * True if has "showItems" attribute
     */
    @Override
    public boolean isSetShowItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[41]) != null;
        }
    }

    /**
     * Sets the "showItems" attribute
     */
    @Override
    public void setShowItems(boolean showItems) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[41]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[41]);
            }
            target.setBooleanValue(showItems);
        }
    }

    /**
     * Sets (as xml) the "showItems" attribute
     */
    @Override
    public void xsetShowItems(org.apache.xmlbeans.XmlBoolean showItems) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[41]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[41]);
            }
            target.set(showItems);
        }
    }

    /**
     * Unsets the "showItems" attribute
     */
    @Override
    public void unsetShowItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[41]);
        }
    }

    /**
     * Gets the "editData" attribute
     */
    @Override
    public boolean getEditData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[42]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[42]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "editData" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEditData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[42]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[42]);
            }
            return target;
        }
    }

    /**
     * True if has "editData" attribute
     */
    @Override
    public boolean isSetEditData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[42]) != null;
        }
    }

    /**
     * Sets the "editData" attribute
     */
    @Override
    public void setEditData(boolean editData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[42]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[42]);
            }
            target.setBooleanValue(editData);
        }
    }

    /**
     * Sets (as xml) the "editData" attribute
     */
    @Override
    public void xsetEditData(org.apache.xmlbeans.XmlBoolean editData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[42]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[42]);
            }
            target.set(editData);
        }
    }

    /**
     * Unsets the "editData" attribute
     */
    @Override
    public void unsetEditData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[42]);
        }
    }

    /**
     * Gets the "disableFieldList" attribute
     */
    @Override
    public boolean getDisableFieldList() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[43]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[43]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "disableFieldList" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDisableFieldList() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[43]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[43]);
            }
            return target;
        }
    }

    /**
     * True if has "disableFieldList" attribute
     */
    @Override
    public boolean isSetDisableFieldList() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[43]) != null;
        }
    }

    /**
     * Sets the "disableFieldList" attribute
     */
    @Override
    public void setDisableFieldList(boolean disableFieldList) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[43]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[43]);
            }
            target.setBooleanValue(disableFieldList);
        }
    }

    /**
     * Sets (as xml) the "disableFieldList" attribute
     */
    @Override
    public void xsetDisableFieldList(org.apache.xmlbeans.XmlBoolean disableFieldList) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[43]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[43]);
            }
            target.set(disableFieldList);
        }
    }

    /**
     * Unsets the "disableFieldList" attribute
     */
    @Override
    public void unsetDisableFieldList() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[43]);
        }
    }

    /**
     * Gets the "showCalcMbrs" attribute
     */
    @Override
    public boolean getShowCalcMbrs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[44]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[44]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showCalcMbrs" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowCalcMbrs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[44]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[44]);
            }
            return target;
        }
    }

    /**
     * True if has "showCalcMbrs" attribute
     */
    @Override
    public boolean isSetShowCalcMbrs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[44]) != null;
        }
    }

    /**
     * Sets the "showCalcMbrs" attribute
     */
    @Override
    public void setShowCalcMbrs(boolean showCalcMbrs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[44]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[44]);
            }
            target.setBooleanValue(showCalcMbrs);
        }
    }

    /**
     * Sets (as xml) the "showCalcMbrs" attribute
     */
    @Override
    public void xsetShowCalcMbrs(org.apache.xmlbeans.XmlBoolean showCalcMbrs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[44]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[44]);
            }
            target.set(showCalcMbrs);
        }
    }

    /**
     * Unsets the "showCalcMbrs" attribute
     */
    @Override
    public void unsetShowCalcMbrs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[44]);
        }
    }

    /**
     * Gets the "visualTotals" attribute
     */
    @Override
    public boolean getVisualTotals() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[45]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[45]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "visualTotals" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetVisualTotals() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[45]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[45]);
            }
            return target;
        }
    }

    /**
     * True if has "visualTotals" attribute
     */
    @Override
    public boolean isSetVisualTotals() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[45]) != null;
        }
    }

    /**
     * Sets the "visualTotals" attribute
     */
    @Override
    public void setVisualTotals(boolean visualTotals) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[45]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[45]);
            }
            target.setBooleanValue(visualTotals);
        }
    }

    /**
     * Sets (as xml) the "visualTotals" attribute
     */
    @Override
    public void xsetVisualTotals(org.apache.xmlbeans.XmlBoolean visualTotals) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[45]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[45]);
            }
            target.set(visualTotals);
        }
    }

    /**
     * Unsets the "visualTotals" attribute
     */
    @Override
    public void unsetVisualTotals() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[45]);
        }
    }

    /**
     * Gets the "showMultipleLabel" attribute
     */
    @Override
    public boolean getShowMultipleLabel() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[46]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[46]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showMultipleLabel" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowMultipleLabel() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[46]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[46]);
            }
            return target;
        }
    }

    /**
     * True if has "showMultipleLabel" attribute
     */
    @Override
    public boolean isSetShowMultipleLabel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[46]) != null;
        }
    }

    /**
     * Sets the "showMultipleLabel" attribute
     */
    @Override
    public void setShowMultipleLabel(boolean showMultipleLabel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[46]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[46]);
            }
            target.setBooleanValue(showMultipleLabel);
        }
    }

    /**
     * Sets (as xml) the "showMultipleLabel" attribute
     */
    @Override
    public void xsetShowMultipleLabel(org.apache.xmlbeans.XmlBoolean showMultipleLabel) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[46]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[46]);
            }
            target.set(showMultipleLabel);
        }
    }

    /**
     * Unsets the "showMultipleLabel" attribute
     */
    @Override
    public void unsetShowMultipleLabel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[46]);
        }
    }

    /**
     * Gets the "showDataDropDown" attribute
     */
    @Override
    public boolean getShowDataDropDown() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[47]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[47]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showDataDropDown" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowDataDropDown() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[47]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[47]);
            }
            return target;
        }
    }

    /**
     * True if has "showDataDropDown" attribute
     */
    @Override
    public boolean isSetShowDataDropDown() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[47]) != null;
        }
    }

    /**
     * Sets the "showDataDropDown" attribute
     */
    @Override
    public void setShowDataDropDown(boolean showDataDropDown) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[47]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[47]);
            }
            target.setBooleanValue(showDataDropDown);
        }
    }

    /**
     * Sets (as xml) the "showDataDropDown" attribute
     */
    @Override
    public void xsetShowDataDropDown(org.apache.xmlbeans.XmlBoolean showDataDropDown) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[47]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[47]);
            }
            target.set(showDataDropDown);
        }
    }

    /**
     * Unsets the "showDataDropDown" attribute
     */
    @Override
    public void unsetShowDataDropDown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[47]);
        }
    }

    /**
     * Gets the "showDrill" attribute
     */
    @Override
    public boolean getShowDrill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[48]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[48]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showDrill" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowDrill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[48]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[48]);
            }
            return target;
        }
    }

    /**
     * True if has "showDrill" attribute
     */
    @Override
    public boolean isSetShowDrill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[48]) != null;
        }
    }

    /**
     * Sets the "showDrill" attribute
     */
    @Override
    public void setShowDrill(boolean showDrill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[48]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[48]);
            }
            target.setBooleanValue(showDrill);
        }
    }

    /**
     * Sets (as xml) the "showDrill" attribute
     */
    @Override
    public void xsetShowDrill(org.apache.xmlbeans.XmlBoolean showDrill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[48]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[48]);
            }
            target.set(showDrill);
        }
    }

    /**
     * Unsets the "showDrill" attribute
     */
    @Override
    public void unsetShowDrill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[48]);
        }
    }

    /**
     * Gets the "printDrill" attribute
     */
    @Override
    public boolean getPrintDrill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[49]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[49]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "printDrill" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPrintDrill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[49]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[49]);
            }
            return target;
        }
    }

    /**
     * True if has "printDrill" attribute
     */
    @Override
    public boolean isSetPrintDrill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[49]) != null;
        }
    }

    /**
     * Sets the "printDrill" attribute
     */
    @Override
    public void setPrintDrill(boolean printDrill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[49]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[49]);
            }
            target.setBooleanValue(printDrill);
        }
    }

    /**
     * Sets (as xml) the "printDrill" attribute
     */
    @Override
    public void xsetPrintDrill(org.apache.xmlbeans.XmlBoolean printDrill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[49]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[49]);
            }
            target.set(printDrill);
        }
    }

    /**
     * Unsets the "printDrill" attribute
     */
    @Override
    public void unsetPrintDrill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[49]);
        }
    }

    /**
     * Gets the "showMemberPropertyTips" attribute
     */
    @Override
    public boolean getShowMemberPropertyTips() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[50]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[50]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showMemberPropertyTips" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowMemberPropertyTips() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[50]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[50]);
            }
            return target;
        }
    }

    /**
     * True if has "showMemberPropertyTips" attribute
     */
    @Override
    public boolean isSetShowMemberPropertyTips() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[50]) != null;
        }
    }

    /**
     * Sets the "showMemberPropertyTips" attribute
     */
    @Override
    public void setShowMemberPropertyTips(boolean showMemberPropertyTips) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[50]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[50]);
            }
            target.setBooleanValue(showMemberPropertyTips);
        }
    }

    /**
     * Sets (as xml) the "showMemberPropertyTips" attribute
     */
    @Override
    public void xsetShowMemberPropertyTips(org.apache.xmlbeans.XmlBoolean showMemberPropertyTips) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[50]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[50]);
            }
            target.set(showMemberPropertyTips);
        }
    }

    /**
     * Unsets the "showMemberPropertyTips" attribute
     */
    @Override
    public void unsetShowMemberPropertyTips() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[50]);
        }
    }

    /**
     * Gets the "showDataTips" attribute
     */
    @Override
    public boolean getShowDataTips() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[51]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[51]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showDataTips" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowDataTips() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[51]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[51]);
            }
            return target;
        }
    }

    /**
     * True if has "showDataTips" attribute
     */
    @Override
    public boolean isSetShowDataTips() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[51]) != null;
        }
    }

    /**
     * Sets the "showDataTips" attribute
     */
    @Override
    public void setShowDataTips(boolean showDataTips) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[51]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[51]);
            }
            target.setBooleanValue(showDataTips);
        }
    }

    /**
     * Sets (as xml) the "showDataTips" attribute
     */
    @Override
    public void xsetShowDataTips(org.apache.xmlbeans.XmlBoolean showDataTips) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[51]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[51]);
            }
            target.set(showDataTips);
        }
    }

    /**
     * Unsets the "showDataTips" attribute
     */
    @Override
    public void unsetShowDataTips() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[51]);
        }
    }

    /**
     * Gets the "enableWizard" attribute
     */
    @Override
    public boolean getEnableWizard() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[52]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[52]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "enableWizard" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEnableWizard() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[52]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[52]);
            }
            return target;
        }
    }

    /**
     * True if has "enableWizard" attribute
     */
    @Override
    public boolean isSetEnableWizard() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[52]) != null;
        }
    }

    /**
     * Sets the "enableWizard" attribute
     */
    @Override
    public void setEnableWizard(boolean enableWizard) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[52]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[52]);
            }
            target.setBooleanValue(enableWizard);
        }
    }

    /**
     * Sets (as xml) the "enableWizard" attribute
     */
    @Override
    public void xsetEnableWizard(org.apache.xmlbeans.XmlBoolean enableWizard) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[52]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[52]);
            }
            target.set(enableWizard);
        }
    }

    /**
     * Unsets the "enableWizard" attribute
     */
    @Override
    public void unsetEnableWizard() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[52]);
        }
    }

    /**
     * Gets the "enableDrill" attribute
     */
    @Override
    public boolean getEnableDrill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[53]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[53]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "enableDrill" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEnableDrill() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[53]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[53]);
            }
            return target;
        }
    }

    /**
     * True if has "enableDrill" attribute
     */
    @Override
    public boolean isSetEnableDrill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[53]) != null;
        }
    }

    /**
     * Sets the "enableDrill" attribute
     */
    @Override
    public void setEnableDrill(boolean enableDrill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[53]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[53]);
            }
            target.setBooleanValue(enableDrill);
        }
    }

    /**
     * Sets (as xml) the "enableDrill" attribute
     */
    @Override
    public void xsetEnableDrill(org.apache.xmlbeans.XmlBoolean enableDrill) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[53]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[53]);
            }
            target.set(enableDrill);
        }
    }

    /**
     * Unsets the "enableDrill" attribute
     */
    @Override
    public void unsetEnableDrill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[53]);
        }
    }

    /**
     * Gets the "enableFieldProperties" attribute
     */
    @Override
    public boolean getEnableFieldProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[54]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[54]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "enableFieldProperties" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEnableFieldProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[54]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[54]);
            }
            return target;
        }
    }

    /**
     * True if has "enableFieldProperties" attribute
     */
    @Override
    public boolean isSetEnableFieldProperties() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[54]) != null;
        }
    }

    /**
     * Sets the "enableFieldProperties" attribute
     */
    @Override
    public void setEnableFieldProperties(boolean enableFieldProperties) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[54]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[54]);
            }
            target.setBooleanValue(enableFieldProperties);
        }
    }

    /**
     * Sets (as xml) the "enableFieldProperties" attribute
     */
    @Override
    public void xsetEnableFieldProperties(org.apache.xmlbeans.XmlBoolean enableFieldProperties) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[54]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[54]);
            }
            target.set(enableFieldProperties);
        }
    }

    /**
     * Unsets the "enableFieldProperties" attribute
     */
    @Override
    public void unsetEnableFieldProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[54]);
        }
    }

    /**
     * Gets the "preserveFormatting" attribute
     */
    @Override
    public boolean getPreserveFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[55]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[55]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "preserveFormatting" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPreserveFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[55]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[55]);
            }
            return target;
        }
    }

    /**
     * True if has "preserveFormatting" attribute
     */
    @Override
    public boolean isSetPreserveFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[55]) != null;
        }
    }

    /**
     * Sets the "preserveFormatting" attribute
     */
    @Override
    public void setPreserveFormatting(boolean preserveFormatting) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[55]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[55]);
            }
            target.setBooleanValue(preserveFormatting);
        }
    }

    /**
     * Sets (as xml) the "preserveFormatting" attribute
     */
    @Override
    public void xsetPreserveFormatting(org.apache.xmlbeans.XmlBoolean preserveFormatting) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[55]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[55]);
            }
            target.set(preserveFormatting);
        }
    }

    /**
     * Unsets the "preserveFormatting" attribute
     */
    @Override
    public void unsetPreserveFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[55]);
        }
    }

    /**
     * Gets the "useAutoFormatting" attribute
     */
    @Override
    public boolean getUseAutoFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[56]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[56]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "useAutoFormatting" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetUseAutoFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[56]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[56]);
            }
            return target;
        }
    }

    /**
     * True if has "useAutoFormatting" attribute
     */
    @Override
    public boolean isSetUseAutoFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[56]) != null;
        }
    }

    /**
     * Sets the "useAutoFormatting" attribute
     */
    @Override
    public void setUseAutoFormatting(boolean useAutoFormatting) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[56]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[56]);
            }
            target.setBooleanValue(useAutoFormatting);
        }
    }

    /**
     * Sets (as xml) the "useAutoFormatting" attribute
     */
    @Override
    public void xsetUseAutoFormatting(org.apache.xmlbeans.XmlBoolean useAutoFormatting) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[56]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[56]);
            }
            target.set(useAutoFormatting);
        }
    }

    /**
     * Unsets the "useAutoFormatting" attribute
     */
    @Override
    public void unsetUseAutoFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[56]);
        }
    }

    /**
     * Gets the "pageWrap" attribute
     */
    @Override
    public long getPageWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[57]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[57]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "pageWrap" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetPageWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[57]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[57]);
            }
            return target;
        }
    }

    /**
     * True if has "pageWrap" attribute
     */
    @Override
    public boolean isSetPageWrap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[57]) != null;
        }
    }

    /**
     * Sets the "pageWrap" attribute
     */
    @Override
    public void setPageWrap(long pageWrap) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[57]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[57]);
            }
            target.setLongValue(pageWrap);
        }
    }

    /**
     * Sets (as xml) the "pageWrap" attribute
     */
    @Override
    public void xsetPageWrap(org.apache.xmlbeans.XmlUnsignedInt pageWrap) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[57]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[57]);
            }
            target.set(pageWrap);
        }
    }

    /**
     * Unsets the "pageWrap" attribute
     */
    @Override
    public void unsetPageWrap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[57]);
        }
    }

    /**
     * Gets the "pageOverThenDown" attribute
     */
    @Override
    public boolean getPageOverThenDown() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[58]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[58]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "pageOverThenDown" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPageOverThenDown() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[58]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[58]);
            }
            return target;
        }
    }

    /**
     * True if has "pageOverThenDown" attribute
     */
    @Override
    public boolean isSetPageOverThenDown() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[58]) != null;
        }
    }

    /**
     * Sets the "pageOverThenDown" attribute
     */
    @Override
    public void setPageOverThenDown(boolean pageOverThenDown) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[58]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[58]);
            }
            target.setBooleanValue(pageOverThenDown);
        }
    }

    /**
     * Sets (as xml) the "pageOverThenDown" attribute
     */
    @Override
    public void xsetPageOverThenDown(org.apache.xmlbeans.XmlBoolean pageOverThenDown) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[58]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[58]);
            }
            target.set(pageOverThenDown);
        }
    }

    /**
     * Unsets the "pageOverThenDown" attribute
     */
    @Override
    public void unsetPageOverThenDown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[58]);
        }
    }

    /**
     * Gets the "subtotalHiddenItems" attribute
     */
    @Override
    public boolean getSubtotalHiddenItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[59]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[59]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "subtotalHiddenItems" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSubtotalHiddenItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[59]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[59]);
            }
            return target;
        }
    }

    /**
     * True if has "subtotalHiddenItems" attribute
     */
    @Override
    public boolean isSetSubtotalHiddenItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[59]) != null;
        }
    }

    /**
     * Sets the "subtotalHiddenItems" attribute
     */
    @Override
    public void setSubtotalHiddenItems(boolean subtotalHiddenItems) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[59]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[59]);
            }
            target.setBooleanValue(subtotalHiddenItems);
        }
    }

    /**
     * Sets (as xml) the "subtotalHiddenItems" attribute
     */
    @Override
    public void xsetSubtotalHiddenItems(org.apache.xmlbeans.XmlBoolean subtotalHiddenItems) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[59]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[59]);
            }
            target.set(subtotalHiddenItems);
        }
    }

    /**
     * Unsets the "subtotalHiddenItems" attribute
     */
    @Override
    public void unsetSubtotalHiddenItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[59]);
        }
    }

    /**
     * Gets the "rowGrandTotals" attribute
     */
    @Override
    public boolean getRowGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[60]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[60]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "rowGrandTotals" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRowGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[60]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[60]);
            }
            return target;
        }
    }

    /**
     * True if has "rowGrandTotals" attribute
     */
    @Override
    public boolean isSetRowGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[60]) != null;
        }
    }

    /**
     * Sets the "rowGrandTotals" attribute
     */
    @Override
    public void setRowGrandTotals(boolean rowGrandTotals) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[60]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[60]);
            }
            target.setBooleanValue(rowGrandTotals);
        }
    }

    /**
     * Sets (as xml) the "rowGrandTotals" attribute
     */
    @Override
    public void xsetRowGrandTotals(org.apache.xmlbeans.XmlBoolean rowGrandTotals) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[60]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[60]);
            }
            target.set(rowGrandTotals);
        }
    }

    /**
     * Unsets the "rowGrandTotals" attribute
     */
    @Override
    public void unsetRowGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[60]);
        }
    }

    /**
     * Gets the "colGrandTotals" attribute
     */
    @Override
    public boolean getColGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[61]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[61]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "colGrandTotals" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetColGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[61]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[61]);
            }
            return target;
        }
    }

    /**
     * True if has "colGrandTotals" attribute
     */
    @Override
    public boolean isSetColGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[61]) != null;
        }
    }

    /**
     * Sets the "colGrandTotals" attribute
     */
    @Override
    public void setColGrandTotals(boolean colGrandTotals) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[61]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[61]);
            }
            target.setBooleanValue(colGrandTotals);
        }
    }

    /**
     * Sets (as xml) the "colGrandTotals" attribute
     */
    @Override
    public void xsetColGrandTotals(org.apache.xmlbeans.XmlBoolean colGrandTotals) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[61]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[61]);
            }
            target.set(colGrandTotals);
        }
    }

    /**
     * Unsets the "colGrandTotals" attribute
     */
    @Override
    public void unsetColGrandTotals() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[61]);
        }
    }

    /**
     * Gets the "fieldPrintTitles" attribute
     */
    @Override
    public boolean getFieldPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[62]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[62]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "fieldPrintTitles" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFieldPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[62]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[62]);
            }
            return target;
        }
    }

    /**
     * True if has "fieldPrintTitles" attribute
     */
    @Override
    public boolean isSetFieldPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[62]) != null;
        }
    }

    /**
     * Sets the "fieldPrintTitles" attribute
     */
    @Override
    public void setFieldPrintTitles(boolean fieldPrintTitles) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[62]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[62]);
            }
            target.setBooleanValue(fieldPrintTitles);
        }
    }

    /**
     * Sets (as xml) the "fieldPrintTitles" attribute
     */
    @Override
    public void xsetFieldPrintTitles(org.apache.xmlbeans.XmlBoolean fieldPrintTitles) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[62]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[62]);
            }
            target.set(fieldPrintTitles);
        }
    }

    /**
     * Unsets the "fieldPrintTitles" attribute
     */
    @Override
    public void unsetFieldPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[62]);
        }
    }

    /**
     * Gets the "itemPrintTitles" attribute
     */
    @Override
    public boolean getItemPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[63]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[63]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "itemPrintTitles" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetItemPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[63]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[63]);
            }
            return target;
        }
    }

    /**
     * True if has "itemPrintTitles" attribute
     */
    @Override
    public boolean isSetItemPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[63]) != null;
        }
    }

    /**
     * Sets the "itemPrintTitles" attribute
     */
    @Override
    public void setItemPrintTitles(boolean itemPrintTitles) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[63]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[63]);
            }
            target.setBooleanValue(itemPrintTitles);
        }
    }

    /**
     * Sets (as xml) the "itemPrintTitles" attribute
     */
    @Override
    public void xsetItemPrintTitles(org.apache.xmlbeans.XmlBoolean itemPrintTitles) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[63]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[63]);
            }
            target.set(itemPrintTitles);
        }
    }

    /**
     * Unsets the "itemPrintTitles" attribute
     */
    @Override
    public void unsetItemPrintTitles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[63]);
        }
    }

    /**
     * Gets the "mergeItem" attribute
     */
    @Override
    public boolean getMergeItem() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[64]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[64]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "mergeItem" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMergeItem() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[64]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[64]);
            }
            return target;
        }
    }

    /**
     * True if has "mergeItem" attribute
     */
    @Override
    public boolean isSetMergeItem() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[64]) != null;
        }
    }

    /**
     * Sets the "mergeItem" attribute
     */
    @Override
    public void setMergeItem(boolean mergeItem) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[64]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[64]);
            }
            target.setBooleanValue(mergeItem);
        }
    }

    /**
     * Sets (as xml) the "mergeItem" attribute
     */
    @Override
    public void xsetMergeItem(org.apache.xmlbeans.XmlBoolean mergeItem) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[64]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[64]);
            }
            target.set(mergeItem);
        }
    }

    /**
     * Unsets the "mergeItem" attribute
     */
    @Override
    public void unsetMergeItem() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[64]);
        }
    }

    /**
     * Gets the "showDropZones" attribute
     */
    @Override
    public boolean getShowDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[65]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[65]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showDropZones" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[65]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[65]);
            }
            return target;
        }
    }

    /**
     * True if has "showDropZones" attribute
     */
    @Override
    public boolean isSetShowDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[65]) != null;
        }
    }

    /**
     * Sets the "showDropZones" attribute
     */
    @Override
    public void setShowDropZones(boolean showDropZones) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[65]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[65]);
            }
            target.setBooleanValue(showDropZones);
        }
    }

    /**
     * Sets (as xml) the "showDropZones" attribute
     */
    @Override
    public void xsetShowDropZones(org.apache.xmlbeans.XmlBoolean showDropZones) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[65]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[65]);
            }
            target.set(showDropZones);
        }
    }

    /**
     * Unsets the "showDropZones" attribute
     */
    @Override
    public void unsetShowDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[65]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[66]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[66]);
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
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[66]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_default_attribute_value(PROPERTY_QNAME[66]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[66]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[66]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[66]);
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
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_attribute_user(PROPERTY_QNAME[66]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_attribute_user(PROPERTY_QNAME[66]);
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
            get_store().remove_attribute(PROPERTY_QNAME[66]);
        }
    }

    /**
     * Gets the "indent" attribute
     */
    @Override
    public long getIndent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[67]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[67]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "indent" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[67]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[67]);
            }
            return target;
        }
    }

    /**
     * True if has "indent" attribute
     */
    @Override
    public boolean isSetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[67]) != null;
        }
    }

    /**
     * Sets the "indent" attribute
     */
    @Override
    public void setIndent(long indent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[67]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[67]);
            }
            target.setLongValue(indent);
        }
    }

    /**
     * Sets (as xml) the "indent" attribute
     */
    @Override
    public void xsetIndent(org.apache.xmlbeans.XmlUnsignedInt indent) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[67]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[67]);
            }
            target.set(indent);
        }
    }

    /**
     * Unsets the "indent" attribute
     */
    @Override
    public void unsetIndent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[67]);
        }
    }

    /**
     * Gets the "showEmptyRow" attribute
     */
    @Override
    public boolean getShowEmptyRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[68]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[68]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showEmptyRow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowEmptyRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[68]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[68]);
            }
            return target;
        }
    }

    /**
     * True if has "showEmptyRow" attribute
     */
    @Override
    public boolean isSetShowEmptyRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[68]) != null;
        }
    }

    /**
     * Sets the "showEmptyRow" attribute
     */
    @Override
    public void setShowEmptyRow(boolean showEmptyRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[68]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[68]);
            }
            target.setBooleanValue(showEmptyRow);
        }
    }

    /**
     * Sets (as xml) the "showEmptyRow" attribute
     */
    @Override
    public void xsetShowEmptyRow(org.apache.xmlbeans.XmlBoolean showEmptyRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[68]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[68]);
            }
            target.set(showEmptyRow);
        }
    }

    /**
     * Unsets the "showEmptyRow" attribute
     */
    @Override
    public void unsetShowEmptyRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[68]);
        }
    }

    /**
     * Gets the "showEmptyCol" attribute
     */
    @Override
    public boolean getShowEmptyCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[69]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[69]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showEmptyCol" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowEmptyCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[69]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[69]);
            }
            return target;
        }
    }

    /**
     * True if has "showEmptyCol" attribute
     */
    @Override
    public boolean isSetShowEmptyCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[69]) != null;
        }
    }

    /**
     * Sets the "showEmptyCol" attribute
     */
    @Override
    public void setShowEmptyCol(boolean showEmptyCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[69]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[69]);
            }
            target.setBooleanValue(showEmptyCol);
        }
    }

    /**
     * Sets (as xml) the "showEmptyCol" attribute
     */
    @Override
    public void xsetShowEmptyCol(org.apache.xmlbeans.XmlBoolean showEmptyCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[69]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[69]);
            }
            target.set(showEmptyCol);
        }
    }

    /**
     * Unsets the "showEmptyCol" attribute
     */
    @Override
    public void unsetShowEmptyCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[69]);
        }
    }

    /**
     * Gets the "showHeaders" attribute
     */
    @Override
    public boolean getShowHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[70]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[70]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showHeaders" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[70]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[70]);
            }
            return target;
        }
    }

    /**
     * True if has "showHeaders" attribute
     */
    @Override
    public boolean isSetShowHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[70]) != null;
        }
    }

    /**
     * Sets the "showHeaders" attribute
     */
    @Override
    public void setShowHeaders(boolean showHeaders) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[70]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[70]);
            }
            target.setBooleanValue(showHeaders);
        }
    }

    /**
     * Sets (as xml) the "showHeaders" attribute
     */
    @Override
    public void xsetShowHeaders(org.apache.xmlbeans.XmlBoolean showHeaders) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[70]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[70]);
            }
            target.set(showHeaders);
        }
    }

    /**
     * Unsets the "showHeaders" attribute
     */
    @Override
    public void unsetShowHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[70]);
        }
    }

    /**
     * Gets the "compact" attribute
     */
    @Override
    public boolean getCompact() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[71]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[71]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "compact" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCompact() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[71]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[71]);
            }
            return target;
        }
    }

    /**
     * True if has "compact" attribute
     */
    @Override
    public boolean isSetCompact() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[71]) != null;
        }
    }

    /**
     * Sets the "compact" attribute
     */
    @Override
    public void setCompact(boolean compact) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[71]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[71]);
            }
            target.setBooleanValue(compact);
        }
    }

    /**
     * Sets (as xml) the "compact" attribute
     */
    @Override
    public void xsetCompact(org.apache.xmlbeans.XmlBoolean compact) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[71]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[71]);
            }
            target.set(compact);
        }
    }

    /**
     * Unsets the "compact" attribute
     */
    @Override
    public void unsetCompact() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[71]);
        }
    }

    /**
     * Gets the "outline" attribute
     */
    @Override
    public boolean getOutline() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[72]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[72]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "outline" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[72]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[72]);
            }
            return target;
        }
    }

    /**
     * True if has "outline" attribute
     */
    @Override
    public boolean isSetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[72]) != null;
        }
    }

    /**
     * Sets the "outline" attribute
     */
    @Override
    public void setOutline(boolean outline) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[72]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[72]);
            }
            target.setBooleanValue(outline);
        }
    }

    /**
     * Sets (as xml) the "outline" attribute
     */
    @Override
    public void xsetOutline(org.apache.xmlbeans.XmlBoolean outline) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[72]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[72]);
            }
            target.set(outline);
        }
    }

    /**
     * Unsets the "outline" attribute
     */
    @Override
    public void unsetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[72]);
        }
    }

    /**
     * Gets the "outlineData" attribute
     */
    @Override
    public boolean getOutlineData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[73]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[73]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "outlineData" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetOutlineData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[73]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[73]);
            }
            return target;
        }
    }

    /**
     * True if has "outlineData" attribute
     */
    @Override
    public boolean isSetOutlineData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[73]) != null;
        }
    }

    /**
     * Sets the "outlineData" attribute
     */
    @Override
    public void setOutlineData(boolean outlineData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[73]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[73]);
            }
            target.setBooleanValue(outlineData);
        }
    }

    /**
     * Sets (as xml) the "outlineData" attribute
     */
    @Override
    public void xsetOutlineData(org.apache.xmlbeans.XmlBoolean outlineData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[73]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[73]);
            }
            target.set(outlineData);
        }
    }

    /**
     * Unsets the "outlineData" attribute
     */
    @Override
    public void unsetOutlineData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[73]);
        }
    }

    /**
     * Gets the "compactData" attribute
     */
    @Override
    public boolean getCompactData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[74]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[74]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "compactData" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCompactData() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[74]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[74]);
            }
            return target;
        }
    }

    /**
     * True if has "compactData" attribute
     */
    @Override
    public boolean isSetCompactData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[74]) != null;
        }
    }

    /**
     * Sets the "compactData" attribute
     */
    @Override
    public void setCompactData(boolean compactData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[74]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[74]);
            }
            target.setBooleanValue(compactData);
        }
    }

    /**
     * Sets (as xml) the "compactData" attribute
     */
    @Override
    public void xsetCompactData(org.apache.xmlbeans.XmlBoolean compactData) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[74]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[74]);
            }
            target.set(compactData);
        }
    }

    /**
     * Unsets the "compactData" attribute
     */
    @Override
    public void unsetCompactData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[74]);
        }
    }

    /**
     * Gets the "published" attribute
     */
    @Override
    public boolean getPublished() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[75]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[75]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "published" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPublished() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[75]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[75]);
            }
            return target;
        }
    }

    /**
     * True if has "published" attribute
     */
    @Override
    public boolean isSetPublished() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[75]) != null;
        }
    }

    /**
     * Sets the "published" attribute
     */
    @Override
    public void setPublished(boolean published) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[75]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[75]);
            }
            target.setBooleanValue(published);
        }
    }

    /**
     * Sets (as xml) the "published" attribute
     */
    @Override
    public void xsetPublished(org.apache.xmlbeans.XmlBoolean published) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[75]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[75]);
            }
            target.set(published);
        }
    }

    /**
     * Unsets the "published" attribute
     */
    @Override
    public void unsetPublished() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[75]);
        }
    }

    /**
     * Gets the "gridDropZones" attribute
     */
    @Override
    public boolean getGridDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[76]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[76]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "gridDropZones" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetGridDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[76]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[76]);
            }
            return target;
        }
    }

    /**
     * True if has "gridDropZones" attribute
     */
    @Override
    public boolean isSetGridDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[76]) != null;
        }
    }

    /**
     * Sets the "gridDropZones" attribute
     */
    @Override
    public void setGridDropZones(boolean gridDropZones) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[76]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[76]);
            }
            target.setBooleanValue(gridDropZones);
        }
    }

    /**
     * Sets (as xml) the "gridDropZones" attribute
     */
    @Override
    public void xsetGridDropZones(org.apache.xmlbeans.XmlBoolean gridDropZones) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[76]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[76]);
            }
            target.set(gridDropZones);
        }
    }

    /**
     * Unsets the "gridDropZones" attribute
     */
    @Override
    public void unsetGridDropZones() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[76]);
        }
    }

    /**
     * Gets the "immersive" attribute
     */
    @Override
    public boolean getImmersive() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[77]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[77]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "immersive" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetImmersive() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[77]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[77]);
            }
            return target;
        }
    }

    /**
     * True if has "immersive" attribute
     */
    @Override
    public boolean isSetImmersive() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[77]) != null;
        }
    }

    /**
     * Sets the "immersive" attribute
     */
    @Override
    public void setImmersive(boolean immersive) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[77]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[77]);
            }
            target.setBooleanValue(immersive);
        }
    }

    /**
     * Sets (as xml) the "immersive" attribute
     */
    @Override
    public void xsetImmersive(org.apache.xmlbeans.XmlBoolean immersive) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[77]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[77]);
            }
            target.set(immersive);
        }
    }

    /**
     * Unsets the "immersive" attribute
     */
    @Override
    public void unsetImmersive() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[77]);
        }
    }

    /**
     * Gets the "multipleFieldFilters" attribute
     */
    @Override
    public boolean getMultipleFieldFilters() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[78]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[78]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "multipleFieldFilters" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMultipleFieldFilters() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[78]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[78]);
            }
            return target;
        }
    }

    /**
     * True if has "multipleFieldFilters" attribute
     */
    @Override
    public boolean isSetMultipleFieldFilters() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[78]) != null;
        }
    }

    /**
     * Sets the "multipleFieldFilters" attribute
     */
    @Override
    public void setMultipleFieldFilters(boolean multipleFieldFilters) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[78]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[78]);
            }
            target.setBooleanValue(multipleFieldFilters);
        }
    }

    /**
     * Sets (as xml) the "multipleFieldFilters" attribute
     */
    @Override
    public void xsetMultipleFieldFilters(org.apache.xmlbeans.XmlBoolean multipleFieldFilters) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[78]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[78]);
            }
            target.set(multipleFieldFilters);
        }
    }

    /**
     * Unsets the "multipleFieldFilters" attribute
     */
    @Override
    public void unsetMultipleFieldFilters() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[78]);
        }
    }

    /**
     * Gets the "chartFormat" attribute
     */
    @Override
    public long getChartFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[79]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[79]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "chartFormat" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetChartFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[79]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[79]);
            }
            return target;
        }
    }

    /**
     * True if has "chartFormat" attribute
     */
    @Override
    public boolean isSetChartFormat() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[79]) != null;
        }
    }

    /**
     * Sets the "chartFormat" attribute
     */
    @Override
    public void setChartFormat(long chartFormat) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[79]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[79]);
            }
            target.setLongValue(chartFormat);
        }
    }

    /**
     * Sets (as xml) the "chartFormat" attribute
     */
    @Override
    public void xsetChartFormat(org.apache.xmlbeans.XmlUnsignedInt chartFormat) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[79]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[79]);
            }
            target.set(chartFormat);
        }
    }

    /**
     * Unsets the "chartFormat" attribute
     */
    @Override
    public void unsetChartFormat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[79]);
        }
    }

    /**
     * Gets the "rowHeaderCaption" attribute
     */
    @Override
    public java.lang.String getRowHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[80]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "rowHeaderCaption" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetRowHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[80]);
            return target;
        }
    }

    /**
     * True if has "rowHeaderCaption" attribute
     */
    @Override
    public boolean isSetRowHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[80]) != null;
        }
    }

    /**
     * Sets the "rowHeaderCaption" attribute
     */
    @Override
    public void setRowHeaderCaption(java.lang.String rowHeaderCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[80]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[80]);
            }
            target.setStringValue(rowHeaderCaption);
        }
    }

    /**
     * Sets (as xml) the "rowHeaderCaption" attribute
     */
    @Override
    public void xsetRowHeaderCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring rowHeaderCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[80]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[80]);
            }
            target.set(rowHeaderCaption);
        }
    }

    /**
     * Unsets the "rowHeaderCaption" attribute
     */
    @Override
    public void unsetRowHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[80]);
        }
    }

    /**
     * Gets the "colHeaderCaption" attribute
     */
    @Override
    public java.lang.String getColHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[81]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "colHeaderCaption" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetColHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[81]);
            return target;
        }
    }

    /**
     * True if has "colHeaderCaption" attribute
     */
    @Override
    public boolean isSetColHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[81]) != null;
        }
    }

    /**
     * Sets the "colHeaderCaption" attribute
     */
    @Override
    public void setColHeaderCaption(java.lang.String colHeaderCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[81]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[81]);
            }
            target.setStringValue(colHeaderCaption);
        }
    }

    /**
     * Sets (as xml) the "colHeaderCaption" attribute
     */
    @Override
    public void xsetColHeaderCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring colHeaderCaption) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_attribute_user(PROPERTY_QNAME[81]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_attribute_user(PROPERTY_QNAME[81]);
            }
            target.set(colHeaderCaption);
        }
    }

    /**
     * Unsets the "colHeaderCaption" attribute
     */
    @Override
    public void unsetColHeaderCaption() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[81]);
        }
    }

    /**
     * Gets the "fieldListSortAscending" attribute
     */
    @Override
    public boolean getFieldListSortAscending() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[82]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[82]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "fieldListSortAscending" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFieldListSortAscending() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[82]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[82]);
            }
            return target;
        }
    }

    /**
     * True if has "fieldListSortAscending" attribute
     */
    @Override
    public boolean isSetFieldListSortAscending() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[82]) != null;
        }
    }

    /**
     * Sets the "fieldListSortAscending" attribute
     */
    @Override
    public void setFieldListSortAscending(boolean fieldListSortAscending) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[82]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[82]);
            }
            target.setBooleanValue(fieldListSortAscending);
        }
    }

    /**
     * Sets (as xml) the "fieldListSortAscending" attribute
     */
    @Override
    public void xsetFieldListSortAscending(org.apache.xmlbeans.XmlBoolean fieldListSortAscending) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[82]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[82]);
            }
            target.set(fieldListSortAscending);
        }
    }

    /**
     * Unsets the "fieldListSortAscending" attribute
     */
    @Override
    public void unsetFieldListSortAscending() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[82]);
        }
    }

    /**
     * Gets the "mdxSubqueries" attribute
     */
    @Override
    public boolean getMdxSubqueries() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[83]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[83]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "mdxSubqueries" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMdxSubqueries() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[83]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[83]);
            }
            return target;
        }
    }

    /**
     * True if has "mdxSubqueries" attribute
     */
    @Override
    public boolean isSetMdxSubqueries() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[83]) != null;
        }
    }

    /**
     * Sets the "mdxSubqueries" attribute
     */
    @Override
    public void setMdxSubqueries(boolean mdxSubqueries) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[83]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[83]);
            }
            target.setBooleanValue(mdxSubqueries);
        }
    }

    /**
     * Sets (as xml) the "mdxSubqueries" attribute
     */
    @Override
    public void xsetMdxSubqueries(org.apache.xmlbeans.XmlBoolean mdxSubqueries) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[83]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[83]);
            }
            target.set(mdxSubqueries);
        }
    }

    /**
     * Unsets the "mdxSubqueries" attribute
     */
    @Override
    public void unsetMdxSubqueries() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[83]);
        }
    }

    /**
     * Gets the "customListSort" attribute
     */
    @Override
    public boolean getCustomListSort() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[84]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[84]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "customListSort" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCustomListSort() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[84]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[84]);
            }
            return target;
        }
    }

    /**
     * True if has "customListSort" attribute
     */
    @Override
    public boolean isSetCustomListSort() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[84]) != null;
        }
    }

    /**
     * Sets the "customListSort" attribute
     */
    @Override
    public void setCustomListSort(boolean customListSort) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[84]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[84]);
            }
            target.setBooleanValue(customListSort);
        }
    }

    /**
     * Sets (as xml) the "customListSort" attribute
     */
    @Override
    public void xsetCustomListSort(org.apache.xmlbeans.XmlBoolean customListSort) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[84]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[84]);
            }
            target.set(customListSort);
        }
    }

    /**
     * Unsets the "customListSort" attribute
     */
    @Override
    public void unsetCustomListSort() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[84]);
        }
    }
}

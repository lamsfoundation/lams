/*
 * XML Type:  CT_Worksheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Worksheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTWorksheetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet {
    private static final long serialVersionUID = 1L;

    public CTWorksheetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dimension"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetViews"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetFormatPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cols"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetData"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetCalcPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetProtection"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "protectedRanges"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "scenarios"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "autoFilter"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sortState"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dataConsolidate"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "customSheetViews"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "mergeCells"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "phoneticPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "conditionalFormatting"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dataValidations"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "hyperlinks"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "printOptions"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pageMargins"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pageSetup"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "headerFooter"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rowBreaks"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "colBreaks"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "customProperties"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellWatches"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "ignoredErrors"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "smartTags"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "drawing"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "legacyDrawing"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "legacyDrawingHF"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "drawingHF"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "picture"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "oleObjects"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "controls"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "webPublishItems"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "tableParts"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
    };


    /**
     * Gets the "sheetPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr getSheetPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sheetPr" element
     */
    @Override
    public boolean isSetSheetPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "sheetPr" element
     */
    @Override
    public void setSheetPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr sheetPr) {
        generatedSetterHelperImpl(sheetPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr addNewSheetPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "sheetPr" element
     */
    @Override
    public void unsetSheetPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "dimension" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension getDimension() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dimension" element
     */
    @Override
    public boolean isSetDimension() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "dimension" element
     */
    @Override
    public void setDimension(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension dimension) {
        generatedSetterHelperImpl(dimension, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dimension" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension addNewDimension() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetDimension)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "dimension" element
     */
    @Override
    public void unsetDimension() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "sheetViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews getSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sheetViews" element
     */
    @Override
    public boolean isSetSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "sheetViews" element
     */
    @Override
    public void setSheetViews(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews sheetViews) {
        generatedSetterHelperImpl(sheetViews, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews addNewSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "sheetViews" element
     */
    @Override
    public void unsetSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "sheetFormatPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr getSheetFormatPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sheetFormatPr" element
     */
    @Override
    public boolean isSetSheetFormatPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "sheetFormatPr" element
     */
    @Override
    public void setSheetFormatPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr sheetFormatPr) {
        generatedSetterHelperImpl(sheetFormatPr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetFormatPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr addNewSheetFormatPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "sheetFormatPr" element
     */
    @Override
    public void unsetSheetFormatPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets a List of "cols" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols> getColsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getColsArray,
                this::setColsArray,
                this::insertNewCols,
                this::removeCols,
                this::sizeOfColsArray
            );
        }
    }

    /**
     * Gets array of all "cols" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols[] getColsArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols[0]);
    }

    /**
     * Gets ith "cols" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols getColsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cols" element
     */
    @Override
    public int sizeOfColsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "cols" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setColsArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols[] colsArray) {
        check_orphaned();
        arraySetterHelper(colsArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "cols" element
     */
    @Override
    public void setColsArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols cols) {
        generatedSetterHelperImpl(cols, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cols" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols insertNewCols(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cols" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols addNewCols() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "cols" element
     */
    @Override
    public void removeCols(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets the "sheetData" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData getSheetData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sheetData" element
     */
    @Override
    public void setSheetData(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData sheetData) {
        generatedSetterHelperImpl(sheetData, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetData" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData addNewSheetData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetData)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Gets the "sheetCalcPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr getSheetCalcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sheetCalcPr" element
     */
    @Override
    public boolean isSetSheetCalcPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "sheetCalcPr" element
     */
    @Override
    public void setSheetCalcPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr sheetCalcPr) {
        generatedSetterHelperImpl(sheetCalcPr, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetCalcPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr addNewSheetCalcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetCalcPr)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "sheetCalcPr" element
     */
    @Override
    public void unsetSheetCalcPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "sheetProtection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection getSheetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sheetProtection" element
     */
    @Override
    public boolean isSetSheetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "sheetProtection" element
     */
    @Override
    public void setSheetProtection(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection sheetProtection) {
        generatedSetterHelperImpl(sheetProtection, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetProtection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection addNewSheetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "sheetProtection" element
     */
    @Override
    public void unsetSheetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "protectedRanges" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges getProtectedRanges() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "protectedRanges" element
     */
    @Override
    public boolean isSetProtectedRanges() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "protectedRanges" element
     */
    @Override
    public void setProtectedRanges(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges protectedRanges) {
        generatedSetterHelperImpl(protectedRanges, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "protectedRanges" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges addNewProtectedRanges() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTProtectedRanges)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "protectedRanges" element
     */
    @Override
    public void unsetProtectedRanges() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "scenarios" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios getScenarios() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "scenarios" element
     */
    @Override
    public boolean isSetScenarios() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "scenarios" element
     */
    @Override
    public void setScenarios(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios scenarios) {
        generatedSetterHelperImpl(scenarios, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "scenarios" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios addNewScenarios() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTScenarios)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "scenarios" element
     */
    @Override
    public void unsetScenarios() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "autoFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter getAutoFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoFilter" element
     */
    @Override
    public boolean isSetAutoFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "autoFilter" element
     */
    @Override
    public void setAutoFilter(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter autoFilter) {
        generatedSetterHelperImpl(autoFilter, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoFilter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter addNewAutoFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "autoFilter" element
     */
    @Override
    public void unsetAutoFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "sortState" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortState getSortState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortState target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortState)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sortState" element
     */
    @Override
    public boolean isSetSortState() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "sortState" element
     */
    @Override
    public void setSortState(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortState sortState) {
        generatedSetterHelperImpl(sortState, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sortState" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortState addNewSortState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortState target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSortState)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "sortState" element
     */
    @Override
    public void unsetSortState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "dataConsolidate" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataConsolidate getDataConsolidate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataConsolidate target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataConsolidate)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dataConsolidate" element
     */
    @Override
    public boolean isSetDataConsolidate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "dataConsolidate" element
     */
    @Override
    public void setDataConsolidate(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataConsolidate dataConsolidate) {
        generatedSetterHelperImpl(dataConsolidate, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dataConsolidate" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataConsolidate addNewDataConsolidate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataConsolidate target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataConsolidate)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "dataConsolidate" element
     */
    @Override
    public void unsetDataConsolidate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "customSheetViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews getCustomSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "customSheetViews" element
     */
    @Override
    public boolean isSetCustomSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "customSheetViews" element
     */
    @Override
    public void setCustomSheetViews(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews customSheetViews) {
        generatedSetterHelperImpl(customSheetViews, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "customSheetViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews addNewCustomSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomSheetViews)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "customSheetViews" element
     */
    @Override
    public void unsetCustomSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "mergeCells" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells getMergeCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mergeCells" element
     */
    @Override
    public boolean isSetMergeCells() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "mergeCells" element
     */
    @Override
    public void setMergeCells(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells mergeCells) {
        generatedSetterHelperImpl(mergeCells, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mergeCells" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells addNewMergeCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCells)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "mergeCells" element
     */
    @Override
    public void unsetMergeCells() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "phoneticPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr getPhoneticPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "phoneticPr" element
     */
    @Override
    public boolean isSetPhoneticPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "phoneticPr" element
     */
    @Override
    public void setPhoneticPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr phoneticPr) {
        generatedSetterHelperImpl(phoneticPr, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "phoneticPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr addNewPhoneticPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPhoneticPr)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "phoneticPr" element
     */
    @Override
    public void unsetPhoneticPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets a List of "conditionalFormatting" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting> getConditionalFormattingList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getConditionalFormattingArray,
                this::setConditionalFormattingArray,
                this::insertNewConditionalFormatting,
                this::removeConditionalFormatting,
                this::sizeOfConditionalFormattingArray
            );
        }
    }

    /**
     * Gets array of all "conditionalFormatting" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting[] getConditionalFormattingArray() {
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting[0]);
    }

    /**
     * Gets ith "conditionalFormatting" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting getConditionalFormattingArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "conditionalFormatting" element
     */
    @Override
    public int sizeOfConditionalFormattingArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "conditionalFormatting" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setConditionalFormattingArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting[] conditionalFormattingArray) {
        check_orphaned();
        arraySetterHelper(conditionalFormattingArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "conditionalFormatting" element
     */
    @Override
    public void setConditionalFormattingArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting conditionalFormatting) {
        generatedSetterHelperImpl(conditionalFormatting, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "conditionalFormatting" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting insertNewConditionalFormatting(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "conditionalFormatting" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting addNewConditionalFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Removes the ith "conditionalFormatting" element
     */
    @Override
    public void removeConditionalFormatting(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets the "dataValidations" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations getDataValidations() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dataValidations" element
     */
    @Override
    public boolean isSetDataValidations() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "dataValidations" element
     */
    @Override
    public void setDataValidations(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations dataValidations) {
        generatedSetterHelperImpl(dataValidations, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dataValidations" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations addNewDataValidations() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataValidations)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "dataValidations" element
     */
    @Override
    public void unsetDataValidations() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "hyperlinks" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks getHyperlinks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hyperlinks" element
     */
    @Override
    public boolean isSetHyperlinks() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "hyperlinks" element
     */
    @Override
    public void setHyperlinks(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks hyperlinks) {
        generatedSetterHelperImpl(hyperlinks, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hyperlinks" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks addNewHyperlinks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlinks)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Unsets the "hyperlinks" element
     */
    @Override
    public void unsetHyperlinks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "printOptions" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions getPrintOptions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printOptions" element
     */
    @Override
    public boolean isSetPrintOptions() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "printOptions" element
     */
    @Override
    public void setPrintOptions(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions printOptions) {
        generatedSetterHelperImpl(printOptions, PROPERTY_QNAME[19], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printOptions" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions addNewPrintOptions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPrintOptions)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Unsets the "printOptions" element
     */
    @Override
    public void unsetPrintOptions() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "pageMargins" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins getPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pageMargins" element
     */
    @Override
    public boolean isSetPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "pageMargins" element
     */
    @Override
    public void setPageMargins(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins pageMargins) {
        generatedSetterHelperImpl(pageMargins, PROPERTY_QNAME[20], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageMargins" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins addNewPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Unsets the "pageMargins" element
     */
    @Override
    public void unsetPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets the "pageSetup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup getPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pageSetup" element
     */
    @Override
    public boolean isSetPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]) != 0;
        }
    }

    /**
     * Sets the "pageSetup" element
     */
    @Override
    public void setPageSetup(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup pageSetup) {
        generatedSetterHelperImpl(pageSetup, PROPERTY_QNAME[21], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageSetup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup addNewPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Unsets the "pageSetup" element
     */
    @Override
    public void unsetPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], 0);
        }
    }

    /**
     * Gets the "headerFooter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter getHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "headerFooter" element
     */
    @Override
    public boolean isSetHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]) != 0;
        }
    }

    /**
     * Sets the "headerFooter" element
     */
    @Override
    public void setHeaderFooter(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter headerFooter) {
        generatedSetterHelperImpl(headerFooter, PROPERTY_QNAME[22], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headerFooter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter addNewHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Unsets the "headerFooter" element
     */
    @Override
    public void unsetHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], 0);
        }
    }

    /**
     * Gets the "rowBreaks" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak getRowBreaks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rowBreaks" element
     */
    @Override
    public boolean isSetRowBreaks() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]) != 0;
        }
    }

    /**
     * Sets the "rowBreaks" element
     */
    @Override
    public void setRowBreaks(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak rowBreaks) {
        generatedSetterHelperImpl(rowBreaks, PROPERTY_QNAME[23], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rowBreaks" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak addNewRowBreaks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Unsets the "rowBreaks" element
     */
    @Override
    public void unsetRowBreaks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], 0);
        }
    }

    /**
     * Gets the "colBreaks" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak getColBreaks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "colBreaks" element
     */
    @Override
    public boolean isSetColBreaks() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]) != 0;
        }
    }

    /**
     * Sets the "colBreaks" element
     */
    @Override
    public void setColBreaks(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak colBreaks) {
        generatedSetterHelperImpl(colBreaks, PROPERTY_QNAME[24], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colBreaks" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak addNewColBreaks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageBreak)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Unsets the "colBreaks" element
     */
    @Override
    public void unsetColBreaks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], 0);
        }
    }

    /**
     * Gets the "customProperties" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties getCustomProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "customProperties" element
     */
    @Override
    public boolean isSetCustomProperties() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]) != 0;
        }
    }

    /**
     * Sets the "customProperties" element
     */
    @Override
    public void setCustomProperties(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties customProperties) {
        generatedSetterHelperImpl(customProperties, PROPERTY_QNAME[25], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "customProperties" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties addNewCustomProperties() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomProperties)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Unsets the "customProperties" element
     */
    @Override
    public void unsetCustomProperties() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], 0);
        }
    }

    /**
     * Gets the "cellWatches" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches getCellWatches() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cellWatches" element
     */
    @Override
    public boolean isSetCellWatches() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]) != 0;
        }
    }

    /**
     * Sets the "cellWatches" element
     */
    @Override
    public void setCellWatches(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches cellWatches) {
        generatedSetterHelperImpl(cellWatches, PROPERTY_QNAME[26], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cellWatches" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches addNewCellWatches() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellWatches)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Unsets the "cellWatches" element
     */
    @Override
    public void unsetCellWatches() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], 0);
        }
    }

    /**
     * Gets the "ignoredErrors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors getIgnoredErrors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors)get_store().find_element_user(PROPERTY_QNAME[27], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ignoredErrors" element
     */
    @Override
    public boolean isSetIgnoredErrors() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]) != 0;
        }
    }

    /**
     * Sets the "ignoredErrors" element
     */
    @Override
    public void setIgnoredErrors(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors ignoredErrors) {
        generatedSetterHelperImpl(ignoredErrors, PROPERTY_QNAME[27], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ignoredErrors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors addNewIgnoredErrors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Unsets the "ignoredErrors" element
     */
    @Override
    public void unsetIgnoredErrors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], 0);
        }
    }

    /**
     * Gets the "smartTags" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags getSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags)get_store().find_element_user(PROPERTY_QNAME[28], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "smartTags" element
     */
    @Override
    public boolean isSetSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]) != 0;
        }
    }

    /**
     * Sets the "smartTags" element
     */
    @Override
    public void setSmartTags(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags smartTags) {
        generatedSetterHelperImpl(smartTags, PROPERTY_QNAME[28], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "smartTags" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags addNewSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTags)get_store().add_element_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Unsets the "smartTags" element
     */
    @Override
    public void unsetSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], 0);
        }
    }

    /**
     * Gets the "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing getDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing)get_store().find_element_user(PROPERTY_QNAME[29], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "drawing" element
     */
    @Override
    public boolean isSetDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]) != 0;
        }
    }

    /**
     * Sets the "drawing" element
     */
    @Override
    public void setDrawing(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing drawing) {
        generatedSetterHelperImpl(drawing, PROPERTY_QNAME[29], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing addNewDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing)get_store().add_element_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * Unsets the "drawing" element
     */
    @Override
    public void unsetDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], 0);
        }
    }

    /**
     * Gets the "legacyDrawing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing getLegacyDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing)get_store().find_element_user(PROPERTY_QNAME[30], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "legacyDrawing" element
     */
    @Override
    public boolean isSetLegacyDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[30]) != 0;
        }
    }

    /**
     * Sets the "legacyDrawing" element
     */
    @Override
    public void setLegacyDrawing(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing legacyDrawing) {
        generatedSetterHelperImpl(legacyDrawing, PROPERTY_QNAME[30], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "legacyDrawing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing addNewLegacyDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing)get_store().add_element_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * Unsets the "legacyDrawing" element
     */
    @Override
    public void unsetLegacyDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[30], 0);
        }
    }

    /**
     * Gets the "legacyDrawingHF" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing getLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing)get_store().find_element_user(PROPERTY_QNAME[31], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "legacyDrawingHF" element
     */
    @Override
    public boolean isSetLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]) != 0;
        }
    }

    /**
     * Sets the "legacyDrawingHF" element
     */
    @Override
    public void setLegacyDrawingHF(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing legacyDrawingHF) {
        generatedSetterHelperImpl(legacyDrawingHF, PROPERTY_QNAME[31], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "legacyDrawingHF" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing addNewLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing)get_store().add_element_user(PROPERTY_QNAME[31]);
            return target;
        }
    }

    /**
     * Unsets the "legacyDrawingHF" element
     */
    @Override
    public void unsetLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], 0);
        }
    }

    /**
     * Gets the "drawingHF" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF getDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF)get_store().find_element_user(PROPERTY_QNAME[32], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "drawingHF" element
     */
    @Override
    public boolean isSetDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]) != 0;
        }
    }

    /**
     * Sets the "drawingHF" element
     */
    @Override
    public void setDrawingHF(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF drawingHF) {
        generatedSetterHelperImpl(drawingHF, PROPERTY_QNAME[32], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawingHF" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF addNewDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Unsets the "drawingHF" element
     */
    @Override
    public void unsetDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], 0);
        }
    }

    /**
     * Gets the "picture" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture getPicture() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture)get_store().find_element_user(PROPERTY_QNAME[33], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "picture" element
     */
    @Override
    public boolean isSetPicture() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]) != 0;
        }
    }

    /**
     * Sets the "picture" element
     */
    @Override
    public void setPicture(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture picture) {
        generatedSetterHelperImpl(picture, PROPERTY_QNAME[33], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "picture" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture addNewPicture() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture)get_store().add_element_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * Unsets the "picture" element
     */
    @Override
    public void unsetPicture() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], 0);
        }
    }

    /**
     * Gets the "oleObjects" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects getOleObjects() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects)get_store().find_element_user(PROPERTY_QNAME[34], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "oleObjects" element
     */
    @Override
    public boolean isSetOleObjects() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[34]) != 0;
        }
    }

    /**
     * Sets the "oleObjects" element
     */
    @Override
    public void setOleObjects(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects oleObjects) {
        generatedSetterHelperImpl(oleObjects, PROPERTY_QNAME[34], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "oleObjects" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects addNewOleObjects() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects)get_store().add_element_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * Unsets the "oleObjects" element
     */
    @Override
    public void unsetOleObjects() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[34], 0);
        }
    }

    /**
     * Gets the "controls" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls getControls() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls)get_store().find_element_user(PROPERTY_QNAME[35], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "controls" element
     */
    @Override
    public boolean isSetControls() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[35]) != 0;
        }
    }

    /**
     * Sets the "controls" element
     */
    @Override
    public void setControls(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls controls) {
        generatedSetterHelperImpl(controls, PROPERTY_QNAME[35], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "controls" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls addNewControls() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControls)get_store().add_element_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * Unsets the "controls" element
     */
    @Override
    public void unsetControls() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[35], 0);
        }
    }

    /**
     * Gets the "webPublishItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems getWebPublishItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems)get_store().find_element_user(PROPERTY_QNAME[36], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "webPublishItems" element
     */
    @Override
    public boolean isSetWebPublishItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[36]) != 0;
        }
    }

    /**
     * Sets the "webPublishItems" element
     */
    @Override
    public void setWebPublishItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems webPublishItems) {
        generatedSetterHelperImpl(webPublishItems, PROPERTY_QNAME[36], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "webPublishItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems addNewWebPublishItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems)get_store().add_element_user(PROPERTY_QNAME[36]);
            return target;
        }
    }

    /**
     * Unsets the "webPublishItems" element
     */
    @Override
    public void unsetWebPublishItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[36], 0);
        }
    }

    /**
     * Gets the "tableParts" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableParts getTableParts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableParts target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableParts)get_store().find_element_user(PROPERTY_QNAME[37], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tableParts" element
     */
    @Override
    public boolean isSetTableParts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[37]) != 0;
        }
    }

    /**
     * Sets the "tableParts" element
     */
    @Override
    public void setTableParts(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableParts tableParts) {
        generatedSetterHelperImpl(tableParts, PROPERTY_QNAME[37], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tableParts" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableParts addNewTableParts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableParts target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableParts)get_store().add_element_user(PROPERTY_QNAME[37]);
            return target;
        }
    }

    /**
     * Unsets the "tableParts" element
     */
    @Override
    public void unsetTableParts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[37], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[38], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[38]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[38], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[38]);
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
            get_store().remove_element(PROPERTY_QNAME[38], 0);
        }
    }
}

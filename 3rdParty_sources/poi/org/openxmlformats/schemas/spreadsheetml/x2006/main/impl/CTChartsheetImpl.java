/*
 * XML Type:  CT_Chartsheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Chartsheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTChartsheetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheet {
    private static final long serialVersionUID = 1L;

    public CTChartsheetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetViews"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheetProtection"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "customSheetViews"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pageMargins"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pageSetup"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "headerFooter"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "drawing"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "legacyDrawing"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "legacyDrawingHF"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "drawingHF"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "picture"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "webPublishItems"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
    };


    /**
     * Gets the "sheetPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetPr getSheetPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
    public void setSheetPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetPr sheetPr) {
        generatedSetterHelperImpl(sheetPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetPr addNewSheetPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetPr)get_store().add_element_user(PROPERTY_QNAME[0]);
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
     * Gets the "sheetViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetViews getSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetViews)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sheetViews" element
     */
    @Override
    public void setSheetViews(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetViews sheetViews) {
        generatedSetterHelperImpl(sheetViews, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetViews addNewSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetViews)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "sheetProtection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection getSheetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "sheetProtection" element
     */
    @Override
    public void setSheetProtection(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection sheetProtection) {
        generatedSetterHelperImpl(sheetProtection, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheetProtection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection addNewSheetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTChartsheetProtection)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "customSheetViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews getCustomSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "customSheetViews" element
     */
    @Override
    public void setCustomSheetViews(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews customSheetViews) {
        generatedSetterHelperImpl(customSheetViews, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "customSheetViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews addNewCustomSheetViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetViews)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "pageMargins" element
     */
    @Override
    public void setPageMargins(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins pageMargins) {
        generatedSetterHelperImpl(pageMargins, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageMargins" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins addNewPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "pageSetup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup getPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "pageSetup" element
     */
    @Override
    public void setPageSetup(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup pageSetup) {
        generatedSetterHelperImpl(pageSetup, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageSetup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup addNewPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "headerFooter" element
     */
    @Override
    public void setHeaderFooter(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter headerFooter) {
        generatedSetterHelperImpl(headerFooter, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headerFooter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter addNewHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "drawing" element
     */
    @Override
    public void setDrawing(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing drawing) {
        generatedSetterHelperImpl(drawing, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing addNewDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawing)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing)get_store().find_element_user(PROPERTY_QNAME[8], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "legacyDrawing" element
     */
    @Override
    public void setLegacyDrawing(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing legacyDrawing) {
        generatedSetterHelperImpl(legacyDrawing, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "legacyDrawing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing addNewLegacyDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing)get_store().add_element_user(PROPERTY_QNAME[8]);
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
            get_store().remove_element(PROPERTY_QNAME[8], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing)get_store().find_element_user(PROPERTY_QNAME[9], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "legacyDrawingHF" element
     */
    @Override
    public void setLegacyDrawingHF(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing legacyDrawingHF) {
        generatedSetterHelperImpl(legacyDrawingHF, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "legacyDrawingHF" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing addNewLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLegacyDrawing)get_store().add_element_user(PROPERTY_QNAME[9]);
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
            get_store().remove_element(PROPERTY_QNAME[9], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF)get_store().find_element_user(PROPERTY_QNAME[10], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "drawingHF" element
     */
    @Override
    public void setDrawingHF(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF drawingHF) {
        generatedSetterHelperImpl(drawingHF, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawingHF" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF addNewDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDrawingHF)get_store().add_element_user(PROPERTY_QNAME[10]);
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
            get_store().remove_element(PROPERTY_QNAME[10], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture)get_store().find_element_user(PROPERTY_QNAME[11], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "picture" element
     */
    @Override
    public void setPicture(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture picture) {
        generatedSetterHelperImpl(picture, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "picture" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture addNewPicture() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetBackgroundPicture)get_store().add_element_user(PROPERTY_QNAME[11]);
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
            get_store().remove_element(PROPERTY_QNAME[11], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems)get_store().find_element_user(PROPERTY_QNAME[12], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "webPublishItems" element
     */
    @Override
    public void setWebPublishItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems webPublishItems) {
        generatedSetterHelperImpl(webPublishItems, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "webPublishItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems addNewWebPublishItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItems)get_store().add_element_user(PROPERTY_QNAME[12]);
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
            get_store().remove_element(PROPERTY_QNAME[12], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[13], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[13]);
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
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }
}

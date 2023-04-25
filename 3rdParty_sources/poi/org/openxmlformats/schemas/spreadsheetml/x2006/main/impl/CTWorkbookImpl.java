/*
 * XML Type:  CT_Workbook
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Workbook(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTWorkbookImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook {
    private static final long serialVersionUID = 1L;

    public CTWorkbookImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "fileVersion"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "fileSharing"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "workbookPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "workbookProtection"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "bookViews"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "sheets"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "functionGroups"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "externalReferences"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "definedNames"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "calcPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "oleSize"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "customWorkbookViews"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotCaches"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "smartTagPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "smartTagTypes"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "webPublishing"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "fileRecoveryPr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "webPublishObjects"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
        new QName("", "conformance"),
    };


    /**
     * Gets the "fileVersion" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion getFileVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fileVersion" element
     */
    @Override
    public boolean isSetFileVersion() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "fileVersion" element
     */
    @Override
    public void setFileVersion(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion fileVersion) {
        generatedSetterHelperImpl(fileVersion, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fileVersion" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion addNewFileVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileVersion)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "fileVersion" element
     */
    @Override
    public void unsetFileVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "fileSharing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing getFileSharing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fileSharing" element
     */
    @Override
    public boolean isSetFileSharing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "fileSharing" element
     */
    @Override
    public void setFileSharing(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing fileSharing) {
        generatedSetterHelperImpl(fileSharing, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fileSharing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing addNewFileSharing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "fileSharing" element
     */
    @Override
    public void unsetFileSharing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "workbookPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr getWorkbookPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "workbookPr" element
     */
    @Override
    public boolean isSetWorkbookPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "workbookPr" element
     */
    @Override
    public void setWorkbookPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr workbookPr) {
        generatedSetterHelperImpl(workbookPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "workbookPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr addNewWorkbookPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "workbookPr" element
     */
    @Override
    public void unsetWorkbookPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "workbookProtection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection getWorkbookProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "workbookProtection" element
     */
    @Override
    public boolean isSetWorkbookProtection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "workbookProtection" element
     */
    @Override
    public void setWorkbookProtection(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection workbookProtection) {
        generatedSetterHelperImpl(workbookProtection, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "workbookProtection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection addNewWorkbookProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookProtection)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "workbookProtection" element
     */
    @Override
    public void unsetWorkbookProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "bookViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews getBookViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bookViews" element
     */
    @Override
    public boolean isSetBookViews() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "bookViews" element
     */
    @Override
    public void setBookViews(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews bookViews) {
        generatedSetterHelperImpl(bookViews, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bookViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews addNewBookViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "bookViews" element
     */
    @Override
    public void unsetBookViews() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "sheets" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets getSheets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sheets" element
     */
    @Override
    public void setSheets(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets sheets) {
        generatedSetterHelperImpl(sheets, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sheets" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets addNewSheets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheets)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Gets the "functionGroups" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups getFunctionGroups() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "functionGroups" element
     */
    @Override
    public boolean isSetFunctionGroups() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "functionGroups" element
     */
    @Override
    public void setFunctionGroups(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups functionGroups) {
        generatedSetterHelperImpl(functionGroups, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "functionGroups" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups addNewFunctionGroups() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "functionGroups" element
     */
    @Override
    public void unsetFunctionGroups() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "externalReferences" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences getExternalReferences() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "externalReferences" element
     */
    @Override
    public boolean isSetExternalReferences() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "externalReferences" element
     */
    @Override
    public void setExternalReferences(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences externalReferences) {
        generatedSetterHelperImpl(externalReferences, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "externalReferences" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences addNewExternalReferences() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "externalReferences" element
     */
    @Override
    public void unsetExternalReferences() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "definedNames" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames getDefinedNames() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "definedNames" element
     */
    @Override
    public boolean isSetDefinedNames() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "definedNames" element
     */
    @Override
    public void setDefinedNames(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames definedNames) {
        generatedSetterHelperImpl(definedNames, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "definedNames" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames addNewDefinedNames() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedNames)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "definedNames" element
     */
    @Override
    public void unsetDefinedNames() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "calcPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr getCalcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "calcPr" element
     */
    @Override
    public boolean isSetCalcPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "calcPr" element
     */
    @Override
    public void setCalcPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr calcPr) {
        generatedSetterHelperImpl(calcPr, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "calcPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr addNewCalcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcPr)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "calcPr" element
     */
    @Override
    public void unsetCalcPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "oleSize" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleSize getOleSize() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleSize target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleSize)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "oleSize" element
     */
    @Override
    public boolean isSetOleSize() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "oleSize" element
     */
    @Override
    public void setOleSize(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleSize oleSize) {
        generatedSetterHelperImpl(oleSize, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "oleSize" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleSize addNewOleSize() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleSize target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleSize)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "oleSize" element
     */
    @Override
    public void unsetOleSize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "customWorkbookViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews getCustomWorkbookViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "customWorkbookViews" element
     */
    @Override
    public boolean isSetCustomWorkbookViews() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "customWorkbookViews" element
     */
    @Override
    public void setCustomWorkbookViews(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews customWorkbookViews) {
        generatedSetterHelperImpl(customWorkbookViews, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "customWorkbookViews" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews addNewCustomWorkbookViews() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomWorkbookViews)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "customWorkbookViews" element
     */
    @Override
    public void unsetCustomWorkbookViews() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "pivotCaches" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches getPivotCaches() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pivotCaches" element
     */
    @Override
    public boolean isSetPivotCaches() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "pivotCaches" element
     */
    @Override
    public void setPivotCaches(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches pivotCaches) {
        generatedSetterHelperImpl(pivotCaches, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotCaches" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches addNewPivotCaches() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCaches)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "pivotCaches" element
     */
    @Override
    public void unsetPivotCaches() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "smartTagPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr getSmartTagPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "smartTagPr" element
     */
    @Override
    public boolean isSetSmartTagPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "smartTagPr" element
     */
    @Override
    public void setSmartTagPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr smartTagPr) {
        generatedSetterHelperImpl(smartTagPr, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "smartTagPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr addNewSmartTagPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "smartTagPr" element
     */
    @Override
    public void unsetSmartTagPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "smartTagTypes" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes getSmartTagTypes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "smartTagTypes" element
     */
    @Override
    public boolean isSetSmartTagTypes() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "smartTagTypes" element
     */
    @Override
    public void setSmartTagTypes(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes smartTagTypes) {
        generatedSetterHelperImpl(smartTagTypes, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "smartTagTypes" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes addNewSmartTagTypes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagTypes)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "smartTagTypes" element
     */
    @Override
    public void unsetSmartTagTypes() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "webPublishing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishing getWebPublishing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishing)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "webPublishing" element
     */
    @Override
    public boolean isSetWebPublishing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "webPublishing" element
     */
    @Override
    public void setWebPublishing(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishing webPublishing) {
        generatedSetterHelperImpl(webPublishing, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "webPublishing" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishing addNewWebPublishing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishing target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishing)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "webPublishing" element
     */
    @Override
    public void unsetWebPublishing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets a List of "fileRecoveryPr" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr> getFileRecoveryPrList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFileRecoveryPrArray,
                this::setFileRecoveryPrArray,
                this::insertNewFileRecoveryPr,
                this::removeFileRecoveryPr,
                this::sizeOfFileRecoveryPrArray
            );
        }
    }

    /**
     * Gets array of all "fileRecoveryPr" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr[] getFileRecoveryPrArray() {
        return getXmlObjectArray(PROPERTY_QNAME[16], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr[0]);
    }

    /**
     * Gets ith "fileRecoveryPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr getFileRecoveryPrArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "fileRecoveryPr" element
     */
    @Override
    public int sizeOfFileRecoveryPrArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "fileRecoveryPr" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFileRecoveryPrArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr[] fileRecoveryPrArray) {
        check_orphaned();
        arraySetterHelper(fileRecoveryPrArray, PROPERTY_QNAME[16]);
    }

    /**
     * Sets ith "fileRecoveryPr" element
     */
    @Override
    public void setFileRecoveryPrArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr fileRecoveryPr) {
        generatedSetterHelperImpl(fileRecoveryPr, PROPERTY_QNAME[16], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fileRecoveryPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr insertNewFileRecoveryPr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "fileRecoveryPr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr addNewFileRecoveryPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileRecoveryPr)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Removes the ith "fileRecoveryPr" element
     */
    @Override
    public void removeFileRecoveryPr(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets the "webPublishObjects" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishObjects getWebPublishObjects() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishObjects target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishObjects)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "webPublishObjects" element
     */
    @Override
    public boolean isSetWebPublishObjects() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "webPublishObjects" element
     */
    @Override
    public void setWebPublishObjects(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishObjects webPublishObjects) {
        generatedSetterHelperImpl(webPublishObjects, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "webPublishObjects" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishObjects addNewWebPublishObjects() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishObjects target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishObjects)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "webPublishObjects" element
     */
    @Override
    public void unsetWebPublishObjects() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
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
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[18], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[18]);
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
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "conformance" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum getConformance() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return (target == null) ? null : (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "conformance" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass xgetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * True if has "conformance" attribute
     */
    @Override
    public boolean isSetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "conformance" attribute
     */
    @Override
    public void setConformance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum conformance) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setEnumValue(conformance);
        }
    }

    /**
     * Sets (as xml) the "conformance" attribute
     */
    @Override
    public void xsetConformance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass conformance) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(conformance);
        }
    }

    /**
     * Unsets the "conformance" attribute
     */
    @Override
    public void unsetConformance() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }
}

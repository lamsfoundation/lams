/*
 * XML Type:  CT_Stylesheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Stylesheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTStylesheetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet {
    private static final long serialVersionUID = 1L;

    public CTStylesheetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "numFmts"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "fonts"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "fills"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "borders"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellStyleXfs"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellXfs"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "cellStyles"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dxfs"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "tableStyles"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "colors"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
    };


    /**
     * Gets the "numFmts" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts getNumFmts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numFmts" element
     */
    @Override
    public boolean isSetNumFmts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "numFmts" element
     */
    @Override
    public void setNumFmts(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts numFmts) {
        generatedSetterHelperImpl(numFmts, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numFmts" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts addNewNumFmts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmts)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "numFmts" element
     */
    @Override
    public void unsetNumFmts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "fonts" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts getFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fonts" element
     */
    @Override
    public boolean isSetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "fonts" element
     */
    @Override
    public void setFonts(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts fonts) {
        generatedSetterHelperImpl(fonts, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fonts" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts addNewFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFonts)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "fonts" element
     */
    @Override
    public void unsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "fills" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills getFills() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fills" element
     */
    @Override
    public boolean isSetFills() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "fills" element
     */
    @Override
    public void setFills(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills fills) {
        generatedSetterHelperImpl(fills, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fills" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills addNewFills() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFills)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "fills" element
     */
    @Override
    public void unsetFills() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "borders" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders getBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "borders" element
     */
    @Override
    public boolean isSetBorders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "borders" element
     */
    @Override
    public void setBorders(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders borders) {
        generatedSetterHelperImpl(borders, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "borders" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders addNewBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorders)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "borders" element
     */
    @Override
    public void unsetBorders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "cellStyleXfs" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs getCellStyleXfs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cellStyleXfs" element
     */
    @Override
    public boolean isSetCellStyleXfs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "cellStyleXfs" element
     */
    @Override
    public void setCellStyleXfs(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs cellStyleXfs) {
        generatedSetterHelperImpl(cellStyleXfs, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cellStyleXfs" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs addNewCellStyleXfs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyleXfs)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "cellStyleXfs" element
     */
    @Override
    public void unsetCellStyleXfs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "cellXfs" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs getCellXfs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cellXfs" element
     */
    @Override
    public boolean isSetCellXfs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "cellXfs" element
     */
    @Override
    public void setCellXfs(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs cellXfs) {
        generatedSetterHelperImpl(cellXfs, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cellXfs" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs addNewCellXfs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellXfs)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "cellXfs" element
     */
    @Override
    public void unsetCellXfs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "cellStyles" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles getCellStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cellStyles" element
     */
    @Override
    public boolean isSetCellStyles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "cellStyles" element
     */
    @Override
    public void setCellStyles(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles cellStyles) {
        generatedSetterHelperImpl(cellStyles, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cellStyles" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles addNewCellStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellStyles)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "cellStyles" element
     */
    @Override
    public void unsetCellStyles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "dxfs" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs getDxfs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dxfs" element
     */
    @Override
    public boolean isSetDxfs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "dxfs" element
     */
    @Override
    public void setDxfs(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs dxfs) {
        generatedSetterHelperImpl(dxfs, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dxfs" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs addNewDxfs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxfs)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "dxfs" element
     */
    @Override
    public void unsetDxfs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "tableStyles" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles getTableStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tableStyles" element
     */
    @Override
    public boolean isSetTableStyles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "tableStyles" element
     */
    @Override
    public void setTableStyles(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles tableStyles) {
        generatedSetterHelperImpl(tableStyles, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tableStyles" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles addNewTableStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyles)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "tableStyles" element
     */
    @Override
    public void unsetTableStyles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "colors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors getColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "colors" element
     */
    @Override
    public boolean isSetColors() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "colors" element
     */
    @Override
    public void setColors(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors colors) {
        generatedSetterHelperImpl(colors, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors addNewColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "colors" element
     */
    @Override
    public void unsetColors() {
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
}

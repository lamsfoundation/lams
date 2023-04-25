/*
 * XML Type:  CT_Lvl
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Lvl(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTLvlImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl {
    private static final long serialVersionUID = 1L;

    public CTLvlImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "start"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numFmt"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lvlRestart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "isLgl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suff"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lvlText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lvlPicBulletId"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "legacy"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lvlJc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ilvl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tplc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tentative"),
    };


    /**
     * Gets the "start" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "start" element
     */
    @Override
    public boolean isSetStart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "start" element
     */
    @Override
    public void setStart(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber start) {
        generatedSetterHelperImpl(start, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "start" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "start" element
     */
    @Override
    public void unsetStart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "numFmt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt getNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numFmt" element
     */
    @Override
    public boolean isSetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "numFmt" element
     */
    @Override
    public void setNumFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt numFmt) {
        generatedSetterHelperImpl(numFmt, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numFmt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt addNewNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "numFmt" element
     */
    @Override
    public void unsetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "lvlRestart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getLvlRestart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvlRestart" element
     */
    @Override
    public boolean isSetLvlRestart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "lvlRestart" element
     */
    @Override
    public void setLvlRestart(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber lvlRestart) {
        generatedSetterHelperImpl(lvlRestart, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvlRestart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewLvlRestart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "lvlRestart" element
     */
    @Override
    public void unsetLvlRestart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "pStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getPStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pStyle" element
     */
    @Override
    public boolean isSetPStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "pStyle" element
     */
    @Override
    public void setPStyle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString pStyle) {
        generatedSetterHelperImpl(pStyle, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewPStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "pStyle" element
     */
    @Override
    public void unsetPStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "isLgl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getIsLgl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "isLgl" element
     */
    @Override
    public boolean isSetIsLgl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "isLgl" element
     */
    @Override
    public void setIsLgl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff isLgl) {
        generatedSetterHelperImpl(isLgl, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "isLgl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewIsLgl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "isLgl" element
     */
    @Override
    public void unsetIsLgl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "suff" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelSuffix getSuff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelSuffix target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelSuffix)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suff" element
     */
    @Override
    public boolean isSetSuff() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "suff" element
     */
    @Override
    public void setSuff(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelSuffix suff) {
        generatedSetterHelperImpl(suff, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suff" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelSuffix addNewSuff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelSuffix target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelSuffix)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "suff" element
     */
    @Override
    public void unsetSuff() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "lvlText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelText getLvlText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelText)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvlText" element
     */
    @Override
    public boolean isSetLvlText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "lvlText" element
     */
    @Override
    public void setLvlText(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelText lvlText) {
        generatedSetterHelperImpl(lvlText, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvlText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelText addNewLvlText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelText target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLevelText)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "lvlText" element
     */
    @Override
    public void unsetLvlText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "lvlPicBulletId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getLvlPicBulletId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvlPicBulletId" element
     */
    @Override
    public boolean isSetLvlPicBulletId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "lvlPicBulletId" element
     */
    @Override
    public void setLvlPicBulletId(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber lvlPicBulletId) {
        generatedSetterHelperImpl(lvlPicBulletId, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvlPicBulletId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewLvlPicBulletId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "lvlPicBulletId" element
     */
    @Override
    public void unsetLvlPicBulletId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "legacy" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy getLegacy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "legacy" element
     */
    @Override
    public boolean isSetLegacy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "legacy" element
     */
    @Override
    public void setLegacy(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy legacy) {
        generatedSetterHelperImpl(legacy, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "legacy" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy addNewLegacy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvlLegacy)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "legacy" element
     */
    @Override
    public void unsetLegacy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "lvlJc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc getLvlJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lvlJc" element
     */
    @Override
    public boolean isSetLvlJc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "lvlJc" element
     */
    @Override
    public void setLvlJc(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc lvlJc) {
        generatedSetterHelperImpl(lvlJc, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lvlJc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc addNewLvlJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "lvlJc" element
     */
    @Override
    public void unsetLvlJc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral getPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pPr" element
     */
    @Override
    public boolean isSetPPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "pPr" element
     */
    @Override
    public void setPPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral pPr) {
        generatedSetterHelperImpl(pPr, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral addNewPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "pPr" element
     */
    @Override
    public void unsetPPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr getRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rPr" element
     */
    @Override
    public boolean isSetRPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "rPr" element
     */
    @Override
    public void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr rPr) {
        generatedSetterHelperImpl(rPr, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr addNewRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "rPr" element
     */
    @Override
    public void unsetRPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "ilvl" attribute
     */
    @Override
    public java.math.BigInteger getIlvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "ilvl" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetIlvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Sets the "ilvl" attribute
     */
    @Override
    public void setIlvl(java.math.BigInteger ilvl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setBigIntegerValue(ilvl);
        }
    }

    /**
     * Sets (as xml) the "ilvl" attribute
     */
    @Override
    public void xsetIlvl(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber ilvl) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(ilvl);
        }
    }

    /**
     * Gets the "tplc" attribute
     */
    @Override
    public byte[] getTplc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "tplc" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetTplc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * True if has "tplc" attribute
     */
    @Override
    public boolean isSetTplc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "tplc" attribute
     */
    @Override
    public void setTplc(byte[] tplc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setByteArrayValue(tplc);
        }
    }

    /**
     * Sets (as xml) the "tplc" attribute
     */
    @Override
    public void xsetTplc(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber tplc) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(tplc);
        }
    }

    /**
     * Unsets the "tplc" attribute
     */
    @Override
    public void unsetTplc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "tentative" attribute
     */
    @Override
    public java.lang.Object getTentative() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "tentative" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetTentative() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * True if has "tentative" attribute
     */
    @Override
    public boolean isSetTentative() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "tentative" attribute
     */
    @Override
    public void setTentative(java.lang.Object tentative) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setObjectValue(tentative);
        }
    }

    /**
     * Sets (as xml) the "tentative" attribute
     */
    @Override
    public void xsetTentative(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff tentative) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(tentative);
        }
    }

    /**
     * Unsets the "tentative" attribute
     */
    @Override
    public void unsetTentative() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }
}

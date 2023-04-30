/*
 * XML Type:  CT_PPrBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PPrBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPPrBaseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase {
    private static final long serialVersionUID = 1L;

    public CTPPrBaseImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "keepNext"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "keepLines"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pageBreakBefore"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "framePr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "widowControl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suppressLineNumbers"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pBdr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "shd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tabs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suppressAutoHyphens"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "kinsoku"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "wordWrap"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "overflowPunct"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "topLinePunct"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autoSpaceDE"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autoSpaceDN"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bidi"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "adjustRightInd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "snapToGrid"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "spacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ind"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "contextualSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "mirrorIndents"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suppressOverlap"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "jc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "textDirection"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "textAlignment"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "textboxTightWrap"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "outlineLvl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "divId"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cnfStyle"),
    };


    /**
     * Gets the "pStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getPStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pStyle" element
     */
    @Override
    public void setPStyle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString pStyle) {
        generatedSetterHelperImpl(pStyle, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewPStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "keepNext" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getKeepNext() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "keepNext" element
     */
    @Override
    public boolean isSetKeepNext() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "keepNext" element
     */
    @Override
    public void setKeepNext(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff keepNext) {
        generatedSetterHelperImpl(keepNext, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "keepNext" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewKeepNext() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "keepNext" element
     */
    @Override
    public void unsetKeepNext() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "keepLines" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getKeepLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "keepLines" element
     */
    @Override
    public boolean isSetKeepLines() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "keepLines" element
     */
    @Override
    public void setKeepLines(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff keepLines) {
        generatedSetterHelperImpl(keepLines, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "keepLines" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewKeepLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "keepLines" element
     */
    @Override
    public void unsetKeepLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "pageBreakBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPageBreakBefore() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pageBreakBefore" element
     */
    @Override
    public boolean isSetPageBreakBefore() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "pageBreakBefore" element
     */
    @Override
    public void setPageBreakBefore(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff pageBreakBefore) {
        generatedSetterHelperImpl(pageBreakBefore, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageBreakBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPageBreakBefore() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "pageBreakBefore" element
     */
    @Override
    public void unsetPageBreakBefore() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "framePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr getFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "framePr" element
     */
    @Override
    public boolean isSetFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "framePr" element
     */
    @Override
    public void setFramePr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr framePr) {
        generatedSetterHelperImpl(framePr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "framePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr addNewFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "framePr" element
     */
    @Override
    public void unsetFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "widowControl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getWidowControl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "widowControl" element
     */
    @Override
    public boolean isSetWidowControl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "widowControl" element
     */
    @Override
    public void setWidowControl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff widowControl) {
        generatedSetterHelperImpl(widowControl, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "widowControl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewWidowControl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "widowControl" element
     */
    @Override
    public void unsetWidowControl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "numPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr getNumPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numPr" element
     */
    @Override
    public boolean isSetNumPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "numPr" element
     */
    @Override
    public void setNumPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr numPr) {
        generatedSetterHelperImpl(numPr, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr addNewNumPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "numPr" element
     */
    @Override
    public void unsetNumPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "suppressLineNumbers" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSuppressLineNumbers() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suppressLineNumbers" element
     */
    @Override
    public boolean isSetSuppressLineNumbers() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "suppressLineNumbers" element
     */
    @Override
    public void setSuppressLineNumbers(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff suppressLineNumbers) {
        generatedSetterHelperImpl(suppressLineNumbers, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suppressLineNumbers" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSuppressLineNumbers() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "suppressLineNumbers" element
     */
    @Override
    public void unsetSuppressLineNumbers() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "pBdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr getPBdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pBdr" element
     */
    @Override
    public boolean isSetPBdr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "pBdr" element
     */
    @Override
    public void setPBdr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr pBdr) {
        generatedSetterHelperImpl(pBdr, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pBdr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr addNewPBdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "pBdr" element
     */
    @Override
    public void unsetPBdr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "shd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd getShd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "shd" element
     */
    @Override
    public boolean isSetShd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "shd" element
     */
    @Override
    public void setShd(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd shd) {
        generatedSetterHelperImpl(shd, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "shd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd addNewShd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "shd" element
     */
    @Override
    public void unsetShd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "tabs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs getTabs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tabs" element
     */
    @Override
    public boolean isSetTabs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "tabs" element
     */
    @Override
    public void setTabs(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs tabs) {
        generatedSetterHelperImpl(tabs, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tabs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs addNewTabs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "tabs" element
     */
    @Override
    public void unsetTabs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "suppressAutoHyphens" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSuppressAutoHyphens() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suppressAutoHyphens" element
     */
    @Override
    public boolean isSetSuppressAutoHyphens() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "suppressAutoHyphens" element
     */
    @Override
    public void setSuppressAutoHyphens(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff suppressAutoHyphens) {
        generatedSetterHelperImpl(suppressAutoHyphens, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suppressAutoHyphens" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSuppressAutoHyphens() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "suppressAutoHyphens" element
     */
    @Override
    public void unsetSuppressAutoHyphens() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "kinsoku" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getKinsoku() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "kinsoku" element
     */
    @Override
    public boolean isSetKinsoku() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "kinsoku" element
     */
    @Override
    public void setKinsoku(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff kinsoku) {
        generatedSetterHelperImpl(kinsoku, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "kinsoku" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewKinsoku() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "kinsoku" element
     */
    @Override
    public void unsetKinsoku() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "wordWrap" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getWordWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wordWrap" element
     */
    @Override
    public boolean isSetWordWrap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "wordWrap" element
     */
    @Override
    public void setWordWrap(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff wordWrap) {
        generatedSetterHelperImpl(wordWrap, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wordWrap" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewWordWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "wordWrap" element
     */
    @Override
    public void unsetWordWrap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "overflowPunct" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getOverflowPunct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "overflowPunct" element
     */
    @Override
    public boolean isSetOverflowPunct() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "overflowPunct" element
     */
    @Override
    public void setOverflowPunct(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff overflowPunct) {
        generatedSetterHelperImpl(overflowPunct, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "overflowPunct" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewOverflowPunct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "overflowPunct" element
     */
    @Override
    public void unsetOverflowPunct() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "topLinePunct" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getTopLinePunct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "topLinePunct" element
     */
    @Override
    public boolean isSetTopLinePunct() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "topLinePunct" element
     */
    @Override
    public void setTopLinePunct(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff topLinePunct) {
        generatedSetterHelperImpl(topLinePunct, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "topLinePunct" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewTopLinePunct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "topLinePunct" element
     */
    @Override
    public void unsetTopLinePunct() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "autoSpaceDE" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAutoSpaceDE() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoSpaceDE" element
     */
    @Override
    public boolean isSetAutoSpaceDE() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "autoSpaceDE" element
     */
    @Override
    public void setAutoSpaceDE(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff autoSpaceDE) {
        generatedSetterHelperImpl(autoSpaceDE, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoSpaceDE" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAutoSpaceDE() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Unsets the "autoSpaceDE" element
     */
    @Override
    public void unsetAutoSpaceDE() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "autoSpaceDN" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAutoSpaceDN() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoSpaceDN" element
     */
    @Override
    public boolean isSetAutoSpaceDN() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "autoSpaceDN" element
     */
    @Override
    public void setAutoSpaceDN(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff autoSpaceDN) {
        generatedSetterHelperImpl(autoSpaceDN, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoSpaceDN" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAutoSpaceDN() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "autoSpaceDN" element
     */
    @Override
    public void unsetAutoSpaceDN() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "bidi" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBidi() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bidi" element
     */
    @Override
    public boolean isSetBidi() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "bidi" element
     */
    @Override
    public void setBidi(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bidi) {
        generatedSetterHelperImpl(bidi, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bidi" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBidi() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Unsets the "bidi" element
     */
    @Override
    public void unsetBidi() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "adjustRightInd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAdjustRightInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "adjustRightInd" element
     */
    @Override
    public boolean isSetAdjustRightInd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "adjustRightInd" element
     */
    @Override
    public void setAdjustRightInd(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff adjustRightInd) {
        generatedSetterHelperImpl(adjustRightInd, PROPERTY_QNAME[19], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "adjustRightInd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAdjustRightInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Unsets the "adjustRightInd" element
     */
    @Override
    public void unsetAdjustRightInd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "snapToGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSnapToGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "snapToGrid" element
     */
    @Override
    public boolean isSetSnapToGrid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "snapToGrid" element
     */
    @Override
    public void setSnapToGrid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff snapToGrid) {
        generatedSetterHelperImpl(snapToGrid, PROPERTY_QNAME[20], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "snapToGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSnapToGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Unsets the "snapToGrid" element
     */
    @Override
    public void unsetSnapToGrid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets the "spacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing getSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spacing" element
     */
    @Override
    public boolean isSetSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]) != 0;
        }
    }

    /**
     * Sets the "spacing" element
     */
    @Override
    public void setSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing spacing) {
        generatedSetterHelperImpl(spacing, PROPERTY_QNAME[21], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing addNewSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Unsets the "spacing" element
     */
    @Override
    public void unsetSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], 0);
        }
    }

    /**
     * Gets the "ind" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd getInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ind" element
     */
    @Override
    public boolean isSetInd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]) != 0;
        }
    }

    /**
     * Sets the "ind" element
     */
    @Override
    public void setInd(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd ind) {
        generatedSetterHelperImpl(ind, PROPERTY_QNAME[22], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ind" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd addNewInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Unsets the "ind" element
     */
    @Override
    public void unsetInd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], 0);
        }
    }

    /**
     * Gets the "contextualSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getContextualSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "contextualSpacing" element
     */
    @Override
    public boolean isSetContextualSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]) != 0;
        }
    }

    /**
     * Sets the "contextualSpacing" element
     */
    @Override
    public void setContextualSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff contextualSpacing) {
        generatedSetterHelperImpl(contextualSpacing, PROPERTY_QNAME[23], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "contextualSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewContextualSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Unsets the "contextualSpacing" element
     */
    @Override
    public void unsetContextualSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], 0);
        }
    }

    /**
     * Gets the "mirrorIndents" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getMirrorIndents() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mirrorIndents" element
     */
    @Override
    public boolean isSetMirrorIndents() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]) != 0;
        }
    }

    /**
     * Sets the "mirrorIndents" element
     */
    @Override
    public void setMirrorIndents(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff mirrorIndents) {
        generatedSetterHelperImpl(mirrorIndents, PROPERTY_QNAME[24], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mirrorIndents" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewMirrorIndents() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Unsets the "mirrorIndents" element
     */
    @Override
    public void unsetMirrorIndents() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], 0);
        }
    }

    /**
     * Gets the "suppressOverlap" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSuppressOverlap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suppressOverlap" element
     */
    @Override
    public boolean isSetSuppressOverlap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]) != 0;
        }
    }

    /**
     * Sets the "suppressOverlap" element
     */
    @Override
    public void setSuppressOverlap(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff suppressOverlap) {
        generatedSetterHelperImpl(suppressOverlap, PROPERTY_QNAME[25], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suppressOverlap" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSuppressOverlap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Unsets the "suppressOverlap" element
     */
    @Override
    public void unsetSuppressOverlap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], 0);
        }
    }

    /**
     * Gets the "jc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc getJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "jc" element
     */
    @Override
    public boolean isSetJc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]) != 0;
        }
    }

    /**
     * Sets the "jc" element
     */
    @Override
    public void setJc(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc jc) {
        generatedSetterHelperImpl(jc, PROPERTY_QNAME[26], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "jc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc addNewJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Unsets the "jc" element
     */
    @Override
    public void unsetJc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], 0);
        }
    }

    /**
     * Gets the "textDirection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection getTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection)get_store().find_element_user(PROPERTY_QNAME[27], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "textDirection" element
     */
    @Override
    public boolean isSetTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]) != 0;
        }
    }

    /**
     * Sets the "textDirection" element
     */
    @Override
    public void setTextDirection(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection textDirection) {
        generatedSetterHelperImpl(textDirection, PROPERTY_QNAME[27], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "textDirection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection addNewTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Unsets the "textDirection" element
     */
    @Override
    public void unsetTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], 0);
        }
    }

    /**
     * Gets the "textAlignment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment getTextAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment)get_store().find_element_user(PROPERTY_QNAME[28], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "textAlignment" element
     */
    @Override
    public boolean isSetTextAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]) != 0;
        }
    }

    /**
     * Sets the "textAlignment" element
     */
    @Override
    public void setTextAlignment(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment textAlignment) {
        generatedSetterHelperImpl(textAlignment, PROPERTY_QNAME[28], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "textAlignment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment addNewTextAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment)get_store().add_element_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Unsets the "textAlignment" element
     */
    @Override
    public void unsetTextAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], 0);
        }
    }

    /**
     * Gets the "textboxTightWrap" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextboxTightWrap getTextboxTightWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextboxTightWrap target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextboxTightWrap)get_store().find_element_user(PROPERTY_QNAME[29], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "textboxTightWrap" element
     */
    @Override
    public boolean isSetTextboxTightWrap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]) != 0;
        }
    }

    /**
     * Sets the "textboxTightWrap" element
     */
    @Override
    public void setTextboxTightWrap(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextboxTightWrap textboxTightWrap) {
        generatedSetterHelperImpl(textboxTightWrap, PROPERTY_QNAME[29], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "textboxTightWrap" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextboxTightWrap addNewTextboxTightWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextboxTightWrap target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextboxTightWrap)get_store().add_element_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * Unsets the "textboxTightWrap" element
     */
    @Override
    public void unsetTextboxTightWrap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], 0);
        }
    }

    /**
     * Gets the "outlineLvl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getOutlineLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[30], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "outlineLvl" element
     */
    @Override
    public boolean isSetOutlineLvl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[30]) != 0;
        }
    }

    /**
     * Sets the "outlineLvl" element
     */
    @Override
    public void setOutlineLvl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber outlineLvl) {
        generatedSetterHelperImpl(outlineLvl, PROPERTY_QNAME[30], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "outlineLvl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewOutlineLvl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * Unsets the "outlineLvl" element
     */
    @Override
    public void unsetOutlineLvl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[30], 0);
        }
    }

    /**
     * Gets the "divId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getDivId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[31], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "divId" element
     */
    @Override
    public boolean isSetDivId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]) != 0;
        }
    }

    /**
     * Sets the "divId" element
     */
    @Override
    public void setDivId(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber divId) {
        generatedSetterHelperImpl(divId, PROPERTY_QNAME[31], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "divId" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewDivId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[31]);
            return target;
        }
    }

    /**
     * Unsets the "divId" element
     */
    @Override
    public void unsetDivId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], 0);
        }
    }

    /**
     * Gets the "cnfStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf getCnfStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf)get_store().find_element_user(PROPERTY_QNAME[32], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cnfStyle" element
     */
    @Override
    public boolean isSetCnfStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]) != 0;
        }
    }

    /**
     * Sets the "cnfStyle" element
     */
    @Override
    public void setCnfStyle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf cnfStyle) {
        generatedSetterHelperImpl(cnfStyle, PROPERTY_QNAME[32], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cnfStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf addNewCnfStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Unsets the "cnfStyle" element
     */
    @Override
    public void unsetCnfStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], 0);
        }
    }
}

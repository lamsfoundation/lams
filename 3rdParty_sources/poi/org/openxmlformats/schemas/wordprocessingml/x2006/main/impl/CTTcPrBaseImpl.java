/*
 * XML Type:  CT_TcPrBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TcPrBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTcPrBaseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrBase {
    private static final long serialVersionUID = 1L;

    public CTTcPrBaseImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cnfStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tcW"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "gridSpan"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hMerge"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vMerge"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tcBorders"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "shd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noWrap"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tcMar"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "textDirection"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tcFitText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vAlign"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hideMark"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "headers"),
    };


    /**
     * Gets the "cnfStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf getCnfStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "cnfStyle" element
     */
    @Override
    public void setCnfStyle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf cnfStyle) {
        generatedSetterHelperImpl(cnfStyle, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cnfStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf addNewCnfStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCnf)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "tcW" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth getTcW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcW" element
     */
    @Override
    public boolean isSetTcW() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "tcW" element
     */
    @Override
    public void setTcW(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth tcW) {
        generatedSetterHelperImpl(tcW, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcW" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth addNewTcW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "tcW" element
     */
    @Override
    public void unsetTcW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "gridSpan" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getGridSpan() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "gridSpan" element
     */
    @Override
    public boolean isSetGridSpan() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "gridSpan" element
     */
    @Override
    public void setGridSpan(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber gridSpan) {
        generatedSetterHelperImpl(gridSpan, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gridSpan" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewGridSpan() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "gridSpan" element
     */
    @Override
    public void unsetGridSpan() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "hMerge" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge getHMerge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hMerge" element
     */
    @Override
    public boolean isSetHMerge() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "hMerge" element
     */
    @Override
    public void setHMerge(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge hMerge) {
        generatedSetterHelperImpl(hMerge, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hMerge" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge addNewHMerge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "hMerge" element
     */
    @Override
    public void unsetHMerge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "vMerge" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge getVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "vMerge" element
     */
    @Override
    public boolean isSetVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "vMerge" element
     */
    @Override
    public void setVMerge(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge vMerge) {
        generatedSetterHelperImpl(vMerge, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "vMerge" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge addNewVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "vMerge" element
     */
    @Override
    public void unsetVMerge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "tcBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders getTcBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcBorders" element
     */
    @Override
    public boolean isSetTcBorders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "tcBorders" element
     */
    @Override
    public void setTcBorders(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders tcBorders) {
        generatedSetterHelperImpl(tcBorders, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders addNewTcBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "tcBorders" element
     */
    @Override
    public void unsetTcBorders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "shd" element
     */
    @Override
    public void setShd(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd shd) {
        generatedSetterHelperImpl(shd, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "shd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd addNewShd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "noWrap" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noWrap" element
     */
    @Override
    public boolean isSetNoWrap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "noWrap" element
     */
    @Override
    public void setNoWrap(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noWrap) {
        generatedSetterHelperImpl(noWrap, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noWrap" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "noWrap" element
     */
    @Override
    public void unsetNoWrap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "tcMar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar getTcMar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcMar" element
     */
    @Override
    public boolean isSetTcMar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "tcMar" element
     */
    @Override
    public void setTcMar(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar tcMar) {
        generatedSetterHelperImpl(tcMar, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcMar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar addNewTcMar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "tcMar" element
     */
    @Override
    public void unsetTcMar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection)get_store().find_element_user(PROPERTY_QNAME[9], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "textDirection" element
     */
    @Override
    public void setTextDirection(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection textDirection) {
        generatedSetterHelperImpl(textDirection, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "textDirection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection addNewTextDirection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection)get_store().add_element_user(PROPERTY_QNAME[9]);
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
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "tcFitText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getTcFitText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcFitText" element
     */
    @Override
    public boolean isSetTcFitText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "tcFitText" element
     */
    @Override
    public void setTcFitText(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff tcFitText) {
        generatedSetterHelperImpl(tcFitText, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcFitText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewTcFitText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "tcFitText" element
     */
    @Override
    public void unsetTcFitText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "vAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc getVAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "vAlign" element
     */
    @Override
    public boolean isSetVAlign() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "vAlign" element
     */
    @Override
    public void setVAlign(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc vAlign) {
        generatedSetterHelperImpl(vAlign, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "vAlign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc addNewVAlign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "vAlign" element
     */
    @Override
    public void unsetVAlign() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "hideMark" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getHideMark() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hideMark" element
     */
    @Override
    public boolean isSetHideMark() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "hideMark" element
     */
    @Override
    public void setHideMark(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff hideMark) {
        generatedSetterHelperImpl(hideMark, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hideMark" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewHideMark() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "hideMark" element
     */
    @Override
    public void unsetHideMark() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "headers" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders getHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "headers" element
     */
    @Override
    public boolean isSetHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "headers" element
     */
    @Override
    public void setHeaders(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders headers) {
        generatedSetterHelperImpl(headers, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headers" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders addNewHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "headers" element
     */
    @Override
    public void unsetHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }
}

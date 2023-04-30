/*
 * XML Type:  CT_TblPrExBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblPrExBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblPrExBaseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase {
    private static final long serialVersionUID = 1L;

    public CTTblPrExBaseImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblW"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "jc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblCellSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblInd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblBorders"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "shd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblLayout"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblCellMar"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblLook"),
    };


    /**
     * Gets the "tblW" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth getTblW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblW" element
     */
    @Override
    public boolean isSetTblW() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tblW" element
     */
    @Override
    public void setTblW(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth tblW) {
        generatedSetterHelperImpl(tblW, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblW" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth addNewTblW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tblW" element
     */
    @Override
    public void unsetTblW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "jc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable getJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "jc" element
     */
    @Override
    public void setJc(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable jc) {
        generatedSetterHelperImpl(jc, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "jc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable addNewJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJcTable)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "tblCellSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth getTblCellSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblCellSpacing" element
     */
    @Override
    public boolean isSetTblCellSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "tblCellSpacing" element
     */
    @Override
    public void setTblCellSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth tblCellSpacing) {
        generatedSetterHelperImpl(tblCellSpacing, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblCellSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth addNewTblCellSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "tblCellSpacing" element
     */
    @Override
    public void unsetTblCellSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "tblInd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth getTblInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblInd" element
     */
    @Override
    public boolean isSetTblInd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "tblInd" element
     */
    @Override
    public void setTblInd(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth tblInd) {
        generatedSetterHelperImpl(tblInd, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblInd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth addNewTblInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "tblInd" element
     */
    @Override
    public void unsetTblInd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "tblBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders getTblBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblBorders" element
     */
    @Override
    public boolean isSetTblBorders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "tblBorders" element
     */
    @Override
    public void setTblBorders(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders tblBorders) {
        generatedSetterHelperImpl(tblBorders, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders addNewTblBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "tblBorders" element
     */
    @Override
    public void unsetTblBorders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
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
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "shd" element
     */
    @Override
    public void setShd(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd shd) {
        generatedSetterHelperImpl(shd, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "shd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd addNewShd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "tblLayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType getTblLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblLayout" element
     */
    @Override
    public boolean isSetTblLayout() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "tblLayout" element
     */
    @Override
    public void setTblLayout(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType tblLayout) {
        generatedSetterHelperImpl(tblLayout, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblLayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType addNewTblLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "tblLayout" element
     */
    @Override
    public void unsetTblLayout() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "tblCellMar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar getTblCellMar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblCellMar" element
     */
    @Override
    public boolean isSetTblCellMar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "tblCellMar" element
     */
    @Override
    public void setTblCellMar(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar tblCellMar) {
        generatedSetterHelperImpl(tblCellMar, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblCellMar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar addNewTblCellMar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "tblCellMar" element
     */
    @Override
    public void unsetTblCellMar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "tblLook" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLook getTblLook() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLook target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLook)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblLook" element
     */
    @Override
    public boolean isSetTblLook() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "tblLook" element
     */
    @Override
    public void setTblLook(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLook tblLook) {
        generatedSetterHelperImpl(tblLook, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblLook" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLook addNewTblLook() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLook target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLook)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "tblLook" element
     */
    @Override
    public void unsetTblLook() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }
}

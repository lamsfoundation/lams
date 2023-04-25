/*
 * XML Type:  CT_DPr
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTDPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DPr(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTDPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTDPr {
    private static final long serialVersionUID = 1L;

    public CTDPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "begChr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "sepChr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "endChr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "grow"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "shp"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "ctrlPr"),
    };


    /**
     * Gets the "begChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTChar getBegChr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTChar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTChar)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "begChr" element
     */
    @Override
    public boolean isSetBegChr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "begChr" element
     */
    @Override
    public void setBegChr(org.openxmlformats.schemas.officeDocument.x2006.math.CTChar begChr) {
        generatedSetterHelperImpl(begChr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "begChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTChar addNewBegChr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTChar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTChar)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "begChr" element
     */
    @Override
    public void unsetBegChr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "sepChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTChar getSepChr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTChar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTChar)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sepChr" element
     */
    @Override
    public boolean isSetSepChr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "sepChr" element
     */
    @Override
    public void setSepChr(org.openxmlformats.schemas.officeDocument.x2006.math.CTChar sepChr) {
        generatedSetterHelperImpl(sepChr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sepChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTChar addNewSepChr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTChar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTChar)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "sepChr" element
     */
    @Override
    public void unsetSepChr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "endChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTChar getEndChr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTChar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTChar)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "endChr" element
     */
    @Override
    public boolean isSetEndChr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "endChr" element
     */
    @Override
    public void setEndChr(org.openxmlformats.schemas.officeDocument.x2006.math.CTChar endChr) {
        generatedSetterHelperImpl(endChr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endChr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTChar addNewEndChr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTChar target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTChar)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "endChr" element
     */
    @Override
    public void unsetEndChr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "grow" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff getGrow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "grow" element
     */
    @Override
    public boolean isSetGrow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "grow" element
     */
    @Override
    public void setGrow(org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff grow) {
        generatedSetterHelperImpl(grow, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "grow" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff addNewGrow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "grow" element
     */
    @Override
    public void unsetGrow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "shp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTShp getShp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTShp target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTShp)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "shp" element
     */
    @Override
    public boolean isSetShp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "shp" element
     */
    @Override
    public void setShp(org.openxmlformats.schemas.officeDocument.x2006.math.CTShp shp) {
        generatedSetterHelperImpl(shp, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "shp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTShp addNewShp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTShp target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTShp)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "shp" element
     */
    @Override
    public void unsetShp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "ctrlPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr getCtrlPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ctrlPr" element
     */
    @Override
    public boolean isSetCtrlPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "ctrlPr" element
     */
    @Override
    public void setCtrlPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr ctrlPr) {
        generatedSetterHelperImpl(ctrlPr, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ctrlPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr addNewCtrlPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "ctrlPr" element
     */
    @Override
    public void unsetCtrlPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }
}

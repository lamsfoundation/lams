/*
 * XML Type:  CT_MPr
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MPr(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTMPrImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTMPr {
    private static final long serialVersionUID = 1L;

    public CTMPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "baseJc"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "plcHide"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "rSpRule"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "cGpRule"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "rSp"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "cSp"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "cGp"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "mcs"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "ctrlPr"),
    };


    /**
     * Gets the "baseJc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTYAlign getBaseJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTYAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTYAlign)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "baseJc" element
     */
    @Override
    public boolean isSetBaseJc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "baseJc" element
     */
    @Override
    public void setBaseJc(org.openxmlformats.schemas.officeDocument.x2006.math.CTYAlign baseJc) {
        generatedSetterHelperImpl(baseJc, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "baseJc" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTYAlign addNewBaseJc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTYAlign target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTYAlign)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "baseJc" element
     */
    @Override
    public void unsetBaseJc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "plcHide" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff getPlcHide() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "plcHide" element
     */
    @Override
    public boolean isSetPlcHide() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "plcHide" element
     */
    @Override
    public void setPlcHide(org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff plcHide) {
        generatedSetterHelperImpl(plcHide, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "plcHide" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff addNewPlcHide() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "plcHide" element
     */
    @Override
    public void unsetPlcHide() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "rSpRule" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule getRSpRule() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rSpRule" element
     */
    @Override
    public boolean isSetRSpRule() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "rSpRule" element
     */
    @Override
    public void setRSpRule(org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule rSpRule) {
        generatedSetterHelperImpl(rSpRule, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rSpRule" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule addNewRSpRule() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "rSpRule" element
     */
    @Override
    public void unsetRSpRule() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "cGpRule" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule getCGpRule() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cGpRule" element
     */
    @Override
    public boolean isSetCGpRule() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "cGpRule" element
     */
    @Override
    public void setCGpRule(org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule cGpRule) {
        generatedSetterHelperImpl(cGpRule, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cGpRule" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule addNewCGpRule() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTSpacingRule)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "cGpRule" element
     */
    @Override
    public void unsetCGpRule() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "rSp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger getRSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rSp" element
     */
    @Override
    public boolean isSetRSp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "rSp" element
     */
    @Override
    public void setRSp(org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger rSp) {
        generatedSetterHelperImpl(rSp, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rSp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger addNewRSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "rSp" element
     */
    @Override
    public void unsetRSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "cSp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger getCSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cSp" element
     */
    @Override
    public boolean isSetCSp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "cSp" element
     */
    @Override
    public void setCSp(org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger cSp) {
        generatedSetterHelperImpl(cSp, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cSp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger addNewCSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "cSp" element
     */
    @Override
    public void unsetCSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "cGp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger getCGp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cGp" element
     */
    @Override
    public boolean isSetCGp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "cGp" element
     */
    @Override
    public void setCGp(org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger cGp) {
        generatedSetterHelperImpl(cGp, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cGp" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger addNewCGp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTUnSignedInteger)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "cGp" element
     */
    @Override
    public void unsetCGp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "mcs" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS getMcs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mcs" element
     */
    @Override
    public boolean isSetMcs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "mcs" element
     */
    @Override
    public void setMcs(org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS mcs) {
        generatedSetterHelperImpl(mcs, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mcs" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS addNewMcs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMCS)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "mcs" element
     */
    @Override
    public void unsetMcs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
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
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr)get_store().find_element_user(PROPERTY_QNAME[8], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "ctrlPr" element
     */
    @Override
    public void setCtrlPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr ctrlPr) {
        generatedSetterHelperImpl(ctrlPr, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ctrlPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr addNewCtrlPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr)get_store().add_element_user(PROPERTY_QNAME[8]);
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
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }
}

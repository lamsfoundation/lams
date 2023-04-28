/*
 * XML Type:  CT_PresentationOf
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PresentationOf(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTPresentationOfImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTPresentationOf {
    private static final long serialVersionUID = 1L;

    public CTPresentationOfImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "extLst"),
        new QName("", "axis"),
        new QName("", "ptType"),
        new QName("", "hideLastTrans"),
        new QName("", "st"),
        new QName("", "cnt"),
        new QName("", "step"),
    };


    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "axis" attribute
     */
    @Override
    public java.util.List getAxis() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "axis" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STAxisTypes xgetAxis() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STAxisTypes target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STAxisTypes)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STAxisTypes)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "axis" attribute
     */
    @Override
    public boolean isSetAxis() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "axis" attribute
     */
    @Override
    public void setAxis(java.util.List axis) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setListValue(axis);
        }
    }

    /**
     * Sets (as xml) the "axis" attribute
     */
    @Override
    public void xsetAxis(org.openxmlformats.schemas.drawingml.x2006.diagram.STAxisTypes axis) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STAxisTypes target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STAxisTypes)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STAxisTypes)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(axis);
        }
    }

    /**
     * Unsets the "axis" attribute
     */
    @Override
    public void unsetAxis() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "ptType" attribute
     */
    @Override
    public java.util.List getPtType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "ptType" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes xgetPtType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "ptType" attribute
     */
    @Override
    public boolean isSetPtType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "ptType" attribute
     */
    @Override
    public void setPtType(java.util.List ptType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setListValue(ptType);
        }
    }

    /**
     * Sets (as xml) the "ptType" attribute
     */
    @Override
    public void xsetPtType(org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes ptType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STElementTypes)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(ptType);
        }
    }

    /**
     * Unsets the "ptType" attribute
     */
    @Override
    public void unsetPtType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "hideLastTrans" attribute
     */
    @Override
    public java.util.List getHideLastTrans() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "hideLastTrans" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STBooleans xgetHideLastTrans() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STBooleans target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STBooleans)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STBooleans)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "hideLastTrans" attribute
     */
    @Override
    public boolean isSetHideLastTrans() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "hideLastTrans" attribute
     */
    @Override
    public void setHideLastTrans(java.util.List hideLastTrans) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setListValue(hideLastTrans);
        }
    }

    /**
     * Sets (as xml) the "hideLastTrans" attribute
     */
    @Override
    public void xsetHideLastTrans(org.openxmlformats.schemas.drawingml.x2006.diagram.STBooleans hideLastTrans) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STBooleans target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STBooleans)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STBooleans)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(hideLastTrans);
        }
    }

    /**
     * Unsets the "hideLastTrans" attribute
     */
    @Override
    public void unsetHideLastTrans() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "st" attribute
     */
    @Override
    public java.util.List getSt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "st" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STInts xgetSt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STInts target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STInts)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STInts)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "st" attribute
     */
    @Override
    public boolean isSetSt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "st" attribute
     */
    @Override
    public void setSt(java.util.List st) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setListValue(st);
        }
    }

    /**
     * Sets (as xml) the "st" attribute
     */
    @Override
    public void xsetSt(org.openxmlformats.schemas.drawingml.x2006.diagram.STInts st) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STInts target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STInts)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STInts)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(st);
        }
    }

    /**
     * Unsets the "st" attribute
     */
    @Override
    public void unsetSt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "cnt" attribute
     */
    @Override
    public java.util.List getCnt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "cnt" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts xgetCnt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "cnt" attribute
     */
    @Override
    public boolean isSetCnt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "cnt" attribute
     */
    @Override
    public void setCnt(java.util.List cnt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setListValue(cnt);
        }
    }

    /**
     * Sets (as xml) the "cnt" attribute
     */
    @Override
    public void xsetCnt(org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts cnt) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STUnsignedInts)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(cnt);
        }
    }

    /**
     * Unsets the "cnt" attribute
     */
    @Override
    public void unsetCnt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "step" attribute
     */
    @Override
    public java.util.List getStep() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? null : target.getListValue();
        }
    }

    /**
     * Gets (as xml) the "step" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STInts xgetStep() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STInts target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STInts)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STInts)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "step" attribute
     */
    @Override
    public boolean isSetStep() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "step" attribute
     */
    @Override
    public void setStep(java.util.List step) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setListValue(step);
        }
    }

    /**
     * Sets (as xml) the "step" attribute
     */
    @Override
    public void xsetStep(org.openxmlformats.schemas.drawingml.x2006.diagram.STInts step) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STInts target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STInts)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STInts)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(step);
        }
    }

    /**
     * Unsets the "step" attribute
     */
    @Override
    public void unsetStep() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}

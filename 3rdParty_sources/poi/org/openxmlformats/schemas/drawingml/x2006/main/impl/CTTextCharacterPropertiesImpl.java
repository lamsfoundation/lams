/*
 * XML Type:  CT_TextCharacterProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextCharacterProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextCharacterPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties {
    private static final long serialVersionUID = 1L;

    public CTTextCharacterPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ln"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "noFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "solidFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gradFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blipFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pattFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "grpFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectDag"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "highlight"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "uLnTx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "uLn"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "uFillTx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "uFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "latin"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ea"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cs"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sym"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hlinkClick"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hlinkMouseOver"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "rtl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "kumimoji"),
        new QName("", "lang"),
        new QName("", "altLang"),
        new QName("", "sz"),
        new QName("", "b"),
        new QName("", "i"),
        new QName("", "u"),
        new QName("", "strike"),
        new QName("", "kern"),
        new QName("", "cap"),
        new QName("", "spc"),
        new QName("", "normalizeH"),
        new QName("", "baseline"),
        new QName("", "noProof"),
        new QName("", "dirty"),
        new QName("", "err"),
        new QName("", "smtClean"),
        new QName("", "smtId"),
        new QName("", "bmk"),
    };


    /**
     * Gets the "ln" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ln" element
     */
    @Override
    public boolean isSetLn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "ln" element
     */
    @Override
    public void setLn(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties ln) {
        generatedSetterHelperImpl(ln, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ln" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "ln" element
     */
    @Override
    public void unsetLn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "noFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties getNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noFill" element
     */
    @Override
    public boolean isSetNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "noFill" element
     */
    @Override
    public void setNoFill(org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties noFill) {
        generatedSetterHelperImpl(noFill, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties addNewNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "noFill" element
     */
    @Override
    public void unsetNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "solidFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties getSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "solidFill" element
     */
    @Override
    public boolean isSetSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "solidFill" element
     */
    @Override
    public void setSolidFill(org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties solidFill) {
        generatedSetterHelperImpl(solidFill, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "solidFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties addNewSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "solidFill" element
     */
    @Override
    public void unsetSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "gradFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties getGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "gradFill" element
     */
    @Override
    public boolean isSetGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "gradFill" element
     */
    @Override
    public void setGradFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties gradFill) {
        generatedSetterHelperImpl(gradFill, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gradFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties addNewGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "gradFill" element
     */
    @Override
    public void unsetGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "blipFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties getBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "blipFill" element
     */
    @Override
    public boolean isSetBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "blipFill" element
     */
    @Override
    public void setBlipFill(org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties blipFill) {
        generatedSetterHelperImpl(blipFill, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "blipFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties addNewBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "blipFill" element
     */
    @Override
    public void unsetBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "pattFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties getPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pattFill" element
     */
    @Override
    public boolean isSetPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "pattFill" element
     */
    @Override
    public void setPattFill(org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties pattFill) {
        generatedSetterHelperImpl(pattFill, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pattFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties addNewPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "pattFill" element
     */
    @Override
    public void unsetPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "grpFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties getGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "grpFill" element
     */
    @Override
    public boolean isSetGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "grpFill" element
     */
    @Override
    public void setGrpFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties grpFill) {
        generatedSetterHelperImpl(grpFill, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "grpFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties addNewGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "grpFill" element
     */
    @Override
    public void unsetGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "effectLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList getEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "effectLst" element
     */
    @Override
    public boolean isSetEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "effectLst" element
     */
    @Override
    public void setEffectLst(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectLst) {
        generatedSetterHelperImpl(effectLst, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList addNewEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "effectLst" element
     */
    @Override
    public void unsetEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "effectDag" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer getEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "effectDag" element
     */
    @Override
    public boolean isSetEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "effectDag" element
     */
    @Override
    public void setEffectDag(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer effectDag) {
        generatedSetterHelperImpl(effectDag, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectDag" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer addNewEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "effectDag" element
     */
    @Override
    public void unsetEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "highlight" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor getHighlight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "highlight" element
     */
    @Override
    public boolean isSetHighlight() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "highlight" element
     */
    @Override
    public void setHighlight(org.openxmlformats.schemas.drawingml.x2006.main.CTColor highlight) {
        generatedSetterHelperImpl(highlight, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "highlight" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewHighlight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "highlight" element
     */
    @Override
    public void unsetHighlight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "uLnTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineLineFollowText getULnTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineLineFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineLineFollowText)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "uLnTx" element
     */
    @Override
    public boolean isSetULnTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "uLnTx" element
     */
    @Override
    public void setULnTx(org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineLineFollowText uLnTx) {
        generatedSetterHelperImpl(uLnTx, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "uLnTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineLineFollowText addNewULnTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineLineFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineLineFollowText)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "uLnTx" element
     */
    @Override
    public void unsetULnTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "uLn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getULn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "uLn" element
     */
    @Override
    public boolean isSetULn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "uLn" element
     */
    @Override
    public void setULn(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties uLn) {
        generatedSetterHelperImpl(uLn, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "uLn" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewULn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "uLn" element
     */
    @Override
    public void unsetULn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "uFillTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillFollowText getUFillTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillFollowText)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "uFillTx" element
     */
    @Override
    public boolean isSetUFillTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "uFillTx" element
     */
    @Override
    public void setUFillTx(org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillFollowText uFillTx) {
        generatedSetterHelperImpl(uFillTx, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "uFillTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillFollowText addNewUFillTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillFollowText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillFollowText)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "uFillTx" element
     */
    @Override
    public void unsetUFillTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "uFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillGroupWrapper getUFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillGroupWrapper target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillGroupWrapper)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "uFill" element
     */
    @Override
    public boolean isSetUFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "uFill" element
     */
    @Override
    public void setUFill(org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillGroupWrapper uFill) {
        generatedSetterHelperImpl(uFill, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "uFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillGroupWrapper addNewUFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillGroupWrapper target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextUnderlineFillGroupWrapper)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "uFill" element
     */
    @Override
    public void unsetUFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "latin" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getLatin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "latin" element
     */
    @Override
    public boolean isSetLatin() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "latin" element
     */
    @Override
    public void setLatin(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont latin) {
        generatedSetterHelperImpl(latin, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "latin" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewLatin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "latin" element
     */
    @Override
    public void unsetLatin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "ea" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getEa() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ea" element
     */
    @Override
    public boolean isSetEa() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "ea" element
     */
    @Override
    public void setEa(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont ea) {
        generatedSetterHelperImpl(ea, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ea" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewEa() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "ea" element
     */
    @Override
    public void unsetEa() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "cs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cs" element
     */
    @Override
    public boolean isSetCs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "cs" element
     */
    @Override
    public void setCs(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont cs) {
        generatedSetterHelperImpl(cs, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewCs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Unsets the "cs" element
     */
    @Override
    public void unsetCs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "sym" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getSym() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sym" element
     */
    @Override
    public boolean isSetSym() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "sym" element
     */
    @Override
    public void setSym(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont sym) {
        generatedSetterHelperImpl(sym, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sym" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewSym() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "sym" element
     */
    @Override
    public void unsetSym() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "hlinkClick" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink getHlinkClick() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hlinkClick" element
     */
    @Override
    public boolean isSetHlinkClick() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "hlinkClick" element
     */
    @Override
    public void setHlinkClick(org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink hlinkClick) {
        generatedSetterHelperImpl(hlinkClick, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hlinkClick" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink addNewHlinkClick() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Unsets the "hlinkClick" element
     */
    @Override
    public void unsetHlinkClick() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "hlinkMouseOver" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink getHlinkMouseOver() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hlinkMouseOver" element
     */
    @Override
    public boolean isSetHlinkMouseOver() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "hlinkMouseOver" element
     */
    @Override
    public void setHlinkMouseOver(org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink hlinkMouseOver) {
        generatedSetterHelperImpl(hlinkMouseOver, PROPERTY_QNAME[19], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hlinkMouseOver" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink addNewHlinkMouseOver() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHyperlink)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Unsets the "hlinkMouseOver" element
     */
    @Override
    public void unsetHlinkMouseOver() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "rtl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBoolean getRtl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rtl" element
     */
    @Override
    public boolean isSetRtl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "rtl" element
     */
    @Override
    public void setRtl(org.openxmlformats.schemas.drawingml.x2006.main.CTBoolean rtl) {
        generatedSetterHelperImpl(rtl, PROPERTY_QNAME[20], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rtl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBoolean addNewRtl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Unsets the "rtl" element
     */
    @Override
    public void unsetRtl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[21], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[21]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[21], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[21]);
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
            get_store().remove_element(PROPERTY_QNAME[21], 0);
        }
    }

    /**
     * Gets the "kumimoji" attribute
     */
    @Override
    public boolean getKumimoji() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "kumimoji" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetKumimoji() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "kumimoji" attribute
     */
    @Override
    public boolean isSetKumimoji() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "kumimoji" attribute
     */
    @Override
    public void setKumimoji(boolean kumimoji) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setBooleanValue(kumimoji);
        }
    }

    /**
     * Sets (as xml) the "kumimoji" attribute
     */
    @Override
    public void xsetKumimoji(org.apache.xmlbeans.XmlBoolean kumimoji) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(kumimoji);
        }
    }

    /**
     * Unsets the "kumimoji" attribute
     */
    @Override
    public void unsetKumimoji() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "lang" attribute
     */
    @Override
    public java.lang.String getLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "lang" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang xgetLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * True if has "lang" attribute
     */
    @Override
    public boolean isSetLang() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "lang" attribute
     */
    @Override
    public void setLang(java.lang.String lang) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setStringValue(lang);
        }
    }

    /**
     * Sets (as xml) the "lang" attribute
     */
    @Override
    public void xsetLang(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang lang) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(lang);
        }
    }

    /**
     * Unsets the "lang" attribute
     */
    @Override
    public void unsetLang() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "altLang" attribute
     */
    @Override
    public java.lang.String getAltLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "altLang" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang xgetAltLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * True if has "altLang" attribute
     */
    @Override
    public boolean isSetAltLang() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "altLang" attribute
     */
    @Override
    public void setAltLang(java.lang.String altLang) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setStringValue(altLang);
        }
    }

    /**
     * Sets (as xml) the "altLang" attribute
     */
    @Override
    public void xsetAltLang(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang altLang) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(altLang);
        }
    }

    /**
     * Unsets the "altLang" attribute
     */
    @Override
    public void unsetAltLang() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "sz" attribute
     */
    @Override
    public int getSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "sz" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize xgetSz() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "sz" attribute
     */
    @Override
    public boolean isSetSz() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "sz" attribute
     */
    @Override
    public void setSz(int sz) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setIntValue(sz);
        }
    }

    /**
     * Sets (as xml) the "sz" attribute
     */
    @Override
    public void xsetSz(org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize sz) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontSize)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(sz);
        }
    }

    /**
     * Unsets the "sz" attribute
     */
    @Override
    public void unsetSz() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "b" attribute
     */
    @Override
    public boolean getB() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "b" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetB() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * True if has "b" attribute
     */
    @Override
    public boolean isSetB() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "b" attribute
     */
    @Override
    public void setB(boolean b) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setBooleanValue(b);
        }
    }

    /**
     * Sets (as xml) the "b" attribute
     */
    @Override
    public void xsetB(org.apache.xmlbeans.XmlBoolean b) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(b);
        }
    }

    /**
     * Unsets the "b" attribute
     */
    @Override
    public void unsetB() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Gets the "i" attribute
     */
    @Override
    public boolean getI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "i" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * True if has "i" attribute
     */
    @Override
    public boolean isSetI() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[27]) != null;
        }
    }

    /**
     * Sets the "i" attribute
     */
    @Override
    public void setI(boolean iValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.setBooleanValue(iValue);
        }
    }

    /**
     * Sets (as xml) the "i" attribute
     */
    @Override
    public void xsetI(org.apache.xmlbeans.XmlBoolean iValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.set(iValue);
        }
    }

    /**
     * Unsets the "i" attribute
     */
    @Override
    public void unsetI() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Gets the "u" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType.Enum getU() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "u" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType xgetU() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * True if has "u" attribute
     */
    @Override
    public boolean isSetU() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[28]) != null;
        }
    }

    /**
     * Sets the "u" attribute
     */
    @Override
    public void setU(org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType.Enum u) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.setEnumValue(u);
        }
    }

    /**
     * Sets (as xml) the "u" attribute
     */
    @Override
    public void xsetU(org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType u) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextUnderlineType)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.set(u);
        }
    }

    /**
     * Unsets the "u" attribute
     */
    @Override
    public void unsetU() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Gets the "strike" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType.Enum getStrike() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "strike" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType xgetStrike() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * True if has "strike" attribute
     */
    @Override
    public boolean isSetStrike() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[29]) != null;
        }
    }

    /**
     * Sets the "strike" attribute
     */
    @Override
    public void setStrike(org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType.Enum strike) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.setEnumValue(strike);
        }
    }

    /**
     * Sets (as xml) the "strike" attribute
     */
    @Override
    public void xsetStrike(org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType strike) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextStrikeType)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.set(strike);
        }
    }

    /**
     * Unsets the "strike" attribute
     */
    @Override
    public void unsetStrike() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[29]);
        }
    }

    /**
     * Gets the "kern" attribute
     */
    @Override
    public int getKern() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "kern" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextNonNegativePoint xgetKern() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextNonNegativePoint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextNonNegativePoint)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * True if has "kern" attribute
     */
    @Override
    public boolean isSetKern() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[30]) != null;
        }
    }

    /**
     * Sets the "kern" attribute
     */
    @Override
    public void setKern(int kern) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[30]);
            }
            target.setIntValue(kern);
        }
    }

    /**
     * Sets (as xml) the "kern" attribute
     */
    @Override
    public void xsetKern(org.openxmlformats.schemas.drawingml.x2006.main.STTextNonNegativePoint kern) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextNonNegativePoint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextNonNegativePoint)get_store().find_attribute_user(PROPERTY_QNAME[30]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextNonNegativePoint)get_store().add_attribute_user(PROPERTY_QNAME[30]);
            }
            target.set(kern);
        }
    }

    /**
     * Unsets the "kern" attribute
     */
    @Override
    public void unsetKern() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[30]);
        }
    }

    /**
     * Gets the "cap" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType.Enum getCap() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[31]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "cap" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType xgetCap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType)get_default_attribute_value(PROPERTY_QNAME[31]);
            }
            return target;
        }
    }

    /**
     * True if has "cap" attribute
     */
    @Override
    public boolean isSetCap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[31]) != null;
        }
    }

    /**
     * Sets the "cap" attribute
     */
    @Override
    public void setCap(org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType.Enum cap) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[31]);
            }
            target.setEnumValue(cap);
        }
    }

    /**
     * Sets (as xml) the "cap" attribute
     */
    @Override
    public void xsetCap(org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType cap) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType)get_store().find_attribute_user(PROPERTY_QNAME[31]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextCapsType)get_store().add_attribute_user(PROPERTY_QNAME[31]);
            }
            target.set(cap);
        }
    }

    /**
     * Unsets the "cap" attribute
     */
    @Override
    public void unsetCap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[31]);
        }
    }

    /**
     * Gets the "spc" attribute
     */
    @Override
    public java.lang.Object getSpc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "spc" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextPoint xgetSpc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextPoint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextPoint)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * True if has "spc" attribute
     */
    @Override
    public boolean isSetSpc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[32]) != null;
        }
    }

    /**
     * Sets the "spc" attribute
     */
    @Override
    public void setSpc(java.lang.Object spc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[32]);
            }
            target.setObjectValue(spc);
        }
    }

    /**
     * Sets (as xml) the "spc" attribute
     */
    @Override
    public void xsetSpc(org.openxmlformats.schemas.drawingml.x2006.main.STTextPoint spc) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextPoint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextPoint)get_store().find_attribute_user(PROPERTY_QNAME[32]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextPoint)get_store().add_attribute_user(PROPERTY_QNAME[32]);
            }
            target.set(spc);
        }
    }

    /**
     * Unsets the "spc" attribute
     */
    @Override
    public void unsetSpc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[32]);
        }
    }

    /**
     * Gets the "normalizeH" attribute
     */
    @Override
    public boolean getNormalizeH() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "normalizeH" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetNormalizeH() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * True if has "normalizeH" attribute
     */
    @Override
    public boolean isSetNormalizeH() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[33]) != null;
        }
    }

    /**
     * Sets the "normalizeH" attribute
     */
    @Override
    public void setNormalizeH(boolean normalizeH) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[33]);
            }
            target.setBooleanValue(normalizeH);
        }
    }

    /**
     * Sets (as xml) the "normalizeH" attribute
     */
    @Override
    public void xsetNormalizeH(org.apache.xmlbeans.XmlBoolean normalizeH) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[33]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[33]);
            }
            target.set(normalizeH);
        }
    }

    /**
     * Unsets the "normalizeH" attribute
     */
    @Override
    public void unsetNormalizeH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[33]);
        }
    }

    /**
     * Gets the "baseline" attribute
     */
    @Override
    public java.lang.Object getBaseline() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "baseline" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetBaseline() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * True if has "baseline" attribute
     */
    @Override
    public boolean isSetBaseline() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[34]) != null;
        }
    }

    /**
     * Sets the "baseline" attribute
     */
    @Override
    public void setBaseline(java.lang.Object baseline) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[34]);
            }
            target.setObjectValue(baseline);
        }
    }

    /**
     * Sets (as xml) the "baseline" attribute
     */
    @Override
    public void xsetBaseline(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage baseline) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().find_attribute_user(PROPERTY_QNAME[34]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPercentage)get_store().add_attribute_user(PROPERTY_QNAME[34]);
            }
            target.set(baseline);
        }
    }

    /**
     * Unsets the "baseline" attribute
     */
    @Override
    public void unsetBaseline() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[34]);
        }
    }

    /**
     * Gets the "noProof" attribute
     */
    @Override
    public boolean getNoProof() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "noProof" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetNoProof() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * True if has "noProof" attribute
     */
    @Override
    public boolean isSetNoProof() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[35]) != null;
        }
    }

    /**
     * Sets the "noProof" attribute
     */
    @Override
    public void setNoProof(boolean noProof) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[35]);
            }
            target.setBooleanValue(noProof);
        }
    }

    /**
     * Sets (as xml) the "noProof" attribute
     */
    @Override
    public void xsetNoProof(org.apache.xmlbeans.XmlBoolean noProof) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[35]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[35]);
            }
            target.set(noProof);
        }
    }

    /**
     * Unsets the "noProof" attribute
     */
    @Override
    public void unsetNoProof() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[35]);
        }
    }

    /**
     * Gets the "dirty" attribute
     */
    @Override
    public boolean getDirty() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[36]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "dirty" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[36]);
            }
            return target;
        }
    }

    /**
     * True if has "dirty" attribute
     */
    @Override
    public boolean isSetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[36]) != null;
        }
    }

    /**
     * Sets the "dirty" attribute
     */
    @Override
    public void setDirty(boolean dirty) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[36]);
            }
            target.setBooleanValue(dirty);
        }
    }

    /**
     * Sets (as xml) the "dirty" attribute
     */
    @Override
    public void xsetDirty(org.apache.xmlbeans.XmlBoolean dirty) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[36]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[36]);
            }
            target.set(dirty);
        }
    }

    /**
     * Unsets the "dirty" attribute
     */
    @Override
    public void unsetDirty() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[36]);
        }
    }

    /**
     * Gets the "err" attribute
     */
    @Override
    public boolean getErr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[37]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "err" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetErr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[37]);
            }
            return target;
        }
    }

    /**
     * True if has "err" attribute
     */
    @Override
    public boolean isSetErr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[37]) != null;
        }
    }

    /**
     * Sets the "err" attribute
     */
    @Override
    public void setErr(boolean err) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[37]);
            }
            target.setBooleanValue(err);
        }
    }

    /**
     * Sets (as xml) the "err" attribute
     */
    @Override
    public void xsetErr(org.apache.xmlbeans.XmlBoolean err) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[37]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[37]);
            }
            target.set(err);
        }
    }

    /**
     * Unsets the "err" attribute
     */
    @Override
    public void unsetErr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[37]);
        }
    }

    /**
     * Gets the "smtClean" attribute
     */
    @Override
    public boolean getSmtClean() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[38]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "smtClean" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSmtClean() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[38]);
            }
            return target;
        }
    }

    /**
     * True if has "smtClean" attribute
     */
    @Override
    public boolean isSetSmtClean() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[38]) != null;
        }
    }

    /**
     * Sets the "smtClean" attribute
     */
    @Override
    public void setSmtClean(boolean smtClean) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[38]);
            }
            target.setBooleanValue(smtClean);
        }
    }

    /**
     * Sets (as xml) the "smtClean" attribute
     */
    @Override
    public void xsetSmtClean(org.apache.xmlbeans.XmlBoolean smtClean) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[38]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[38]);
            }
            target.set(smtClean);
        }
    }

    /**
     * Unsets the "smtClean" attribute
     */
    @Override
    public void unsetSmtClean() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[38]);
        }
    }

    /**
     * Gets the "smtId" attribute
     */
    @Override
    public long getSmtId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[39]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "smtId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetSmtId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[39]);
            }
            return target;
        }
    }

    /**
     * True if has "smtId" attribute
     */
    @Override
    public boolean isSetSmtId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[39]) != null;
        }
    }

    /**
     * Sets the "smtId" attribute
     */
    @Override
    public void setSmtId(long smtId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[39]);
            }
            target.setLongValue(smtId);
        }
    }

    /**
     * Sets (as xml) the "smtId" attribute
     */
    @Override
    public void xsetSmtId(org.apache.xmlbeans.XmlUnsignedInt smtId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[39]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[39]);
            }
            target.set(smtId);
        }
    }

    /**
     * Unsets the "smtId" attribute
     */
    @Override
    public void unsetSmtId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[39]);
        }
    }

    /**
     * Gets the "bmk" attribute
     */
    @Override
    public java.lang.String getBmk() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "bmk" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetBmk() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            return target;
        }
    }

    /**
     * True if has "bmk" attribute
     */
    @Override
    public boolean isSetBmk() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[40]) != null;
        }
    }

    /**
     * Sets the "bmk" attribute
     */
    @Override
    public void setBmk(java.lang.String bmk) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[40]);
            }
            target.setStringValue(bmk);
        }
    }

    /**
     * Sets (as xml) the "bmk" attribute
     */
    @Override
    public void xsetBmk(org.apache.xmlbeans.XmlString bmk) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[40]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[40]);
            }
            target.set(bmk);
        }
    }

    /**
     * Unsets the "bmk" attribute
     */
    @Override
    public void unsetBmk() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[40]);
        }
    }
}

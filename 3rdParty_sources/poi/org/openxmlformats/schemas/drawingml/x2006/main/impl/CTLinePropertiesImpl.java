/*
 * XML Type:  CT_LineProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_LineProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTLinePropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties {
    private static final long serialVersionUID = 1L;

    public CTLinePropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "noFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "solidFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gradFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pattFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "prstDash"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "custDash"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "round"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "bevel"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "miter"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "headEnd"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tailEnd"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "w"),
        new QName("", "cap"),
        new QName("", "cmpd"),
        new QName("", "algn"),
    };


    /**
     * Gets the "noFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties getNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "noFill" element
     */
    @Override
    public void setNoFill(org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties noFill) {
        generatedSetterHelperImpl(noFill, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties addNewNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "solidFill" element
     */
    @Override
    public void setSolidFill(org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties solidFill) {
        generatedSetterHelperImpl(solidFill, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "solidFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties addNewSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "gradFill" element
     */
    @Override
    public void setGradFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties gradFill) {
        generatedSetterHelperImpl(gradFill, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gradFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties addNewGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "pattFill" element
     */
    @Override
    public void setPattFill(org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties pattFill) {
        generatedSetterHelperImpl(pattFill, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pattFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties addNewPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "prstDash" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties getPrstDash() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "prstDash" element
     */
    @Override
    public boolean isSetPrstDash() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "prstDash" element
     */
    @Override
    public void setPrstDash(org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties prstDash) {
        generatedSetterHelperImpl(prstDash, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "prstDash" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties addNewPrstDash() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "prstDash" element
     */
    @Override
    public void unsetPrstDash() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "custDash" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList getCustDash() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "custDash" element
     */
    @Override
    public boolean isSetCustDash() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "custDash" element
     */
    @Override
    public void setCustDash(org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList custDash) {
        generatedSetterHelperImpl(custDash, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "custDash" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList addNewCustDash() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDashStopList)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "custDash" element
     */
    @Override
    public void unsetCustDash() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "round" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinRound getRound() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinRound target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinRound)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "round" element
     */
    @Override
    public boolean isSetRound() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "round" element
     */
    @Override
    public void setRound(org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinRound round) {
        generatedSetterHelperImpl(round, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "round" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinRound addNewRound() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinRound target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinRound)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "round" element
     */
    @Override
    public void unsetRound() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "bevel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinBevel getBevel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinBevel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinBevel)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bevel" element
     */
    @Override
    public boolean isSetBevel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "bevel" element
     */
    @Override
    public void setBevel(org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinBevel bevel) {
        generatedSetterHelperImpl(bevel, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bevel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinBevel addNewBevel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinBevel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinBevel)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "bevel" element
     */
    @Override
    public void unsetBevel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "miter" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties getMiter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "miter" element
     */
    @Override
    public boolean isSetMiter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "miter" element
     */
    @Override
    public void setMiter(org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties miter) {
        generatedSetterHelperImpl(miter, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "miter" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties addNewMiter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "miter" element
     */
    @Override
    public void unsetMiter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "headEnd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties getHeadEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "headEnd" element
     */
    @Override
    public boolean isSetHeadEnd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "headEnd" element
     */
    @Override
    public void setHeadEnd(org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties headEnd) {
        generatedSetterHelperImpl(headEnd, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headEnd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties addNewHeadEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "headEnd" element
     */
    @Override
    public void unsetHeadEnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "tailEnd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties getTailEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tailEnd" element
     */
    @Override
    public boolean isSetTailEnd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "tailEnd" element
     */
    @Override
    public void setTailEnd(org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties tailEnd) {
        generatedSetterHelperImpl(tailEnd, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tailEnd" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties addNewTailEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "tailEnd" element
     */
    @Override
    public void unsetTailEnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[11], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[11]);
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
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "w" attribute
     */
    @Override
    public int getW() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "w" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STLineWidth xgetW() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STLineWidth target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STLineWidth)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "w" attribute
     */
    @Override
    public boolean isSetW() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "w" attribute
     */
    @Override
    public void setW(int w) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setIntValue(w);
        }
    }

    /**
     * Sets (as xml) the "w" attribute
     */
    @Override
    public void xsetW(org.openxmlformats.schemas.drawingml.x2006.main.STLineWidth w) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STLineWidth target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STLineWidth)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STLineWidth)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(w);
        }
    }

    /**
     * Unsets the "w" attribute
     */
    @Override
    public void unsetW() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "cap" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STLineCap.Enum getCap() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STLineCap.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "cap" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STLineCap xgetCap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STLineCap target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STLineCap)get_store().find_attribute_user(PROPERTY_QNAME[13]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "cap" attribute
     */
    @Override
    public void setCap(org.openxmlformats.schemas.drawingml.x2006.main.STLineCap.Enum cap) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setEnumValue(cap);
        }
    }

    /**
     * Sets (as xml) the "cap" attribute
     */
    @Override
    public void xsetCap(org.openxmlformats.schemas.drawingml.x2006.main.STLineCap cap) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STLineCap target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STLineCap)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STLineCap)get_store().add_attribute_user(PROPERTY_QNAME[13]);
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
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "cmpd" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine.Enum getCmpd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "cmpd" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine xgetCmpd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * True if has "cmpd" attribute
     */
    @Override
    public boolean isSetCmpd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "cmpd" attribute
     */
    @Override
    public void setCmpd(org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine.Enum cmpd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setEnumValue(cmpd);
        }
    }

    /**
     * Sets (as xml) the "cmpd" attribute
     */
    @Override
    public void xsetCmpd(org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine cmpd) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCompoundLine)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(cmpd);
        }
    }

    /**
     * Unsets the "cmpd" attribute
     */
    @Override
    public void unsetCmpd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "algn" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment.Enum getAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "algn" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment xgetAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * True if has "algn" attribute
     */
    @Override
    public boolean isSetAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "algn" attribute
     */
    @Override
    public void setAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment.Enum algn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setEnumValue(algn);
        }
    }

    /**
     * Sets (as xml) the "algn" attribute
     */
    @Override
    public void xsetAlgn(org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment algn) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPenAlignment)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(algn);
        }
    }

    /**
     * Unsets the "algn" attribute
     */
    @Override
    public void unsetAlgn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }
}

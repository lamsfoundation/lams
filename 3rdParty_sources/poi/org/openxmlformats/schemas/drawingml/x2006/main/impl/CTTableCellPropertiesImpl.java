/*
 * XML Type:  CT_TableCellProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TableCellProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTableCellPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellProperties {
    private static final long serialVersionUID = 1L;

    public CTTableCellPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnL"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnR"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnT"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnB"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnTlToBr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnBlToTr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cell3D"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "noFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "solidFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gradFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blipFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pattFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "grpFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "headers"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "marL"),
        new QName("", "marR"),
        new QName("", "marT"),
        new QName("", "marB"),
        new QName("", "vert"),
        new QName("", "anchor"),
        new QName("", "anchorCtr"),
        new QName("", "horzOverflow"),
    };


    /**
     * Gets the "lnL" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLnL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnL" element
     */
    @Override
    public boolean isSetLnL() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "lnL" element
     */
    @Override
    public void setLnL(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lnL) {
        generatedSetterHelperImpl(lnL, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnL" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLnL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "lnL" element
     */
    @Override
    public void unsetLnL() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "lnR" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLnR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnR" element
     */
    @Override
    public boolean isSetLnR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "lnR" element
     */
    @Override
    public void setLnR(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lnR) {
        generatedSetterHelperImpl(lnR, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnR" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLnR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "lnR" element
     */
    @Override
    public void unsetLnR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "lnT" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLnT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnT" element
     */
    @Override
    public boolean isSetLnT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "lnT" element
     */
    @Override
    public void setLnT(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lnT) {
        generatedSetterHelperImpl(lnT, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnT" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLnT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "lnT" element
     */
    @Override
    public void unsetLnT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "lnB" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLnB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnB" element
     */
    @Override
    public boolean isSetLnB() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "lnB" element
     */
    @Override
    public void setLnB(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lnB) {
        generatedSetterHelperImpl(lnB, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnB" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLnB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "lnB" element
     */
    @Override
    public void unsetLnB() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "lnTlToBr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLnTlToBr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnTlToBr" element
     */
    @Override
    public boolean isSetLnTlToBr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "lnTlToBr" element
     */
    @Override
    public void setLnTlToBr(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lnTlToBr) {
        generatedSetterHelperImpl(lnTlToBr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnTlToBr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLnTlToBr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "lnTlToBr" element
     */
    @Override
    public void unsetLnTlToBr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "lnBlToTr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLnBlToTr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnBlToTr" element
     */
    @Override
    public boolean isSetLnBlToTr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "lnBlToTr" element
     */
    @Override
    public void setLnBlToTr(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties lnBlToTr) {
        generatedSetterHelperImpl(lnBlToTr, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnBlToTr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLnBlToTr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "lnBlToTr" element
     */
    @Override
    public void unsetLnBlToTr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "cell3D" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D getCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cell3D" element
     */
    @Override
    public boolean isSetCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "cell3D" element
     */
    @Override
    public void setCell3D(org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D cell3D) {
        generatedSetterHelperImpl(cell3D, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cell3D" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D addNewCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "cell3D" element
     */
    @Override
    public void unsetCell3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().find_element_user(PROPERTY_QNAME[7], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "noFill" element
     */
    @Override
    public void setNoFill(org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties noFill) {
        generatedSetterHelperImpl(noFill, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties addNewNoFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().find_element_user(PROPERTY_QNAME[8], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "solidFill" element
     */
    @Override
    public void setSolidFill(org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties solidFill) {
        generatedSetterHelperImpl(solidFill, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "solidFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties addNewSolidFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties)get_store().add_element_user(PROPERTY_QNAME[8]);
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
            get_store().remove_element(PROPERTY_QNAME[8], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().find_element_user(PROPERTY_QNAME[9], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "gradFill" element
     */
    @Override
    public void setGradFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties gradFill) {
        generatedSetterHelperImpl(gradFill, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gradFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties addNewGradFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties)get_store().add_element_user(PROPERTY_QNAME[9]);
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
            get_store().remove_element(PROPERTY_QNAME[9], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().find_element_user(PROPERTY_QNAME[10], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "blipFill" element
     */
    @Override
    public void setBlipFill(org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties blipFill) {
        generatedSetterHelperImpl(blipFill, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "blipFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties addNewBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().add_element_user(PROPERTY_QNAME[10]);
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
            get_store().remove_element(PROPERTY_QNAME[10], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().find_element_user(PROPERTY_QNAME[11], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "pattFill" element
     */
    @Override
    public void setPattFill(org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties pattFill) {
        generatedSetterHelperImpl(pattFill, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pattFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties addNewPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().add_element_user(PROPERTY_QNAME[11]);
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
            get_store().remove_element(PROPERTY_QNAME[11], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().find_element_user(PROPERTY_QNAME[12], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "grpFill" element
     */
    @Override
    public void setGrpFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties grpFill) {
        generatedSetterHelperImpl(grpFill, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "grpFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties addNewGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().add_element_user(PROPERTY_QNAME[12]);
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
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "headers" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders getHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders)get_store().find_element_user(PROPERTY_QNAME[13], 0);
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
    public void setHeaders(org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders headers) {
        generatedSetterHelperImpl(headers, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headers" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders addNewHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHeaders)get_store().add_element_user(PROPERTY_QNAME[13]);
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

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[14], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[14]);
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
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "marL" attribute
     */
    @Override
    public java.lang.Object getMarL() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "marL" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetMarL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return target;
        }
    }

    /**
     * True if has "marL" attribute
     */
    @Override
    public boolean isSetMarL() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "marL" attribute
     */
    @Override
    public void setMarL(java.lang.Object marL) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setObjectValue(marL);
        }
    }

    /**
     * Sets (as xml) the "marL" attribute
     */
    @Override
    public void xsetMarL(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 marL) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(marL);
        }
    }

    /**
     * Unsets the "marL" attribute
     */
    @Override
    public void unsetMarL() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "marR" attribute
     */
    @Override
    public java.lang.Object getMarR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "marR" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetMarR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return target;
        }
    }

    /**
     * True if has "marR" attribute
     */
    @Override
    public boolean isSetMarR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "marR" attribute
     */
    @Override
    public void setMarR(java.lang.Object marR) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setObjectValue(marR);
        }
    }

    /**
     * Sets (as xml) the "marR" attribute
     */
    @Override
    public void xsetMarR(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 marR) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(marR);
        }
    }

    /**
     * Unsets the "marR" attribute
     */
    @Override
    public void unsetMarR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "marT" attribute
     */
    @Override
    public java.lang.Object getMarT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "marT" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetMarT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return target;
        }
    }

    /**
     * True if has "marT" attribute
     */
    @Override
    public boolean isSetMarT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "marT" attribute
     */
    @Override
    public void setMarT(java.lang.Object marT) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setObjectValue(marT);
        }
    }

    /**
     * Sets (as xml) the "marT" attribute
     */
    @Override
    public void xsetMarT(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 marT) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(marT);
        }
    }

    /**
     * Unsets the "marT" attribute
     */
    @Override
    public void unsetMarT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "marB" attribute
     */
    @Override
    public java.lang.Object getMarB() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "marB" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetMarB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_default_attribute_value(PROPERTY_QNAME[18]);
            }
            return target;
        }
    }

    /**
     * True if has "marB" attribute
     */
    @Override
    public boolean isSetMarB() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "marB" attribute
     */
    @Override
    public void setMarB(java.lang.Object marB) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setObjectValue(marB);
        }
    }

    /**
     * Sets (as xml) the "marB" attribute
     */
    @Override
    public void xsetMarB(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 marB) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(marB);
        }
    }

    /**
     * Unsets the "marB" attribute
     */
    @Override
    public void unsetMarB() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "vert" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum getVert() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vert" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType xgetVert() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType)get_default_attribute_value(PROPERTY_QNAME[19]);
            }
            return target;
        }
    }

    /**
     * True if has "vert" attribute
     */
    @Override
    public boolean isSetVert() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "vert" attribute
     */
    @Override
    public void setVert(org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum vert) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setEnumValue(vert);
        }
    }

    /**
     * Sets (as xml) the "vert" attribute
     */
    @Override
    public void xsetVert(org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType vert) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(vert);
        }
    }

    /**
     * Unsets the "vert" attribute
     */
    @Override
    public void unsetVert() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "anchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum getAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "anchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType xgetAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType)get_default_attribute_value(PROPERTY_QNAME[20]);
            }
            return target;
        }
    }

    /**
     * True if has "anchor" attribute
     */
    @Override
    public boolean isSetAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "anchor" attribute
     */
    @Override
    public void setAnchor(org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum anchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setEnumValue(anchor);
        }
    }

    /**
     * Sets (as xml) the "anchor" attribute
     */
    @Override
    public void xsetAnchor(org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType anchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(anchor);
        }
    }

    /**
     * Unsets the "anchor" attribute
     */
    @Override
    public void unsetAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "anchorCtr" attribute
     */
    @Override
    public boolean getAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[21]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "anchorCtr" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[21]);
            }
            return target;
        }
    }

    /**
     * True if has "anchorCtr" attribute
     */
    @Override
    public boolean isSetAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "anchorCtr" attribute
     */
    @Override
    public void setAnchorCtr(boolean anchorCtr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setBooleanValue(anchorCtr);
        }
    }

    /**
     * Sets (as xml) the "anchorCtr" attribute
     */
    @Override
    public void xsetAnchorCtr(org.apache.xmlbeans.XmlBoolean anchorCtr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(anchorCtr);
        }
    }

    /**
     * Unsets the "anchorCtr" attribute
     */
    @Override
    public void unsetAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "horzOverflow" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType.Enum getHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[22]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "horzOverflow" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType xgetHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType)get_default_attribute_value(PROPERTY_QNAME[22]);
            }
            return target;
        }
    }

    /**
     * True if has "horzOverflow" attribute
     */
    @Override
    public boolean isSetHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "horzOverflow" attribute
     */
    @Override
    public void setHorzOverflow(org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType.Enum horzOverflow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setEnumValue(horzOverflow);
        }
    }

    /**
     * Sets (as xml) the "horzOverflow" attribute
     */
    @Override
    public void xsetHorzOverflow(org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType horzOverflow) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(horzOverflow);
        }
    }

    /**
     * Unsets the "horzOverflow" attribute
     */
    @Override
    public void unsetHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }
}

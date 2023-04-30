/*
 * XML Type:  CT_GroupShapeProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GroupShapeProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGroupShapePropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties {
    private static final long serialVersionUID = 1L;

    public CTGroupShapePropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "xfrm"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "noFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "solidFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gradFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blipFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pattFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "grpFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectDag"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "scene3d"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "bwMode"),
    };


    /**
     * Gets the "xfrm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D getXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "xfrm" element
     */
    @Override
    public boolean isSetXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "xfrm" element
     */
    @Override
    public void setXfrm(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D xfrm) {
        generatedSetterHelperImpl(xfrm, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "xfrm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D addNewXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "xfrm" element
     */
    @Override
    public void unsetXfrm() {
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
     * Gets the "scene3d" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D getScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "scene3d" element
     */
    @Override
    public boolean isSetScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "scene3d" element
     */
    @Override
    public void setScene3D(org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D scene3D) {
        generatedSetterHelperImpl(scene3D, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "scene3d" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D addNewScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "scene3d" element
     */
    @Override
    public void unsetScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[10], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[10]);
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
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "bwMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum getBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bwMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode xgetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "bwMode" attribute
     */
    @Override
    public boolean isSetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "bwMode" attribute
     */
    @Override
    public void setBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum bwMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setEnumValue(bwMode);
        }
    }

    /**
     * Sets (as xml) the "bwMode" attribute
     */
    @Override
    public void xsetBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode bwMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(bwMode);
        }
    }

    /**
     * Unsets the "bwMode" attribute
     */
    @Override
    public void unsetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }
}

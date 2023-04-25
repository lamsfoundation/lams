/*
 * XML Type:  CT_TableProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TableProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTablePropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTableProperties {
    private static final long serialVersionUID = 1L;

    public CTTablePropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "noFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "solidFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gradFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blipFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pattFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "grpFill"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectDag"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tableStyle"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tableStyleId"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "rtl"),
        new QName("", "firstRow"),
        new QName("", "firstCol"),
        new QName("", "lastRow"),
        new QName("", "lastCol"),
        new QName("", "bandRow"),
        new QName("", "bandCol"),
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
     * Gets the "blipFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties getBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "blipFill" element
     */
    @Override
    public void setBlipFill(org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties blipFill) {
        generatedSetterHelperImpl(blipFill, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "blipFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties addNewBlipFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "pattFill" element
     */
    @Override
    public void setPattFill(org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties pattFill) {
        generatedSetterHelperImpl(pattFill, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pattFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties addNewPattFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "grpFill" element
     */
    @Override
    public void setGrpFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties grpFill) {
        generatedSetterHelperImpl(grpFill, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "grpFill" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties addNewGrpFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "effectLst" element
     */
    @Override
    public void setEffectLst(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectLst) {
        generatedSetterHelperImpl(effectLst, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList addNewEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer)get_store().find_element_user(PROPERTY_QNAME[7], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "effectDag" element
     */
    @Override
    public void setEffectDag(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer effectDag) {
        generatedSetterHelperImpl(effectDag, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectDag" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer addNewEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "tableStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle getTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tableStyle" element
     */
    @Override
    public boolean isSetTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "tableStyle" element
     */
    @Override
    public void setTableStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle tableStyle) {
        generatedSetterHelperImpl(tableStyle, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tableStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle addNewTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "tableStyle" element
     */
    @Override
    public void unsetTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "tableStyleId" element
     */
    @Override
    public java.lang.String getTableStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "tableStyleId" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetTableStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return target;
        }
    }

    /**
     * True if has "tableStyleId" element
     */
    @Override
    public boolean isSetTableStyleId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "tableStyleId" element
     */
    @Override
    public void setTableStyleId(java.lang.String tableStyleId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[9]);
            }
            target.setStringValue(tableStyleId);
        }
    }

    /**
     * Sets (as xml) the "tableStyleId" element
     */
    @Override
    public void xsetTableStyleId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid tableStyleId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().add_element_user(PROPERTY_QNAME[9]);
            }
            target.set(tableStyleId);
        }
    }

    /**
     * Unsets the "tableStyleId" element
     */
    @Override
    public void unsetTableStyleId() {
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
     * Gets the "rtl" attribute
     */
    @Override
    public boolean getRtl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "rtl" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRtl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return target;
        }
    }

    /**
     * True if has "rtl" attribute
     */
    @Override
    public boolean isSetRtl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "rtl" attribute
     */
    @Override
    public void setRtl(boolean rtl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setBooleanValue(rtl);
        }
    }

    /**
     * Sets (as xml) the "rtl" attribute
     */
    @Override
    public void xsetRtl(org.apache.xmlbeans.XmlBoolean rtl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(rtl);
        }
    }

    /**
     * Unsets the "rtl" attribute
     */
    @Override
    public void unsetRtl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "firstRow" attribute
     */
    @Override
    public boolean getFirstRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[12]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "firstRow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFirstRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[12]);
            }
            return target;
        }
    }

    /**
     * True if has "firstRow" attribute
     */
    @Override
    public boolean isSetFirstRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "firstRow" attribute
     */
    @Override
    public void setFirstRow(boolean firstRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setBooleanValue(firstRow);
        }
    }

    /**
     * Sets (as xml) the "firstRow" attribute
     */
    @Override
    public void xsetFirstRow(org.apache.xmlbeans.XmlBoolean firstRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(firstRow);
        }
    }

    /**
     * Unsets the "firstRow" attribute
     */
    @Override
    public void unsetFirstRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "firstCol" attribute
     */
    @Override
    public boolean getFirstCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "firstCol" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFirstCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[13]);
            }
            return target;
        }
    }

    /**
     * True if has "firstCol" attribute
     */
    @Override
    public boolean isSetFirstCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "firstCol" attribute
     */
    @Override
    public void setFirstCol(boolean firstCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setBooleanValue(firstCol);
        }
    }

    /**
     * Sets (as xml) the "firstCol" attribute
     */
    @Override
    public void xsetFirstCol(org.apache.xmlbeans.XmlBoolean firstCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(firstCol);
        }
    }

    /**
     * Unsets the "firstCol" attribute
     */
    @Override
    public void unsetFirstCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "lastRow" attribute
     */
    @Override
    public boolean getLastRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "lastRow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetLastRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[14]);
            }
            return target;
        }
    }

    /**
     * True if has "lastRow" attribute
     */
    @Override
    public boolean isSetLastRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "lastRow" attribute
     */
    @Override
    public void setLastRow(boolean lastRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setBooleanValue(lastRow);
        }
    }

    /**
     * Sets (as xml) the "lastRow" attribute
     */
    @Override
    public void xsetLastRow(org.apache.xmlbeans.XmlBoolean lastRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(lastRow);
        }
    }

    /**
     * Unsets the "lastRow" attribute
     */
    @Override
    public void unsetLastRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "lastCol" attribute
     */
    @Override
    public boolean getLastCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "lastCol" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetLastCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[15]);
            }
            return target;
        }
    }

    /**
     * True if has "lastCol" attribute
     */
    @Override
    public boolean isSetLastCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "lastCol" attribute
     */
    @Override
    public void setLastCol(boolean lastCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setBooleanValue(lastCol);
        }
    }

    /**
     * Sets (as xml) the "lastCol" attribute
     */
    @Override
    public void xsetLastCol(org.apache.xmlbeans.XmlBoolean lastCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(lastCol);
        }
    }

    /**
     * Unsets the "lastCol" attribute
     */
    @Override
    public void unsetLastCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "bandRow" attribute
     */
    @Override
    public boolean getBandRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "bandRow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetBandRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[16]);
            }
            return target;
        }
    }

    /**
     * True if has "bandRow" attribute
     */
    @Override
    public boolean isSetBandRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "bandRow" attribute
     */
    @Override
    public void setBandRow(boolean bandRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setBooleanValue(bandRow);
        }
    }

    /**
     * Sets (as xml) the "bandRow" attribute
     */
    @Override
    public void xsetBandRow(org.apache.xmlbeans.XmlBoolean bandRow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(bandRow);
        }
    }

    /**
     * Unsets the "bandRow" attribute
     */
    @Override
    public void unsetBandRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "bandCol" attribute
     */
    @Override
    public boolean getBandCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "bandCol" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetBandCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[17]);
            }
            return target;
        }
    }

    /**
     * True if has "bandCol" attribute
     */
    @Override
    public boolean isSetBandCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "bandCol" attribute
     */
    @Override
    public void setBandCol(boolean bandCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setBooleanValue(bandCol);
        }
    }

    /**
     * Sets (as xml) the "bandCol" attribute
     */
    @Override
    public void xsetBandCol(org.apache.xmlbeans.XmlBoolean bandCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(bandCol);
        }
    }

    /**
     * Unsets the "bandCol" attribute
     */
    @Override
    public void unsetBandCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }
}

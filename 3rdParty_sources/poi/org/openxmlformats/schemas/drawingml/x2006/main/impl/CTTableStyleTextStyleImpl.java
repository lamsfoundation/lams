/*
 * XML Type:  CT_TableStyleTextStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TableStyleTextStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTableStyleTextStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle {
    private static final long serialVersionUID = 1L;

    public CTTableStyleTextStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "font"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fontRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "scrgbClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "srgbClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hslClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sysClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "schemeClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "prstClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "b"),
        new QName("", "i"),
    };


    /**
     * Gets the "font" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection getFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "font" element
     */
    @Override
    public boolean isSetFont() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "font" element
     */
    @Override
    public void setFont(org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection font) {
        generatedSetterHelperImpl(font, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "font" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection addNewFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFontCollection)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "font" element
     */
    @Override
    public void unsetFont() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "fontRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference getFontRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fontRef" element
     */
    @Override
    public boolean isSetFontRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "fontRef" element
     */
    @Override
    public void setFontRef(org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference fontRef) {
        generatedSetterHelperImpl(fontRef, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fontRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference addNewFontRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "fontRef" element
     */
    @Override
    public void unsetFontRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "scrgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor getScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "scrgbClr" element
     */
    @Override
    public boolean isSetScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "scrgbClr" element
     */
    @Override
    public void setScrgbClr(org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor scrgbClr) {
        generatedSetterHelperImpl(scrgbClr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "scrgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor addNewScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "scrgbClr" element
     */
    @Override
    public void unsetScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "srgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor getSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "srgbClr" element
     */
    @Override
    public boolean isSetSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "srgbClr" element
     */
    @Override
    public void setSrgbClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor srgbClr) {
        generatedSetterHelperImpl(srgbClr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "srgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor addNewSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "srgbClr" element
     */
    @Override
    public void unsetSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "hslClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor getHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hslClr" element
     */
    @Override
    public boolean isSetHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "hslClr" element
     */
    @Override
    public void setHslClr(org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor hslClr) {
        generatedSetterHelperImpl(hslClr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hslClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor addNewHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "hslClr" element
     */
    @Override
    public void unsetHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "sysClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor getSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sysClr" element
     */
    @Override
    public boolean isSetSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "sysClr" element
     */
    @Override
    public void setSysClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor sysClr) {
        generatedSetterHelperImpl(sysClr, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sysClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor addNewSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "sysClr" element
     */
    @Override
    public void unsetSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "schemeClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor getSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "schemeClr" element
     */
    @Override
    public boolean isSetSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "schemeClr" element
     */
    @Override
    public void setSchemeClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor schemeClr) {
        generatedSetterHelperImpl(schemeClr, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "schemeClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor addNewSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "schemeClr" element
     */
    @Override
    public void unsetSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "prstClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor getPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "prstClr" element
     */
    @Override
    public boolean isSetPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "prstClr" element
     */
    @Override
    public void setPrstClr(org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor prstClr) {
        generatedSetterHelperImpl(prstClr, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "prstClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor addNewPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "prstClr" element
     */
    @Override
    public void unsetPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[8], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[8]);
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
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "b" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType.Enum getB() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "b" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType xgetB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
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
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "b" attribute
     */
    @Override
    public void setB(org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType.Enum b) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setEnumValue(b);
        }
    }

    /**
     * Sets (as xml) the "b" attribute
     */
    @Override
    public void xsetB(org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType b) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType)get_store().add_attribute_user(PROPERTY_QNAME[9]);
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
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "i" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType.Enum getI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "i" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType xgetI() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType)get_default_attribute_value(PROPERTY_QNAME[10]);
            }
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
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "i" attribute
     */
    @Override
    public void setI(org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType.Enum iValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setEnumValue(iValue);
        }
    }

    /**
     * Sets (as xml) the "i" attribute
     */
    @Override
    public void xsetI(org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType iValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STOnOffStyleType)get_store().add_attribute_user(PROPERTY_QNAME[10]);
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
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }
}

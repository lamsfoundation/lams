/*
 * XML Type:  CT_PresetShadowEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PresetShadowEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPresetShadowEffectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect {
    private static final long serialVersionUID = 1L;

    public CTPresetShadowEffectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "scrgbClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "srgbClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "hslClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sysClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "schemeClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "prstClr"),
        new QName("", "prst"),
        new QName("", "dist"),
        new QName("", "dir"),
    };


    /**
     * Gets the "scrgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor getScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "scrgbClr" element
     */
    @Override
    public void setScrgbClr(org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor scrgbClr) {
        generatedSetterHelperImpl(scrgbClr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "scrgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor addNewScrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScRgbColor)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "srgbClr" element
     */
    @Override
    public void setSrgbClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor srgbClr) {
        generatedSetterHelperImpl(srgbClr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "srgbClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor addNewSrgbClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "hslClr" element
     */
    @Override
    public void setHslClr(org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor hslClr) {
        generatedSetterHelperImpl(hslClr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hslClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor addNewHslClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTHslColor)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "sysClr" element
     */
    @Override
    public void setSysClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor sysClr) {
        generatedSetterHelperImpl(sysClr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sysClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor addNewSysClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSystemColor)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "schemeClr" element
     */
    @Override
    public void setSchemeClr(org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor schemeClr) {
        generatedSetterHelperImpl(schemeClr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "schemeClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor addNewSchemeClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSchemeColor)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "prstClr" element
     */
    @Override
    public void setPrstClr(org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor prstClr) {
        generatedSetterHelperImpl(prstClr, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "prstClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor addNewPrstClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetColor)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "prst" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal.Enum getPrst() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "prst" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal xgetPrst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Sets the "prst" attribute
     */
    @Override
    public void setPrst(org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal.Enum prst) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setEnumValue(prst);
        }
    }

    /**
     * Sets (as xml) the "prst" attribute
     */
    @Override
    public void xsetPrst(org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal prst) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(prst);
        }
    }

    /**
     * Gets the "dist" attribute
     */
    @Override
    public long getDist() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "dist" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetDist() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return target;
        }
    }

    /**
     * True if has "dist" attribute
     */
    @Override
    public boolean isSetDist() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "dist" attribute
     */
    @Override
    public void setDist(long dist) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setLongValue(dist);
        }
    }

    /**
     * Sets (as xml) the "dist" attribute
     */
    @Override
    public void xsetDist(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate dist) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(dist);
        }
    }

    /**
     * Unsets the "dist" attribute
     */
    @Override
    public void unsetDist() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "dir" attribute
     */
    @Override
    public int getDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "dir" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle xgetDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "dir" attribute
     */
    @Override
    public boolean isSetDir() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "dir" attribute
     */
    @Override
    public void setDir(int dir) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setIntValue(dir);
        }
    }

    /**
     * Sets (as xml) the "dir" attribute
     */
    @Override
    public void xsetDir(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle dir) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedAngle)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(dir);
        }
    }

    /**
     * Unsets the "dir" attribute
     */
    @Override
    public void unsetDir() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }
}

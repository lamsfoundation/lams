/*
 * XML Type:  CT_PatternFillProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PatternFillProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPatternFillPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties {
    private static final long serialVersionUID = 1L;

    public CTPatternFillPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fgClr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "bgClr"),
        new QName("", "prst"),
    };


    /**
     * Gets the "fgClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor getFgClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fgClr" element
     */
    @Override
    public boolean isSetFgClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "fgClr" element
     */
    @Override
    public void setFgClr(org.openxmlformats.schemas.drawingml.x2006.main.CTColor fgClr) {
        generatedSetterHelperImpl(fgClr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fgClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewFgClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "fgClr" element
     */
    @Override
    public void unsetFgClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "bgClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor getBgClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bgClr" element
     */
    @Override
    public boolean isSetBgClr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "bgClr" element
     */
    @Override
    public void setBgClr(org.openxmlformats.schemas.drawingml.x2006.main.CTColor bgClr) {
        generatedSetterHelperImpl(bgClr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bgClr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewBgClr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "bgClr" element
     */
    @Override
    public void unsetBgClr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "prst" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal.Enum getPrst() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "prst" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal xgetPrst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "prst" attribute
     */
    @Override
    public boolean isSetPrst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "prst" attribute
     */
    @Override
    public void setPrst(org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal.Enum prst) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(prst);
        }
    }

    /**
     * Sets (as xml) the "prst" attribute
     */
    @Override
    public void xsetPrst(org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal prst) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(prst);
        }
    }

    /**
     * Unsets the "prst" attribute
     */
    @Override
    public void unsetPrst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

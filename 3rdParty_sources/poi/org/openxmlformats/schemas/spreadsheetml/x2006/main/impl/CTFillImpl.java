/*
 * XML Type:  CT_Fill
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Fill(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTFillImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill {
    private static final long serialVersionUID = 1L;

    public CTFillImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "patternFill"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "gradientFill"),
    };


    /**
     * Gets the "patternFill" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill getPatternFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "patternFill" element
     */
    @Override
    public boolean isSetPatternFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "patternFill" element
     */
    @Override
    public void setPatternFill(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill patternFill) {
        generatedSetterHelperImpl(patternFill, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "patternFill" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill addNewPatternFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPatternFill)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "patternFill" element
     */
    @Override
    public void unsetPatternFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "gradientFill" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill getGradientFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "gradientFill" element
     */
    @Override
    public boolean isSetGradientFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "gradientFill" element
     */
    @Override
    public void setGradientFill(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill gradientFill) {
        generatedSetterHelperImpl(gradientFill, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gradientFill" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill addNewGradientFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "gradientFill" element
     */
    @Override
    public void unsetGradientFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

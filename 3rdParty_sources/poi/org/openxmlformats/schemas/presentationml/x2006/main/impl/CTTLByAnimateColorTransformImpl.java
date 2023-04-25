/*
 * XML Type:  CT_TLByAnimateColorTransform
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLByAnimateColorTransform
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLByAnimateColorTransform(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLByAnimateColorTransformImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLByAnimateColorTransform {
    private static final long serialVersionUID = 1L;

    public CTTLByAnimateColorTransformImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "rgb"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "hsl"),
    };


    /**
     * Gets the "rgb" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform getRgb() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rgb" element
     */
    @Override
    public boolean isSetRgb() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rgb" element
     */
    @Override
    public void setRgb(org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform rgb) {
        generatedSetterHelperImpl(rgb, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rgb" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform addNewRgb() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rgb" element
     */
    @Override
    public void unsetRgb() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "hsl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform getHsl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hsl" element
     */
    @Override
    public boolean isSetHsl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "hsl" element
     */
    @Override
    public void setHsl(org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform hsl) {
        generatedSetterHelperImpl(hsl, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hsl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform addNewHsl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "hsl" element
     */
    @Override
    public void unsetHsl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

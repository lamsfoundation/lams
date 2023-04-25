/*
 * XML Type:  CT_ColorChangeEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorChangeEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTColorChangeEffectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTColorChangeEffect {
    private static final long serialVersionUID = 1L;

    public CTColorChangeEffectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "clrFrom"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "clrTo"),
        new QName("", "useA"),
    };


    /**
     * Gets the "clrFrom" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor getClrFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "clrFrom" element
     */
    @Override
    public void setClrFrom(org.openxmlformats.schemas.drawingml.x2006.main.CTColor clrFrom) {
        generatedSetterHelperImpl(clrFrom, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrFrom" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewClrFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "clrTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor getClrTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "clrTo" element
     */
    @Override
    public void setClrTo(org.openxmlformats.schemas.drawingml.x2006.main.CTColor clrTo) {
        generatedSetterHelperImpl(clrTo, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColor addNewClrTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColor)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "useA" attribute
     */
    @Override
    public boolean getUseA() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "useA" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetUseA() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "useA" attribute
     */
    @Override
    public boolean isSetUseA() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "useA" attribute
     */
    @Override
    public void setUseA(boolean useA) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(useA);
        }
    }

    /**
     * Sets (as xml) the "useA" attribute
     */
    @Override
    public void xsetUseA(org.apache.xmlbeans.XmlBoolean useA) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(useA);
        }
    }

    /**
     * Unsets the "useA" attribute
     */
    @Override
    public void unsetUseA() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

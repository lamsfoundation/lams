/*
 * XML Type:  CT_AlphaModulateFixedEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AlphaModulateFixedEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTAlphaModulateFixedEffectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect {
    private static final long serialVersionUID = 1L;

    public CTAlphaModulateFixedEffectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "amt"),
    };


    /**
     * Gets the "amt" attribute
     */
    @Override
    public java.lang.Object getAmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "amt" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage xgetAmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "amt" attribute
     */
    @Override
    public boolean isSetAmt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "amt" attribute
     */
    @Override
    public void setAmt(java.lang.Object amt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(amt);
        }
    }

    /**
     * Sets (as xml) the "amt" attribute
     */
    @Override
    public void xsetAmt(org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage amt) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(amt);
        }
    }

    /**
     * Unsets the "amt" attribute
     */
    @Override
    public void unsetAmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }
}

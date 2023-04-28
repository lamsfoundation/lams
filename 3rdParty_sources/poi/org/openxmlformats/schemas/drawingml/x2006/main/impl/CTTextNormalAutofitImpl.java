/*
 * XML Type:  CT_TextNormalAutofit
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextNormalAutofit(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextNormalAutofitImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit {
    private static final long serialVersionUID = 1L;

    public CTTextNormalAutofitImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "fontScale"),
        new QName("", "lnSpcReduction"),
    };


    /**
     * Gets the "fontScale" attribute
     */
    @Override
    public java.lang.Object getFontScale() {
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
     * Gets (as xml) the "fontScale" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString xgetFontScale() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "fontScale" attribute
     */
    @Override
    public boolean isSetFontScale() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "fontScale" attribute
     */
    @Override
    public void setFontScale(java.lang.Object fontScale) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(fontScale);
        }
    }

    /**
     * Sets (as xml) the "fontScale" attribute
     */
    @Override
    public void xsetFontScale(org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString fontScale) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextFontScalePercentOrPercentString)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(fontScale);
        }
    }

    /**
     * Unsets the "fontScale" attribute
     */
    @Override
    public void unsetFontScale() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "lnSpcReduction" attribute
     */
    @Override
    public java.lang.Object getLnSpcReduction() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "lnSpcReduction" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString xgetLnSpcReduction() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "lnSpcReduction" attribute
     */
    @Override
    public boolean isSetLnSpcReduction() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "lnSpcReduction" attribute
     */
    @Override
    public void setLnSpcReduction(java.lang.Object lnSpcReduction) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(lnSpcReduction);
        }
    }

    /**
     * Sets (as xml) the "lnSpcReduction" attribute
     */
    @Override
    public void xsetLnSpcReduction(org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString lnSpcReduction) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextSpacingPercentOrPercentString)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(lnSpcReduction);
        }
    }

    /**
     * Unsets the "lnSpcReduction" attribute
     */
    @Override
    public void unsetLnSpcReduction() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

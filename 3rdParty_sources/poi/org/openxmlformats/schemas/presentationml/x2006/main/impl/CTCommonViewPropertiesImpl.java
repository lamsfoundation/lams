/*
 * XML Type:  CT_CommonViewProperties
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCommonViewProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CommonViewProperties(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTCommonViewPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTCommonViewProperties {
    private static final long serialVersionUID = 1L;

    public CTCommonViewPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "scale"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "origin"),
        new QName("", "varScale"),
    };


    /**
     * Gets the "scale" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D getScale() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "scale" element
     */
    @Override
    public void setScale(org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D scale) {
        generatedSetterHelperImpl(scale, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "scale" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D addNewScale() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "origin" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D getOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "origin" element
     */
    @Override
    public void setOrigin(org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D origin) {
        generatedSetterHelperImpl(origin, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "origin" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D addNewOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "varScale" attribute
     */
    @Override
    public boolean getVarScale() {
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
     * Gets (as xml) the "varScale" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetVarScale() {
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
     * True if has "varScale" attribute
     */
    @Override
    public boolean isSetVarScale() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "varScale" attribute
     */
    @Override
    public void setVarScale(boolean varScale) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(varScale);
        }
    }

    /**
     * Sets (as xml) the "varScale" attribute
     */
    @Override
    public void xsetVarScale(org.apache.xmlbeans.XmlBoolean varScale) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(varScale);
        }
    }

    /**
     * Unsets the "varScale" attribute
     */
    @Override
    public void unsetVarScale() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

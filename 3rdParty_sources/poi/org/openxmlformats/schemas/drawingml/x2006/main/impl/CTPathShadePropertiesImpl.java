/*
 * XML Type:  CT_PathShadeProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PathShadeProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPathShadePropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPathShadeProperties {
    private static final long serialVersionUID = 1L;

    public CTPathShadePropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fillToRect"),
        new QName("", "path"),
    };


    /**
     * Gets the "fillToRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect getFillToRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fillToRect" element
     */
    @Override
    public boolean isSetFillToRect() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "fillToRect" element
     */
    @Override
    public void setFillToRect(org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect fillToRect) {
        generatedSetterHelperImpl(fillToRect, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fillToRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect addNewFillToRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "fillToRect" element
     */
    @Override
    public void unsetFillToRect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "path" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType.Enum getPath() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "path" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType xgetPath() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "path" attribute
     */
    @Override
    public boolean isSetPath() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "path" attribute
     */
    @Override
    public void setPath(org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType.Enum path) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(path);
        }
    }

    /**
     * Sets (as xml) the "path" attribute
     */
    @Override
    public void xsetPath(org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType path) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPathShadeType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(path);
        }
    }

    /**
     * Unsets the "path" attribute
     */
    @Override
    public void unsetPath() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

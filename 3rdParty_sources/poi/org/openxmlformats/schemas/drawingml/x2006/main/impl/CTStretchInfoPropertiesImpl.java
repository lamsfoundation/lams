/*
 * XML Type:  CT_StretchInfoProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_StretchInfoProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTStretchInfoPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTStretchInfoProperties {
    private static final long serialVersionUID = 1L;

    public CTStretchInfoPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fillRect"),
    };


    /**
     * Gets the "fillRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect getFillRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fillRect" element
     */
    @Override
    public boolean isSetFillRect() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "fillRect" element
     */
    @Override
    public void setFillRect(org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect fillRect) {
        generatedSetterHelperImpl(fillRect, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fillRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect addNewFillRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeRect)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "fillRect" element
     */
    @Override
    public void unsetFillRect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}

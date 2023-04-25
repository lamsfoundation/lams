/*
 * XML Type:  CT_Scale2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Scale2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTScale2DImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTScale2D {
    private static final long serialVersionUID = 1L;

    public CTScale2DImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sy"),
    };


    /**
     * Gets the "sx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRatio getSx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRatio target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRatio)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sx" element
     */
    @Override
    public void setSx(org.openxmlformats.schemas.drawingml.x2006.main.CTRatio sx) {
        generatedSetterHelperImpl(sx, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRatio addNewSx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRatio target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRatio)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "sy" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRatio getSy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRatio target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRatio)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "sy" element
     */
    @Override
    public void setSy(org.openxmlformats.schemas.drawingml.x2006.main.CTRatio sy) {
        generatedSetterHelperImpl(sy, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sy" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTRatio addNewSy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTRatio target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTRatio)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }
}

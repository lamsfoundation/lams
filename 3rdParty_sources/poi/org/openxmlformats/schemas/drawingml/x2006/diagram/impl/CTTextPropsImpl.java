/*
 * XML Type:  CT_TextProps
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTTextProps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextProps(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTTextPropsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTTextProps {
    private static final long serialVersionUID = 1L;

    public CTTextPropsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sp3d"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "flatTx"),
    };


    /**
     * Gets the "sp3d" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D getSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sp3d" element
     */
    @Override
    public boolean isSetSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "sp3d" element
     */
    @Override
    public void setSp3D(org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D sp3D) {
        generatedSetterHelperImpl(sp3D, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sp3d" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D addNewSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "sp3d" element
     */
    @Override
    public void unsetSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "flatTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText getFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "flatTx" element
     */
    @Override
    public boolean isSetFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "flatTx" element
     */
    @Override
    public void setFlatTx(org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText flatTx) {
        generatedSetterHelperImpl(flatTx, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "flatTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText addNewFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "flatTx" element
     */
    @Override
    public void unsetFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

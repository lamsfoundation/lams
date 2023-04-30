/*
 * XML Type:  CT_ShapeStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ShapeStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTShapeStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle {
    private static final long serialVersionUID = 1L;

    public CTShapeStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fillRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectRef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fontRef"),
    };


    /**
     * Gets the "lnRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getLnRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "lnRef" element
     */
    @Override
    public void setLnRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference lnRef) {
        generatedSetterHelperImpl(lnRef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewLnRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "fillRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "fillRef" element
     */
    @Override
    public void setFillRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference fillRef) {
        generatedSetterHelperImpl(fillRef, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fillRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewFillRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "effectRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getEffectRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "effectRef" element
     */
    @Override
    public void setEffectRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference effectRef) {
        generatedSetterHelperImpl(effectRef, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewEffectRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "fontRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference getFontRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "fontRef" element
     */
    @Override
    public void setFontRef(org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference fontRef) {
        generatedSetterHelperImpl(fontRef, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fontRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference addNewFontRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }
}

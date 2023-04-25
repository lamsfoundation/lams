/*
 * XML Type:  CT_ThemeableLineStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ThemeableLineStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTThemeableLineStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle {
    private static final long serialVersionUID = 1L;

    public CTThemeableLineStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ln"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnRef"),
    };


    /**
     * Gets the "ln" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ln" element
     */
    @Override
    public boolean isSetLn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "ln" element
     */
    @Override
    public void setLn(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties ln) {
        generatedSetterHelperImpl(ln, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ln" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "ln" element
     */
    @Override
    public void unsetLn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "lnRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getLnRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnRef" element
     */
    @Override
    public boolean isSetLnRef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "lnRef" element
     */
    @Override
    public void setLnRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference lnRef) {
        generatedSetterHelperImpl(lnRef, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnRef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewLnRef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "lnRef" element
     */
    @Override
    public void unsetLnRef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

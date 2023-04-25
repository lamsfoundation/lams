/*
 * XML Type:  CT_Backdrop
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBackdrop
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Backdrop(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTBackdropImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTBackdrop {
    private static final long serialVersionUID = 1L;

    public CTBackdropImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "anchor"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "norm"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "up"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
    };


    /**
     * Gets the "anchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D getAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "anchor" element
     */
    @Override
    public void setAnchor(org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D anchor) {
        generatedSetterHelperImpl(anchor, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "anchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D addNewAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint3D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "norm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D getNorm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "norm" element
     */
    @Override
    public void setNorm(org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D norm) {
        generatedSetterHelperImpl(norm, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "norm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D addNewNorm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "up" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D getUp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "up" element
     */
    @Override
    public void setUp(org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D up) {
        generatedSetterHelperImpl(up, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "up" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D addNewUp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTVector3D)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}

/*
 * XML Type:  CT_GvmlTextShape
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GvmlTextShape(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGvmlTextShapeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlTextShape {
    private static final long serialVersionUID = 1L;

    public CTGvmlTextShapeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "txBody"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "useSpRect"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "xfrm"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
    };


    /**
     * Gets the "txBody" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody getTxBody() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "txBody" element
     */
    @Override
    public void setTxBody(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody txBody) {
        generatedSetterHelperImpl(txBody, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txBody" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewTxBody() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "useSpRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlUseShapeRectangle getUseSpRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlUseShapeRectangle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlUseShapeRectangle)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useSpRect" element
     */
    @Override
    public boolean isSetUseSpRect() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "useSpRect" element
     */
    @Override
    public void setUseSpRect(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlUseShapeRectangle useSpRect) {
        generatedSetterHelperImpl(useSpRect, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useSpRect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlUseShapeRectangle addNewUseSpRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlUseShapeRectangle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlUseShapeRectangle)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "useSpRect" element
     */
    @Override
    public void unsetUseSpRect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "xfrm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D getXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "xfrm" element
     */
    @Override
    public boolean isSetXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "xfrm" element
     */
    @Override
    public void setXfrm(org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D xfrm) {
        generatedSetterHelperImpl(xfrm, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "xfrm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D addNewXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "xfrm" element
     */
    @Override
    public void unsetXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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

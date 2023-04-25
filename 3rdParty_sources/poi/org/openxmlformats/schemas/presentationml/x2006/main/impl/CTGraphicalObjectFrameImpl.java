/*
 * XML Type:  CT_GraphicalObjectFrame
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GraphicalObjectFrame(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTGraphicalObjectFrameImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame {
    private static final long serialVersionUID = 1L;

    public CTGraphicalObjectFrameImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "nvGraphicFramePr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "xfrm"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "graphic"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "bwMode"),
    };


    /**
     * Gets the "nvGraphicFramePr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual getNvGraphicFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "nvGraphicFramePr" element
     */
    @Override
    public void setNvGraphicFramePr(org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual nvGraphicFramePr) {
        generatedSetterHelperImpl(nvGraphicFramePr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "nvGraphicFramePr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual addNewNvGraphicFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrameNonVisual)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "xfrm" element
     */
    @Override
    public void setXfrm(org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D xfrm) {
        generatedSetterHelperImpl(xfrm, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "xfrm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D addNewXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "graphic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject getGraphic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "graphic" element
     */
    @Override
    public void setGraphic(org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject graphic) {
        generatedSetterHelperImpl(graphic, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "graphic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject addNewGraphic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().add_element_user(PROPERTY_QNAME[3]);
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

    /**
     * Gets the "bwMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum getBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bwMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode xgetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "bwMode" attribute
     */
    @Override
    public boolean isSetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "bwMode" attribute
     */
    @Override
    public void setBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum bwMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(bwMode);
        }
    }

    /**
     * Sets (as xml) the "bwMode" attribute
     */
    @Override
    public void xsetBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode bwMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(bwMode);
        }
    }

    /**
     * Unsets the "bwMode" attribute
     */
    @Override
    public void unsetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

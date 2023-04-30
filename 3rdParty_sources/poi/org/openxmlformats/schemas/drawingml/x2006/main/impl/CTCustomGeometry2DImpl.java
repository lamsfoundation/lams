/*
 * XML Type:  CT_CustomGeometry2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTCustomGeometry2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CustomGeometry2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTCustomGeometry2DImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTCustomGeometry2D {
    private static final long serialVersionUID = 1L;

    public CTCustomGeometry2DImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "avLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "gdLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ahLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cxnLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "rect"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pathLst"),
    };


    /**
     * Gets the "avLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList getAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "avLst" element
     */
    @Override
    public boolean isSetAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "avLst" element
     */
    @Override
    public void setAvLst(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList avLst) {
        generatedSetterHelperImpl(avLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "avLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList addNewAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "avLst" element
     */
    @Override
    public void unsetAvLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "gdLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList getGdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "gdLst" element
     */
    @Override
    public boolean isSetGdLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "gdLst" element
     */
    @Override
    public void setGdLst(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList gdLst) {
        generatedSetterHelperImpl(gdLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gdLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList addNewGdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuideList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "gdLst" element
     */
    @Override
    public void unsetGdLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "ahLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList getAhLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ahLst" element
     */
    @Override
    public boolean isSetAhLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "ahLst" element
     */
    @Override
    public void setAhLst(org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList ahLst) {
        generatedSetterHelperImpl(ahLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ahLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList addNewAhLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjustHandleList)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "ahLst" element
     */
    @Override
    public void unsetAhLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "cxnLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList getCxnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cxnLst" element
     */
    @Override
    public boolean isSetCxnLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "cxnLst" element
     */
    @Override
    public void setCxnLst(org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList cxnLst) {
        generatedSetterHelperImpl(cxnLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cxnLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList addNewCxnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "cxnLst" element
     */
    @Override
    public void unsetCxnLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "rect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect getRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rect" element
     */
    @Override
    public boolean isSetRect() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "rect" element
     */
    @Override
    public void setRect(org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect rect) {
        generatedSetterHelperImpl(rect, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rect" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect addNewRect() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGeomRect)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "rect" element
     */
    @Override
    public void unsetRect() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "pathLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList getPathLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "pathLst" element
     */
    @Override
    public void setPathLst(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList pathLst) {
        generatedSetterHelperImpl(pathLst, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pathLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList addNewPathLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }
}

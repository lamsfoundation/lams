/*
 * XML Type:  CT_TableCellBorderStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TableCellBorderStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTableCellBorderStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle {
    private static final long serialVersionUID = 1L;

    public CTTableCellBorderStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "left"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "right"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "top"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "bottom"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "insideH"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "insideV"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tl2br"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tr2bl"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
    };


    /**
     * Gets the "left" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle getLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "left" element
     */
    @Override
    public boolean isSetLeft() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "left" element
     */
    @Override
    public void setLeft(org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle left) {
        generatedSetterHelperImpl(left, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "left" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle addNewLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "left" element
     */
    @Override
    public void unsetLeft() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "right" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle getRight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "right" element
     */
    @Override
    public boolean isSetRight() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "right" element
     */
    @Override
    public void setRight(org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle right) {
        generatedSetterHelperImpl(right, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "right" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle addNewRight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "right" element
     */
    @Override
    public void unsetRight() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "top" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle getTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "top" element
     */
    @Override
    public boolean isSetTop() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "top" element
     */
    @Override
    public void setTop(org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle top) {
        generatedSetterHelperImpl(top, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "top" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle addNewTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "top" element
     */
    @Override
    public void unsetTop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "bottom" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle getBottom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bottom" element
     */
    @Override
    public boolean isSetBottom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "bottom" element
     */
    @Override
    public void setBottom(org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle bottom) {
        generatedSetterHelperImpl(bottom, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bottom" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle addNewBottom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "bottom" element
     */
    @Override
    public void unsetBottom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "insideH" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle getInsideH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "insideH" element
     */
    @Override
    public boolean isSetInsideH() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "insideH" element
     */
    @Override
    public void setInsideH(org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle insideH) {
        generatedSetterHelperImpl(insideH, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "insideH" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle addNewInsideH() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "insideH" element
     */
    @Override
    public void unsetInsideH() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "insideV" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle getInsideV() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "insideV" element
     */
    @Override
    public boolean isSetInsideV() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "insideV" element
     */
    @Override
    public void setInsideV(org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle insideV) {
        generatedSetterHelperImpl(insideV, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "insideV" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle addNewInsideV() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "insideV" element
     */
    @Override
    public void unsetInsideV() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "tl2br" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle getTl2Br() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tl2br" element
     */
    @Override
    public boolean isSetTl2Br() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "tl2br" element
     */
    @Override
    public void setTl2Br(org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle tl2Br) {
        generatedSetterHelperImpl(tl2Br, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tl2br" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle addNewTl2Br() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "tl2br" element
     */
    @Override
    public void unsetTl2Br() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "tr2bl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle getTr2Bl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tr2bl" element
     */
    @Override
    public boolean isSetTr2Bl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "tr2bl" element
     */
    @Override
    public void setTr2Bl(org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle tr2Bl) {
        generatedSetterHelperImpl(tr2Bl, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tr2bl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle addNewTr2Bl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "tr2bl" element
     */
    @Override
    public void unsetTr2Bl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[8], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[8]);
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
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }
}

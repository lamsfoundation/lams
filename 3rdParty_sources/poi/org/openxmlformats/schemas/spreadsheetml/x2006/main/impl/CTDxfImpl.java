/*
 * XML Type:  CT_Dxf
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Dxf(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTDxfImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf {
    private static final long serialVersionUID = 1L;

    public CTDxfImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "font"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "numFmt"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "fill"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "alignment"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "border"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "protection"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
    };


    /**
     * Gets the "font" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont getFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "font" element
     */
    @Override
    public boolean isSetFont() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "font" element
     */
    @Override
    public void setFont(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont font) {
        generatedSetterHelperImpl(font, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "font" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont addNewFont() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "font" element
     */
    @Override
    public void unsetFont() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "numFmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt getNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numFmt" element
     */
    @Override
    public boolean isSetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "numFmt" element
     */
    @Override
    public void setNumFmt(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt numFmt) {
        generatedSetterHelperImpl(numFmt, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numFmt" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt addNewNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTNumFmt)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "numFmt" element
     */
    @Override
    public void unsetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "fill" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill getFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fill" element
     */
    @Override
    public boolean isSetFill() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "fill" element
     */
    @Override
    public void setFill(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill fill) {
        generatedSetterHelperImpl(fill, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fill" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill addNewFill() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "fill" element
     */
    @Override
    public void unsetFill() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "alignment" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment getAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "alignment" element
     */
    @Override
    public boolean isSetAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "alignment" element
     */
    @Override
    public void setAlignment(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment alignment) {
        generatedSetterHelperImpl(alignment, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "alignment" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment addNewAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "alignment" element
     */
    @Override
    public void unsetAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "border" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder getBorder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "border" element
     */
    @Override
    public boolean isSetBorder() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "border" element
     */
    @Override
    public void setBorder(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder border) {
        generatedSetterHelperImpl(border, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "border" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder addNewBorder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "border" element
     */
    @Override
    public void unsetBorder() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "protection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection getProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "protection" element
     */
    @Override
    public boolean isSetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "protection" element
     */
    @Override
    public void setProtection(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection protection) {
        generatedSetterHelperImpl(protection, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "protection" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection addNewProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "protection" element
     */
    @Override
    public void unsetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }
}

/*
 * XML Type:  CT_Border
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Border(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTBorderImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder {
    private static final long serialVersionUID = 1L;

    public CTBorderImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "start"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "end"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "left"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "right"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "top"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "bottom"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "diagonal"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "vertical"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "horizontal"),
        new QName("", "diagonalUp"),
        new QName("", "diagonalDown"),
        new QName("", "outline"),
    };


    /**
     * Gets the "start" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "start" element
     */
    @Override
    public boolean isSetStart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "start" element
     */
    @Override
    public void setStart(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr start) {
        generatedSetterHelperImpl(start, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "start" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "start" element
     */
    @Override
    public void unsetStart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "end" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "end" element
     */
    @Override
    public boolean isSetEnd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "end" element
     */
    @Override
    public void setEnd(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr end) {
        generatedSetterHelperImpl(end, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "end" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewEnd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "end" element
     */
    @Override
    public void unsetEnd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "left" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "left" element
     */
    @Override
    public void setLeft(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr left) {
        generatedSetterHelperImpl(left, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "left" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewLeft() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "right" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getRight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "right" element
     */
    @Override
    public void setRight(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr right) {
        generatedSetterHelperImpl(right, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "right" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewRight() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "top" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "top" element
     */
    @Override
    public void setTop(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr top) {
        generatedSetterHelperImpl(top, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "top" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "bottom" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getBottom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "bottom" element
     */
    @Override
    public void setBottom(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr bottom) {
        generatedSetterHelperImpl(bottom, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bottom" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewBottom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "diagonal" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getDiagonal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "diagonal" element
     */
    @Override
    public boolean isSetDiagonal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "diagonal" element
     */
    @Override
    public void setDiagonal(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr diagonal) {
        generatedSetterHelperImpl(diagonal, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "diagonal" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewDiagonal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "diagonal" element
     */
    @Override
    public void unsetDiagonal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "vertical" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getVertical() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "vertical" element
     */
    @Override
    public boolean isSetVertical() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "vertical" element
     */
    @Override
    public void setVertical(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr vertical) {
        generatedSetterHelperImpl(vertical, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "vertical" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewVertical() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "vertical" element
     */
    @Override
    public void unsetVertical() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "horizontal" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr getHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "horizontal" element
     */
    @Override
    public boolean isSetHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "horizontal" element
     */
    @Override
    public void setHorizontal(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr horizontal) {
        generatedSetterHelperImpl(horizontal, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "horizontal" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr addNewHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "horizontal" element
     */
    @Override
    public void unsetHorizontal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "diagonalUp" attribute
     */
    @Override
    public boolean getDiagonalUp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "diagonalUp" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDiagonalUp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "diagonalUp" attribute
     */
    @Override
    public boolean isSetDiagonalUp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "diagonalUp" attribute
     */
    @Override
    public void setDiagonalUp(boolean diagonalUp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(diagonalUp);
        }
    }

    /**
     * Sets (as xml) the "diagonalUp" attribute
     */
    @Override
    public void xsetDiagonalUp(org.apache.xmlbeans.XmlBoolean diagonalUp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(diagonalUp);
        }
    }

    /**
     * Unsets the "diagonalUp" attribute
     */
    @Override
    public void unsetDiagonalUp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "diagonalDown" attribute
     */
    @Override
    public boolean getDiagonalDown() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "diagonalDown" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetDiagonalDown() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "diagonalDown" attribute
     */
    @Override
    public boolean isSetDiagonalDown() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "diagonalDown" attribute
     */
    @Override
    public void setDiagonalDown(boolean diagonalDown) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setBooleanValue(diagonalDown);
        }
    }

    /**
     * Sets (as xml) the "diagonalDown" attribute
     */
    @Override
    public void xsetDiagonalDown(org.apache.xmlbeans.XmlBoolean diagonalDown) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(diagonalDown);
        }
    }

    /**
     * Unsets the "diagonalDown" attribute
     */
    @Override
    public void unsetDiagonalDown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "outline" attribute
     */
    @Override
    public boolean getOutline() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "outline" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[11]);
            }
            return target;
        }
    }

    /**
     * True if has "outline" attribute
     */
    @Override
    public boolean isSetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "outline" attribute
     */
    @Override
    public void setOutline(boolean outline) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setBooleanValue(outline);
        }
    }

    /**
     * Sets (as xml) the "outline" attribute
     */
    @Override
    public void xsetOutline(org.apache.xmlbeans.XmlBoolean outline) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(outline);
        }
    }

    /**
     * Unsets the "outline" attribute
     */
    @Override
    public void unsetOutline() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }
}

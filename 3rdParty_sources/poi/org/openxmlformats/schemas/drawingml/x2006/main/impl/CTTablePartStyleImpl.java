/*
 * XML Type:  CT_TablePartStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TablePartStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTablePartStyleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTablePartStyle {
    private static final long serialVersionUID = 1L;

    public CTTablePartStyleImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tcTxStyle"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tcStyle"),
    };


    /**
     * Gets the "tcTxStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle getTcTxStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcTxStyle" element
     */
    @Override
    public boolean isSetTcTxStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tcTxStyle" element
     */
    @Override
    public void setTcTxStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle tcTxStyle) {
        generatedSetterHelperImpl(tcTxStyle, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcTxStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle addNewTcTxStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleTextStyle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tcTxStyle" element
     */
    @Override
    public void unsetTcTxStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "tcStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle getTcStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcStyle" element
     */
    @Override
    public boolean isSetTcStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "tcStyle" element
     */
    @Override
    public void setTcStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle tcStyle) {
        generatedSetterHelperImpl(tcStyle, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle addNewTcStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "tcStyle" element
     */
    @Override
    public void unsetTcStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

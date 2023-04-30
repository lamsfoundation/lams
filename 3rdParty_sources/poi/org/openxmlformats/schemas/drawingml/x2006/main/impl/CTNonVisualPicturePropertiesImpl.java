/*
 * XML Type:  CT_NonVisualPictureProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NonVisualPictureProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTNonVisualPicturePropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualPictureProperties {
    private static final long serialVersionUID = 1L;

    public CTNonVisualPicturePropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "picLocks"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "preferRelativeResize"),
    };


    /**
     * Gets the "picLocks" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking getPicLocks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "picLocks" element
     */
    @Override
    public boolean isSetPicLocks() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "picLocks" element
     */
    @Override
    public void setPicLocks(org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking picLocks) {
        generatedSetterHelperImpl(picLocks, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "picLocks" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking addNewPicLocks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPictureLocking)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "picLocks" element
     */
    @Override
    public void unsetPicLocks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "preferRelativeResize" attribute
     */
    @Override
    public boolean getPreferRelativeResize() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "preferRelativeResize" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPreferRelativeResize() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "preferRelativeResize" attribute
     */
    @Override
    public boolean isSetPreferRelativeResize() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "preferRelativeResize" attribute
     */
    @Override
    public void setPreferRelativeResize(boolean preferRelativeResize) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(preferRelativeResize);
        }
    }

    /**
     * Sets (as xml) the "preferRelativeResize" attribute
     */
    @Override
    public void xsetPreferRelativeResize(org.apache.xmlbeans.XmlBoolean preferRelativeResize) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(preferRelativeResize);
        }
    }

    /**
     * Unsets the "preferRelativeResize" attribute
     */
    @Override
    public void unsetPreferRelativeResize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

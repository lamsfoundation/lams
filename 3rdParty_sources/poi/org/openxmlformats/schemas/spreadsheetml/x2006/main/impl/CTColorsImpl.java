/*
 * XML Type:  CT_Colors
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Colors(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTColorsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColors {
    private static final long serialVersionUID = 1L;

    public CTColorsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "indexedColors"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "mruColors"),
    };


    /**
     * Gets the "indexedColors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors getIndexedColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "indexedColors" element
     */
    @Override
    public boolean isSetIndexedColors() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "indexedColors" element
     */
    @Override
    public void setIndexedColors(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors indexedColors) {
        generatedSetterHelperImpl(indexedColors, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "indexedColors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors addNewIndexedColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIndexedColors)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "indexedColors" element
     */
    @Override
    public void unsetIndexedColors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "mruColors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors getMruColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mruColors" element
     */
    @Override
    public boolean isSetMruColors() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "mruColors" element
     */
    @Override
    public void setMruColors(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors mruColors) {
        generatedSetterHelperImpl(mruColors, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mruColors" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors addNewMruColors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMRUColors)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "mruColors" element
     */
    @Override
    public void unsetMruColors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

/*
 * XML Type:  CT_ColorSchemeAndMapping
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorSchemeAndMapping(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTColorSchemeAndMappingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeAndMapping {
    private static final long serialVersionUID = 1L;

    public CTColorSchemeAndMappingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "clrScheme"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "clrMap"),
    };


    /**
     * Gets the "clrScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme getClrScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "clrScheme" element
     */
    @Override
    public void setClrScheme(org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme clrScheme) {
        generatedSetterHelperImpl(clrScheme, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrScheme" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme addNewClrScheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorScheme)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "clrMap" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping getClrMap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clrMap" element
     */
    @Override
    public boolean isSetClrMap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "clrMap" element
     */
    @Override
    public void setClrMap(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping clrMap) {
        generatedSetterHelperImpl(clrMap, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrMap" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping addNewClrMap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "clrMap" element
     */
    @Override
    public void unsetClrMap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

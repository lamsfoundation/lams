/*
 * XML Type:  CT_ColorMappingOverride
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ColorMappingOverride(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTColorMappingOverrideImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride {
    private static final long serialVersionUID = 1L;

    public CTColorMappingOverrideImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "masterClrMapping"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "overrideClrMapping"),
    };


    /**
     * Gets the "masterClrMapping" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement getMasterClrMapping() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "masterClrMapping" element
     */
    @Override
    public boolean isSetMasterClrMapping() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "masterClrMapping" element
     */
    @Override
    public void setMasterClrMapping(org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement masterClrMapping) {
        generatedSetterHelperImpl(masterClrMapping, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "masterClrMapping" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement addNewMasterClrMapping() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "masterClrMapping" element
     */
    @Override
    public void unsetMasterClrMapping() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "overrideClrMapping" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping getOverrideClrMapping() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "overrideClrMapping" element
     */
    @Override
    public boolean isSetOverrideClrMapping() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "overrideClrMapping" element
     */
    @Override
    public void setOverrideClrMapping(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping overrideClrMapping) {
        generatedSetterHelperImpl(overrideClrMapping, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "overrideClrMapping" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping addNewOverrideClrMapping() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "overrideClrMapping" element
     */
    @Override
    public void unsetOverrideClrMapping() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

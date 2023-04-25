/*
 * XML Type:  CT_EffectProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTEffectProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_EffectProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTEffectPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTEffectProperties {
    private static final long serialVersionUID = 1L;

    public CTEffectPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "effectDag"),
    };


    /**
     * Gets the "effectLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList getEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "effectLst" element
     */
    @Override
    public boolean isSetEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "effectLst" element
     */
    @Override
    public void setEffectLst(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectLst) {
        generatedSetterHelperImpl(effectLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList addNewEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "effectLst" element
     */
    @Override
    public void unsetEffectLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "effectDag" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer getEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "effectDag" element
     */
    @Override
    public boolean isSetEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "effectDag" element
     */
    @Override
    public void setEffectDag(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer effectDag) {
        generatedSetterHelperImpl(effectDag, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectDag" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer addNewEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "effectDag" element
     */
    @Override
    public void unsetEffectDag() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

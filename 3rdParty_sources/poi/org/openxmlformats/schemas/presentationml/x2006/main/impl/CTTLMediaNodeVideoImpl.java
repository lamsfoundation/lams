/*
 * XML Type:  CT_TLMediaNodeVideo
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLMediaNodeVideo(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLMediaNodeVideoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo {
    private static final long serialVersionUID = 1L;

    public CTTLMediaNodeVideoImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cMediaNode"),
        new QName("", "fullScrn"),
    };


    /**
     * Gets the "cMediaNode" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData getCMediaNode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cMediaNode" element
     */
    @Override
    public void setCMediaNode(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData cMediaNode) {
        generatedSetterHelperImpl(cMediaNode, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cMediaNode" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData addNewCMediaNode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "fullScrn" attribute
     */
    @Override
    public boolean getFullScrn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "fullScrn" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFullScrn() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "fullScrn" attribute
     */
    @Override
    public boolean isSetFullScrn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "fullScrn" attribute
     */
    @Override
    public void setFullScrn(boolean fullScrn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setBooleanValue(fullScrn);
        }
    }

    /**
     * Sets (as xml) the "fullScrn" attribute
     */
    @Override
    public void xsetFullScrn(org.apache.xmlbeans.XmlBoolean fullScrn) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(fullScrn);
        }
    }

    /**
     * Unsets the "fullScrn" attribute
     */
    @Override
    public void unsetFullScrn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

/*
 * XML Type:  CT_TLSetBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLSetBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLSetBehaviorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLSetBehavior {
    private static final long serialVersionUID = 1L;

    public CTTLSetBehaviorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cBhvr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "to"),
    };


    /**
     * Gets the "cBhvr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData getCBhvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cBhvr" element
     */
    @Override
    public void setCBhvr(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData cBhvr) {
        generatedSetterHelperImpl(cBhvr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cBhvr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData addNewCBhvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "to" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant getTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "to" element
     */
    @Override
    public boolean isSetTo() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "to" element
     */
    @Override
    public void setTo(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant to) {
        generatedSetterHelperImpl(to, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "to" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant addNewTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "to" element
     */
    @Override
    public void unsetTo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

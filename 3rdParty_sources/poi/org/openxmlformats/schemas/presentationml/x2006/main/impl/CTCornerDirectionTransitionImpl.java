/*
 * XML Type:  CT_CornerDirectionTransition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CornerDirectionTransition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTCornerDirectionTransitionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition {
    private static final long serialVersionUID = 1L;

    public CTCornerDirectionTransitionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "dir"),
    };


    /**
     * Gets the "dir" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType.Enum getDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "dir" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType xgetDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "dir" attribute
     */
    @Override
    public boolean isSetDir() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "dir" attribute
     */
    @Override
    public void setDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType.Enum dir) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(dir);
        }
    }

    /**
     * Sets (as xml) the "dir" attribute
     */
    @Override
    public void xsetDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType dir) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionCornerDirectionType)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(dir);
        }
    }

    /**
     * Unsets the "dir" attribute
     */
    @Override
    public void unsetDir() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }
}

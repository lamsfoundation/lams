/*
 * XML Type:  CT_SplitTransition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SplitTransition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSplitTransitionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition {
    private static final long serialVersionUID = 1L;

    public CTSplitTransitionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "orient"),
        new QName("", "dir"),
    };


    /**
     * Gets the "orient" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum getOrient() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "orient" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STDirection xgetOrient() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STDirection target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STDirection)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STDirection)get_default_attribute_value(PROPERTY_QNAME[0]);
            }
            return target;
        }
    }

    /**
     * True if has "orient" attribute
     */
    @Override
    public boolean isSetOrient() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "orient" attribute
     */
    @Override
    public void setOrient(org.openxmlformats.schemas.presentationml.x2006.main.STDirection.Enum orient) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(orient);
        }
    }

    /**
     * Sets (as xml) the "orient" attribute
     */
    @Override
    public void xsetOrient(org.openxmlformats.schemas.presentationml.x2006.main.STDirection orient) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STDirection target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STDirection)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STDirection)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(orient);
        }
    }

    /**
     * Unsets the "orient" attribute
     */
    @Override
    public void unsetOrient() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "dir" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType.Enum getDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "dir" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType xgetDir() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType)get_default_attribute_value(PROPERTY_QNAME[1]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "dir" attribute
     */
    @Override
    public void setDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType.Enum dir) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(dir);
        }
    }

    /**
     * Sets (as xml) the "dir" attribute
     */
    @Override
    public void xsetDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType dir) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

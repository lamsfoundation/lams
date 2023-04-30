/*
 * XML Type:  CT_TLAnimateScaleBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLAnimateScaleBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLAnimateScaleBehaviorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateScaleBehavior {
    private static final long serialVersionUID = 1L;

    public CTTLAnimateScaleBehaviorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cBhvr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "by"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "from"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "to"),
        new QName("", "zoomContents"),
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
     * Gets the "by" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "by" element
     */
    @Override
    public boolean isSetBy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "by" element
     */
    @Override
    public void setBy(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint by) {
        generatedSetterHelperImpl(by, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "by" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "by" element
     */
    @Override
    public void unsetBy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "from" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "from" element
     */
    @Override
    public boolean isSetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "from" element
     */
    @Override
    public void setFrom(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint from) {
        generatedSetterHelperImpl(from, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "from" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "from" element
     */
    @Override
    public void unsetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "to" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "to" element
     */
    @Override
    public void setTo(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint to) {
        generatedSetterHelperImpl(to, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "to" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "zoomContents" attribute
     */
    @Override
    public boolean getZoomContents() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "zoomContents" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetZoomContents() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "zoomContents" attribute
     */
    @Override
    public boolean isSetZoomContents() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "zoomContents" attribute
     */
    @Override
    public void setZoomContents(boolean zoomContents) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(zoomContents);
        }
    }

    /**
     * Sets (as xml) the "zoomContents" attribute
     */
    @Override
    public void xsetZoomContents(org.apache.xmlbeans.XmlBoolean zoomContents) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(zoomContents);
        }
    }

    /**
     * Unsets the "zoomContents" attribute
     */
    @Override
    public void unsetZoomContents() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

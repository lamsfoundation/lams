/*
 * XML Type:  CT_TLTimeAnimateValue
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTimeAnimateValue(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTimeAnimateValueImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue {
    private static final long serialVersionUID = 1L;

    public CTTLTimeAnimateValueImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "val"),
        new QName("", "tm"),
        new QName("", "fmla"),
    };


    /**
     * Gets the "val" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant getVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "val" element
     */
    @Override
    public boolean isSetVal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "val" element
     */
    @Override
    public void setVal(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant val) {
        generatedSetterHelperImpl(val, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "val" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant addNewVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "val" element
     */
    @Override
    public void unsetVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "tm" attribute
     */
    @Override
    public java.lang.Object getTm() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "tm" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime xgetTm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "tm" attribute
     */
    @Override
    public boolean isSetTm() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "tm" attribute
     */
    @Override
    public void setTm(java.lang.Object tm) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(tm);
        }
    }

    /**
     * Sets (as xml) the "tm" attribute
     */
    @Override
    public void xsetTm(org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime tm) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeAnimateValueTime)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(tm);
        }
    }

    /**
     * Unsets the "tm" attribute
     */
    @Override
    public void unsetTm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "fmla" attribute
     */
    @Override
    public java.lang.String getFmla() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "fmla" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetFmla() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "fmla" attribute
     */
    @Override
    public boolean isSetFmla() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "fmla" attribute
     */
    @Override
    public void setFmla(java.lang.String fmla) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(fmla);
        }
    }

    /**
     * Sets (as xml) the "fmla" attribute
     */
    @Override
    public void xsetFmla(org.apache.xmlbeans.XmlString fmla) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(fmla);
        }
    }

    /**
     * Unsets the "fmla" attribute
     */
    @Override
    public void unsetFmla() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

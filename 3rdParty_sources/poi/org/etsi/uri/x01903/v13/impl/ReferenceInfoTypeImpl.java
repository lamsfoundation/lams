/*
 * XML Type:  ReferenceInfoType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ReferenceInfoType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML ReferenceInfoType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public class ReferenceInfoTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.etsi.uri.x01903.v13.ReferenceInfoType {
    private static final long serialVersionUID = 1L;

    public ReferenceInfoTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethod"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue"),
        new QName("", "Id"),
        new QName("", "URI"),
    };


    /**
     * Gets the "DigestMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DigestMethodType getDigestMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestMethodType)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "DigestMethod" element
     */
    @Override
    public void setDigestMethod(org.w3.x2000.x09.xmldsig.DigestMethodType digestMethod) {
        generatedSetterHelperImpl(digestMethod, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DigestMethod" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DigestMethodType addNewDigestMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestMethodType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestMethodType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "DigestValue" element
     */
    @Override
    public byte[] getDigestValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "DigestValue" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.DigestValueType xgetDigestValue() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * Sets the "DigestValue" element
     */
    @Override
    public void setDigestValue(byte[] digestValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setByteArrayValue(digestValue);
        }
    }

    /**
     * Sets (as xml) the "DigestValue" element
     */
    @Override
    public void xsetDigestValue(org.w3.x2000.x09.xmldsig.DigestValueType digestValue) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.DigestValueType target = null;
            target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.DigestValueType)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(digestValue);
        }
    }

    /**
     * Gets the "Id" attribute
     */
    @Override
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Id" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlID xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "Id" attribute
     */
    @Override
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "Id" attribute
     */
    @Override
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "Id" attribute
     */
    @Override
    public void xsetId(org.apache.xmlbeans.XmlID id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlID target = null;
            target = (org.apache.xmlbeans.XmlID)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlID)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "Id" attribute
     */
    @Override
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "URI" attribute
     */
    @Override
    public java.lang.String getURI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "URI" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetURI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "URI" attribute
     */
    @Override
    public boolean isSetURI() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "URI" attribute
     */
    @Override
    public void setURI(java.lang.String uri) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(uri);
        }
    }

    /**
     * Sets (as xml) the "URI" attribute
     */
    @Override
    public void xsetURI(org.apache.xmlbeans.XmlAnyURI uri) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(uri);
        }
    }

    /**
     * Unsets the "URI" attribute
     */
    @Override
    public void unsetURI() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}

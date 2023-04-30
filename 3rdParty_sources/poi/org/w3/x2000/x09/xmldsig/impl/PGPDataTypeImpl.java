/*
 * XML Type:  PGPDataType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.PGPDataType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML PGPDataType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class PGPDataTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.PGPDataType {
    private static final long serialVersionUID = 1L;

    public PGPDataTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyID"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "PGPKeyPacket"),
    };


    /**
     * Gets the "PGPKeyID" element
     */
    @Override
    public byte[] getPGPKeyID() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "PGPKeyID" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetPGPKeyID() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "PGPKeyID" element
     */
    @Override
    public boolean isSetPGPKeyID() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "PGPKeyID" element
     */
    @Override
    public void setPGPKeyID(byte[] pgpKeyID) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setByteArrayValue(pgpKeyID);
        }
    }

    /**
     * Sets (as xml) the "PGPKeyID" element
     */
    @Override
    public void xsetPGPKeyID(org.apache.xmlbeans.XmlBase64Binary pgpKeyID) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(pgpKeyID);
        }
    }

    /**
     * Unsets the "PGPKeyID" element
     */
    @Override
    public void unsetPGPKeyID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "PGPKeyPacket" element
     */
    @Override
    public byte[] getPGPKeyPacket() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "PGPKeyPacket" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetPGPKeyPacket() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "PGPKeyPacket" element
     */
    @Override
    public boolean isSetPGPKeyPacket() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "PGPKeyPacket" element
     */
    @Override
    public void setPGPKeyPacket(byte[] pgpKeyPacket) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setByteArrayValue(pgpKeyPacket);
        }
    }

    /**
     * Sets (as xml) the "PGPKeyPacket" element
     */
    @Override
    public void xsetPGPKeyPacket(org.apache.xmlbeans.XmlBase64Binary pgpKeyPacket) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(pgpKeyPacket);
        }
    }

    /**
     * Unsets the "PGPKeyPacket" element
     */
    @Override
    public void unsetPGPKeyPacket() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

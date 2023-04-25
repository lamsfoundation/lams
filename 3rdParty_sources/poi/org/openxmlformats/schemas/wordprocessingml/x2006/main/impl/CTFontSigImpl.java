/*
 * XML Type:  CT_FontSig
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FontSig(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFontSigImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFontSig {
    private static final long serialVersionUID = 1L;

    public CTFontSigImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "usb0"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "usb1"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "usb2"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "usb3"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "csb0"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "csb1"),
    };


    /**
     * Gets the "usb0" attribute
     */
    @Override
    public byte[] getUsb0() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "usb0" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetUsb0() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "usb0" attribute
     */
    @Override
    public void setUsb0(byte[] usb0) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setByteArrayValue(usb0);
        }
    }

    /**
     * Sets (as xml) the "usb0" attribute
     */
    @Override
    public void xsetUsb0(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber usb0) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(usb0);
        }
    }

    /**
     * Gets the "usb1" attribute
     */
    @Override
    public byte[] getUsb1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "usb1" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetUsb1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "usb1" attribute
     */
    @Override
    public void setUsb1(byte[] usb1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setByteArrayValue(usb1);
        }
    }

    /**
     * Sets (as xml) the "usb1" attribute
     */
    @Override
    public void xsetUsb1(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber usb1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(usb1);
        }
    }

    /**
     * Gets the "usb2" attribute
     */
    @Override
    public byte[] getUsb2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "usb2" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetUsb2() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "usb2" attribute
     */
    @Override
    public void setUsb2(byte[] usb2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setByteArrayValue(usb2);
        }
    }

    /**
     * Sets (as xml) the "usb2" attribute
     */
    @Override
    public void xsetUsb2(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber usb2) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(usb2);
        }
    }

    /**
     * Gets the "usb3" attribute
     */
    @Override
    public byte[] getUsb3() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "usb3" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetUsb3() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "usb3" attribute
     */
    @Override
    public void setUsb3(byte[] usb3) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setByteArrayValue(usb3);
        }
    }

    /**
     * Sets (as xml) the "usb3" attribute
     */
    @Override
    public void xsetUsb3(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber usb3) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(usb3);
        }
    }

    /**
     * Gets the "csb0" attribute
     */
    @Override
    public byte[] getCsb0() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "csb0" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetCsb0() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Sets the "csb0" attribute
     */
    @Override
    public void setCsb0(byte[] csb0) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setByteArrayValue(csb0);
        }
    }

    /**
     * Sets (as xml) the "csb0" attribute
     */
    @Override
    public void xsetCsb0(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber csb0) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(csb0);
        }
    }

    /**
     * Gets the "csb1" attribute
     */
    @Override
    public byte[] getCsb1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "csb1" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetCsb1() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Sets the "csb1" attribute
     */
    @Override
    public void setCsb1(byte[] csb1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setByteArrayValue(csb1);
        }
    }

    /**
     * Sets (as xml) the "csb1" attribute
     */
    @Override
    public void xsetCsb1(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber csb1) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(csb1);
        }
    }
}

/*
 * XML Type:  DSAKeyValueType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.DSAKeyValueType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML DSAKeyValueType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class DSAKeyValueTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.DSAKeyValueType {
    private static final long serialVersionUID = 1L;

    public DSAKeyValueTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "P"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "Q"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "G"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "Y"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "J"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "Seed"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "PgenCounter"),
    };


    /**
     * Gets the "P" element
     */
    @Override
    public byte[] getP() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "P" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CryptoBinary xgetP() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "P" element
     */
    @Override
    public boolean isSetP() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "P" element
     */
    @Override
    public void setP(byte[] p) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setByteArrayValue(p);
        }
    }

    /**
     * Sets (as xml) the "P" element
     */
    @Override
    public void xsetP(org.w3.x2000.x09.xmldsig.CryptoBinary p) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(p);
        }
    }

    /**
     * Unsets the "P" element
     */
    @Override
    public void unsetP() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "Q" element
     */
    @Override
    public byte[] getQ() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "Q" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CryptoBinary xgetQ() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "Q" element
     */
    @Override
    public boolean isSetQ() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "Q" element
     */
    @Override
    public void setQ(byte[] q) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setByteArrayValue(q);
        }
    }

    /**
     * Sets (as xml) the "Q" element
     */
    @Override
    public void xsetQ(org.w3.x2000.x09.xmldsig.CryptoBinary q) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(q);
        }
    }

    /**
     * Unsets the "Q" element
     */
    @Override
    public void unsetQ() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "G" element
     */
    @Override
    public byte[] getG() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "G" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CryptoBinary xgetG() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return target;
        }
    }

    /**
     * True if has "G" element
     */
    @Override
    public boolean isSetG() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "G" element
     */
    @Override
    public void setG(byte[] g) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.setByteArrayValue(g);
        }
    }

    /**
     * Sets (as xml) the "G" element
     */
    @Override
    public void xsetG(org.w3.x2000.x09.xmldsig.CryptoBinary g) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.set(g);
        }
    }

    /**
     * Unsets the "G" element
     */
    @Override
    public void unsetG() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "Y" element
     */
    @Override
    public byte[] getY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "Y" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CryptoBinary xgetY() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return target;
        }
    }

    /**
     * Sets the "Y" element
     */
    @Override
    public void setY(byte[] y) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.setByteArrayValue(y);
        }
    }

    /**
     * Sets (as xml) the "Y" element
     */
    @Override
    public void xsetY(org.w3.x2000.x09.xmldsig.CryptoBinary y) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.set(y);
        }
    }

    /**
     * Gets the "J" element
     */
    @Override
    public byte[] getJ() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "J" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CryptoBinary xgetJ() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return target;
        }
    }

    /**
     * True if has "J" element
     */
    @Override
    public boolean isSetJ() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "J" element
     */
    @Override
    public void setJ(byte[] j) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[4]);
            }
            target.setByteArrayValue(j);
        }
    }

    /**
     * Sets (as xml) the "J" element
     */
    @Override
    public void xsetJ(org.w3.x2000.x09.xmldsig.CryptoBinary j) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().add_element_user(PROPERTY_QNAME[4]);
            }
            target.set(j);
        }
    }

    /**
     * Unsets the "J" element
     */
    @Override
    public void unsetJ() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "Seed" element
     */
    @Override
    public byte[] getSeed() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "Seed" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CryptoBinary xgetSeed() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return target;
        }
    }

    /**
     * True if has "Seed" element
     */
    @Override
    public boolean isSetSeed() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "Seed" element
     */
    @Override
    public void setSeed(byte[] seed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[5]);
            }
            target.setByteArrayValue(seed);
        }
    }

    /**
     * Sets (as xml) the "Seed" element
     */
    @Override
    public void xsetSeed(org.w3.x2000.x09.xmldsig.CryptoBinary seed) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().add_element_user(PROPERTY_QNAME[5]);
            }
            target.set(seed);
        }
    }

    /**
     * Unsets the "Seed" element
     */
    @Override
    public void unsetSeed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "PgenCounter" element
     */
    @Override
    public byte[] getPgenCounter() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "PgenCounter" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.CryptoBinary xgetPgenCounter() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return target;
        }
    }

    /**
     * True if has "PgenCounter" element
     */
    @Override
    public boolean isSetPgenCounter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "PgenCounter" element
     */
    @Override
    public void setPgenCounter(byte[] pgenCounter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[6]);
            }
            target.setByteArrayValue(pgenCounter);
        }
    }

    /**
     * Sets (as xml) the "PgenCounter" element
     */
    @Override
    public void xsetPgenCounter(org.w3.x2000.x09.xmldsig.CryptoBinary pgenCounter) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.CryptoBinary target = null;
            target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            if (target == null) {
                target = (org.w3.x2000.x09.xmldsig.CryptoBinary)get_store().add_element_user(PROPERTY_QNAME[6]);
            }
            target.set(pgenCounter);
        }
    }

    /**
     * Unsets the "PgenCounter" element
     */
    @Override
    public void unsetPgenCounter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }
}

/*
 * XML Type:  CT_TabStop
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TabStop(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTabStopImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop {
    private static final long serialVersionUID = 1L;

    public CTTabStopImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "val"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "leader"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pos"),
    };


    /**
     * Gets the "val" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.Enum getVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "val" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc xgetVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "val" attribute
     */
    @Override
    public void setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.Enum val) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(val);
        }
    }

    /**
     * Sets (as xml) the "val" attribute
     */
    @Override
    public void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc val) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(val);
        }
    }

    /**
     * Gets the "leader" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc.Enum getLeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "leader" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc xgetLeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "leader" attribute
     */
    @Override
    public boolean isSetLeader() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "leader" attribute
     */
    @Override
    public void setLeader(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc.Enum leader) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(leader);
        }
    }

    /**
     * Sets (as xml) the "leader" attribute
     */
    @Override
    public void xsetLeader(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc leader) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(leader);
        }
    }

    /**
     * Unsets the "leader" attribute
     */
    @Override
    public void unsetLeader() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "pos" attribute
     */
    @Override
    public java.lang.Object getPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "pos" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "pos" attribute
     */
    @Override
    public void setPos(java.lang.Object pos) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(pos);
        }
    }

    /**
     * Sets (as xml) the "pos" attribute
     */
    @Override
    public void xsetPos(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure pos) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(pos);
        }
    }
}

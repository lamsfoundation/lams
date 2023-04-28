/*
 * XML Type:  CT_Proof
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Proof(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTProofImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof {
    private static final long serialVersionUID = 1L;

    public CTProofImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "spelling"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "grammar"),
    };


    /**
     * Gets the "spelling" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum getSpelling() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "spelling" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof xgetSpelling() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "spelling" attribute
     */
    @Override
    public boolean isSetSpelling() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "spelling" attribute
     */
    @Override
    public void setSpelling(org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum spelling) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setEnumValue(spelling);
        }
    }

    /**
     * Sets (as xml) the "spelling" attribute
     */
    @Override
    public void xsetSpelling(org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof spelling) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(spelling);
        }
    }

    /**
     * Unsets the "spelling" attribute
     */
    @Override
    public void unsetSpelling() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "grammar" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum getGrammar() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "grammar" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof xgetGrammar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "grammar" attribute
     */
    @Override
    public boolean isSetGrammar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "grammar" attribute
     */
    @Override
    public void setGrammar(org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum grammar) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setEnumValue(grammar);
        }
    }

    /**
     * Sets (as xml) the "grammar" attribute
     */
    @Override
    public void xsetGrammar(org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof grammar) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(grammar);
        }
    }

    /**
     * Unsets the "grammar" attribute
     */
    @Override
    public void unsetGrammar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

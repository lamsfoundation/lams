/*
 * XML Type:  CT_FtnEdnSepRef
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FtnEdnSepRef(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTFtnEdnSepRefImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef {
    private static final long serialVersionUID = 1L;

    public CTFtnEdnSepRefImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "id"),
    };


    /**
     * Gets the "id" attribute
     */
    @Override
    public java.math.BigInteger getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "id" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "id" attribute
     */
    @Override
    public void setId(java.math.BigInteger id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setBigIntegerValue(id);
        }
    }

    /**
     * Sets (as xml) the "id" attribute
     */
    @Override
    public void xsetId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber id) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(id);
        }
    }
}

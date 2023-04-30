/*
 * XML Type:  CT_TrackChangeNumbering
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TrackChangeNumbering(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTrackChangeNumberingImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering {
    private static final long serialVersionUID = 1L;

    public CTTrackChangeNumberingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "original"),
    };


    /**
     * Gets the "original" attribute
     */
    @Override
    public java.lang.String getOriginal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "original" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetOriginal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "original" attribute
     */
    @Override
    public boolean isSetOriginal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "original" attribute
     */
    @Override
    public void setOriginal(java.lang.String original) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(original);
        }
    }

    /**
     * Sets (as xml) the "original" attribute
     */
    @Override
    public void xsetOriginal(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString original) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(original);
        }
    }

    /**
     * Unsets the "original" attribute
     */
    @Override
    public void unsetOriginal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }
}

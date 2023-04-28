/*
 * XML Type:  CT_MoveBookmark
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MoveBookmark(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTMoveBookmarkImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTBookmarkImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMoveBookmark {
    private static final long serialVersionUID = 1L;

    public CTMoveBookmarkImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "author"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "date"),
    };


    /**
     * Gets the "author" attribute
     */
    @Override
    public java.lang.String getAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "author" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "author" attribute
     */
    @Override
    public void setAuthor(java.lang.String author) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(author);
        }
    }

    /**
     * Sets (as xml) the "author" attribute
     */
    @Override
    public void xsetAuthor(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString author) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(author);
        }
    }

    /**
     * Gets the "date" attribute
     */
    @Override
    public java.util.Calendar getDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * Gets (as xml) the "date" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime xgetDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "date" attribute
     */
    @Override
    public void setDate(java.util.Calendar date) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setCalendarValue(date);
        }
    }

    /**
     * Sets (as xml) the "date" attribute
     */
    @Override
    public void xsetDate(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime date) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(date);
        }
    }
}

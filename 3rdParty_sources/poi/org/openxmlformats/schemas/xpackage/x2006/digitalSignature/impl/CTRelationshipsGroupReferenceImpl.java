/*
 * XML Type:  CT_RelationshipsGroupReference
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RelationshipsGroupReference(@http://schemas.openxmlformats.org/package/2006/digital-signature).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference.
 */
public class CTRelationshipsGroupReferenceImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference {
    private static final long serialVersionUID = 1L;

    public CTRelationshipsGroupReferenceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType, true);
    }

    protected CTRelationshipsGroupReferenceImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
        super(sType, b);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "SourceType"),
    };


    /**
     * Gets the "SourceType" attribute
     */
    @Override
    public java.lang.String getSourceType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "SourceType" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetSourceType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "SourceType" attribute
     */
    @Override
    public void setSourceType(java.lang.String sourceType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(sourceType);
        }
    }

    /**
     * Sets (as xml) the "SourceType" attribute
     */
    @Override
    public void xsetSourceType(org.apache.xmlbeans.XmlAnyURI sourceType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(sourceType);
        }
    }
}

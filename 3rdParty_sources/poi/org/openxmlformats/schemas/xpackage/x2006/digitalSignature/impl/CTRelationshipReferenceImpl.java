/*
 * XML Type:  CT_RelationshipReference
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_RelationshipReference(@http://schemas.openxmlformats.org/package/2006/digital-signature).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference.
 */
public class CTRelationshipReferenceImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference {
    private static final long serialVersionUID = 1L;

    public CTRelationshipReferenceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType, true);
    }

    protected CTRelationshipReferenceImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
        super(sType, b);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "SourceId"),
    };


    /**
     * Gets the "SourceId" attribute
     */
    @Override
    public java.lang.String getSourceId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "SourceId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetSourceId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "SourceId" attribute
     */
    @Override
    public void setSourceId(java.lang.String sourceId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(sourceId);
        }
    }

    /**
     * Sets (as xml) the "SourceId" attribute
     */
    @Override
    public void xsetSourceId(org.apache.xmlbeans.XmlString sourceId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(sourceId);
        }
    }
}

/*
 * An XML document type.
 * Localname: RelationshipReference
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipReferenceDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one RelationshipReference(@http://schemas.openxmlformats.org/package/2006/digital-signature) element.
 *
 * This is a complex type.
 */
public class RelationshipReferenceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipReferenceDocument {
    private static final long serialVersionUID = 1L;

    public RelationshipReferenceDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/package/2006/digital-signature", "RelationshipReference"),
    };


    /**
     * Gets the "RelationshipReference" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference getRelationshipReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "RelationshipReference" element
     */
    @Override
    public void setRelationshipReference(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference relationshipReference) {
        generatedSetterHelperImpl(relationshipReference, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "RelationshipReference" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference addNewRelationshipReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

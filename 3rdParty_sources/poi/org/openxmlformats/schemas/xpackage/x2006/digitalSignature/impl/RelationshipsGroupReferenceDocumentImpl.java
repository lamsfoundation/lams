/*
 * An XML document type.
 * Localname: RelationshipsGroupReference
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipsGroupReferenceDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one RelationshipsGroupReference(@http://schemas.openxmlformats.org/package/2006/digital-signature) element.
 *
 * This is a complex type.
 */
public class RelationshipsGroupReferenceDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipsGroupReferenceDocument {
    private static final long serialVersionUID = 1L;

    public RelationshipsGroupReferenceDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/package/2006/digital-signature", "RelationshipsGroupReference"),
    };


    /**
     * Gets the "RelationshipsGroupReference" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference getRelationshipsGroupReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "RelationshipsGroupReference" element
     */
    @Override
    public void setRelationshipsGroupReference(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference relationshipsGroupReference) {
        generatedSetterHelperImpl(relationshipsGroupReference, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "RelationshipsGroupReference" element
     */
    @Override
    public org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference addNewRelationshipsGroupReference() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference target = null;
            target = (org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

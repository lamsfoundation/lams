/*
 * An XML document type.
 * Localname: RelationshipReference
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipReferenceDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one RelationshipReference(@http://schemas.openxmlformats.org/package/2006/digital-signature) element.
 *
 * This is a complex type.
 */
public interface RelationshipReferenceDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipReferenceDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "relationshipreference8903doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "RelationshipReference" element
     */
    org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference getRelationshipReference();

    /**
     * Sets the "RelationshipReference" element
     */
    void setRelationshipReference(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference relationshipReference);

    /**
     * Appends and returns a new empty "RelationshipReference" element
     */
    org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference addNewRelationshipReference();
}

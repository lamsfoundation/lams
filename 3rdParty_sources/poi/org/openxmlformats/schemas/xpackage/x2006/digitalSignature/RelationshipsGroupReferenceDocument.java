/*
 * An XML document type.
 * Localname: RelationshipsGroupReference
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipsGroupReferenceDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one RelationshipsGroupReference(@http://schemas.openxmlformats.org/package/2006/digital-signature) element.
 *
 * This is a complex type.
 */
public interface RelationshipsGroupReferenceDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.digitalSignature.RelationshipsGroupReferenceDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "relationshipsgroupreference9f0fdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "RelationshipsGroupReference" element
     */
    org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference getRelationshipsGroupReference();

    /**
     * Sets the "RelationshipsGroupReference" element
     */
    void setRelationshipsGroupReference(org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference relationshipsGroupReference);

    /**
     * Appends and returns a new empty "RelationshipsGroupReference" element
     */
    org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference addNewRelationshipsGroupReference();
}

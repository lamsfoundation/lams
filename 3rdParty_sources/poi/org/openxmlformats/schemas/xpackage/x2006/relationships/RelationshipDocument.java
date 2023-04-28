/*
 * An XML document type.
 * Localname: Relationship
 * Namespace: http://schemas.openxmlformats.org/package/2006/relationships
 * Java type: org.openxmlformats.schemas.xpackage.x2006.relationships.RelationshipDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one Relationship(@http://schemas.openxmlformats.org/package/2006/relationships) element.
 *
 * This is a complex type.
 */
public interface RelationshipDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.relationships.RelationshipDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "relationshipfe04doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Relationship" element
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship getRelationship();

    /**
     * Sets the "Relationship" element
     */
    void setRelationship(org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship relationship);

    /**
     * Appends and returns a new empty "Relationship" element
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship addNewRelationship();
}

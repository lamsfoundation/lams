/*
 * An XML document type.
 * Localname: Relationships
 * Namespace: http://schemas.openxmlformats.org/package/2006/relationships
 * Java type: org.openxmlformats.schemas.xpackage.x2006.relationships.RelationshipsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one Relationships(@http://schemas.openxmlformats.org/package/2006/relationships) element.
 *
 * This is a complex type.
 */
public interface RelationshipsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.relationships.RelationshipsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "relationships93b3doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Relationships" element
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships getRelationships();

    /**
     * Sets the "Relationships" element
     */
    void setRelationships(org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships relationships);

    /**
     * Appends and returns a new empty "Relationships" element
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships addNewRelationships();
}

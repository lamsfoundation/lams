/*
 * XML Type:  CT_Relationships
 * Namespace: http://schemas.openxmlformats.org/package/2006/relationships
 * Java type: org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Relationships(@http://schemas.openxmlformats.org/package/2006/relationships).
 *
 * This is a complex type.
 */
public interface CTRelationships extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationships> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrelationshipse33ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "Relationship" elements
     */
    java.util.List<org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship> getRelationshipList();

    /**
     * Gets array of all "Relationship" elements
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship[] getRelationshipArray();

    /**
     * Gets ith "Relationship" element
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship getRelationshipArray(int i);

    /**
     * Returns number of "Relationship" element
     */
    int sizeOfRelationshipArray();

    /**
     * Sets array of all "Relationship" element
     */
    void setRelationshipArray(org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship[] relationshipArray);

    /**
     * Sets ith "Relationship" element
     */
    void setRelationshipArray(int i, org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship relationship);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Relationship" element
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship insertNewRelationship(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Relationship" element
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship addNewRelationship();

    /**
     * Removes the ith "Relationship" element
     */
    void removeRelationship(int i);
}

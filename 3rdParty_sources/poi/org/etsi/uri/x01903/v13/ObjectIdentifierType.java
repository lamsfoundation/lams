/*
 * XML Type:  ObjectIdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ObjectIdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ObjectIdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface ObjectIdentifierType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.ObjectIdentifierType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "objectidentifiertype2f56type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Identifier" element
     */
    org.etsi.uri.x01903.v13.IdentifierType getIdentifier();

    /**
     * Sets the "Identifier" element
     */
    void setIdentifier(org.etsi.uri.x01903.v13.IdentifierType identifier);

    /**
     * Appends and returns a new empty "Identifier" element
     */
    org.etsi.uri.x01903.v13.IdentifierType addNewIdentifier();

    /**
     * Gets the "Description" element
     */
    java.lang.String getDescription();

    /**
     * Gets (as xml) the "Description" element
     */
    org.apache.xmlbeans.XmlString xgetDescription();

    /**
     * True if has "Description" element
     */
    boolean isSetDescription();

    /**
     * Sets the "Description" element
     */
    void setDescription(java.lang.String description);

    /**
     * Sets (as xml) the "Description" element
     */
    void xsetDescription(org.apache.xmlbeans.XmlString description);

    /**
     * Unsets the "Description" element
     */
    void unsetDescription();

    /**
     * Gets the "DocumentationReferences" element
     */
    org.etsi.uri.x01903.v13.DocumentationReferencesType getDocumentationReferences();

    /**
     * True if has "DocumentationReferences" element
     */
    boolean isSetDocumentationReferences();

    /**
     * Sets the "DocumentationReferences" element
     */
    void setDocumentationReferences(org.etsi.uri.x01903.v13.DocumentationReferencesType documentationReferences);

    /**
     * Appends and returns a new empty "DocumentationReferences" element
     */
    org.etsi.uri.x01903.v13.DocumentationReferencesType addNewDocumentationReferences();

    /**
     * Unsets the "DocumentationReferences" element
     */
    void unsetDocumentationReferences();
}

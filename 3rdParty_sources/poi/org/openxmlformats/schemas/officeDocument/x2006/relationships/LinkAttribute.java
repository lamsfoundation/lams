/*
 * An XML attribute type.
 * Localname: link
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.LinkAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one link(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface LinkAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.LinkAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "linkf4d1attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "link" attribute
     */
    java.lang.String getLink();

    /**
     * Gets (as xml) the "link" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetLink();

    /**
     * True if has "link" attribute
     */
    boolean isSetLink();

    /**
     * Sets the "link" attribute
     */
    void setLink(java.lang.String link);

    /**
     * Sets (as xml) the "link" attribute
     */
    void xsetLink(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId link);

    /**
     * Unsets the "link" attribute
     */
    void unsetLink();
}

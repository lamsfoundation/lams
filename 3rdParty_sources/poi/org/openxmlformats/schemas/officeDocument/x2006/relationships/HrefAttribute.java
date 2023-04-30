/*
 * An XML attribute type.
 * Localname: href
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.HrefAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one href(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface HrefAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.HrefAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "href3b82attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "href" attribute
     */
    java.lang.String getHref();

    /**
     * Gets (as xml) the "href" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetHref();

    /**
     * True if has "href" attribute
     */
    boolean isSetHref();

    /**
     * Sets the "href" attribute
     */
    void setHref(java.lang.String href);

    /**
     * Sets (as xml) the "href" attribute
     */
    void xsetHref(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId href);

    /**
     * Unsets the "href" attribute
     */
    void unsetHref();
}

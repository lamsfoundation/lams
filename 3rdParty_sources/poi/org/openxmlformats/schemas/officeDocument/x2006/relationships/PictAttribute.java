/*
 * An XML attribute type.
 * Localname: pict
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.PictAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one pict(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface PictAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.PictAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "pictad01attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pict" attribute
     */
    java.lang.String getPict();

    /**
     * Gets (as xml) the "pict" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetPict();

    /**
     * True if has "pict" attribute
     */
    boolean isSetPict();

    /**
     * Sets the "pict" attribute
     */
    void setPict(java.lang.String pict);

    /**
     * Sets (as xml) the "pict" attribute
     */
    void xsetPict(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId pict);

    /**
     * Unsets the "pict" attribute
     */
    void unsetPict();
}

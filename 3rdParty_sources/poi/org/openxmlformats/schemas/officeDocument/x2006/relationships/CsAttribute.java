/*
 * An XML attribute type.
 * Localname: cs
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.CsAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one cs(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface CsAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.CsAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cs4507attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cs" attribute
     */
    java.lang.String getCs();

    /**
     * Gets (as xml) the "cs" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetCs();

    /**
     * True if has "cs" attribute
     */
    boolean isSetCs();

    /**
     * Sets the "cs" attribute
     */
    void setCs(java.lang.String cs);

    /**
     * Sets (as xml) the "cs" attribute
     */
    void xsetCs(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId cs);

    /**
     * Unsets the "cs" attribute
     */
    void unsetCs();
}

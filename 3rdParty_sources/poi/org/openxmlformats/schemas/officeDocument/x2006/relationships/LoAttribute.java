/*
 * An XML attribute type.
 * Localname: lo
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.LoAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one lo(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface LoAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.LoAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "lo6c9aattrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "lo" attribute
     */
    java.lang.String getLo();

    /**
     * Gets (as xml) the "lo" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetLo();

    /**
     * True if has "lo" attribute
     */
    boolean isSetLo();

    /**
     * Sets the "lo" attribute
     */
    void setLo(java.lang.String lo);

    /**
     * Sets (as xml) the "lo" attribute
     */
    void xsetLo(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId lo);

    /**
     * Unsets the "lo" attribute
     */
    void unsetLo();
}

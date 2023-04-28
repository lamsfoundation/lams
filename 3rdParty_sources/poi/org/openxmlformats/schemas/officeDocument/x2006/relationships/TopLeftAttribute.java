/*
 * An XML attribute type.
 * Localname: topLeft
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.TopLeftAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one topLeft(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface TopLeftAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.TopLeftAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "topleft849dattrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "topLeft" attribute
     */
    java.lang.String getTopLeft();

    /**
     * Gets (as xml) the "topLeft" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetTopLeft();

    /**
     * True if has "topLeft" attribute
     */
    boolean isSetTopLeft();

    /**
     * Sets the "topLeft" attribute
     */
    void setTopLeft(java.lang.String topLeft);

    /**
     * Sets (as xml) the "topLeft" attribute
     */
    void xsetTopLeft(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId topLeft);

    /**
     * Unsets the "topLeft" attribute
     */
    void unsetTopLeft();
}

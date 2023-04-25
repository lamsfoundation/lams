/*
 * An XML attribute type.
 * Localname: topRight
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.TopRightAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one topRight(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface TopRightAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.TopRightAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "toprightbe9eattrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "topRight" attribute
     */
    java.lang.String getTopRight();

    /**
     * Gets (as xml) the "topRight" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetTopRight();

    /**
     * True if has "topRight" attribute
     */
    boolean isSetTopRight();

    /**
     * Sets the "topRight" attribute
     */
    void setTopRight(java.lang.String topRight);

    /**
     * Sets (as xml) the "topRight" attribute
     */
    void xsetTopRight(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId topRight);

    /**
     * Unsets the "topRight" attribute
     */
    void unsetTopRight();
}

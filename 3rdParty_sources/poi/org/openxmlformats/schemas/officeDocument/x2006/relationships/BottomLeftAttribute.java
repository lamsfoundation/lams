/*
 * An XML attribute type.
 * Localname: bottomLeft
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.BottomLeftAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one bottomLeft(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface BottomLeftAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.BottomLeftAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "bottomleftad69attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bottomLeft" attribute
     */
    java.lang.String getBottomLeft();

    /**
     * Gets (as xml) the "bottomLeft" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetBottomLeft();

    /**
     * True if has "bottomLeft" attribute
     */
    boolean isSetBottomLeft();

    /**
     * Sets the "bottomLeft" attribute
     */
    void setBottomLeft(java.lang.String bottomLeft);

    /**
     * Sets (as xml) the "bottomLeft" attribute
     */
    void xsetBottomLeft(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId bottomLeft);

    /**
     * Unsets the "bottomLeft" attribute
     */
    void unsetBottomLeft();
}

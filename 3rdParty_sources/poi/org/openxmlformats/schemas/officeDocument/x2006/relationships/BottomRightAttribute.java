/*
 * An XML attribute type.
 * Localname: bottomRight
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.BottomRightAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one bottomRight(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface BottomRightAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.BottomRightAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "bottomrightaf52attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bottomRight" attribute
     */
    java.lang.String getBottomRight();

    /**
     * Gets (as xml) the "bottomRight" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetBottomRight();

    /**
     * True if has "bottomRight" attribute
     */
    boolean isSetBottomRight();

    /**
     * Sets the "bottomRight" attribute
     */
    void setBottomRight(java.lang.String bottomRight);

    /**
     * Sets (as xml) the "bottomRight" attribute
     */
    void xsetBottomRight(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId bottomRight);

    /**
     * Unsets the "bottomRight" attribute
     */
    void unsetBottomRight();
}

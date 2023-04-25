/*
 * An XML attribute type.
 * Localname: dm
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.DmAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one dm(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface DmAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.DmAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "dm48a0attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dm" attribute
     */
    java.lang.String getDm();

    /**
     * Gets (as xml) the "dm" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetDm();

    /**
     * True if has "dm" attribute
     */
    boolean isSetDm();

    /**
     * Sets the "dm" attribute
     */
    void setDm(java.lang.String dm);

    /**
     * Sets (as xml) the "dm" attribute
     */
    void xsetDm(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId dm);

    /**
     * Unsets the "dm" attribute
     */
    void unsetDm();
}

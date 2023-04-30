/*
 * An XML attribute type.
 * Localname: blip
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.BlipAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one blip(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface BlipAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.BlipAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "blipe848attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "blip" attribute
     */
    java.lang.String getBlip();

    /**
     * Gets (as xml) the "blip" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetBlip();

    /**
     * True if has "blip" attribute
     */
    boolean isSetBlip();

    /**
     * Sets the "blip" attribute
     */
    void setBlip(java.lang.String blip);

    /**
     * Sets (as xml) the "blip" attribute
     */
    void xsetBlip(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId blip);

    /**
     * Unsets the "blip" attribute
     */
    void unsetBlip();
}

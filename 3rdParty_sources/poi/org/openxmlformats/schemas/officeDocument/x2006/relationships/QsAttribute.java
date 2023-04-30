/*
 * An XML attribute type.
 * Localname: qs
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/relationships
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.relationships.QsAttribute
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one qs(@http://schemas.openxmlformats.org/officeDocument/2006/relationships) attribute.
 *
 * This is a complex type.
 */
public interface QsAttribute extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.relationships.QsAttribute> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "qs8db9attrtypetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "qs" attribute
     */
    java.lang.String getQs();

    /**
     * Gets (as xml) the "qs" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetQs();

    /**
     * True if has "qs" attribute
     */
    boolean isSetQs();

    /**
     * Sets the "qs" attribute
     */
    void setQs(java.lang.String qs);

    /**
     * Sets (as xml) the "qs" attribute
     */
    void xsetQs(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId qs);

    /**
     * Unsets the "qs" attribute
     */
    void unsetQs();
}

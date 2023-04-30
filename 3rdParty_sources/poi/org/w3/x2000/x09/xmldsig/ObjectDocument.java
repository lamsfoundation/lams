/*
 * An XML document type.
 * Localname: Object
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.ObjectDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one Object(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface ObjectDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.ObjectDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "objectaaf0doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Object" element
     */
    org.w3.x2000.x09.xmldsig.ObjectType getObject();

    /**
     * Sets the "Object" element
     */
    void setObject(org.w3.x2000.x09.xmldsig.ObjectType object);

    /**
     * Appends and returns a new empty "Object" element
     */
    org.w3.x2000.x09.xmldsig.ObjectType addNewObject();
}

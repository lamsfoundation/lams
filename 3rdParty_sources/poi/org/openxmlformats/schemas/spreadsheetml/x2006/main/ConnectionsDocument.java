/*
 * An XML document type.
 * Localname: connections
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.ConnectionsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one connections(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface ConnectionsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.ConnectionsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "connectionsbd20doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "connections" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections getConnections();

    /**
     * Sets the "connections" element
     */
    void setConnections(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections connections);

    /**
     * Appends and returns a new empty "connections" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections addNewConnections();
}

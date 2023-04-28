/*
 * An XML document type.
 * Localname: users
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.UsersDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one users(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface UsersDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.UsersDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "users4153doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "users" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers getUsers();

    /**
     * Sets the "users" element
     */
    void setUsers(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers users);

    /**
     * Appends and returns a new empty "users" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTUsers addNewUsers();
}

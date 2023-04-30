/*
 * XML Type:  CT_Connections
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Connections(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTConnections extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnections> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconnectionsf5f4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "connection" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection> getConnectionList();

    /**
     * Gets array of all "connection" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection[] getConnectionArray();

    /**
     * Gets ith "connection" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection getConnectionArray(int i);

    /**
     * Returns number of "connection" element
     */
    int sizeOfConnectionArray();

    /**
     * Sets array of all "connection" element
     */
    void setConnectionArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection[] connectionArray);

    /**
     * Sets ith "connection" element
     */
    void setConnectionArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection connection);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "connection" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection insertNewConnection(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "connection" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConnection addNewConnection();

    /**
     * Removes the ith "connection" element
     */
    void removeConnection(int i);
}

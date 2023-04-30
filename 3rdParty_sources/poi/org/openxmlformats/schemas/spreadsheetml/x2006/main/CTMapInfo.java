/*
 * XML Type:  CT_MapInfo
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MapInfo(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMapInfo extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmapinfo1a09type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "Schema" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema> getSchemaList();

    /**
     * Gets array of all "Schema" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema[] getSchemaArray();

    /**
     * Gets ith "Schema" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema getSchemaArray(int i);

    /**
     * Returns number of "Schema" element
     */
    int sizeOfSchemaArray();

    /**
     * Sets array of all "Schema" element
     */
    void setSchemaArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema[] schemaArray);

    /**
     * Sets ith "Schema" element
     */
    void setSchemaArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema schema);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Schema" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema insertNewSchema(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Schema" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema addNewSchema();

    /**
     * Removes the ith "Schema" element
     */
    void removeSchema(int i);

    /**
     * Gets a List of "Map" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap> getMapList();

    /**
     * Gets array of all "Map" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap[] getMapArray();

    /**
     * Gets ith "Map" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap getMapArray(int i);

    /**
     * Returns number of "Map" element
     */
    int sizeOfMapArray();

    /**
     * Sets array of all "Map" element
     */
    void setMapArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap[] mapArray);

    /**
     * Sets ith "Map" element
     */
    void setMapArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap map);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Map" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap insertNewMap(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Map" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap addNewMap();

    /**
     * Removes the ith "Map" element
     */
    void removeMap(int i);

    /**
     * Gets the "SelectionNamespaces" attribute
     */
    java.lang.String getSelectionNamespaces();

    /**
     * Gets (as xml) the "SelectionNamespaces" attribute
     */
    org.apache.xmlbeans.XmlString xgetSelectionNamespaces();

    /**
     * Sets the "SelectionNamespaces" attribute
     */
    void setSelectionNamespaces(java.lang.String selectionNamespaces);

    /**
     * Sets (as xml) the "SelectionNamespaces" attribute
     */
    void xsetSelectionNamespaces(org.apache.xmlbeans.XmlString selectionNamespaces);
}

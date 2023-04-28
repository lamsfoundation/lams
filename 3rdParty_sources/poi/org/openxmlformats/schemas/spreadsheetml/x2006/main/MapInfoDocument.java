/*
 * An XML document type.
 * Localname: MapInfo
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one MapInfo(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface MapInfoDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.MapInfoDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "mapinfo5715doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "MapInfo" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo getMapInfo();

    /**
     * Sets the "MapInfo" element
     */
    void setMapInfo(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo mapInfo);

    /**
     * Appends and returns a new empty "MapInfo" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo addNewMapInfo();
}

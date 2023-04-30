/*
 * XML Type:  CT_ExternalData
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ExternalData(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTExternalData extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctexternaldata2e07type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "autoUpdate" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getAutoUpdate();

    /**
     * True if has "autoUpdate" element
     */
    boolean isSetAutoUpdate();

    /**
     * Sets the "autoUpdate" element
     */
    void setAutoUpdate(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean autoUpdate);

    /**
     * Appends and returns a new empty "autoUpdate" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewAutoUpdate();

    /**
     * Unsets the "autoUpdate" element
     */
    void unsetAutoUpdate();

    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);
}

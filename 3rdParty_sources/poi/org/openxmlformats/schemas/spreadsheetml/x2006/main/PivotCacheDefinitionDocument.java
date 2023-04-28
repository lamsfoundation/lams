/*
 * An XML document type.
 * Localname: pivotCacheDefinition
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotCacheDefinitionDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one pivotCacheDefinition(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface PivotCacheDefinitionDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotCacheDefinitionDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "pivotcachedefinitione1b0doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pivotCacheDefinition" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition getPivotCacheDefinition();

    /**
     * Sets the "pivotCacheDefinition" element
     */
    void setPivotCacheDefinition(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition pivotCacheDefinition);

    /**
     * Appends and returns a new empty "pivotCacheDefinition" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition addNewPivotCacheDefinition();
}

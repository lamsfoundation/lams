/*
 * An XML document type.
 * Localname: pivotTableDefinition
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotTableDefinitionDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one pivotTableDefinition(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface PivotTableDefinitionDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotTableDefinitionDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "pivottabledefinition07fcdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pivotTableDefinition" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition getPivotTableDefinition();

    /**
     * Sets the "pivotTableDefinition" element
     */
    void setPivotTableDefinition(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition pivotTableDefinition);

    /**
     * Appends and returns a new empty "pivotTableDefinition" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition addNewPivotTableDefinition();
}

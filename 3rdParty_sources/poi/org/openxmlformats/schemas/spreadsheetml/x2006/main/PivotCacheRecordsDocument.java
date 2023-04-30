/*
 * An XML document type.
 * Localname: pivotCacheRecords
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotCacheRecordsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one pivotCacheRecords(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public interface PivotCacheRecordsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotCacheRecordsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "pivotcacherecords120ddoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pivotCacheRecords" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords getPivotCacheRecords();

    /**
     * Sets the "pivotCacheRecords" element
     */
    void setPivotCacheRecords(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords pivotCacheRecords);

    /**
     * Appends and returns a new empty "pivotCacheRecords" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords addNewPivotCacheRecords();
}

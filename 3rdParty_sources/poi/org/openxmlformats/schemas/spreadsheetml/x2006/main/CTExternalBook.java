/*
 * XML Type:  CT_ExternalBook
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalBook
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ExternalBook(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTExternalBook extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalBook> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctexternalbookc89dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sheetNames" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetNames getSheetNames();

    /**
     * True if has "sheetNames" element
     */
    boolean isSetSheetNames();

    /**
     * Sets the "sheetNames" element
     */
    void setSheetNames(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetNames sheetNames);

    /**
     * Appends and returns a new empty "sheetNames" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetNames addNewSheetNames();

    /**
     * Unsets the "sheetNames" element
     */
    void unsetSheetNames();

    /**
     * Gets the "definedNames" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames getDefinedNames();

    /**
     * True if has "definedNames" element
     */
    boolean isSetDefinedNames();

    /**
     * Sets the "definedNames" element
     */
    void setDefinedNames(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames definedNames);

    /**
     * Appends and returns a new empty "definedNames" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalDefinedNames addNewDefinedNames();

    /**
     * Unsets the "definedNames" element
     */
    void unsetDefinedNames();

    /**
     * Gets the "sheetDataSet" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetDataSet getSheetDataSet();

    /**
     * True if has "sheetDataSet" element
     */
    boolean isSetSheetDataSet();

    /**
     * Sets the "sheetDataSet" element
     */
    void setSheetDataSet(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetDataSet sheetDataSet);

    /**
     * Appends and returns a new empty "sheetDataSet" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalSheetDataSet addNewSheetDataSet();

    /**
     * Unsets the "sheetDataSet" element
     */
    void unsetSheetDataSet();

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

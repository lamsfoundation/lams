/*
 * XML Type:  CT_DatastoreItem
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/customXml
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customXml;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DatastoreItem(@http://schemas.openxmlformats.org/officeDocument/2006/customXml).
 *
 * This is a complex type.
 */
public interface CTDatastoreItem extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreItem> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdatastoreitem24fctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "schemaRefs" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs getSchemaRefs();

    /**
     * True if has "schemaRefs" element
     */
    boolean isSetSchemaRefs();

    /**
     * Sets the "schemaRefs" element
     */
    void setSchemaRefs(org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs schemaRefs);

    /**
     * Appends and returns a new empty "schemaRefs" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs addNewSchemaRefs();

    /**
     * Unsets the "schemaRefs" element
     */
    void unsetSchemaRefs();

    /**
     * Gets the "itemID" attribute
     */
    java.lang.String getItemID();

    /**
     * Gets (as xml) the "itemID" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetItemID();

    /**
     * Sets the "itemID" attribute
     */
    void setItemID(java.lang.String itemID);

    /**
     * Sets (as xml) the "itemID" attribute
     */
    void xsetItemID(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid itemID);
}

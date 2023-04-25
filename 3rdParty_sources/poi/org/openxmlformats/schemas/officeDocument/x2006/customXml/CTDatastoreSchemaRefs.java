/*
 * XML Type:  CT_DatastoreSchemaRefs
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/customXml
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customXml;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DatastoreSchemaRefs(@http://schemas.openxmlformats.org/officeDocument/2006/customXml).
 *
 * This is a complex type.
 */
public interface CTDatastoreSchemaRefs extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRefs> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdatastoreschemarefsefeatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "schemaRef" elements
     */
    java.util.List<org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef> getSchemaRefList();

    /**
     * Gets array of all "schemaRef" elements
     */
    org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef[] getSchemaRefArray();

    /**
     * Gets ith "schemaRef" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef getSchemaRefArray(int i);

    /**
     * Returns number of "schemaRef" element
     */
    int sizeOfSchemaRefArray();

    /**
     * Sets array of all "schemaRef" element
     */
    void setSchemaRefArray(org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef[] schemaRefArray);

    /**
     * Sets ith "schemaRef" element
     */
    void setSchemaRefArray(int i, org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef schemaRef);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "schemaRef" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef insertNewSchemaRef(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "schemaRef" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customXml.CTDatastoreSchemaRef addNewSchemaRef();

    /**
     * Removes the ith "schemaRef" element
     */
    void removeSchemaRef(int i);
}

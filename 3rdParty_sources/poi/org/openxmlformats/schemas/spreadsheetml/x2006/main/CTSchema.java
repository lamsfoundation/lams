/*
 * XML Type:  CT_Schema
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Schema(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSchema extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctschema0e6atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ID" attribute
     */
    java.lang.String getID();

    /**
     * Gets (as xml) the "ID" attribute
     */
    org.apache.xmlbeans.XmlString xgetID();

    /**
     * Sets the "ID" attribute
     */
    void setID(java.lang.String id);

    /**
     * Sets (as xml) the "ID" attribute
     */
    void xsetID(org.apache.xmlbeans.XmlString id);

    /**
     * Gets the "SchemaRef" attribute
     */
    java.lang.String getSchemaRef();

    /**
     * Gets (as xml) the "SchemaRef" attribute
     */
    org.apache.xmlbeans.XmlString xgetSchemaRef();

    /**
     * True if has "SchemaRef" attribute
     */
    boolean isSetSchemaRef();

    /**
     * Sets the "SchemaRef" attribute
     */
    void setSchemaRef(java.lang.String schemaRef);

    /**
     * Sets (as xml) the "SchemaRef" attribute
     */
    void xsetSchemaRef(org.apache.xmlbeans.XmlString schemaRef);

    /**
     * Unsets the "SchemaRef" attribute
     */
    void unsetSchemaRef();

    /**
     * Gets the "Namespace" attribute
     */
    java.lang.String getNamespace();

    /**
     * Gets (as xml) the "Namespace" attribute
     */
    org.apache.xmlbeans.XmlString xgetNamespace();

    /**
     * True if has "Namespace" attribute
     */
    boolean isSetNamespace();

    /**
     * Sets the "Namespace" attribute
     */
    void setNamespace(java.lang.String namespace);

    /**
     * Sets (as xml) the "Namespace" attribute
     */
    void xsetNamespace(org.apache.xmlbeans.XmlString namespace);

    /**
     * Unsets the "Namespace" attribute
     */
    void unsetNamespace();

    /**
     * Gets the "SchemaLanguage" attribute
     */
    java.lang.String getSchemaLanguage();

    /**
     * Gets (as xml) the "SchemaLanguage" attribute
     */
    org.apache.xmlbeans.XmlToken xgetSchemaLanguage();

    /**
     * True if has "SchemaLanguage" attribute
     */
    boolean isSetSchemaLanguage();

    /**
     * Sets the "SchemaLanguage" attribute
     */
    void setSchemaLanguage(java.lang.String schemaLanguage);

    /**
     * Sets (as xml) the "SchemaLanguage" attribute
     */
    void xsetSchemaLanguage(org.apache.xmlbeans.XmlToken schemaLanguage);

    /**
     * Unsets the "SchemaLanguage" attribute
     */
    void unsetSchemaLanguage();
}

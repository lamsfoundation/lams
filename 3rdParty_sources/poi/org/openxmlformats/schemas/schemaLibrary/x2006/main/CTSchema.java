/*
 * XML Type:  CT_Schema
 * Namespace: http://schemas.openxmlformats.org/schemaLibrary/2006/main
 * Java type: org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.schemaLibrary.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Schema(@http://schemas.openxmlformats.org/schemaLibrary/2006/main).
 *
 * This is a complex type.
 */
public interface CTSchema extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchema> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctschemac919type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "uri" attribute
     */
    java.lang.String getUri();

    /**
     * Gets (as xml) the "uri" attribute
     */
    org.apache.xmlbeans.XmlString xgetUri();

    /**
     * True if has "uri" attribute
     */
    boolean isSetUri();

    /**
     * Sets the "uri" attribute
     */
    void setUri(java.lang.String uri);

    /**
     * Sets (as xml) the "uri" attribute
     */
    void xsetUri(org.apache.xmlbeans.XmlString uri);

    /**
     * Unsets the "uri" attribute
     */
    void unsetUri();

    /**
     * Gets the "manifestLocation" attribute
     */
    java.lang.String getManifestLocation();

    /**
     * Gets (as xml) the "manifestLocation" attribute
     */
    org.apache.xmlbeans.XmlString xgetManifestLocation();

    /**
     * True if has "manifestLocation" attribute
     */
    boolean isSetManifestLocation();

    /**
     * Sets the "manifestLocation" attribute
     */
    void setManifestLocation(java.lang.String manifestLocation);

    /**
     * Sets (as xml) the "manifestLocation" attribute
     */
    void xsetManifestLocation(org.apache.xmlbeans.XmlString manifestLocation);

    /**
     * Unsets the "manifestLocation" attribute
     */
    void unsetManifestLocation();

    /**
     * Gets the "schemaLocation" attribute
     */
    java.lang.String getSchemaLocation();

    /**
     * Gets (as xml) the "schemaLocation" attribute
     */
    org.apache.xmlbeans.XmlString xgetSchemaLocation();

    /**
     * True if has "schemaLocation" attribute
     */
    boolean isSetSchemaLocation();

    /**
     * Sets the "schemaLocation" attribute
     */
    void setSchemaLocation(java.lang.String schemaLocation);

    /**
     * Sets (as xml) the "schemaLocation" attribute
     */
    void xsetSchemaLocation(org.apache.xmlbeans.XmlString schemaLocation);

    /**
     * Unsets the "schemaLocation" attribute
     */
    void unsetSchemaLocation();

    /**
     * Gets the "schemaLanguage" attribute
     */
    java.lang.String getSchemaLanguage();

    /**
     * Gets (as xml) the "schemaLanguage" attribute
     */
    org.apache.xmlbeans.XmlToken xgetSchemaLanguage();

    /**
     * True if has "schemaLanguage" attribute
     */
    boolean isSetSchemaLanguage();

    /**
     * Sets the "schemaLanguage" attribute
     */
    void setSchemaLanguage(java.lang.String schemaLanguage);

    /**
     * Sets (as xml) the "schemaLanguage" attribute
     */
    void xsetSchemaLanguage(org.apache.xmlbeans.XmlToken schemaLanguage);

    /**
     * Unsets the "schemaLanguage" attribute
     */
    void unsetSchemaLanguage();
}

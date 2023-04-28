/*
 * XML Type:  DocumentationReferencesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.DocumentationReferencesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML DocumentationReferencesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface DocumentationReferencesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.DocumentationReferencesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "documentationreferencestype4bbetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "DocumentationReference" elements
     */
    java.util.List<java.lang.String> getDocumentationReferenceList();

    /**
     * Gets array of all "DocumentationReference" elements
     */
    java.lang.String[] getDocumentationReferenceArray();

    /**
     * Gets ith "DocumentationReference" element
     */
    java.lang.String getDocumentationReferenceArray(int i);

    /**
     * Gets (as xml) a List of "DocumentationReference" elements
     */
    java.util.List<org.apache.xmlbeans.XmlAnyURI> xgetDocumentationReferenceList();

    /**
     * Gets (as xml) array of all "DocumentationReference" elements
     */
    org.apache.xmlbeans.XmlAnyURI[] xgetDocumentationReferenceArray();

    /**
     * Gets (as xml) ith "DocumentationReference" element
     */
    org.apache.xmlbeans.XmlAnyURI xgetDocumentationReferenceArray(int i);

    /**
     * Returns number of "DocumentationReference" element
     */
    int sizeOfDocumentationReferenceArray();

    /**
     * Sets array of all "DocumentationReference" element
     */
    void setDocumentationReferenceArray(java.lang.String[] documentationReferenceArray);

    /**
     * Sets ith "DocumentationReference" element
     */
    void setDocumentationReferenceArray(int i, java.lang.String documentationReference);

    /**
     * Sets (as xml) array of all "DocumentationReference" element
     */
    void xsetDocumentationReferenceArray(org.apache.xmlbeans.XmlAnyURI[] documentationReferenceArray);

    /**
     * Sets (as xml) ith "DocumentationReference" element
     */
    void xsetDocumentationReferenceArray(int i, org.apache.xmlbeans.XmlAnyURI documentationReference);

    /**
     * Inserts the value as the ith "DocumentationReference" element
     */
    void insertDocumentationReference(int i, java.lang.String documentationReference);

    /**
     * Appends the value as the last "DocumentationReference" element
     */
    void addDocumentationReference(java.lang.String documentationReference);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "DocumentationReference" element
     */
    org.apache.xmlbeans.XmlAnyURI insertNewDocumentationReference(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "DocumentationReference" element
     */
    org.apache.xmlbeans.XmlAnyURI addNewDocumentationReference();

    /**
     * Removes the ith "DocumentationReference" element
     */
    void removeDocumentationReference(int i);
}

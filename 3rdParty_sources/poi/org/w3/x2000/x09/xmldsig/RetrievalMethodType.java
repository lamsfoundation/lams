/*
 * XML Type:  RetrievalMethodType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.RetrievalMethodType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML RetrievalMethodType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface RetrievalMethodType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.RetrievalMethodType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "retrievalmethodtype2862type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Transforms" element
     */
    org.w3.x2000.x09.xmldsig.TransformsType getTransforms();

    /**
     * True if has "Transforms" element
     */
    boolean isSetTransforms();

    /**
     * Sets the "Transforms" element
     */
    void setTransforms(org.w3.x2000.x09.xmldsig.TransformsType transforms);

    /**
     * Appends and returns a new empty "Transforms" element
     */
    org.w3.x2000.x09.xmldsig.TransformsType addNewTransforms();

    /**
     * Unsets the "Transforms" element
     */
    void unsetTransforms();

    /**
     * Gets the "URI" attribute
     */
    java.lang.String getURI();

    /**
     * Gets (as xml) the "URI" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetURI();

    /**
     * True if has "URI" attribute
     */
    boolean isSetURI();

    /**
     * Sets the "URI" attribute
     */
    void setURI(java.lang.String uri);

    /**
     * Sets (as xml) the "URI" attribute
     */
    void xsetURI(org.apache.xmlbeans.XmlAnyURI uri);

    /**
     * Unsets the "URI" attribute
     */
    void unsetURI();

    /**
     * Gets the "Type" attribute
     */
    java.lang.String getType();

    /**
     * Gets (as xml) the "Type" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetType();

    /**
     * True if has "Type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "Type" attribute
     */
    void setType(java.lang.String type);

    /**
     * Sets (as xml) the "Type" attribute
     */
    void xsetType(org.apache.xmlbeans.XmlAnyURI type);

    /**
     * Unsets the "Type" attribute
     */
    void unsetType();
}

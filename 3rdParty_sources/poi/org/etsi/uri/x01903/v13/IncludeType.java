/*
 * XML Type:  IncludeType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IncludeType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML IncludeType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface IncludeType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.IncludeType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "includetypee104type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "URI" attribute
     */
    java.lang.String getURI();

    /**
     * Gets (as xml) the "URI" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetURI();

    /**
     * Sets the "URI" attribute
     */
    void setURI(java.lang.String uri);

    /**
     * Sets (as xml) the "URI" attribute
     */
    void xsetURI(org.apache.xmlbeans.XmlAnyURI uri);

    /**
     * Gets the "referencedData" attribute
     */
    boolean getReferencedData();

    /**
     * Gets (as xml) the "referencedData" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetReferencedData();

    /**
     * True if has "referencedData" attribute
     */
    boolean isSetReferencedData();

    /**
     * Sets the "referencedData" attribute
     */
    void setReferencedData(boolean referencedData);

    /**
     * Sets (as xml) the "referencedData" attribute
     */
    void xsetReferencedData(org.apache.xmlbeans.XmlBoolean referencedData);

    /**
     * Unsets the "referencedData" attribute
     */
    void unsetReferencedData();
}

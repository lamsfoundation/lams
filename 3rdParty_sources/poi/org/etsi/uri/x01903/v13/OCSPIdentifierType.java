/*
 * XML Type:  OCSPIdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OCSPIdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML OCSPIdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface OCSPIdentifierType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.OCSPIdentifierType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ocspidentifiertype3968type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ResponderID" element
     */
    org.etsi.uri.x01903.v13.ResponderIDType getResponderID();

    /**
     * Sets the "ResponderID" element
     */
    void setResponderID(org.etsi.uri.x01903.v13.ResponderIDType responderID);

    /**
     * Appends and returns a new empty "ResponderID" element
     */
    org.etsi.uri.x01903.v13.ResponderIDType addNewResponderID();

    /**
     * Gets the "ProducedAt" element
     */
    java.util.Calendar getProducedAt();

    /**
     * Gets (as xml) the "ProducedAt" element
     */
    org.apache.xmlbeans.XmlDateTime xgetProducedAt();

    /**
     * Sets the "ProducedAt" element
     */
    void setProducedAt(java.util.Calendar producedAt);

    /**
     * Sets (as xml) the "ProducedAt" element
     */
    void xsetProducedAt(org.apache.xmlbeans.XmlDateTime producedAt);

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
}

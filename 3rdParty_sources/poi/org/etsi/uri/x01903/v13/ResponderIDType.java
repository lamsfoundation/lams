/*
 * XML Type:  ResponderIDType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.ResponderIDType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ResponderIDType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface ResponderIDType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.ResponderIDType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "responderidtype55b9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ByName" element
     */
    java.lang.String getByName();

    /**
     * Gets (as xml) the "ByName" element
     */
    org.apache.xmlbeans.XmlString xgetByName();

    /**
     * True if has "ByName" element
     */
    boolean isSetByName();

    /**
     * Sets the "ByName" element
     */
    void setByName(java.lang.String byName);

    /**
     * Sets (as xml) the "ByName" element
     */
    void xsetByName(org.apache.xmlbeans.XmlString byName);

    /**
     * Unsets the "ByName" element
     */
    void unsetByName();

    /**
     * Gets the "ByKey" element
     */
    byte[] getByKey();

    /**
     * Gets (as xml) the "ByKey" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetByKey();

    /**
     * True if has "ByKey" element
     */
    boolean isSetByKey();

    /**
     * Sets the "ByKey" element
     */
    void setByKey(byte[] byKey);

    /**
     * Sets (as xml) the "ByKey" element
     */
    void xsetByKey(org.apache.xmlbeans.XmlBase64Binary byKey);

    /**
     * Unsets the "ByKey" element
     */
    void unsetByKey();
}

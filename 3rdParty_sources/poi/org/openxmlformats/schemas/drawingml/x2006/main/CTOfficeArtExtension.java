/*
 * XML Type:  CT_OfficeArtExtension
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OfficeArtExtension(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOfficeArtExtension extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctofficeartextension8e53type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "uri" attribute
     */
    java.lang.String getUri();

    /**
     * Gets (as xml) the "uri" attribute
     */
    org.apache.xmlbeans.XmlToken xgetUri();

    /**
     * Sets the "uri" attribute
     */
    void setUri(java.lang.String uri);

    /**
     * Sets (as xml) the "uri" attribute
     */
    void xsetUri(org.apache.xmlbeans.XmlToken uri);
}

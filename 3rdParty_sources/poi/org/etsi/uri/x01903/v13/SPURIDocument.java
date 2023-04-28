/*
 * An XML document type.
 * Localname: SPURI
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SPURIDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SPURI(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SPURIDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SPURIDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "spuri84e1doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SPURI" element
     */
    java.lang.String getSPURI();

    /**
     * Gets (as xml) the "SPURI" element
     */
    org.apache.xmlbeans.XmlAnyURI xgetSPURI();

    /**
     * Sets the "SPURI" element
     */
    void setSPURI(java.lang.String spuri);

    /**
     * Sets (as xml) the "SPURI" element
     */
    void xsetSPURI(org.apache.xmlbeans.XmlAnyURI spuri);
}

/*
 * An XML document type.
 * Localname: SPKIData
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.SPKIDataDocument
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SPKIData(@http://www.w3.org/2000/09/xmldsig#) element.
 *
 * This is a complex type.
 */
public interface SPKIDataDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.SPKIDataDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "spkidata640adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SPKIData" element
     */
    org.w3.x2000.x09.xmldsig.SPKIDataType getSPKIData();

    /**
     * Sets the "SPKIData" element
     */
    void setSPKIData(org.w3.x2000.x09.xmldsig.SPKIDataType spkiData);

    /**
     * Appends and returns a new empty "SPKIData" element
     */
    org.w3.x2000.x09.xmldsig.SPKIDataType addNewSPKIData();
}

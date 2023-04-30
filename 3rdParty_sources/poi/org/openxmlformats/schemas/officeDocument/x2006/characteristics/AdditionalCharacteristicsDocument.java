/*
 * An XML document type.
 * Localname: additionalCharacteristics
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/characteristics
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.characteristics.AdditionalCharacteristicsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.characteristics;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one additionalCharacteristics(@http://schemas.openxmlformats.org/officeDocument/2006/characteristics) element.
 *
 * This is a complex type.
 */
public interface AdditionalCharacteristicsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.characteristics.AdditionalCharacteristicsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "additionalcharacteristicsea21doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "additionalCharacteristics" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics getAdditionalCharacteristics();

    /**
     * Sets the "additionalCharacteristics" element
     */
    void setAdditionalCharacteristics(org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics additionalCharacteristics);

    /**
     * Appends and returns a new empty "additionalCharacteristics" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics addNewAdditionalCharacteristics();
}

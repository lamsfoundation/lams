/*
 * An XML document type.
 * Localname: Properties
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/custom-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customProperties;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one Properties(@http://schemas.openxmlformats.org/officeDocument/2006/custom-properties) element.
 *
 * This is a complex type.
 */
public interface PropertiesDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.customProperties.PropertiesDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "properties288cdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Properties" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties getProperties();

    /**
     * Sets the "Properties" element
     */
    void setProperties(org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties properties);

    /**
     * Appends and returns a new empty "Properties" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties addNewProperties();
}

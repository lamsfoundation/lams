/*
 * An XML document type.
 * Localname: sldMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one sldMaster(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface SldMasterDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.SldMasterDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sldmaster5156doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sldMaster" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster getSldMaster();

    /**
     * Sets the "sldMaster" element
     */
    void setSldMaster(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster sldMaster);

    /**
     * Appends and returns a new empty "sldMaster" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster addNewSldMaster();
}

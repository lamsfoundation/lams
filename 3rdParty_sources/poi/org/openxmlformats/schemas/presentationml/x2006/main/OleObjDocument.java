/*
 * An XML document type.
 * Localname: oleObj
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.OleObjDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one oleObj(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface OleObjDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.OleObjDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "oleobj8482doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "oleObj" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject getOleObj();

    /**
     * Sets the "oleObj" element
     */
    void setOleObj(org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject oleObj);

    /**
     * Appends and returns a new empty "oleObj" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTOleObject addNewOleObj();
}

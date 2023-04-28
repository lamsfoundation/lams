/*
 * An XML document type.
 * Localname: numbering
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one numbering(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface NumberingDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.NumberingDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "numbering1c4ddoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "numbering" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering getNumbering();

    /**
     * Sets the "numbering" element
     */
    void setNumbering(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering numbering);

    /**
     * Appends and returns a new empty "numbering" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumbering addNewNumbering();
}

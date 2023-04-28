/*
 * XML Type:  CT_GlossaryDocument
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GlossaryDocument(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGlossaryDocument1 extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocumentBase {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGlossaryDocument1> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctglossarydocument922ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "docParts" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts getDocParts();

    /**
     * True if has "docParts" element
     */
    boolean isSetDocParts();

    /**
     * Sets the "docParts" element
     */
    void setDocParts(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts docParts);

    /**
     * Appends and returns a new empty "docParts" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocParts addNewDocParts();

    /**
     * Unsets the "docParts" element
     */
    void unsetDocParts();
}

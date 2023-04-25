/*
 * XML Type:  CT_DocPart
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocPart(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocPart extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPart> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocpart7f27type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "docPartPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr getDocPartPr();

    /**
     * True if has "docPartPr" element
     */
    boolean isSetDocPartPr();

    /**
     * Sets the "docPartPr" element
     */
    void setDocPartPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr docPartPr);

    /**
     * Appends and returns a new empty "docPartPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr addNewDocPartPr();

    /**
     * Unsets the "docPartPr" element
     */
    void unsetDocPartPr();

    /**
     * Gets the "docPartBody" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody getDocPartBody();

    /**
     * True if has "docPartBody" element
     */
    boolean isSetDocPartBody();

    /**
     * Sets the "docPartBody" element
     */
    void setDocPartBody(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody docPartBody);

    /**
     * Appends and returns a new empty "docPartBody" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody addNewDocPartBody();

    /**
     * Unsets the "docPartBody" element
     */
    void unsetDocPartBody();
}

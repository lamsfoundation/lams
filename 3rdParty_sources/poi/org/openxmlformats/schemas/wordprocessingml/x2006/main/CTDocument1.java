/*
 * XML Type:  CT_Document
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Document(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocument1 extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocumentBase {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocument64adtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "body" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody getBody();

    /**
     * True if has "body" element
     */
    boolean isSetBody();

    /**
     * Sets the "body" element
     */
    void setBody(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody body);

    /**
     * Appends and returns a new empty "body" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody addNewBody();

    /**
     * Unsets the "body" element
     */
    void unsetBody();

    /**
     * Gets the "conformance" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum getConformance();

    /**
     * Gets (as xml) the "conformance" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass xgetConformance();

    /**
     * True if has "conformance" attribute
     */
    boolean isSetConformance();

    /**
     * Sets the "conformance" attribute
     */
    void setConformance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass.Enum conformance);

    /**
     * Sets (as xml) the "conformance" attribute
     */
    void xsetConformance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STConformanceClass conformance);

    /**
     * Unsets the "conformance" attribute
     */
    void unsetConformance();
}

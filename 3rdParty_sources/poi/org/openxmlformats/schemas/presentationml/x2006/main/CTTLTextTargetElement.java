/*
 * XML Type:  CT_TLTextTargetElement
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTextTargetElement(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTextTargetElement extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltexttargetelement6f09type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "charRg" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange getCharRg();

    /**
     * True if has "charRg" element
     */
    boolean isSetCharRg();

    /**
     * Sets the "charRg" element
     */
    void setCharRg(org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange charRg);

    /**
     * Appends and returns a new empty "charRg" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange addNewCharRg();

    /**
     * Unsets the "charRg" element
     */
    void unsetCharRg();

    /**
     * Gets the "pRg" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange getPRg();

    /**
     * True if has "pRg" element
     */
    boolean isSetPRg();

    /**
     * Sets the "pRg" element
     */
    void setPRg(org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange pRg);

    /**
     * Appends and returns a new empty "pRg" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange addNewPRg();

    /**
     * Unsets the "pRg" element
     */
    void unsetPRg();
}

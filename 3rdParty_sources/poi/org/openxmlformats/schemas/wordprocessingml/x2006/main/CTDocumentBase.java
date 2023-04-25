/*
 * XML Type:  CT_DocumentBase
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocumentBase
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocumentBase(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocumentBase extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocumentBase> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocumentbasedf5ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "background" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground getBackground();

    /**
     * True if has "background" element
     */
    boolean isSetBackground();

    /**
     * Sets the "background" element
     */
    void setBackground(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground background);

    /**
     * Appends and returns a new empty "background" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBackground addNewBackground();

    /**
     * Unsets the "background" element
     */
    void unsetBackground();
}

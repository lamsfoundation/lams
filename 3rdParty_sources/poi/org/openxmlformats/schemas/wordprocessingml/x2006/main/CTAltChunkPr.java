/*
 * XML Type:  CT_AltChunkPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAltChunkPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AltChunkPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAltChunkPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAltChunkPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctaltchunkpr3382type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "matchSrc" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getMatchSrc();

    /**
     * True if has "matchSrc" element
     */
    boolean isSetMatchSrc();

    /**
     * Sets the "matchSrc" element
     */
    void setMatchSrc(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff matchSrc);

    /**
     * Appends and returns a new empty "matchSrc" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewMatchSrc();

    /**
     * Unsets the "matchSrc" element
     */
    void unsetMatchSrc();
}

/*
 * XML Type:  CT_AltChunk
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAltChunk
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AltChunk(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAltChunk extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAltChunk> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctaltchunk5c24type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "altChunkPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAltChunkPr getAltChunkPr();

    /**
     * True if has "altChunkPr" element
     */
    boolean isSetAltChunkPr();

    /**
     * Sets the "altChunkPr" element
     */
    void setAltChunkPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAltChunkPr altChunkPr);

    /**
     * Appends and returns a new empty "altChunkPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAltChunkPr addNewAltChunkPr();

    /**
     * Unsets the "altChunkPr" element
     */
    void unsetAltChunkPr();

    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * True if has "id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);

    /**
     * Unsets the "id" attribute
     */
    void unsetId();
}

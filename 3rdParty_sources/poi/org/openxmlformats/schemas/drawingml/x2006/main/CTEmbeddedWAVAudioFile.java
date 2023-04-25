/*
 * XML Type:  CT_EmbeddedWAVAudioFile
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_EmbeddedWAVAudioFile(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEmbeddedWAVAudioFile extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctembeddedwavaudiofile19abtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "embed" attribute
     */
    java.lang.String getEmbed();

    /**
     * Gets (as xml) the "embed" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetEmbed();

    /**
     * Sets the "embed" attribute
     */
    void setEmbed(java.lang.String embed);

    /**
     * Sets (as xml) the "embed" attribute
     */
    void xsetEmbed(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId embed);

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();

    /**
     * True if has "name" attribute
     */
    boolean isSetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);

    /**
     * Unsets the "name" attribute
     */
    void unsetName();
}

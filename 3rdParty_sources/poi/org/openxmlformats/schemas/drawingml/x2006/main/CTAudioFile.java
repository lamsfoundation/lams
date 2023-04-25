/*
 * XML Type:  CT_AudioFile
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AudioFile(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAudioFile extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAudioFile> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctaudiofile1563type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "link" attribute
     */
    java.lang.String getLink();

    /**
     * Gets (as xml) the "link" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetLink();

    /**
     * Sets the "link" attribute
     */
    void setLink(java.lang.String link);

    /**
     * Sets (as xml) the "link" attribute
     */
    void xsetLink(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId link);

    /**
     * Gets the "contentType" attribute
     */
    java.lang.String getContentType();

    /**
     * Gets (as xml) the "contentType" attribute
     */
    org.apache.xmlbeans.XmlString xgetContentType();

    /**
     * True if has "contentType" attribute
     */
    boolean isSetContentType();

    /**
     * Sets the "contentType" attribute
     */
    void setContentType(java.lang.String contentType);

    /**
     * Sets (as xml) the "contentType" attribute
     */
    void xsetContentType(org.apache.xmlbeans.XmlString contentType);

    /**
     * Unsets the "contentType" attribute
     */
    void unsetContentType();
}

/*
 * XML Type:  CT_AudioCD
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AudioCD(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAudioCD extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCD> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctaudiocdbea8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "st" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCDTime getSt();

    /**
     * Sets the "st" element
     */
    void setSt(org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCDTime st);

    /**
     * Appends and returns a new empty "st" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCDTime addNewSt();

    /**
     * Gets the "end" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCDTime getEnd();

    /**
     * Sets the "end" element
     */
    void setEnd(org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCDTime end);

    /**
     * Appends and returns a new empty "end" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCDTime addNewEnd();

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
}

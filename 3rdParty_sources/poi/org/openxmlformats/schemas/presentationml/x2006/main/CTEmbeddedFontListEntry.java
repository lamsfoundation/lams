/*
 * XML Type:  CT_EmbeddedFontListEntry
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_EmbeddedFontListEntry(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEmbeddedFontListEntry extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontListEntry> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctembeddedfontlistentry48b4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "font" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont getFont();

    /**
     * Sets the "font" element
     */
    void setFont(org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont font);

    /**
     * Appends and returns a new empty "font" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont addNewFont();

    /**
     * Gets the "regular" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId getRegular();

    /**
     * True if has "regular" element
     */
    boolean isSetRegular();

    /**
     * Sets the "regular" element
     */
    void setRegular(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId regular);

    /**
     * Appends and returns a new empty "regular" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId addNewRegular();

    /**
     * Unsets the "regular" element
     */
    void unsetRegular();

    /**
     * Gets the "bold" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId getBold();

    /**
     * True if has "bold" element
     */
    boolean isSetBold();

    /**
     * Sets the "bold" element
     */
    void setBold(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId bold);

    /**
     * Appends and returns a new empty "bold" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId addNewBold();

    /**
     * Unsets the "bold" element
     */
    void unsetBold();

    /**
     * Gets the "italic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId getItalic();

    /**
     * True if has "italic" element
     */
    boolean isSetItalic();

    /**
     * Sets the "italic" element
     */
    void setItalic(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId italic);

    /**
     * Appends and returns a new empty "italic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId addNewItalic();

    /**
     * Unsets the "italic" element
     */
    void unsetItalic();

    /**
     * Gets the "boldItalic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId getBoldItalic();

    /**
     * True if has "boldItalic" element
     */
    boolean isSetBoldItalic();

    /**
     * Sets the "boldItalic" element
     */
    void setBoldItalic(org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId boldItalic);

    /**
     * Appends and returns a new empty "boldItalic" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmbeddedFontDataId addNewBoldItalic();

    /**
     * Unsets the "boldItalic" element
     */
    void unsetBoldItalic();
}

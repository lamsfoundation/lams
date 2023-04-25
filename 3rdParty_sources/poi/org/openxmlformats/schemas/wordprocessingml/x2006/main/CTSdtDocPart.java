/*
 * XML Type:  CT_SdtDocPart
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SdtDocPart(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSdtDocPart extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsdtdocpartcea0type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "docPartGallery" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getDocPartGallery();

    /**
     * True if has "docPartGallery" element
     */
    boolean isSetDocPartGallery();

    /**
     * Sets the "docPartGallery" element
     */
    void setDocPartGallery(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString docPartGallery);

    /**
     * Appends and returns a new empty "docPartGallery" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewDocPartGallery();

    /**
     * Unsets the "docPartGallery" element
     */
    void unsetDocPartGallery();

    /**
     * Gets the "docPartCategory" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getDocPartCategory();

    /**
     * True if has "docPartCategory" element
     */
    boolean isSetDocPartCategory();

    /**
     * Sets the "docPartCategory" element
     */
    void setDocPartCategory(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString docPartCategory);

    /**
     * Appends and returns a new empty "docPartCategory" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewDocPartCategory();

    /**
     * Unsets the "docPartCategory" element
     */
    void unsetDocPartCategory();

    /**
     * Gets the "docPartUnique" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDocPartUnique();

    /**
     * True if has "docPartUnique" element
     */
    boolean isSetDocPartUnique();

    /**
     * Sets the "docPartUnique" element
     */
    void setDocPartUnique(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff docPartUnique);

    /**
     * Appends and returns a new empty "docPartUnique" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDocPartUnique();

    /**
     * Unsets the "docPartUnique" element
     */
    void unsetDocPartUnique();
}

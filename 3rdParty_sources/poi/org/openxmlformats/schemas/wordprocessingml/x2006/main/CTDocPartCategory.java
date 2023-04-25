/*
 * XML Type:  CT_DocPartCategory
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartCategory
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocPartCategory(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocPartCategory extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartCategory> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocpartcategory38c9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "name" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getName();

    /**
     * Sets the "name" element
     */
    void setName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString name);

    /**
     * Appends and returns a new empty "name" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewName();

    /**
     * Gets the "gallery" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery getGallery();

    /**
     * Sets the "gallery" element
     */
    void setGallery(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery gallery);

    /**
     * Appends and returns a new empty "gallery" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartGallery addNewGallery();
}

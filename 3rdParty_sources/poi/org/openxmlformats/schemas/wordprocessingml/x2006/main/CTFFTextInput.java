/*
 * XML Type:  CT_FFTextInput
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FFTextInput(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFFTextInput extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextInput> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfftextinput3155type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "type" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextType getType();

    /**
     * True if has "type" element
     */
    boolean isSetType();

    /**
     * Sets the "type" element
     */
    void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextType type);

    /**
     * Appends and returns a new empty "type" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextType addNewType();

    /**
     * Unsets the "type" element
     */
    void unsetType();

    /**
     * Gets the "default" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getDefault();

    /**
     * True if has "default" element
     */
    boolean isSetDefault();

    /**
     * Sets the "default" element
     */
    void setDefault(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString xdefault);

    /**
     * Appends and returns a new empty "default" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewDefault();

    /**
     * Unsets the "default" element
     */
    void unsetDefault();

    /**
     * Gets the "maxLength" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getMaxLength();

    /**
     * True if has "maxLength" element
     */
    boolean isSetMaxLength();

    /**
     * Sets the "maxLength" element
     */
    void setMaxLength(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber maxLength);

    /**
     * Appends and returns a new empty "maxLength" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewMaxLength();

    /**
     * Unsets the "maxLength" element
     */
    void unsetMaxLength();

    /**
     * Gets the "format" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getFormat();

    /**
     * True if has "format" element
     */
    boolean isSetFormat();

    /**
     * Sets the "format" element
     */
    void setFormat(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString format);

    /**
     * Appends and returns a new empty "format" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewFormat();

    /**
     * Unsets the "format" element
     */
    void unsetFormat();
}

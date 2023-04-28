/*
 * XML Type:  CT_FFCheckBox
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FFCheckBox(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFFCheckBox extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFCheckBox> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctffcheckboxf3a5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "size" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getSize();

    /**
     * True if has "size" element
     */
    boolean isSetSize();

    /**
     * Sets the "size" element
     */
    void setSize(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure size);

    /**
     * Appends and returns a new empty "size" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewSize();

    /**
     * Unsets the "size" element
     */
    void unsetSize();

    /**
     * Gets the "sizeAuto" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSizeAuto();

    /**
     * True if has "sizeAuto" element
     */
    boolean isSetSizeAuto();

    /**
     * Sets the "sizeAuto" element
     */
    void setSizeAuto(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff sizeAuto);

    /**
     * Appends and returns a new empty "sizeAuto" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSizeAuto();

    /**
     * Unsets the "sizeAuto" element
     */
    void unsetSizeAuto();

    /**
     * Gets the "default" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDefault();

    /**
     * True if has "default" element
     */
    boolean isSetDefault();

    /**
     * Sets the "default" element
     */
    void setDefault(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff xdefault);

    /**
     * Appends and returns a new empty "default" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDefault();

    /**
     * Unsets the "default" element
     */
    void unsetDefault();

    /**
     * Gets the "checked" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getChecked();

    /**
     * True if has "checked" element
     */
    boolean isSetChecked();

    /**
     * Sets the "checked" element
     */
    void setChecked(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff checked);

    /**
     * Appends and returns a new empty "checked" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewChecked();

    /**
     * Unsets the "checked" element
     */
    void unsetChecked();
}

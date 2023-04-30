/*
 * XML Type:  CT_TextField
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextField(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextField extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTextField> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextfield752ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STExternalConnectionType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STExternalConnectionType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STExternalConnectionType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STExternalConnectionType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "position" attribute
     */
    long getPosition();

    /**
     * Gets (as xml) the "position" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetPosition();

    /**
     * True if has "position" attribute
     */
    boolean isSetPosition();

    /**
     * Sets the "position" attribute
     */
    void setPosition(long position);

    /**
     * Sets (as xml) the "position" attribute
     */
    void xsetPosition(org.apache.xmlbeans.XmlUnsignedInt position);

    /**
     * Unsets the "position" attribute
     */
    void unsetPosition();
}

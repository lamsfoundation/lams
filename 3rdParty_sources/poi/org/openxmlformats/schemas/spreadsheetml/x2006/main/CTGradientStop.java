/*
 * XML Type:  CT_GradientStop
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientStop
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GradientStop(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGradientStop extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientStop> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgradientstopd51btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "color" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor getColor();

    /**
     * Sets the "color" element
     */
    void setColor(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor color);

    /**
     * Appends and returns a new empty "color" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor addNewColor();

    /**
     * Gets the "position" attribute
     */
    double getPosition();

    /**
     * Gets (as xml) the "position" attribute
     */
    org.apache.xmlbeans.XmlDouble xgetPosition();

    /**
     * Sets the "position" attribute
     */
    void setPosition(double position);

    /**
     * Sets (as xml) the "position" attribute
     */
    void xsetPosition(org.apache.xmlbeans.XmlDouble position);
}

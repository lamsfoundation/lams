/*
 * XML Type:  CT_ThemeableLineStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ThemeableLineStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTThemeableLineStyle extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTThemeableLineStyle> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctthemeablelinestyle4d95type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ln" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLn();

    /**
     * True if has "ln" element
     */
    boolean isSetLn();

    /**
     * Sets the "ln" element
     */
    void setLn(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties ln);

    /**
     * Appends and returns a new empty "ln" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLn();

    /**
     * Unsets the "ln" element
     */
    void unsetLn();

    /**
     * Gets the "lnRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getLnRef();

    /**
     * True if has "lnRef" element
     */
    boolean isSetLnRef();

    /**
     * Sets the "lnRef" element
     */
    void setLnRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference lnRef);

    /**
     * Appends and returns a new empty "lnRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewLnRef();

    /**
     * Unsets the "lnRef" element
     */
    void unsetLnRef();
}

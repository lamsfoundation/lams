/*
 * XML Type:  CT_ServerFormat
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ServerFormat(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTServerFormat extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctserverformat84e3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "culture" attribute
     */
    java.lang.String getCulture();

    /**
     * Gets (as xml) the "culture" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetCulture();

    /**
     * True if has "culture" attribute
     */
    boolean isSetCulture();

    /**
     * Sets the "culture" attribute
     */
    void setCulture(java.lang.String culture);

    /**
     * Sets (as xml) the "culture" attribute
     */
    void xsetCulture(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring culture);

    /**
     * Unsets the "culture" attribute
     */
    void unsetCulture();

    /**
     * Gets the "format" attribute
     */
    java.lang.String getFormat();

    /**
     * Gets (as xml) the "format" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetFormat();

    /**
     * True if has "format" attribute
     */
    boolean isSetFormat();

    /**
     * Sets the "format" attribute
     */
    void setFormat(java.lang.String format);

    /**
     * Sets (as xml) the "format" attribute
     */
    void xsetFormat(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring format);

    /**
     * Unsets the "format" attribute
     */
    void unsetFormat();
}

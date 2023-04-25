/*
 * XML Type:  CT_SmartTagPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SmartTagPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSmartTagPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsmarttagpr0ebctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "embed" attribute
     */
    boolean getEmbed();

    /**
     * Gets (as xml) the "embed" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetEmbed();

    /**
     * True if has "embed" attribute
     */
    boolean isSetEmbed();

    /**
     * Sets the "embed" attribute
     */
    void setEmbed(boolean embed);

    /**
     * Sets (as xml) the "embed" attribute
     */
    void xsetEmbed(org.apache.xmlbeans.XmlBoolean embed);

    /**
     * Unsets the "embed" attribute
     */
    void unsetEmbed();

    /**
     * Gets the "show" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STSmartTagShow.Enum getShow();

    /**
     * Gets (as xml) the "show" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STSmartTagShow xgetShow();

    /**
     * True if has "show" attribute
     */
    boolean isSetShow();

    /**
     * Sets the "show" attribute
     */
    void setShow(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSmartTagShow.Enum show);

    /**
     * Sets (as xml) the "show" attribute
     */
    void xsetShow(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSmartTagShow show);

    /**
     * Unsets the "show" attribute
     */
    void unsetShow();
}

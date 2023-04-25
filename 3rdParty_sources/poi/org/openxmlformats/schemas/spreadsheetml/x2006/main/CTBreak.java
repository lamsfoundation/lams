/*
 * XML Type:  CT_Break
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Break(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBreak extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBreak> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbreak815etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "id" attribute
     */
    long getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetId();

    /**
     * True if has "id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "id" attribute
     */
    void setId(long id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlUnsignedInt id);

    /**
     * Unsets the "id" attribute
     */
    void unsetId();

    /**
     * Gets the "min" attribute
     */
    long getMin();

    /**
     * Gets (as xml) the "min" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetMin();

    /**
     * True if has "min" attribute
     */
    boolean isSetMin();

    /**
     * Sets the "min" attribute
     */
    void setMin(long min);

    /**
     * Sets (as xml) the "min" attribute
     */
    void xsetMin(org.apache.xmlbeans.XmlUnsignedInt min);

    /**
     * Unsets the "min" attribute
     */
    void unsetMin();

    /**
     * Gets the "max" attribute
     */
    long getMax();

    /**
     * Gets (as xml) the "max" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetMax();

    /**
     * True if has "max" attribute
     */
    boolean isSetMax();

    /**
     * Sets the "max" attribute
     */
    void setMax(long max);

    /**
     * Sets (as xml) the "max" attribute
     */
    void xsetMax(org.apache.xmlbeans.XmlUnsignedInt max);

    /**
     * Unsets the "max" attribute
     */
    void unsetMax();

    /**
     * Gets the "man" attribute
     */
    boolean getMan();

    /**
     * Gets (as xml) the "man" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetMan();

    /**
     * True if has "man" attribute
     */
    boolean isSetMan();

    /**
     * Sets the "man" attribute
     */
    void setMan(boolean man);

    /**
     * Sets (as xml) the "man" attribute
     */
    void xsetMan(org.apache.xmlbeans.XmlBoolean man);

    /**
     * Unsets the "man" attribute
     */
    void unsetMan();

    /**
     * Gets the "pt" attribute
     */
    boolean getPt();

    /**
     * Gets (as xml) the "pt" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetPt();

    /**
     * True if has "pt" attribute
     */
    boolean isSetPt();

    /**
     * Sets the "pt" attribute
     */
    void setPt(boolean pt);

    /**
     * Sets (as xml) the "pt" attribute
     */
    void xsetPt(org.apache.xmlbeans.XmlBoolean pt);

    /**
     * Unsets the "pt" attribute
     */
    void unsetPt();
}

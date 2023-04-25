/*
 * XML Type:  CT_DataBar
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBar
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DataBar(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDataBar extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBar> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdatabar4128type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cfvo" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo> getCfvoList();

    /**
     * Gets array of all "cfvo" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo[] getCfvoArray();

    /**
     * Gets ith "cfvo" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo getCfvoArray(int i);

    /**
     * Returns number of "cfvo" element
     */
    int sizeOfCfvoArray();

    /**
     * Sets array of all "cfvo" element
     */
    void setCfvoArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo[] cfvoArray);

    /**
     * Sets ith "cfvo" element
     */
    void setCfvoArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo cfvo);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cfvo" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo insertNewCfvo(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cfvo" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo addNewCfvo();

    /**
     * Removes the ith "cfvo" element
     */
    void removeCfvo(int i);

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
     * Gets the "minLength" attribute
     */
    long getMinLength();

    /**
     * Gets (as xml) the "minLength" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetMinLength();

    /**
     * True if has "minLength" attribute
     */
    boolean isSetMinLength();

    /**
     * Sets the "minLength" attribute
     */
    void setMinLength(long minLength);

    /**
     * Sets (as xml) the "minLength" attribute
     */
    void xsetMinLength(org.apache.xmlbeans.XmlUnsignedInt minLength);

    /**
     * Unsets the "minLength" attribute
     */
    void unsetMinLength();

    /**
     * Gets the "maxLength" attribute
     */
    long getMaxLength();

    /**
     * Gets (as xml) the "maxLength" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetMaxLength();

    /**
     * True if has "maxLength" attribute
     */
    boolean isSetMaxLength();

    /**
     * Sets the "maxLength" attribute
     */
    void setMaxLength(long maxLength);

    /**
     * Sets (as xml) the "maxLength" attribute
     */
    void xsetMaxLength(org.apache.xmlbeans.XmlUnsignedInt maxLength);

    /**
     * Unsets the "maxLength" attribute
     */
    void unsetMaxLength();

    /**
     * Gets the "showValue" attribute
     */
    boolean getShowValue();

    /**
     * Gets (as xml) the "showValue" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowValue();

    /**
     * True if has "showValue" attribute
     */
    boolean isSetShowValue();

    /**
     * Sets the "showValue" attribute
     */
    void setShowValue(boolean showValue);

    /**
     * Sets (as xml) the "showValue" attribute
     */
    void xsetShowValue(org.apache.xmlbeans.XmlBoolean showValue);

    /**
     * Unsets the "showValue" attribute
     */
    void unsetShowValue();
}

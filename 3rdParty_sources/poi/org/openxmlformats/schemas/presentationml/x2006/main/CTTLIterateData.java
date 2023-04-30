/*
 * XML Type:  CT_TLIterateData
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLIterateData(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLIterateData extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateData> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttliteratedatac1c3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tmAbs" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateIntervalTime getTmAbs();

    /**
     * True if has "tmAbs" element
     */
    boolean isSetTmAbs();

    /**
     * Sets the "tmAbs" element
     */
    void setTmAbs(org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateIntervalTime tmAbs);

    /**
     * Appends and returns a new empty "tmAbs" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateIntervalTime addNewTmAbs();

    /**
     * Unsets the "tmAbs" element
     */
    void unsetTmAbs();

    /**
     * Gets the "tmPct" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateIntervalPercentage getTmPct();

    /**
     * True if has "tmPct" element
     */
    boolean isSetTmPct();

    /**
     * Sets the "tmPct" element
     */
    void setTmPct(org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateIntervalPercentage tmPct);

    /**
     * Appends and returns a new empty "tmPct" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLIterateIntervalPercentage addNewTmPct();

    /**
     * Unsets the "tmPct" element
     */
    void unsetTmPct();

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STIterateType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STIterateType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.presentationml.x2006.main.STIterateType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.presentationml.x2006.main.STIterateType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "backwards" attribute
     */
    boolean getBackwards();

    /**
     * Gets (as xml) the "backwards" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetBackwards();

    /**
     * True if has "backwards" attribute
     */
    boolean isSetBackwards();

    /**
     * Sets the "backwards" attribute
     */
    void setBackwards(boolean backwards);

    /**
     * Sets (as xml) the "backwards" attribute
     */
    void xsetBackwards(org.apache.xmlbeans.XmlBoolean backwards);

    /**
     * Unsets the "backwards" attribute
     */
    void unsetBackwards();
}

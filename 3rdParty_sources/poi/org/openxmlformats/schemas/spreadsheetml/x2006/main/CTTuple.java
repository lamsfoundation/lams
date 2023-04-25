/*
 * XML Type:  CT_Tuple
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Tuple(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTuple extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttuplef2e7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fld" attribute
     */
    long getFld();

    /**
     * Gets (as xml) the "fld" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetFld();

    /**
     * True if has "fld" attribute
     */
    boolean isSetFld();

    /**
     * Sets the "fld" attribute
     */
    void setFld(long fld);

    /**
     * Sets (as xml) the "fld" attribute
     */
    void xsetFld(org.apache.xmlbeans.XmlUnsignedInt fld);

    /**
     * Unsets the "fld" attribute
     */
    void unsetFld();

    /**
     * Gets the "hier" attribute
     */
    long getHier();

    /**
     * Gets (as xml) the "hier" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetHier();

    /**
     * True if has "hier" attribute
     */
    boolean isSetHier();

    /**
     * Sets the "hier" attribute
     */
    void setHier(long hier);

    /**
     * Sets (as xml) the "hier" attribute
     */
    void xsetHier(org.apache.xmlbeans.XmlUnsignedInt hier);

    /**
     * Unsets the "hier" attribute
     */
    void unsetHier();

    /**
     * Gets the "item" attribute
     */
    long getItem();

    /**
     * Gets (as xml) the "item" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetItem();

    /**
     * Sets the "item" attribute
     */
    void setItem(long item);

    /**
     * Sets (as xml) the "item" attribute
     */
    void xsetItem(org.apache.xmlbeans.XmlUnsignedInt item);
}

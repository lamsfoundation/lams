/*
 * XML Type:  CT_RevisionInsertSheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RevisionInsertSheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRevisionInsertSheet extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionInsertSheet> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrevisioninsertsheet6d6atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rId" attribute
     */
    long getRId();

    /**
     * Gets (as xml) the "rId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetRId();

    /**
     * Sets the "rId" attribute
     */
    void setRId(long rId);

    /**
     * Sets (as xml) the "rId" attribute
     */
    void xsetRId(org.apache.xmlbeans.XmlUnsignedInt rId);

    /**
     * Gets the "ua" attribute
     */
    boolean getUa();

    /**
     * Gets (as xml) the "ua" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUa();

    /**
     * True if has "ua" attribute
     */
    boolean isSetUa();

    /**
     * Sets the "ua" attribute
     */
    void setUa(boolean ua);

    /**
     * Sets (as xml) the "ua" attribute
     */
    void xsetUa(org.apache.xmlbeans.XmlBoolean ua);

    /**
     * Unsets the "ua" attribute
     */
    void unsetUa();

    /**
     * Gets the "ra" attribute
     */
    boolean getRa();

    /**
     * Gets (as xml) the "ra" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetRa();

    /**
     * True if has "ra" attribute
     */
    boolean isSetRa();

    /**
     * Sets the "ra" attribute
     */
    void setRa(boolean ra);

    /**
     * Sets (as xml) the "ra" attribute
     */
    void xsetRa(org.apache.xmlbeans.XmlBoolean ra);

    /**
     * Unsets the "ra" attribute
     */
    void unsetRa();

    /**
     * Gets the "sheetId" attribute
     */
    long getSheetId();

    /**
     * Gets (as xml) the "sheetId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSheetId();

    /**
     * Sets the "sheetId" attribute
     */
    void setSheetId(long sheetId);

    /**
     * Sets (as xml) the "sheetId" attribute
     */
    void xsetSheetId(org.apache.xmlbeans.XmlUnsignedInt sheetId);

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Gets the "sheetPosition" attribute
     */
    long getSheetPosition();

    /**
     * Gets (as xml) the "sheetPosition" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSheetPosition();

    /**
     * Sets the "sheetPosition" attribute
     */
    void setSheetPosition(long sheetPosition);

    /**
     * Sets (as xml) the "sheetPosition" attribute
     */
    void xsetSheetPosition(org.apache.xmlbeans.XmlUnsignedInt sheetPosition);
}

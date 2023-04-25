/*
 * XML Type:  CT_RevisionSheetRename
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RevisionSheetRename(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRevisionSheetRename extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionSheetRename> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrevisionsheetrename8a01type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

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
     * Gets the "oldName" attribute
     */
    java.lang.String getOldName();

    /**
     * Gets (as xml) the "oldName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetOldName();

    /**
     * Sets the "oldName" attribute
     */
    void setOldName(java.lang.String oldName);

    /**
     * Sets (as xml) the "oldName" attribute
     */
    void xsetOldName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring oldName);

    /**
     * Gets the "newName" attribute
     */
    java.lang.String getNewName();

    /**
     * Gets (as xml) the "newName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetNewName();

    /**
     * Sets the "newName" attribute
     */
    void setNewName(java.lang.String newName);

    /**
     * Sets (as xml) the "newName" attribute
     */
    void xsetNewName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring newName);
}

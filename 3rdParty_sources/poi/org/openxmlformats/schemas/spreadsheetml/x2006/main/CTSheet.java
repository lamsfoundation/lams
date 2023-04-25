/*
 * XML Type:  CT_Sheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Sheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSheet extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsheet4dbetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
     * Gets the "state" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState.Enum getState();

    /**
     * Gets (as xml) the "state" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState xgetState();

    /**
     * True if has "state" attribute
     */
    boolean isSetState();

    /**
     * Sets the "state" attribute
     */
    void setState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState.Enum state);

    /**
     * Sets (as xml) the "state" attribute
     */
    void xsetState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState state);

    /**
     * Unsets the "state" attribute
     */
    void unsetState();

    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);
}

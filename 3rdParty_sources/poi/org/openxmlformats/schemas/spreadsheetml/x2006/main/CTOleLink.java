/*
 * XML Type:  CT_OleLink
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleLink
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OleLink(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOleLink extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleLink> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctolelinkda21type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "oleItems" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems getOleItems();

    /**
     * True if has "oleItems" element
     */
    boolean isSetOleItems();

    /**
     * Sets the "oleItems" element
     */
    void setOleItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems oleItems);

    /**
     * Appends and returns a new empty "oleItems" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleItems addNewOleItems();

    /**
     * Unsets the "oleItems" element
     */
    void unsetOleItems();

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

    /**
     * Gets the "progId" attribute
     */
    java.lang.String getProgId();

    /**
     * Gets (as xml) the "progId" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetProgId();

    /**
     * Sets the "progId" attribute
     */
    void setProgId(java.lang.String progId);

    /**
     * Sets (as xml) the "progId" attribute
     */
    void xsetProgId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring progId);
}

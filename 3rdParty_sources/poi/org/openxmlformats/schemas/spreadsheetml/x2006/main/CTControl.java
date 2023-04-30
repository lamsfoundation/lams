/*
 * XML Type:  CT_Control
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Control(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTControl extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControl> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcontrol997ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "controlPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControlPr getControlPr();

    /**
     * True if has "controlPr" element
     */
    boolean isSetControlPr();

    /**
     * Sets the "controlPr" element
     */
    void setControlPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControlPr controlPr);

    /**
     * Appends and returns a new empty "controlPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControlPr addNewControlPr();

    /**
     * Unsets the "controlPr" element
     */
    void unsetControlPr();

    /**
     * Gets the "shapeId" attribute
     */
    long getShapeId();

    /**
     * Gets (as xml) the "shapeId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetShapeId();

    /**
     * Sets the "shapeId" attribute
     */
    void setShapeId(long shapeId);

    /**
     * Sets (as xml) the "shapeId" attribute
     */
    void xsetShapeId(org.apache.xmlbeans.XmlUnsignedInt shapeId);

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
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();

    /**
     * True if has "name" attribute
     */
    boolean isSetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);

    /**
     * Unsets the "name" attribute
     */
    void unsetName();
}

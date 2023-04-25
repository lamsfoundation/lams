/*
 * XML Type:  CT_ObjectAnchor
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ObjectAnchor(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTObjectAnchor extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctobjectanchor5a1dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "from" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker getFrom();

    /**
     * Sets the "from" element
     */
    void setFrom(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker from);

    /**
     * Appends and returns a new empty "from" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker addNewFrom();

    /**
     * Gets the "to" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker getTo();

    /**
     * Sets the "to" element
     */
    void setTo(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker to);

    /**
     * Appends and returns a new empty "to" element
     */
    org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker addNewTo();

    /**
     * Gets the "moveWithCells" attribute
     */
    boolean getMoveWithCells();

    /**
     * Gets (as xml) the "moveWithCells" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetMoveWithCells();

    /**
     * True if has "moveWithCells" attribute
     */
    boolean isSetMoveWithCells();

    /**
     * Sets the "moveWithCells" attribute
     */
    void setMoveWithCells(boolean moveWithCells);

    /**
     * Sets (as xml) the "moveWithCells" attribute
     */
    void xsetMoveWithCells(org.apache.xmlbeans.XmlBoolean moveWithCells);

    /**
     * Unsets the "moveWithCells" attribute
     */
    void unsetMoveWithCells();

    /**
     * Gets the "sizeWithCells" attribute
     */
    boolean getSizeWithCells();

    /**
     * Gets (as xml) the "sizeWithCells" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetSizeWithCells();

    /**
     * True if has "sizeWithCells" attribute
     */
    boolean isSetSizeWithCells();

    /**
     * Sets the "sizeWithCells" attribute
     */
    void setSizeWithCells(boolean sizeWithCells);

    /**
     * Sets (as xml) the "sizeWithCells" attribute
     */
    void xsetSizeWithCells(org.apache.xmlbeans.XmlBoolean sizeWithCells);

    /**
     * Unsets the "sizeWithCells" attribute
     */
    void unsetSizeWithCells();
}

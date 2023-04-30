/*
 * XML Type:  CT_ColorFilter
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ColorFilter(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTColorFilter extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorFilter> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcolorfilterfddatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dxfId" attribute
     */
    long getDxfId();

    /**
     * Gets (as xml) the "dxfId" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId xgetDxfId();

    /**
     * True if has "dxfId" attribute
     */
    boolean isSetDxfId();

    /**
     * Sets the "dxfId" attribute
     */
    void setDxfId(long dxfId);

    /**
     * Sets (as xml) the "dxfId" attribute
     */
    void xsetDxfId(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDxfId dxfId);

    /**
     * Unsets the "dxfId" attribute
     */
    void unsetDxfId();

    /**
     * Gets the "cellColor" attribute
     */
    boolean getCellColor();

    /**
     * Gets (as xml) the "cellColor" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetCellColor();

    /**
     * True if has "cellColor" attribute
     */
    boolean isSetCellColor();

    /**
     * Sets the "cellColor" attribute
     */
    void setCellColor(boolean cellColor);

    /**
     * Sets (as xml) the "cellColor" attribute
     */
    void xsetCellColor(org.apache.xmlbeans.XmlBoolean cellColor);

    /**
     * Unsets the "cellColor" attribute
     */
    void unsetCellColor();
}

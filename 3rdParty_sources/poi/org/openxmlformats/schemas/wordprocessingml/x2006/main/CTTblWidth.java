/*
 * XML Type:  CT_TblWidth
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblWidth(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblWidth extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblwidthec40type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "w" attribute
     */
    java.lang.Object getW();

    /**
     * Gets (as xml) the "w" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STMeasurementOrPercent xgetW();

    /**
     * True if has "w" attribute
     */
    boolean isSetW();

    /**
     * Sets the "w" attribute
     */
    void setW(java.lang.Object w);

    /**
     * Sets (as xml) the "w" attribute
     */
    void xsetW(org.openxmlformats.schemas.wordprocessingml.x2006.main.STMeasurementOrPercent w);

    /**
     * Unsets the "w" attribute
     */
    void unsetW();

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();
}

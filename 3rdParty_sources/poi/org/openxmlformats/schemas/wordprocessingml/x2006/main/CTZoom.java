/*
 * XML Type:  CT_Zoom
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Zoom(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTZoom extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctzoomc275type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STZoom.Enum getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STZoom xgetVal();

    /**
     * True if has "val" attribute
     */
    boolean isSetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STZoom.Enum val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STZoom val);

    /**
     * Unsets the "val" attribute
     */
    void unsetVal();

    /**
     * Gets the "percent" attribute
     */
    java.lang.Object getPercent();

    /**
     * Gets (as xml) the "percent" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumberOrPercent xgetPercent();

    /**
     * Sets the "percent" attribute
     */
    void setPercent(java.lang.Object percent);

    /**
     * Sets (as xml) the "percent" attribute
     */
    void xsetPercent(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumberOrPercent percent);
}

/*
 * XML Type:  CT_LuminanceEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LuminanceEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLuminanceEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTLuminanceEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctluminanceeffectb41atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bright" attribute
     */
    java.lang.Object getBright();

    /**
     * Gets (as xml) the "bright" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetBright();

    /**
     * True if has "bright" attribute
     */
    boolean isSetBright();

    /**
     * Sets the "bright" attribute
     */
    void setBright(java.lang.Object bright);

    /**
     * Sets (as xml) the "bright" attribute
     */
    void xsetBright(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage bright);

    /**
     * Unsets the "bright" attribute
     */
    void unsetBright();

    /**
     * Gets the "contrast" attribute
     */
    java.lang.Object getContrast();

    /**
     * Gets (as xml) the "contrast" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetContrast();

    /**
     * True if has "contrast" attribute
     */
    boolean isSetContrast();

    /**
     * Sets the "contrast" attribute
     */
    void setContrast(java.lang.Object contrast);

    /**
     * Sets (as xml) the "contrast" attribute
     */
    void xsetContrast(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage contrast);

    /**
     * Unsets the "contrast" attribute
     */
    void unsetContrast();
}

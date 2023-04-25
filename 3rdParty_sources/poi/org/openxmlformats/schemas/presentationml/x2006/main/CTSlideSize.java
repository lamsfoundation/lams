/*
 * XML Type:  CT_SlideSize
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideSize(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideSize extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSize> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslidesizeb0fdtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cx" attribute
     */
    int getCx();

    /**
     * Gets (as xml) the "cx" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeCoordinate xgetCx();

    /**
     * Sets the "cx" attribute
     */
    void setCx(int cx);

    /**
     * Sets (as xml) the "cx" attribute
     */
    void xsetCx(org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeCoordinate cx);

    /**
     * Gets the "cy" attribute
     */
    int getCy();

    /**
     * Gets (as xml) the "cy" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeCoordinate xgetCy();

    /**
     * Sets the "cy" attribute
     */
    void setCy(int cy);

    /**
     * Sets (as xml) the "cy" attribute
     */
    void xsetCy(org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeCoordinate cy);

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();
}

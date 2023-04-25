/*
 * XML Type:  CT_TLByAnimateColorTransform
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLByAnimateColorTransform
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLByAnimateColorTransform(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLByAnimateColorTransform extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLByAnimateColorTransform> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlbyanimatecolortransform87b4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rgb" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform getRgb();

    /**
     * True if has "rgb" element
     */
    boolean isSetRgb();

    /**
     * Sets the "rgb" element
     */
    void setRgb(org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform rgb);

    /**
     * Appends and returns a new empty "rgb" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLByRgbColorTransform addNewRgb();

    /**
     * Unsets the "rgb" element
     */
    void unsetRgb();

    /**
     * Gets the "hsl" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform getHsl();

    /**
     * True if has "hsl" element
     */
    boolean isSetHsl();

    /**
     * Sets the "hsl" element
     */
    void setHsl(org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform hsl);

    /**
     * Appends and returns a new empty "hsl" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform addNewHsl();

    /**
     * Unsets the "hsl" element
     */
    void unsetHsl();
}

/*
 * XML Type:  CT_TLByHslColorTransform
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLByHslColorTransform(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLByHslColorTransform extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLByHslColorTransform> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlbyhslcolortransform2bd4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "h" attribute
     */
    int getH();

    /**
     * Gets (as xml) the "h" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetH();

    /**
     * Sets the "h" attribute
     */
    void setH(int h);

    /**
     * Sets (as xml) the "h" attribute
     */
    void xsetH(org.openxmlformats.schemas.drawingml.x2006.main.STAngle h);

    /**
     * Gets the "s" attribute
     */
    java.lang.Object getS();

    /**
     * Gets (as xml) the "s" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetS();

    /**
     * Sets the "s" attribute
     */
    void setS(java.lang.Object s);

    /**
     * Sets (as xml) the "s" attribute
     */
    void xsetS(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage s);

    /**
     * Gets the "l" attribute
     */
    java.lang.Object getL();

    /**
     * Gets (as xml) the "l" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage xgetL();

    /**
     * Sets the "l" attribute
     */
    void setL(java.lang.Object l);

    /**
     * Sets (as xml) the "l" attribute
     */
    void xsetL(org.openxmlformats.schemas.drawingml.x2006.main.STFixedPercentage l);
}

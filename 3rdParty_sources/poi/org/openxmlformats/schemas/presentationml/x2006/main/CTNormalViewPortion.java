/*
 * XML Type:  CT_NormalViewPortion
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NormalViewPortion(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNormalViewPortion extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTNormalViewPortion> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnormalviewportione994type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sz" attribute
     */
    java.lang.Object getSz();

    /**
     * Gets (as xml) the "sz" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetSz();

    /**
     * Sets the "sz" attribute
     */
    void setSz(java.lang.Object sz);

    /**
     * Sets (as xml) the "sz" attribute
     */
    void xsetSz(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage sz);

    /**
     * Gets the "autoAdjust" attribute
     */
    boolean getAutoAdjust();

    /**
     * Gets (as xml) the "autoAdjust" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoAdjust();

    /**
     * True if has "autoAdjust" attribute
     */
    boolean isSetAutoAdjust();

    /**
     * Sets the "autoAdjust" attribute
     */
    void setAutoAdjust(boolean autoAdjust);

    /**
     * Sets (as xml) the "autoAdjust" attribute
     */
    void xsetAutoAdjust(org.apache.xmlbeans.XmlBoolean autoAdjust);

    /**
     * Unsets the "autoAdjust" attribute
     */
    void unsetAutoAdjust();
}

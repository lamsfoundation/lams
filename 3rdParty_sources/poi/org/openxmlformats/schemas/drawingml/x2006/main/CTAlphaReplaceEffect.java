/*
 * XML Type:  CT_AlphaReplaceEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AlphaReplaceEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAlphaReplaceEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaReplaceEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctalphareplaceeffecte3c2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "a" attribute
     */
    java.lang.Object getA();

    /**
     * Gets (as xml) the "a" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetA();

    /**
     * Sets the "a" attribute
     */
    void setA(java.lang.Object a);

    /**
     * Sets (as xml) the "a" attribute
     */
    void xsetA(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage a);
}

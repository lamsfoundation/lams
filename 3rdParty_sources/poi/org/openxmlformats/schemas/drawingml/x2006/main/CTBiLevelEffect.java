/*
 * XML Type:  CT_BiLevelEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BiLevelEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBiLevelEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTBiLevelEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbileveleffectc7dftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "thresh" attribute
     */
    java.lang.Object getThresh();

    /**
     * Gets (as xml) the "thresh" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage xgetThresh();

    /**
     * Sets the "thresh" attribute
     */
    void setThresh(java.lang.Object thresh);

    /**
     * Sets (as xml) the "thresh" attribute
     */
    void xsetThresh(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveFixedPercentage thresh);
}

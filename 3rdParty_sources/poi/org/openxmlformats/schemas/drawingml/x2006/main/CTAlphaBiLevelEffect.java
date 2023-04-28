/*
 * XML Type:  CT_AlphaBiLevelEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AlphaBiLevelEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAlphaBiLevelEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaBiLevelEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctalphabileveleffect690btype");
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

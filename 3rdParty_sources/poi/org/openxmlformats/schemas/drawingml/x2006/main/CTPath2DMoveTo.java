/*
 * XML Type:  CT_Path2DMoveTo
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Path2DMoveTo(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPath2DMoveTo extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpath2dmovetoa01etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D getPt();

    /**
     * Sets the "pt" element
     */
    void setPt(org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D pt);

    /**
     * Appends and returns a new empty "pt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D addNewPt();
}

/*
 * XML Type:  CT_CTCategory
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CTCategory(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTCTCategory extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTCategory> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctctcategorya262type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "type" attribute
     */
    java.lang.String getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetType();

    /**
     * Sets the "type" attribute
     */
    void setType(java.lang.String type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.apache.xmlbeans.XmlAnyURI type);

    /**
     * Gets the "pri" attribute
     */
    long getPri();

    /**
     * Gets (as xml) the "pri" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetPri();

    /**
     * Sets the "pri" attribute
     */
    void setPri(long pri);

    /**
     * Sets (as xml) the "pri" attribute
     */
    void xsetPri(org.apache.xmlbeans.XmlUnsignedInt pri);
}

/*
 * XML Type:  CT_GraphicalObject
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GraphicalObject(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGraphicalObject extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgraphicalobject1ce3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "graphicData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData getGraphicData();

    /**
     * Sets the "graphicData" element
     */
    void setGraphicData(org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData graphicData);

    /**
     * Appends and returns a new empty "graphicData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData addNewGraphicData();
}

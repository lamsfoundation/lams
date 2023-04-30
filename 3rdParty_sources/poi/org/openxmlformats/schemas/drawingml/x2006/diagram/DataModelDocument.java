/*
 * An XML document type.
 * Localname: dataModel
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.DataModelDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one dataModel(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public interface DataModelDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.DataModelDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "datamodelf6b0doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dataModel" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel getDataModel();

    /**
     * Sets the "dataModel" element
     */
    void setDataModel(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel dataModel);

    /**
     * Appends and returns a new empty "dataModel" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel addNewDataModel();
}

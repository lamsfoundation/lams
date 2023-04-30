/*
 * XML Type:  CT_SampleData
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SampleData(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTSampleData extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsampledata6dfdtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dataModel" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel getDataModel();

    /**
     * True if has "dataModel" element
     */
    boolean isSetDataModel();

    /**
     * Sets the "dataModel" element
     */
    void setDataModel(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel dataModel);

    /**
     * Appends and returns a new empty "dataModel" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel addNewDataModel();

    /**
     * Unsets the "dataModel" element
     */
    void unsetDataModel();

    /**
     * Gets the "useDef" attribute
     */
    boolean getUseDef();

    /**
     * Gets (as xml) the "useDef" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUseDef();

    /**
     * True if has "useDef" attribute
     */
    boolean isSetUseDef();

    /**
     * Sets the "useDef" attribute
     */
    void setUseDef(boolean useDef);

    /**
     * Sets (as xml) the "useDef" attribute
     */
    void xsetUseDef(org.apache.xmlbeans.XmlBoolean useDef);

    /**
     * Unsets the "useDef" attribute
     */
    void unsetUseDef();
}

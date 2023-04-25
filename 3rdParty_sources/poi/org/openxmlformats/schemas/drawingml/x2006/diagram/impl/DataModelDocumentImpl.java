/*
 * An XML document type.
 * Localname: dataModel
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.DataModelDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one dataModel(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class DataModelDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.DataModelDocument {
    private static final long serialVersionUID = 1L;

    public DataModelDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "dataModel"),
    };


    /**
     * Gets the "dataModel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel getDataModel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "dataModel" element
     */
    @Override
    public void setDataModel(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel dataModel) {
        generatedSetterHelperImpl(dataModel, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dataModel" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel addNewDataModel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

/*
 * An XML document type.
 * Localname: layoutDef
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one layoutDef(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class LayoutDefDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefDocument {
    private static final long serialVersionUID = 1L;

    public LayoutDefDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "layoutDef"),
    };


    /**
     * Gets the "layoutDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition getLayoutDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "layoutDef" element
     */
    @Override
    public void setLayoutDef(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition layoutDef) {
        generatedSetterHelperImpl(layoutDef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "layoutDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition addNewLayoutDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

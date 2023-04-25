/*
 * An XML document type.
 * Localname: layoutDefHdrLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefHdrLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one layoutDefHdrLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class LayoutDefHdrLstDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.LayoutDefHdrLstDocument {
    private static final long serialVersionUID = 1L;

    public LayoutDefHdrLstDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "layoutDefHdrLst"),
    };


    /**
     * Gets the "layoutDefHdrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst getLayoutDefHdrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "layoutDefHdrLst" element
     */
    @Override
    public void setLayoutDefHdrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst layoutDefHdrLst) {
        generatedSetterHelperImpl(layoutDefHdrLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "layoutDefHdrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst addNewLayoutDefHdrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinitionHeaderLst)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

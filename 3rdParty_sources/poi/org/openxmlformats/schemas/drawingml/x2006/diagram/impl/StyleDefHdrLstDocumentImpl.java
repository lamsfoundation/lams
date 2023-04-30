/*
 * An XML document type.
 * Localname: styleDefHdrLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefHdrLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one styleDefHdrLst(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class StyleDefHdrLstDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefHdrLstDocument {
    private static final long serialVersionUID = 1L;

    public StyleDefHdrLstDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "styleDefHdrLst"),
    };


    /**
     * Gets the "styleDefHdrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst getStyleDefHdrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "styleDefHdrLst" element
     */
    @Override
    public void setStyleDefHdrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst styleDefHdrLst) {
        generatedSetterHelperImpl(styleDefHdrLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styleDefHdrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst addNewStyleDefHdrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeaderLst)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

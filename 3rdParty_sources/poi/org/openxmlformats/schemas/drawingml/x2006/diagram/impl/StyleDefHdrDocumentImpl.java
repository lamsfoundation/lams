/*
 * An XML document type.
 * Localname: styleDefHdr
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefHdrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one styleDefHdr(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class StyleDefHdrDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefHdrDocument {
    private static final long serialVersionUID = 1L;

    public StyleDefHdrDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "styleDefHdr"),
    };


    /**
     * Gets the "styleDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader getStyleDefHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "styleDefHdr" element
     */
    @Override
    public void setStyleDefHdr(org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader styleDefHdr) {
        generatedSetterHelperImpl(styleDefHdr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styleDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader addNewStyleDefHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinitionHeader)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

/*
 * An XML document type.
 * Localname: anchor
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.AnchorDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one anchor(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing) element.
 *
 * This is a complex type.
 */
public class AnchorDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.AnchorDocument {
    private static final long serialVersionUID = 1L;

    public AnchorDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "anchor"),
    };


    /**
     * Gets the "anchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor getAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "anchor" element
     */
    @Override
    public void setAnchor(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor anchor) {
        generatedSetterHelperImpl(anchor, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "anchor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor addNewAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

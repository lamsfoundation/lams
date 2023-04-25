/*
 * An XML document type.
 * Localname: wgp
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.WgpDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one wgp(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing) element.
 *
 * This is a complex type.
 */
public class WgpDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.WgpDocument {
    private static final long serialVersionUID = 1L;

    public WgpDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "wgp"),
    };


    /**
     * Gets the "wgp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup getWgp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "wgp" element
     */
    @Override
    public void setWgp(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup wgp) {
        generatedSetterHelperImpl(wgp, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wgp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup addNewWgp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingGroup)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

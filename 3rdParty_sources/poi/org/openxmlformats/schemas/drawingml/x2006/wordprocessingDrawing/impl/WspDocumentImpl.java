/*
 * An XML document type.
 * Localname: wsp
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.WspDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one wsp(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing) element.
 *
 * This is a complex type.
 */
public class WspDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.WspDocument {
    private static final long serialVersionUID = 1L;

    public WspDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "wsp"),
    };


    /**
     * Gets the "wsp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape getWsp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "wsp" element
     */
    @Override
    public void setWsp(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape wsp) {
        generatedSetterHelperImpl(wsp, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wsp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape addNewWsp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

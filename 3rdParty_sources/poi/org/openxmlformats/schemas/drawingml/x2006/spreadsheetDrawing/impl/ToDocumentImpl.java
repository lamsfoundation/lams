/*
 * An XML document type.
 * Localname: to
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.ToDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one to(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing) element.
 *
 * This is a complex type.
 */
public class ToDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.ToDocument {
    private static final long serialVersionUID = 1L;

    public ToDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "to"),
    };


    /**
     * Gets the "to" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker getTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "to" element
     */
    @Override
    public void setTo(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker to) {
        generatedSetterHelperImpl(to, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "to" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker addNewTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

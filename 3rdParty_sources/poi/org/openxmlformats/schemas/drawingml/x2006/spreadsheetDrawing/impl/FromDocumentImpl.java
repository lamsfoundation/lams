/*
 * An XML document type.
 * Localname: from
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.FromDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one from(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing) element.
 *
 * This is a complex type.
 */
public class FromDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.FromDocument {
    private static final long serialVersionUID = 1L;

    public FromDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "from"),
    };


    /**
     * Gets the "from" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker getFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "from" element
     */
    @Override
    public void setFrom(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker from) {
        generatedSetterHelperImpl(from, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "from" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker addNewFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

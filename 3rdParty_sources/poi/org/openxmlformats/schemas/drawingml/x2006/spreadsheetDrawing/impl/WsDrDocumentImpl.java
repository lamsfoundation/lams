/*
 * An XML document type.
 * Localname: wsDr
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.WsDrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one wsDr(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing) element.
 *
 * This is a complex type.
 */
public class WsDrDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.WsDrDocument {
    private static final long serialVersionUID = 1L;

    public WsDrDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "wsDr"),
    };


    /**
     * Gets the "wsDr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing getWsDr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "wsDr" element
     */
    @Override
    public void setWsDr(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing wsDr) {
        generatedSetterHelperImpl(wsDr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wsDr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing addNewWsDr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTDrawing)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

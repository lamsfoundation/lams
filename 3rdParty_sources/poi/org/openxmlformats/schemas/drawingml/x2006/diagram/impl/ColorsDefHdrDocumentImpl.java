/*
 * An XML document type.
 * Localname: colorsDefHdr
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefHdrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one colorsDefHdr(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class ColorsDefHdrDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefHdrDocument {
    private static final long serialVersionUID = 1L;

    public ColorsDefHdrDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "colorsDefHdr"),
    };


    /**
     * Gets the "colorsDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader getColorsDefHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "colorsDefHdr" element
     */
    @Override
    public void setColorsDefHdr(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader colorsDefHdr) {
        generatedSetterHelperImpl(colorsDefHdr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colorsDefHdr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader addNewColorsDefHdr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransformHeader)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

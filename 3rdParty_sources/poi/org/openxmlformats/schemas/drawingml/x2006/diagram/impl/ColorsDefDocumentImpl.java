/*
 * An XML document type.
 * Localname: colorsDef
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one colorsDef(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class ColorsDefDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.ColorsDefDocument {
    private static final long serialVersionUID = 1L;

    public ColorsDefDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "colorsDef"),
    };


    /**
     * Gets the "colorsDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform getColorsDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "colorsDef" element
     */
    @Override
    public void setColorsDef(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform colorsDef) {
        generatedSetterHelperImpl(colorsDef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "colorsDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform addNewColorsDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColorTransform)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

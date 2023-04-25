/*
 * An XML document type.
 * Localname: relIds
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.RelIdsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one relIds(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class RelIdsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.RelIdsDocument {
    private static final long serialVersionUID = 1L;

    public RelIdsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "relIds"),
    };


    /**
     * Gets the "relIds" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds getRelIds() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "relIds" element
     */
    @Override
    public void setRelIds(org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds relIds) {
        generatedSetterHelperImpl(relIds, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "relIds" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds addNewRelIds() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTRelIds)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

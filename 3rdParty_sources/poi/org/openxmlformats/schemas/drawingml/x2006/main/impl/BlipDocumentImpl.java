/*
 * An XML document type.
 * Localname: blip
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.BlipDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one blip(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public class BlipDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.BlipDocument {
    private static final long serialVersionUID = 1L;

    public BlipDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blip"),
    };


    /**
     * Gets the "blip" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlip getBlip() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlip target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlip)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "blip" element
     */
    @Override
    public void setBlip(org.openxmlformats.schemas.drawingml.x2006.main.CTBlip blip) {
        generatedSetterHelperImpl(blip, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "blip" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlip addNewBlip() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlip target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlip)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

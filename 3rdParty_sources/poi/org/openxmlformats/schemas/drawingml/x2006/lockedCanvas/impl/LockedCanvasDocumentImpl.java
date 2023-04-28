/*
 * An XML document type.
 * Localname: lockedCanvas
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/lockedCanvas
 * Java type: org.openxmlformats.schemas.drawingml.x2006.lockedCanvas.LockedCanvasDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.lockedCanvas.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one lockedCanvas(@http://schemas.openxmlformats.org/drawingml/2006/lockedCanvas) element.
 *
 * This is a complex type.
 */
public class LockedCanvasDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.lockedCanvas.LockedCanvasDocument {
    private static final long serialVersionUID = 1L;

    public LockedCanvasDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/lockedCanvas", "lockedCanvas"),
    };


    /**
     * Gets the "lockedCanvas" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape getLockedCanvas() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "lockedCanvas" element
     */
    @Override
    public void setLockedCanvas(org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape lockedCanvas) {
        generatedSetterHelperImpl(lockedCanvas, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lockedCanvas" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape addNewLockedCanvas() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShape)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

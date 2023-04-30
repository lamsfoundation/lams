/*
 * An XML document type.
 * Localname: userShapes
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.UserShapesDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one userShapes(@http://schemas.openxmlformats.org/drawingml/2006/chart) element.
 *
 * This is a complex type.
 */
public class UserShapesDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.UserShapesDocument {
    private static final long serialVersionUID = 1L;

    public UserShapesDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "userShapes"),
    };


    /**
     * Gets the "userShapes" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing getUserShapes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "userShapes" element
     */
    @Override
    public void setUserShapes(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing userShapes) {
        generatedSetterHelperImpl(userShapes, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "userShapes" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing addNewUserShapes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTDrawing)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

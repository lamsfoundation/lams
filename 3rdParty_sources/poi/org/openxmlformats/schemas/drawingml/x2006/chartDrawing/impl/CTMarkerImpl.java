/*
 * XML Type:  CT_Marker
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chartDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chartDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Marker(@http://schemas.openxmlformats.org/drawingml/2006/chartDrawing).
 *
 * This is a complex type.
 */
public class CTMarkerImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker {
    private static final long serialVersionUID = 1L;

    public CTMarkerImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "x"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "y"),
    };


    /**
     * Gets the "x" element
     */
    @Override
    public double getX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? 0.0 : target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "x" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate xgetX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "x" element
     */
    @Override
    public void setX(double x) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setDoubleValue(x);
        }
    }

    /**
     * Sets (as xml) the "x" element
     */
    @Override
    public void xsetX(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate x) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(x);
        }
    }

    /**
     * Gets the "y" element
     */
    @Override
    public double getY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? 0.0 : target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "y" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate xgetY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * Sets the "y" element
     */
    @Override
    public void setY(double y) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setDoubleValue(y);
        }
    }

    /**
     * Sets (as xml) the "y" element
     */
    @Override
    public void xsetY(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate y) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.STMarkerCoordinate)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(y);
        }
    }
}

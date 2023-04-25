/*
 * XML Type:  CT_Marker
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Marker(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public class CTMarkerImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker {
    private static final long serialVersionUID = 1L;

    public CTMarkerImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "col"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "colOff"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "row"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "rowOff"),
    };


    /**
     * Gets the "col" element
     */
    @Override
    public int getCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "col" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID xgetCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * Sets the "col" element
     */
    @Override
    public void setCol(int col) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setIntValue(col);
        }
    }

    /**
     * Sets (as xml) the "col" element
     */
    @Override
    public void xsetCol(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID col) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STColID)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(col);
        }
    }

    /**
     * Gets the "colOff" element
     */
    @Override
    public java.lang.Object getColOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "colOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetColOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * Sets the "colOff" element
     */
    @Override
    public void setColOff(java.lang.Object colOff) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(colOff);
        }
    }

    /**
     * Sets (as xml) the "colOff" element
     */
    @Override
    public void xsetColOff(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate colOff) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(colOff);
        }
    }

    /**
     * Gets the "row" element
     */
    @Override
    public int getRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "row" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID xgetRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return target;
        }
    }

    /**
     * Sets the "row" element
     */
    @Override
    public void setRow(int row) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.setIntValue(row);
        }
    }

    /**
     * Sets (as xml) the "row" element
     */
    @Override
    public void xsetRow(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID row) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STRowID)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.set(row);
        }
    }

    /**
     * Gets the "rowOff" element
     */
    @Override
    public java.lang.Object getRowOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "rowOff" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate xgetRowOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return target;
        }
    }

    /**
     * Sets the "rowOff" element
     */
    @Override
    public void setRowOff(java.lang.Object rowOff) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(rowOff);
        }
    }

    /**
     * Sets (as xml) the "rowOff" element
     */
    @Override
    public void xsetRowOff(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate rowOff) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.set(rowOff);
        }
    }
}

/*
 * XML Type:  CT_ObjectAnchor
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ObjectAnchor(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTObjectAnchorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor {
    private static final long serialVersionUID = 1L;

    public CTObjectAnchorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "from"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "to"),
        new QName("", "moveWithCells"),
        new QName("", "sizeWithCells"),
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

    /**
     * Gets the "to" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker getTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "to" element
     */
    @Override
    public void setTo(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker to) {
        generatedSetterHelperImpl(to, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "to" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker addNewTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "moveWithCells" attribute
     */
    @Override
    public boolean getMoveWithCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "moveWithCells" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMoveWithCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "moveWithCells" attribute
     */
    @Override
    public boolean isSetMoveWithCells() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "moveWithCells" attribute
     */
    @Override
    public void setMoveWithCells(boolean moveWithCells) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(moveWithCells);
        }
    }

    /**
     * Sets (as xml) the "moveWithCells" attribute
     */
    @Override
    public void xsetMoveWithCells(org.apache.xmlbeans.XmlBoolean moveWithCells) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(moveWithCells);
        }
    }

    /**
     * Unsets the "moveWithCells" attribute
     */
    @Override
    public void unsetMoveWithCells() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "sizeWithCells" attribute
     */
    @Override
    public boolean getSizeWithCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "sizeWithCells" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSizeWithCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "sizeWithCells" attribute
     */
    @Override
    public boolean isSetSizeWithCells() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "sizeWithCells" attribute
     */
    @Override
    public void setSizeWithCells(boolean sizeWithCells) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setBooleanValue(sizeWithCells);
        }
    }

    /**
     * Sets (as xml) the "sizeWithCells" attribute
     */
    @Override
    public void xsetSizeWithCells(org.apache.xmlbeans.XmlBoolean sizeWithCells) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(sizeWithCells);
        }
    }

    /**
     * Unsets the "sizeWithCells" attribute
     */
    @Override
    public void unsetSizeWithCells() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}

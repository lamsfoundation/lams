/*
 * XML Type:  CT_PositiveSize2D
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PositiveSize2D(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPositiveSize2DImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D {
    private static final long serialVersionUID = 1L;

    public CTPositiveSize2DImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "cx"),
        new QName("", "cy"),
    };


    /**
     * Gets the "cx" attribute
     */
    @Override
    public long getCx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "cx" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetCx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "cx" attribute
     */
    @Override
    public void setCx(long cx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setLongValue(cx);
        }
    }

    /**
     * Sets (as xml) the "cx" attribute
     */
    @Override
    public void xsetCx(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate cx) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(cx);
        }
    }

    /**
     * Gets the "cy" attribute
     */
    @Override
    public long getCy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "cy" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate xgetCy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "cy" attribute
     */
    @Override
    public void setCy(long cy) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(cy);
        }
    }

    /**
     * Sets (as xml) the "cy" attribute
     */
    @Override
    public void xsetCy(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate cy) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(cy);
        }
    }
}

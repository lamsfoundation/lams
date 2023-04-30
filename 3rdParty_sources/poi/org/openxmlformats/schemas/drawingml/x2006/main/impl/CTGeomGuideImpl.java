/*
 * XML Type:  CT_GeomGuide
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GeomGuide(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGeomGuideImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide {
    private static final long serialVersionUID = 1L;

    public CTGeomGuideImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "name"),
        new QName("", "fmla"),
    };


    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName name) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideName)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(name);
        }
    }

    /**
     * Gets the "fmla" attribute
     */
    @Override
    public java.lang.String getFmla() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "fmla" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula xgetFmla() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "fmla" attribute
     */
    @Override
    public void setFmla(java.lang.String fmla) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(fmla);
        }
    }

    /**
     * Sets (as xml) the "fmla" attribute
     */
    @Override
    public void xsetFmla(org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula fmla) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STGeomGuideFormula)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(fmla);
        }
    }
}

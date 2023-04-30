/*
 * XML Type:  CT_TextSpacing
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextSpacing(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextSpacingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing {
    private static final long serialVersionUID = 1L;

    public CTTextSpacingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "spcPct"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "spcPts"),
    };


    /**
     * Gets the "spcPct" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent getSpcPct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spcPct" element
     */
    @Override
    public boolean isSetSpcPct() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "spcPct" element
     */
    @Override
    public void setSpcPct(org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent spcPct) {
        generatedSetterHelperImpl(spcPct, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spcPct" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent addNewSpcPct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "spcPct" element
     */
    @Override
    public void unsetSpcPct() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "spcPts" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint getSpcPts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spcPts" element
     */
    @Override
    public boolean isSetSpcPts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "spcPts" element
     */
    @Override
    public void setSpcPts(org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint spcPts) {
        generatedSetterHelperImpl(spcPts, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spcPts" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint addNewSpcPts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "spcPts" element
     */
    @Override
    public void unsetSpcPts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}

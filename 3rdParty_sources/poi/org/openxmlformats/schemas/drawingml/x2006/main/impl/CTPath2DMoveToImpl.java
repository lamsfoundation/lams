/*
 * XML Type:  CT_Path2DMoveTo
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Path2DMoveTo(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPath2DMoveToImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DMoveTo {
    private static final long serialVersionUID = 1L;

    public CTPath2DMoveToImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "pt"),
    };


    /**
     * Gets the "pt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D getPt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "pt" element
     */
    @Override
    public void setPt(org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D pt) {
        generatedSetterHelperImpl(pt, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D addNewPt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAdjPoint2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

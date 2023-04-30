/*
 * XML Type:  CT_GvmlGroupShapeNonVisual
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GvmlGroupShapeNonVisual(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGvmlGroupShapeNonVisualImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGvmlGroupShapeNonVisual {
    private static final long serialVersionUID = 1L;

    public CTGvmlGroupShapeNonVisualImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cNvPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "cNvGrpSpPr"),
    };


    /**
     * Gets the "cNvPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps getCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cNvPr" element
     */
    @Override
    public void setCNvPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps cNvPr) {
        generatedSetterHelperImpl(cNvPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps addNewCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "cNvGrpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps getCNvGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cNvGrpSpPr" element
     */
    @Override
    public void setCNvGrpSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps cNvGrpSpPr) {
        generatedSetterHelperImpl(cNvGrpSpPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvGrpSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps addNewCNvGrpSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGroupDrawingShapeProps)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }
}

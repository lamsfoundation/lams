/*
 * XML Type:  CT_ConnectorNonVisual
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ConnectorNonVisual(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTConnectorNonVisualImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTConnectorNonVisual {
    private static final long serialVersionUID = 1L;

    public CTConnectorNonVisualImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cNvPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cNvCxnSpPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "nvPr"),
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
     * Gets the "cNvCxnSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties getCNvCxnSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cNvCxnSpPr" element
     */
    @Override
    public void setCNvCxnSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties cNvCxnSpPr) {
        generatedSetterHelperImpl(cNvCxnSpPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvCxnSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties addNewCNvCxnSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "nvPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps getNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "nvPr" element
     */
    @Override
    public void setNvPr(org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps nvPr) {
        generatedSetterHelperImpl(nvPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "nvPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps addNewNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTApplicationNonVisualDrawingProps)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }
}

/*
 * An XML document type.
 * Localname: styleDef
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one styleDef(@http://schemas.openxmlformats.org/drawingml/2006/diagram) element.
 *
 * This is a complex type.
 */
public class StyleDefDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.StyleDefDocument {
    private static final long serialVersionUID = 1L;

    public StyleDefDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "styleDef"),
    };


    /**
     * Gets the "styleDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition getStyleDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "styleDef" element
     */
    @Override
    public void setStyleDef(org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition styleDef) {
        generatedSetterHelperImpl(styleDef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styleDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition addNewStyleDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTStyleDefinition)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

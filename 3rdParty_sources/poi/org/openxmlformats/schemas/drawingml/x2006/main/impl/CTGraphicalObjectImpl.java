/*
 * XML Type:  CT_GraphicalObject
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GraphicalObject(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTGraphicalObjectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject {
    private static final long serialVersionUID = 1L;

    public CTGraphicalObjectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "graphicData"),
    };


    /**
     * Gets the "graphicData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData getGraphicData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "graphicData" element
     */
    @Override
    public void setGraphicData(org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData graphicData) {
        generatedSetterHelperImpl(graphicData, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "graphicData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData addNewGraphicData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObjectData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

/*
 * An XML document type.
 * Localname: pivotTableDefinition
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotTableDefinitionDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one pivotTableDefinition(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class PivotTableDefinitionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotTableDefinitionDocument {
    private static final long serialVersionUID = 1L;

    public PivotTableDefinitionDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotTableDefinition"),
    };


    /**
     * Gets the "pivotTableDefinition" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition getPivotTableDefinition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "pivotTableDefinition" element
     */
    @Override
    public void setPivotTableDefinition(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition pivotTableDefinition) {
        generatedSetterHelperImpl(pivotTableDefinition, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotTableDefinition" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition addNewPivotTableDefinition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

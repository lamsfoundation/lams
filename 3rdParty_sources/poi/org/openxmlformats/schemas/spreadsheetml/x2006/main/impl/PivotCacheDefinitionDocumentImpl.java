/*
 * An XML document type.
 * Localname: pivotCacheDefinition
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotCacheDefinitionDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one pivotCacheDefinition(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class PivotCacheDefinitionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotCacheDefinitionDocument {
    private static final long serialVersionUID = 1L;

    public PivotCacheDefinitionDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotCacheDefinition"),
    };


    /**
     * Gets the "pivotCacheDefinition" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition getPivotCacheDefinition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "pivotCacheDefinition" element
     */
    @Override
    public void setPivotCacheDefinition(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition pivotCacheDefinition) {
        generatedSetterHelperImpl(pivotCacheDefinition, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotCacheDefinition" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition addNewPivotCacheDefinition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheDefinition)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

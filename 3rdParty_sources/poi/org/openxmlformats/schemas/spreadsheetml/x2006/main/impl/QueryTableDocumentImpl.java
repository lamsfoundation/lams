/*
 * An XML document type.
 * Localname: queryTable
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.QueryTableDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one queryTable(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class QueryTableDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.QueryTableDocument {
    private static final long serialVersionUID = 1L;

    public QueryTableDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "queryTable"),
    };


    /**
     * Gets the "queryTable" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable getQueryTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "queryTable" element
     */
    @Override
    public void setQueryTable(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable queryTable) {
        generatedSetterHelperImpl(queryTable, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "queryTable" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable addNewQueryTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQueryTable)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

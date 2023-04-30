/*
 * An XML document type.
 * Localname: pivotCacheRecords
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotCacheRecordsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one pivotCacheRecords(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class PivotCacheRecordsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.PivotCacheRecordsDocument {
    private static final long serialVersionUID = 1L;

    public PivotCacheRecordsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pivotCacheRecords"),
    };


    /**
     * Gets the "pivotCacheRecords" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords getPivotCacheRecords() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "pivotCacheRecords" element
     */
    @Override
    public void setPivotCacheRecords(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords pivotCacheRecords) {
        generatedSetterHelperImpl(pivotCacheRecords, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotCacheRecords" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords addNewPivotCacheRecords() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotCacheRecords)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

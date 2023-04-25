/*
 * An XML document type.
 * Localname: workbook
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one workbook(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class WorkbookDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument {
    private static final long serialVersionUID = 1L;

    public WorkbookDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "workbook"),
    };


    /**
     * Gets the "workbook" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook getWorkbook() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "workbook" element
     */
    @Override
    public void setWorkbook(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook workbook) {
        generatedSetterHelperImpl(workbook, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "workbook" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook addNewWorkbook() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

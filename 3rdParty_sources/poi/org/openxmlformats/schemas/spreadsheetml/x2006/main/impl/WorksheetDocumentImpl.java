/*
 * An XML document type.
 * Localname: worksheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one worksheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class WorksheetDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.WorksheetDocument {
    private static final long serialVersionUID = 1L;

    public WorksheetDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "worksheet"),
    };


    /**
     * Gets the "worksheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet getWorksheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "worksheet" element
     */
    @Override
    public void setWorksheet(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet worksheet) {
        generatedSetterHelperImpl(worksheet, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "worksheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet addNewWorksheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

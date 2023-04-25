/*
 * An XML document type.
 * Localname: dialogsheet
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.DialogsheetDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one dialogsheet(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class DialogsheetDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.DialogsheetDocument {
    private static final long serialVersionUID = 1L;

    public DialogsheetDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "dialogsheet"),
    };


    /**
     * Gets the "dialogsheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet getDialogsheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "dialogsheet" element
     */
    @Override
    public void setDialogsheet(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet dialogsheet) {
        generatedSetterHelperImpl(dialogsheet, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dialogsheet" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet addNewDialogsheet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDialogsheet)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

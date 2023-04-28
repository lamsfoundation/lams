/*
 * An XML document type.
 * Localname: table
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.TableDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one table(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class TableDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.TableDocument {
    private static final long serialVersionUID = 1L;

    public TableDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "table"),
    };


    /**
     * Gets the "table" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable getTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "table" element
     */
    @Override
    public void setTable(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable table) {
        generatedSetterHelperImpl(table, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "table" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable addNewTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

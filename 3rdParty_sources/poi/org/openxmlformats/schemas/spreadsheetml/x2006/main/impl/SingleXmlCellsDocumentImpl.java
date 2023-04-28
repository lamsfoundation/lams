/*
 * An XML document type.
 * Localname: singleXmlCells
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one singleXmlCells(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class SingleXmlCellsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.SingleXmlCellsDocument {
    private static final long serialVersionUID = 1L;

    public SingleXmlCellsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "singleXmlCells"),
    };


    /**
     * Gets the "singleXmlCells" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells getSingleXmlCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "singleXmlCells" element
     */
    @Override
    public void setSingleXmlCells(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells singleXmlCells) {
        generatedSetterHelperImpl(singleXmlCells, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "singleXmlCells" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells addNewSingleXmlCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSingleXmlCells)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

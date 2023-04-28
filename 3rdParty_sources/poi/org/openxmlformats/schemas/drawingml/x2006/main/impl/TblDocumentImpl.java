/*
 * An XML document type.
 * Localname: tbl
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.TblDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one tbl(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public class TblDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.TblDocument {
    private static final long serialVersionUID = 1L;

    public TblDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tbl"),
    };


    /**
     * Gets the "tbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTable getTbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTable target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTable)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tbl" element
     */
    @Override
    public void setTbl(org.openxmlformats.schemas.drawingml.x2006.main.CTTable tbl) {
        generatedSetterHelperImpl(tbl, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tbl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTable addNewTbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTable target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTable)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

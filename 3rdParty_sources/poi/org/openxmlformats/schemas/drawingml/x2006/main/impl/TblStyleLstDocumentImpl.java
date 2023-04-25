/*
 * An XML document type.
 * Localname: tblStyleLst
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.TblStyleLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one tblStyleLst(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public class TblStyleLstDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.TblStyleLstDocument {
    private static final long serialVersionUID = 1L;

    public TblStyleLstDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tblStyleLst"),
    };


    /**
     * Gets the "tblStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList getTblStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tblStyleLst" element
     */
    @Override
    public void setTblStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList tblStyleLst) {
        generatedSetterHelperImpl(tblStyleLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblStyleLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList addNewTblStyleLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

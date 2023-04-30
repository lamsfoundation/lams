/*
 * XML Type:  CT_TblPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblPrChangeImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange {
    private static final long serialVersionUID = 1L;

    public CTTblPrChangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblPr"),
    };


    /**
     * Gets the "tblPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase getTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tblPr" element
     */
    @Override
    public void setTblPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase tblPr) {
        generatedSetterHelperImpl(tblPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase addNewTblPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

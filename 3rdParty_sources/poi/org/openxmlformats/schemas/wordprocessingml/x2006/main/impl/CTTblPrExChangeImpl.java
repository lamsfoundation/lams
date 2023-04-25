/*
 * XML Type:  CT_TblPrExChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblPrExChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblPrExChangeImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange {
    private static final long serialVersionUID = 1L;

    public CTTblPrExChangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblPrEx"),
    };


    /**
     * Gets the "tblPrEx" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase getTblPrEx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tblPrEx" element
     */
    @Override
    public void setTblPrEx(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase tblPrEx) {
        generatedSetterHelperImpl(tblPrEx, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblPrEx" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase addNewTblPrEx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExBase)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

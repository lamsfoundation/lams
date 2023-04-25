/*
 * XML Type:  CT_TblPrEx
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrEx
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblPrEx(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblPrExImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTblPrExBaseImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrEx {
    private static final long serialVersionUID = 1L;

    public CTTblPrExImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblPrExChange"),
    };


    /**
     * Gets the "tblPrExChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange getTblPrExChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblPrExChange" element
     */
    @Override
    public boolean isSetTblPrExChange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tblPrExChange" element
     */
    @Override
    public void setTblPrExChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange tblPrExChange) {
        generatedSetterHelperImpl(tblPrExChange, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblPrExChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange addNewTblPrExChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrExChange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tblPrExChange" element
     */
    @Override
    public void unsetTblPrExChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}

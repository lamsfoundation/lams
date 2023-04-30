/*
 * XML Type:  CT_TblPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblPrImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTblPrBaseImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr {
    private static final long serialVersionUID = 1L;

    public CTTblPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblPrChange"),
    };


    /**
     * Gets the "tblPrChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange getTblPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblPrChange" element
     */
    @Override
    public boolean isSetTblPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tblPrChange" element
     */
    @Override
    public void setTblPrChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange tblPrChange) {
        generatedSetterHelperImpl(tblPrChange, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblPrChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange addNewTblPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrChange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tblPrChange" element
     */
    @Override
    public void unsetTblPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}

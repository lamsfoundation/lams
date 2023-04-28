/*
 * XML Type:  CT_TblGrid
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblGrid(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblGridImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTblGridBaseImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid {
    private static final long serialVersionUID = 1L;

    public CTTblGridImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblGridChange"),
    };


    /**
     * Gets the "tblGridChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange getTblGridChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tblGridChange" element
     */
    @Override
    public boolean isSetTblGridChange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tblGridChange" element
     */
    @Override
    public void setTblGridChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange tblGridChange) {
        generatedSetterHelperImpl(tblGridChange, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblGridChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange addNewTblGridChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tblGridChange" element
     */
    @Override
    public void unsetTblGridChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}

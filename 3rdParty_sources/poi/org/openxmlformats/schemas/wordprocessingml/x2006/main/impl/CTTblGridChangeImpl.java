/*
 * XML Type:  CT_TblGridChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TblGridChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTblGridChangeImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTMarkupImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridChange {
    private static final long serialVersionUID = 1L;

    public CTTblGridChangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tblGrid"),
    };


    /**
     * Gets the "tblGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase getTblGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tblGrid" element
     */
    @Override
    public void setTblGrid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase tblGrid) {
        generatedSetterHelperImpl(tblGrid, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tblGrid" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase addNewTblGrid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridBase)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

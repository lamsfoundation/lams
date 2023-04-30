/*
 * XML Type:  CT_MC
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTMC
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MC(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public class CTMCImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.math.CTMC {
    private static final long serialVersionUID = 1L;

    public CTMCImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "mcPr"),
    };


    /**
     * Gets the "mcPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr getMcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mcPr" element
     */
    @Override
    public boolean isSetMcPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "mcPr" element
     */
    @Override
    public void setMcPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr mcPr) {
        generatedSetterHelperImpl(mcPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mcPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr addNewMcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "mcPr" element
     */
    @Override
    public void unsetMcPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}

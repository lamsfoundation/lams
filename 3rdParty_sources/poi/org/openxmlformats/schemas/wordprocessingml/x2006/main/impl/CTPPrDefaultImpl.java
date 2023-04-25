/*
 * XML Type:  CT_PPrDefault
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PPrDefault(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPPrDefaultImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault {
    private static final long serialVersionUID = 1L;

    public CTPPrDefaultImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pPr"),
    };


    /**
     * Gets the "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral getPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pPr" element
     */
    @Override
    public boolean isSetPPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pPr" element
     */
    @Override
    public void setPPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral pPr) {
        generatedSetterHelperImpl(pPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral addNewPPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "pPr" element
     */
    @Override
    public void unsetPPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}

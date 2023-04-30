/*
 * XML Type:  CT_PPrGeneral
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PPrGeneral(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPPrGeneralImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTPPrBaseImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral {
    private static final long serialVersionUID = 1L;

    public CTPPrGeneralImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pPrChange"),
    };


    /**
     * Gets the "pPrChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange getPPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pPrChange" element
     */
    @Override
    public boolean isSetPPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pPrChange" element
     */
    @Override
    public void setPPrChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange pPrChange) {
        generatedSetterHelperImpl(pPrChange, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pPrChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange addNewPPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "pPrChange" element
     */
    @Override
    public void unsetPPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}

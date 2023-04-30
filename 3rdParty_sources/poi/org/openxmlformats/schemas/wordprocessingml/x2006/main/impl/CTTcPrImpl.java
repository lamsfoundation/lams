/*
 * XML Type:  CT_TcPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TcPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTcPrImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTcPrInnerImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr {
    private static final long serialVersionUID = 1L;

    public CTTcPrImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tcPrChange"),
    };


    /**
     * Gets the "tcPrChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange getTcPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tcPrChange" element
     */
    @Override
    public boolean isSetTcPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tcPrChange" element
     */
    @Override
    public void setTcPrChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange tcPrChange) {
        generatedSetterHelperImpl(tcPrChange, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcPrChange" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange addNewTcPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tcPrChange" element
     */
    @Override
    public void unsetTcPrChange() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }
}

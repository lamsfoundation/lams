/*
 * XML Type:  CT_TcPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TcPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTcPrChangeImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange {
    private static final long serialVersionUID = 1L;

    public CTTcPrChangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "tcPr"),
    };


    /**
     * Gets the "tcPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner getTcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tcPr" element
     */
    @Override
    public void setTcPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner tcPr) {
        generatedSetterHelperImpl(tcPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tcPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner addNewTcPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

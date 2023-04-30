/*
 * XML Type:  CT_ParaRPrChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ParaRPrChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTParaRPrChangeImpl extends org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTTrackChangeImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrChange {
    private static final long serialVersionUID = 1L;

    public CTParaRPrChangeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rPr"),
    };


    /**
     * Gets the "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal getRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "rPr" element
     */
    @Override
    public void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal rPr) {
        generatedSetterHelperImpl(rPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal addNewRPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrOriginal)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

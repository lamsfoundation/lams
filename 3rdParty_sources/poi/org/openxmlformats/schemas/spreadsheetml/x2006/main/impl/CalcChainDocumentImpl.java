/*
 * An XML document type.
 * Localname: calcChain
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one calcChain(@http://schemas.openxmlformats.org/spreadsheetml/2006/main) element.
 *
 * This is a complex type.
 */
public class CalcChainDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CalcChainDocument {
    private static final long serialVersionUID = 1L;

    public CalcChainDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "calcChain"),
    };


    /**
     * Gets the "calcChain" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain getCalcChain() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "calcChain" element
     */
    @Override
    public void setCalcChain(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain calcChain) {
        generatedSetterHelperImpl(calcChain, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "calcChain" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain addNewCalcChain() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCalcChain)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

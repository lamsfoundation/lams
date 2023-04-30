/*
 * An XML document type.
 * Localname: txbxContent
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.TxbxContentDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one txbxContent(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public class TxbxContentDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.TxbxContentDocument {
    private static final long serialVersionUID = 1L;

    public TxbxContentDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "txbxContent"),
    };


    /**
     * Gets the "txbxContent" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent getTxbxContent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "txbxContent" element
     */
    @Override
    public void setTxbxContent(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent txbxContent) {
        generatedSetterHelperImpl(txbxContent, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txbxContent" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent addNewTxbxContent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

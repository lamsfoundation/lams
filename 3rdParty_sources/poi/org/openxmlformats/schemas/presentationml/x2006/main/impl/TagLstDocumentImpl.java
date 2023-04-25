/*
 * An XML document type.
 * Localname: tagLst
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.TagLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one tagLst(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public class TagLstDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.TagLstDocument {
    private static final long serialVersionUID = 1L;

    public TagLstDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tagLst"),
    };


    /**
     * Gets the "tagLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTagList getTagLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTagList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTagList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tagLst" element
     */
    @Override
    public void setTagLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTagList tagLst) {
        generatedSetterHelperImpl(tagLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tagLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTagList addNewTagLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTagList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTagList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

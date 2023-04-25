/*
 * An XML document type.
 * Localname: pic
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/picture
 * Java type: org.openxmlformats.schemas.drawingml.x2006.picture.PicDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.picture.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one pic(@http://schemas.openxmlformats.org/drawingml/2006/picture) element.
 *
 * This is a complex type.
 */
public class PicDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.picture.PicDocument {
    private static final long serialVersionUID = 1L;

    public PicDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/picture", "pic"),
    };


    /**
     * Gets the "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture getPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "pic" element
     */
    @Override
    public void setPic(org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture pic) {
        generatedSetterHelperImpl(pic, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture addNewPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}

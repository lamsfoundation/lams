/*
 * An XML document type.
 * Localname: pic
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/picture
 * Java type: org.openxmlformats.schemas.drawingml.x2006.picture.PicDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.picture;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one pic(@http://schemas.openxmlformats.org/drawingml/2006/picture) element.
 *
 * This is a complex type.
 */
public interface PicDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.picture.PicDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "pic8010doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture getPic();

    /**
     * Sets the "pic" element
     */
    void setPic(org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture pic);

    /**
     * Appends and returns a new empty "pic" element
     */
    org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture addNewPic();
}

/*
 * An XML document type.
 * Localname: tagLst
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.TagLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one tagLst(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface TagLstDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.TagLstDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "taglst05dedoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tagLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTagList getTagLst();

    /**
     * Sets the "tagLst" element
     */
    void setTagLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTagList tagLst);

    /**
     * Appends and returns a new empty "tagLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTagList addNewTagLst();
}

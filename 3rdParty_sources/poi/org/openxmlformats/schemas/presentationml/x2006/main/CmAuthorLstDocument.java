/*
 * An XML document type.
 * Localname: cmAuthorLst
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CmAuthorLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one cmAuthorLst(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface CmAuthorLstDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CmAuthorLstDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cmauthorlst86abdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cmAuthorLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList getCmAuthorLst();

    /**
     * Sets the "cmAuthorLst" element
     */
    void setCmAuthorLst(org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList cmAuthorLst);

    /**
     * Appends and returns a new empty "cmAuthorLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthorList addNewCmAuthorLst();
}
